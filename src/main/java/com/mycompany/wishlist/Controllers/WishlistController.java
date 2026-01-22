package com.mycompany.wishlist.Controllers;

import java.io.IOException;
import java.util.List;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.DAO.GiftDAO;
import com.mycompany.wishlist.Models.Gift;
import com.mycompany.wishlist.Models.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class WishlistController {

    // Store current user (you'll need to pass this from login)
    private User currentUser;

    @FXML
    private VBox wishlistPane;

    @FXML
    private ScrollPane scrollPane;

    // Constructor or setter for current user
    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadUserGifts(); // Load gifts when user is set
    }

    // Method to load gifts from database
    private void loadUserGifts() {
        if (currentUser == null) return;

        // Clear existing items
        wishlistPane.getChildren().clear();

        // Get gifts from database
        GiftDAO giftDAO = new GiftDAO();
        List<Gift> gifts = giftDAO.getGiftsByUser(currentUser.getUserId());

        // If no gifts, show message
        if (gifts.isEmpty()) {
            Label noItems = new Label("No items in your wishlist yet!");
            noItems.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            wishlistPane.getChildren().add(noItems);
            return;
        }

        // Add each gift to the UI
        for (Gift gift : gifts) {
            VBox giftItem = createGiftItem(gift);
            wishlistPane.getChildren().add(giftItem);
        }
    }

    // Create a single gift item UI component
    private VBox createGiftItem(Gift gift) {
        VBox itemContainer = new VBox();
        itemContainer.setSpacing(10);
        itemContainer.setStyle("-fx-background-color: transparent;");

        // Padding
        itemContainer.setPadding(new javafx.geometry.Insets(15, 20, 15, 20));

        // Main content row
        HBox contentRow = new HBox();
        contentRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Left side: Name and description
        VBox leftColumn = new VBox();
        leftColumn.setSpacing(5);
        leftColumn.setStyle("-fx-pref-width: 400;");

        Label nameLabel = new Label(gift.getGiftName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;");

        // Optional: Add description if you have it in your model
//        Label descLabel = new Label(getGiftDescription(gift));
//        descLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 14px;");

//        leftColumn.getChildren().addAll(nameLabel, descLabel);

        // Right side: Price and delete button
        HBox rightColumn = new HBox();
        rightColumn.setAlignment(javafx.geometry.Pos.CENTER);
        rightColumn.setSpacing(15);

        Label priceLabel = new Label("$" + gift.getPrice());
        priceLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;");

        Label deleteLabel = new Label("🗑");
        deleteLabel.setStyle("-fx-text-fill: #ff4d4d; -fx-font-size: 22px; -fx-cursor: hand;");
        deleteLabel.setOnMouseClicked(event -> deleteGift(gift.getGiftId()));

        rightColumn.getChildren().addAll(priceLabel, deleteLabel);

        // Add columns to content row
        contentRow.getChildren().addAll(leftColumn, rightColumn);

        // Separator line
        Line separator = new Line(0, 0, 480, 0);
        separator.setStroke(javafx.scene.paint.Color.web("#ffffff33"));

        // Add to container
        itemContainer.getChildren().addAll(contentRow, separator);

        return itemContainer;
    }
    private String getGiftDescription(Gift gift) {
        // You can modify this based on your Gift model
        // For example, you might want to show status or other info
        return "Status: " + gift.getStatus();
    }

    private void deleteGift(int giftId) {
        System.out.println("Deleting gift ID: " + giftId);
        // TODO: Implement delete functionality in GiftDAO
        // After deleting, reload the gifts
        loadUserGifts();
    }

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
        App.setRoot("index");
    }

    @FXML
    private void deleteItem(MouseEvent event){
        System.out.println("Delete clicked");
    }
}