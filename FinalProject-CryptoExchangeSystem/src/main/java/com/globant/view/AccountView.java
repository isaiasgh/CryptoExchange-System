package com.globant.view;

import com.globant.model.Finance.Wallet;
import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

public class AccountView extends View {
    public void displayAccountMenu () {
        System.out.println("Account Menu:");
        System.out.println("[1] Deposit Money");
        System.out.println("[2] View Wallet Balance");
        System.out.println("[3] Buy Cryptocurrencies from Exchange System");
        System.out.println("[4] Place Order");
        System.out.println("[5] View Transaction history");
        System.out.println("[6] Logout");
    }

    public BigDecimal getAmountInput() {
        try {
            System.out.print("Enter the amount: ");
            return scanner.nextBigDecimal();
        } catch (InputMismatchException e) {
            super.showError("Invalid amount format. Please enter a valid number or type 0 to cancel");
            scanner.nextLine();
            return getAmountInput();
        }
    }

    public void diplayCancelationMessage() {
        super.showInfo("Deposit has been canceled");
    }

    public void displayDepositConfirmation(User user, Wallet wallet) {
        super.showInfo("Wallet balance has been successfully updated");
        displayWalletBalance(user, wallet);
    }

    public void displayWalletBalance (User user, Wallet wallet) {
        super.showInfo("Wallet balance:");
        String message = "User ID: %s\nFiat Money Balance: %s\n";

        List<Object> ref = new ArrayList<>();
        ref.add(user.getId());
        ref.add(wallet.getFiatMoneyBalance());

        for (Map.Entry<Cryptocurrency, BigDecimal> entry : wallet.getCryptocurrenciesBalance().entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                message += entry.getKey().getShortHandSymbol() + " Balance: %s\n";
                ref.add(entry.getValue());
            }
        }

        System.out.printf(message, ref.toArray());
    }
}