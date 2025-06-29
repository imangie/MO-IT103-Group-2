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

/**
 * Purpose: Manages payroll processing for employees.
 * - Calculates payroll information based on employee salary data.
 * - Allows HR to generate and view payroll records.
 */


public class HREmployeeList {

    @FXML
    private TableView<Employee> emp_table;
    @FXML
    private Button newEmployeeButton;
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

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/org/example/motorphui/data/motorph_employee_data.csv")))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length >= 19) {
                    String empNumber = data[0];
                    String lastName = data[1];
                    String firstName = data[2];
                    String birthday = data[3];
                    String address = data[4];
                    String phoneNumber = data[5];
                    String sss = data[6];
                    String philHealth = data[7];
                    String tin = data[8];
                    String pagIbig = data[9];
                    String status = data[10];
                    String position = data[11];
                    String immediateSupervisor = data[12];
                    String basicSalary = data[13];
                    String riceSubsidy = data[14];
                    String phoneAllowance = data[15];
                    String clothingAllowance = data[16];
                    String grossSemiMonthlyRate = data[17];
                    String hourlyRate = data[18];

                    Employee employee = new Employee(
                            empNumber,          // employeeNumber
                            lastName,           // lastName
                            firstName,         // firstName
                            birthday,          // birthday
                            address,           // address
                            phoneNumber,       // phoneNumber
                            sss,              // sss
                            philHealth,       // philHealth
                            tin,              // tin
                            pagIbig,          // pagIbig
                            status,           // status (from data[10])
                            position,         // position
                            immediateSupervisor, // immediateSupervisor (from data[12])
                            basicSalary,      // basicSalary
                            riceSubsidy,      // riceSubsidy
                            phoneAllowance,   // phoneAllowance
                            clothingAllowance, // clothingAllowance
                            data[17],         // grossSemiMonthlyRate
                            hourlyRate        // hourlyRate
                    );

                    employeeData.add(employee);
                }
            }

            emp_table.setItems(employeeData);

        } catch (Exception e) {
            System.out.println("Error loading employee data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void calculatePayrollForEmployee(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hr_payslip.fxml"));
            Parent root = loader.load();

            HRPayslip controller = loader.getController();
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an employee to generate payroll.");
            alert.showAndWait();
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
            System.out.println("Error reading attendance file: " + e.getMessage());
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
                stage.showAndWait();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to open Add Employee form: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Action Cancelled", "Add employee operation cancelled.");
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