package com.sparkx.util;

import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class Util {

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static void checkPass(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword))
            System.out.println("The password matches.");
        else
            System.out.println("The password does not match.");
    }

    public static UUID getUUID() {
        return UUID.randomUUID();
    }
}
