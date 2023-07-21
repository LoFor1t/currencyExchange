package com.curencyexchange.currencyexchange;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyModel {
    private static Currency createNewCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
    }

    public static List<Currency> getCurrencies () throws SQLException {
        final String query = "SELECT * FROM currencies";

        List<Currency> currencyList = new ArrayList<>();

        Connection dbConnection = DBConnection.getDBConnection();

        PreparedStatement statement = dbConnection.prepareStatement(query);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            currencyList.add(createNewCurrency(resultSet));
        }

        return currencyList;
    }

    public static Currency getCurrencyByCode(String code) throws SQLException {
        final String query = "SELECT * FROM currencies WHERE code = ?";

        Connection dbConnection = DBConnection.getDBConnection();

        PreparedStatement statement = dbConnection.prepareStatement(query);

        statement.setString(1, code);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();

        if(resultSet.next()) {
            return createNewCurrency(resultSet);
        }
        return null;
    }

    public static Currency getCurrencyByID(int id) throws SQLException {
        final String query = "SELECT * FROM currencies WHERE id = ?";

        Connection dbConnection = DBConnection.getDBConnection();

        PreparedStatement statement = dbConnection.prepareStatement(query);

        statement.setInt(1, id);

        statement.execute();

        ResultSet resultSet = statement.getResultSet();

        if(resultSet.next()) {
            return createNewCurrency(resultSet);
        }
        return null;
    }
}
