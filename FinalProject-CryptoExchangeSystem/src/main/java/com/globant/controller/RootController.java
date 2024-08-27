package com.globant.controller;

import com.globant.service.ExchangeSystemService;
import com.globant.view.MainView;

public class RootController {
    private final MainView view;
    private final AccountController accountController;

    public RootController (MainView view) {
        this.view = view;
        accountController = new AccountController (view);
    }

    public void run () {
        ExchangeSystemService.read();

        while (true) {
            view.displayInitialMenu ();
            int choice = view.getUserChoice (3);
            switch (choice) {
                case 1:
                    accountController.handleLogIn();
                    break;
                case 2:
                    accountController.handleRegister();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    view.showError("Invalid option. Please try again.");
            }
        }
    }
}