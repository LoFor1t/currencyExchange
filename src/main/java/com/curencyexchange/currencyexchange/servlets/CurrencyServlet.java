package com.curencyexchange.currencyexchange.servlets;

import com.curencyexchange.currencyexchange.dataClasses.Currency;
import com.curencyexchange.currencyexchange.models.CurrencyModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getPathInfo();

        if (requestPath == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Код валюты отсутсвует в адресе");
            return;
        }

        String currencyCode = requestPath.replace("/", "");

        if (currencyCode.length() != 3) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Введён не верный код валюты.");
            return;
        }

        try {
            Currency currency = CurrencyModel.getCurrencyByCode(currencyCode);
            if (currency == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Валюта не найдена");
                return;
            }
            new ObjectMapper().writeValue(resp.getWriter(), currency);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка");
            System.out.println(e.getMessage());
        }
    }
}
