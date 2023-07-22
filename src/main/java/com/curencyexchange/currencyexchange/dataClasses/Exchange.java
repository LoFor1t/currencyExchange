package com.curencyexchange.currencyexchange.dataClasses;

import java.math.BigDecimal;

public class Exchange {
    public Currency baseCurrency;
    public Currency targetCurrency;
    public BigDecimal rate;
    public String amount;
    public BigDecimal convertedAmount;

    public Exchange(Currency baseCurrency, Currency targetCurrency, BigDecimal rate, String amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
}
