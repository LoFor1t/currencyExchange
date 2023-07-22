package com.curencyexchange.currencyexchange.servlets;

import com.curencyexchange.currencyexchange.dataClasses.ExchangeRate;
import com.curencyexchange.currencyexchange.models.ExchangeRateModel;
import com.curencyexchange.currencyexchange.Utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("PATCH")) {
            doPatch(req, resp);
        }
        else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getPathInfo();

        String currencyPair = Utils.checkExchangeRate(requestPath);

        if (currencyPair == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Код валютной пары отсутствует в адресе");
            return;
        }

        try {
            ExchangeRate exchangeRate = ExchangeRateModel.getExchangeRateByCode(currencyPair.substring(0, 3), currencyPair.substring(3, 6));
            if (exchangeRate == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Обменный курс для пары не найден");
                return;
            }
            new ObjectMapper().writeValue(resp.getWriter(), exchangeRate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getPathInfo();

        String currencyPair = Utils.checkExchangeRate(requestPath);

        if (currencyPair == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует нужное поле в форме.");
            return;
        }

        String stringRate = req.getParameter("rate");

        if (stringRate == null || stringRate.length() == 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует нужное поле формы.");
            return;
        }

        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(stringRate));

        String baseCurrencyCode = currencyPair.substring(0, 3);
        String targetCurrencyCode = currencyPair.substring(3, 6);

        try {
            ExchangeRateModel.updateRate(baseCurrencyCode, targetCurrencyCode, rate);
            new ObjectMapper().writeValue(resp.getWriter(), ExchangeRateModel.getExchangeRateByCode(baseCurrencyCode, targetCurrencyCode));
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            System.out.println(e.getMessage());
        }
    }
}
