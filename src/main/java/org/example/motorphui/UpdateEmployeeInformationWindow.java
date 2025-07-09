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
import java.util.Objects;
import java.util.Optional;


/**
 * Purpose: Allows HR to view and manage employee records.
 * - Displays a table of employee records with selected core info.
 * - Provides functionality to view (inline), update (inline), and delete employee information.
 * - Loads and saves employee data from/to a CSV file.
 */


public class UpdateEmployeeInformationWindow {

    @FXML
    private AnchorPane root;
    @FXML
    private TableView<Employee> emp_table;
    @FXML
    private Label emp_info_label;

    //Added buttons for Update worflow
    @FXML    private Button deleteemp_button;
    @FXML    private Button updateButton;
    @FXML    private Button saveButton;
    @FXML    private Button cancelButton;

    // Declare columns for each property in Employee class.
    @FXML    private TableColumn<Employee, String> empNumColumn;
    @FXML    private TableColumn<Employee, String> lastNameColumn;
    @FXML    private TableColumn<Employee, String> firstNameColumn;

    // FXML fields for the inline employee details form on the right
    @FXML    private TextField employeeNumberField;
    @FXML    private TextField lastNameField;
    @FXML    private TextField firstNameField;
    @FXML    private DatePicker birthdayField;
    @FXML    private TextField addressField;
    @FXML    private TextField phoneNumberField;
    @FXML    private TextField sssField;
    @FXML    private TextField philHealthField;
    @FXML    private TextField tinField;
    @FXML    private TextField pagIbigField;
    @FXML    private TextField positionField;
    @FXML    private TextField basicSalaryField;
    @FXML    private TextField riceSubsidyField;
    @FXML    private TextField phoneAllowanceField;
    @FXML    private TextField clothingAllowanceField;
    @FXML    private TextField hourlyRateField;
    @FXML    private TextField statusField;
    @FXML    private TextField immediateSupervisorField;
    @FXML    private TextField grossSemiMonthlyField;

    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private EmployeeListWindow parentController;
    private static final String EMPLOYEE_DATA_FILE = "src/main/resources/org/example/motorphui/data/motorph_employee_data.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    private Employee originalEmployeeData; // Store the original employee data for change tracking

    /**
     * Prevent editing on fields unless update mode is enabled.
     */
    private void addDisableEditListeners(javafx.scene.control.TextInputControl field) {
        // Listener for mouse click (when they click on the field)
        field.setOnMousePressed(event -> {
            if (!field.isEditable()) {
                showAlert(Alert.AlertType.INFORMATION, "Editing Not Enabled",
                        "Please click the 'Update' button first to modify employee information.");
                event.consume();
            }
        });

        // Listener for key press (when they try to type into the field)
        field.setOnKeyPressed(event -> {
            if (!field.isEditable()) {
                showAlert(Alert.AlertType.INFORMATION, "Editing Not Enabled",
                        "Please click the 'Update' button first to modify employee information.");
                event.consume();
            }
        });
    }
    /**
     * Set the parent controller for this window.
     */
    public void setParentController(EmployeeListWindow controller) {
        this.parentController = controller;
    }

