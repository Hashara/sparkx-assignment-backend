package com.sparkx.util;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Date;
import java.util.UUID;

public class Util {

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static UUID getUUID() {
        return UUID.randomUUID();
    }

    public static Date getDate() {
        long millis = System.currentTimeMillis();
        return new Date(millis);
    }
}
