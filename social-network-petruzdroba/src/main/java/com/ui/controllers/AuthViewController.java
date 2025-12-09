package com.ui.controllers;

import com.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.awt.*;

public class AuthViewController {
    private UserService service;

    public AuthViewController() {}

    public void setUserService(UserService userService) {
        this.service = userService;
    }

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
}
