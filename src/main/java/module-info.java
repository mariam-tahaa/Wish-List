module com.mycompany.wishlist {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.wishlist to javafx.fxml;
    exports com.mycompany.wishlist;
}
