package com.globant.view;

import com.globant.model.system.Cryptocurrency;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class View {
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLUE = "\u001B[34m";
    protected static final String ANSI_YELLOW = "\u001B[33m";
    protected static final String ANSI_GREEN = "\u001B[32m";

    protected final Scanner scanner = new Scanner(System.in);
    protected static final int INVALID_CHOICE = -1;

    public void displayCancellationMessage(String subject) {
        showError(subject + " has been canceled");
    }

    public void displayFiatMoneyBalance(BigDecimal fiatMoneyInOrders, BigDecimal availableFiatMoney) {
        System.out.printf(
                ANSI_BLUE + "Fiat Money Balance:" + ANSI_RESET + "\n" +
                        "  Available: " + ANSI_GREEN + "$%s\n" + ANSI_RESET +
                        "  Committed in Orders: " + "$%s\n",
                availableFiatMoney, fiatMoneyInOrders
        );
    }

    public void displayCryptoBalance(BigDecimal cryptoInSellOrders, BigDecimal availableCrypto, Cryptocurrency crypto) {
        System.out.printf(
                ANSI_BLUE + crypto.getName() + " Balance:" + ANSI_RESET + "\n" +
                        "  Available: " + ANSI_GREEN + "%s " + crypto.getShorthandSymbol() + "\n" + ANSI_RESET +
                        "  Committed in Sell Orders: " + "%s " + crypto.getShorthandSymbol() + "\n",
                availableCrypto, cryptoInSellOrders
        );
    }

    public void showError(String errorMessage) {
        System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
    }

    public int getUserChoice (String message, int minRange, int maxRange) {
        while (true) {
            System.out.print(message);
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < minRange || choice > maxRange) {
                    showError("Choice out of range. Please enter a number between " + minRange + " and " + maxRange + ".");
                } else {
                    return choice;
                }
            } catch (InputMismatchException e) {
                showError("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    public int getUserChoice (int minRange, int maxRange) {
        return getUserChoice("Enter your choice: ", minRange, maxRange);
    }

    public BigDecimal getBigDecimalInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                String input = scanner.nextLine();

                BigDecimal returnInput = new BigDecimal(input.trim());

                if (returnInput.compareTo(BigDecimal.ZERO) < 0) {
                    showError("Invalid amount format. Please enter a valid number greater than 0 or type 0 to cancel.");
                } else {
                    return returnInput;
                }

            } catch (NumberFormatException e) {
                showError("Invalid amount format. Please enter a valid number or type 0 to cancel.");
            }
        }
    }

    public String getUserCryptoChoice() {
        System.out.print("Enter the shorthand symbol: ");
        String userChoice = scanner.next().toUpperCase();
        scanner.nextLine();
        return userChoice;
    }

    public void showInfo(String message) {
        System.out.println(ANSI_BLUE + message + ANSI_RESET);
    }

    public void showWarning(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }

    public void close() {
        scanner.close();
    }
}