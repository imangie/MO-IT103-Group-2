package org.example.motorphui;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {
        private final SimpleStringProperty employeeNumber;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty sss;
        private final SimpleStringProperty philHealth;
        private final SimpleStringProperty tin;
        private final SimpleStringProperty pagIbig;

        public Employee(String employeeNumber, String lastName, String firstName, String sss, String philHealth, String tin, String pagIbig) {
            this.employeeNumber = new SimpleStringProperty(employeeNumber);
            this.lastName = new SimpleStringProperty(lastName);
            this.firstName = new SimpleStringProperty(firstName);
            this.sss = new SimpleStringProperty(sss);
            this.philHealth = new SimpleStringProperty(philHealth);
            this.tin = new SimpleStringProperty(tin);
            this.pagIbig = new SimpleStringProperty(pagIbig);
        }

        public String getEmployeeNumber() { return employeeNumber.get(); }
        public String getLastName() { return lastName.get(); }
        public String getFirstName() { return firstName.get(); }
        public String getSss() { return sss.get(); }
        public String getPhilHealth() { return philHealth.get(); }
        public String getTin() { return tin.get(); }
        public String getPagIbig() { return pagIbig.get(); }

        public StringProperty employeeNumberProperty() { return employeeNumber; }
        public StringProperty lastNameProperty() { return lastName; }
        public StringProperty firstNameProperty() { return firstName; }
        public StringProperty sssProperty() { return sss; }
        public StringProperty philHealthProperty() { return philHealth; }
        public StringProperty tinProperty() { return tin; }
        public StringProperty pagIbigProperty() { return pagIbig; }
    }
