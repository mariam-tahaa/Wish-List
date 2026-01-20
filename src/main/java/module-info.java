module com.mycompany.wishlist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.mycompany.wishlist.Controllers to javafx.fxml;
    exports com.mycompany.wishlist;
}
