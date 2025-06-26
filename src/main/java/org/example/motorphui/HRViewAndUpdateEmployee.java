// src/main/java/org/example/motorphui/HRViewAndUpdateEmployee.java
package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class HRViewAndUpdateEmployee {

    @FXML private TextField employeeNumberField;
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

    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private Button updateModeButton;

    private Employee employee; // The employee being viewed/updated
    private HREmployeeView parentController; // Reference to the main HR view controller

    private TextField[] editableFields; // Array to easily manage editable state of fields

    @FXML
    public void initialize() {
        // Initialize the array of fields that can be edited
        editableFields = new TextField[]{
                lastNameField, firstNameField, birthdayField, addressField, phoneNumberField,
                sssField, philHealthField, tinField, pagIbigField, positionField,
                basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, hourlyRateField
        };

        // Set fields to non-editable and hide save/cancel buttons initially
        setFieldsEditable(false);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        updateModeButton.setVisible(true); // Show update mode button
        deleteButton.setVisible(true);    // Show delete button
        employeeNumberField.setEditable(false); // Employee number is never editable
    }

    /**
     * Sets the Employee object to be displayed and updated.
     * Populates all text fields with the employee's data.
     * @param employee The Employee object.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;

        employeeNumberField.setText(employee.getEmployeeNumber());
        lastNameField.setText(employee.getLastName());
        firstNameField.setText(employee.getFirstName());
        birthdayField.setText(employee.getBirthday());
        addressField.setText(employee.getAddress());
        phoneNumberField.setText(employee.getPhoneNumber());
        sssField.setText(employee.getSss());
        philHealthField.setText(employee.getPhilHealth());
        tinField.setText(employee.getTin());
        pagIbigField.setText(employee.getPagIbig());
        positionField.setText(employee.getPosition());
        basicSalaryField.setText(employee.getBasicSalary());
        riceSubsidyField.setText(employee.getRiceSubsidy());
        phoneAllowanceField.setText(employee.getPhoneAllowance());
        clothingAllowanceField.setText(employee.getClothingAllowance());
        hourlyRateField.setText(employee.getHourlyRate());

        // Ensure fields are not editable when initially setting the employee
        setFieldsEditable(false);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        updateModeButton.setVisible(true);
        deleteButton.setVisible(true);
    }

    /**
     * Sets the reference to the parent controller (HREmployeeView) to allow communication.
     * @param controller The HREmployeeView instance.
     */
    public void setParentController(HREmployeeView controller) {
        this.parentController = controller;
    }

    /**
     * Sets the editable state of the designated text fields.
     * @param editable True to make fields editable, false otherwise.
     */
    private void setFieldsEditable(boolean editable) {
        for (TextField field : editableFields) {
            if (field != null) {
                field.setEditable(editable);
            }
        }
    }

    /**
     * Handles the action when the "Update Mode" button is clicked.
     * Makes fields editable and changes button visibility.
     */
    @FXML
    private void handleUpdateModeButton() {
        setFieldsEditable(true);
        updateModeButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
        deleteButton.setVisible(false); // Hide delete button during editing
    }

    /**
     * Handles the action when the "Save" button is clicked.
     * Saves updated employee information and reverts UI state.
     */
    @FXML
    private void handleSaveButton() {
        if (employee != null) {
            // --- Input Validation (add more specific validation as needed) ---
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

            // Basic numeric validation for salary/rate fields
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

            // Update the Employee object with new values from text fields
            employee.setLastName(lastNameField.getText());
            employee.setFirstName(firstNameField.getText());
            employee.setBirthday(birthdayField.getText());
            employee.setAddress(addressField.getText());
            employee.setPhoneNumber(phoneNumberField.getText());
            employee.setSss(sssField.getText());
            employee.setPhilHealth(philHealthField.getText());
            employee.setTin(tinField.getText());
            employee.setPagIbig(pagIbigField.getText());
            employee.setPosition(positionField.getText());
            employee.setBasicSalary(basicSalaryField.getText());
            employee.setRiceSubsidy(riceSubsidyField.getText());
            employee.setPhoneAllowance(phoneAllowanceField.getText());
            employee.setClothingAllowance(clothingAllowanceField.getText());
            employee.setHourlyRate(hourlyRateField.getText());

            // Inform the parent controller (HREmployeeView) to refresh its table
            // The HREmployeeView.refreshTable() reloads from CSV, effectively saving changes.
            if (parentController != null) {
                parentController.refreshTable();
            }

            showAlert(Alert.AlertType.INFORMATION, "Update Successful", null, "Employee information updated successfully.");

            // Revert UI to read-only state
            setFieldsEditable(false);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            updateModeButton.setVisible(true);
            deleteButton.setVisible(true); // Show delete button after saving
            closeWindow(); // Close the update window
        }
    }

    /**
     * Handles the action when the "Delete" button is clicked.
     * Confirms deletion and informs the parent controller to remove the employee.
     */
    @FXML
    private void handleDeleteButton() {
        if (employee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Employee: " + employee.getFirstName() + " " + employee.getLastName() + "?");
            alert.setContentText("Are you sure you want to delete this employee record? This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (parentController != null) {
                    parentController.removeEmployee(employee); // Delegate removal to parent
                }
                showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", null, "Employee record deleted successfully.");
                closeWindow(); // Close the update window after deletion
            }
        }
    }

    /**
     * Handles the action when the "Cancel" button is clicked.
     * Discards changes and reverts fields to original values, then restores UI state.
     */
    @FXML
    private void handleCancelButton() {
        if (employee != null) {
            setEmployee(this.employee); // Re-populate fields with original data and set to read-only
            // The setEmployee method handles button visibility correctly for read-only mode.
            deleteButton.setVisible(true); // Ensure delete button is visible after canceling edits
        }
    }

    private void closeWindow() {
        // Find the stage and close it
        Stage stage = (Stage) (saveButton != null ? saveButton : (cancelButton != null ? cancelButton : deleteButton)).getScene().getWindow();
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