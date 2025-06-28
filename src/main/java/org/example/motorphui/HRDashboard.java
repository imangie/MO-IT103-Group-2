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


public class HRDashboard {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private Button emp_button, payroll_button;

    @FXML
    public void initialize() {
        loadView("hr_update_employee_information_view.fxml");
        setActiveButton(emp_button);
    }

    @FXML
    private void onEmployeesClicked() {
        loadView("hr_update_employee_information_view.fxml");
        setActiveButton(emp_button);
    }

    @FXML
    private void onPayrollClicked() {
        loadView("hr_employee_list.fxml");
        setActiveButton(payroll_button);
    }

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

    private void setActiveButton(Button active) {
        emp_button.getStyleClass().remove("menu-button-active");
        payroll_button.getStyleClass().remove("menu-button-active");

        if (!active.getStyleClass().contains("menu-button-active")) {
            active.getStyleClass().add("menu-button-active");
        }
    }
}