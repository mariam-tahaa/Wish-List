package com.mycompany.wishlist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import com.mycompany.wishlist.Helpers.DBConnection;
import com.mycompany.wishlist.Models.User;
import com.mycompany.wishlist.Services.UserService;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("index"), 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/com/mycompany/wishlist/" + fxml + ".fxml")
        );
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
       // test database connection
        try {
            DBConnection.getConnection();
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }


/* 
// test userservice
UserService userService = new UserService();
User user = new User();
user.setUserName("mazen");
user.setMail("mazen@gmail.com");
user.setPass("password123");
boolean registered = userService.register(user, "password123");
System.out.println("User registered: " + registered);   
*/
        launch(args);
    }

}