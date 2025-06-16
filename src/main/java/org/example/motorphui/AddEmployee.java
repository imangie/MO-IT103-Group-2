package org.example.motorphui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.*;
import java.util.Optional;

/**
 * Purpose: Manages the functionality for adding a new employee to the system.
 * - Validates that all required fields are filled before submitting.
 * - Restricts non-numeric input in specific fields (e.g., Employee Number, Phone Number).
 * - Displays confirmation prompt and success/error alerts.
 */

public class AddEmployee {
    @FXML
    private TextField employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
            phoneNumberField, sssField, philHealthField, tinField, pagIbigField, positionField,
            basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, hourlyRateField,
            statusField, immediateSupervisorField;

    @FXML
    private Button addEmpButton;
    @FXML
    private Button cancelButton;

    private final String employeeDataFile = "src/main/resources/org/example/motorphui/data/motorph_employee_data.csv";
    private HREmployeeView parentController;

    private boolean phoneNumberAlertShown = false;
    private boolean sssAlertShown = false;
    private boolean tinAlertShown = false;
    private boolean philHealthAlertShown = false;
    private boolean pagIbigAlertShown = false;

    public void SetParentController(HREmployeeView parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void initialize() {
        addEmpButton.setDisable(true);
        TextField[] fields = {employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
                phoneNumberField, sssField, philHealthField, tinField, pagIbigField, positionField,
                basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, hourlyRateField,
                statusField, immediateSupervisorField};

        for (TextField field : fields) {
            field.textProperty().addListener((obs, oldVal, newVal) -> {
                addEmpButton.setDisable(!allFieldsFilled());
            });
        }

        // Validation for Employee Number (Numeric Only)
        employeeNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Only digits allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Employee Number must be numeric.");
                employeeNumberField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        // Name Validation (Only Letters)
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {  // Only letters and spaces allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Names must only contain letters and spaces.");
                lastNameField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {  // Only letters and spaces allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Names must only contain letters and spaces.");
                firstNameField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        // Validate Salary and Allowances (numeric values allowed)
        basicSalaryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {  // Allows digits and decimals
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Salary must be a valid number.");
                basicSalaryField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        riceSubsidyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Allows digits
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Rice Subsidy must be a valid number.");
                riceSubsidyField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        phoneAllowanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {  // Allows digits and decimals
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Allowance must be a valid number.");
                phoneAllowanceField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        clothingAllowanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {  // Allows digits and decimals
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Clothing Allowance must be a valid number.");
                clothingAllowanceField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        hourlyRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {  // Allows digits and decimals
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Hourly Rate must be a valid number.");
                hourlyRateField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        // Validate Pag-Ibig, TIN, SSS, PhilHealth, Phone Number (Numeric Only)
        pagIbigField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Only digits allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Pag-Ibig must be numeric.");
                pagIbigField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        tinField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {  // Only digits and dashes allowed for TIN
                if (!newValue.equals(oldValue)) { // Avoid setting the value if no change
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "TIN must be numeric and can contain dashes.");
                    tinField.setText(oldValue);  // Revert to the previous valid value
                }
            }
        });

        sssField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {  // Only digits and dashes allowed for SSS
                if (!newValue.equals(oldValue)) { // Avoid setting the value if no change
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "SSS must be numeric and can contain dashes.");
                    sssField.setText(oldValue);  // Revert to the previous valid value
                }
            }
        });

        philHealthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Only digits allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "PhilHealth must be numeric.");
                philHealthField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {  // Only digits and dashes allowed
                if (!newValue.equals(oldValue)) { // Avoid setting the value if no change
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number must be numeric and can contain dashes.");
                    phoneNumberField.setText(oldValue);  // Revert to the previous valid value
                }
            }
        });
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        if (!allFieldsFilled()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields before submitting.");
            return;
        }

        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Add Employee");
        confirmAlert.setHeaderText("Add New Employee");
        confirmAlert.setContentText("Are you sure you want to add this employee?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Employee newEmployee = new Employee(
                    employeeNumberField.getText().trim(),
                    lastNameField.getText().trim(),
                    firstNameField.getText().trim(),
                    birthdayField.getText().trim(),
                    addressField.getText().trim(),
                    phoneNumberField.getText().trim(),
                    sssField.getText().trim(),
                    philHealthField.getText().trim(),
                    tinField.getText().trim(),
                    pagIbigField.getText().trim(),
                    statusField.getText().trim(),
                    positionField.getText().trim(),
                    immediateSupervisorField.getText().trim(),
                    basicSalaryField.getText().trim(),
                    riceSubsidyField.getText().trim(),
                    phoneAllowanceField.getText().trim(),
                    clothingAllowanceField.getText().trim(),
                    "0",                                // grossSemiMonthlyRate - you might want to add this field
                    hourlyRateField.getText().trim()
);

            // Add to parent's list and save
            if (parentController != null) {
                parentController.addEmployee(newEmployee);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee added successfully.");
                clearFields();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        }
    }

    private boolean allFieldsFilled() {
        TextField[] fields = {employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
                phoneNumberField, sssField, philHealthField, tinField, pagIbigField, positionField,
                basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, hourlyRateField,
                statusField, immediateSupervisorField};

        for (TextField f : fields) {
            if (f.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void clearFields() {
        TextField[] fields = {employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
                phoneNumberField, sssField, philHealthField, tinField, pagIbigField, positionField,
                basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, hourlyRateField,
                statusField, immediateSupervisorField};
        
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