package com.globant.controller;

import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;
import com.globant.service.ExchangeSystemService;
import com.globant.service.FinanceService;
import com.globant.view.AccountView;

import java.math.BigDecimal;

public class SystemController {
    private final AccountView accountView = new AccountView();
    private final FinanceService financeService = new FinanceService();

    private User user;

    public SystemController (User user) {
        this.user = user;
    }

    public void handleAccountMenu () {
        while (true) {
            accountView.displayAccountMenu ();
            int choice = accountView.getUserChoice (6);
            switch (choice) {
                case 1:
                    BigDecimal amount = accountView.getAmountInput();

                    if (amount.compareTo(new BigDecimal("0")) == 0) {
                        accountView.diplayCancelationMessage();
                        break;
                    }

                    financeService.deposit(user, amount);
                    ExchangeSystemService.write();
                    accountView.displayDepositConfirmation(user, user.getWallet());
                    break;
                case 2:
                    accountView.displayWalletBalance(user, user.getWallet());
                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:
                    return;
                default:
                    accountView.showError("Invalid option. Please try again.");
            }
        }
    }
}