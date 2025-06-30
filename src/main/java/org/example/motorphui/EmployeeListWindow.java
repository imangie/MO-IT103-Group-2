package org.example.motorphui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.Optional;


/**
 * Purpose: Manages and displays the main list of all employees for HR,
 * providing tools for employee record management and payroll slip generation.
 * - Displays a comprehensive table of employee records including personal and statutory details.
 * - Facilitates adding new employee records through a dedicated window.
 * - Provides functionality to generate and view payroll slips for selected employees,
 * including calculation of monthly hours from attendance records.
 * - Loads and persists employee data to/from a CSV file.
 */


public class EmployeeListWindow {

    @FXML
    private TableView<Employee> emp_table;
    @FXML
    private Button newEmployeeButton;
    @FXML
    private Button viewEmployeeButton;
    @FXML
    private TableColumn<Employee, String> empNumColumn, lastNameColumn, firstNameColumn, sssColumn, philHealthColumn, tinColumn, pagIbigColumn;
    @FXML
    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    private static final String EMPLOYEE_DATA_FILE = "src/main/resources/org/example/motorphui/data/motorph_employee_data.csv";

    public void initialize() {
        // Set up table column cell value factories
        empNumColumn.setCellValueFactory(cellData -> cellData.getValue().employeeNumberProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        sssColumn.setCellValueFactory(cellData -> cellData.getValue().sssProperty());
        philHealthColumn.setCellValueFactory(cellData -> cellData.getValue().philHealthProperty());
        tinColumn.setCellValueFactory(cellData -> cellData.getValue().tinProperty());
        pagIbigColumn.setCellValueFactory(cellData -> cellData.getValue().pagIbigProperty());

        // Load employee data into the table on initialization
        loadEmployeesFromCSV();

        // Add double-click event listener to the TableView for updating employees
        emp_table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && emp_table.getSelectionModel().getSelectedItem() != null) {
                openUpdateEmployeeWindow();
            }
        });
    }

    private void calculatePayrollForEmployee(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("payslip.fxml"));
            Parent root = loader.load();

            Payslip controller = loader.getController();
            controller.setEmployee(employee);

            Stage stage = new Stage();
            stage.setTitle("Payroll Slip");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCalculatePayroll() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            calculatePayrollForEmployee(selectedEmployee);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "No Selection", "Please select an employee to generate payroll.");
        }
    }

    @FXML
    private void handleViewEmployeeButton() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to view their details.");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view_employee_details_window.fxml"));
            Parent root = fxmlLoader.load();

            ViewEmployeeDetailsWindow viewController = fxmlLoader.getController();
            viewController.displayEmployeeDetails(selectedEmployee); // Pass the selected employee

            Stage viewStage = new Stage();
            viewStage.setTitle("Employee Details: " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());
            viewStage.setScene(new Scene(root));
            viewStage.initModality(Modality.APPLICATION_MODAL);
            viewStage.setResizable(false);
            viewStage.showAndWait(); // Show and wait for it to be closed
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the employee details window.");
        }
    }

    public void addEmployee(Employee employee) {
        employeeData.add(employee);
        saveEmployeesToCSV(EMPLOYEE_DATA_FILE);
        refreshTable();
    }

    //Refreshes the TableView by reloading data from the CSV file.
    //This method is public so it can be called from the other controllers.
    public void refreshTable() {
        loadEmployeesFromCSV();
    }

    @FXML
    public void openAddEmployeeWindow() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Add Employee");
        confirmation.setHeaderText("Adding New Employee?");
        confirmation.setContentText("Are you sure you want to add a new employee record?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/motorphui/add_employee.fxml"));
                Parent root = loader.load();

                NewEmployee addController = loader.getController();
                addController.SetParentController(this);

                Stage stage = new Stage();
                stage.setTitle("Add New Employee");
                stage.setScene(new Scene(root));
                // Refresh table when the add employee window closes
                stage.setOnHidden(event -> {
                    System.out.println("Add employee window closed. Refreshing Employee List table.");
                    refreshTable();
                });
                stage.showAndWait();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to open Add Employee form: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Add employee operation cancelled.");
        }
    }

    // Opens the HRUpdateEmployeeInformation window for the selected employee.
    // This method is triggered by double-clicking a row in the TableView.
    private void openUpdateEmployeeWindow() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.WARNING, "No Employee Selected", "Please select an employee from the list to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/motorphui/hr_update_employee_information.fxml"));
            Parent root = loader.load();

            UpdateEmployeeInformationWindow updateController = loader.getController();

            updateController.displayEmployeeDetails(selectedEmployee);
            updateController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Update Employee Information");
            stage.setScene(new Scene(root));

            // Refresh the table when the update window closes
            stage.setOnHidden(event -> {
                System.out.println("Update window closed. Refreshing Employee List table.");
                refreshTable();
            });

            stage.showAndWait();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to open Update Employee form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveEmployeesToCSV(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,PhilHealth #,TIN #,Pag-Ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate\n");

            for (Employee emp : employeeData) {
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

    private void loadEmployeesFromCSV() {
        employeeData.clear(); // Clear existing data before loading new
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DATA_FILE))) {
            reader.readLine(); // Skip header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 19) {
                    Employee emp = new Employee(
                            data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9],
                            data[10], data[11], data[12], data[13], data[14], data[15], data[16], data[17], data[18]
                    );
                    employeeData.add(emp);
                } else {
                    System.err.println("Skipping malformed line in CSV: " + line);
                }
            }
            emp_table.setItems(employeeData); // Set the updated data to the TableView
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load employee data.");
        }
    }
}