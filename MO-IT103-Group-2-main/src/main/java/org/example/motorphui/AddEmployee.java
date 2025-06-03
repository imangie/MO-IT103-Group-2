package org.example.motorphui;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;

public class AddEmployee {

    @FXML
    private TextField EmpNumTxtAr, LastNameTxtAr, FirstNameTxtAr, BDayTxtAr, AddressTxtAr, PhoneNumTxtAr,
            SSSNumTxtAr, PHNumTxtAr, TINNumTxtAr, PgbgNumTxtAr, StatusTxtAr, PositionTxtAr,
            ImmedSupTxtAr, BasSalTxtAr1, RiceTxtAr1, PhoneAllowTxtAr, ClothTxtAr,
            GrossSemiTxtAr, HourRateTxtAr;
    @FXML
    private Button addEmpButton;
    @FXML
    private Button cancelButton;

    private final String employeeDataFile = "motorph_employee_data.csv";

    private void initialize() {
        addEmpButton.setDisable(true);

        // Enable add button only if all fields are filled
        TextField[] fields = {EmpNumTxtAr, LastNameTxtAr, FirstNameTxtAr, BDayTxtAr, AddressTxtAr, PhoneNumTxtAr,
                SSSNumTxtAr, PHNumTxtAr, TINNumTxtAr, PgbgNumTxtAr, StatusTxtAr, PositionTxtAr,
                ImmedSupTxtAr, BasSalTxtAr1, RiceTxtAr1, PhoneAllowTxtAr, ClothTxtAr,
                GrossSemiTxtAr, HourRateTxtAr};

        for (TextField field : fields) {
            field.textProperty().addListener((obs, oldVal, newVal) -> {
                boolean allFilled = true;
                for (TextField f : fields) {
                    if (f.getText().trim().isEmpty()) {
                        allFilled = false;
                        break;
                    }
                }
                addEmpButton.setDisable(!allFilled);
            });
        }
    }


    @FXML
    private void onAddButtonClick(ActionEvent event) {
        if (!allFieldsFilled()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields before submitting.");
            return;
        }

        File file = new File(employeeDataFile);
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (!created) throw new IOException("Failed to create the employee data file.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Could not create employee data file: " + e.getMessage());
                return;
            }
        }

        if (!file.canWrite()) {
            showAlert(Alert.AlertType.ERROR, "Permission Denied", "CSV file is read-only. Please enable write permission.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String line = String.join(",",
                    EmpNumTxtAr.getText().trim(),
                    LastNameTxtAr.getText().trim(),
                    FirstNameTxtAr.getText().trim(),
                    BDayTxtAr.getText().trim(),
                    AddressTxtAr.getText().trim(),
                    PhoneNumTxtAr.getText().trim(),
                    SSSNumTxtAr.getText().trim(),
                    PHNumTxtAr.getText().trim(),
                    TINNumTxtAr.getText().trim(),
                    PgbgNumTxtAr.getText().trim(),
                    StatusTxtAr.getText().trim(),
                    PositionTxtAr.getText().trim(),
                    ImmedSupTxtAr.getText().trim(),
                    BasSalTxtAr1.getText().trim(),
                    RiceTxtAr1.getText().trim(),
                    PhoneAllowTxtAr.getText().trim(),
                    ClothTxtAr.getText().trim(),
                    GrossSemiTxtAr.getText().trim(),
                    HourRateTxtAr.getText().trim()
            );

            writer.write(line);
            writer.newLine();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Employee added successfully.");
            clearFields();
            addEmpButton.setDisable(true);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Error", "Error writing to CSV file: " + e.getMessage());
        }
    }

    private boolean allFieldsFilled() {
        TextField[] fields = {EmpNumTxtAr, LastNameTxtAr, FirstNameTxtAr, BDayTxtAr, AddressTxtAr, PhoneNumTxtAr,
                SSSNumTxtAr, PHNumTxtAr, TINNumTxtAr, PgbgNumTxtAr, StatusTxtAr, PositionTxtAr,
                ImmedSupTxtAr, BasSalTxtAr1, RiceTxtAr1, PhoneAllowTxtAr, ClothTxtAr,
                GrossSemiTxtAr, HourRateTxtAr};

        for (TextField f : fields) {
            if (f.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void clearFields() {
        EmpNumTxtAr.clear();
        LastNameTxtAr.clear();
        FirstNameTxtAr.clear();
        BDayTxtAr.clear();
        AddressTxtAr.clear();
        PhoneNumTxtAr.clear();
        SSSNumTxtAr.clear();
        PHNumTxtAr.clear();
        TINNumTxtAr.clear();
        PgbgNumTxtAr.clear();
        StatusTxtAr.clear();
        PositionTxtAr.clear();
        ImmedSupTxtAr.clear();
        BasSalTxtAr1.clear();
        RiceTxtAr1.clear();
        PhoneAllowTxtAr.clear();
        ClothTxtAr.clear();
        GrossSemiTxtAr.clear();
        HourRateTxtAr.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private File getEmployeeDataFile() throws IOException {
        File file = new File("motorph_employee_data.csv");

        if (!file.exists()) {
            InputStream input = getClass().getResourceAsStream("/org/example/motorphui/data/motorph_employee_data.csv");
            if (input == null) {
                throw new FileNotFoundException("Resource CSV not found in classpath.");
            }

            Files.copy(input, file.toPath());
            System.out.println("Copied CSV from resources to: " + file.getAbsolutePath());
        }

        return file;
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}