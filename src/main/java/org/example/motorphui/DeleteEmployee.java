package org.example.motorphui;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/**
 * Purpose: Handles the deletion of employee records.
 * - Confirms the deletion of an employee record.
 * - Removes the employee from the system and updates the data file.
 */


public class DeleteEmployee {

    private final ObservableList<Employee> employeeList;
    private final String EMPLOYEE_DATA_FILE = "src/main/resources/org/example/motorphui/data/motorph_employee_data.csv";

    // Constructor that accepts the employeeList
    public DeleteEmployee(ObservableList<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public void handleDeleteEmployeeButton(Employee selectedEmployee) {
        if (selectedEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Employee: " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + "?");
            alert.setContentText("Are you sure you want to delete this employee record? This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                employeeList.remove(selectedEmployee);
                saveEmployeesToCSV();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Employee Selected");
            alert.setContentText("Please select an employee in the table to delete.");
            alert.showAndWait();
        }
    }

    private void saveEmployeesToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EMPLOYEE_DATA_FILE))) {
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("Failed to save employee data.");
            alert.setContentText("An error occurred while writing to the CSV file.");
            alert.showAndWait();
        }
    }
}
