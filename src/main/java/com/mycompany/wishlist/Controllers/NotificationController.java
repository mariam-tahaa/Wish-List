package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.Notification;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.NotificationService;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class NotificationController {
    @FXML
    private VBox NotificationPane;
    @FXML private Label username;
    
    private NotificationService service = new NotificationService();
    private User currentUser;
    
    @FXML
    public void initialize() {
        currentUser = SessionManager.getCurrentUser();
        username.setText(currentUser.getUserName());
        loadNotifications();
    }

    private void loadNotifications(){
        List<Notification> notifications = service.getAllNotifications();

        NotificationPane.getChildren().clear();

        if (notifications == null || notifications.isEmpty()) {
            Label empty = new Label("No notifications yet");
            empty.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            NotificationPane.getChildren().add(empty);
            return;
        }

        for (Notification n : notifications) {
            NotificationPane.getChildren().add(createNotificationItem(n));
        }
    }
    
    private VBox createNotificationItem(Notification notification) {
        String notificationText = notification.getContent();

        VBox textBox = new VBox(5); // will hold name + message (or just message)

        Label message;

        if (notificationText.contains(" contributed ")) {

            int index = notificationText.indexOf(" contributed ");

            String name = notificationText.substring(0, index);
            String content = notificationText.substring(index + 1);

            Label nameLabel = new Label(name);
            nameLabel.setStyle(
                "-fx-text-fill: white; " +
                "-fx-font-size: 20px; " +
                "-fx-font-weight: bold;"
            );

            message = new Label(content);
            message.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
            message.setWrapText(true);

            textBox.getChildren().addAll(nameLabel, message);

        } else {

            message = new Label(notificationText);
            message.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
            message.setWrapText(true);

            textBox.getChildren().add(message);
        }

        HBox.setHgrow(textBox, javafx.scene.layout.Priority.ALWAYS);
        textBox.setMaxWidth(380);

        Label readBtn = new Label("✔");
        readBtn.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-cursor: hand;");

        readBtn.setMinWidth(40);
        readBtn.setPrefWidth(40);
        readBtn.setAlignment(Pos.CENTER);

        readBtn.setOnMouseClicked(e -> {
            read(notification.getNotId());
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(10, textBox, spacer, readBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Line divider = new Line();
        divider.setEndX(480);
        divider.setStyle("-fx-stroke: #ffffff33;");

        VBox card = new VBox(10, row, divider);
        card.setPadding(new Insets(15, 20, 15, 20));

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
    private void markRead(MouseEvent event){
        System.out.println("Read clicked");
    }
    
    private void read(int notificationId){
        service.markNotificationAsRead(notificationId);
        loadNotifications();
    }


}
