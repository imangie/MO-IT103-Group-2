package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Purpose: Displays the HR dashboard after login.
 * - Allows HR to navigate between different sections (Employee Management, Attendance, Payroll).
 * - Provides functionality to log out of the system.
 */


public class Dashboard {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private Button updateEmpInfo, employeeList;
    @FXML
    private Button logout_button;

    /**
     * Initializes the dashboard by loading the default employee list view upon startup.
     */
    @FXML
    public void initialize() {
        loadView("employee_list.fxml");
        setActiveButton(employeeList);
    }

    /**
     * Loads the employee information update view when the button is clicked.
     */
    @FXML
    private void onEmployeesClicked() {
        loadView("update_employee_information_window.fxml");
        setActiveButton(updateEmpInfo);
    }

    /**
     * Loads the employee list view for payroll purposes when the calculate payroll button is clicked.
     */
    @FXML
    private void onPayrollClicked() {
        loadView("employee_list.fxml");
        setActiveButton(employeeList);
    }

    /**
     * Loads and displays a specified FXML view within the main content pane of the dashboard.
     */
    private void loadView(String fxml) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().setAll(pane);

            // Anchor the pane to all sides of contentPane to fill it entirely
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manages the visual styling to highlight the currently selected navigation button.
     */
    private void setActiveButton(Button active) {
        updateEmpInfo.getStyleClass().remove("menu-button-active");
        employeeList.getStyleClass().remove("menu-button-active");

        if (!active.getStyleClass().contains("menu-button-active")) {
            active.getStyleClass().add("menu-button-active");
        }
    }

    /**
     * Handles the user logout by returning to the main login screen.
     */
    @FXML
    private void onLogoutClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logout_button.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}