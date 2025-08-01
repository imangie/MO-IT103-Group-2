package org.example.motorphui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Purpose: Launches the main application and handles the initialization of the system.
 * - Handles the startup process and sets up the main user interface (UI).
 * - Manages the application lifecycle.
 */


public class  MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("landing_page.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); //

        stage.setTitle("MotorPH");

        // Set the scene for the stage (window) and show it
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // Launch the JavaFX application
    }
}