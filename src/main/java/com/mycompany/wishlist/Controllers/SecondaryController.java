package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;

import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}