package com.curencyexchange.currencyexchange.dataClasses;

public class Currency {
    public int ID;
    public String name;
    public String code;
    public String sign;

    public Currency(int id, String code, String fullName, String sign) {
        this.ID = id;
        this.code = code;
        this.name = fullName;
        this.sign = sign;
    }
}
