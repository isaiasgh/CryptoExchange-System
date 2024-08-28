package com.globant.controller;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.User;
import com.globant.service.ExchangeSystemService;
import com.globant.service.FinanceService;
import com.globant.service.InsufficientExchangeFundsException;
import com.globant.service.InsufficientFundsException;
import com.globant.view.AccountView;

import java.math.BigDecimal;

public class SystemController {
    private final AccountView accountView = new AccountView();
    private final FinanceService financeService = new FinanceService();
    private final PlaceOrderController placeOrderController;

    private User user;

    public SystemController (User user) {
        this.user = user;
        placeOrderController = new PlaceOrderController (user, financeService);
    }

    public void handleAccountMenu () {
        while (true) {
            accountView.displayAccountMenu ();
            int choice = accountView.getUserChoice (6);
            BigDecimal amount;

            switch (choice) {
                case 1:
                    amount = accountView.getBigDecimalInput("Enter the amount: ");

                    if (amount.compareTo(new BigDecimal("0")) == 0) {
                        accountView.displayCancellationMessage("Deposit");
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
                    if (user.getWallet().getFiatMoneyBalance().equals(new BigDecimal("0"))) {
                        accountView.showError("Currently you do not have fiat money in your account. Please make a deposit and try again later.");
                        break;
                    }

                    accountView.displayCryptocurrenciesInfo();
                    accountView.displayFiatMoneyBalance(user.getWallet());
                    String selectedCrypto = accountView.getUserCryptoChoice();
                    Cryptocurrency crypto = handleSelectedCrypto (selectedCrypto);

                    if (crypto == null) {
                        accountView.displayCancellationMessage("Purchase");
                        break;
                    }

                    amount = accountView.getBigDecimalInput("Enter the amount: ");

                    if (amount.compareTo(new BigDecimal("0")) == 0) {
                        accountView.displayCancellationMessage("Purchase");
                        break;
                    }

                    BigDecimal totalPrice = handleBuyService(crypto, amount);

                    if (totalPrice.compareTo(new BigDecimal("0")) != 0) {
                        financeService.generateTransaction(user, amount, totalPrice, crypto);
                        ExchangeSystemService.write();
                        accountView.displayPurchaseConfirmation(user, user.getWallet(), crypto, amount, totalPrice);
                    }

                    break;
                case 4:
                    placeOrderController.handlePlaceOrderMenu();
                    break;
                case 5:
                    accountView.displayTransactions(user);
                    break;
                case 6:
                    return;
                default:
                    accountView.showError("Invalid option. Please try again.");
            }
        }
    }

    public BigDecimal handleBuyService (Cryptocurrency crypto, BigDecimal amount) {
        try {
            return financeService.buy(user.getWallet(), crypto, amount);
        } catch (InsufficientFundsException e) {
            accountView.showError("ERROR: You do not have enough funds in your account. Please try again later.");
            return new BigDecimal("0");
        } catch (InsufficientExchangeFundsException e) {
            accountView.showError("ERROR: The exchange system does not have enough amount available.");
            return new BigDecimal("0");
        }
    }

    public Cryptocurrency handleSelectedCrypto (String selectedCrypto) {
        Cryptocurrency crypto = financeService.getCryptoSelected(selectedCrypto);

        if (selectedCrypto.equals("0")) return null;

        if (crypto == null) {
            accountView.showError("Invalid input. Please enter a valid shorthand symbol or '0' to cancel.");
            String newSelectedCrypto = accountView.getUserCryptoChoice().toUpperCase();
            return handleSelectedCrypto(newSelectedCrypto);
        }

        return crypto;
    }
}