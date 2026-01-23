package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FriendPagaController {
    @FXML
    private TextField amount;

    @FXML
    private void contribute(ActionEvent event) {
        System.out.println("Contribute");
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
}
