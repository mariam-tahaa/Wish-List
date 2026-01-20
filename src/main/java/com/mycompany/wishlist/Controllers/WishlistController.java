package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class WishlistController {

    @FXML
    private VBox wishlistPane;

    @FXML
    private void goToAddItem(MouseEvent event) {
        System.out.println("Go to Add Item screen");
        // App.setRoot("add_item");
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
    private void goToContributions(MouseEvent event) {
        System.out.println("Contributions clicked");
    }

    @FXML
    private void goToNotifications(MouseEvent event) {
        System.out.println("Notifications clicked");
    }

    @FXML
    private void goToFriends(MouseEvent event) {
        System.out.println("Friends clicked");
    }

    @FXML
    private void goToFriendRequests(MouseEvent event) {
        System.out.println("Friend Requests clicked");
    }

    @FXML
    private void goToAllUsers(MouseEvent event) {
        System.out.println("All Users clicked");
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
