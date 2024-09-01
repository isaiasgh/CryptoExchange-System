package com.globant.controller;

import com.globant.service.ExchangeSystemService;
import com.globant.service.OrderMatchingService;
import com.globant.service.fluctuation.PriceFluctuationStrategy;
import com.globant.view.MainView;

public class RootController {
    private final MainView view;
    private final AccountController accountController;
    private static final int DEFAULT_FLUCTUATION_THRESHOLD = 2;

    public RootController (MainView view) {
        this.view = view;
        accountController = new AccountController (view);
    }

    public void configureSystem (PriceFluctuationStrategy strategy, int frequencyOfFluctuation) {
        ExchangeSystemService.read();
        OrderMatchingService.getInstance().setStrategy(strategy);
        if (!OrderMatchingService.getInstance().setFrequency(frequencyOfFluctuation)) {
            view.showError("ERROR: The system cannot afford a non-positive frequency. The system will use a default frequency of " + DEFAULT_FLUCTUATION_THRESHOLD + ".");
            OrderMatchingService.getInstance().setFrequency(DEFAULT_FLUCTUATION_THRESHOLD);
        }
    }

    public void run () {
        while (true) {
            view.displayInitialMenu ();
            int choice = view.getUserChoice (1, 3);
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