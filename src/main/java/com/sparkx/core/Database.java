package com.sparkx.core;

import com.sparkx.config.DBConfig;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private static BasicDataSource connectionPool;

    private Database(){}

    static {
        connectionPool = new BasicDataSource();
        connectionPool.setDriverClassName("org.postgresql.Driver");
        connectionPool.setUrl(DBConfig.DB_URL);
        connectionPool.setUsername(DBConfig.USERNAME);
        connectionPool.setPassword(DBConfig.PASSWORD);
        connectionPool.setMinIdle(5);
        connectionPool.setMaxIdle(10);
        connectionPool.setMaxOpenPreparedStatements(100);
    }
    public static Connection getConnection() throws SQLException
    {
        return connectionPool.getConnection();
    }

}
