package com.globant.view;

import com.globant.model.Orders.BuyOrder;
import com.globant.model.Orders.Order;
import com.globant.model.Orders.SellingOrder;
import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;

import java.util.List;

public class PlaceOrderView extends View {
    public void displayPlaceOrderMenu () {
        System.out.println("Place Order Menu:");
        System.out.println("[1] Place Buying Order");
        System.out.println("[2] Place Selling Order");
        System.out.println("[3] Return");
    }

    public void displayCryptocurrencies () {
        showInfo("Cryptocurrencies: ");
        List<Cryptocurrency> cryptos = ExchangeSystem.getInstance().getCryptos();

        for (Cryptocurrency crypto : cryptos) {
            System.out.println(crypto.getName() + " - " + crypto.getShorthandSymbol());
        }
    }

    public void displayBuyOrderConfirmation(BuyOrder order) {
        String details = getBuyOrderDetails(order);
        displayOrderConfirmation(
                "Buy Order",
                "Your buy order has been placed successfully.",
                details, ANSI_BLUE
        );
    }

    public void displaySellingOrderConfirmation(SellingOrder order) {
        String details = getSellingOrderDetails(order);
        displayOrderConfirmation(
                "Selling Order",
                "Your selling order has been placed successfully.",
                details, ANSI_BLUE
        );
    }

    public void displayRemoveOrderConfirmation(Order order) {
        String details = "";

        if (order instanceof BuyOrder buyOrder) {
            details = getBuyOrderDetails(buyOrder);
        } else if (order instanceof SellingOrder sellingOrder) {
            details = getSellingOrderDetails(sellingOrder);
        }

        displayOrderConfirmation(
                "Order Removal",
                ANSI_RED + "The order has been permanently removed. This action cannot be undone." + ANSI_RESET,
                details, ANSI_RED
        );
    }

    private void displayOrderConfirmation(String orderType, String successMessage, String details, String ANSI) {
        super.showInfo(ANSI + orderType + " Confirmation:" + ANSI_RESET);
        System.out.println(ANSI + "-------------------" + ANSI_RESET);
        System.out.println(details);
        System.out.println(ANSI + "-------------------" + ANSI_RESET);
        super.showWarning(successMessage);
    }

    private String getBuyOrderDetails(BuyOrder order) {
        return String.format(
                "Buy Order ID: " + ANSI_GREEN + "%s\n" + ANSI_RESET +
                        "Cryptocurrency: " + ANSI_GREEN + "%s\n" + ANSI_RESET +
                        "Amount: " + ANSI_GREEN + "%s %s\n" + ANSI_RESET +
                        "Maximum Price: " + ANSI_GREEN + "$%s" + ANSI_RESET,
                order.getID(),
                order.getCryptocurrencyType().getShorthandSymbol(),
                order.getAmount(),
                order.getCryptocurrencyType().getShorthandSymbol(),
                order.getMaximumPrice()
        );
    }

    private String getSellingOrderDetails(SellingOrder order) {
        return String.format(
                "Selling Order ID: " + ANSI_GREEN + "%s\n" + ANSI_RESET +
                        "Cryptocurrency: " + ANSI_GREEN + "%s\n" + ANSI_RESET +
                        "Amount: " + ANSI_GREEN + "%s %s\n" + ANSI_RESET +
                        "Minimum Price: " + ANSI_GREEN + "$%s" + ANSI_RESET,
                order.getID(),
                order.getCryptocurrencyType().getShorthandSymbol(),
                order.getAmount(),
                order.getCryptocurrencyType().getShorthandSymbol(),
                order.getMinimumPrice()
        );
    }

    public void displayCryptoMarketPrice (Cryptocurrency crypto) {
        super.showInfo("Current " + crypto.getShorthandSymbol() + " price: $" + crypto.getMarketPrice());
    }
}