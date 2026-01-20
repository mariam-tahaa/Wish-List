package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {
    @FXML
    private TextField mail;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private Label backArrow;

    @FXML
    private Label eyeIcon;

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        App.setRoot("index");
    }

    @FXML
    private void changeVisibility(MouseEvent event) {
        System.out.println("Clicked!");
    }

    @FXML
    private void login(ActionEvent event) {}
}