    /**
     * Initialize the UI components and listeners.
     */
    @FXML
    public void initialize() {
        root.setMinWidth(1440);
        root.setMinHeight(1024);
        emp_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        empNumColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        loadEmployeesFromCSV();

        // Initially disable all text fields and date picker
        setFieldsEditable(false);
        employeeNumberField.setEditable(false);
        employeeNumberField.setStyle("-fx-control-inner-background: #F0F0F0;");

        // Initially hide Save and Cancel buttons
        saveButton.setVisible(false);
        saveButton.setManaged(false); // Does not take up space in display
        cancelButton.setVisible(false);
        cancelButton.setManaged(false);

        // Initially disable Update and Delete buttons
        updateButton.setDisable(true);
        deleteemp_button.setDisable(true);

        // Apply the listeners to all fields that should only be editable after clicking "Update"
        TextField[] fieldsToMonitor = {
                lastNameField, firstNameField, addressField, phoneNumberField, sssField, philHealthField,
                tinField, pagIbigField, positionField, basicSalaryField, riceSubsidyField,
                phoneAllowanceField, clothingAllowanceField, hourlyRateField, statusField,
                immediateSupervisorField, grossSemiMonthlyField
        };

        for (TextField field : fieldsToMonitor) {
            addDisableEditListeners(field);
        }

        // Special handling for DatePicker's internal text field editor
        addDisableEditListeners(birthdayField.getEditor());

        emp_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayEmployeeDetails(newSelection);
                updateButton.setDisable(false);
                deleteemp_button.setDisable(false);
                // When a new row is selected, if we were in edit mode, cancel it.
                if (saveButton.isVisible()) {
                    handleCancelButton();
                }
            } else {
                clearEmployeeDetails();
                updateButton.setDisable(true);
                deleteemp_button.setDisable(true);
                // Ensure edit mode is off if no selection
                setFieldsEditable(false);
                saveButton.setVisible(false);
                saveButton.setManaged(false);
                cancelButton.setVisible(false);
                cancelButton.setManaged(false);
                updateButton.setVisible(true);
                updateButton.setManaged(true);
                originalEmployeeData = null; // This clears original employee data when nothing is selected
            }
        });

        // Real-time Input Validation using Listeners
        // Validation for Employee Number (Numeric Only)
        employeeNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Only digits allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Employee Number must be numeric.");
                employeeNumberField.setText(oldValue);  // Revert to the previous valid value
            }
            updateSaveButtonState();
        });

        // Name Validation (Only Letters and Spaces)
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {  // Only letters and spaces allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Last Name must only contain letters and spaces.");
                lastNameField.setText(oldValue);  // Revert to the previous valid value
            }
            updateSaveButtonState();
        });

        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {  // Only letters and spaces allowed
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "First Name must only contain letters and spaces.");
                firstNameField.setText(oldValue);  // Revert to the previous valid value
            }
            updateSaveButtonState();
        });

        // Add listener for DatePicker
        birthdayField.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSaveButtonState();
        });

        // Add listeners to ALL other editable TextFields
        addressField.textProperty().addListener((obs, oldVal, newVal) -> updateSaveButtonState());
        phoneNumberField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[0-9-]*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Number must be numeric and can contain dashes.");
                phoneNumberField.setText(oldVal); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });
        sssField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[0-9-]*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "SSS must be numeric and can contain dashes.");
                sssField.setText(oldVal); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });
        philHealthField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "PhilHealth must be numeric.");
                philHealthField.setText(oldVal); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });
        tinField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[0-9-]*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "TIN must be numeric and can contain dashes.");
                tinField.setText(oldVal); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });
        pagIbigField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Pag-Ibig must be numeric.");
                pagIbigField.setText(oldVal); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });

        statusField.textProperty().addListener((obs, oldVal, newVal) -> updateSaveButtonState());
        positionField.textProperty().addListener((obs, oldVal, newVal) -> updateSaveButtonState());
        immediateSupervisorField.textProperty().addListener((obs, oldVal, newVal) -> updateSaveButtonState());

        // Validate Salary and Allowances (numeric values allowed, including decimals for some)
        basicSalaryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Basic Salary must be a valid number.");
                basicSalaryField.setText(oldValue); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });

        riceSubsidyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Rice Subsidy must be a valid number.");
                riceSubsidyField.setText(oldValue); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });

        phoneAllowanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Phone Allowance must be a valid number.");
                phoneAllowanceField.setText(oldValue);  // Revert to the previous valid value
            }
            updateSaveButtonState();
        });

        clothingAllowanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Clothing Allowance must be a valid number.");
                clothingAllowanceField.setText(oldValue); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });

        hourlyRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Hourly Rate must be a valid number.");
                hourlyRateField.setText(oldValue); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });
        //Listener for Gross Semi-Monthly Rate (assuming it's editable)
        grossSemiMonthlyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Gross Semi-Monthly Rate must be a valid number.");
                grossSemiMonthlyField.setText(oldValue); // Revert to the previous valid value
            }
            updateSaveButtonState();
        });
    }
    /**
     * Displays the details of the selected employee in the fields.
     */
    public void displayEmployeeDetails(Employee employee) {
        // Store a copy of the original employee data
        this.originalEmployeeData = new Employee(employee.getEmployeeNumber(), employee.getLastName(), employee.getFirstName(),
                employee.getBirthday(), employee.getAddress(), employee.getPhoneNumber(), employee.getSss(),
                employee.getPhilHealth(), employee.getTin(), employee.getPagIbig(), employee.getStatus(),
                employee.getPosition(), employee.getImmediateSupervisor(), employee.getBasicSalary(),
                employee.getRiceSubsidy(), employee.getPhoneAllowance(), employee.getClothingAllowance(),
                employee.getGrossSemiMonthlyRate(), employee.getHourlyRate());

        employeeNumberField.setText(employee.getEmployeeNumber());
        lastNameField.setText(employee.getLastName());
        firstNameField.setText(employee.getFirstName());

        if (employee.getBirthday() != null && !employee.getBirthday().isEmpty()) {
            try {
                birthdayField.setValue(LocalDate.parse(employee.getBirthday(), DATE_FORMATTER));
            } catch (DateTimeParseException e) {
                birthdayField.setValue(null);
                System.err.println("Error parsing birthday for Employee " + employee.getEmployeeNumber() + ": " + employee.getBirthday() + " - " + e.getMessage());
            }
        } else {
            birthdayField.setValue(null);
        }

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
        grossSemiMonthlyField.setText(employee.getGrossSemiMonthlyRate());
        hourlyRateField.setText(employee.getHourlyRate());

        updateSaveButtonState();
    }

    /**
     * Clears all employee detail fields.
     */
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

        originalEmployeeData = null;
        updateSaveButtonState();
    }

    /**
     * Enables or disables editing for the input fields.
     */
    private void setFieldsEditable(boolean editable) {
        TextField[] textFields = {
                lastNameField, firstNameField, addressField,
                phoneNumberField, sssField, philHealthField, tinField, pagIbigField,
                positionField, basicSalaryField, riceSubsidyField, phoneAllowanceField,
                clothingAllowanceField, hourlyRateField, statusField, immediateSupervisorField,
                grossSemiMonthlyField
        };
        for (TextField field : textFields) {
            field.setEditable(editable);
            if (editable) {
                field.setStyle("-fx-control-inner-background: white;"); // Set background to white
            } else {
                field.setStyle("-fx-control-inner-background: #F0F0F0;"); // Set background to light gray
            }
        }
        birthdayField.setEditable(editable);
        // Special handling for DatePicker's internal TextField
        birthdayField.getEditor().setEditable(editable);
        if (editable) {
            birthdayField.getEditor().setStyle("-fx-control-inner-background: white;");
        } else {
            birthdayField.getEditor().setStyle("-fx-control-inner-background: #F0F0F0;");
        }
    }

    /**
     * Loads employee data from the CSV file into the list and table.
     */
    private void loadEmployeesFromCSV() {
        employeeList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DATA_FILE))) {
            reader.readLine(); // Skip header row
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
                            data[18]  // hourlyRate
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

    /**
     * Updates an employee’s record in the list and saves changes.
     */
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

    /**
     * Handles the Update button click, entering edit mode after confirmation.
     */
    @FXML
    private void handleUpdateButton() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            // Added confirmation dialog before entering edit mode
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Update");
            confirmation.setHeaderText("Update Employee Record?");
            confirmation.setContentText("Are you sure you want to update this employee record information?");

            // This defines custom button types
            ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            confirmation.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == buttonTypeYes) {
                // User confirmed, proceed to edit mode
                setFieldsEditable(true);
                updateButton.setVisible(false);
                updateButton.setManaged(false); // Hides button and removes it from layout calculations
                deleteemp_button.setDisable(true); // Disable delete during edit
                saveButton.setVisible(true);
                saveButton.setManaged(true);
                cancelButton.setVisible(true);
                cancelButton.setManaged(true);
            } else {
                // User cancelled the update operation
                showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Update operation cancelled.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to update.");
        }
    }

    /**
     * Handles saving changes to an employee record after validation and confirmation.
     */
    @FXML
    private void handleSaveButton() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {

            // Basic validation for required fields
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

            // Custom ButtonTypes for Yes/Cancel
            ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmation.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
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
                this.originalEmployeeData = updatedEmployee; // Update original data after save
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee record updated successfully!");
                exitEditMode();
                updateSaveButtonState();

                if (parentController != null) {
                    parentController.refreshTable();
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Save operation cancelled.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee in the table to update.");
            exitEditMode(); // Ensure edit mode is exited if no selection
        }
    }

    /**
     * Handles cancelling edit mode and discards changes.
     */
    @FXML
    private void handleCancelButton() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            displayEmployeeDetails(selectedEmployee); // Revert fields to original selected employee's data
        } else {
            clearEmployeeDetails(); // Clear fields if no employee selected
        }
        showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Edit operation cancelled. Changes discarded.");
        exitEditMode();
    }

    /**
     * Exits edit mode and resets buttons and field states.
     */
    private void exitEditMode() {
        setFieldsEditable(false);
        saveButton.setVisible(false);
        saveButton.setManaged(false);
        cancelButton.setVisible(false);
        cancelButton.setManaged(false);
        updateButton.setVisible(true);
        updateButton.setManaged(true);
        // Re-enable delete and update buttons if an item is still selected
        if (emp_table.getSelectionModel().getSelectedItem() != null) {
            updateButton.setDisable(false);
            deleteemp_button.setDisable(false);
        } else {
            updateButton.setDisable(true);
            deleteemp_button.setDisable(true);
        }
    }

    /**
     * Handles deleting an employee after confirmation.
     */
    @FXML
    private void handleDeleteEmployeeButton() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Delete Employee Record?");
            confirmation.setContentText("Are you sure you want to delete employee " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + " (Employee #: " + selectedEmployee.getEmployeeNumber() + ")? This action cannot be undone.");

            // Custom ButtonTypes for Yes/Cancel
            ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmation.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                employeeList.remove(selectedEmployee);
                saveEmployeesToCSV(EMPLOYEE_DATA_FILE);
                refreshTable(); // This refreshes the *current* table in HRUpdateEmployeeInformation
                showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "Employee record deleted successfully.");
                clearEmployeeDetails();
                updateButton.setDisable(true);
                deleteemp_button.setDisable(true);
                originalEmployeeData = null;
                updateSaveButtonState();

                // Notify the parent controller to refresh its table
                if (parentController != null) {
                    parentController.refreshTable();
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Delete operation cancelled.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee in the table to delete.");
        }
    }

    /**
     * Refreshes the employee table and reselects the previously selected employee if possible.
     */
    public void refreshTable() {
        loadEmployeesFromCSV();
        // After refreshing, re-select the employee if it still exists in the list
        Employee currentlySelected = emp_table.getSelectionModel().getSelectedItem();
        if (currentlySelected != null) {
            Optional<Employee> found = employeeList.stream()
                    .filter(e -> e.getEmployeeNumber().equals(currentlySelected.getEmployeeNumber()))
                    .findFirst();
            if (found.isPresent()) {
                emp_table.getSelectionModel().select(found.get());
                displayEmployeeDetails(found.get()); // Ensure fields are updated after refresh
            } else {
                clearEmployeeDetails();
                updateButton.setDisable(true);
                deleteemp_button.setDisable(true);
            }
        } else {
            clearEmployeeDetails();
            updateButton.setDisable(true);
            deleteemp_button.setDisable(true);
        }
    }

    /**
     * Saves the employee list to the CSV file.
     */
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

    /**
     * Shows an alert popup with given type, title, and message.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Checks if a string is a valid double; shows error alert if not.
     */
    private boolean isValidDouble(String text, String fieldName) {
        if (text.trim().isEmpty()) {
            return true; // Consider empty numeric fields as valid for now, or add a specific check if they are required.
        }
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", fieldName + " must be a valid number.");
            return false;
        }
    }

    /**
     * Enables or disables the Save button based on whether changes were made.
     */
    private void updateSaveButtonState() {
        boolean changesMade = areFieldsModified();
        saveButton.setDisable(!changesMade); // Disable if no changes
    }

    /**
     * Checks if any fields have been changed compared to the original employee data.
     */
    private boolean areFieldsModified() {
        if (originalEmployeeData == null) {
            return false; // No employee loaded, so no modifications possible
        }

        // Compare each field. Use nullSafeEquals for strings and nullSafeNumericEquals for numbers.

        if (!nullSafeEquals(lastNameField.getText(), originalEmployeeData.getLastName())) return true;
        if (!nullSafeEquals(firstNameField.getText(), originalEmployeeData.getFirstName())) return true;
        if (!nullSafeEquals(addressField.getText(), originalEmployeeData.getAddress())) return true;
        if (!nullSafeEquals(phoneNumberField.getText(), originalEmployeeData.getPhoneNumber())) return true;
        if (!nullSafeEquals(sssField.getText(), originalEmployeeData.getSss())) return true;
        if (!nullSafeEquals(philHealthField.getText(), originalEmployeeData.getPhilHealth())) return true;
        if (!nullSafeEquals(tinField.getText(), originalEmployeeData.getTin())) return true;
        if (!nullSafeEquals(pagIbigField.getText(), originalEmployeeData.getPagIbig())) return true;
        if (!nullSafeEquals(statusField.getText(), originalEmployeeData.getStatus())) return true;
        if (!nullSafeEquals(positionField.getText(), originalEmployeeData.getPosition())) return true;
        if (!nullSafeEquals(immediateSupervisorField.getText(), originalEmployeeData.getImmediateSupervisor())) return true;

        // Date field
        String currentBirthday = (birthdayField.getValue() != null) ? birthdayField.getValue().format(DATE_FORMATTER) : "";
        if (!nullSafeEquals(currentBirthday, originalEmployeeData.getBirthday())) return true;

        // Numeric fields (parse to Double for robust comparison, handling "100.0" vs "100")
        if (!nullSafeNumericEquals(basicSalaryField.getText(), originalEmployeeData.getBasicSalary())) return true;
        if (!nullSafeNumericEquals(riceSubsidyField.getText(), originalEmployeeData.getRiceSubsidy())) return true;
        if (!nullSafeNumericEquals(phoneAllowanceField.getText(), originalEmployeeData.getPhoneAllowance())) return true;
        if (!nullSafeNumericEquals(clothingAllowanceField.getText(), originalEmployeeData.getClothingAllowance())) return true;
        if (!nullSafeNumericEquals(grossSemiMonthlyField.getText(), originalEmployeeData.getGrossSemiMonthlyRate())) return true;
        if (!nullSafeNumericEquals(hourlyRateField.getText(), originalEmployeeData.getHourlyRate())) return true;

        return false;
    }

    /**
     * Compares two strings safely, treating null or empty as equal.
     */
    private boolean nullSafeEquals(String s1, String s2) {
        String trimmedS1 = (s1 == null || s1.trim().isEmpty()) ? null : s1.trim();
        String trimmedS2 = (s2 == null || s2.trim().isEmpty()) ? null : s2.trim();
        return Objects.equals(trimmedS1, trimmedS2);
    }

    /**
     * Compares two numeric strings as doubles safely, treating invalid or empty as equal.
     */
    private boolean nullSafeNumericEquals(String s1, String s2) {
        Double d1 = null;
        Double d2 = null;
        try {
            if (s1 != null && !s1.trim().isEmpty()) d1 = Double.parseDouble(s1);
        } catch (NumberFormatException e) {
            // Ignore, d1 remains null
        }
        try {
            if (s2 != null && !s2.trim().isEmpty()) d2 = Double.parseDouble(s2);
        } catch (NumberFormatException e) {
            // Ignore, d2 remains null
        }
        return Objects.equals(d1, d2);
    }
}