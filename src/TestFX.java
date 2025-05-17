import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

public class TestFX extends Application {
    public void start(Stage stage) {
        try {
            // Essayer de charger à partir du chemin du fichier plutôt que du classpath
            File fichierFxml = new File("src/transport/ui/FenetrePrincipale.fxml");

            if (!fichierFxml.exists()) {
                System.err.println("Impossible de trouver FenetrePrincipale.fxml à: " + fichierFxml.getAbsolutePath());
                // Essayer un autre emplacement
                fichierFxml = new File(
                        "c:/Users/21355/Desktop/TP_Yasmine_Aya/TP-POO/src/transport/ui/FenetrePrincipale.fxml");

                if (!fichierFxml.exists()) {
                    System.err.println("Impossible de trouver FenetrePrincipale.fxml à l'emplacement alternatif");

                    // Tenter avec l'ancien nom MainWindow.fxml (version anglaise)
                    fichierFxml = new File("src/transport/ui/MainWindow.fxml");
                    if (!fichierFxml.exists()) {
                        System.err.println("Impossible de trouver également MainWindow.fxml");
                        return;
                    }
                }
            }

            System.out.println("Fichier FXML trouvé à: " + fichierFxml.getAbsolutePath());

            // Charger la fenêtre principale à partir de l'URL du fichier
            FXMLLoader loader = new FXMLLoader(fichierFxml.toURI().toURL());
            Parent racine = loader.load();
            Scene scene = new Scene(racine, 900, 650);

            try {
                // Ajouter la feuille de style CSS
                File fichierCSS = new File("src/transport/ui/styles/application.css");
                if (fichierCSS.exists()) {
                    scene.getStylesheets().add(fichierCSS.toURI().toURL().toString());
                    System.out.println("CSS chargé avec succès");
                } else {
                    System.err.println("Fichier CSS introuvable: " + fichierCSS.getAbsolutePath());
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement du CSS: " + e.getMessage());
            }

            stage.setTitle("ESI-RUN - Système de Gestion des Transports");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Erreur de chargement FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}