package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import transport.core.GestionnaireDonnees;
import transport.core.CarteNavigation;
import transport.core.Billet;
import transport.core.TitreTransport;

public class ControlleurValidationBillet {

    @FXML
    private TextField champIdBillet;
    @FXML
    private Label etiquetteMessage;
    @FXML
    private GridPane grilleInfoBillet;
    @FXML
    private Label etiquetteType;
    @FXML
    private Label etiquetteProprietaire;
    @FXML
    private Label etiquetteDateAchat;
    @FXML
    private Label etiquetteDateExpiration;
    @FXML
    private Label etiquetteStatut;
    @FXML
    private Button boutonValider;

    private GestionnaireDonnees gestionnaireDonnees = GestionnaireDonnees.getInstance();

    @FXML
    private void initialize() {
        grilleInfoBillet.setVisible(false);

        // Ajout d'un style visuel plus attractif
        boutonValider.getStyleClass().add("bouton-primaire");

        // Ajout d'un gestionnaire d'événements pour le champ de texte
        champIdBillet.setOnAction(event -> handleValider(null));
    }

    @FXML
    private void handleValider(ActionEvent event) {
        String idBillet = champIdBillet.getText().trim();

        if (idBillet.isEmpty()) {
            afficherMessage("Veuillez saisir un identifiant de titre", true);
            return;
        }

        TitreTransport titre = gestionnaireDonnees.getTitreParId(idBillet);

        if (titre == null) {
            afficherMessage("Billet/Carte introuvable", true);
            grilleInfoBillet.setVisible(false);
            return;
        }

        // Afficher les informations du titre
        afficherInfoTitre(titre);
    }

    private void afficherInfoTitre(TitreTransport titre) {
        boolean estValide = titre.estValide();

        // Définir les informations de base
        etiquetteType.setText(titre instanceof Billet ? "Billet" : "Carte de Navigation");
        etiquetteProprietaire.setText("Propriétaire: " + titre.getProprietaire().getNomComplet());
        etiquetteDateAchat.setText("Date d'achat: " + titre.getDateAchatFormatee());

        // Afficher la date d'expiration pour une carte de navigation
        if (titre instanceof CarteNavigation) {
            CarteNavigation carte = (CarteNavigation) titre;
            etiquetteDateExpiration.setText("Expire le: " + carte.getDateExpiration());
            etiquetteDateExpiration.setVisible(true);
        } else {
            etiquetteDateExpiration.setVisible(false);
        }

        // Définir le statut
        etiquetteStatut.setText("Statut: " + (estValide ? "VALIDE" : "EXPIRÉ"));
        etiquetteStatut.getStyleClass().clear();
        etiquetteStatut.getStyleClass().add(estValide ? "statut-valide" : "statut-invalide");

        // Afficher le message de résultat de la validation
        if (estValide) {
            afficherMessage("Le titre est valide", false);
        } else {
            afficherMessage("Le titre a expiré", true);
        }

        grilleInfoBillet.setVisible(true);
    }

    private void afficherMessage(String message, boolean estErreur) {
        etiquetteMessage.setText(message);
        etiquetteMessage.getStyleClass().clear();
        etiquetteMessage.getStyleClass().add(estErreur ? "message-erreur" : "message-succes");
    }
}
