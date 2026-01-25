package com.mycompany.wishlist.Controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.mycompany.wishlist.App;
import com.mycompany.wishlist.Helpers.SessionManager;
import com.mycompany.wishlist.Models.Contribution;
import com.mycompany.wishlist.Models.Gift;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.ContributionService;
import com.mycompany.wishlist.Services.FriendsService;
import com.mycompany.wishlist.Services.GiftService;
import com.mycompany.wishlist.Services.UserService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class FriendPageController {

    private GiftService giftService = new GiftService();
    private ContributionService contributionService = new ContributionService();
    private UserService userService = new UserService();
    private User currentUser;
    private User friendUser; // The friend whose page we're viewing
    private FriendsService friendsService = new FriendsService();

    @FXML
    private VBox giftsPane;

    @FXML
    private Label friendName;

    @FXML
    private Label username;

    @FXML
    private void initialize() {
        // Get current user from session
        currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            // Display current username
            username.setText(currentUser.getUserName());

            // Get friend ID from session or navigation parameters
            // You'll need to pass the friend ID when navigating to this page
            Integer friendId = SessionManager.getFriendId();

            if (friendId != null) {
                loadFriendPage(friendId);
            } else {
                // Show error if no friend selected
                showErrorMessage("No friend selected", "Please select a friend from the friends list.");
            }
        } else {
            // If no user is logged in
            showErrorMessage("Not logged in", "Please login to view friend's wishlist.");
        }
    }

    private void loadFriendPage(int friendId) {
        // Clear existing items
        giftsPane.getChildren().clear();

        // Get friend user details
        friendUser = friendsService.getUserById(friendId);

        if (friendUser == null) {
            showErrorMessage("Friend not found", "The selected friend could not be found.");
            return;
        }

        // Set friend name
        friendName.setText(friendUser.getUserName() + "'s Wishlist");

        // Get friend's gifts
        List<Gift> friendGifts = giftService.getUserGifts(friendId);

        if (friendGifts.isEmpty()) {
            Label noItems = new Label(friendUser.getUserName() + " has no items in their wishlist yet!");
            noItems.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            noItems.setPadding(new Insets(20));
            giftsPane.getChildren().add(noItems);
            return;
        }

        // Add each gift to the UI
        for (Gift gift : friendGifts) {
            if (!"Completed".equalsIgnoreCase(gift.getStatus())) {
                VBox giftItem = createGiftItem(gift);
                giftsPane.getChildren().add(giftItem);
            }
        }
    }

    // Create a single gift item UI component
    private VBox createGiftItem(Gift gift) {
        VBox itemContainer = new VBox();
        itemContainer.setSpacing(10);
        itemContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-background-radius: 10;");
        itemContainer.setPadding(new Insets(15, 20, 15, 20));

        // Main content row
        HBox contentRow = new HBox();
        contentRow.setAlignment(Pos.CENTER_LEFT);

        // Left side: Name, description, and status
        VBox leftColumn = new VBox();
        leftColumn.setSpacing(5);
        leftColumn.setStyle("-fx-pref-width: 350;");

        Label nameLabel = new Label(gift.getGiftName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 20px;");
        leftColumn.getChildren().add(nameLabel);

        // Add description if available
        if (gift.getDescription() != null && !gift.getDescription().trim().isEmpty()) {
            Label descLabel = new Label(gift.getDescription());
            descLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 14px; -fx-wrap-text: true;");
            descLabel.setMaxWidth(330);
            leftColumn.getChildren().add(descLabel);
        }

        Label statusLabel = new Label("Status: " + gift.getStatus());
        statusLabel.setStyle("-fx-text-fill: #f5b995; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 14px;");
        leftColumn.getChildren().add(statusLabel);

        // Calculate remaining amount
        BigDecimal totalContributed = BigDecimal.ZERO;
        List<Contribution> contributions = contributionService.getContributionsByGiftId(gift.getGiftId());
        for (Contribution c : contributions) {
            totalContributed = totalContributed.add(c.getPercentage());
        }

        BigDecimal percentageContributed = totalContributed;
        BigDecimal amountContributed = gift.getPrice().multiply(percentageContributed.divide(new BigDecimal("100")));
        BigDecimal remainingAmount = gift.getPrice().subtract(amountContributed);

        // Input and button section
        VBox inputSection = new VBox();
        inputSection.setSpacing(10);
        inputSection.setPadding(new Insets(10, 0, 0, 0));

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount ($)");
        amountField.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 14px;");
        amountField.setPrefHeight(35);

        Button contributeButton = new Button("Contribute");
        contributeButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-size: 14px;");
        contributeButton.setPrefHeight(35);
        contributeButton.setPrefWidth(120);

        // Set button action
        contributeButton.setOnAction(event -> handleContribution(gift, amountField.getText()));

        HBox inputRow = new HBox(15, amountField, contributeButton);
        inputRow.setAlignment(Pos.CENTER_LEFT);

        inputSection.getChildren().addAll(inputRow);
        leftColumn.getChildren().add(inputSection);

        // Right side: Price and remaining
        VBox rightColumn = new VBox();
        rightColumn.setAlignment(Pos.CENTER);
        rightColumn.setSpacing(5);
        rightColumn.setPrefWidth(100);

        Label priceLabel = new Label("$" + gift.getPrice());
        priceLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 18px;");

        Label remainingLabel = new Label("Remaining");
        remainingLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 14px;");

        Label remainingAmountLabel = new Label("$" + remainingAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        remainingAmountLabel.setStyle("-fx-text-fill: #f5b995; -fx-font-family: 'Arial Rounded MT Bold'; -fx-font-size: 18px; -fx-font-weight: bold;");

        rightColumn.getChildren().addAll(priceLabel, remainingLabel, remainingAmountLabel);

        // Add columns to content row
        contentRow.getChildren().addAll(leftColumn, rightColumn);

        // Separator line
        Line separator = new Line(0, 0, 480, 0);
        separator.setStroke(javafx.scene.paint.Color.web("#ffffff33"));

        // Add to container
        itemContainer.getChildren().addAll(contentRow, separator);

        return itemContainer;
    }

    private void handleContribution(Gift gift, String amountStr) {
        if (currentUser == null || friendUser == null) return;

        try {
            // Validate input
            if (amountStr == null || amountStr.trim().isEmpty()) {
                showErrorMessage("Invalid Amount", "Please enter a valid amount.");
                return;
            }

            BigDecimal amount = new BigDecimal(amountStr.trim());

            // Validate amount
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showErrorMessage("Invalid Amount", "Amount must be greater than 0.");
                return;
            }

            // Calculate percentage
            BigDecimal price = gift.getPrice();
            BigDecimal percentage = amount.divide(price, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));

            // Check if gift is already completed
            if ("Completed".equalsIgnoreCase(gift.getStatus())) {
                showErrorMessage("Gift Completed", "This gift has already been fully funded.");
                return;
            }

            // Check existing contributions
            List<Contribution> existingContributions = contributionService.getContributionsByGiftId(gift.getGiftId());
            BigDecimal currentTotal = BigDecimal.ZERO;
            for (Contribution c : existingContributions) {
                currentTotal = currentTotal.add(c.getPercentage());
            }

            BigDecimal newTotal = currentTotal.add(percentage);

            if (newTotal.compareTo(new BigDecimal("100")) > 0) {
                // Calculate maximum possible contribution
                BigDecimal maxPercentage = new BigDecimal("100").subtract(currentTotal);
                BigDecimal maxAmount = price.multiply(maxPercentage.divide(new BigDecimal("100")));
                showErrorMessage("Over Contribution",
                        "Maximum you can contribute is $" + maxAmount.setScale(2, BigDecimal.ROUND_HALF_UP) +
                                " (" + maxPercentage.setScale(2, BigDecimal.ROUND_HALF_UP) + "%)");
                return;
            }

            // Create contribution
            Contribution contribution = new Contribution();
            contribution.setGiftId(gift.getGiftId());
            contribution.setContributorId(currentUser.getUserId());
            contribution.setPercentage(percentage);

            // Add contribution via service
            boolean success = contributionService.addContribution(contribution);

            if (success) {
                showSuccessMessage("Contribution Successful",
                        "You contributed $" + amount.setScale(2, BigDecimal.ROUND_HALF_UP) +
                                " (" + percentage.setScale(2, BigDecimal.ROUND_HALF_UP) + "%) to " + gift.getGiftName());

                // Reload the page to show updated amounts
                loadFriendPage(friendUser.getUserId());
            } else {
                showErrorMessage("Contribution Failed", "Unable to process your contribution. Please try again.");
            }

        } catch (NumberFormatException e) {
            showErrorMessage("Invalid Amount", "Please enter a valid numeric amount.");
        } catch (Exception e) {
            showErrorMessage("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.show();
    }

    private void showSuccessMessage(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.show();
    }

    @FXML
    private void contribute(ActionEvent event) {
        // This method can be removed since we're handling contributions in handleContribution
        System.out.println("Contribute button clicked");
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
        // Clear session
        SessionManager.clearSession();
        App.setRoot("index");
    }
}