package com.globant.view;

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
                crypto.getShortHandSymbol(),
                amountPurchased,
                crypto.getShortHandSymbol(),
                totalCost,
                wallet.getFiatMoneyBalance(),
                crypto.getShortHandSymbol(),
                updatedCryptoBalance,
                crypto.getShortHandSymbol());
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

    public String getSelectedCrypto() {
        System.out.print("Enter the shorthand symbol: ");
        return scanner.next().toUpperCase();
    }

    public void displayCryptocurrenciesInfo () {
        super.showInfo("Cryptocurrencies: ");
        List <String> listInfo = ExchangeSystem.getInstance().getCryptosInfo();

        for (String s : listInfo) {
            System.out.println(ANSI_BLUE + "========================================================" + ANSI_RESET);
            System.out.println(s);
        }

        System.out.println(ANSI_BLUE + "========================================================" + ANSI_RESET);
    }

    public void displayCancelationMessage(String subject) {
        super.showInfo(subject + " has been canceled");
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