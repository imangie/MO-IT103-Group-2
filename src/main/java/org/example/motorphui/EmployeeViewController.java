package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class EmployeeViewController {
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
}
