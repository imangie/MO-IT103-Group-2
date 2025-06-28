package org.example.motorphui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;

public class HRPayroll {

    @FXML
    private TableView<Employee> emp_table;
    @FXML
    private Button addemp_button;
    @FXML
    private Button updateemp_button;
    @FXML
    private Button viewemp_button;
    @FXML
    private TableColumn<Employee, String> empNumColumn, lastNameColumn, firstNameColumn, sssColumn, philHealthColumn, tinColumn, pagIbigColumn;
    @FXML
    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    private static final String EMPLOYEE_DATA_FILE = "src/main/resources/org/example/motorphui/data/motorph_employee_data.csv";

    public void initialize() {
        empNumColumn.setCellValueFactory(cellData -> cellData.getValue().employeeNumberProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        sssColumn.setCellValueFactory(cellData -> cellData.getValue().sssProperty());
        philHealthColumn.setCellValueFactory(cellData -> cellData.getValue().philHealthProperty());
        tinColumn.setCellValueFactory(cellData -> cellData.getValue().tinProperty());
        pagIbigColumn.setCellValueFactory(cellData -> cellData.getValue().pagIbigProperty());

        loadEmployeesFromCSV();
        emp_table.setItems(employeeData);
    }

    private void generatePayrollForEmployee(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/motorphui/hr_payslip.fxml"));
            Parent root = loader.load();

            HRPayslip controller = loader.getController();
            controller.setEmployee(employee);

            Stage stage = new Stage();
            stage.setTitle("Payroll Slip");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Payroll Slip window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onGeneratePayroll() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            generatePayrollForEmployee(selectedEmployee);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "No Selection", "Please select an employee to generate payroll.");
        }
    }

    private double calculateMonthlyHours(String empNumber, String month) {
        double totalHours = 0.0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/org/example/motorphui/data/motorph_attendance_records.csv")))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 7 && data[0].equals(empNumber) && data[4].equalsIgnoreCase(month)) {
                    String login = data[5];
                    String logout = data[6];
                    double loginTime = parseTimeToDecimal(login);
                    double logoutTime = parseTimeToDecimal(logout);
                    totalHours += (logoutTime - loginTime);
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Attendance Error", "Error reading attendance file: " + e.getMessage());
            e.printStackTrace();
        }
        return totalHours;
    }

    private double parseTimeToDecimal(String time) {
        try {
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours + (minutes / 60.0);
        } catch (Exception e) {
            System.out.println("Invalid time format: " + time);
            return 0.0;
        }
    }

    public void addEmployee(Employee employee) {
        employeeData.add(employee);
        saveEmployeesToCSV(EMPLOYEE_DATA_FILE);
        refreshTable();
    }

    public void updateEmployee(Employee updatedEmployee) {
        for (int i = 0; i < employeeData.size(); i++) {
            if (employeeData.get(i).getEmployeeNumber().equals(updatedEmployee.getEmployeeNumber())) {
                employeeData.set(i, updatedEmployee);
                break;
            }
        }
        saveEmployeesToCSV(EMPLOYEE_DATA_FILE);
        refreshTable();
    }

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/motorphui/update_employee.fxml"));
                Parent root = loader.load();

                Update addController = loader.getController();
                addController.setParentController(this);
                addController.setEmployeeToUpdate(null);

                Stage stage = new Stage();
                stage.setTitle("Add New Employee");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                refreshTable();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to open Add Employee form: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Add employee operation cancelled.");
        }
    }

    @FXML
    public void openUpdateEmployeeWindow() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/motorphui/update_employee.fxml"));
            Parent root = loader.load();

            Update updateController = loader.getController();
            updateController.setParentController(this);
            updateController.setEmployeeToUpdate(selectedEmployee);

            Stage stage = new Stage();
            stage.setTitle("Update Employee");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshTable();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to open Update Employee form: " + e.getMessage());
        }
    }

    @FXML
    public void openViewEmployeeWindow() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to view.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/motorphui/hr_view_and_update_employee.fxml"));
            Parent root = loader.load();

            HRViewAndUpdateEmployee controller = loader.getController();
            controller.setEmployee(selectedEmployee);
            controller.setReadOnly();

            Stage stage = new Stage();
            stage.setTitle("View Employee Information");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to open View Employee window: " + e.getMessage());
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
        employeeData.clear();
        File file = new File(EMPLOYEE_DATA_FILE);
        if (!file.exists()) {
            emp_table.setItems(employeeData);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_DATA_FILE))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length == 19) {
                    Employee emp = new Employee(
                            data[0], data[1], data[2], data[3], data[4],
                            data[5], data[6], data[7], data[8], data[9],
                            data[10], data[11], data[12], data[13], data[14],
                            data[15], data[16], data[17], data[18]
                    );
                    employeeData.add(emp);
                }
            }
            emp_table.setItems(employeeData);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load employee data.");
        }
    }
}