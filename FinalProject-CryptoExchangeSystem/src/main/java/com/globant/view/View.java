package com.globant.view;

import java.util.InputMismatchException;
import java.util.Scanner;

public class View {
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLUE = "\u001B[34m";

    protected final Scanner scanner = new Scanner(System.in);
    protected static final int INVALID_CHOICE = -1;

    public void showError(String errorMessage) {
        System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
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