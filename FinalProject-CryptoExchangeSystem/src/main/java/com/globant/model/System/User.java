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

    public User (int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.wallet = new Wallet();
        this.transactions = new ArrayList<>();
    }
}