package org.example.motorphui;

import org.example.motorphui.model.Payslip; // Import the Payslip model

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Payroll {

    // Constants for attendance calculation
    private static final String WORK_START = "8:00";
    private static final String WORK_END = "17:00"; // 5:00 PM
    private static final int GRACE_PERIOD_MINUTES = 10; // 10 minutes grace period for being late
    private static final int BREAK_HOUR_MINUTES = 60; // 1 hour break

    // Resource path for attendance CSV
    private final String ATTENDANCE_CSV_RESOURCE_PATH = "/org/example/motorphui/data/motorph_attendance_records.csv";

    // Date formatter for attendance records
    private static final DateTimeFormatter ATTENDANCE_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");


    public Payroll() {
    }

    //Calculates the SSS deduction based on the basic monthly pay.
    public double calculateSssDeduction(double basicPay) {
        double sssDeduction = 0;
        if (basicPay < 3250.00) {
            sssDeduction = 135.00;
        } else if (basicPay <= 3750.00) {
            sssDeduction = 157.50;
        } else if (basicPay <= 4250.00) {
            sssDeduction = 180.00;
        } else if (basicPay <= 4750.00) {
            sssDeduction = 202.50;
        } else if (basicPay <= 5250.00) {
            sssDeduction = 225.00;
        } else if (basicPay <= 5750.00) {
            sssDeduction = 247.50;
        } else if (basicPay <= 6250.00) {
            sssDeduction = 270.00;
        } else if (basicPay <= 6750.00) {
            sssDeduction = 292.50;
        } else if (basicPay <= 7250.00) {
            sssDeduction = 315.00;
        } else if (basicPay <= 7750.00) {
            sssDeduction = 337.50;
        } else if (basicPay <= 8250.00) {
            sssDeduction = 360.00;
        } else if (basicPay <= 8750.00) {
            sssDeduction = 382.50;
        } else if (basicPay <= 9250.00) {
            sssDeduction = 405.00;
        } else if (basicPay <= 9750.00) {
            sssDeduction = 427.50;
        } else if (basicPay <= 10250.00) {
            sssDeduction = 450.00;
        } else if (basicPay <= 10750.00) {
            sssDeduction = 472.50;
        } else if (basicPay <= 11250.00) {
            sssDeduction = 495.00;
        } else if (basicPay <= 11750.00) {
            sssDeduction = 517.50;
        } else if (basicPay <= 12250.00) {
            sssDeduction = 540.00;
        } else if (basicPay <= 12750.00) {
            sssDeduction = 562.50;
        } else if (basicPay <= 13250.00) {
            sssDeduction = 585.00;
        } else if (basicPay <= 13750.00) {
            sssDeduction = 607.50;
        } else if (basicPay <= 14250.00) {
            sssDeduction = 630.00;
        } else if (basicPay <= 14750.00) {
            sssDeduction = 652.50;
        } else if (basicPay <= 15250.00) {
            sssDeduction = 675.00;
        } else if (basicPay <= 15750.00) {
            sssDeduction = 697.50;
        } else if (basicPay <= 16250.00) {
            sssDeduction = 720.00;
        } else if (basicPay <= 16750.00) {
            sssDeduction = 742.50;
        } else if (basicPay <= 17250.00) {
            sssDeduction = 765.00;
        } else if (basicPay <= 17750.00) {
            sssDeduction = 787.50;
        } else if (basicPay <= 18250.00) {
            sssDeduction = 810.00;
        } else if (basicPay <= 18750.00) {
            sssDeduction = 832.50;
        } else if (basicPay <= 19250.00) {
            sssDeduction = 855.00;
        } else if (basicPay <= 19750.00) {
            sssDeduction = 877.50;
        } else if (basicPay <= 20250.00) {
            sssDeduction = 900.00;
        } else if (basicPay <= 20750.00) {
            sssDeduction = 922.50;
        } else if (basicPay <= 21250.00) {
            sssDeduction = 945.00;
        } else if (basicPay <= 21750.00) {
            sssDeduction = 967.50;
        } else if (basicPay <= 22250.00) {
            sssDeduction = 990.00;
        } else if (basicPay <= 22750.00) {
            sssDeduction = 1012.50;
        } else if (basicPay <= 23250.00) {
            sssDeduction = 1035.00;
        } else if (basicPay <= 23750.00) {
            sssDeduction = 1057.50;
        } else if (basicPay <= 24250.00) {
            sssDeduction = 1080.00;
        } else if (basicPay <= 24750.00) {
            sssDeduction = 1102.50;
        } else {
            sssDeduction = 1125.00;
        }
        return sssDeduction;
    }

    //Calculates the Philhealth deduction based on the basic monthly pay.
    public double calculatePhilhealthDeduction(double basicPay) {
        double philhealthDeduction;
        double contributionRate = 0.03; //3% Premium Rate

        if (basicPay < 10000) {
            philhealthDeduction = 300.00;
        } else if (basicPay >= 60000) {
            philhealthDeduction = 1800.00;
        } else {
            philhealthDeduction = basicPay * contributionRate;
        }

        return philhealthDeduction / 2; // Employee Share (50%)
    }

    //Calculates the Pag-ibig deduction based on the basic monthly pay.
    public double calculatePagibigDeduction(double totalIncome) {
        double employeeContribution;

        if (totalIncome < 1500) {
            employeeContribution = totalIncome * 0.01; // 1% At least 1,000 to 1,500
        } else { //
            employeeContribution = totalIncome * 0.02; // 2% At Over 1,500
        }

        // The maximum contribution amount is 100.
        if (employeeContribution > 100) {
            employeeContribution = 100;
        }
        return employeeContribution;
    }

    //Calculates the WitholdingTax deduction based on the basic monthly pay.
    public double calculateWithholdingTax(double taxableIncome) {
        double withholdingTax = 0;
        if (taxableIncome <= 20832) {
            withholdingTax = 0; // 20,832 and below = No withholding tax
        } else if (taxableIncome <= 33332) {
            withholdingTax = (taxableIncome - 20832) * 0.20;
        } else if (taxableIncome <= 66667) {
            withholdingTax = 2500 + (taxableIncome - 33332) * 0.25;
        } else if (taxableIncome <= 166667) {
            withholdingTax = 10833.33 + (taxableIncome - 66667) * 0.30;
        } else if (taxableIncome <= 666667) {
            withholdingTax = 40833.33 + (taxableIncome - 166667) * 0.32;
        } else { // 666,667 and above = 200,833.33 plus 35% in excess of 666,667
            withholdingTax = 200833.33 + (taxableIncome - 666667) * 0.35;
        }
        return withholdingTax;
    }

    //Process Attendance fetch from motorph_attendance_records.csv
    public Object[] processAttendance(String[] attendance, String employeeId, double hourlyRate) {
        // Data structure of attendance array:
        // [0] Employee #, [1] Last Name, [2] First Name, [3] Date, [4] Time In, [5] Time Out

        if (!attendance[0].trim().equals(employeeId.trim())) {
            return null;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        LocalTime standardStartTime = LocalTime.parse(WORK_START, timeFormatter);
        LocalTime standardEndTime = LocalTime.parse(WORK_END, timeFormatter);

        LocalTime loginTime;
        LocalTime logoutTime;
        try {
            loginTime = LocalTime.parse(attendance[4], timeFormatter); // Time In
            logoutTime = LocalTime.parse(attendance[5], timeFormatter); // Time Out
        } catch (DateTimeParseException e) {
            System.err.println("  ERROR: PayrollCalculator.processAttendance: DateTimeParseException for time: " + e.getMessage() + " for line: " + String.join(",", attendance));
            return null;
        }

        Duration lateDuration = calculateLateDuration(standardStartTime, loginTime);
        Duration overtimeDuration = calculateOvertimeDuration(standardEndTime, logoutTime);
        Duration breakDuration = Duration.ofMinutes(BREAK_HOUR_MINUTES);
        Duration workedDuration = calculateWorkedDuration(standardStartTime, standardEndTime, loginTime, logoutTime, breakDuration);

        // Calculate regular hours worked (total worked duration minus overtime duration)
        Duration actualRegularWorkedDuration = workedDuration.minus(overtimeDuration);
        if (actualRegularWorkedDuration.isNegative()) {
            actualRegularWorkedDuration = Duration.ZERO; //  no negative regular hours
        }

        // Earnings for the day should represent regular earnings based on regular hours worked
        double dailyRegularEarnings = (hourlyRate > 0) ? (actualRegularWorkedDuration.toMinutes() / 60.0 * hourlyRate) : 0.0;
        // Overtime earnings will be calculated separately

        String dateString = attendance[3]; // Date
        String timeInString = attendance[4]; // Time In
        String timeOutString = attendance[5]; // Time Out

        String late = formatMinutesToHoursMinutes((int) lateDuration.toMinutes());
        String ot = formatMinutesToHoursMinutes((int) overtimeDuration.toMinutes());
        String regularHoursFormatted = formatMinutesToHoursMinutes((int) actualRegularWorkedDuration.toMinutes());

        // Return Object array: [0] Date String, [1] Time In, [2] Time Out, [3] Late (hh:mm),
        // [4] Overtime (hh:mm), [5] Regular Hours (hh:mm), [6] Daily Regular Earnings
        return new Object[]{dateString, timeInString, timeOutString, late, ot, regularHoursFormatted, dailyRegularEarnings};
    }

  //Helper Method to format minutes
    public String formatMinutesToHoursMinutes(int totalMinutes) {
        if (totalMinutes < 0) {
            return "00:00"; // Should not happen with proper duration calculation, but for safety
        }
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

   //Helper Method to parse minutes
    public long parseHoursMinutesToMinutes(String hhMmString) {
        if (hhMmString == null || !hhMmString.matches("\\d{2}:\\d{2}")) {
            return 0;
        }
        String[] parts = hhMmString.split(":");
        try {
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            return hours * 60 + minutes;
        } catch (NumberFormatException e) {
            System.err.println("ERROR: PayrollCalculator.parseHoursMinutesToMinutes: Invalid number format for time string: " + hhMmString + ". Error: " + e.getMessage());
            return 0;
        }
    }

   //Calculate late duration
    public Duration calculateLateDuration(LocalTime standardStartTime, LocalTime loginTime) {
        if (loginTime.isAfter(standardStartTime.plusMinutes(GRACE_PERIOD_MINUTES))) {
            return Duration.between(standardStartTime.plusMinutes(GRACE_PERIOD_MINUTES), loginTime);
        } else {
            return Duration.ZERO;
        }
    }

 //Calulate overtime duration
    public Duration calculateOvertimeDuration(LocalTime standardEndTime, LocalTime logoutTime) {
        if (logoutTime.isAfter(standardEndTime)) {
            return Duration.between(standardEndTime, logoutTime);
        } else {
            return Duration.ZERO;
        }
    }

   //Calculate Worked Hours
    public Duration calculateWorkedDuration(LocalTime standardStartTime, LocalTime standardEndTime, LocalTime loginTime, LocalTime logoutTime, Duration breakDuration) {
        // Determine the actual start time for calculating paid hours (later of login or standard start)
        LocalTime effectiveStartTime = loginTime.isBefore(standardStartTime) ? standardStartTime : loginTime;

        // Determine the actual end time for calculating paid hours (earlier of logout or standard end)
        LocalTime effectiveEndTime = logoutTime.isAfter(standardEndTime) ? standardEndTime : logoutTime;

        Duration workedTimeBeforeBreak;
        if (effectiveStartTime.isBefore(effectiveEndTime)) {
            workedTimeBeforeBreak = Duration.between(effectiveStartTime, effectiveEndTime);
        } else {
            workedTimeBeforeBreak = Duration.ZERO; // No work duration if effective start is after effective end
        }

        Duration netWorkedTime = workedTimeBeforeBreak.minus(breakDuration);
        if (netWorkedTime.isNegative()) {
            netWorkedTime = Duration.ZERO; // Cannot have negative worked time
        }
        return netWorkedTime;
    }

   //Reads attendance record for Employee Number from CSV
    public List<String[]> getAttendanceRecordsForPeriod(String employeeNumber, String year, int monthValue, int dayRangeStart, int dayRangeEnd) {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(ATTENDANCE_CSV_RESOURCE_PATH)))) {
            if (reader == null) {
                System.err.println("ERROR: PayrollCalculator.getAttendanceRecordsForPeriod: Resource stream is NULL for " + ATTENDANCE_CSV_RESOURCE_PATH);
                return records;
            }
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                // Ensure sufficient columns (at least Employee #, Date, Time In, Time Out)
                if (data.length >= 6 && data[0].trim().equals(employeeNumber.trim())) {
                    try {
                        LocalDate date = LocalDate.parse(data[3].trim(), ATTENDANCE_DATE_FORMATTER);
                        if (String.valueOf(date.getYear()).equals(year) &&
                                date.getMonthValue() == monthValue &&
                                date.getDayOfMonth() >= dayRangeStart &&
                                date.getDayOfMonth() <= dayRangeEnd) {
                            records.add(data);
                        }
                    } catch (DateTimeParseException e) {
                        System.err.println("    ERROR: PayrollCalculator.getAttendanceRecordsForPeriod: DateTimeParseException for date: '" + data[3] + "'. Error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: PayrollCalculator.getAttendanceRecordsForPeriod: IOException during CSV loading: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("ERROR: PayrollCalculator.getAttendanceRecordsForPeriod: NullPointerException (resource not found): " + e.getMessage());
        }
        return records;
    }

    //Get Year from CSV
    public Set<String> getUniqueYearsFromAttendance() {
        Set<String> uniqueYears = new TreeSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(ATTENDANCE_CSV_RESOURCE_PATH)))) {

            if (reader == null) {
                System.err.println("ERROR: PayrollCalculator.getUniqueYearsFromAttendance: Resource stream is NULL for " + ATTENDANCE_CSV_RESOURCE_PATH);
                return uniqueYears;
            }

            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 4) { // Needs at least the date column
                    try {
                        LocalDate date = LocalDate.parse(data[3], ATTENDANCE_DATE_FORMATTER);
                        uniqueYears.add(String.valueOf(date.getYear()));
                    } catch (DateTimeParseException e) {
                        System.err.println("  ERROR: PayrollCalculator.getUniqueYearsFromAttendance: DateTimeParseException for date: '" + data[3] + "' (Expected MM/dd/yyyy). Error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: PayrollCalculator.getUniqueYearsFromAttendance: IOException during CSV loading for years: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("ERROR: PayrollCalculator.getUniqueYearsFromAttendance: NullPointerException (likely resource not found before BufferedReader): " + e.getMessage());
        }
        return uniqueYears;
    }

  //Reads pay period for the selected year from CSV
    public List<String> getSortedPayPeriodsForEmployee(String year, String employeeNumber) {
        Set<String> periodsSet = new TreeSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(ATTENDANCE_CSV_RESOURCE_PATH)))) {

            if (reader == null) {
                System.err.println("ERROR: PayrollCalculator.getSortedPayPeriodsForEmployee: Resource stream is NULL for " + ATTENDANCE_CSV_RESOURCE_PATH);
                return new ArrayList<>();
            }

            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 4 && data[0].equals(employeeNumber)) {
                    try {
                        LocalDate date = LocalDate.parse(data[3], ATTENDANCE_DATE_FORMATTER);
                        if (String.valueOf(date.getYear()).equals(year)) {
                            String monthName = date.getMonth().name().substring(0, 1).toUpperCase() +
                                    date.getMonth().name().substring(1).toLowerCase();

                            // Determine the correct end day for the second half of the month
                            String periodRange;
                            if (date.getDayOfMonth() <= 15) {
                                periodRange = "1-15";
                            } else {
                                // Dynamically get the last day of the month
                                int lastDayOfMonth = date.lengthOfMonth();
                                periodRange = "16-" + lastDayOfMonth;
                            }
                            periodsSet.add(monthName + " " + periodRange);
                        }
                    } catch (DateTimeParseException e) {
                        System.err.println("    ERROR: PayrollCalculator.getSortedPayPeriodsForEmployee: DateTimeParseException for date: '" + data[3] + "' (Expected MM/dd/yyyy). Error: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: PayrollCalculator.getSortedPayPeriodsForEmployee: IOException during CSV loading for pay periods: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("ERROR: PayrollCalculator.getSortedPayPeriodsForEmployee: NullPointerException (likely resource not found before BufferedReader): " + e.getMessage());
        }


        List<String> periodsList = new ArrayList<>(periodsSet);
        // Custom sort to ensure chronological order of months and then by day range
        Collections.sort(periodsList, new Comparator<String>() {
            @Override
            public int compare(String p1, String p2) {
                String month1Str = p1.split(" ")[0];
                String month2Str = p2.split(" ")[0];

                Month month1 = Month.valueOf(month1Str.toUpperCase());
                Month month2 = Month.valueOf(month2Str.toUpperCase());

                int monthComparison = month1.compareTo(month2);
                if (monthComparison != 0) {
                    return monthComparison;
                }

                // If months are the same, sort by day range (1-15 comes before 16-XX)
                // String comparison will naturally put "1-15" before "16-XX"
                String range1 = p1.split(" ")[1];
                String range2 = p2.split(" ")[1];
                return range1.compareTo(range2);
            }
        });
        return periodsList;
    }


     //Parses a string value to double, returning 0.0 if invalid.
    public double parseDoubleOrDefault(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException | NullPointerException e) {
            return 0.0;
        }
    }

    //Calculates the full payslip for a given employee and pay period.
    public Payslip calculateFullPayslip(Employee employee, String selectedYear, String selectedPayPeriod) {
        // Parse employee's numeric data
        double monthlyBasicSalary = parseDoubleOrDefault(employee.getBasicSalary());
        double monthlyRiceSubsidy = parseDoubleOrDefault(employee.getRiceSubsidy());
        double monthlyPhoneAllowance = parseDoubleOrDefault(employee.getPhoneAllowance());
        double monthlyClothingAllowance = parseDoubleOrDefault(employee.getClothingAllowance());
        double hourlyRate = parseDoubleOrDefault(employee.getHourlyRate());

        // Initialize accumulators for the pay period
        double totalRegularEarnings = 0.0;
        long totalOvertimeMinutes = 0;
        long totalRegularWorkedMinutes = 0; // Total regular hours in minutes for the period

        // Parse pay period details
        String employeeNumber = employee.getEmployeeNumber();
        String monthName = selectedPayPeriod.split(" ")[0];
        int monthValue = Month.valueOf(monthName.toUpperCase()).getValue();
        int dayRangeStart = Integer.parseInt(selectedPayPeriod.split(" ")[1].split("-")[0]);
        int dayRangeEnd = Integer.parseInt(selectedPayPeriod.split(" ")[1].split("-")[1]);

        // Fetch and process attendance records for the period
        List<String[]> attendanceRecords = getAttendanceRecordsForPeriod(
                employeeNumber, selectedYear, monthValue, dayRangeStart, dayRangeEnd
        );

        Set<LocalDate> uniqueWorkDays = new TreeSet<>();

        for (String[] data : attendanceRecords) {
            // NOTE: processAttendance returns Object[]. A dedicated DailyAttendanceSummary class
            Object[] processedDayData = processAttendance(data, employeeNumber, hourlyRate);
            if (processedDayData != null) {
                try {
                    LocalDate date = LocalDate.parse(data[3].trim(), ATTENDANCE_DATE_FORMATTER);
                    uniqueWorkDays.add(date);
                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing date from attendance record for unique days in calculateFullPayslip: " + data[3]);
                }

                totalRegularEarnings += (double) processedDayData[6]; // Daily Regular Earnings

                String otString = (String) processedDayData[4]; // Overtime in HH:MM
                totalOvertimeMinutes += parseHoursMinutesToMinutes(otString);

                String regularHoursString = (String) processedDayData[5]; // Regular Hours in HH:MM
                totalRegularWorkedMinutes += parseHoursMinutesToMinutes(regularHoursString);
            }
        }
        int totalDaysWorked = uniqueWorkDays.size();

        // --- FINAL CALCULATIONS FOR THE PAYSLIP ---

        double overtimeHourlyRate = hourlyRate;
        double overtimeAmount = (totalOvertimeMinutes / 60.0) * overtimeHourlyRate;

        double regularPayForPeriod = totalRegularEarnings;
        double earningsGrossIncome = regularPayForPeriod + overtimeAmount;

        // Benefits (assumed to be monthly, divided by 2 for bi-monthly pay period)
        double benefitsRiceSubsidy = monthlyRiceSubsidy / 2.0;
        double benefitsPhoneAllowance = monthlyPhoneAllowance / 2.0;
        double benefitsClothingAllowance = monthlyClothingAllowance / 2.0;
        double benefitsTotal = benefitsRiceSubsidy + benefitsPhoneAllowance + benefitsClothingAllowance;

        // Deductions (calculated monthly then divided by 2 for bi-monthly pay period)
        double monthlySssDeduction = calculateSssDeduction(monthlyBasicSalary);
        double sssDeduction = monthlySssDeduction / 2.0;

        double monthlyPhilHealthDeduction = calculatePhilhealthDeduction(monthlyBasicSalary);
        double philHealthDeduction = monthlyPhilHealthDeduction / 2.0;

        // For Pag-IBIG, the calculation is often based on the total monthly gross compensation.
        double monthlyProjectedTotalCompensationForPagIbig = (regularPayForPeriod + overtimeAmount + benefitsTotal) * 2;
        double monthlyPagIbigContribution = calculatePagibigDeduction(monthlyProjectedTotalCompensationForPagIbig);
        double pagIbigDeduction = monthlyPagIbigContribution / 2.0;


        // Gross compensation income for tax includes earnings and benefits.
        double monthlyGrossCompensationIncomeForTax = (earningsGrossIncome + benefitsTotal) * 2;
        double totalMonthlyStatutoryDeductionsForTax = monthlySssDeduction + monthlyPhilHealthDeduction + monthlyPagIbigContribution;
        double monthlyTaxableIncome = monthlyGrossCompensationIncomeForTax - totalMonthlyStatutoryDeductionsForTax;
        if (monthlyTaxableIncome < 0) {
            monthlyTaxableIncome = 0; // Taxable income cannot be negative
        }
        double monthlyWithholdingTax = calculateWithholdingTax(monthlyTaxableIncome);
        double withholdingTax = monthlyWithholdingTax / 2.0;

        double totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + withholdingTax;

        double netPay = earningsGrossIncome + benefitsTotal - totalDeductions;

        // Determine Average Daily Rate
        double averageDailyRate = (totalDaysWorked > 0) ? regularPayForPeriod / totalDaysWorked : 0.0;


        // Create and return the Payslip object
        return new Payslip(
                employee.getEmployeeNumber(),
                String.format("%s, %s", employee.getLastName(), employee.getFirstName()),
                employee.getPosition(),
                selectedPayPeriod,
                selectedYear,
                monthlyBasicSalary,
                averageDailyRate,
                totalDaysWorked,
                formatMinutesToHoursMinutes((int)totalRegularWorkedMinutes),
                overtimeAmount,
                earningsGrossIncome,
                benefitsRiceSubsidy,
                benefitsPhoneAllowance,
                benefitsClothingAllowance,
                benefitsTotal,
                sssDeduction,
                philHealthDeduction,
                pagIbigDeduction,
                withholdingTax,
                totalDeductions,
                netPay
        );
    }
}