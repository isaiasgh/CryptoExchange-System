package com.globant.controller;

import com.globant.model.system.User;
import com.globant.service.ExchangeSystemService;
import com.globant.service.exceptions.UnknownAccountException;
import com.globant.service.UserService;
import com.globant.view.MainView;

public class AccountController {
    private MainView view;
    private UserService userService = new UserService ();
    private SystemController systemController;

    public AccountController (MainView view) {
        this.view = view;
    }

    public void handleLogIn () {
        String email = view.getEmailInput();
        String password = view.getPasswordInput();

        try{
            User user = userService.logIn(email, password);
            if (user == null) {
                view.showError("Incorrect credentials");
                return;
            }

            systemController = new SystemController(user);
            systemController.handleAccountMenu();
        } catch (UnknownAccountException e) {
            view.showError("Incorrect credentials");
        }
    }

    public void handleRegister () {
        String name = view.getNameInput();
        String email = view.getEmailInput();

        if (userService.isEmailUsed(email)) {
            view.showError ("That email is currently been used");
            handleEmailUsed();
            return;
        }

        String password = view.getPasswordInput();
        User user = userService.registerUser(name, email, password);
        ExchangeSystemService.write();
        view.showInfo("Your account has been successfully created!");
        view.showInfo("Account details: " + user);
    }

    public void handleEmailUsed () {
        view.retryEmailInput();
        while (true) {
            int choice = view.getUserChoice (1, 2);
            switch (choice) {
                case 1:
                    handleRegister ();
                    return;
                case 2:
                    return;
                default:
                    view.showError("Invalid option. Please try again.");
            }
        }
    }
}