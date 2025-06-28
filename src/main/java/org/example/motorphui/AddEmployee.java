// src/main/java/org/example/motorphui/HRAddEmployeeController.java
package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEmployee {

    @FXML private TextField employeeNumberField; // For displaying the auto-generated number
    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField birthdayField;
    @FXML private TextField addressField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField sssField;
    @FXML private TextField philHealthField;
    @FXML private TextField tinField;
    @FXML private TextField pagIbigField;
    @FXML private TextField positionField;
    @FXML private TextField basicSalaryField;
    @FXML private TextField riceSubsidyField;
    @FXML private TextField phoneAllowanceField;
    @FXML private TextField clothingAllowanceField;
    @FXML private TextField hourlyRateField;

    private HREmployeeView parentController;

    @FXML
    public void initialize() {
        // Set the employee number field to be non-editable with light gray background.
        employeeNumberField.setEditable(false);
        employeeNumberField.setStyle("-fx-control-inner-background: #e0e0e0;");
    }

    // Method to set the reference to the parent controller (HREmployeeView)
    public void setParentController(HREmployeeView controller) {
        this.parentController = controller;
    }

    // Method to receive and display the auto-generated employee number
    public void setEmployeeNumber(String empNum) {
        employeeNumberField.setText(empNum);
    }

    @FXML
    private void handleAddButton() {
        // Input Validation. Check if all required text fields are not empty
        if (lastNameField.getText().trim().isEmpty() || firstNameField.getText().trim().isEmpty() ||
                birthdayField.getText().trim().isEmpty() || addressField.getText().trim().isEmpty() ||
                phoneNumberField.getText().trim().isEmpty() || sssField.getText().trim().isEmpty() ||
                philHealthField.getText().trim().isEmpty() || tinField.getText().trim().isEmpty() ||
                pagIbigField.getText().trim().isEmpty() || positionField.getText().trim().isEmpty() ||
                basicSalaryField.getText().trim().isEmpty() || riceSubsidyField.getText().trim().isEmpty() ||
                phoneAllowanceField.getText().trim().isEmpty() || clothingAllowanceField.getText().trim().isEmpty() ||
                hourlyRateField.getText().trim().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Missing Information", null, "Please fill in all employee details.");
            return;
        }

        // Validate numeric fields
        try {
            Double.parseDouble(basicSalaryField.getText());
            Double.parseDouble(riceSubsidyField.getText());
            Double.parseDouble(phoneAllowanceField.getText());
            Double.parseDouble(clothingAllowanceField.getText());
            Double.parseDouble(hourlyRateField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", null, "Please enter valid numeric values for salary and allowance fields.");
            return;
        }

        // Get the employee number from the read-only field (which was set by parentController)
        String newEmployeeNumber = employeeNumberField.getText();

        // Create a new Employee object using the gathered data
        Employee newEmployee = new Employee(
                newEmployeeNumber,
                lastNameField.getText(),
                firstNameField.getText(),
                sssField.getText(),
                philHealthField.getText(),
                tinField.getText(),
                pagIbigField.getText(),
                birthdayField.getText(),
                addressField.getText(),
                phoneNumberField.getText(),
                positionField.getText(),
                riceSubsidyField.getText(),
                phoneAllowanceField.getText(),
                clothingAllowanceField.getText(),
                hourlyRateField.getText(),
                basicSalaryField.getText()
        );

        // Inform the parent controller to add this new employee to its list and save to CSV
        if (parentController != null) {
            parentController.addEmployee(newEmployee);
        }

        showAlert(Alert.AlertType.INFORMATION, "Employee Added", null,
                "New employee " + newEmployee.getFirstName() + " " + newEmployee.getLastName() +
                        " (Emp#: " + newEmployee.getEmployeeNumber() + ") has been added successfully.");

        closeWindow();
    }

    @FXML
    private void handleCancelButton() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) lastNameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}