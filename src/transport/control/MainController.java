package transport.control;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private VBox welcomeScreen;

    @FXML
    private void initialize() {
        // Initialize the main screen
    }

    @FXML
    private void handleHomeAction(MouseEvent event) {
        // Return to the welcome screen
        mainPane.setCenter(welcomeScreen);
    }

    @FXML
    private void handleAddUserAction(ActionEvent event) {
        loadView("/transport/ui/UserManagementView.fxml");
    }

    @FXML
    private void handleBuyTicketAction(ActionEvent event) {
        loadView("/transport/ui/TicketPurchaseView.fxml");
    }

    @FXML
    private void handleShowTicketsAction(ActionEvent event) {
        loadView("/transport/ui/TicketListView.fxml");
    }

    @FXML
    private void handleValidateTicketAction(ActionEvent event) {
        loadView("/transport/ui/TicketValidationView.fxml");
    }

    @FXML
    private void handleManageComplaintsAction(ActionEvent event) {
        loadView("/transport/ui/ComplaintView.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            // Save the welcome screen if it hasn't been saved yet
            if (welcomeScreen == null) {
                welcomeScreen = (VBox) mainPane.getCenter();
            }

            // Use file-based loading instead of resource-based loading
            File file = new File("src" + fxmlPath);
            if (!file.exists()) {
                // Try the absolute path if relative path doesn't work
                file = new File("c:/Users/21355/Desktop/TUTO-4-POO/src" + fxmlPath);
                if (!file.exists()) {
                    System.err.println("Cannot find file: " + fxmlPath);
                    return;
                }
            }

            FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
            Parent view = loader.load();
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to open a new window
    public static void openNewWindow(String fxmlPath, String title) {
        try {
            // Use file-based loading instead of resource-based loading
            File file = new File("src" + fxmlPath);
            if (!file.exists()) {
                // Try the absolute path if relative path doesn't work
                file = new File("c:/Users/21355/Desktop/TUTO-4-POO/src" + fxmlPath);
                if (!file.exists()) {
                    System.err.println("Cannot find file: " + fxmlPath);
                    return;
                }
            }

            FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
