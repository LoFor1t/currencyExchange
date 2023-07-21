package com.curencyexchange.currencyexchange.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Utils {
    public static String checkExchangeRate(String requestPath) {
        if (requestPath == null) {
            return null;
        }

        String currencyPair = requestPath.replace("/", "");

        if (currencyPair.length() != 6) {
            return null;
        }
        return currencyPair;
    }
}
