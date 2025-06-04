package org.example.motorphui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EmployeeLogin {
    @FXML
    private TextField employeeid_field;

    @FXML
    private TextField username_field;

    @FXML
    private Button login_button;

    @FXML
    private Label back_label;

    @FXML
    private PasswordField password_field;

    @FXML
    private TextField visible_password_field;

    @FXML
    private CheckBox show_password_check;

    //@FXML
    //private Label errorMessage;

    @FXML
    private void initialize() {
        password_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!visible_password_field.isFocused()) {
                visible_password_field.setText(newValue);
            }
        });

        visible_password_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!password_field.isFocused()) {
                password_field.setText(newValue);
            }
        });

        // toggle visibility of password field
        show_password_check.setOnAction(event -> {
            boolean show = show_password_check.isSelected();
            visible_password_field.setVisible(show);
            visible_password_field.setManaged(show);
            password_field.setVisible(!show);
            password_field.setManaged(!show);
        });
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String empId = employeeid_field.getText();
        String username = username_field.getText();
        String password = password_field.getText();

        if (empId.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "All fields are required.");
            return;
        }

        // Authenticate user
        if (Authentication.authenticate(empId, username, password)) {
            Employee employee = Authentication.getEmployeeData(empId);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("employee_dashboard.fxml"));
                Parent root = loader.load();

                EmployeeDashboard dashboardController = loader.getController();
                dashboardController.loadProfile(employee);

                Stage stage = (Stage) login_button.getScene().getWindow();
                Scene scene = new Scene(root);

                stage.setMinWidth(1440);
                stage.setMinHeight(1024);

                stage.setWidth(1440);
                stage.setHeight(1024);

                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error loading profile screen.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("landing_page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) back_label.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
