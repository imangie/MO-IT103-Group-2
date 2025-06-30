package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Purpose: Allows users to view an employee's entire information in a separate window.
 * - Logic: when the user clicks on an employee in the table view, they can click on the "View Employee" button
 * - which will open a window showing their entire information.
 */


public class ViewEmployeeDetailsWindow {

    @FXML private Label employeeNumberLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label birthdayLabel;
    @FXML private Label addressLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label sssLabel;
    @FXML private Label philHealthLabel;
    @FXML private Label tinLabel;
    @FXML private Label pagIbigLabel;
    @FXML private Label statusLabel;
    @FXML private Label positionLabel;
    @FXML private Label immediateSupervisorLabel;
    @FXML private Label basicSalaryLabel;
    @FXML private Label riceSubsidyLabel;
    @FXML private Label phoneAllowanceLabel;
    @FXML private Label clothingAllowanceLabel;
    @FXML private Label grossSemiMonthlyLabel;
    @FXML private Label hourlyRateLabel;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

     // Populates the UI labels with the details of the given Employee object.
    public void displayEmployeeDetails(Employee employee) {
        if (employee == null) {
            clearDetails();
            return;
        }

        employeeNumberLabel.setText(employee.getEmployeeNumber());
        lastNameLabel.setText(employee.getLastName());
        firstNameLabel.setText(employee.getFirstName());

        if (employee.getBirthday() != null && !employee.getBirthday().isEmpty()) {
            try {
                birthdayLabel.setText(LocalDate.parse(employee.getBirthday(), DATE_FORMATTER).format(DATE_FORMATTER));
            } catch (DateTimeParseException e) {
                birthdayLabel.setText("Invalid Date");
                System.err.println("Error parsing birthday for Employee " + employee.getEmployeeNumber() + ": " + employee.getBirthday() + " - " + e.getMessage());
            }
        } else {
            birthdayLabel.setText("N/A");
        }

        addressLabel.setText(employee.getAddress());
        phoneNumberLabel.setText(employee.getPhoneNumber());
        sssLabel.setText(employee.getSss());
        philHealthLabel.setText(employee.getPhilHealth());
        tinLabel.setText(employee.getTin());
        pagIbigLabel.setText(employee.getPagIbig());
        statusLabel.setText(employee.getStatus());
        positionLabel.setText(employee.getPosition());
        immediateSupervisorLabel.setText(employee.getImmediateSupervisor());
        basicSalaryLabel.setText(employee.getBasicSalary());
        riceSubsidyLabel.setText(employee.getRiceSubsidy());
        phoneAllowanceLabel.setText(employee.getPhoneAllowance());
        clothingAllowanceLabel.setText(employee.getClothingAllowance());
        grossSemiMonthlyLabel.setText(employee.getGrossSemiMonthlyRate());
        hourlyRateLabel.setText(employee.getHourlyRate());
    }

    // Clears all detail labels
    private void clearDetails() {
        employeeNumberLabel.setText("N/A");
        lastNameLabel.setText("N/A");
        firstNameLabel.setText("N/A");
        birthdayLabel.setText("N/A");
        addressLabel.setText("N/A");
        phoneNumberLabel.setText("N/A");
        sssLabel.setText("N/A");
        philHealthLabel.setText("N/A");
        tinLabel.setText("N/A");
        pagIbigLabel.setText("N/A");
        statusLabel.setText("N/A");
        positionLabel.setText("N/A");
        immediateSupervisorLabel.setText("N/A");
        basicSalaryLabel.setText("N/A");
        riceSubsidyLabel.setText("N/A");
        phoneAllowanceLabel.setText("N/A");
        clothingAllowanceLabel.setText("N/A");
        grossSemiMonthlyLabel.setText("N/A");
        hourlyRateLabel.setText("N/A");
    }

    @FXML
    private void closeWindow() {
        // Get the current stage and close it
        Stage stage = (Stage) employeeNumberLabel.getScene().getWindow();
        stage.close();
    }
}