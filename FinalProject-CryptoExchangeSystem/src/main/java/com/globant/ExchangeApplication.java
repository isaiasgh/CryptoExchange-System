package com.globant;

import com.globant.controller.RootController;
import com.globant.service.fluctuation.MatchBasedPriceFluctuationStrategy;
import com.globant.service.fluctuation.RandomPriceFluctuationStrategy;
import com.globant.view.MainView;

public class ExchangeApplication {
    public static void main(String[] args) {
        MainView view = new MainView();
        RootController rootController = new RootController(view);
        rootController.configureSystem(new MatchBasedPriceFluctuationStrategy(), 2);
        rootController.run();
        view.close();
    }
}