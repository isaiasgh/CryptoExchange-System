package com.globant.view;

public class MainView extends View {
    public void displayInitialMenu () {
        System.out.println("Main Menu:");
        System.out.println("[1] Login");
        System.out.println("[2] Register");
        System.out.println("[3] Quit");
    }

    public void retryEmailInput () {
        System.out.println("Would you like to try with other email or return to the Main Menu?");
        System.out.println("[1] Try again");
        System.out.println("[2] Return to the Main Menu");
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
}