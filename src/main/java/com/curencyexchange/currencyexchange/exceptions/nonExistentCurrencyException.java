package com.curencyexchange.currencyexchange.exceptions;

public class nonExistentCurrencyException extends Exception{
    public nonExistentCurrencyException() {
        super("Currency isn't exist in database.");
    }
}
