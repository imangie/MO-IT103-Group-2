// src/main/java/org/example/motorphui/HREmployeeView.java
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HREmployeeView {

    @FXML
    private AnchorPane root;
    @FXML
    private TableView<Employee> emp_table;
    @FXML
    private Label emp_info_label;
    @FXML
    private Button viewandupdate_button;
    @FXML
    private Button addemp_button;


    @FXML
    private TableColumn<Employee, String> empNumColumn;
    @FXML
    private TableColumn<Employee, String> lastNameColumn;
    @FXML
    private TableColumn<Employee, String> firstNameColumn;
    @FXML
    private TableColumn<Employee, String> sssColumn;
    @FXML
    private TableColumn<Employee, String> philHealthColumn;
    @FXML
    private TableColumn<Employee, String> tinColumn;
    @FXML
    private TableColumn<Employee, String> pagIbigColumn;

    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private final String CSV_FILE_PATH = "src/main/resources/org/example/motorphui/data/motorph_employee_data.csv";

    @FXML
    public void initialize() {
        root.setMinWidth(1440);
        root.setMinHeight(1024);

        emp_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        empNumColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        sssColumn.setCellValueFactory(new PropertyValueFactory<>("sss"));
        philHealthColumn.setCellValueFactory(new PropertyValueFactory<>("philHealth"));
        tinColumn.setCellValueFactory(new PropertyValueFactory<>("tin"));
        pagIbigColumn.setCellValueFactory(new PropertyValueFactory<>("pagIbig"));

        // Initially disable update button until an employee is selected
        viewandupdate_button.setDisable(true);

        // Listener to enable/disable view and update button based on table selection
        emp_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            viewandupdate_button.setDisable(!isSelected);
        });

        loadEmployeesFromCSV(CSV_FILE_PATH);
    }

    private void loadEmployeesFromCSV(String filePath) {
        employeeList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Read header line
            if (line == null) {
                System.out.println("CSV file is empty or missing header.");
                return;
            }
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);

                // Ensure data array has at least 19 elements to match expected CSV columns
                if (data.length >= 19) {
                    Employee emp = new Employee(
                            data[0], // employeeNumber
                            data[1], // lastName
                            data[2], // firstName
                            data[6], // sss
                            data[7], // philHealth
                            data[8], // tin
                            data[9], // pagIbig
                            data[3], // birthday
                            data[4], // address
                            data[5], // phoneNumber
                            data[11], // position
                            data[14], // riceSubsidy
                            data[15], // phoneAllowance
                            data[16], // clothingAllowance
                            data[18], // hourlyRate
                            data[13]  // basicSalary
                    );
                    employeeList.add(emp);
                } else {
                    System.err.println("Invalid row (skipped) due to insufficient columns: " + line);
                }
            }
            emp_table.setItems(employeeList);
        } catch (IOException e) {
            System.err.println("Error loading CSV: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText("Failed to load employee data.");
            alert.setContentText("Check if the CSV file exists at:\n" + filePath + "\nError: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void saveEmployeesToCSV(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // Ensure this matches the header in your actual CSV file
            writer.write("Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,PhilHealth #,TIN #,Pag-Ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate\n");

            for (Employee emp : employeeList) {

                writer.write(String.join(",",
                        emp.getEmployeeNumber(),    // 0
                        emp.getLastName(),          // 1
                        emp.getFirstName(),         // 2
                        emp.getBirthday(),          // 3
                        emp.getAddress(),           // 4
                        emp.getPhoneNumber(),       // 5
                        emp.getSss(),               // 6
                        emp.getPhilHealth(),        // 7
                        emp.getTin(),               // 8
                        emp.getPagIbig(),           // 9
                        "",                         // 10: Status (placeholder if not in Employee model, or pass actual status)
                        emp.getPosition(),          // 11
                        "",                         // 12: Immediate Supervisor (placeholder)
                        emp.getBasicSalary(),       // 13
                        emp.getRiceSubsidy(),       // 14
                        emp.getPhoneAllowance(),    // 15
                        emp.getClothingAllowance(), // 16
                        "",                         // 17: Gross Semi-monthly Rate (placeholder)
                        emp.getHourlyRate()         // 18
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving CSV: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("Failed to save employee data.");
            alert.setContentText("An error occurred while writing to the CSV file.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleViewAndUpdateButton() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hr_view_and_update_employee.fxml"));
                Parent parent = loader.load();

                HRViewAndUpdateEmployee controller = loader.getController();
                controller.setEmployee(selectedEmployee);
                controller.setParentController(this);

                Stage stage = new Stage();
                stage.setTitle("MotorPH Portal");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(parent));
                stage.setResizable(false);
                stage.showAndWait();

                refreshTable(); // Refresh table after update/delete operation in the child window

            } catch (IOException e) {
                System.err.println("Error loading view and update employee window: " + e.getMessage());
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not open the update window.");
                alert.setContentText("An error occurred while trying to load the employee update form.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Employee Selected");
            alert.setContentText("Please select an employee in the table to view or update their information.");
            alert.showAndWait();
        }
    }

    public void refreshTable() {
        // Re-load from CSV to pick up all changes (additions, deletions, updates)
        loadEmployeesFromCSV(CSV_FILE_PATH);
    }

    @FXML
    private void handleAddEmployeeButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_employee.fxml"));
            Parent parent = loader.load();

            AddEmployee controller = loader.getController();
            controller.setParentController(this);

            // Generate the next employee number and pass it to the add controller
            String nextEmpNum = generateNextEmployeeNumber();
            controller.setEmployeeNumber(nextEmpNum);

            Stage stage = new Stage();
            stage.setTitle("Add New Employee");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.showAndWait();

            refreshTable(); // Refresh table after the add operation

        } catch (IOException e) {
            System.err.println("Error loading add employee window: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not open the add employee window.");
            alert.setContentText("An error occurred while trying to load the add employee form. Details: " + e.getMessage());
            alert.showAndWait();
        }
    }


     //Generates the next sequential employee number based on existing numbers in the list.
    private String generateNextEmployeeNumber() {
        int maxEmpNum = 0;
        if (employeeList.isEmpty()) {
            return String.format("%05d", 1); // Start with 00001 if list is empty
        }

        for (Employee emp : employeeList) {
            try {
                int currentEmpNum = Integer.parseInt(emp.getEmployeeNumber());
                if (currentEmpNum > maxEmpNum) {
                    maxEmpNum = currentEmpNum;
                }
            } catch (NumberFormatException e) {
                // Log or handle cases where employeeNumber might not be purely numeric
                System.err.println("Non-numeric Employee Number found during ID generation: " + emp.getEmployeeNumber() + ". Skipping for max calculation.");
            }
        }
        // Increment the max number and format it with leading zeros
        return String.format("%05d", maxEmpNum + 1);
    }
    public void updateEmployee(Employee updatedEmployee) {
        boolean foundAndUpdated = false;
        for (int i = 0; i < employeeList.size(); i++) {
            // Find the employee by their unique employee number
            if (employeeList.get(i).getEmployeeNumber().equals(updatedEmployee.getEmployeeNumber())) {
                employeeList.set(i, updatedEmployee); // Replace the old object with the updated one
                foundAndUpdated = true;
                break; // Found and updated, exit loop
            }
        }

        if (foundAndUpdated) {
            saveEmployeesToCSV(CSV_FILE_PATH); // Save changes immediately after updating
        } else {
            System.err.println("Error: Could not find employee " + updatedEmployee.getEmployeeNumber() + " to update.");
        }
    }

   //Called by AddEmployee to add a new employee to the list and save.
    public void addEmployee(Employee newEmployee) {
        employeeList.add(newEmployee);
        saveEmployeesToCSV(CSV_FILE_PATH); // Save changes immediately after adding
    }



    //Called by HRViewAndUpdateEmployee to remove an employee
    public void removeEmployee(Employee employeeToRemove) {
        employeeList.remove(employeeToRemove);
        saveEmployeesToCSV(CSV_FILE_PATH); // Save changes immediately after removal
    }
}
