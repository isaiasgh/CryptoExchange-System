package com.globant.view;

import com.globant.model.finance.Transaction;
import com.globant.model.finance.Wallet;
import com.globant.model.orders.BuyOrder;
import com.globant.model.orders.Order;
import com.globant.model.orders.OrderBook;
import com.globant.model.orders.SellingOrder;
import com.globant.model.system.Cryptocurrency;
import com.globant.model.system.ExchangeSystem;
import com.globant.model.system.User;
import com.globant.service.FinanceService;
import com.globant.service.OrderBookService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
        super.showInfo("Purchase Confirmation:");
        System.out.println(ANSI_BLUE + "--------------------------------------" + ANSI_RESET);
        String message =
                "User ID: " + ANSI_YELLOW + "%s\n" + ANSI_RESET +
                        "Purchased Cryptocurrency: " + ANSI_GREEN + "%s\n" + ANSI_RESET +
                        "Amount Purchased: " + ANSI_GREEN + "%s %s\n" + ANSI_RESET +
                        "Total Cost: " + ANSI_GREEN + "$%s\n" + ANSI_RESET +
                        "Updated Fiat Money Balance: " + ANSI_GREEN + "$%s\n" + ANSI_RESET +
                        "Updated %s Balance: " + ANSI_GREEN + "%s %s\n" + ANSI_RESET;

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
        System.out.println(ANSI_BLUE + "--------------------------------------" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Your purchase has been successfully completed." + ANSI_RESET);
    }

    public void displayTransactions(User user) {
        List<Transaction> transactions = user.getTransactions();

        if (transactions.isEmpty()) {
            super.showInfo(ANSI_BLUE + "No transactions found." + ANSI_RESET);
            return;
        }

        super.showInfo(ANSI_BLUE + "Transaction History for " + user.getName() + ":" + ANSI_RESET);

        for (Transaction transaction : transactions) {
            displayTransaction(transaction);
        }
    }

    private void displayTransaction(Transaction transaction) {
        String priceColor = transaction.getType().equals('B') ? ANSI_GREEN : ANSI_RED;
        String type = transaction.getType().equals('B') ? "Buy" : "Sell";

        System.out.printf(
                "  Transaction ID: " + ANSI_GREEN + "%s\n" + ANSI_RESET +
                        "  Type: " + ANSI_YELLOW + "%s\n" + ANSI_RESET +
                        "  Amount: " + "%s %s\n" +
                        "  Price: " + priceColor + "$%s\n" + ANSI_RESET +
                        "  Cryptocurrency: " + "%s\n" +
                        ANSI_BLUE + "  ------------------------------------------------------------\n" + ANSI_RESET,
                transaction.getID(),
                type,
                transaction.getAmountTraded(),
                transaction.getCryptocurrency().getShorthandSymbol(),
                transaction.getPrice(),
                transaction.getCryptocurrency().getName()
        );
    }

    public boolean displayActiveOrders(OrderBook orderBook, User user) {
        boolean hasActiveOrders = false;

        if (!orderBook.getBuyOrders().isEmpty()) {
            displayActiveBuyOrders(orderBook.getBuyOrders(), user);
            hasActiveOrders = true;
        }

        if (!orderBook.getSellingOrders().isEmpty()) {
            displayActiveSellingOrders(orderBook.getSellingOrders(), user);
            hasActiveOrders = true;
        }

        if (!hasActiveOrders) {
            super.showInfo(ANSI_YELLOW + "No active orders found." + ANSI_RESET);
        }

        return hasActiveOrders;
    }

    private void displayActiveSellingOrders(List<SellingOrder> sellingOrders, User user) {
        super.showInfo(ANSI_BLUE + "Active Selling Orders:" + ANSI_RESET);
        for (SellingOrder sellingOrder : sellingOrders) {
            if (sellingOrder.getOwner().equals(user)) {
                displayOrderDetails(sellingOrder);
            }
        }
    }

    private void displayActiveBuyOrders(List<BuyOrder> buyOrders, User user) {
        super.showInfo(ANSI_BLUE + "Active Buy Orders:" + ANSI_RESET);
        for (BuyOrder buyOrder : buyOrders) {
            if (buyOrder.getOwner().equals(user)) {
                displayOrderDetails(buyOrder);
            }
        }
    }

    private void displayOrderDetails(Order order) {
        String type = (order instanceof SellingOrder) ? "Selling" : "Buy";
        String status = "Active";
        String priceLabel = (order instanceof SellingOrder) ? "Minimum Price" : "Maximum Price";

        System.out.printf(
                "  %s Order ID: " + ANSI_GREEN + "%s\n" + ANSI_RESET +
                        "  Cryptocurrency: " + "%s\n" +
                        "  " + priceLabel + ": " + ANSI_GREEN + "$%s\n" + ANSI_RESET +
                        "  Amount: " + "%s %s\n" +
                        "  Status: " + ANSI_YELLOW + "%s\n" + ANSI_RESET,
                type,
                order.getID(),
                order.getCryptocurrencyType().getName(),
                (order instanceof SellingOrder) ? ((SellingOrder) order).getMinimumPrice() : ((BuyOrder) order).getMaximumPrice(),
                order.getAmount(),
                order.getCryptocurrencyType().getShorthandSymbol(),
                status
        );
        System.out.println(ANSI_BLUE + "  -------------------" + ANSI_RESET);
    }

    public void showCancelOrderMenu() {
        super.showInfo("Would you like to cancel any orders?");
        System.out.println("[1] Yes");
        System.out.println("[2] No");
    }

    public void displayCryptocurrenciesInfo() {
        super.showInfo(ANSI_BLUE + "Cryptocurrencies Info:" + ANSI_RESET);

        List<Cryptocurrency> cryptos = ExchangeSystem.getInstance().getCryptocurrenciesList();

        if (cryptos.isEmpty()) {
            showError("No cryptocurrencies available.");
            return;
        }

        System.out.printf(
                ANSI_BLUE + "%-40s %-10s %-15s %-15s\n" + ANSI_RESET,
                "ID", "Name", "Symbol", "Market Price"
        );
        System.out.println(ANSI_BLUE + "----------------------------------------------------------------------------------------" + ANSI_RESET);

        for (Cryptocurrency crypto : cryptos) {
            System.out.printf(
                    "%-40s %-10s %-15s $%-14s\n",
                    crypto.getUniqueID(),
                    crypto.getName(),
                    crypto.getShorthandSymbol(),
                    crypto.getMarketPrice()
            );
        }

        System.out.println(ANSI_BLUE + "----------------------------------------------------------------------------------------" + ANSI_RESET);
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

    public void displayLogoutConfirmation() {
        System.out.println(ANSI_BLUE + "You have successfully logged out. Thank you for using our service!" + ANSI_RESET);
    }
}