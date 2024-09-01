package com.globant.model.system;

import com.globant.model.finance.Transaction;
import com.globant.model.finance.Wallet;

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

    public boolean addTransaction (Transaction transaction) {
        transactions.add(transaction);
        return true;
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
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