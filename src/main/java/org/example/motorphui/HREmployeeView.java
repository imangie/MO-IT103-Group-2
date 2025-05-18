package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class HREmployeeView {
    @FXML
    private AnchorPane root;
    @FXML
    private TableView emp_table;
    @FXML
    private Label emp_info_label;
    @FXML
    private Button viewandupdate_button;
    @FXML
    private Button addemp_button;
    @FXML
    private Button deleteemp_button;

    @FXML
    public void initialize() {
        root.setMinWidth(1440);
        root.setMinHeight(1024);

        emp_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    // 1. **TableView for Employee Information**
    // - The table should have columns for Employee #, Last Name, First Name, SSS #, PhilHealth #, TIN #, and Pag-Ibig #.
    // - Provide a list of employees with these details and dynamically populate the TableView.
    // - For each employee, the following data will be displayed: employee number, last name, first name, SSS number, PhilHealth number, TIN number, and Pag-Ibig number.

    // 2. **View and Update Button**
    // - When clicked, the selected employee should be displayed in a popup window (e.g., a modal dialog) where the user can view and edit the employee's information.
    // - The backend will be responsible for fetching the selected employee's data, populating the modal fields with their current information, and allowing modifications.
    // - After making changes, the backend should save the updated information to the database (or data source) when the user submits the form.

    // 3. **Add Employee Button**
    // - When clicked, a popup window will open with input fields for the employee's details: Employee #, Last Name, First Name, SSS #, PhilHealth #, TIN #, Pag-Ibig #.
    // - The backend should handle receiving the data entered in the form and adding the new employee to the system (CSV).
    // - Validation checks for required fields should be done before saving the new employee to the system.


    // 4. **Delete Employee Button**
    // - When clicked, the backend should first show a confirmation dialog asking if the user is sure they want to delete the selected employee. The confirmation message will be: "Are you sure you want to delete [Employee Name]?"
    // - If confirmed, the backend should delete the selected employee's data from the system.
    // - After deletion, the TableView should be refreshed to reflect the changes.

}
