package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import transport.core.DataManager;
import transport.core.NavigationCard;
import transport.core.Ticket;
import transport.core.TransportTitle;

public class TicketValidationController {

    @FXML
    private TextField ticketIdField;
    @FXML
    private Label messageLabel;
    @FXML
    private VBox ticketInfoBox;
    @FXML
    private Label typeLabel;
    @FXML
    private Label ownerLabel;
    @FXML
    private Label purchaseDateLabel;
    @FXML
    private Label expirationDateLabel;
    @FXML
    private Label statusLabel;

    private DataManager dataManager = DataManager.getInstance();

    @FXML
    private void initialize() {
        ticketInfoBox.setVisible(false);
    }

    @FXML
    private void handleValidate(ActionEvent event) {
        String ticketId = ticketIdField.getText().trim();

        if (ticketId.isEmpty()) {
            showMessage("Please enter a ticket ID", true);
            return;
        }

        TransportTitle title = dataManager.getTitleById(ticketId);

        if (title == null) {
            showMessage("Ticket/Card not found", true);
            ticketInfoBox.setVisible(false);
            return;
        }

        // Display title information
        displayTitleInfo(title);
    }

    private void displayTitleInfo(TransportTitle title) {
        boolean isValid = title.isValid();

        // Set basic information
        typeLabel.setText(title instanceof Ticket ? "Ticket" : "Navigation Card");
        ownerLabel.setText("Owner: " + title.getOwner().getFullName());
        purchaseDateLabel.setText("Purchase Date: " + title.getFormattedPurchaseDate());

        // Show expiration date for navigation card
        if (title instanceof NavigationCard) {
            NavigationCard card = (NavigationCard) title;
            expirationDateLabel.setText("Expires: " + card.getExpirationDate());
            expirationDateLabel.setVisible(true);
        } else {
            expirationDateLabel.setVisible(false);
        }

        // Set status
        statusLabel.setText("Status: " + (isValid ? "VALID" : "EXPIRED"));
        statusLabel.setStyle(isValid ? "-fx-text-fill: green; -fx-font-weight: bold;"
                : "-fx-text-fill: red; -fx-font-weight: bold;");

        // Show validation result message
        if (isValid) {
            showMessage("Title is valid", false);
        } else {
            showMessage("Title has expired", true);
        }

        ticketInfoBox.setVisible(true);
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}
