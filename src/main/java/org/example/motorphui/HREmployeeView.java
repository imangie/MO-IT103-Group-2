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

    // Declare columns for each property in Employee class
    @FXML
    private TableColumn<Employee, String> empNumColumn;
    @FXML
    private TableColumn<Employee, String> lastNameColumn;
    @FXML
    private TableColumn<Employee, String> firstNameColumn;
    @FXML
    private TableColumn<Employee, String> birthdayColumn;
    @FXML
    private TableColumn<Employee, String> addressColumn;
    @FXML
    private TableColumn<Employee, String> phoneNumberColumn;
    @FXML
    private TableColumn<Employee, String> sssColumn;
    @FXML
    private TableColumn<Employee, String> philHealthColumn;
    @FXML
    private TableColumn<Employee, String> tinColumn;
    @FXML
    private TableColumn<Employee, String> pagIbigColumn;
    @FXML
    private TableColumn<Employee, String> statusColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> supervisorColumn;
    @FXML
    private TableColumn<Employee, String> basicSalaryColumn;
    @FXML
    private TableColumn<Employee, String> riceSubsidyColumn;
    @FXML
    private TableColumn<Employee, String> phoneAllowanceColumn;
    @FXML
    private TableColumn<Employee, String> clothingAllowanceColumn;
    @FXML
    private TableColumn<Employee, String> grossSemiMonthlyColumn;
    @FXML
    private TableColumn<Employee, String> hourlyRateColumn;

    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        root.setMinWidth(1440);
        root.setMinHeight(1024);
        emp_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set column cell value factories for each property in the Employee class
        empNumColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        sssColumn.setCellValueFactory(new PropertyValueFactory<>("sss"));
        philHealthColumn.setCellValueFactory(new PropertyValueFactory<>("philHealth"));
        tinColumn.setCellValueFactory(new PropertyValueFactory<>("tin"));
        pagIbigColumn.setCellValueFactory(new PropertyValueFactory<>("pagIbig"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        supervisorColumn.setCellValueFactory(new PropertyValueFactory<>("immediateSupervisor"));
        basicSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("basicSalary"));
        riceSubsidyColumn.setCellValueFactory(new PropertyValueFactory<>("riceSubsidy"));
        phoneAllowanceColumn.setCellValueFactory(new PropertyValueFactory<>("phoneAllowance"));
        clothingAllowanceColumn.setCellValueFactory(new PropertyValueFactory<>("clothingAllowance"));
        grossSemiMonthlyColumn.setCellValueFactory(new PropertyValueFactory<>("grossSemiMonthlyRate"));
        hourlyRateColumn.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));

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
            String line = reader.readLine();

            employeeList.clear();  // Clear old data

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (data.length == 19) {
                    Employee emp = new Employee(
                            data[0],  // employeeNumber
                            data[1],  // lastName
                            data[2],  // firstName
                            data[3],  // birthday
                            data[4],  // address
                            data[5],  // phoneNumber
                            data[6],  // sss
                            data[7],  // philHealth
                            data[8],  // tin
                            data[9],  // pagIbig
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

    //private void openUpdateEmployeeWindow(Employee employee) {
        // initialize the view and update button here, make it able to select an employee from the table, then load the fxml file
        // do the rest in the HRViewAndUpdateEmployee.java
    //}
}
