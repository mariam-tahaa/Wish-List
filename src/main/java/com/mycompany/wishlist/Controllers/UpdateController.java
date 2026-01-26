package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.Gift;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.GiftService;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UpdateController {

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
    private Button updBtn;

    @FXML
    private VBox addPane;

    @FXML
    private Label generalError;

    @FXML private Label username;
    
    private GiftService giftService = new GiftService();
    private User currentUser;
    private Gift gift;
    @FXML
    private void initialize() {
        gift = giftService.getGiftById(SessionManager.getGiftId());
        name.setText(gift.getGiftName());
        price.setText(gift.getPrice().toString());
        description.setText(gift.getDescription());
        name.setEditable(false);
        price.setEditable(false);
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
    private void update(ActionEvent event) throws IOException {
        gift.setDescription(description.getText());
        giftService.updateGift(gift);
        App.setRoot("wishlist");
    }
}