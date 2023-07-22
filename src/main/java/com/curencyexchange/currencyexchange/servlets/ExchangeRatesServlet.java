package com.curencyexchange.currencyexchange.servlets;

import com.curencyexchange.currencyexchange.models.ExchangeRateModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            new ObjectMapper().writeValue(resp.getWriter(), ExchangeRateModel.getExchangeRates());
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database is not available.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String stringRate = req.getParameter("rate");

        if (baseCurrencyCode.length() == 0 || targetCurrencyCode.length() == 0 || stringRate.length() == 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует нужно поле формы.");
            return;
        }

        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(stringRate));

        try {
            ExchangeRateModel.createNewExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            new ObjectMapper().writeValue(resp.getWriter(), ExchangeRateModel.getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode));
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                resp.sendError(HttpServletResponse.SC_CONFLICT, "Валютная пара с таким кодом уже существует.");
                return;
            }
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database is not available");
        }
    }
}
