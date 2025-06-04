package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        positionField.setText(employee.getPosition());
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
        if (employee != null) {

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

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}