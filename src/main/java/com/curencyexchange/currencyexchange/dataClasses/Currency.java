package com.curencyexchange.currencyexchange.dataClasses;

import jakarta.persistence.*;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue
    @Column(name = "id")
    public int id;

    @Column(name = "fullname", nullable = false)
    public String name;

    @Column(name = "code", length = 3, nullable = false)
    public String code;

    @Column(name = "sign", nullable = true)
    public String sign;

    public Currency(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public Currency() {

    }
}
