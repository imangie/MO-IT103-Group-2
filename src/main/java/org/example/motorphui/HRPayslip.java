package org.example.motorphui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Purpose: Allows HR to view and manage employee payslips.
 * - Displays detailed payslip information for employees.
 * - Allows HR to generate and review payslip records.
 */


public class HRPayslip {

    @FXML
    private ChoiceBox<String> PayPeriodCHBox;
    @FXML
    private Label hoursWorkedLabel;

    @FXML
    private Label empNumLabel, NameLabel, sssLabel, philHealthLabel, tinLabel, pagIbigLabel;
    @FXML
    private Label BdayLabel, AddressLabel, PhoneLabel, positionLabel, RiceLabel;
    @FXML
    private Label PhoneAllowLabel, ClothingLabel, RateLabel;
    @FXML
    private Label GrossLabel, SConLabel, PHConLabel, PConLabel, WTLabel, DeductLabel;
    @FXML
    private Label NetLabel;

    private Employee employee;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @FXML
    public void initialize() {
        populatePayPeriodChoiceBox();

        PayPeriodCHBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (employee != null && newVal != null) {

                String[] dates = ((String) newVal).split(" - ");
                if (dates.length == 2) {
                    try {
                        LocalDate startDate = LocalDate.parse(dates[0].trim(), DATE_FORMATTER);
                        LocalDate endDate = LocalDate.parse(dates[1].trim(), DATE_FORMATTER);
                        double hours = calculatePayPeriodHours(employee.getEmployeeNumber(), startDate, endDate);
                        setPayPeriodHours(hours);
                    } catch (DateTimeParseException e) {
                        System.err.println("Error parsing date from pay period string: " + newVal);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void populatePayPeriodChoiceBox() {
        Set<LocalDate> uniqueDates = new TreeSet<>();
        System.out.println("Attempting to read attendance records from: /org/example/motorphui/data/motorph_attendance_records.csv");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/org/example/motorphui/data/motorph_attendance_records.csv")))) {

            System.out.println("Successfully opened stream to CSV file.");
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (isFirstLine) {
                    isFirstLine = false;
                    System.out.println("Skipping header line: " + line);
                    continue;
                }

                String[] data = line.split(",");
                if (data.length >= 4) {
                    try {

                        LocalDate date = LocalDate.parse(data[3].trim(), DATE_FORMATTER);
                        uniqueDates.add(date);

                    } catch (DateTimeParseException e) {
                        System.err.println("Error parsing date from CSV line " + lineNumber + ": " + line + " -> " + e.getMessage());
                    }
                } else {
                    System.err.println("Skipping malformed CSV line " + lineNumber + " (not enough columns): " + line);
                }
            }
            System.out.println("Finished reading CSV. Found " + uniqueDates.size() + " unique dates.");
        } catch (IOException e) {
            System.err.println("Error reading attendance file for pay period generation (IOException): " + e.getMessage());
            e.printStackTrace();
            System.err.println("Ensure 'motorph_attendance_records.csv' is in 'src/main/resources/org/example/motorphui/data/'");
        } catch (NullPointerException e) {
            System.err.println("Error: CSV file not found at the specified path. NullPointerException: " + e.getMessage());
            e.printStackTrace();
            System.err.println("Ensure 'motorph_attendance_records.csv' is in 'src/main/resources/org/example/motorphui/data/' and correctly packaged.");
        }


        List<LocalDate> sortedDates = new ArrayList<>(uniqueDates);
        if (sortedDates.isEmpty()) {
            System.out.println("No unique dates found from CSV. PayPeriodCHBox will be empty.");
            return;
        }
        Collections.sort(sortedDates); // Ensure dates are sorted

        List<String> payPeriods = new ArrayList<>();
        if (!sortedDates.isEmpty()) {
            LocalDate currentPeriodStart = sortedDates.get(0);
            LocalDate lastDateInPeriod = currentPeriodStart.plusDays(4); // A 5-day period (start day + 4 more days)

            for (LocalDate date : sortedDates) {
                if (date.isAfter(lastDateInPeriod)) {

                    payPeriods.add(currentPeriodStart.format(DATE_FORMATTER) + " - " + lastDateInPeriod.format(DATE_FORMATTER));
                    currentPeriodStart = date; // Start the new period with the current date
                    lastDateInPeriod = currentPeriodStart.plusDays(4); // Define the end of the new 5-day period

                    while (currentPeriodStart.isAfter(lastDateInPeriod)) {
                        lastDateInPeriod = lastDateInPeriod.plusDays(5); // Advance by a full period
                    }
                }

            }

            payPeriods.add(currentPeriodStart.format(DATE_FORMATTER) + " - " + lastDateInPeriod.format(DATE_FORMATTER));
        }

        System.out.println("Generated " + payPeriods.size() + " pay periods.");
        if (!payPeriods.isEmpty()) {
            System.out.println("First generated pay period: " + payPeriods.get(0));
            System.out.println("Last generated pay period: " + payPeriods.get(payPeriods.size() - 1));
        }

        PayPeriodCHBox.setItems(FXCollections.observableArrayList(payPeriods));
        System.out.println("Pay periods set to ChoiceBox.");
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;

        double basicSalary = parseDoubleSafe(employee.getBasicSalary());
        double sssContrib = calculateSSSContribution(basicSalary);
        double philHealthContrib = calculatePhilHealthContribution(basicSalary);
        double pagibigContrib = calculatePagibigContribution(basicSalary);
        double withholdingTax = calculateWithholdingTax(basicSalary, sssContrib, philHealthContrib, pagibigContrib);

        empNumLabel.setText("Emp #: " + employee.getEmployeeNumber());
        NameLabel.setText("Name: " + employee.getFirstName() + " " + employee.getLastName());
        sssLabel.setText("SSS: " + employee.getSss());
        philHealthLabel.setText("PhilHealth: " + employee.getPhilHealth());
        tinLabel.setText("TIN: " + employee.getTin());
        pagIbigLabel.setText("Pag-Ibig: " + employee.getPagIbig());
        BdayLabel.setText("Birthday: " + employee.getBirthday());
        AddressLabel.setText("Address: " + employee.getAddress());
        PhoneLabel.setText("Phone Number: " + employee.getPhoneNumber());
        positionLabel.setText("Position: " + employee.getPosition());
        RiceLabel.setText("Rice Subsidy: " + employee.getRiceSubsidy());
        PhoneAllowLabel.setText("Phone Allowance: " + employee.getPhoneAllowance());
        ClothingLabel.setText("Clothing Allowance: " + employee.getClothingAllowance());
        RateLabel.setText("Hourly Rate: " + employee.getHourlyRate());
        SConLabel.setText(String.format("SSS Contribution: %.2f", sssContrib));
        PHConLabel.setText(String.format("PhilHealth Contribution: %.2f", philHealthContrib));
        PConLabel.setText(String.format("Pag-Ibig Contribution: %.2f", pagibigContrib));
        WTLabel.setText(String.format("Withholding Tax: %.2f", withholdingTax));

        double totalDeductions = sssContrib + philHealthContrib + pagibigContrib + withholdingTax;
        DeductLabel.setText(String.format("Total Deductions: %.2f", totalDeductions));
    }

    public void setPayPeriodHours(double hours) {
        hoursWorkedLabel.setText("Hours Worked: " + String.format("%.2f", hours));

        if (employee != null) {
            double hourlyRate = parseDoubleSafe(employee.getHourlyRate());
            double grossPay = hours * hourlyRate;
            GrossLabel.setText(String.format("Gross Pay: %.2f", grossPay));

            double sssContrib = calculateSSSContribution(parseDoubleSafe(employee.getBasicSalary()));
            double philHealthContrib = calculatePhilHealthContribution(parseDoubleSafe(employee.getBasicSalary()));
            double pagibigContrib = calculatePagibigContribution(parseDoubleSafe(employee.getBasicSalary()));
            double withholdingTax = calculateWithholdingTax(
                    parseDoubleSafe(employee.getBasicSalary()),
                    sssContrib,
                    philHealthContrib,
                    pagibigContrib
            );
            double totalDeductions = sssContrib + philHealthContrib + pagibigContrib + withholdingTax;

            double netSalary = grossPay - totalDeductions;
            NetLabel.setText(String.format("Net Salary: %.2f", netSalary));
        }
    }

    private double calculatePayPeriodHours(String empNumber, LocalDate startDate, LocalDate endDate) { // Modified signature
        double totalHours = 0.0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/org/example/motorphui/data/motorph_attendance_records.csv")))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header row
                }

                String[] data = line.split(",");
                if (data.length >= 6 && data[0].trim().equals(empNumber)) {
                    try {
                        LocalDate recordDate = LocalDate.parse(data[3].trim(), DATE_FORMATTER);

                        if (!recordDate.isBefore(startDate) && !recordDate.isAfter(endDate)) {
                            double loginTime = parseTimeToDecimal(data[4].trim());
                            double logoutTime = parseTimeToDecimal(data[5].trim());
                            double hoursWorked = logoutTime - loginTime;

                            if (hoursWorked > 0) {
                                totalHours += hoursWorked;
                            }
                        }
                    } catch (DateTimeParseException e) {
                        System.err.println("Error parsing date in attendance record: " + data[3] + " from line: " + line);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing time in attendance record: " + data[4] + " or " + data[5] + " from line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading attendance file: " + e.getMessage());
            e.printStackTrace();
        }

        return totalHours;
    }

    private double parseTimeToDecimal(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours + (minutes / 60.0);
    }

    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s.replace(",", "").trim());
        } catch (Exception e) {
            System.err.println("Error parsing double: " + s + " - " + e.getMessage());
            return 0.0;
        }
    }

    private double calculateSSSContribution(double basicSalary) {
        if (basicSalary < 3250) return 135.00;
        else if (basicSalary < 3750) return 157.50;
        else if (basicSalary < 4250) return 180.00;
        else if (basicSalary < 4750) return 202.50;
        else if (basicSalary < 5250) return 225.00;
        else if (basicSalary < 5750) return 247.50;
        else if (basicSalary < 6250) return 270.00;
        else if (basicSalary < 6750) return 292.50;
        else if (basicSalary < 7250) return 315.00;
        else if (basicSalary < 7750) return 337.50;
        else if (basicSalary < 8250) return 360.00;
        else if (basicSalary < 8750) return 382.50;
        else if (basicSalary < 9250) return 405.00;
        else if (basicSalary < 9750) return 427.50;
        else if (basicSalary < 10250) return 450.00;
        else if (basicSalary < 10750) return 472.50;
        else if (basicSalary < 11250) return 495.00;
        else if (basicSalary < 11750) return 517.50;
        else if (basicSalary < 12250) return 540.00;
        else if (basicSalary < 12750) return 562.50;
        else if (basicSalary < 13250) return 585.00;
        else if (basicSalary < 13750) return 607.50;
        else if (basicSalary < 14250) return 630.00;
        else if (basicSalary < 14750) return 652.50;
        else if (basicSalary < 15250) return 675.00;
        else if (basicSalary < 15750) return 697.50;
        else if (basicSalary < 16250) return 720.00;
        else if (basicSalary < 16750) return 742.50;
        else if (basicSalary < 17250) return 765.00;
        else if (basicSalary < 17750) return 787.50;
        else if (basicSalary < 18250) return 810.00;
        else if (basicSalary < 18750) return 832.50;
        else if (basicSalary < 19250) return 855.00;
        else if (basicSalary < 19750) return 877.50;
        else if (basicSalary < 20250) return 900.00;
        else if (basicSalary < 20750) return 922.50;
        else if (basicSalary < 21250) return 945.00;
        else if (basicSalary < 21750) return 967.50;
        else if (basicSalary < 22250) return 990.00;
        else if (basicSalary < 22750) return 1012.50;
        else if (basicSalary < 23250) return 1035.00;
        else if (basicSalary < 23750) return 1057.50;
        else if (basicSalary < 24250) return 1080.00;
        else if (basicSalary < 24750) return 1102.50;
        else return 1125.00;
    }

    public static double calculatePhilHealthContribution(double basicSalary) {
        double premium = basicSalary * 0.03;
        if (premium > 1800) premium = 1800;
        return premium / 2;
    }

    public static double calculatePagibigContribution(double basicSalary) {
        if (basicSalary >= 1000 && basicSalary <= 1500) return basicSalary * 0.01;
        else if (basicSalary > 1500) return basicSalary * 0.02;
        else return 0;
    }

    public static double calculateWithholdingTax(double basicSalary, double sss, double philhealth, double pagibig) {
        double taxableIncome = basicSalary - (sss + philhealth + pagibig);
        double tax = 0;

        if (taxableIncome <= 20832) {
            tax = 0;
        } else if (taxableIncome < 33333) {
            tax = (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome < 66667) {
            tax = 2500 + (taxableIncome - 33333) * 0.25;
        } else if (taxableIncome < 166667) {
            tax = (10833 + (taxableIncome - 66667)) * 0.30;
        } else if (taxableIncome < 666667) {
            tax = (40833.33 + (taxableIncome - 166667)) * 0.32;
        } else {
            tax = (200833.33 + (taxableIncome - 666667)) * 0.35;
        }
        return tax;
    }
}