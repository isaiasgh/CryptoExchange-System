package com.globant.controller;

import com.globant.model.system.Cryptocurrency;
import com.globant.model.system.ExchangeSystem;
import com.globant.model.system.User;
import com.globant.service.ExchangeSystemService;
import com.globant.service.FinanceService;
import com.globant.service.OrderBookService;
import com.globant.service.exceptions.InsufficientExchangeFundsException;
import com.globant.util.BudgetCheckResult;
import com.globant.view.AccountView;

import java.math.BigDecimal;

public class SystemController {
    private final AccountView accountView = new AccountView();
    private final FinanceService financeService = new FinanceService();
    private final ManageOrderController manageOrderController;

    private User user;

    public SystemController (User user) {
        this.user = user;
        financeService.setUser (user);
        manageOrderController = new ManageOrderController(user, financeService);
    }

    public void handleAccountMenu () {
        while (true) {
            accountView.displayAccountMenu ();
            int choice = accountView.getUserChoice (1, 7);
            BigDecimal amount;
            BudgetCheckResult result;

            switch (choice) {
                case 1:
                    amount = accountView.getBigDecimalInput("Enter the amount: ");

                    if (amount.compareTo(new BigDecimal("0")) == 0) {
                        accountView.displayCancellationMessage("Deposit");
                        break;
                    }

                    financeService.deposit(amount);
                    ExchangeSystemService.write();
                    accountView.displayDepositConfirmation(user, user.getWallet());
                    break;
                case 2:
                    accountView.displayWalletBalance(user, user.getWallet());
                    break;
                case 3:
                    accountView.displayCryptocurrenciesInfo();

                    if (user.getWallet().getFiatMoneyBalance().equals(new BigDecimal("0"))) {
                        accountView.showError("Currently you do not have fiat money in your account. Please make a deposit and try again later.");
                        break;
                    }

                    accountView.displayFiatMoneyBalance (OrderBookService.fiatAmountInBuyOrders(user.getWallet(), user), FinanceService.getAvailableFiatMoney (user));
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
                        financeService.generateTransaction(amount, totalPrice, crypto);
                        ExchangeSystemService.write();
                        accountView.displayPurchaseConfirmation(user, user.getWallet(), crypto, amount, totalPrice);
                    }

                    break;
                case 4:
                    manageOrderController.handlePlaceOrderMenu();
                    break;
                case 5:
                    boolean hasActiveOrders = accountView.displayActiveOrders(ExchangeSystem.getInstance().getOrderBook(), user);

                    if (hasActiveOrders) {
                        handleCancelOrderMenu();
                    }

                    break;
                case 6:
                    accountView.displayTransactions(user);
                    break;
                case 7:
                    accountView.displayLogoutConfirmation();
                    return;
                default:
                    accountView.showError("Invalid option. Please try again.");
            }
        }
    }

    private BigDecimal handleBuyService (Cryptocurrency crypto, BigDecimal quantity) {
        BudgetCheckResult result = financeService.handleEnoughFiatBudget(quantity.multiply(crypto.getMarketPrice()));

        if (!result.isSuccess()) {
            accountView.showError(result.getErrorMessage());
            return BigDecimal.ZERO;
        }

        try {
            return financeService.buy(crypto, quantity);
        } catch (InsufficientExchangeFundsException e) {
            accountView.showError("ERROR: The exchange system does not have enough amount available.");
            return BigDecimal.ZERO;
        }
    }

    private Cryptocurrency handleSelectedCrypto (String selectedCrypto) {
        Cryptocurrency crypto = financeService.getCryptoSelected(selectedCrypto);

        if (selectedCrypto.equals("0")) return null;

        if (crypto == null) {
            accountView.showError("Invalid input. Please enter a valid shorthand symbol or '0' to cancel.");
            String newSelectedCrypto = accountView.getUserCryptoChoice().toUpperCase();
            return handleSelectedCrypto(newSelectedCrypto);
        }

        return crypto;
    }

    private void handleCancelOrderMenu () {
        accountView.showCancelOrderMenu();
        while (true) {
            int choice = accountView.getUserChoice (1, 2);
            switch (choice) {
                case 1:
                    manageOrderController.cancelOrder();
                    return;
                case 2:
                    return;
                default:
                    accountView.showError("Invalid option. Please try again.");
            }
        }
    }
}