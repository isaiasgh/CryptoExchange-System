package com.globant.service;

import com.globant.model.system.ExchangeSystem;
import com.globant.model.system.User;

public class UserService {
    public boolean isEmailUsed (String email) {
        return ExchangeSystem.getInstance().containsUserByEmail(email);
    }

    public User registerUser (String name, String email, String password) {
        ExchangeSystem exchangeSystem = ExchangeSystem.getInstance();

        int id = exchangeSystem.getNextUserId();
        User user = new User (id, name, email, password);
        exchangeSystem.addUser(user);
        return user;
    }

    public User logIn (String email, String password) {
        if (!isEmailUsed(email)) throw new UnknownAccountException();

        return ExchangeSystem.getInstance().getUser(email, password);
    }
}