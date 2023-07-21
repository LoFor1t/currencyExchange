package com.curencyexchange.currencyexchange;

import java.math.BigDecimal;

public class ExchangeRate {
    public int id;
    public Currency baseCurrency;
    public Currency targetCurrency;
    public BigDecimal rate;

    public ExchangeRate(int id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
