package com.globant.view;

import com.globant.model.Finance.Transaction;
import com.globant.model.Finance.Wallet;
import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;

import java.math.BigDecimal;
import java.util.*;

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

    public void displayPurchaseConfirmation(User user, Wallet wallet, Cryptocurrency crypto, BigDecimal amountPurchased, BigDecimal totalCost) {
        super.showInfo("Purchase completed successfully.");
        String message = "User ID: %s\n" +
                "Purchased Cryptocurrency: %s\n" +
                "Amount Purchased: %s %s\n" +
                "Total Cost: $%s\n" +
                "Updated Fiat Money Balance: $%s\n" +
                "Updated %s Balance: %s %s\n";

        BigDecimal updatedCryptoBalance = wallet.getCryptocurrenciesBalance().get(crypto);
        System.out.printf(message,
                user.getId(),
                crypto.getShorthandSymbol(),
                amountPurchased,
                crypto.getShorthandSymbol(),
                totalCost,
                wallet.getFiatMoneyBalance(),
                crypto.getShorthandSymbol(),
                updatedCryptoBalance,
                crypto.getShorthandSymbol());
    }

    public void displayTransactions (User user) {
        List<Transaction> transactions = user.getTransactions();

        if (transactions.isEmpty()) {
            super.showInfo("No transactions found.");
            return;
        }

        super.showInfo("Transaction History for " + user.getName() + ":");

        for (Transaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getID());
            System.out.println("Type: " + transaction.getType());
            System.out.println("Amount: " + transaction.getAmountTraded() + " " + transaction.getCryptocurrency().getShorthandSymbol());
            System.out.println("Price payed: $" + transaction.getPrice());
            System.out.println(ANSI_BLUE + "----------" + ANSI_RESET);
        }
    }

    public void displayCryptocurrenciesInfo () {
        super.showInfo("Cryptocurrencies: ");
        List<String> listInfo = ExchangeSystem.getInstance().getCryptosInfo();

        for (String s : listInfo) {
            System.out.println(ANSI_BLUE + "========================================================" + ANSI_RESET);
            System.out.println(s);
        }

        System.out.println(ANSI_BLUE + "========================================================" + ANSI_RESET);
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
                message += entry.getKey().getShorthandSymbol() + " Balance: %s\n";
                ref.add(entry.getValue());
            }
        }

        System.out.printf(message, ref.toArray());
    }
}