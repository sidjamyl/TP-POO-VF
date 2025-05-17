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
import javafx.stage.StageStyle;

public class ControlleurPrincipal {

    @FXML
    private BorderPane panneauPrincipal;

    @FXML
    private VBox ecranAccueil;

    @FXML
    private void initialize() {
        // Initialisation de l'écran principal
    }

    @FXML
    private void handleActionAccueil(MouseEvent event) {
        // Retour à l'écran d'accueil
        panneauPrincipal.setCenter(ecranAccueil);
    }

    @FXML
    private void handleActionAjoutUtilisateur(ActionEvent event) {
        chargerVue("/transport/ui/VueGestionUtilisateurs.fxml", "/transport/ui/UserManagementView.fxml");
    }

    @FXML
    private void handleActionAchatBillet(ActionEvent event) {
        chargerVue("/transport/ui/VueAchatBillet.fxml", "/transport/ui/TicketPurchaseView.fxml");
    }

    @FXML
    private void handleActionAfficherBillets(ActionEvent event) {
        chargerVue("/transport/ui/VueListeBillets.fxml", "/transport/ui/TicketListView.fxml");
    }

    @FXML
    private void handleActionValiderBillet(ActionEvent event) {
        chargerVue("/transport/ui/VueValidationBillet.fxml", "/transport/ui/TicketValidationView.fxml");
    }

    @FXML
    private void handleActionGererReclamations(ActionEvent event) {
        chargerVue("/transport/ui/VueReclamations.fxml", "/transport/ui/ComplaintView.fxml");
    }

    private void chargerVue(String cheminFxml, String cheminAlternatif) {
        try {
            // Sauvegarder l'écran d'accueil s'il n'a pas encore été sauvegardé
            if (ecranAccueil == null) {
                ecranAccueil = (VBox) panneauPrincipal.getCenter();
            }

            // Utiliser le chargement basé sur des fichiers plutôt que sur des ressources
            File fichier = new File("src" + cheminFxml);
            if (!fichier.exists()) {
                // Essayer le chemin absolu si le chemin relatif ne fonctionne pas
                fichier = new File("c:/Users/21355/Desktop/TP_Yasmine_Aya/TP-POO/src" + cheminFxml);

                // Si le fichier français n'existe pas, essayer avec la version anglaise
                if (!fichier.exists()) {
                    System.err.println("Impossible de trouver le fichier français: " + cheminFxml);
                    System.err.println("Tentative avec le fichier anglais: " + cheminAlternatif);

                    // Essayer le chemin anglais
                    fichier = new File("src" + cheminAlternatif);
                    if (!fichier.exists()) {
                        fichier = new File("c:/Users/21355/Desktop/TP_Yasmine_Aya/TP-POO/src" + cheminAlternatif);
                        if (!fichier.exists()) {
                            System.err.println("Impossible de trouver le fichier anglais: " + cheminAlternatif);
                            return;
                        }
                    }
                }
            }

            System.out.println("Chargement du fichier: " + fichier.getAbsolutePath());

            FXMLLoader chargeur = new FXMLLoader(fichier.toURI().toURL());
            Parent vue = chargeur.load();

            // Effet de transition (fade-in)
            vue.setOpacity(0);
            panneauPrincipal.setCenter(vue);

            // Animation de fondu - version compatible JavaFX 8
            javafx.animation.FadeTransition transition = new javafx.animation.FadeTransition(
                    javafx.util.Duration.millis(300), vue);
            transition.setFromValue(0);
            transition.setToValue(1);
            transition.play();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode auxiliaire pour ouvrir une nouvelle fenêtre
    public static void ouvrirNouvelleFenetre(String cheminFxml, String titre) {
        try {
            // Utiliser le chargement basé sur des fichiers plutôt que sur des ressources
            File fichier = new File("src" + cheminFxml);
            if (!fichier.exists()) {
                // Essayer le chemin absolu si le chemin relatif ne fonctionne pas
                fichier = new File("c:/Users/21355/Desktop/TP_Yasmine_Aya/TP-POO/src" + cheminFxml);
                if (!fichier.exists()) {
                    // Essayer le chemin anglais
                    String cheminAnglais = "/transport/ui/" + cheminFxml.substring(cheminFxml.lastIndexOf("/") + 1)
                            .replace("Vue", "View");
                    fichier = new File("src" + cheminAnglais);
                    if (!fichier.exists()) {
                        fichier = new File("c:/Users/21355/Desktop/TP_Yasmine_Aya/TP-POO/src" + cheminAnglais);
                        if (!fichier.exists()) {
                            System.err.println("Impossible de trouver le fichier: " + cheminFxml);
                            return;
                        }
                    }
                }
            }

            System.out.println("Ouverture d'une nouvelle fenêtre avec: " + fichier.getAbsolutePath());

            FXMLLoader chargeur = new FXMLLoader(fichier.toURI().toURL());
            Parent racine = chargeur.load();
            Stage scene = new Stage();
            scene.setTitle(titre);
            scene.initStyle(StageStyle.DECORATED);

            // Ajouter une ombre à la fenêtre
            scene.setScene(new Scene(racine));
            scene.show();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la fenêtre: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
