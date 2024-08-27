package com.globant.controller;

import com.globant.model.System.User;
import com.globant.view.AccountView;

public class SystemController {
    AccountView accountView = new AccountView();

    private User user;

    public SystemController (User user) {
        this.user = user;
    }

    public void handleAccountMenu () {

    }
}
