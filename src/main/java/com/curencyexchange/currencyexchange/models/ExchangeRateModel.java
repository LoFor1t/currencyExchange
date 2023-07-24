package com.curencyexchange.currencyexchange.models;

import com.curencyexchange.currencyexchange.Utils.DBConnection;
import com.curencyexchange.currencyexchange.dataClasses.Currency;
import com.curencyexchange.currencyexchange.dataClasses.ExchangeRate;
import com.curencyexchange.currencyexchange.exceptions.nonExistentCurrencyException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateModel {
    public static List<ExchangeRate> getExchangeRates() throws SQLException {
        try (Session session = DBConnection.getSessionFactory().openSession()) {
            Query<ExchangeRate> query = session.createQuery("from ExchangeRate", ExchangeRate.class);
            return query.list();
        }
    }

    public static ExchangeRate getExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, nonExistentCurrencyException {
        Currency baseCurrency = CurrencyModel.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = CurrencyModel.getCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new nonExistentCurrencyException();
        }

        try (Session session = DBConnection.getSessionFactory().openSession()) {
            final String hql = "FROM ExchangeRate WHERE baseCurrency.id = :basecurrencyid AND targetCurrency.id = :targetcurrencyid";
            Query<ExchangeRate> query = session.createQuery(hql, ExchangeRate.class);
            query.setParameter("basecurrencyid", baseCurrency.id);
            query.setParameter("targetcurrencyid", targetCurrency.id);
            List<ExchangeRate> exchangeRate = query.list();
            if (exchangeRate.size() != 0) {
                return exchangeRate.get(0);
            } else {
                return null;
            }
        }
    }

    public static void createNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws SQLException, nonExistentCurrencyException {
        Currency baseCurrency = CurrencyModel.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = CurrencyModel.getCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new nonExistentCurrencyException();
        }

        ExchangeRate newExchangeRate = new ExchangeRate(baseCurrency, targetCurrency, rate);

        try (Session session = DBConnection.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(newExchangeRate);
            transaction.commit();
        }
    }

    public static void updateRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal newRate) throws SQLException, nonExistentCurrencyException {
        Currency baseCurrency = CurrencyModel.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = CurrencyModel.getCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new nonExistentCurrencyException();
        }
        ExchangeRate exchangeRate = getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate == null) {
            throw new nonExistentCurrencyException();
        }
        exchangeRate.rate = newRate;
        try (Session session = DBConnection.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(exchangeRate);
            transaction.commit();
        }
    }
}
