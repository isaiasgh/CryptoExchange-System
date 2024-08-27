package com.globant.service;

import com.globant.model.Finance.Wallet;
import com.globant.model.System.User;

import java.math.BigDecimal;

public class FinanceService {
    public boolean deposit (User user, BigDecimal amount) {
        Wallet wallet = user.getWallet();
        wallet.addFiatMoney(amount);
        return true;
    }
}