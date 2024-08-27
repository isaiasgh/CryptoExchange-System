package com.globant.service;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;

import java.util.Arrays;
import java.util.List;

public class UserService {
    private final List<Cryptocurrency> cryptos = Arrays.asList(
            ExchangeSystem.getInstance().getCryptocurrencyByShorthandSymbol("BTN"),
            ExchangeSystem.getInstance().getCryptocurrencyByShorthandSymbol("ETH"),
            ExchangeSystem.getInstance().getCryptocurrencyByShorthandSymbol("DOGE")
    );

    public boolean isEmailUsed (String email) {
        return ExchangeSystem.getInstance().containsUserByEmail(email);
    }

    public User registerUser (String name, String email, String password) {
        ExchangeSystem exchangeSystem = ExchangeSystem.getInstance();

        int id = exchangeSystem.getNextUserId();
        User user = new User (id, name, email, password, cryptos);
        exchangeSystem.addUser(user);
        return user;
    }

    public User logIn (String email, String password) {
        if (!isEmailUsed(email)) throw new UnknownAccountException();

        return ExchangeSystem.getInstance().getUser(email, password);
    }
}