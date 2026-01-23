package com.mycompany.wishlist.Controllers;

import java.io.IOException;
import java.util.List;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.DAO.FriendshipDAO;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.AllUsersService;
import com.mycompany.wishlist.Services.FriendRequestsService;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AllUsersController {
    @FXML
    private VBox allPane;

    private AllUsersService service = new AllUsersService();
    private FriendRequestsService.FriendRequestService requestService =
        new FriendRequestsService().new FriendRequestService();
    

    public void initialize(){
        User user = new User();
        user.setUserId(2);
        user.setUserName("Ahmed Ali");
        user.setMail("Ahmed@mail.com");
        user.setPass("pass");
        SessionManager.setCurrentUser(user);
        loadAllUsers();
    }

    private void loadAllUsers() {
        allPane.getChildren().clear();

        int currentUserId = SessionManager.getCurrentUser().getUserId();

        FriendshipDAO dao = new FriendshipDAO();
        List<String> users = dao.getAllUsersWithFriendshipStatus(currentUserId);

        for (String row : users) {
            String userIdStr = row.split("ID: ")[1].split(" \\| Username")[0]; // "5"
            String username = row.split("Username: ")[1].split(" \\| Status")[0]; // "Farida"
            String status = row.split("Status: ")[1];

            int userId = Integer.parseInt(userIdStr);

            allPane.getChildren().add(
                createUserRow(userId, username, status)
            );
        }
    }

    private Node createUserRow(int userId, String username, String status) {
        Label name = new Label(username);
        name.setStyle("-fx-text-fill: white; -fx-font-size: 24; -fx-font-weight: bold;");

        VBox nameBox = new VBox(name);
        HBox.setHgrow(nameBox, javafx.scene.layout.Priority.ALWAYS);

        Label action = new Label();
        action.setStyle("-fx-font-size: 22; -fx-cursor: hand;");

        switch (status) {
            case "FRIEND":
                action.setText("unfriend");
                action.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 24; -fx-font-weight: bold; -fx-cursor: hand;");
                action.setOnMouseClicked(e -> unfriendUser(userId));
                break;

            case "REQUEST_SENT":
                action.setText("Pending");
                action.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 24; -fx-font-weight: bold; -fx-cursor: hand;");
                break;

            case "REQUEST_RECEIVED":
                action.setText("Accept");
                action.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 24; -fx-font-weight: bold; -fx-cursor: hand;");
                action.setOnMouseClicked(e -> sendRequest(userId));
                break;

            default:
                action.setText("+ Add Friend");
                action.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 24; -fx-font-weight: bold; -fx-cursor: hand;");
                action.setOnMouseClicked(e -> addfriendUser(userId));
        }

        HBox row = new HBox(nameBox, action);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.setSpacing(20);

        VBox card = new VBox(row, new javafx.scene.shape.Line(480, 0, 0, 0));
        card.setPadding(new javafx.geometry.Insets(15, 20, 15, 20));

        return card;
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
    private void addFriend(MouseEvent event) {
        System.out.println("Add Friend");
    }

    private void unfriendUser(int userId){
        service.unfriend(userId);
        initialize();
    }

    private void sendRequest(int userId){
        requestService.acceptRequest(userId);
        initialize();
    }

    private void addfriendUser(int userId){
        service.sendFriendRequest(userId);
        initialize();
    }
}
