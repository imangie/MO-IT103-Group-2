package org.example.motorphui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Purpose: Allows HR to view and manage employee records.
 * - Displays a table of employee records with selected core info.
 * - Provides functionality to view (inline), update (inline), and delete employee information.
 * - Loads and saves employee data from/to a CSV file.
 */
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

    // Declare columns for each property in Employee class.
    // These are commented out because you've removed them from FXML.
    @FXML private TableColumn<Employee, String> empNumColumn;
    @FXML private TableColumn<Employee, String> lastNameColumn;
    @FXML private TableColumn<Employee, String> firstNameColumn;
    //@FXML private TableColumn<Employee, String> birthdayColumn;
    //@FXML private TableColumn<Employee, String> addressColumn;
    //@FXML private TableColumn<Employee, String> phoneNumberColumn;
    //@FXML private TableColumn<Employee, String> sssColumn;
    //@FXML private TableColumn<Employee, String> philHealthColumn;
    //@FXML private TableColumn<Employee, String> tinColumn;
    //@FXML private TableColumn<Employee, String> pagIbigColumn;
    //@FXML private TableColumn<Employee, String> statusColumn;
    //@FXML private TableColumn<Employee, String> positionColumn;
    //@FXML private TableColumn<Employee, String> supervisorColumn;
    //@FXML private TableColumn<Employee, String> basicSalaryColumn;
    //@FXML private TableColumn<Employee, String> riceSubsidyColumn;
    //@FXML private TableColumn<Employee, String> phoneAllowanceColumn;
    //@FXML private TableColumn<Employee, String> clothingAllowanceColumn;
    //@FXML private TableColumn<Employee, String> grossSemiMonthlyColumn;
    //@FXML private TableColumn<Employee, String> hourlyRateColumn;

    // FXML fields for the inline employee details form on the right
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
                employeeNumberField.setText(newSelection.getEmployeeNumber());
                lastNameField.setText(newSelection.getLastName());
                firstNameField.setText(newSelection.getFirstName());

                if (newSelection.getBirthday() != null && !newSelection.getBirthday().isEmpty()) {
                    try {
                        birthdayField.setValue(LocalDate.parse(newSelection.getBirthday(), DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        birthdayField.setValue(null);
                        System.err.println("Error parsing birthday for Employee " + newSelection.getEmployeeNumber() + ": " + newSelection.getBirthday() + " - " + e.getMessage());
                    }
                } else {
                    birthdayField.setValue(null);
                }

                addressField.setText(newSelection.getAddress());
                phoneNumberField.setText(newSelection.getPhoneNumber());
                sssField.setText(newSelection.getSss());
                philHealthField.setText(newSelection.getPhilHealth());
                tinField.setText(newSelection.getTin());
                pagIbigField.setText(newSelection.getPagIbig());
                statusField.setText(newSelection.getStatus());
                positionField.setText(newSelection.getPosition());
                immediateSupervisorField.setText(newSelection.getImmediateSupervisor());
                basicSalaryField.setText(newSelection.getBasicSalary());
                riceSubsidyField.setText(newSelection.getRiceSubsidy());
                phoneAllowanceField.setText(newSelection.getPhoneAllowance());
                clothingAllowanceField.setText(newSelection.getClothingAllowance());
                grossSemiMonthlyField.setText(newSelection.getGrossSemiMonthlyRate());
                hourlyRateField.setText(newSelection.getHourlyRate());

                saveChangesButton.setDisable(false);
                deleteemp_button.setDisable(false);

            } else {
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

                saveChangesButton.setDisable(true);
                deleteemp_button.setDisable(true);
            }
        });

        // Ensure buttons are initially disabled if no row is selected by default
        saveChangesButton.setDisable(true);
        deleteemp_button.setDisable(true);

        // Real-time Input Validation using Listeners
        // Validation for Employee Number (Numeric Only)
        employeeNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Only digits allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Employee Number must be numeric.");
                employeeNumberField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        // Name Validation (Only Letters and Spaces)
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {  // Only letters and spaces allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Last Name must only contain letters and spaces.");
                lastNameField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {  // Only letters and spaces allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "First Name must only contain letters and spaces.");
                firstNameField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        // Validate Salary and Allowances (numeric values allowed, including decimals for some)
        basicSalaryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {  // Allows digits and decimals
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Basic Salary must be a valid number.");
                basicSalaryField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        riceSubsidyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Allows digits (assuming whole numbers for rice subsidy)
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

        // Validate Pag-Ibig, TIN, SSS, PhilHealth, Phone Number (Numeric or Numeric with Dashes)
        pagIbigField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Only digits allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Pag-Ibig must be numeric.");
                pagIbigField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        tinField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {  // Only digits and dashes allowed for TIN
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "TIN must be numeric and can contain dashes.");
                tinField.setText(oldValue);  // Revert to the previous valid value
            }
        });

        sssField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9-]*")) {  // Only digits and dashes allowed for SSS
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "SSS must be numeric and can contain dashes.");
                sssField.setText(oldValue);  // Revert to the previous valid value
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
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number must be numeric and can contain dashes.");
                phoneNumberField.setText(oldValue);  // Revert to the previous valid value
            }
        });
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
                            data[0], // employeeNumber
                            data[1], // lastName
                            data[2], // firstName
                            data[3], // birthday
                            data[4], // address
                            data[5], // phoneNumber
                            data[6], // sss
                            data[7], // philHealth
                            data[8], // tin
                            data[9], // pagIbig
                            data[10], // status
                            data[11], // position
                            data[12], // immediateSupervisor
                            data[13], // basicSalary
                            data[14], // riceSubsidy
                            data[15], // phoneAllowance
                            data[16], // clothingAllowance
                            data[17], // grossSemiMonthlyRate
                            data[18]
                    );
                    employeeList.add(emp);
                }
            }
            emp_table.setItems(employeeList);
        } catch (IOException e) {
            e.printStackTrace();
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

            // Basic validation for required fields (still good to have for final check)
            if (employeeNumberField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() ||
                    firstNameField.getText().trim().isEmpty() ||
                    birthdayField.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Employee Number, Last Name, First Name, and Birthday are required.");
                return;
            }

            // This isValidDouble check complements the listeners by also catching empty fields if they're meant to be numeric.
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
                saveChangesButton.setDisable(true);
                deleteemp_button.setDisable(true);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Delete operation cancelled.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee in the table to delete.");
        }
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
            e.printStackTrace();
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