package com.curencyexchange.currencyexchange.servlets;

import com.curencyexchange.currencyexchange.models.CurrencyDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            new ObjectMapper().writeValue(resp.getWriter(), CurrencyDAO.getCurrencies());
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database is not available.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (name.length() == 0 || code.length() == 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует нужное поле формы.");
            return;
        }

        try {
            CurrencyDAO.createNewCurrency(name, code, sign.length() == 0 ? null : sign);
            new ObjectMapper().writeValue(resp.getWriter(), CurrencyDAO.getCurrencyByCode(code));
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                resp.sendError(HttpServletResponse.SC_CONFLICT, "Валюта с таким кодом уже существует.");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database is not available.");
            }
        }

    }
}
