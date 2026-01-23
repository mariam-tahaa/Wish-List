package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.Helpers.SessionManager;
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

public class LoginController {
    @FXML
    private TextField mail;

    @FXML
    private PasswordField passwordField; // Renamed from 'password'

    @FXML
    private TextField visiblePasswordField; // New TextField for visible password

    @FXML
    private Button loginBtn;

    @FXML
    private Label backArrow;

    @FXML
    private Label eyeIcon;

    @FXML
    private Label generalError;

    @FXML
    private Label mailError;

    @FXML
    private Label passwordError;

    @FXML
    private StackPane passwordContainer; // Container for password fields

    private UserService userService = new UserService();
    private boolean passwordVisible = false;

    @FXML
    private void initialize() {
        // Initialize error labels as invisible
        clearErrors();

        // Set up the visible password field (hidden initially)
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false); // Don't take up layout space

        // Copy style from passwordField to visiblePasswordField
        visiblePasswordField.setStyle(passwordField.getStyle());
        visiblePasswordField.setPrefHeight(passwordField.getPrefHeight());
        visiblePasswordField.setPromptText("Password");
        visiblePasswordField.setFont(passwordField.getFont());

        // Keep both fields in sync
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            visiblePasswordField.setText(newValue);
        });

        visiblePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordField.setText(newValue);
        });

        // Add listeners to clear errors when user types
        mail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearError(mailError);
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearError(passwordError);
            }
        });
    }

    private void clearErrors() {
        clearError(generalError);
        clearError(mailError);
        clearError(passwordError);
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

        if (passwordVisible) {
            // Show password in plain text
            eyeIcon.setText("👁‍🗨"); // Open eye icon
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);

            // Move focus to visible field
            visiblePasswordField.requestFocus();
            visiblePasswordField.positionCaret(visiblePasswordField.getText().length());
        } else {
            // Hide password with dots
            eyeIcon.setText("👁"); // Closed eye icon
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            // Move focus back to password field
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());
        }
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        clearErrors();

        String userMail = mail.getText().trim();
        String userPassword = passwordField.getText(); // Always get from passwordField

        // Basic validation
        boolean hasError = false;

        if (userMail.isEmpty()) {
            showError(mailError, "Email is required");
            hasError = true;
        }

        if (userPassword.isEmpty()) {
            showError(passwordError, "Password is required");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // Call service to validate login
        String result = userService.login(userMail, userPassword);

        if ("SUCCESS".equals(result)) {
            // Get the user object
            User loggedInUser = userService.loginAndGetUser(userMail, userPassword);

            if (loggedInUser != null) {
                // Store user in session
                SessionManager.setCurrentUser(loggedInUser);

                // Show success message briefly
                showSuccess("Login successful! Redirecting...");

                // Delay before redirect (optional)
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        javafx.application.Platform.runLater(() -> {
                            try {
                                App.setRoot("home");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showError(generalError, "Login failed. Please try again.");
            }
        } else {
            // Show appropriate error based on service response
            if (result.contains("email") || result.contains("not found")) {
                showError(mailError, result);
            } else if (result.contains("Password") || result.contains("Incorrect")) {
                showError(passwordError, result);
            } else {
                showError(generalError, result);
            }
        }
    }
}