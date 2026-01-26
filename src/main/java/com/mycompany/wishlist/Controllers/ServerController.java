package com.mycompany.wishlist.Controllers;

import java.io.IOException;

import com.mycompany.wishlist.App;

import javafx.fxml.FXML;

public class ServerController {

    @FXML
    private void startServer() {
        System.out.println("Server started");
    }

    @FXML
    private void stopServer() {
        System.out.println("Server stopped");
}
}
