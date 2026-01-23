package com.mycompany.wishlist.Controllers;

import java.io.IOException;
import java.math.BigDecimal;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.Gift;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.GiftService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AddItemController {
    @FXML
    private TextField name;

    @FXML
    private Label nameError;

    @FXML
    private TextField price;

    @FXML
    private Label priceError;

    @FXML
    private TextField description;

    @FXML
    private Label descriptionError;

    @FXML
    private Button addBtn;

    @FXML
    private VBox addPane;

    @FXML
    private Label generalError;

    private GiftService giftService = new GiftService();
    private User currentUser;

    @FXML
    private void initialize() {
        // Get current user from session
        currentUser = SessionManager.getCurrentUser();

        // Set up text change listeners to clear errors
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearError(nameError);
            }
        });

        price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearError(priceError);
            }
        });

        description.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                clearError(descriptionError);
            }
        });

        clearAllErrors();
    }

    private void clearAllErrors() {
        clearError(nameError);
        clearError(priceError);
        clearError(descriptionError);
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

    // Navigation methods
    @FXML
    private void goToHome(MouseEvent event) throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void goToWishlist(MouseEvent event) throws IOException {
        App.setRoot("wishlist");
    }

    @FXML
    private void goToContributions(MouseEvent event) throws IOException {
        App.setRoot("contributions");
    }

    @FXML
    private void goToNotifications(MouseEvent event) throws IOException {
        App.setRoot("notifications");
    }

    @FXML
    private void goToFriends(MouseEvent event) throws IOException {
        App.setRoot("friends");
    }

    @FXML
    private void goToFriendRequests(MouseEvent event) throws IOException {
        App.setRoot("friendRequests");
    }

    @FXML
    private void goToAllUsers(MouseEvent event) throws IOException {
        App.setRoot("allUsers");
    }

    @FXML
    private void logout(MouseEvent event) throws IOException {
        SessionManager.clearSession();
        App.setRoot("index");
    }

    @FXML
    private void add(ActionEvent event) throws IOException {
        clearAllErrors();

        // Check if user is logged in
        if (currentUser == null) {
            showGeneralError("Please login to add items");
            return;
        }

        // Get values from form
        String giftName = name.getText().trim();
        String priceText = price.getText().trim();
        String giftDescription = description.getText().trim();

        // Validate inputs
        boolean hasError = false;

        if (giftName.isEmpty()) {
            showError(nameError, "Gift name is required");
            hasError = true;
        } else if (giftName.length() > 100) {
            showError(nameError, "Gift name cannot exceed 100 characters");
            hasError = true;
        }

        if (priceText.isEmpty()) {
            showError(priceError, "Price is required");
            hasError = true;
        } else {
            try {
                BigDecimal priceValue = new BigDecimal(priceText);
                if (priceValue.compareTo(BigDecimal.ZERO) <= 0) {
                    showError(priceError, "Price must be greater than 0");
                    hasError = true;
                } else if (priceValue.compareTo(new BigDecimal("1000000")) > 0) {
                    showError(priceError, "Price cannot exceed $1,000,000");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                showError(priceError, "Please enter a valid number (e.g., 19.99)");
                hasError = true;
            }
        }

        if (giftDescription.length() > 500) {
            showError(descriptionError, "Description cannot exceed 500 characters");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        try {
            // Create gift object
            Gift newGift = new Gift();
            newGift.setGiftName(giftName);
            newGift.setPrice(new BigDecimal(priceText));
            newGift.setDescription(giftDescription);
            newGift.setStatus("Incomplete");
            newGift.setOwnerUserId(currentUser.getUserId());

            // Call service to add gift
            String result = giftService.addGift(newGift);

            if ("SUCCESS".equals(result)) {
                showSuccess("Gift added successfully! Redirecting...");

                // Delay before redirecting
                new Thread(() -> {
                    try {
                        Thread.sleep(1500); // 1.5 second delay
                        javafx.application.Platform.runLater(() -> {
                            try {
                                App.setRoot("wishlist");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

            } else {
                // Show error from service
                showGeneralError(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showGeneralError("An error occurred. Please try again.");
        }
    }
}