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
    @FXML private TextField statusField;
    @FXML private TextField positionField;
    @FXML private TextField immediateSupervisorField;
    @FXML private TextField basicSalaryField;
    @FXML private TextField riceSubsidyField;
    @FXML private TextField phoneAllowanceField;
    @FXML private TextField clothingAllowanceField;
    @FXML private TextField hourlyRateField;

    @FXML private Button cancelButton;

    private Employee employee;
    private HREmployeeView parentController;

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
        statusField.setText(employee.getStatus());
        positionField.setText(employee.getPosition());
        immediateSupervisorField.setText(employee.getImmediateSupervisor());
        basicSalaryField.setText(employee.getBasicSalary());
        riceSubsidyField.setText(employee.getRiceSubsidy());
        phoneAllowanceField.setText(employee.getPhoneAllowance());
        clothingAllowanceField.setText(employee.getClothingAllowance());
        hourlyRateField.setText(employee.getHourlyRate());

        employeeNumberField.setEditable(false);
    }

    public void setParentController(HREmployeeView controller) {
        this.parentController = controller;
    }

    @FXML
    private void handleSaveButton() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Save");
        confirmationAlert.setHeaderText("Are you sure you want to save these changes?");
        confirmationAlert.setContentText("This will update the employee's information.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            employee.setLastName(lastNameField.getText());
            employee.setFirstName(firstNameField.getText());
            employee.setBirthday(birthdayField.getText());
            employee.setAddress(addressField.getText());
            employee.setPhoneNumber(phoneNumberField.getText());
            employee.setSss(sssField.getText());
            employee.setPhilHealth(philHealthField.getText());
            employee.setTin(tinField.getText());
            employee.setPagIbig(pagIbigField.getText());
            employee.setStatus(statusField.getText());
            employee.setPosition(positionField.getText());
            employee.setImmediateSupervisor(immediateSupervisorField.getText());
            employee.setBasicSalary(basicSalaryField.getText());
            employee.setRiceSubsidy(riceSubsidyField.getText());
            employee.setPhoneAllowance(phoneAllowanceField.getText());
            employee.setClothingAllowance(clothingAllowanceField.getText());
            employee.setHourlyRate(hourlyRateField.getText());

            if (parentController != null) {
                parentController.refreshTable();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Successful");
            alert.setHeaderText(null);
            alert.setContentText("Employee information updated successfully.");
            alert.showAndWait();

            closeWindow();
        }
    }

    @FXML
    private void handleCancelButton() {
        closeWindow();
    }

    @FXML
    private void handleRefreshButton() {

        if (parentController != null) {
            parentController.refreshTable();
        }

        clearAllFields();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Refreshed");
        alert.setHeaderText(null);
        alert.setContentText("Employee table/view has been refreshed and fields cleared.");
        alert.showAndWait();
    }

    private void clearAllFields() {
        employeeNumberField.clear();
        lastNameField.clear();
        firstNameField.clear();
        birthdayField.clear();
        addressField.clear();
        phoneNumberField.clear();
        sssField.clear();
        philHealthField.clear();
        tinField.clear();
        pagIbigField.clear();
        statusField.clear();
        positionField.clear();
        immediateSupervisorField.clear();
        basicSalaryField.clear();
        riceSubsidyField.clear();
        phoneAllowanceField.clear();
        clothingAllowanceField.clear();
        hourlyRateField.clear();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}