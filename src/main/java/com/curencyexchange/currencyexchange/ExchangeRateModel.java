package com.curencyexchange.currencyexchange;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static ExchangeRate getExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        Currency baseCurrency = CurrencyModel.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = CurrencyModel.getCurrencyByCode(targetCurrencyCode);

        final String query = "SELECT * FROM exchangeRates WHERE basecurrencyid = ? AND targetcurrencyid = ?";

        Connection dbConnection = DBConnection.getDBConnection();

        PreparedStatement statement = dbConnection.prepareStatement(query);

        statement.setInt(1, baseCurrency.ID);
        statement.setInt(2, targetCurrency.ID);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();

        if (resultSet.next()) {
            return createNewExchangeRate(resultSet, baseCurrency, targetCurrency);
        }
        return null;
    }

    public static void createNewExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws SQLException {
        Currency baseCurrency = CurrencyModel.getCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = CurrencyModel.getCurrencyByCode(targetCurrencyCode);

        final String query = "INSERT INTO exchangerates(basecurrencyid, targetcurrencyid, rate) VALUES (?, ?, ?)";

        Connection dbConnection = DBConnection.getDBConnection();

        PreparedStatement statement = dbConnection.prepareStatement(query);

        statement.setInt(1, baseCurrency.ID);
        statement.setInt(2, targetCurrency.ID);
        statement.setBigDecimal(3, rate);

        statement.execute();
    }
}
