package com.sparkx.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private static Connection dbConnection;
    private static final String URL = "jdbc:postgresql://localhost/SparkX";

    private Database(){}

    public static synchronized Connection getDbConnection(){
        Properties props = new Properties();

        props.setProperty("user", "postgres");
        props.setProperty("password", "");
        if (dbConnection == null){
            try {
                dbConnection = DriverManager.getConnection(URL, props);
                System.out.println("Connected to the PostgreSQL server successfully.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return dbConnection;
    }


}
