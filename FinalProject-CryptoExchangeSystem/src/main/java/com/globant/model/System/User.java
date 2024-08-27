package com.globant.model.System;

import com.globant.model.Finance.Transaction;
import com.globant.model.Finance.Wallet;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class User implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
    private Wallet wallet;
    private List <Transaction> transactions;

    public User (int id, String name, String email, String password, List cryptos) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.wallet = new Wallet(cryptos);
        this.transactions = new ArrayList<>();
    }

    public String getEmail () {
        return email;
    }

    public String getPassword () {
        return password;
    }

    public Wallet getWallet () {
        return wallet;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}