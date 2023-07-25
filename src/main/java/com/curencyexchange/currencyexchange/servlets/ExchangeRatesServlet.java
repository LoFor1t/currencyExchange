package com.curencyexchange.currencyexchange.servlets;

import com.curencyexchange.currencyexchange.exceptions.nonExistentCurrencyException;
import com.curencyexchange.currencyexchange.models.ExchangeRateDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            new ObjectMapper().writeValue(resp.getWriter(), ExchangeRateDAO.getExchangeRates());
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database is not available.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String stringRate = req.getParameter("rate");

        if (baseCurrencyCode == null || targetCurrencyCode == null || stringRate == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует нужно поле формы.");
            return;
        }

        if (baseCurrencyCode.length() == 0 || targetCurrencyCode.length() == 0 || stringRate.length() == 0){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует нужно поле формы.");
            return;
        }

        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(stringRate));

        try {
            ExchangeRateDAO.createNewExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            new ObjectMapper().writeValue(resp.getWriter(), ExchangeRateDAO.getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode));
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                resp.sendError(HttpServletResponse.SC_CONFLICT, "Валютная пара с таким кодом уже существует.");
                return;
            }
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database is not available");
        } catch (nonExistentCurrencyException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "We did not find one of currencies.");
        }
    }
}
