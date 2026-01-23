package com.mycompany.wishlist.Controllers;

import java.io.IOException;
import java.util.List;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.DAO.UserDAO;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.FreindsService;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class FriendsController {
    @FXML
    private VBox friendsPane;

    private FreindsService friendsService = new FreindsService();

    private UserDAO userDao = new UserDAO();

    @FXML
    private void initialize() {
        User user = new User();
        user.setUserId(2);
        user.setUserName("Ahmed Ali");
        user.setMail("Ahmed@mail.com");
        user.setPass("pass");
        SessionManager.setCurrentUser(user);
        loadFriends();
    }

    private void loadFriends() {
        friendsPane.getChildren().clear(); 

        List<Integer> friendIds = friendsService.getAllFriends();

        if (friendIds.isEmpty()) {
                Label empty = new Label("No friend requests");
                empty.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Arial Rounded MT Bold';");
                friendsPane.getChildren().add(empty);
                return;
            }

        for (Integer friendId : friendIds) {
            HBox friendBox = createFriendBox(friendId);

            VBox friendWrapper = new VBox(friendBox);
            friendWrapper.setSpacing(10); 
            friendWrapper.setPadding(new Insets(10, 20, 10, 20)); 

            Line separator = new Line(0, 0, 480, 0);
            separator.setStyle("-fx-stroke: #ffffff33;");

            friendWrapper.getChildren().add(separator); 
            friendsPane.getChildren().add(friendWrapper);
        }
    }

    private HBox createFriendBox(Integer friendId) {
        HBox hbox = new HBox();
        hbox.setSpacing(20); 
        hbox.setStyle("-fx-alignment: CENTER_LEFT;");

        User friend = userDao.getUserById(friendId);
        Label nameLabel = new Label(friend.getUserName()); 
        nameLabel.setStyle("-fx-text-fill: WHITE; -fx-font-size: 24px; -fx-font-family: 'Arial Rounded MT Bold';");
        nameLabel.setOnMouseClicked(event -> goToFriendPage(event, friendId));
        nameLabel.setCursor(javafx.scene.Cursor.HAND);

        Label unfriendLabel = new Label("unfriend");
        unfriendLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 22px; -fx-font-family: 'Arial Rounded MT Bold';");
        unfriendLabel.setOnMouseClicked(event -> unfriend(event, friendId));
        unfriendLabel.setCursor(javafx.scene.Cursor.HAND);

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        hbox.getChildren().addAll(nameLabel, spacer, unfriendLabel);

        return hbox;
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
    private void unfriend(MouseEvent event) {
        System.out.println("Unfriend");
    }

    @FXML
    private void goToFriendPage(MouseEvent event, int friendId) {
        System.out.println("Go to Friend Page: " + friendId);
        try {
            App.setRoot("friendPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void unfriend(MouseEvent event, int friendId) {
        boolean success = friendsService.unfriend(friendId);
        if (success) loadFriends(); // Reload the list
    }
}
