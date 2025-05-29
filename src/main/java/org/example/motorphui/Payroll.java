package org.example.motorphui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class Payroll {

    @FXML
    private TableView<Employee> emp_table;
    @FXML
    private TableColumn<Employee, String> empNumColumn, lastNameColumn, firstNameColumn, sssColumn, philHealthColumn, tinColumn, pagIbigColumn;
    @FXML
    private Button genPayrollButton;

    public void initialize() {
        // Bind each column to the Employee properties
        empNumColumn.setCellValueFactory(cell -> cell.getValue().employeeNumberProperty());
        lastNameColumn.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
        firstNameColumn.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
        sssColumn.setCellValueFactory(cell -> cell.getValue().sssProperty());
        philHealthColumn.setCellValueFactory(cell -> cell.getValue().philHealthProperty());
        tinColumn.setCellValueFactory(cell -> cell.getValue().tinProperty());
        pagIbigColumn.setCellValueFactory(cell -> cell.getValue().pagIbigProperty());

        // Load sample data
        ObservableList<Employee> employees = FXCollections.observableArrayList(
                new Employee("10001", "Garcia", "Manuel III", "44-4506057-3", "820126853951", "442-605-657-000", "691295330870"),
                new Employee("10002", "Lim", "Antonio", "52-2061274-9", "331735646338", "683-102-776-000", "663904995411"),
                new Employee("10003", "Aquino", "Bianca Sofia", "30-8870406-2", "177451189665", "971-711-280-000", "171519773969"),
                new Employee("10004", "Reyes", "Isabella", "40-2511815-0", "341911411254", "876-809-437-000", "416946776041"),
                new Employee("10005", "Hernandez", "Eduard", "50-5577638-1", "957436191812", "031-702-374-000", "952347222457"),
                new Employee("10006", "Villanueva", "Andrea Mae", "49-1632020-8", "382189453145", "317-674-022-000", "441093369646"),
                new Employee("10007", "San Jose", "Brad", "40-2400714-1", "239192926939", "672-474-690-000", "210850209964"),
                new Employee("10008", "Romualdez", "Alice", "55-4476527-2", "545652640232", "888-572-294-000", "211385556888"),
                new Employee("10009", "Atienza", "Rosie", "41-0644692-3", "708988234853", "604-997-793-000", "260107732354"),
                new Employee("10010", "Alvaro", "Roderick", "64-7605054-4", "578114853194", "525-420-419-000", "799254095212"),
                new Employee("10011", "Salcedo", "Anthony", "26-9647608-3", "126445315651", "210-805-911-000", "218002473454"),
                new Employee("10012", "Lopez", "Josie", "44-8563448-3", "431709011012", "218-489-737-000", "113071293354"),
                new Employee("10013", "Farala", "Martha", "45-5656375-0", "233693897247", "210-835-851-000", "631130283546"),
                new Employee("10014", "Martinez", "Leila", "27-2090996-4", "515741057496", "275-792-513-000", "101205445886"),
                new Employee("10015", "Romualdez", "Fredrick", "26-8768374-1", "308366860059", "598-065-761-000", "223057707853"),
                new Employee("10016", "Mata", "Christian", "49-2959312-6", "824187961962", "103-100-522-000", "631052853464"),
                new Employee("10017", "De Leon", "Selena", "27-2090208-8", "587272469938", "482-259-498-000", "719007608464"),
                new Employee("10018", "San Jose", "Allison", "45-3251383-0", "745148459521", "121-203-336-000", "114901859343"),
                new Employee("10019", "Rosario", "Cydney", "49-1629900-2", "579253435499", "122-244-511-000", "265104358643"),
                new Employee("10020", "Bautista", "Mark", "49-1647342-5", "399665157135", "273-970-941-000", "260054585575"),
                new Employee("10021", "Lazaro", "Darlene", "45-5617168-2", "606386917510", "354-650-951-000", "104907708845"),
                new Employee("10022", "Delos Santos", "Kolby", "52-0109570-6", "357451271274", "187-500-345-000", "113017988667"),
                new Employee("10023", "Santos", "Vella", "52-9883524-3", "548670482885", "101-558-994-000", "360028104576"),
                new Employee("10024", "Del Rosario", "Tomas", "45-5866331-6", "953901539995", "560-735-732-000", "913108649964"),
                new Employee("10025", "Tolentino", "Jacklyn", "47-1692793-0", "753800654114", "841-177-857-000", "210546661243"),
                new Employee("10026", "Gutierrez", "Percival", "40-9504657-8", "797639382265", "502-995-671-000", "210897095686"),
                new Employee("10027", "Manalaysay", "Garfield", "45-3298166-4", "810909286264", "336-676-445-000", "211274476563"),
                new Employee("10028", "Villegas", "Lizeth", "40-2400719-4", "934389652994", "210-395-397-000", "122238077997"),
                new Employee("10029", "Ramos", "Carol", "60-1152206-4", "351830469744", "395-032-717-000", "212141893454"),
                new Employee("10030", "Maceda", "Emelia", "54-1331005-0", "465087894112", "215-973-013-000", "515012579765"),
                new Employee("10031", "Aguilar", "Delia", "52-1859253-1", "136451303068", "599-312-588-000", "110018813465"),
                new Employee("10032", "Castro", "John Rafael", "26-7145133-4", "601644902402", "404-768-309-000", "6.97764E+11"),
                new Employee("10033", "Martinez", "Carlos Ian", "11-5062972-7", "380685387212", "256-436-296-000", "993372963726"),
                new Employee("10034", "Santos", "Beatriz", "20-2987501-5", "918460050077", "911-529-713-000", "874042259378")
        );

        emp_table.setItems(employees);
    }

    @FXML
    private void onGeneratePayroll() {
        Employee selectedEmployee = emp_table.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            generatePayrollForEmployee(selectedEmployee);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an employee to generate payroll.");
            alert.showAndWait();
        }
    }

    private void generatePayrollForEmployee(Employee employee) {
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
}
    // Initialization method to set up the TableView and its columns
    // Set the columns' cell value factories (this binds each column to a property in the Employee class)
    // Then load the employee data into the table

    // Create a method for the "Generate Payroll" Button
    // Get the SELECTED employee from the TableView
    // This is where the logic for generating the payroll will go
    // Generate a payroll slip for the selected employee

    // Example: generatePayrollForEmployee(selectedEmployee);
    // After generating the payroll, display it in a new window (payslip.fxml)

    // If no employee is selected, show a message to the user