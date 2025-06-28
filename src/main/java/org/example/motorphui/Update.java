package org.example.motorphui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class Update {

    @FXML
    private TextField employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
            phoneNumberField, sssField, philHealthField, tinField, pagIbigField, positionField,
            basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, hourlyRateField,
            statusField, immediateSupervisorField;

    @FXML
    private Button updateEmpButton;
    @FXML
    private Button cancelButton;

    private HRPayroll parentController;
    private Employee employeeToUpdate = null;

    public void setParentController(HRPayroll parentController) {
        this.parentController = parentController;
    }

    public void setEmployeeToUpdate(Employee employee) {
        this.employeeToUpdate = employee;
        if (employee != null) {
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
            statusField.setText(employee.getStatus());
            positionField.setText(employee.getPosition());
            immediateSupervisorField.setText(employee.getImmediateSupervisor());
            basicSalaryField.setText(employee.getBasicSalary());
            riceSubsidyField.setText(employee.getRiceSubsidy());
            phoneAllowanceField.setText(employee.getPhoneAllowance());
            clothingAllowanceField.setText(employee.getClothingAllowance());
            hourlyRateField.setText(employee.getHourlyRate());
            updateEmpButton.setDisable(false); // Enable the button if updating
            // Optionally, prevent editing employee number on update:
            // employeeNumberField.setEditable(false);
        } else {
            clearFields();
            updateEmpButton.setDisable(true);
            // employeeNumberField.setEditable(true);
        }
    }

    @FXML
    private void initialize() {
        // updateEmpButton.setText("Update"); // Not needed if set in FXML
        updateEmpButton.setDisable(true);
        TextField[] fields = {employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
                phoneNumberField, sssField, philHealthField, tinField, pagIbigField, positionField,
                basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, hourlyRateField,
                statusField, immediateSupervisorField};

        for (TextField field : fields) {
            field.textProperty().addListener((obs, oldVal, newVal) -> {
                updateEmpButton.setDisable(!allFieldsFilled());
            });
        }

        employeeNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Employee Number must be numeric.");
                employeeNumberField.setText(oldValue);
            }
        });

        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Names must only contain letters and spaces.");
                lastNameField.setText(oldValue);
            }
        });

        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Names must only contain letters and spaces.");
                firstNameField.setText(oldValue);
            }
        });

        basicSalaryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Salary must be a valid number.");
                basicSalaryField.setText(oldValue);
            }
        });

        riceSubsidyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Rice Subsidy must be a valid number.");
                riceSubsidyField.setText(oldValue);
            }
        });

        phoneAllowanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Allowance must be a valid number.");
                phoneAllowanceField.setText(oldValue);
            }
        });

        clothingAllowanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Clothing Allowance must be a valid number.");
                clothingAllowanceField.setText(oldValue);
            }
        });

        hourlyRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Hourly Rate must be a valid number.");
                hourlyRateField.setText(oldValue);
            }
        });

        pagIbigField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Pag-Ibig must be numeric.");
                pagIbigField.setText(oldValue);
            }
        });

        tinField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {
                if (!newValue.equals(oldValue)) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "TIN must be numeric and can contain dashes.");
                    tinField.setText(oldValue);
                }
            }
        });

        sssField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {
                if (!newValue.equals(oldValue)) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "SSS must be numeric and can contain dashes.");
                    sssField.setText(oldValue);
                }
            }
        });

        philHealthField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "PhilHealth must be numeric.");
                philHealthField.setText(oldValue);
            }
        });

        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {
                if (!newValue.equals(oldValue)) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number must be numeric and can contain dashes.");
                    phoneNumberField.setText(oldValue);
                }
            }
        });
    }

    @FXML
    private void onUpdateButtonClick(ActionEvent event) {
        if (!allFieldsFilled()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields before submitting.");
            return;
        }

        if (employeeToUpdate == null) {
            showAlert(Alert.AlertType.WARNING, "No Employee Selected", "Please select an employee from the list to update.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Update Employee");
        confirmAlert.setHeaderText("Update Employee");
        confirmAlert.setContentText("Are you sure you want to update this employee?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            employeeToUpdate.setEmployeeNumber(employeeNumberField.getText().trim());
            employeeToUpdate.setLastName(lastNameField.getText().trim());
            employeeToUpdate.setFirstName(firstNameField.getText().trim());
            employeeToUpdate.setBirthday(birthdayField.getText().trim());
            employeeToUpdate.setAddress(addressField.getText().trim());
            employeeToUpdate.setPhoneNumber(phoneNumberField.getText().trim());
            employeeToUpdate.setSss(sssField.getText().trim());
            employeeToUpdate.setPhilHealth(philHealthField.getText().trim());
            employeeToUpdate.setTin(tinField.getText().trim());
            employeeToUpdate.setPagIbig(pagIbigField.getText().trim());
            employeeToUpdate.setStatus(statusField.getText().trim());
            employeeToUpdate.setPosition(positionField.getText().trim());
            employeeToUpdate.setImmediateSupervisor(immediateSupervisorField.getText().trim());
            employeeToUpdate.setBasicSalary(basicSalaryField.getText().trim());
            employeeToUpdate.setRiceSubsidy(riceSubsidyField.getText().trim());
            employeeToUpdate.setPhoneAllowance(phoneAllowanceField.getText().trim());
            employeeToUpdate.setClothingAllowance(clothingAllowanceField.getText().trim());
            employeeToUpdate.setHourlyRate(hourlyRateField.getText().trim());

            if (parentController != null) {
                parentController.updateEmployee(employeeToUpdate);
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Employee updated successfully.");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
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