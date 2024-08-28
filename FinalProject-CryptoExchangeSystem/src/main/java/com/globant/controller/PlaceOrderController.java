package com.globant.controller;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.User;
import com.globant.service.FinanceService;
import com.globant.service.InsufficientFundsException;
import com.globant.service.OrderBookService;
import com.globant.view.PlaceOrderView;

import java.math.BigDecimal;

public class PlaceOrderController {
    private User user;
    private PlaceOrderView placeOrderView = new PlaceOrderView();
    private FinanceService financeService;
    private OrderBookService orderBookService = new OrderBookService();

    public PlaceOrderController (User user, FinanceService financeService) {
        this.user = user;
        this.financeService = financeService;
    }

    public void handlePlaceOrderMenu () {
        String selectedCrypto;
        Cryptocurrency crypto;

        while (true) {
            placeOrderView.displayPlaceOrderMenu();
            int choice = placeOrderView.getUserChoice(3);
            BigDecimal amount;
            boolean enoughFunds;

            switch (choice) {
                case 1:
                    placeOrderView.displayCryptocurrencies();
                    selectedCrypto = placeOrderView.getUserCryptoChoice();
                    crypto = handleSelectedCrypto (selectedCrypto);

                    if (crypto == null) {
                        placeOrderView.displayCancellationMessage("The creation of a new buy order");
                        break;
                    }

                    placeOrderView.displayCryptoMarketPrice (crypto);

                    amount = placeOrderView.getBigDecimalInput("Enter the amount you want to buy: ");
                    BigDecimal maximumPrice = placeOrderView.getBigDecimalInput("Enter the maximum price you want to pay: ");

                    enoughFunds = handleEnoughBudget (maximumPrice);

                    if (enoughFunds) {
                        orderBookService.createBuyOrder(crypto, amount, maximumPrice, user);
                        placeOrderView.displayBuyOrderConfirmation(crypto, amount, maximumPrice, user);
                    }

                    break;
                case 2:
                    placeOrderView.displayCryptocurrencies();
                    selectedCrypto = placeOrderView.getUserCryptoChoice();
                    crypto = handleSelectedCrypto (selectedCrypto);

                    if (crypto == null) {
                        placeOrderView.displayCancellationMessage("The creation of a new selling order");
                        break;
                    }

                    placeOrderView.displayCryptoMarketPrice (crypto);

                    amount = placeOrderView.getBigDecimalInput("Enter the amount you want to sell: ");

                    enoughFunds = handleEnoughBudget (amount, crypto);

                    if (enoughFunds) {
                        BigDecimal minimumPrice = placeOrderView.getBigDecimalInput("Enter the minimum price you want tu sell: ");
                        orderBookService.createSellingOrder(crypto, amount, minimumPrice, user);
                    }

                    break;
                case 3:
                    return;
                default:
                    placeOrderView.showError("Invalid option. Please try again.");
            }
        }
    }

    public Cryptocurrency handleSelectedCrypto (String selectedCrypto) {
        Cryptocurrency crypto = financeService.getCryptoSelected(selectedCrypto);

        if (selectedCrypto.equals("0")) return null;

        if (crypto == null) {
            placeOrderView.showError("Invalid input. Please enter a valid shorthand symbol or '0' to cancel.");
            String newSelectedCrypto = placeOrderView.getUserCryptoChoice().toUpperCase();
            return handleSelectedCrypto(newSelectedCrypto);
        }

        return crypto;
    }

    public boolean handleEnoughBudget (BigDecimal amount) {
        try {
            return financeService.checkEnoughFunds(user.getWallet(), amount);
        } catch (InsufficientFundsException e) {
            placeOrderView.showError("ERROR: You do not have enough funds in your account to place this buy order. Please try again later.");
            return false;
        }
    }

    public boolean handleEnoughBudget (BigDecimal amount, Cryptocurrency crypto) {
        try {
            return financeService.checkEnoughFunds(user.getWallet(), amount, crypto);
        } catch (InsufficientFundsException e) {
            placeOrderView.showError("ERROR: You do not have enough cryptocurrency in your account to place this selling order. Please try again later.");
            return false;
        }
    }
}