package com.globant.view;

import com.globant.model.Finance.Transaction;
import com.globant.model.Finance.Wallet;
import com.globant.model.Orders.BuyOrder;
import com.globant.model.Orders.Order;
import com.globant.model.Orders.OrderBook;
import com.globant.model.Orders.SellingOrder;
import com.globant.model.System.Cryptocurrency;
import com.globant.model.System.ExchangeSystem;
import com.globant.model.System.User;
import com.globant.service.FinanceService;
import com.globant.service.OrderBookService;

import java.math.BigDecimal;
import java.util.*;

public class AccountView extends View {
    public void displayAccountMenu () {
        System.out.println("Account Menu:");
        System.out.println("[1] Deposit Money");
        System.out.println("[2] View Wallet Balance");
        System.out.println("[3] Buy Cryptocurrencies from Exchange System");
        System.out.println("[4] Place Order");
        System.out.println("[5] Check my active orders");
        System.out.println("[6] View Transaction history");
        System.out.println("[7] Logout");
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
            if (transaction.getType().equals('B')) {
                System.out.println("Price payed: $" + transaction.getPrice());
            } else {
                System.out.println("Price received: $" + transaction.getPrice());
            }
            System.out.println(ANSI_BLUE + "----------" + ANSI_RESET);
        }
    }

    public boolean displayActiveOrders(OrderBook orderBook, User user) {
        List<BuyOrder> buyOrders = orderBook.getBuyOrders();
        List<SellingOrder> sellingOrders = orderBook.getSellingOrders();

        boolean hasActiveOrders = false;

        if (!buyOrders.isEmpty()) {
            super.showInfo("Active Buy Orders:");
            hasActiveOrders = true;
            for (BuyOrder buyOrder : buyOrders) {
                if (buyOrder.getOwner().equals(user)) {
                    System.out.println("Buy Order ID: " + buyOrder.getID());
                    System.out.println("Cryptocurrency: " + buyOrder.getCryptocurrencyType().getName());
                    System.out.println("Maximum Price: $" + buyOrder.getMaximumPrice());
                    System.out.println("Amount: " + buyOrder.getAmount() + " " + buyOrder.getCryptocurrencyType().getShorthandSymbol());
                    System.out.println("Status: Active");
                    System.out.println(ANSI_BLUE + "----------" + ANSI_RESET);
                }
            }
        }

        if (!sellingOrders.isEmpty()) {
            super.showInfo("Active Selling Orders:");
            hasActiveOrders = true;
            for (SellingOrder sellingOrder : sellingOrders) {
                if (sellingOrder.getOwner().equals(user)) {
                    System.out.println("Selling Order ID: " + sellingOrder.getID());
                    System.out.println("Cryptocurrency: " + sellingOrder.getCryptocurrencyType().getName());
                    System.out.println("Minimum Price: $" + sellingOrder.getMinimumPrice());
                    System.out.println("Amount: " + sellingOrder.getAmount() + " " + sellingOrder.getCryptocurrencyType().getShorthandSymbol());
                    System.out.println("Status: Active");
                    System.out.println(ANSI_BLUE + "----------" + ANSI_RESET);
                }
            }
        }

        if (!hasActiveOrders) {
            super.showInfo("No active orders found.");
        }
        return hasActiveOrders;
    }

    public void showCancelOrderMenu() {
        super.showInfo("Would you like to cancel any orders?");
        System.out.println("[1] Yes");
        System.out.println("[2] No");
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
        BigDecimal fiatMoneyBalance = wallet.getFiatMoneyBalance();
        BigDecimal fiatMoneyInOrders = OrderBookService.fiatAmountInBuyOrders(wallet, user);
        BigDecimal availableFiatMoney = fiatMoneyBalance.subtract(fiatMoneyInOrders);
        super.showInfo("Wallet balance has been successfully updated");
        displayFiatMoneyBalance(fiatMoneyInOrders, availableFiatMoney);
    }

    public void displayWalletBalance(User user, Wallet wallet) {
        super.showInfo("Wallet balance:");

        BigDecimal fiatMoneyBalance = wallet.getFiatMoneyBalance();
        BigDecimal fiatMoneyInOrders = OrderBookService.fiatAmountInBuyOrders(wallet, user);
        BigDecimal availableFiatMoney = fiatMoneyBalance.subtract(fiatMoneyInOrders);

        System.out.printf("User ID: %s\n",user.getId());
        displayFiatMoneyBalance(fiatMoneyInOrders, availableFiatMoney);

        for (Map.Entry<Cryptocurrency, BigDecimal> entry : wallet.getCryptocurrenciesBalance().entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                Cryptocurrency crypto = entry.getKey();
                displayCryptoBalance(OrderBookService.cryptoAmountInSellingOrders(crypto, user.getWallet(), user), FinanceService.getAvailableCrypto (user, crypto), crypto);
            }
        }
    }
}