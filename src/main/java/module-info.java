module org.example.motorphui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;

    opens org.example.motorphui to javafx.fxml;
    exports org.example.motorphui;
}