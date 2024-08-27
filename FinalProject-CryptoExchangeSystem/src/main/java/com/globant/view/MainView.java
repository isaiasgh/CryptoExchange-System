package com.globant.view;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainView {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";

    private final Scanner scanner = new Scanner(System.in);
    private static final int INVALID_CHOICE = -1;

    public void displayInitialMenu () {
        System.out.println("Main Menu:");
        System.out.println("[1] Login");
        System.out.println("[2] Register");
        System.out.println("[3] Quit");
    }

    public void displayTryAgain () {
        System.out.println("Would you like to try with other email or return to the Main Menu?");
        System.out.println("[1] Try again");
        System.out.println("[2] Return to the Main Menu");
    }

    public void showError(String errorMessage) {
        System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
    }

    public String getNameInput () {
        System.out.print("Enter your name: ");
        return scanner.next();
    }

    public String getEmailInput () {
        System.out.print("Enter your email: ");
        return scanner.next();
    }

    public String getPasswordInput () {
        System.out.print("Enter a password: ");
        return scanner.next();
    }

    public int getUserChoice (int range) {
        System.out.print("Enter your choice: ");
        try {
            int choice = scanner.nextInt();
            if (choice < 1 || choice > range) {
                return INVALID_CHOICE;
            }
            return choice;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return INVALID_CHOICE;
        }
    }

    public void showInfo(String message) {
        System.out.println(ANSI_BLUE + message + ANSI_RESET);
    }

    public void close() {
        scanner.close();
    }
}