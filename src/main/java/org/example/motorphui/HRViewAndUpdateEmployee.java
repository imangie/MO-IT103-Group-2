package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.example.motorphui.model.Payslip; // Import the Payslip class

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

    @FXML private ComboBox<String> yearComboBox;
    @FXML private ComboBox<String> payPeriodComboBox;
    @FXML private TextArea payslipTextArea;

    private Employee employee;
    private HREmployeeView parentController; // This is the main table view controller

    private TextField[] editableFields;
    private Payroll payroll;


    @FXML
    public void initialize() {
        editableFields = new TextField[]{
                lastNameField, firstNameField, birthdayField, addressField, phoneNumberField,
                sssField, philHealthField, tinField, pagIbigField, positionField,
                basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, hourlyRateField
        };

        setFieldsEditable(false);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        updateModeButton.setVisible(true);
        deleteButton.setVisible(true);
        employeeNumberField.setEditable(false);
        employeeNumberField.setStyle("-fx-control-inner-background: #e0e0e0;");

        payslipTextArea.setEditable(false);
        payslipTextArea.setFont(javafx.scene.text.Font.font("monospace", 12));

        payroll = new Payroll();

        populatePayslipComboBoxes();

        yearComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal != null && employee != null) {
                populatePayPeriodComboBox(newVal, employee.getEmployeeNumber());
            } else if (newVal == null) {
                payPeriodComboBox.getItems().clear();
            } else { // employee == null
                payPeriodComboBox.getItems().clear();
            }
        });
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
        positionField.setText(employee.getPosition());
        basicSalaryField.setText(employee.getBasicSalary());
        riceSubsidyField.setText(employee.getRiceSubsidy());
        phoneAllowanceField.setText(employee.getPhoneAllowance());
        clothingAllowanceField.setText(employee.getClothingAllowance());
        hourlyRateField.setText(employee.getHourlyRate());

        setFieldsEditable(false);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        updateModeButton.setVisible(true);
        deleteButton.setVisible(true);

        payslipTextArea.clear();
        if (yearComboBox.getValue() != null) {
            populatePayPeriodComboBox(yearComboBox.getValue(), employee.getEmployeeNumber());
        } else {
        }
    }

    public void setParentController(HREmployeeView controller) {
        this.parentController = controller;
    }

    private void setFieldsEditable(boolean editable) {
        String editableStyle = "";
        String viewOnlyStyle = "-fx-control-inner-background: #e0e0e0;";

        for (TextField field : editableFields) {
            if (field != null) {
                field.setEditable(editable);
                if (editable) {
                    field.setStyle(editableStyle);
                } else {
                    field.setStyle(viewOnlyStyle);
                }
            }
        }
    }

    @FXML
    private void handleUpdateModeButton() {
        setFieldsEditable(true);
        updateModeButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
        deleteButton.setVisible(false);
    }

    @FXML
    private void handleSaveButton() {
        if (employee != null) {
            // Input validation for mandatory text fields (cannot be empty)
            if (lastNameField.getText().trim().isEmpty() ||
                    firstNameField.getText().trim().isEmpty() ||
                    birthdayField.getText().trim().isEmpty() ||
                    addressField.getText().trim().isEmpty() ||
                    phoneNumberField.getText().trim().isEmpty() ||
                    positionField.getText().trim().isEmpty() ||
                    sssField.getText().trim().isEmpty() ||
                    philHealthField.getText().trim().isEmpty() ||
                    tinField.getText().trim().isEmpty() ||
                    pagIbigField.getText().trim().isEmpty())
            {

                showAlert(Alert.AlertType.WARNING, "Missing Information", null, "Please fill in all mandatory employee details.");
                return;
            }

            // Numeric format validation for fields that must contain numbers

            if (!isValidNumericInput(basicSalaryField.getText()) ||
                    !isValidNumericInput(riceSubsidyField.getText()) ||
                    !isValidNumericInput(phoneAllowanceField.getText()) ||
                    !isValidNumericInput(clothingAllowanceField.getText()) ||
                    !isValidNumericInput(hourlyRateField.getText()))
            {

                showAlert(Alert.AlertType.WARNING, "Invalid Input", null,
                        "Please ensure all numeric fields (e.g., salaries, allowances, SSS, PhilHealth, TIN, Pag-IBIG) contain valid numbers.");
                return;
            }
            Employee updatedEmployee = new Employee(
                    employee.getEmployeeNumber(), // The employee number does not change
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

            if (parentController != null) {
                parentController.refreshTable();
                parentController.updateEmployee(updatedEmployee);
            }

            showAlert(Alert.AlertType.INFORMATION, "Update Successful", null, "Employee information updated successfully.");

            setFieldsEditable(false);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            updateModeButton.setVisible(true);
            deleteButton.setVisible(true);
            closeWindow();
        }
    }

    private boolean isValidNumericInput(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


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
                    parentController.removeEmployee(employee);
                }
                showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", null, "Employee record deleted successfully.");
                closeWindow();
            }
        }
    }

    @FXML
    private void handleCancelButton() {
        if (employee != null) {
            setEmployee(this.employee);
            setFieldsEditable(false);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            updateModeButton.setVisible(true);
            deleteButton.setVisible(true);
        }
    }

    private void closeWindow() {
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

    private void populatePayslipComboBoxes() {
        Set<String> uniqueYears = payroll.getUniqueYearsFromAttendance();
        yearComboBox.getItems().setAll(uniqueYears);
        if (!uniqueYears.isEmpty()) {
            yearComboBox.getSelectionModel().selectFirst();
        } else {
        }
    }

    private void populatePayPeriodComboBox(String year, String employeeNumber) {
        payPeriodComboBox.getItems().clear();
        List<String> periodsList = payroll.getSortedPayPeriodsForEmployee(year, employeeNumber);
        payPeriodComboBox.getItems().setAll(periodsList);
        if (!periodsList.isEmpty()) {
            payPeriodComboBox.getSelectionModel().selectFirst();
        } else {
        }
    }


    @FXML
    private void handleCalculatePayslipButton() {
        if (employee == null) {
            showAlert(Alert.AlertType.WARNING, "No Employee Selected", null, "Please select an employee first.");
            return;
        }
        String selectedYear = yearComboBox.getSelectionModel().getSelectedItem();
        String selectedPayPeriod = payPeriodComboBox.getSelectionModel().getSelectedItem();

        if (selectedYear == null || selectedPayPeriod == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Selection", null, "Please select both a year and a pay period.");
            return;
        }

        try {
            Payslip payslip = payroll.calculateFullPayslip(employee, selectedYear, selectedPayPeriod);

            // Set the formatted payslip text to the TextArea
            String formattedPayslip = payslip.formatPayslip();
            payslipTextArea.setText(formattedPayslip);

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Calculation Error", "Invalid Pay Period Data",
                    "There was an error processing the pay period data: " + e.getMessage());
            payslipTextArea.setText("Error calculating payslip.");
        } catch (Exception e) { // Catch any other unexpected errors during calculation
            showAlert(Alert.AlertType.ERROR, "Calculation Error", "An unexpected error occurred",
                    "Failed to calculate payslip: " + e.getMessage() + "\n" +
                            "Please check employee data and attendance records.");
            payslipTextArea.setText("Error calculating payslip.");
            e.printStackTrace(); // Log the full stack trace for debugging
        }
    }
}