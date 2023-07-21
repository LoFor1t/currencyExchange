package com.curencyexchange.currencyexchange;

import java.sql.*;

public class DBConnection {
    private static Connection connection;

    public static Connection getDBConnection () throws SQLException {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException();
            }
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/currencyExchange",
                    "postgres", "password");
        }
        return connection;
    }
}
