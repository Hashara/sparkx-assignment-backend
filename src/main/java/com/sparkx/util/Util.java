package com.sparkx.util;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static java.sql.Date getDate() {
        long millis = System.currentTimeMillis();
        return new java.sql.Date(millis);
    }

    public static Date covertStringToDate(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date utilDate = sdf.parse(strDate);
            java.sql.Date date = new java.sql.Date(utilDate.getTime());
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
