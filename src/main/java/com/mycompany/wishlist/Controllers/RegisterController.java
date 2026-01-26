package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.UserService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;

public class RegisterController {
    @FXML
    private TextField name;
    @FXML
    private Label nameError;
    @FXML
    private TextField mail;
    @FXML
    private Label mailError;

    // Password fields
    @FXML
    private PasswordField pass;
    @FXML
    private TextField visiblePass; // New visible password field
    @FXML
    private Label passError;
    @FXML
    private StackPane passContainer;

    // Re-Password fields
    @FXML
    private PasswordField rePass;
    @FXML
    private TextField visibleRePass; // New visible re-password field
    @FXML
    private Label rePassError;
    @FXML
    private StackPane rePassContainer;

    @FXML
    private Button rgstBtn;
    @FXML
    private Label backArrow;
    @FXML
    private Label eyeIcon;
    @FXML
    private Label eyeIcon2;
    @FXML
    private Label generalError;

    private UserService userService = new UserService();
    private boolean passwordVisible = false;
    private boolean rePasswordVisible = false;

    @FXML
    private void initialize() {
        // Initialize visible password fields (hidden initially)
        initializePasswordField(pass, visiblePass, passContainer);
        initializePasswordField(rePass, visibleRePass, rePassContainer);

        // Set up text change listeners to clear errors when user types
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearError(nameError);
            }
        });

        mail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearError(mailError);
            }
        });

        pass.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearError(passError);
            }
        });

        rePass.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearError(rePassError);
            }
        });

        // Initialize error labels as invisible
        clearAllErrors();
    }

    private void initializePasswordField(PasswordField passwordField, TextField visibleField, StackPane container) {
        // Hide visible field initially
        visibleField.setVisible(false);
        visibleField.setManaged(false);

        // Copy style from password field
        visibleField.setStyle(passwordField.getStyle());
        visibleField.setPrefHeight(passwordField.getPrefHeight());
        visibleField.setPromptText(passwordField.getPromptText());
        visibleField.setFont(passwordField.getFont());

        // Keep both fields in sync
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            visibleField.setText(newValue);
        });

        visibleField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordField.setText(newValue);
        });
    }

    private void clearAllErrors() {
        clearError(nameError);
        clearError(mailError);
        clearError(passError);
        clearError(rePassError);
        clearError(generalError);
    }

    private void clearError(Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }
    }

    private void showError(Label errorLabel, String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setTextFill(Color.RED);
            errorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
            errorLabel.setVisible(true);
        }
    }

    private void showGeneralError(String message) {
        if (generalError != null) {
            generalError.setText(message);
            generalError.setTextFill(Color.RED);
            generalError.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: center;");
            generalError.setVisible(true);
        }
    }

    private void showSuccess(String message) {
        if (generalError != null) {
            generalError.setText(message);
            generalError.setTextFill(Color.GREEN);
            generalError.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: center;");
            generalError.setVisible(true);
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        App.setRoot("index");
    }

    @FXML
    private void changeVisibility(MouseEvent event) {
        passwordVisible = !passwordVisible;
        togglePasswordVisibility(pass, visiblePass, passContainer, eyeIcon, passwordVisible);
    }

    @FXML
    private void changeVisibility2(MouseEvent event) {
        rePasswordVisible = !rePasswordVisible;
        togglePasswordVisibility(rePass, visibleRePass, rePassContainer, eyeIcon2, rePasswordVisible);
    }

    private void togglePasswordVisibility(PasswordField passwordField, TextField visibleField,
                                          StackPane container, Label eyeLabel, boolean isVisible) {
        if (isVisible) {
            // Show password in plain text
            eyeLabel.setText("👁‍🗨");
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            visibleField.setVisible(true);
            visibleField.setManaged(true);

            // Move focus to visible field
            visibleField.requestFocus();
            visibleField.positionCaret(visibleField.getText().length());
        } else {
            // Hide password with dots
            eyeLabel.setText("👁");
            visibleField.setVisible(false);
            visibleField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            // Move focus back to password field
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());
        }
    }

    @FXML
    private void register(ActionEvent event) throws IOException {
        clearAllErrors();

        // Get values from form
        String userName = name.getText().trim();
        String userMail = mail.getText().trim();
        String password = pass.getText(); // Always get from hidden field
        String rePassword = rePass.getText(); // Always get from hidden field

        // Basic field validation
        boolean hasError = false;

        if (userName.isEmpty()) {
            showError(nameError, "Name is required");
            hasError = true;
        }

        if (userMail.isEmpty()) {
            showError(mailError, "Email is required");
            hasError = true;
        }

        if (password.isEmpty()) {
            showError(passError, "Password is required");
            hasError = true;
        }

        if (rePassword.isEmpty()) {
            showError(rePassError, "Please re-enter password");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // Create user object
        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setMail(userMail);
        newUser.setPass(password);

        // Call service
        String result = userService.register(newUser, rePassword);

        if ("SUCCESS".equals(result)) {
            showSuccess("Registration successful! Redirecting...");

            // Delay before redirecting (optional)
            new Thread(() -> {
                try {
                    Thread.sleep(1500); // 1.5 second delay
                    javafx.application.Platform.runLater(() -> {
                        try {
                            App.setRoot("login");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else {
            // Show appropriate error based on service response
            if (result.contains("email")) {
                showError(mailError, result);
            } else if (result.contains("Password")) {
                showError(passError, result);
            } else if (result.contains("match")) {
                showError(rePassError, result);
            } else {
                showGeneralError(result);
            }
        }
    }
}