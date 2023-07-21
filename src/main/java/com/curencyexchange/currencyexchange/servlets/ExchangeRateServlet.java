package com.curencyexchange.currencyexchange.servlets;

import com.curencyexchange.currencyexchange.ExchangeRate;
import com.curencyexchange.currencyexchange.ExchangeRateModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getPathInfo();

        if (requestPath == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Код валютной пары отсутствует в адресе");
            return;
        }

        String currencyPair = requestPath.replace("/", "");

        if (currencyPair.length() != 6) {
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
}
