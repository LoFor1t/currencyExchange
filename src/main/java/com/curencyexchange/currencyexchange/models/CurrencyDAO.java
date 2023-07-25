package com.curencyexchange.currencyexchange.models;

import com.curencyexchange.currencyexchange.Utils.DBConnection;
import com.curencyexchange.currencyexchange.dataClasses.Currency;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;

public class CurrencyDAO {
    public static List<Currency> getCurrencies () throws SQLException {
        try (Session session = DBConnection.getSessionFactory().openSession()) {
            Query<Currency> query = session.createQuery("from Currency", Currency.class);
            return query.list();
        }
    }

    public static Currency getCurrencyByCode(String code) throws SQLException {
        try (Session session = DBConnection.getSessionFactory().openSession()) {
            final String hql = "FROM Currency WHERE code = :code";
            Query<Currency> query = session.createQuery(hql, Currency.class);
            query.setParameter("code", code);
            List<Currency> currency = query.list();
            if (currency.size() != 0) {
                return currency.get(0);
            } else {
                return null;
            }
        }
    }

    public static Currency getCurrencyByID(int id) throws SQLException {
        try (Session session = DBConnection.getSessionFactory().openSession()) {
            Currency currency = session.get(Currency.class, id);
            return currency;
        }
    }

    public static void createNewCurrency(String name, String code, String sign) throws SQLException {
        Currency newCurrency = new Currency(name, code, sign);

        try (Session session = DBConnection.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(newCurrency);
            transaction.commit();
        }
    }
}
