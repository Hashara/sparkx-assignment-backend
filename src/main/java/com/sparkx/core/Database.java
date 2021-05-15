package com.sparkx.core;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost/SparkX";
    private static BasicDataSource connectionPool;

    private Database(){}

    static {
        connectionPool = new BasicDataSource();
        connectionPool.setDriverClassName("org.postgresql.Driver");
        connectionPool.setUrl(URL);
        connectionPool.setUsername("postgres");
        connectionPool.setPassword("19970923");
        connectionPool.setMinIdle(5);
        connectionPool.setMaxIdle(10);
        connectionPool.setMaxOpenPreparedStatements(100);
    }
    public static Connection getConnection() throws SQLException
    {
        return connectionPool.getConnection();
    }

}
