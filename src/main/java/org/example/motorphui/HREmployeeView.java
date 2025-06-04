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
import java.util.Optional;

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
    private Button deleteemp_button;

    // Declare columns for each property in Employee class
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
        // Optional: set UI minimums
        root.setMinWidth(1440);
        root.setMinHeight(1024);

        emp_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set column cell value factories
        empNumColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        sssColumn.setCellValueFactory(new PropertyValueFactory<>("sss"));
        philHealthColumn.setCellValueFactory(new PropertyValueFactory<>("philHealth"));
        tinColumn.setCellValueFactory(new PropertyValueFactory<>("tin"));
        pagIbigColumn.setCellValueFactory(new PropertyValueFactory<>("pagIbig"));

        // Disable viewandupdate_button initially
        viewandupdate_button.setDisable(true);

        // Enable viewandupdate_button when a row is selected
        emp_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            viewandupdate_button.setDisable(newSelection == null);
        });

        // Load CSV
        loadEmployeesFromCSV(CSV_FILE_PATH);
    }

    private void loadEmployeesFromCSV(String filePath) {
        employeeList.clear(); 
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1); // Allow empty fields

                // Ensure data array has enough elements
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
                    System.err.println("Invalid row (skipped): " + line);
                }
            }
            emp_table.setItems(employeeList);
        } catch (IOException e) {
            System.err.println("Error loading CSV: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText("Failed to load employee data.");
            alert.setContentText("Check if the CSV file exists at:\n" + filePath);
            alert.showAndWait();
        }
    }

    private void saveEmployeesToCSV(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header (assuming a fixed header for now)
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
                        "", // Status (assuming empty for now, not in Employee class)
                        emp.getPosition(),
                        "", // Immediate Supervisor (assuming empty for now, not in Employee class)
                        emp.getBasicSalary(),
                        emp.getRiceSubsidy(),
                        emp.getPhoneAllowance(),
                        emp.getClothingAllowance(),
                        "", // Gross Semi-monthly Rate (assuming empty for now, not in Employee class)
                        emp.getHourlyRate()
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
                controller.setParentController(this); // Pass this controller to the child

                Stage stage = new Stage();
                stage.setTitle("View and Update Employee");
                stage.initModality(Modality.APPLICATION_MODAL); // Block other windows
                stage.setScene(new Scene(parent));
                stage.setResizable(false);
                stage.showAndWait(); // Wait for the pop-up to close

                // After the pop-up closes, refresh the table data
                refreshTable();

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
        emp_table.refresh();
        saveEmployeesToCSV(CSV_FILE_PATH);
        loadEmployeesFromCSV(CSV_FILE_PATH);
    }


    // Implement Add Employee logic (placeholder)
    @FXML
    private void handleAddEmployeeButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Employee");
        alert.setHeaderText("Add Employee functionality not yet implemented.");
        alert.setContentText("This feature will allow adding new employee records.");
        alert.showAndWait();
    }

    // Implement Delete Employee logic (placeholder)
    @FXML
    private void handleDeleteEmployeeButton() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Employee: " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + "?");
            alert.setContentText("Are you sure you want to delete this employee record? This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                employeeList.remove(selectedEmployee);
                saveEmployeesToCSV(CSV_FILE_PATH); // Save changes after deletion
                refreshTable(); // Refresh the table
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Deletion Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Employee record deleted successfully.");
                successAlert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Employee Selected");
            alert.setContentText("Please select an employee in the table to delete.");
            alert.showAndWait();
        }
    }
}