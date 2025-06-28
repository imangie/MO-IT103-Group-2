package org.example.motorphui.model; // Consider renaming 'model' to 'payslip' or 'data' if more appropriate

public class Payslip {

    // Define preferred column widths for payslip output
    private static final int LABEL_COL_WIDTH = 25;
    private static final int VALUE_COL_WIDTH = 18;

    // Payslip Employee Details
    private String employeeNumber;
    private String employeeName;
    private String position;
    private String payPeriod;
    private String selectedYear;

    // Earnings
    private double monthlyRate;
    private double averageDailyRate;
    private int daysWorked;
    private String regularHoursFormatted;
    private double overtimeEarnings;
    private double grossIncome;

    // Benefits
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double totalBenefits;

    // Deductions
    private double sssContribution;
    private double philhealthContribution;
    private double pagibigContribution;
    private double withholdingTax;
    private double totalDeductions;

    // Summary
    private double netPay;

    // --- Constructor ---
    public Payslip(String employeeNumber, String employeeName, String position, String payPeriod, String selectedYear,
                   double monthlyRate, double averageDailyRate, int daysWorked, String regularHoursFormatted,
                   double overtimeEarnings, double grossIncome, double riceSubsidy, double phoneAllowance,
                   double clothingAllowance, double totalBenefits, double sssContribution,
                   double philhealthContribution, double pagibigContribution, double withholdingTax,
                   double totalDeductions, double netPay) {
        this.employeeNumber = employeeNumber;
        this.employeeName = employeeName;
        this.position = position;
        this.payPeriod = payPeriod;
        this.selectedYear = selectedYear;
        this.monthlyRate = monthlyRate;
        this.averageDailyRate = averageDailyRate;
        this.daysWorked = daysWorked;
        this.regularHoursFormatted = regularHoursFormatted;
        this.overtimeEarnings = overtimeEarnings;
        this.grossIncome = grossIncome;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.totalBenefits = totalBenefits;
        this.sssContribution = sssContribution;
        this.philhealthContribution = philhealthContribution;
        this.pagibigContribution = pagibigContribution;
        this.withholdingTax = withholdingTax;
        this.totalDeductions = totalDeductions;
        this.netPay = netPay;
    }

