package org.example.motorphui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.FileReader;
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
    private Button deleteemp_button;
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

        // Load CSV
        loadEmployeesFromCSV("src/main/resources/org/example/motorphui/data/motorph_employee_data.csv"); //
    }

    private void loadEmployeesFromCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1); // Allow empty fields

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
}
