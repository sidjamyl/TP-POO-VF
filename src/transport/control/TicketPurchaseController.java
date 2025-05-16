package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import transport.core.*;
import transport.core.TransportTitle.PaymentMethod;

public class TicketPurchaseController {

    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private ComboBox<String> titleTypeComboBox;
    @FXML
    private ComboBox<PaymentMethod> paymentMethodComboBox;
    @FXML
    private Label priceLabel;
    @FXML
    private Label userCategoryLabel;
    @FXML
    private Label messageLabel;

    private DataManager dataManager = DataManager.getInstance();

    @FXML
    private void initialize() {
        // Initialize title type combo box
        titleTypeComboBox.setItems(FXCollections.observableArrayList("Ticket", "Navigation Card"));
        titleTypeComboBox.setValue("Ticket");

        // Initialize payment method combo box
        paymentMethodComboBox.setItems(FXCollections.observableArrayList(
                PaymentMethod.CASH, PaymentMethod.DAHABIA, PaymentMethod.BARIDIMOB));
        paymentMethodComboBox.setValue(PaymentMethod.CASH);

        // Load users into the combo box
        refreshUserList();

        // Update price when title type or user changes
        titleTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updatePricePreview();
        });

        userComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateUserCategory();
            updatePricePreview();
        });
    }

    private void refreshUserList() {
        userComboBox.setItems(FXCollections.observableArrayList(dataManager.getUsers()));
        if (!userComboBox.getItems().isEmpty()) {
            userComboBox.getSelectionModel().selectFirst();
            updateUserCategory();
        }
    }

    private void updateUserCategory() {
        User selectedUser = userComboBox.getValue();
        if (selectedUser instanceof RegularUser) {
            RegularUser user = (RegularUser) selectedUser;
            userCategoryLabel.setText("Category: " + user.getCategory());
        } else {
            userCategoryLabel.setText("Category: N/A");
        }
    }

    private void updatePricePreview() {
        User selectedUser = userComboBox.getValue();
        String selectedTitleType = titleTypeComboBox.getValue();

        if (selectedUser == null) {
            priceLabel.setText("Price: N/A");
            return;
        }

        if (selectedTitleType.equals("Ticket")) {
            priceLabel.setText("Price: 50 DA");
        } else if (selectedTitleType.equals("Navigation Card")) {
            if (selectedUser instanceof RegularUser) {
                RegularUser user = (RegularUser) selectedUser;
                double basePrice = 5000.0; // Modifié de 1000.0 à 5000.0
                double discount = user.getCategory().getDiscountPercentage() / 100.0;
                double finalPrice = basePrice * (1 - discount);
                priceLabel.setText(String.format("Price: %.2f DA", finalPrice));
            } else {
                priceLabel.setText("Price: 5000 DA"); // Modifié de 1000 à 5000
            }
        }
    }

    @FXML
    private void handlePurchase(ActionEvent event) {
        try {
            User selectedUser = userComboBox.getValue();
            String selectedTitleType = titleTypeComboBox.getValue();
            PaymentMethod paymentMethod = paymentMethodComboBox.getValue();

            if (selectedUser == null) {
                showMessage("Please select a user", true);
                return;
            }

            // Generate a sequential ID for the title
            String titleId = dataManager.getNextTitleId();

            TransportTitle title;

            if (selectedTitleType.equals("Ticket")) {
                title = new Ticket(titleId, selectedUser, paymentMethod);
            } else {
                if (!(selectedUser instanceof RegularUser)) {
                    showMessage("Only regular users can have navigation cards", true);
                    return;
                }

                try {
                    title = new NavigationCard(titleId, (RegularUser) selectedUser, paymentMethod);
                } catch (ReductionImpossibleException e) {
                    showMessage("Cannot create navigation card: " + e.getMessage(), true);
                    return;
                } catch (TransportException e) {
                    showMessage("Error: " + e.getMessage(), true);
                    return;
                }
            }

            // Add title to data manager
            dataManager.addTitle(title);

            // Show success message and price
            showMessage("Purchase successful! Title ID: " + titleId, false);
            priceLabel.setText(String.format("Price: %.2f DA", title.getPrice()));

        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        MainController.openNewWindow("/transport/ui/UserManagementView.fxml", "Add User");

        // Refresh the user list after the window is closed
        refreshUserList();
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}
