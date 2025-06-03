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

import java.io.*;
import java.nio.file.Files;

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

        addemp_button.setOnAction(event -> openAddEmployeeWindow());

        loadEmployeesFromCSV("motorph_employee_data.csv");
    }

    private void loadEmployeesFromCSV(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Not Found");
            alert.setHeaderText("Employee data file not found");
            alert.setContentText("Please make sure " + filePath + " exists.");
            alert.showAndWait();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // read header and discard

            employeeList.clear();  // clear old data

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);

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
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText("Failed to load employee data.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void openAddEmployeeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/motorphui/add_employee.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Employee");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText("Failed to open Add Employee form.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}