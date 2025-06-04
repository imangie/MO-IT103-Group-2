package org.example.motorphui;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {
    private final StringProperty employeeNumber;
    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty sss;
    private final StringProperty philHealth;
    private final StringProperty tin;
    private final StringProperty pagIbig;
    private final StringProperty birthday;
    private final StringProperty address;
    private final StringProperty phoneNumber;
    private final StringProperty position;
    private final StringProperty riceSubsidy;
    private final StringProperty phoneAllowance;
    private final StringProperty clothingAllowance;
    private final StringProperty hourlyRate;
    private final StringProperty basicSalary;

    public Employee(String employeeNumber, String lastName, String firstName, String sss, String philHealth, String tin,
                    String pagIbig, String birthday, String address, String phoneNumber, String position,
                    String riceSubsidy, String phoneAllowance, String clothingAllowance, String hourlyRate,
                    String basicSalary) {
        this.employeeNumber = new SimpleStringProperty(employeeNumber);
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.sss = new SimpleStringProperty(sss);
        this.philHealth = new SimpleStringProperty(philHealth);
        this.tin = new SimpleStringProperty(tin);
        this.pagIbig = new SimpleStringProperty(pagIbig);
        this.birthday = new SimpleStringProperty(birthday);
        this.address = new SimpleStringProperty(address);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.position = new SimpleStringProperty(position);
        this.riceSubsidy = new SimpleStringProperty(riceSubsidy);
        this.phoneAllowance = new SimpleStringProperty(phoneAllowance);
        this.clothingAllowance = new SimpleStringProperty(clothingAllowance);
        this.hourlyRate = new SimpleStringProperty(hourlyRate);
        this.basicSalary = new SimpleStringProperty(basicSalary);
    }

    public StringProperty employeeNumberProperty() { return employeeNumber; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty sssProperty() { return sss; }
    public StringProperty philHealthProperty() { return philHealth; }
    public StringProperty tinProperty() { return tin; }
    public StringProperty pagIbigProperty() { return pagIbig; }

    public String getEmployeeNumber() { return employeeNumber.get(); }
    public String getLastName() { return lastName.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getSss() { return sss.get(); }
    public String getPhilHealth() { return philHealth.get(); }
    public String getTin() { return tin.get(); }
    public String getPagIbig() { return pagIbig.get(); }
    public String getBirthday() { return birthday.get(); }
    public String getAddress() { return address.get(); }
    public String getPhoneNumber() { return phoneNumber.get(); }
    public String getPosition() { return position.get(); }
    public String getRiceSubsidy() { return riceSubsidy.get(); }
    public String getPhoneAllowance() { return phoneAllowance.get(); }
    public String getClothingAllowance() { return clothingAllowance.get(); }
    public String getHourlyRate() { return hourlyRate.get(); }
    public String getBasicSalary() { return basicSalary.get(); }

    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber.set(employeeNumber); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public void setSss(String sss) { this.sss.set(sss); }
    public void setPhilHealth(String philHealth) { this.philHealth.set(philHealth); }
    public void setTin(String tin) { this.tin.set(tin); }
    public void setPagIbig(String pagIbig) { this.pagIbig.set(pagIbig); }
    public void setBirthday(String birthday) { this.birthday.set(birthday); }
    public void setAddress(String address) { this.address.set(address); }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber.set(phoneNumber); }
    public void setPosition(String position) { this.position.set(position); }
    public void setRiceSubsidy(String riceSubsidy) { this.riceSubsidy.set(riceSubsidy); }
    public void setPhoneAllowance(String phoneAllowance) { this.phoneAllowance.set(phoneAllowance); }
    public void setClothingAllowance(String clothingAllowance) { this.clothingAllowance.set(clothingAllowance); }
    public void setHourlyRate(String hourlyRate) { this.hourlyRate.set(hourlyRate); }
    public void setBasicSalary(String basicSalary) { this.basicSalary.set(basicSalary); }

}