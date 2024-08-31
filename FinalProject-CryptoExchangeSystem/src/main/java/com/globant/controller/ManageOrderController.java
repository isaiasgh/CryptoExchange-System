package com.globant.controller;

import com.globant.model.Orders.Order;
import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;
import com.globant.service.*;
import com.globant.util.BudgetCheckResult;
import com.globant.view.PlaceOrderView;

import java.math.BigDecimal;

public class ManageOrderController {
    private User user;
    private PlaceOrderView placeOrderView = new PlaceOrderView();
    private FinanceService financeService;
    private OrderBookService orderBookService = new OrderBookService();

    public ManageOrderController(User user, FinanceService financeService) {
        this.user = user;
        this.financeService = financeService;
    }

    public void handlePlaceOrderMenu () {
        String selectedCrypto;
        Cryptocurrency crypto;

        while (true) {
            placeOrderView.displayPlaceOrderMenu();
            int choice = placeOrderView.getUserChoice(1, 3);
            BigDecimal amount;
            BudgetCheckResult result;

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
                    placeOrderView.displayFiatMoneyBalance (OrderBookService.fiatAmountInBuyOrders(user.getWallet(), user), financeService.getAvailableFiatMoney (user));
                    if (!validateNonZeroFiatBalance ()) break;
                    amount = placeOrderView.getBigDecimalInput("Enter the amount you want to buy: ");
                    if (!isValidAmount(amount)) break;
                    BigDecimal maximumPrice = placeOrderView.getBigDecimalInput("Enter the maximum price you want to pay: ");
                    if (!isValidAmount(maximumPrice)) break;
                    result = financeService.handleEnoughFiatBudget (maximumPrice, user);

                    if (result.isSuccess()) {
                        orderBookService.createBuyOrder(crypto, amount, maximumPrice, user);
                        placeOrderView.displayBuyOrderConfirmation(crypto, amount, maximumPrice, user);
                        ExchangeSystemService.write();
                    } else placeOrderView.showError(result.getErrorMessage());

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
                    placeOrderView.displayCryptoBalance(OrderBookService.cryptoAmountInSellingOrders(crypto, user.getWallet(), user), FinanceService.getAvailableCrypto (user, crypto), crypto);
                    if (!validateNonZeroCryptoBalance (crypto)) break;
                    amount = placeOrderView.getBigDecimalInput("Enter the amount you want to sell: ");
                    if (!isValidAmount(amount)) break;
                    result = financeService.handleEnoughCryptoBudget (amount, crypto, user);

                    if (result.isSuccess()) {
                        BigDecimal minimumPrice = placeOrderView.getBigDecimalInput("Enter the minimum price you want tu sell: ");
                        if (!isValidAmount(minimumPrice)) break;
                        orderBookService.createSellingOrder(crypto, amount, minimumPrice, user);
                        placeOrderView.displaySellingOrderConfirmation(crypto, amount, minimumPrice, user);
                        ExchangeSystemService.write();
                    }

                    break;
                case 3:
                    return;
                default:
                    placeOrderView.showError("Invalid option. Please try again.");
            }
        }
    }

    public void cancelOrder() {
        int orderID = placeOrderView.getUserChoice("Enter the order ID: ", 0, ExchangeSystem.getInstance().getOrderBook().getLastOrderID());
        Order order = handleOrderSelection(orderID);

        if (order == null) {
            placeOrderView.displayCancellationMessage("The order removal process");
            return;
        }

        orderBookService.removeOrder(order);
        placeOrderView.displayRemoveOrderConfirmation (order);
        ExchangeSystemService.write();
    }

    private boolean validateNonZeroCryptoBalance  (Cryptocurrency crypto) {
        if (user.getWallet().getCryptocurrenciesBalance().get(crypto).equals(new BigDecimal("0"))) {
            placeOrderView.showError("You do not have " + crypto.getName() + " to trade.");
            placeOrderView.displayCancellationMessage("The creation of a new selling order");
            return false;
        }

        if (user.getWallet().getCryptocurrenciesBalance().get(crypto).equals(OrderBookService.cryptoAmountInSellingOrders (crypto, user.getWallet(), user))) {
            placeOrderView.showError("Your current sell orders already account for all your available " + crypto.getName() + ". Please acquire more cryptocurrencies and try again later.");
            placeOrderView.displayCancellationMessage("The creation of a new selling order");
            return false;
        }

        return true;
    }

    private boolean validateNonZeroFiatBalance  () {
        if (user.getWallet().getFiatMoneyBalance().equals(new BigDecimal("0"))) {
            placeOrderView.showError("You do not have fiat money to trade.");
            placeOrderView.displayCancellationMessage("The creation of a new buying order");
            return false;
        }

        if (user.getWallet().getFiatMoneyBalance().equals(OrderBookService.fiatAmountInBuyOrders (user.getWallet(), user))) {
            placeOrderView.showError("Your current buy orders already account for all your available fiat money. Please deposit more funds and try again later.");
            placeOrderView.displayCancellationMessage("The creation of a new buying order");
            return false;
        }

        return true;
    }

    private boolean isValidAmount (BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("0")) == 0) {
            placeOrderView.displayCancellationMessage("Order creation");
            return false;
        }
        return true;
    }

    private Cryptocurrency handleSelectedCrypto (String selectedCrypto) {
        Cryptocurrency crypto = financeService.getCryptoSelected(selectedCrypto);

        if (selectedCrypto.equals("0")) return null;

        if (crypto == null) {
            placeOrderView.showError("Invalid input. Please enter a valid shorthand symbol or '0' to cancel.");
            String newSelectedCrypto = placeOrderView.getUserCryptoChoice().toUpperCase();
            return handleSelectedCrypto(newSelectedCrypto);
        }

        return crypto;
    }

    private Order handleOrderSelection (int selectedOrder) {
        Order order = orderBookService.getOrderSelected(selectedOrder);

        if (selectedOrder == 0) return null;

        if (order == null) {
            placeOrderView.showError("Invalid ID. Please enter a valid ID or '0' to cancel.");
            int newSelectedOrder = placeOrderView.getUserChoice("Enter the order ID: ", 0, ExchangeSystem.getInstance().getOrderBook().getLastOrderID());
            return handleOrderSelection(newSelectedOrder);
        }

        return order;
    }
}