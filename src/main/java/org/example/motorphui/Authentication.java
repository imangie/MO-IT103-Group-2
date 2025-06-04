package org.example.motorphui;

import java.io.*;

public class Authentication {
    private static final String CREDENTIALS_FILE_PATH = "/org/example/motorphui/data/motorph_employee_credentials.csv";
    private static final String HR_CREDENTIALS_FILE_PATH = "/org/example/motorphui/data/motorph_hr_credentials.csv";

    // Method for HR
    public static boolean authenticateHR(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Authentication.class.getResourceAsStream(HR_CREDENTIALS_FILE_PATH)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 2) {  // Check that there are 2 values (Username, Password)
                    if (data[0].equals(username) && data[1].equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method for Employee
    public static boolean authenticate(String empId, String username, String password) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Authentication.class.getResourceAsStream(CREDENTIALS_FILE_PATH)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    if (data[0].trim().equals(empId.trim()) && data[1].trim().equals(username.trim())) {
                        return data[2].trim().equals(password.trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static Employee getEmployeeData(String empId) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Authentication.class.getResourceAsStream("/org/example/motorphui/data/motorph_employee_data.csv")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 19 && data[0].trim().equals(empId.trim())) {
                    return new Employee(
                            data[0], data[1], data[2], data[3], data[4],
                            data[5], data[6], data[7], data[8], data[9],
                            data[10], data[11], data[12], data[13], data[14],
                            data[15], data[16], data[17], data[18]
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}