package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import transport.core.*;
import transport.core.Complaint.ComplaintType;
import transport.core.Complaint.EntityType;
import transport.core.Complaint.Station;
import transport.core.Complaint.TransportMeans;

import java.util.UUID;

public class ComplaintController {

    @FXML
    private ComboBox<ComplaintType> typeComboBox;
    @FXML
    private ComboBox<EntityType> entityTypeComboBox;
    @FXML
    private ComboBox<User> reporterComboBox;

    // Replace TextField with ComboBoxes
    @FXML
    private ComboBox<Object> entityComboBox; // Will hold either Station or TransportMeans

    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<Complaint> complaintsTable;
    @FXML
    private TableColumn<Complaint, String> idColumn;
    @FXML
    private TableColumn<Complaint, ComplaintType> typeColumn;
    @FXML
    private TableColumn<Complaint, String> entityColumn;
    @FXML
    private TableColumn<Complaint, String> entityTypeColumn;
    @FXML
    private TableColumn<Complaint, String> reporterColumn;
    @FXML
    private TableColumn<Complaint, String> dateColumn;

    private DataManager dataManager = DataManager.getInstance();

    @FXML
    private void initialize() {
        // Initialize complaint type combo box
        typeComboBox.setItems(FXCollections.observableArrayList(ComplaintType.values()));
        if (!typeComboBox.getItems().isEmpty()) {
            typeComboBox.getSelectionModel().selectFirst();
        }

        // Initialize entity type combo box and add listener to update entity options
        entityTypeComboBox.setItems(FXCollections.observableArrayList(EntityType.values()));
        if (!entityTypeComboBox.getItems().isEmpty()) {
            entityTypeComboBox.getSelectionModel().selectFirst();
            updateEntityOptions(entityTypeComboBox.getValue());
        }

        // Add listener to update entity options when entity type changes
        entityTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateEntityOptions(newVal);
            }
        });

        // Load users into reporter combo box
        reporterComboBox.setItems(FXCollections.observableArrayList(dataManager.getUsers()));
        if (!reporterComboBox.getItems().isEmpty()) {
            reporterComboBox.getSelectionModel().selectFirst();
        }

        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("complaintId"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        entityColumn.setCellValueFactory(new PropertyValueFactory<>("entityConcerned"));
        entityTypeColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getEntityType().toString()));
        reporterColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getReporter().getFullName()));
        dateColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getFormattedReportDate()));

        // Load existing complaints
        loadComplaints();
    }

    // Method to update entity options based on selected entity type
    private void updateEntityOptions(EntityType entityType) {
        if (entityType == EntityType.STATION) {
            // Cast to Object[] to avoid varargs warning
            Station[] stations = Station.values();
            entityComboBox.setItems(FXCollections.observableArrayList((Object[]) stations));
        } else {
            // Cast to Object[] to avoid varargs warning
            TransportMeans[] transportMeans = TransportMeans.values();
            entityComboBox.setItems(FXCollections.observableArrayList((Object[]) transportMeans));
        }

        if (!entityComboBox.getItems().isEmpty()) {
            entityComboBox.getSelectionModel().selectFirst();
        }
    }

    private void loadComplaints() {
        complaintsTable.setItems(FXCollections.observableArrayList(dataManager.getComplaints()));
    }

    @FXML
    private void handleAddComplaint(ActionEvent event) {
        try {
            ComplaintType type = typeComboBox.getValue();
            EntityType entityType = entityTypeComboBox.getValue();
            User reporter = reporterComboBox.getValue();
            Object selectedEntity = entityComboBox.getValue();
            String description = descriptionArea.getText();

            // Validate required fields
            if (type == null || entityType == null || reporter == null || selectedEntity == null
                    || description.isEmpty()) {
                showMessage("Please fill all required fields", true);
                return;
            }

            // Get entity name from selected object
            String entityName = selectedEntity.toString();

            // Generate a unique ID for the complaint
            String complaintId = UUID.randomUUID().toString().substring(0, 8);

            // Create and add the complaint
            Complaint complaint = new Complaint(complaintId, type, reporter, entityName, entityType, description);
            dataManager.addComplaint(complaint);

            // Clear fields and show success message
            clearFields();
            showMessage("Complaint registered successfully!", false);

            // Reload complaints table
            loadComplaints();

            // Check for suspension warning - count by entity name AND type
            int count = dataManager.getComplaintsCountForEntity(entityName, entityType);
            if (count >= 3) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Suspension Warning");
                alert.setHeaderText(entityType + " Suspension Required");
                alert.setContentText("The " + entityType.toString().toLowerCase() + " '" + entityName +
                        "' has received " + count + " complaints and should be suspended for investigation.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        if (!typeComboBox.getItems().isEmpty()) {
            typeComboBox.getSelectionModel().selectFirst();
        }
        if (!entityTypeComboBox.getItems().isEmpty()) {
            entityTypeComboBox.getSelectionModel().selectFirst();
            updateEntityOptions(entityTypeComboBox.getValue());
        }
        if (!reporterComboBox.getItems().isEmpty()) {
            reporterComboBox.getSelectionModel().selectFirst();
        }
        if (!entityComboBox.getItems().isEmpty()) {
            entityComboBox.getSelectionModel().selectFirst();
        }
        descriptionArea.clear();
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadComplaints();
    }
}
