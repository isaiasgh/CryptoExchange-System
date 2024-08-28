package com.globant.view;

import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;

import java.math.BigDecimal;
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

    public void displayBuyOrderConfirmation(Cryptocurrency crypto, BigDecimal amount, BigDecimal maximumPrice, User user)  {
        super.showInfo("Order Confirmation:");
        System.out.println("-------------------");
        System.out.println("User ID: " + user.getId());
        System.out.println("Cryptocurrency: " + crypto.getShorthandSymbol());
        System.out.println("Amount: " + amount + " " + crypto.getShorthandSymbol());
        System.out.println("Maximum Price: $" + maximumPrice);
        System.out.println("-------------------");
        super.showInfo("Your buy order has been placed successfully.");
    }

    public void displayCryptoMarketPrice (Cryptocurrency crypto) {
        super.showInfo("Current " + crypto.getShorthandSymbol() + " price: $" + crypto.getMarketPrice());
    }
}