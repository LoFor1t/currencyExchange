package com.curencyexchange.currencyexchange.dataClasses;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "exchangeRates")
public class ExchangeRate {
    @Id
    @GeneratedValue
    @Column(name = "id")
    public int id;

    @ManyToOne
    @JoinColumn(name = "baseCurrencyId", nullable = false)
    public Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "targetCurrencyId", nullable = false)
    public Currency targetCurrency;

    @Column(name = "rate", nullable = false)
    public BigDecimal rate;

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public ExchangeRate() {

    }
}
