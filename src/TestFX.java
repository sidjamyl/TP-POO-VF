import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

public class TestFX extends Application {
    public void start(Stage stage) {
        try {
            // Try loading from file path instead of classpath
            File fxmlFile = new File("src/transport/ui/MainWindow.fxml");

            if (!fxmlFile.exists()) {
                System.err.println("Cannot find MainWindow.fxml at: " + fxmlFile.getAbsolutePath());
                // Try alternative location
                fxmlFile = new File("c:/Users/21355/Desktop/TUTO-4-POO/src/transport/ui/MainWindow.fxml");

                if (!fxmlFile.exists()) {
                    System.err.println("Cannot find MainWindow.fxml at alternate location either");
                    return;
                }
            }

            System.out.println("Found FXML file at: " + fxmlFile.getAbsolutePath());

            // Load the main window from file URL
            Parent root = FXMLLoader.load(fxmlFile.toURI().toURL());
            Scene scene = new Scene(root, 800, 600);
            stage.setTitle("ESI-RUN - Transport Management System");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}