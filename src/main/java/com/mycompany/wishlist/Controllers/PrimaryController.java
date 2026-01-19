package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
