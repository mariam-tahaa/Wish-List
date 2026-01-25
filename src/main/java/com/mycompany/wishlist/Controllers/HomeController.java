package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.Contribution;
import com.mycompany.wishlist.Models.Gift;
import com.mycompany.wishlist.Models.Notification;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.ContributionService;
import com.mycompany.wishlist.Services.FriendsService;
import com.mycompany.wishlist.Services.GiftService;
import com.mycompany.wishlist.Services.NotificationService;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class HomeController {
    @FXML private Label numFriends;
    @FXML private Label numWishlists;
    @FXML private Label numNotifications;
    @FXML private Label numContributions;
    @FXML private Label username;
    private FriendsService friendsService = new FriendsService();
    private GiftService giftService = new GiftService();
    private NotificationService notiService = new NotificationService();
    private ContributionService contService = new ContributionService();

    @FXML
    private void initialize() {
        // Example dynamic values
        User currentUser = SessionManager.getCurrentUser();
        username.setText(currentUser.getUserName());

        // Get friends count        
        try {
            List<Integer> friendIds = friendsService.getAllFriends();
            int friendsCount = 0;
            if (friendIds != null) {
                friendsCount = friendIds.size();
            }
            numFriends.setText(friendsCount + " Friends");
        } catch (Exception e) {
            System.err.println("Error loading Friends: " + e.getMessage());
            numFriends.setText("0 Friends");
        }

        // Get gifts count
        try {
            List<Gift> gifts = giftService.getUserGifts(currentUser.getUserId());
            int giftsCount = 0;
            if (gifts != null) {
                giftsCount = gifts.size();
            }
            numWishlists.setText(giftsCount + " Wish Items");
        } catch (Exception e) {
            System.err.println("Error loading wish items: " + e.getMessage());
            numNotifications.setText("0 Wish Items");
        }

        // Get notifications count - WITH NULL CHECK
        try {
            List<Notification> notifications = notiService.getAllNotifications();
            int notiCount = 0;
            if (notifications != null) {
                notiCount = notifications.size();
            }
            numNotifications.setText(notiCount + " Notifications");
        } catch (Exception e) {
            System.err.println("Error loading notifications: " + e.getMessage());
            numNotifications.setText("0 Notifications"); // Default to 0 on error
        }

        // get contributions count
        try {
            List<Contribution> contributions = contService.getFullUserContributions(currentUser.getUserId());
            int contCount = 0;
            if(contributions != null) {
                contCount = contributions.size();
            }
            numContributions.setText(contCount+" Contributions");
        }catch (Exception e){
            System.err.println("Error loading contributions: " + e.getMessage());
            numContributions.setText("0 Contributions");
        }
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