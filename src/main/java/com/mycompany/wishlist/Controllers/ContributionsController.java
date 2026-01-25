package com.mycompany.wishlist.Controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.Contribution;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.ContributionService;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ContributionsController {

    private ContributionService contributionService = new ContributionService();
    private User currentUser;

    @FXML
    private VBox contPane;

    @FXML
    private Label username;

    @FXML
    private void initialize() {
        // Get current user from session
        currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            // Display username
            username.setText(currentUser.getUserName());

            // Load contributions
            loadUserContributions();
        } else {
            // If no user is logged in, show message
            Label noUser = new Label("Please login to view your contributions");
            noUser.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
            contPane.getChildren().clear();
            contPane.getChildren().add(noUser);
        }
    }

    // Method to load contributions from database
    private void loadUserContributions() {
        if (currentUser == null) return;

        // Clear existing items (keep the header row)
        contPane.getChildren().clear();

        // Add header row
        HBox headerRow = createHeaderRow();
        contPane.getChildren().add(headerRow);

        // Get contributions from database via service
        List<Contribution> contributions = contributionService.getFullUserContributions(currentUser.getUserId());

        // If no contributions, show message
        if (contributions.isEmpty()) {
            Label noItems = new Label("No contributions yet!");
            noItems.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            contPane.getChildren().add(noItems);
            return;
        }

        // Add each contribution to the UI
        for (Contribution contribution : contributions) {
            HBox contributionRow = createContributionRow(contribution);
            contPane.getChildren().add(contributionRow);
        }

        // Calculate and add total contributions
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Contribution contribution : contributions) {
            if (contribution.getGift() != null) {
                BigDecimal percentage = contribution.getPercentage();
                BigDecimal giftPrice = contribution.getGift().getPrice();
                BigDecimal amount = giftPrice.multiply(percentage.divide(new BigDecimal("100")));
                totalAmount = totalAmount.add(amount);
            }
        }

        // Add total row
        HBox totalRow = createTotalRow(totalAmount);
        contPane.getChildren().add(totalRow);
    }

    // Create header row
    private HBox createHeaderRow() {
        HBox headerRow = new HBox();
        headerRow.setSpacing(10.0);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label friendLabel = new Label("Friend");
        friendLabel.setPrefWidth(120.0);
        friendLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        friendLabel.setFont(Font.font("Arial Rounded MT Bold", 20.0));

        Label itemLabel = new Label("Item");
        itemLabel.setPrefWidth(120.0);
        itemLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        itemLabel.setFont(Font.font("Arial Rounded MT Bold", 20.0));

        Label percentageLabel = new Label("Percentage");
        percentageLabel.setPrefWidth(130.0);
        percentageLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        percentageLabel.setFont(Font.font("Arial Rounded MT Bold", 20.0));

        Label amountLabel = new Label("Amount");
        amountLabel.setPrefWidth(100.0);
        amountLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        amountLabel.setFont(Font.font("Arial Rounded MT Bold", 20.0));

        headerRow.getChildren().addAll(friendLabel, itemLabel, percentageLabel, amountLabel);

        return headerRow;
    }

    // Create a single contribution row
    private HBox createContributionRow(Contribution contribution) {
        HBox row = new HBox();
        row.setSpacing(10.0);
        row.setAlignment(Pos.CENTER_LEFT);

        // Friend name
        String friendName = "Unknown";
        if (contribution.getFriend() != null) {
            friendName = contribution.getFriend().getUserName();
        } else if (contribution.getGift() != null) {
            friendName = "User #" + contribution.getGift().getOwnerUserId();
        }

        Label friendLabel = new Label(friendName);
        friendLabel.setPrefWidth(120.0);
        friendLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        friendLabel.setFont(Font.font("Arial Rounded MT Bold", 18.0));

        // Item name
        String itemName = "Unknown";
        if (contribution.getGift() != null) {
            itemName = contribution.getGift().getGiftName();
        }

        Label itemLabel = new Label(itemName);
        itemLabel.setPrefWidth(120.0);
        itemLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        itemLabel.setFont(Font.font("Arial Rounded MT Bold", 18.0));

        // Percentage
        BigDecimal percentage = contribution.getPercentage();
        Label percentageLabel = new Label(percentage + "%");
        percentageLabel.setPrefWidth(130.0);
        percentageLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        percentageLabel.setFont(Font.font("Arial Rounded MT Bold", 18.0));

        // Calculate amount
        BigDecimal amount = BigDecimal.ZERO;
        if (contribution.getGift() != null) {
            BigDecimal giftPrice = contribution.getGift().getPrice();
            amount = giftPrice.multiply(percentage.divide(new BigDecimal("100")));
        }

        Label amountLabel = new Label("$" + amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        amountLabel.setPrefWidth(100.0);
        amountLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        amountLabel.setFont(Font.font("Arial Rounded MT Bold", 18.0));

        row.getChildren().addAll(friendLabel, itemLabel, percentageLabel, amountLabel);

        return row;
    }

    // Create total row
    private HBox createTotalRow(BigDecimal totalAmount) {
        HBox totalRow = new HBox();
        totalRow.setSpacing(10.0);
        totalRow.setAlignment(Pos.CENTER_LEFT);
        totalRow.setPadding(new Insets(20, 0, 0, 0));

        // Empty labels for alignment
        Label empty1 = new Label("");
        empty1.setPrefWidth(120.0);

        Label empty2 = new Label("");
        empty2.setPrefWidth(120.0);

        Label totalText = new Label("Total:");
        totalText.setPrefWidth(130.0);
        totalText.setTextFill(javafx.scene.paint.Color.web("#f5b995"));
        totalText.setFont(Font.font("Arial Rounded MT Bold", 18.0));

        Label totalAmountLabel = new Label("$" + totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        totalAmountLabel.setPrefWidth(100.0);
        totalAmountLabel.setTextFill(javafx.scene.paint.Color.web("#f5b995"));
        totalAmountLabel.setFont(Font.font("Arial Rounded MT Bold", 18.0));
        totalAmountLabel.setStyle("-fx-font-weight: bold;");

        totalRow.getChildren().addAll(empty1, empty2, totalText, totalAmountLabel);

        return totalRow;
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
        // Already on contributions page, just reload
        loadUserContributions();
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