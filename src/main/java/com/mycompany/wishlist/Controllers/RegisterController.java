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

public class RegisterController {
     @FXML
    private TextField name;

    @FXML
    private TextField mail;

    @FXML
    private PasswordField pass;

    @FXML
    private PasswordField rePass;

    @FXML
    private Button rgstBtn;

    @FXML
    private Label backArrow;

    @FXML
    private Label eyeIcon;

    @FXML
    private Label eyeIcon2;

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        App.setRoot("index");
    }

    @FXML
    private void changeVisibility(MouseEvent event) {
        System.out.println("Clicked!");
    }

     @FXML
    private void changeVisibility2(MouseEvent event) {
        System.out.println("Clicked!");
    }

    @FXML
    private void register(ActionEvent event) throws IOException {
        App.setRoot("home");
    }
}
