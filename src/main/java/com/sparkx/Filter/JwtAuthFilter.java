package com.sparkx.Filter;

import com.sparkx.service.AuthService;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "JWTFilter")
public class JwtAuthFilter implements Filter {
    private static final Logger logger = Logger.getLogger(JwtAuthFilter.class.getName());

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer "; // with trailing space to separate token

    private static final int STATUS_CODE_UNAUTHORIZED = 401;
    private AuthService authService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        authService = new AuthService();
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        try {

            String jwt = getBearerToken(req);

            if (jwt != null && !jwt.isEmpty()) {

                Claims claims = AuthService.decodeJWT(jwt);
                req.setAttribute("role", claims.get("role"));
                req.setAttribute("hospitalId", claims.get("hospitalId"));
                req.setAttribute("userId", claims.getId());
                filterChain.doFilter(req, res);
                logger.info("Logged in using JWT");
            } else {
                logger.info("Failed logging in with security token");
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setContentLength(0);
                httpResponse.setStatus(STATUS_CODE_UNAUTHORIZED);
            }

        } catch (final Exception e) {
            logger.info("Failed logging in with security token", e);
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setContentLength(0);
            httpResponse.setStatus(STATUS_CODE_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
        logger.info("JwtAuthenticationFilter destroyed");
    }

    private String getBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
            return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
        }
        return null;
    }
}
