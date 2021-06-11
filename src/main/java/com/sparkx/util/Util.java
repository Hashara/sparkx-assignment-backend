package com.sparkx.util;

import org.mindrot.jbcrypt.BCrypt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
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

    public static String getPropValues(String value) throws IOException {

        String result = "";
        String propFileName = "config.properties";

        try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream(propFileName);) {
            Properties prop = new Properties();


            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            result = prop.getProperty(value);
            ;


        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return result;
    }
}
