package com.sparkx.service;

import com.google.gson.Gson;
import com.sparkx.Exception.UnauthorizedException;
import com.sparkx.core.Database;
import com.sparkx.model.Person;
import com.sparkx.model.dao.AuthDAO;
import com.sparkx.util.Message;
import com.sparkx.util.Query;
import com.sparkx.util.Util;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

public class AuthService {
    public static String createJWT(String issuer, Person person, long ttlMillis) throws IOException {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Util.getPropValues("SECRET_KEY"));
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(person.getUserId().toString())
                .setIssuedAt(now)
                .setIssuer(issuer)
                .claim("role", person.getRole())
                .claim("name", person.getFirst_name() + " " + person.getLast_name())
                .signWith(signatureAlgorithm, signingKey);

        if (person.getHospitalId() != null) {
            builder.claim("hospitalId", person.getHospitalId().toString());
        }

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) throws IOException, MalformedJwtException, ExpiredJwtException {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims;
        claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(Util.getPropValues("SECRET_KEY")))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public AuthDAO authenticate(String email, String password) throws IOException, UnauthorizedException {
        Person authenticatedUser = new PersonService().getAuthenticatedUser(email, password);
        if (authenticatedUser == null) {
            throw new UnauthorizedException(Message.INVALID_CREDENTIALS);
        }
        String jwt = AuthService.createJWT("NCMS", authenticatedUser, 30000000);
        AuthDAO authDAO = new AuthDAO();
        authDAO.setJwt(jwt);
        authDAO.setPerson(authenticatedUser);

        return authDAO;
    }


}
