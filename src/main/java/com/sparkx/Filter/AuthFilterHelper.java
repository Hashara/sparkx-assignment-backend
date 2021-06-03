package com.sparkx.Filter;

import com.sparkx.model.Person;
import com.sparkx.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class AuthFilterHelper {

    public static Person authUser(HttpServletRequest req, HttpServletResponse res) {

        Person person = null;

        String authHeader = req.getHeader("Authorization");
        if (authHeader != null) {

            String[] authHeaderSplit = authHeader.split("\\s");

            for (int i = 0; i < authHeaderSplit.length; i++) {
                String token = authHeaderSplit[i];
                if (token.equalsIgnoreCase("Basic")) {

                    String credentials = new String(Base64.getDecoder().decode(authHeaderSplit[i + 1]));
                    int index = credentials.indexOf(":");
                    if (index != -1) {
                        String username = credentials.substring(0, index).trim();
                        String password = credentials.substring(index + 1).trim();
                        person = new PersonService().authenticate(username, password);
                    }
                }
            }
        }
        return person;
    }
}
