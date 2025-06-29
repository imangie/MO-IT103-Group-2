module org.example.motorphui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;

    opens org.example.motorphui to javafx.fxml;
    exports org.example.motorphui;
}