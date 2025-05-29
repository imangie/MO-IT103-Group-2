package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Payslip {

    @FXML private Label empNumLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label sssLabel;
    @FXML private Label philHealthLabel;
    @FXML private Label tinLabel;
    @FXML private Label pagIbigLabel;

    public void setEmployee(Employee employee) {
        empNumLabel.setText(employee.getEmployeeNumber());
        firstNameLabel.setText(employee.getFirstName());
        lastNameLabel.setText(employee.getLastName());
        sssLabel.setText(employee.getSss());
        philHealthLabel.setText(employee.getPhilHealth());
        tinLabel.setText(employee.getTin());
        pagIbigLabel.setText(employee.getPagIbig());
    }
}
