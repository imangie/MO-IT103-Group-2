package org.example.motorphui;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {
    private final StringProperty employeeNumber;
    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty birthday;
    private final StringProperty address;
    private final StringProperty phoneNumber;
    private final StringProperty sss;
    private final StringProperty philHealth;
    private final StringProperty tin;
    private final StringProperty pagIbig;
    private final StringProperty status;
    private final StringProperty position;
    private final StringProperty immediateSupervisor;
    private final StringProperty basicSalary;
    private final StringProperty riceSubsidy;
    private final StringProperty phoneAllowance;
    private final StringProperty clothingAllowance;
    private final StringProperty grossSemiMonthlyRate;
    private final StringProperty hourlyRate;

    public Employee(String employeeNumber, String lastName, String firstName, String birthday, String address,
                    String phoneNumber, String sss, String philHealth, String tin, String pagIbig, String status,
                    String position, String immediateSupervisor, String basicSalary, String riceSubsidy,
                    String phoneAllowance, String clothingAllowance, String grossSemiMonthlyRate, String hourlyRate) {
        this.employeeNumber = new SimpleStringProperty(employeeNumber);
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.birthday = new SimpleStringProperty(birthday);
        this.address = new SimpleStringProperty(address);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.sss = new SimpleStringProperty(sss);
        this.philHealth = new SimpleStringProperty(philHealth);
        this.tin = new SimpleStringProperty(tin);
        this.pagIbig = new SimpleStringProperty(pagIbig);
        this.status = new SimpleStringProperty(status);
        this.position = new SimpleStringProperty(position);
        this.immediateSupervisor = new SimpleStringProperty(immediateSupervisor);
        this.basicSalary = new SimpleStringProperty(basicSalary);
        this.riceSubsidy = new SimpleStringProperty(riceSubsidy);
        this.phoneAllowance = new SimpleStringProperty(phoneAllowance);
        this.clothingAllowance = new SimpleStringProperty(clothingAllowance);
        this.grossSemiMonthlyRate = new SimpleStringProperty(grossSemiMonthlyRate);
        this.hourlyRate = new SimpleStringProperty(hourlyRate);
    }

    public StringProperty employeeNumberProperty() { return employeeNumber; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty birthdayProperty() { return birthday; }
    public StringProperty addressProperty() { return address; }
    public StringProperty phoneNumberProperty() { return phoneNumber; }
    public StringProperty sssProperty() { return sss; }
    public StringProperty philHealthProperty() { return philHealth; }
    public StringProperty tinProperty() { return tin; }
    public StringProperty pagIbigProperty() { return pagIbig; }
    public StringProperty statusProperty() { return status; }
    public StringProperty positionProperty() { return position; }
    public StringProperty immediateSupervisorProperty() { return immediateSupervisor; }
    public StringProperty basicSalaryProperty() { return basicSalary; }
    public StringProperty riceSubsidyProperty() { return riceSubsidy; }
    public StringProperty phoneAllowanceProperty() { return phoneAllowance; }
    public StringProperty clothingAllowanceProperty() { return clothingAllowance; }
    public StringProperty grossSemiMonthlyRateProperty() { return grossSemiMonthlyRate; }
    public StringProperty hourlyRateProperty() { return hourlyRate; }

    // Getters for the fields
    public String getEmployeeNumber() { return employeeNumber.get(); }
    public String getLastName() { return lastName.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getBirthday() { return birthday.get(); }
    public String getAddress() { return address.get(); }
    public String getPhoneNumber() { return phoneNumber.get(); }
    public String getSss() { return sss.get(); }
    public String getPhilHealth() { return philHealth.get(); }
    public String getTin() { return tin.get(); }
    public String getPagIbig() { return pagIbig.get(); }
    public String getStatus() { return status.get(); }
    public String getPosition() { return position.get(); }
    public String getImmediateSupervisor() { return immediateSupervisor.get(); }
    public String getBasicSalary() { return basicSalary.get(); }
    public String getRiceSubsidy() { return riceSubsidy.get(); }
    public String getPhoneAllowance() { return phoneAllowance.get(); }
    public String getClothingAllowance() { return clothingAllowance.get(); }
    public String getGrossSemiMonthlyRate() { return grossSemiMonthlyRate.get(); }
    public String getHourlyRate() { return hourlyRate.get(); }
}