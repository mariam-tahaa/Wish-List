package com.mycompany.wishlist.Controllers;

import java.io.IOException;
import java.util.List;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.DAO.UserDAO;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.FriendRequest;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.FriendRequestsService;

import com.mycompany.wishlist.Services.FriendsService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FriendRequestsController {
    @FXML
    private VBox requestsPane;

    @FXML private Label username;
    
    private FriendRequestsService.FriendRequestService service =
        new FriendRequestsService().new FriendRequestService();

    private FriendsService friendsService = new FriendsService();
    private User currentUser;
    
    public void initialize(){
        currentUser = SessionManager.getCurrentUser();
        username.setText(currentUser.getUserName());
        loadFriendRequests();
    }

    private void loadFriendRequests() {
        try {
            requestsPane.getChildren().clear();
            List<FriendRequest> requests = service.getPendingFriendRequests();

            if (requests.isEmpty()) {
                Label empty = new Label("No friend requests");
                empty.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-family: 'Arial Rounded MT Bold';");
                requestsPane.getChildren().add(empty);
                return;
            }

            for (FriendRequest req : requests) {
                requestsPane.getChildren().add(createRequestCard(req));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Label error = new Label("Error loading friend requests");
            error.setStyle("-fx-text-fill: red;");
            requestsPane.getChildren().add(error);
        }
    }

    private VBox createRequestCard(FriendRequest request) {
        User reqUser = friendsService.getUserById(request.getSenderId());
        Label name = new Label(reqUser.getUserName());
        name.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold;");

        Label msg = new Label("Sent you a friend request...");
        msg.setStyle("-fx-text-fill: white;");

        VBox textBox = new VBox(name, msg);
        textBox.setSpacing(4);

        Label accept = new Label("✔");
        accept.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 26; -fx-cursor: hand;");
        accept.setOnMouseClicked(e -> acceptRequest(request.getReqId()));

        Label decline = new Label("✘");
        decline.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 26; -fx-cursor: hand;");
        decline.setOnMouseClicked(e -> declineRequest(request.getReqId()));

        HBox actions = new HBox(20, accept, decline);
        actions.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        HBox.setHgrow(textBox, javafx.scene.layout.Priority.ALWAYS);

        HBox row = new HBox(textBox, actions);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.setSpacing(20);
        row.setPrefWidth(480);

        VBox card = new VBox(row, new javafx.scene.shape.Line(480, 0, 0, 0));
        card.setStyle("-fx-padding: 15;");

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
    private void accept(MouseEvent event) {
        System.out.println("accepted");
    }

    @FXML
    private void decline(MouseEvent event) {
        System.out.println("Declined");
    }

    private void acceptRequest(int requestId) {
        if (service.acceptRequest(requestId)) {
            loadFriendRequests();
        }
    }

    private void declineRequest(int requestId) {
        if (service.declineRequest(requestId)) {
            loadFriendRequests();
        }
    }
}
