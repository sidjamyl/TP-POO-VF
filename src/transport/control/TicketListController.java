package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import transport.core.DataManager;
import transport.core.Ticket;
import transport.core.TransportTitle;

public class TicketListController {

    @FXML
    private TableView<TransportTitle> ticketsTable;
    @FXML
    private TableColumn<TransportTitle, String> idColumn;
    @FXML
    private TableColumn<TransportTitle, String> typeColumn;
    @FXML
    private TableColumn<TransportTitle, String> ownerColumn;
    @FXML
    private TableColumn<TransportTitle, String> dateColumn;
    @FXML
    private TableColumn<TransportTitle, Double> priceColumn;
    @FXML
    private TableColumn<TransportTitle, String> validityColumn;
    @FXML
    private Label titleCountLabel;

    private DataManager dataManager = DataManager.getInstance();

    @FXML
    private void initialize() {
        // Configure columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("titleId"));

        typeColumn.setCellValueFactory(cellData -> {
            TransportTitle title = cellData.getValue();
            String type = title instanceof Ticket ? "Ticket" : "Navigation Card";
            return new SimpleStringProperty(type);
        });

        ownerColumn.setCellValueFactory(cellData -> {
            String owner = cellData.getValue().getOwner().getFullName();
            return new SimpleStringProperty(owner);
        });

        dateColumn.setCellValueFactory(cellData -> {
            String date = cellData.getValue().getFormattedPurchaseDate();
            return new SimpleStringProperty(date);
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        validityColumn.setCellValueFactory(cellData -> {
            boolean valid = cellData.getValue().isValid();
            String status = valid ? "Valid" : "Expired";
            return new SimpleStringProperty(status);
        });

        // Load titles into table
        loadTitles();
    }

    private void loadTitles() {
        // Get sorted list of titles (most recent first)
        ticketsTable.setItems(FXCollections.observableArrayList(dataManager.getTitles()));

        // Update count label
        titleCountLabel.setText("Total: " + ticketsTable.getItems().size() + " titles");
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadTitles();
    }
}
