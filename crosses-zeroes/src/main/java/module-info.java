module com.alex {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.alex to javafx.fxml;
    exports com.alex;
    exports com.alex.controllers;
    opens com.alex.controllers;
}