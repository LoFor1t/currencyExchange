package com.curencyexchange.currencyexchange.models;

import com.curencyexchange.currencyexchange.Utils.DBConnection;
import com.curencyexchange.currencyexchange.dataClasses.Currency;
import com.curencyexchange.currencyexchange.dataClasses.ExchangeRate;
import com.curencyexchange.currencyexchange.exceptions.nonExistentCurrencyException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateModel {
    private static ExchangeRate createNewExchangeRate(ResultSet resultSet, Currency baseCurrency, Currency targetCurrency) throws SQLException {
        return new ExchangeRate(resultSet.getInt(1), baseCurrency, targetCurrency, resultSet.getBigDecimal(4));
    }

    public static List<ExchangeRate> getExchangeRates() throws SQLException {
        final String query = "SELECT * FROM exchangeRates";

        ArrayList<ExchangeRate> exchangeRatesList = new ArrayList<>();

        Connection dbConnection = DBConnection.getDBConnection();

        PreparedStatement statement = dbConnection.prepareStatement(query);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            Currency baseCurrency = CurrencyModel.getCurrencyByID(resultSet.getInt(2));
            Currency targetCurrency = CurrencyModel.getCurrencyByID(resultSet.getInt(3));
            exchangeRatesList.add(createNewExchangeRate(resultSet, baseCurrency, targetCurrency));
        }

        return exchangeRatesList;
    }

    public static ExchangeRate getExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, nonExistentCurrencyException {
        Currency baseCurrency = CurrencyModel.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = CurrencyModel.getCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new nonExistentCurrencyException();
        }

        final String query = "SELECT * FROM exchangeRates WHERE basecurrencyid = ? AND targetcurrencyid = ?";

        Connection dbConnection = DBConnection.getDBConnection();

        PreparedStatement statement = dbConnection.prepareStatement(query);

        statement.setInt(1, baseCurrency.id);
        statement.setInt(2, targetCurrency.id);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();

        if (resultSet.next()) {
            return createNewExchangeRate(resultSet, baseCurrency, targetCurrency);
        }
        return null;
    }

    public static void createNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws SQLException, nonExistentCurrencyException {
        Currency baseCurrency = CurrencyModel.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = CurrencyModel.getCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new nonExistentCurrencyException();
        }

        final String query = "INSERT INTO exchangerates(basecurrencyid, targetcurrencyid, rate) VALUES (?, ?, ?)";

        Connection dbConnection = DBConnection.getDBConnection();

        PreparedStatement statement = dbConnection.prepareStatement(query);

        statement.setInt(1, baseCurrency.id);
        statement.setInt(2, targetCurrency.id);
        statement.setBigDecimal(3, rate);

        statement.execute();
    }

    public static void updateRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal newRate) throws SQLException, nonExistentCurrencyException {
        Currency baseCurrency = CurrencyModel.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = CurrencyModel.getCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new nonExistentCurrencyException();
        }

        final String query = "UPDATE exchangeRates SET rate = ? WHERE basecurrencyid = ? AND targetcurrencyid = ?";

        Connection dbConnection = DBConnection.getDBConnection();

        PreparedStatement statement = dbConnection.prepareStatement(query);

        statement.setBigDecimal(1, newRate);
        statement.setInt(2, baseCurrency.id);
        statement.setInt(3, targetCurrency.id);

        statement.execute();
    }
}
