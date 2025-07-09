package org.example.motorphui;

import java.io.*;

/**
 * Purpose: Handles user authentication for both HR and employee logins.
 * - Authenticates user credentials by checking against stored CSV files.
 * - Retrieves employee data after successful login.
 */


public class Authentication {
    private static final String CREDENTIALS_FILE_PATH = "/org/example/motorphui/data/motorph_employee_credentials.csv";
    private static final String HR_CREDENTIALS_FILE_PATH = "/org/example/motorphui/data/motorph_hr_credentials.csv";

    /**
     * Verifies HR login credentials by comparing the provided username and password against the HR credentials CSV file.
     */
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
}