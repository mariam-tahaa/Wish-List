package com.mycompany.wishlist.Controllers;

import java.io.IOException;
import java.util.List;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.Models.Gift;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.GiftService;
import com.mycompany.wishlist.Helpers.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class WishlistController {

    private GiftService giftService = new GiftService();
    private User currentUser;
    
    @FXML private Label username;

    @FXML
    private VBox wishlistPane;

    // Called when controller is initialized
    @FXML
    private void initialize() {
        // Get current user from session
        currentUser = SessionManager.getCurrentUser();
        username.setText(currentUser.getUserName());

        if (currentUser != null) {
            loadUserGifts();
        } else {
            // If no user is logged in, show message
            Label noUser = new Label("Please login to view your wishlist");
            noUser.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
            wishlistPane.getChildren().add(noUser);
        }
    }

    // Method to load gifts from database
    private void loadUserGifts() {
        if (currentUser == null) return;

        // Clear existing items
        wishlistPane.getChildren().clear();

        // Get gifts from database via service
        List<Gift> gifts = giftService.getUserGifts(currentUser.getUserId());

        // If no gifts, show message
        if (gifts.isEmpty()) {
            Label noItems = new Label("No items in your wishlist yet!");
            noItems.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            wishlistPane.getChildren().add(noItems);
            return;
        }

        // Add each gift to the UI
        for (Gift gift : gifts) {
            VBox giftItem = createGiftItem(gift);
            wishlistPane.getChildren().add(giftItem);
        }

        // Add total count
        Label totalLabel = new Label("Total items: " + gifts.size());
        totalLabel.setStyle("-fx-text-fill: #f5b995; -fx-font-size: 14px; -fx-font-weight: bold;");
        totalLabel.setPadding(new javafx.geometry.Insets(10, 20, 0, 20));
        wishlistPane.getChildren().add(totalLabel);
    }

    // Create a single gift item UI component
    private VBox createGiftItem(Gift gift) {
        VBox itemContainer = new VBox();
        itemContainer.setSpacing(10);
        itemContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 10;");

        // Padding
        itemContainer.setPadding(new javafx.geometry.Insets(15, 20, 15, 20));

        // Main content row
        HBox contentRow = new HBox();
        contentRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Left side: Name, description, and status
        VBox leftColumn = new VBox();
        leftColumn.setSpacing(5);
        leftColumn.setStyle("-fx-pref-width: 400;");

        Label nameLabel = new Label(gift.getGiftName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;");
        leftColumn.getChildren().add(nameLabel);

        // Add description if available
        if (gift.getDescription() != null && !gift.getDescription().trim().isEmpty()) {
            Label descLabel = new Label(gift.getDescription());
            descLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 14px; -fx-wrap-text: true;");
            descLabel.setMaxWidth(380);
            leftColumn.getChildren().add(descLabel);
        }

        Label statusLabel = new Label("Status: " + gift.getStatus());
        statusLabel.setStyle("-fx-text-fill: #f5b995; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 14px;");

        leftColumn.getChildren().add(statusLabel);

        // Right side: Price and delete button
        HBox rightColumn = new HBox();
        rightColumn.setAlignment(javafx.geometry.Pos.CENTER);
        rightColumn.setSpacing(15);

        Label priceLabel = new Label("$" + gift.getPrice());
        priceLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;");

        Label updateLabel = new Label("✎");
        updateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-cursor: hand;");
        updateLabel.setOnMouseClicked(event -> updateGift(gift.getGiftId()));
        
        Label deleteLabel = new Label("🗑");
        deleteLabel.setStyle("-fx-text-fill: #ff4d4d; -fx-font-size: 22px; -fx-cursor: hand;");
        deleteLabel.setOnMouseClicked(event -> deleteGift(gift.getGiftId()));

        rightColumn.getChildren().addAll(priceLabel, updateLabel, deleteLabel);

        // Add columns to content row
        contentRow.getChildren().addAll(leftColumn, rightColumn);

        // Separator line
        Line separator = new Line(0, 0, 480, 0);
        separator.setStroke(javafx.scene.paint.Color.web("#ffffff33"));

        // Add to container
        itemContainer.getChildren().addAll(contentRow, separator);

        return itemContainer;
    }

    private void updateGift(int giftId){
        SessionManager.setGiftId(giftId);
        System.out.println("Update: " + giftId);
        try {
            App.setRoot("updateItem");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void deleteGift(int giftId) {
        if (currentUser == null) return;

        // Create custom dialog
        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Delete Gift");
        dialog.setHeaderText("Are you sure you want to delete this gift?");
        dialog.setContentText("This action cannot be undone.");

        // Apply your app's styling to the dialog
        javafx.scene.control.DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().add("custom-dialog");

        // Set custom styles to match your app's dark theme
        dialogPane.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.9); " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #f5b995; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 2; " +
                        "-fx-padding: 20;"
        );

        // Style the header text
        dialogPane.lookup(".header-panel").setStyle(
                "-fx-background-color: transparent;"
        );

        javafx.scene.control.Label headerLabel = (javafx.scene.control.Label) dialogPane.lookup(".header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle(
                    "-fx-text-fill: white; " +
                            "-fx-font-family: 'Arial Rounded MT Bold'; " +
                            "-fx-font-size: 18px; " +
                            "-fx-font-weight: bold;"
            );
        }

        // Style the content text
        javafx.scene.control.Label contentLabel = (javafx.scene.control.Label) dialogPane.lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setStyle(
                    "-fx-text-fill: #cccccc; " +
                            "-fx-font-family: 'Arial Rounded MT Bold'; " +
                            "-fx-font-size: 14px; " +
                            "-fx-wrap-text: true;"
            );
        }

        // Add custom buttons
        javafx.scene.control.ButtonType okButton = new javafx.scene.control.ButtonType("OK",
                javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        javafx.scene.control.ButtonType cancelButton = new javafx.scene.control.ButtonType("Cancel",
                javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);

        dialogPane.getButtonTypes().addAll(okButton, cancelButton);

        // Style the buttons
        javafx.scene.control.Button okBtn = (javafx.scene.control.Button) dialogPane.lookupButton(okButton);
        okBtn.setStyle(
                "-fx-background-color: #ff4d4d; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Arial Rounded MT Bold'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 8 20; " +
                        "-fx-cursor: hand;"
        );

        javafx.scene.control.Button cancelBtn = (javafx.scene.control.Button) dialogPane.lookupButton(cancelButton);
        cancelBtn.setStyle(
                "-fx-background-color: #333333; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Arial Rounded MT Bold'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 8 20; " +
                        "-fx-cursor: hand;"
        );

        // Add hover effects
        okBtn.setOnMouseEntered(e -> okBtn.setStyle(
                "-fx-background-color: #ff6666; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Arial Rounded MT Bold'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 8 20; " +
                        "-fx-cursor: hand;"
        ));

        okBtn.setOnMouseExited(e -> okBtn.setStyle(
                "-fx-background-color: #ff4d4d; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Arial Rounded MT Bold'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 8 20; " +
                        "-fx-cursor: hand;"
        ));

        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(
                "-fx-background-color: #444444; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Arial Rounded MT Bold'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 8 20; " +
                        "-fx-cursor: hand;"
        ));

        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(
                "-fx-background-color: #333333; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Arial Rounded MT Bold'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 8 20; " +
                        "-fx-cursor: hand;"
        ));

        // Handle the response
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return true;
            }
            return false;
        });

        // Show dialog and handle result
        dialog.showAndWait().ifPresent(result -> {
            if (result) {
                // Call service to delete gift
                String deleteResult = giftService.deleteGift(giftId, currentUser.getUserId());

                if ("SUCCESS".equals(deleteResult)) {
                    // Show custom success alert
                    showCustomAlert("Success", "Gift deleted successfully",
                            AlertType.INFORMATION, Color.web("#4CAF50"));

                    // Reload gifts
                    loadUserGifts();
                } else {
                    // Show custom error alert
                    showCustomAlert("Error", "Failed to delete gift",
                            AlertType.ERROR, Color.web("#ff4d4d"));
                }
            }
        });
    }

    // Helper method for custom alerts
    private void showCustomAlert(String title, String message, AlertType type, Color color) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);

        // Style the alert dialog
        javafx.scene.control.DialogPane alertPane = alert.getDialogPane();
        alertPane.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.9); " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: " + toHexString(color) + "; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 2; " +
                        "-fx-padding: 20;"
        );

        // Style the header text
        alertPane.lookup(".header-panel").setStyle("-fx-background-color: transparent;");

        javafx.scene.control.Label headerLabel = (javafx.scene.control.Label) alertPane.lookup(".header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle(
                    "-fx-text-fill: white; " +
                            "-fx-font-family: 'Arial Rounded MT Bold'; " +
                            "-fx-font-size: 18px; " +
                            "-fx-font-weight: bold;"
            );
        }

        // Style the content area
        javafx.scene.control.Label contentLabel = (javafx.scene.control.Label) alertPane.lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setStyle(
                    "-fx-text-fill: #cccccc; " +
                            "-fx-font-family: 'Arial Rounded MT Bold'; " +
                            "-fx-font-size: 14px; " +
                            "-fx-wrap-text: true;"
            );
        }

        // Style the OK button
        javafx.scene.control.Button okButton = (javafx.scene.control.Button) alertPane.lookupButton(
                javafx.scene.control.ButtonType.OK);
        if (okButton != null) {
            okButton.setStyle(
                    "-fx-background-color: " + toHexString(color) + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-family: 'Arial Rounded MT Bold'; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 5; " +
                            "-fx-padding: 8 20; " +
                            "-fx-cursor: hand;"
            );
        }

        alert.show();
    }

    // Helper method to convert Color to hex string
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // Navigation methods remain the same
    @FXML
    private void goToAddItem(MouseEvent event) throws IOException {
        App.setRoot("addItem");
    }

    @FXML
    private void goToHome(MouseEvent event) throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void goToWishlist(MouseEvent event) throws IOException {
        // Already on wishlist, just reload
        loadUserGifts();
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
        // Clear session
        SessionManager.clearSession();
        App.setRoot("index");
    }
}