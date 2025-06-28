package org.example.motorphui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class HREmployeeView {

    @FXML
    private AnchorPane root;
    @FXML
    private TableView<Employee> emp_table;
    @FXML
    private Label emp_info_label;
    @FXML
    private Button saveChangesButton;
    @FXML
    private Button deleteemp_button;
    @FXML private TableColumn<Employee, String> empNumColumn;
    @FXML private TableColumn<Employee, String> lastNameColumn;
    @FXML private TableColumn<Employee, String> firstNameColumn;
    @FXML private TextField employeeNumberField;
    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private DatePicker birthdayField;
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
    @FXML private TextField statusField;
    @FXML private TextField immediateSupervisorField;
    @FXML private TextField grossSemiMonthlyField;

    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    private static final String EMPLOYEE_DATA_FILE = "src/main/resources/org/example/motorphui/data/motorph_employee_data.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    @FXML
    public void initialize() {
        root.setMinWidth(1440);
        root.setMinHeight(1024);
        emp_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        empNumColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        loadEmployeesFromCSV();

        emp_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showEmployeeDetails(newSelection);
                saveChangesButton.setDisable(false);
                deleteemp_button.setDisable(false);
            } else {
                clearEmployeeDetails();
                saveChangesButton.setDisable(true);
                deleteemp_button.setDisable(true);
            }
        });

        saveChangesButton.setDisable(true);
        deleteemp_button.setDisable(true);

        employeeNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Employee Number must be numeric.");
                employeeNumberField.setText(oldValue);
            }
        });

        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Last Name must only contain letters and spaces.");
                lastNameField.setText(oldValue);
            }
        });

        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "First Name must only contain letters and spaces.");
                firstNameField.setText(oldValue);
            }
        });

        basicSalaryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Basic Salary must be a valid number.");
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
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "TIN must be numeric and can contain dashes.");
                tinField.setText(oldValue);
            }
        });

        sssField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "SSS must be numeric and can contain dashes.");
                sssField.setText(oldValue);
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
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number must be numeric and can contain dashes.");
                phoneNumberField.setText(oldValue);
            }
        });
    }

    private void showEmployeeDetails(Employee emp) {
        employeeNumberField.setText(emp.getEmployeeNumber());
        lastNameField.setText(emp.getLastName());
        firstNameField.setText(emp.getFirstName());
        if (emp.getBirthday() != null && !emp.getBirthday().isEmpty()) {
            try {
                birthdayField.setValue(LocalDate.parse(emp.getBirthday(), DATE_FORMATTER));
            } catch (DateTimeParseException e) {
                birthdayField.setValue(null);
            }
        } else {
            birthdayField.setValue(null);
        }
        addressField.setText(emp.getAddress());
        phoneNumberField.setText(emp.getPhoneNumber());
        sssField.setText(emp.getSss());
        philHealthField.setText(emp.getPhilHealth());
        tinField.setText(emp.getTin());
        pagIbigField.setText(emp.getPagIbig());
        statusField.setText(emp.getStatus());
        positionField.setText(emp.getPosition());
        immediateSupervisorField.setText(emp.getImmediateSupervisor());
        basicSalaryField.setText(emp.getBasicSalary());
        riceSubsidyField.setText(emp.getRiceSubsidy());
        phoneAllowanceField.setText(emp.getPhoneAllowance());
        clothingAllowanceField.setText(emp.getClothingAllowance());
        grossSemiMonthlyField.setText(emp.getGrossSemiMonthlyRate());
        hourlyRateField.setText(emp.getHourlyRate());
    }

    private void clearEmployeeDetails() {
        employeeNumberField.clear();
        lastNameField.clear();
        firstNameField.clear();
        birthdayField.setValue(null);
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
        grossSemiMonthlyField.clear();
        hourlyRateField.clear();
    }

    private void loadEmployeesFromCSV() {
        employeeList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DATA_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 19) {
                    Employee emp = new Employee(
                            data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9],
                            data[10], data[11], data[12], data[13], data[14], data[15], data[16], data[17], data[18]
                    );
                    employeeList.add(emp);
                }
            }
            emp_table.setItems(employeeList);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load employee data.");
        }
    }

    public void updateEmployee(Employee updatedEmployee) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getEmployeeNumber().equals(updatedEmployee.getEmployeeNumber())) {
                employeeList.set(i, updatedEmployee);
                break;
            }
        }
        saveEmployeesToCSV(EMPLOYEE_DATA_FILE);
        refreshTable();
    }

    @FXML
    public void handleSaveChangesButton() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            if (employeeNumberField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() ||
                    firstNameField.getText().trim().isEmpty() ||
                    birthdayField.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Employee Number, Last Name, First Name, and Birthday are required.");
                return;
            }

            if (!isValidDouble(basicSalaryField.getText(), "Basic Salary") ||
                    !isValidDouble(riceSubsidyField.getText(), "Rice Subsidy") ||
                    !isValidDouble(phoneAllowanceField.getText(), "Phone Allowance") ||
                    !isValidDouble(clothingAllowanceField.getText(), "Clothing Allowance") ||
                    !isValidDouble(grossSemiMonthlyField.getText(), "Gross Semi-Monthly Rate") ||
                    !isValidDouble(hourlyRateField.getText(), "Hourly Rate")) {
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Save");
            confirmation.setHeaderText("Save Changes?");
            confirmation.setContentText("Are you sure you want to save these changes to the employee record?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Employee updatedEmployee = new Employee(
                        employeeNumberField.getText(),
                        lastNameField.getText(),
                        firstNameField.getText(),
                        (birthdayField.getValue() != null) ? birthdayField.getValue().format(DATE_FORMATTER) : "",
                        addressField.getText(),
                        phoneNumberField.getText(),
                        sssField.getText(),
                        philHealthField.getText(),
                        tinField.getText(),
                        pagIbigField.getText(),
                        statusField.getText(),
                        positionField.getText(),
                        immediateSupervisorField.getText(),
                        basicSalaryField.getText(),
                        riceSubsidyField.getText(),
                        phoneAllowanceField.getText(),
                        clothingAllowanceField.getText(),
                        grossSemiMonthlyField.getText(),
                        hourlyRateField.getText()
                );
                updateEmployee(updatedEmployee);
                for (Employee emp : emp_table.getItems()) {
                    if (emp.getEmployeeNumber().equals(updatedEmployee.getEmployeeNumber())) {
                        emp_table.getSelectionModel().select(emp);
                        showEmployeeDetails(emp);
                        break;
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee record updated successfully!");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Save operation cancelled.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee in the table to update.");
        }
    }

    @FXML
    private void handleDeleteEmployeeButton() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Delete Employee Record?");
            confirmation.setContentText("Are you sure you want to delete employee " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + " (Employee #: " + selectedEmployee.getEmployeeNumber() + ")? This action cannot be undone.");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                employeeList.remove(selectedEmployee);
                saveEmployeesToCSV(EMPLOYEE_DATA_FILE);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "Employee record deleted successfully.");
                clearEmployeeDetails();
                saveChangesButton.setDisable(true);
                deleteemp_button.setDisable(true);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Delete operation cancelled.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee in the table to delete.");
        }
    }

    @FXML
    private void handleUpdateEmployee() {
        handleSaveChangesButton();
    }

    public void refreshTable() {
        loadEmployeesFromCSV();
    }

    private void saveEmployeesToCSV(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,PhilHealth #,TIN #,Pag-Ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate\n");
            for (Employee emp : employeeList) {
                writer.write(String.join(",",
                        emp.getEmployeeNumber(),
                        emp.getLastName(),
                        emp.getFirstName(),
                        emp.getBirthday(),
                        emp.getAddress(),
                        emp.getPhoneNumber(),
                        emp.getSss(),
                        emp.getPhilHealth(),
                        emp.getTin(),
                        emp.getPagIbig(),
                        emp.getStatus(),
                        emp.getPosition(),
                        emp.getImmediateSupervisor(),
                        emp.getBasicSalary(),
                        emp.getRiceSubsidy(),
                        emp.getPhoneAllowance(),
                        emp.getClothingAllowance(),
                        emp.getGrossSemiMonthlyRate(),
                        emp.getHourlyRate()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to save employee data.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidDouble(String text, String fieldName) {
        if (text.trim().isEmpty()) {
            return true;
        }
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", fieldName + " must be a valid number.");
            return false;
        }
    }
}