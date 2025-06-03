package org.example.motorphui;

public class HRViewAndUpdateEmployee {
}

// When the "View and Update" button is clicked, this opens a pop-up window (hr_view_and_update_employee.fxml)
// to display the employee's current information for viewing and updating.

// The pop-up window allows the HR user to view and edit the selected employee's details.
// Once the user makes any changes, they can click the "Save" button to save the updated information.

// A confirmation dialog should appear when the "Save" button is clicked, asking the user:
// "Are you sure you want to save these changes?" This ensures the user is aware of the action being performed.

// If the user confirms (clicks "Yes"), the updated information should be saved
// and the employee's record should be updated accordingly (in the CSV file).

// If the user clicks "No" on the confirmation prompt, the changes will not be saved,
// and the pop-up window will remain open for further editing or cancellation.

// After saving, the pop-up window should either close automatically or provide the option
// to cancel the action and close without saving any changes.

// In case of errors (e.g., file issues or invalid input), an error alert should be displayed
// to inform the user about the issue.
