package org.example.motorphui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Employee employee;
    private HREmployeeView parentController;

    public void setParentController(HREmployeeView controller) {
        this.parentController = controller;
    }

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

    @FXML
    private void initialize() {
        saveButton.setDisable(true);
        TextField[] fields = {employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
                phoneNumberField, sssField, philHealthField, tinField, pagIbigField, positionField,
                basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, hourlyRateField,
                statusField, immediateSupervisorField};

        for (TextField field : fields) {
            field.textProperty().addListener((obs, oldVal, newVal) -> {
                saveButton.setDisable(!allFieldsFilled());
            });
        }

        lastNameField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[a-zA-Z]") && !keyEvent.getText().isEmpty()) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Last name must be letters.");
            }
        });

        firstNameField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[a-zA-Z]") && !keyEvent.getText().isEmpty()) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "First name must be letters.");
            }
        });

        birthdayField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9/]") && !keyEvent.getText().isEmpty()) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Birthday must be numeric and can contain forward slashes.");
            }
        });

        phoneNumberField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9-]") && !keyEvent.getText().isEmpty()) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number must be numeric and can contain dashes.");
            }
        });

        sssField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9-]")) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "SSS must be numeric and can contain dashes.");
            }
        });

        tinField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9-]")) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "TIN must be numeric and can contain dashes.");
            }
        });

        philHealthField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "PhilHealth must be numeric.");
            }
        });

        pagIbigField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Pag-Ibig must be numeric.");
            }
        });

        basicSalaryField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]") || (basicSalaryField.getText().contains(".") && keyEvent.getCharacter().equals("."))) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Salary must be numeric.");
            }
        });

        riceSubsidyField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]") || (riceSubsidyField.getText().contains(".") && keyEvent.getCharacter().equals("."))) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Rice Subsidy must be numeric.");
            }
        });

        phoneAllowanceField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]") || (phoneAllowanceField.getText().contains(".") && keyEvent.getCharacter().equals("."))) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Allowance must be numeric.");
            }
        });

        clothingAllowanceField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]") || (clothingAllowanceField.getText().contains(".") && keyEvent.getCharacter().equals("."))) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Clothing Allowance must be numeric.");
            }
        });

        hourlyRateField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]") || (hourlyRateField.getText().contains(".") && keyEvent.getCharacter().equals("."))) {
                keyEvent.consume();
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Hourly Rate must be numeric and can contain one decimal point.");
            }
        });
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

    private boolean validateEmployeeData() {
        String empNumber = employeeNumberField.getText();
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();

        if (!empNumber.matches("\\d*") && !empNumber.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Employee Number must be numeric.");
            return false;
        }

        if (!lastName.matches("[a-zA-Z ]*") && !lastName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Last Name must only contain letters and spaces.");
            return false;
        }

        if (!firstName.matches("[a-zA-Z ]*") && !firstName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "First Name must only contain letters and spaces.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (validateEmployeeData()) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Employee Information");
            confirmAlert.setContentText("Are you sure you want to update this employee's information?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Employee updatedEmployee = new Employee(
                        employee.getEmployeeNumber(),
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
                        employee.getGrossSemiMonthlyRate(),
                        hourlyRateField.getText().trim()
                );

                if (parentController != null) {
                    parentController.updateEmployee(updatedEmployee);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee information updated successfully.");

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                }
            }
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

    // Add this method so the same controller can be used for read-only viewing
    public void setReadOnly() {
        TextField[] fields = {
                employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
                phoneNumberField, sssField, philHealthField, tinField, pagIbigField, statusField,
                positionField, immediateSupervisorField, basicSalaryField, riceSubsidyField,
                phoneAllowanceField, clothingAllowanceField, hourlyRateField
        };
        for (TextField field : fields) {
            field.setEditable(false);
        }
        saveButton.setVisible(false);
        if (cancelButton != null) {
            cancelButton.setText("Close");
        }
    }
}