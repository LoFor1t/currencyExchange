package com.curencyexchange.currencyexchange.Utils;

import com.curencyexchange.currencyexchange.dataClasses.Currency;
import com.curencyexchange.currencyexchange.dataClasses.ExchangeRate;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;

public class DBConnection {
    private static Connection connection;
    private static SessionFactory sessionFactory;


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

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Currency.class)
                    .addAnnotatedClass(ExchangeRate.class)
                    .buildSessionFactory();
        }

        return sessionFactory;
    }
}
