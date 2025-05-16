package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import transport.core.*;
import java.time.format.DateTimeFormatter;

public class UserManagementController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private ChoiceBox<String> userTypeChoice;
    @FXML
    private VBox employeeFields;
    @FXML
    private TextField employeeIdField;
    @FXML
    private CheckBox handicapedCheckBox;
    @FXML
    private ComboBox<TypeFonction> fonctionComboBox;
    @FXML
    private Label messageLabel;

    // Optional references - might be null if they don't exist in FXML
    @FXML
    private VBox categorySection; // Optional

    @FXML
    private ComboBox<String> categoryComboBox; // Optional

    private DataManager dataManager = DataManager.getInstance();

    @FXML
    private void initialize() {
        // Initialize user type choice box
        if (userTypeChoice != null) {
            userTypeChoice.setItems(FXCollections.observableArrayList("Regular User", "Employee"));
            userTypeChoice.setValue("Regular User");

            // Show/hide employee fields based on selected user type
            userTypeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && employeeFields != null) {
                    employeeFields.setVisible(newVal.equals("Employee"));
                    employeeFields.setManaged(newVal.equals("Employee"));
                }
            });
        }

        // Hide employee fields initially
        if (employeeFields != null) {
            employeeFields.setVisible(false);
            employeeFields.setManaged(false);
        }

        // Hide the category section if it exists (categories are assigned
        // automatically)
        if (categorySection != null) {
            categorySection.setVisible(false);
            categorySection.setManaged(false);
        }

        // Initialize the function combobox
        if (fonctionComboBox != null) {
            fonctionComboBox.setItems(FXCollections.observableArrayList(TypeFonction.values()));
            fonctionComboBox.setValue(TypeFonction.AGENT);
        }
    }

    @FXML
    private void handleSaveUser(ActionEvent event) {
        try {
            // Validate required fields
            if (firstNameField == null || lastNameField == null || birthDatePicker == null ||
                    firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()
                    || birthDatePicker.getValue() == null) {
                showMessage("Please fill all required fields", true);
                return;
            }

            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String dateOfBirth = birthDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            boolean handicaped = handicapedCheckBox != null && handicapedCheckBox.isSelected();

            User user;

            if (userTypeChoice.getValue().equals("Regular User")) {
                // Create regular user
                user = new RegularUser(firstName, lastName, dateOfBirth, handicaped);
            } else {
                // For employees, validate employee specific fields
                if (employeeIdField == null || employeeIdField.getText().isEmpty() || fonctionComboBox == null) {
                    showMessage("Please fill employee ID and function", true);
                    return;
                }

                String employeeId = employeeIdField.getText();
                TypeFonction fonction = fonctionComboBox.getValue();

                user = new Employee(employeeId, firstName, lastName, dateOfBirth, handicaped, fonction);
            }

            // Add user to data manager
            dataManager.addUser(user);

            // Clear fields and show success message
            clearFields();
            showMessage("User added successfully! ID: " + user.getId(), false);

        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        if (firstNameField != null)
            firstNameField.clear();
        if (lastNameField != null)
            lastNameField.clear();
        if (birthDatePicker != null)
            birthDatePicker.setValue(null);
        if (employeeIdField != null)
            employeeIdField.clear();
        if (handicapedCheckBox != null)
            handicapedCheckBox.setSelected(false);
        if (fonctionComboBox != null)
            fonctionComboBox.setValue(TypeFonction.AGENT);
    }

    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        }
    }
}