    // --- Getters for all data fields ---
    public String getEmployeeNumber() { return employeeNumber; }
    public String getEmployeeName() { return employeeName; }
    public String getPosition() { return position; }
    public String getPayPeriod() { return payPeriod; }
    public String getSelectedYear() { return selectedYear; }
    public double getMonthlyRate() { return monthlyRate; }
    public double getAverageDailyRate() { return averageDailyRate; }
    public int getDaysWorked() { return daysWorked; }
    public String getRegularHoursFormatted() { return regularHoursFormatted; }
    public double getOvertimeEarnings() { return overtimeEarnings; }
    public double getGrossIncome() { return grossIncome; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getTotalBenefits() { return totalBenefits; }
    public double getSssContribution() { return sssContribution; }
    public double getPhilhealthContribution() { return philhealthContribution; }
    public double getPagibigContribution() { return pagibigContribution; }
    public double getWithholdingTax() { return withholdingTax; }
    public double getTotalDeductions() { return totalDeductions; }
    public double getNetPay() { return netPay; }


  //Payslop Format
    public String formatPayslip() {
        StringBuilder payslip = new StringBuilder();

        // Header
        int totalLineLength = LABEL_COL_WIDTH + VALUE_COL_WIDTH + 3;
        String headerBorder = String.format("%" + totalLineLength + "s", "").replace(' ', '=');

        payslip.append(headerBorder).append("\n");
        payslip.append(String.format("%" + (totalLineLength / 2 + 10) + "s", "M O T O R P H   P A Y S L I P")).append("\n");
        payslip.append(headerBorder).append("\n\n");

        // Employee Details
        payslip.append("EMPLOYEE DETAILS:\n");
        appendTableLine(payslip, "Employee ID", getEmployeeNumber(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, false, false, false);
        appendTableLine(payslip, "Employee Name", getEmployeeName(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, false, false, false);
        appendTableLine(payslip, "Position", getPosition(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, false, false, false);
        appendTableLine(payslip, "Pay Period", String.format("%s, %s", getPayPeriod(), getSelectedYear()), LABEL_COL_WIDTH, VALUE_COL_WIDTH, false, false, true);
        payslip.append("\n");

        // Earnings
        payslip.append("EARNINGS:\n");
        appendTableLine(payslip, "Monthly Rate", getMonthlyRate(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        appendTableLine(payslip, "Daily Rate (Avg)", getAverageDailyRate(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        appendTableLine(payslip, "Days Worked", (double) getDaysWorked(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, false, false, true);
        appendTableLine(payslip, "Overtime Earnings", getOvertimeEarnings(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        payslip.append(String.format("%-" + (LABEL_COL_WIDTH + 2) + "s %" + (VALUE_COL_WIDTH + 3) + "s\n", "", "-------------------------"));
        appendTableLine(payslip, "GROSS INCOME", getGrossIncome(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        payslip.append("\n");

        // Benefits
        payslip.append("BENEFITS:\n");
        appendTableLine(payslip, "Rice Subsidy", getRiceSubsidy(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        appendTableLine(payslip, "Phone Allowance", getPhoneAllowance(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        appendTableLine(payslip, "Clothing Allowance", getClothingAllowance(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        payslip.append(String.format("%-" + (LABEL_COL_WIDTH + 2) + "s %" + (VALUE_COL_WIDTH + 3) + "s\n", "", "-------------------------"));
        appendTableLine(payslip, "TOTAL BENEFITS", getTotalBenefits(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        payslip.append("\n");

        // Deductions
        payslip.append("DEDUCTIONS:\n");
        appendTableLine(payslip, "SSS Contribution", getSssContribution(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        appendTableLine(payslip, "Philhealth Cont.", getPhilhealthContribution(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        appendTableLine(payslip, "Pag-IBIG Cont.", getPagibigContribution(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        appendTableLine(payslip, "Withholding Tax", getWithholdingTax(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        payslip.append(String.format("%-" + (LABEL_COL_WIDTH + 2) + "s %" + (VALUE_COL_WIDTH + 3) + "s\n", "", "-------------------------"));
        appendTableLine(payslip, "TOTAL DEDUCTIONS", getTotalDeductions(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        payslip.append("\n");

        // Summary
        payslip.append("SUMMARY:\n");
        appendTableLine(payslip, "Gross Income", getGrossIncome(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        appendTableLine(payslip, "Total Benefits", getTotalBenefits(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        appendTableLine(payslip, "Total Deductions", getTotalDeductions(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        payslip.append(String.format("%-" + (LABEL_COL_WIDTH + 2) + "s %" + (VALUE_COL_WIDTH + 3) + "s\n", "", "-------------------------"));
        appendTableLine(payslip, "NET PAY", getNetPay(), LABEL_COL_WIDTH, VALUE_COL_WIDTH, true, true, true);
        payslip.append("\n");

        payslip.append(headerBorder).append("\n");

        return payslip.toString();
    }

    //Helper method to append a formatted line to the StringBuilder for table-like display.
    private void appendTableLine(StringBuilder builder, String label, Object value,
                                 int labelWidth, int valueWidth,
                                 boolean includeCurrencyPrefix, boolean twoDecimalPlaces, boolean numericAlign) {
        String formattedLabel = String.format("%-" + labelWidth + "s", label + ":");
        String formattedValue;

        if (value instanceof Double) {
            String numFormat = twoDecimalPlaces ? "%,." + 2 + "f" : "%,.0f";
            formattedValue = String.format(numFormat, (Double) value);
            if (includeCurrencyPrefix) {
                formattedValue = "Php " + formattedValue;
            }
            formattedValue = String.format("%" + valueWidth + "s", formattedValue);
        } else if (value instanceof Integer) {
            formattedValue = String.format("%,d", (Integer) value);
            if (includeCurrencyPrefix) {
                formattedValue = "Php " + formattedValue;
            }
            formattedValue = String.format("%" + valueWidth + "s", formattedValue);
        } else {
            String stringValue = String.valueOf(value);
            if (stringValue.length() > valueWidth) {
                stringValue = stringValue.substring(0, valueWidth);
            }
            formattedValue = String.format("%-" + valueWidth + "s", stringValue);
        }

        builder.append("  ").append(formattedLabel).append(" ").append(formattedValue).append("\n");
    }
}