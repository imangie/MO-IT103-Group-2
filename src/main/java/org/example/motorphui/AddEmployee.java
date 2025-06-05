package org.example.motorphui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;

public class AddEmployee {
    @FXML
    private TextField EmpNumTxtAr, LastNameTxtAr, FirstNameTxtAr, BDayTxtAr, AddressTxtAr, PhoneNumTxtAr,
            SSSNumTxtAr, PHNumTxtAr, TINNumTxtAr, PgbgNumTxtAr, StatusTxtAr, PositionTxtAr,
            ImmedSupTxtAr, BasSalTxtAr1, RiceTxtAr1, PhoneAllowTxtAr, ClothTxtAr, GrossSemiTxtAr, HourRateTxtAr;

    @FXML
    private Button addEmpButton;
    @FXML
    private Button cancelButton;

    private final String employeeDataFile = "src/main/resources/org/example/motorphui/data/motorph_employee_data.csv";
    private HREmployeeView parentController;

    public void SetParentController(HREmployeeView parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void initialize() {
        addEmpButton.setDisable(true);
        TextField[] fields = {EmpNumTxtAr, LastNameTxtAr, FirstNameTxtAr, BDayTxtAr, AddressTxtAr, PhoneNumTxtAr,
                SSSNumTxtAr, PHNumTxtAr, TINNumTxtAr, PgbgNumTxtAr, StatusTxtAr, PositionTxtAr,
                ImmedSupTxtAr, BasSalTxtAr1, RiceTxtAr1, PhoneAllowTxtAr, ClothTxtAr,
                GrossSemiTxtAr, HourRateTxtAr};

        for (TextField field : fields) {
            field.textProperty().addListener((obs, oldVal, newVal) -> {
                addEmpButton.setDisable(!allFieldsFilled());
            });
        }
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        if (!allFieldsFilled()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields before submitting.");
            return;
        }

        // Create new employee object
        Employee newEmployee = new Employee(
            EmpNumTxtAr.getText().trim(),      // Employee Number
            LastNameTxtAr.getText().trim(),     // Last Name
            FirstNameTxtAr.getText().trim(),    // First Name
            BDayTxtAr.getText().trim(),        // Birthday
            AddressTxtAr.getText().trim(),     // Address
            PhoneNumTxtAr.getText().trim(),    // Phone Number
            SSSNumTxtAr.getText().trim(),      // SSS
            PHNumTxtAr.getText().trim(),       // PhilHealth
            TINNumTxtAr.getText().trim(),      // TIN
            PgbgNumTxtAr.getText().trim(),     // Pagibig
            StatusTxtAr.getText().trim(),      // Status
            PositionTxtAr.getText().trim(),    // Position
            ImmedSupTxtAr.getText().trim(),    // Immediate Supervisor
            BasSalTxtAr1.getText().trim(),     // Basic Salary
            RiceTxtAr1.getText().trim(),       // Rice Subsidy
            PhoneAllowTxtAr.getText().trim(),  // Phone Allowance
            ClothTxtAr.getText().trim(),       // Clothing Allowance
            GrossSemiTxtAr.getText().trim(),   // Gross Semi-monthly Rate
            HourRateTxtAr.getText().trim()     // Hourly Rate
        );

        try {
            // Add to parent's list first
            if (parentController != null) {
                parentController.addEmployee(newEmployee);
            }

            // Then write to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(employeeDataFile, true))) {
                String line = String.join(",",
                    EmpNumTxtAr.getText().trim(),      
                    // ... rest of your fields ...
                    HourRateTxtAr.getText().trim()     
                );

                writer.write(line);
                writer.newLine();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee added successfully.");

                // Clear the input fields and close
                clearFields();
                addEmpButton.setDisable(true);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Error writing to CSV file: " + e.getMessage());
        }
    }

    private boolean allFieldsFilled() {
        TextField[] fields = {EmpNumTxtAr, LastNameTxtAr, FirstNameTxtAr, BDayTxtAr, AddressTxtAr, PhoneNumTxtAr,
                SSSNumTxtAr, PHNumTxtAr, TINNumTxtAr, PgbgNumTxtAr, StatusTxtAr, PositionTxtAr,
                ImmedSupTxtAr, BasSalTxtAr1, RiceTxtAr1, PhoneAllowTxtAr, ClothTxtAr,
                GrossSemiTxtAr, HourRateTxtAr};

        for (TextField f : fields) {
            if (f.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void clearFields() {
        TextField[] fields = {EmpNumTxtAr, LastNameTxtAr, FirstNameTxtAr, BDayTxtAr, AddressTxtAr, PhoneNumTxtAr,
                SSSNumTxtAr, PHNumTxtAr, TINNumTxtAr, PgbgNumTxtAr, StatusTxtAr, PositionTxtAr,
                ImmedSupTxtAr, BasSalTxtAr1, RiceTxtAr1, PhoneAllowTxtAr, ClothTxtAr,
                GrossSemiTxtAr, HourRateTxtAr};
        
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}