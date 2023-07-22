package com.curencyexchange.currencyexchange.servlets;

import com.curencyexchange.currencyexchange.exceptions.nonExistentCurrencyException;
import com.curencyexchange.currencyexchange.models.CurrencyModel;
import com.curencyexchange.currencyexchange.dataClasses.Exchange;
import com.curencyexchange.currencyexchange.dataClasses.ExchangeRate;
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

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRate exchangeRate;
        BigDecimal rate;

        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (from == null || to == null || amount == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует нужно поле формы");
            return;
        }

        try {
            exchangeRate = ExchangeRateModel.getExchangeRateByCode(from, to);
            if (exchangeRate != null) {
                rate = exchangeRate.rate;
            } else {
                exchangeRate = ExchangeRateModel.getExchangeRateByCode(to, from);
                if (exchangeRate != null) {
                    rate = BigDecimal.valueOf(1/exchangeRate.rate.doubleValue());
                } else {
                    ExchangeRate fromExchangeRate = ExchangeRateModel.getExchangeRateByCode("USD", from);
                    ExchangeRate toExchangeRate = ExchangeRateModel.getExchangeRateByCode("USD", to);
                    if (fromExchangeRate != null && toExchangeRate != null) {
                        rate = BigDecimal.valueOf(toExchangeRate.rate.doubleValue() / fromExchangeRate.rate.doubleValue());
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Такой валютной пары не существует.");
                        return;
                    }
                }
            }
            new ObjectMapper().writeValue(resp.getWriter(), new Exchange(CurrencyModel.getCurrencyByCode(from),
                    CurrencyModel.getCurrencyByCode(to),
                    rate,
                    amount,
                    rate.multiply(BigDecimal.valueOf(Double.parseDouble(amount)))));
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        } catch (nonExistentCurrencyException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "We did not find one of currencies.");
        }
    }
}
