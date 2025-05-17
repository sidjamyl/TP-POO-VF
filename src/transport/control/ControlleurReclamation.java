package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import transport.core.*;
import transport.core.Reclamation.TypeReclamation;
import transport.core.Reclamation.TypeEntite;
import transport.core.Reclamation.Station;
import transport.core.Reclamation.MoyenTransport;

import java.util.UUID;

public class ControlleurReclamation {

    @FXML
    private ComboBox<TypeReclamation> comboTypeReclamation;
    @FXML
    private ComboBox<TypeEntite> comboTypeEntite;
    @FXML
    private ComboBox<Utilisateur> comboDeclarant;
    @FXML
    private ComboBox<Object> comboEntite; // Contiendra soit Station soit MoyenTransport
    @FXML
    private TextArea zoneDescription;
    @FXML
    private Label etiquetteMessage;
    @FXML
    private TableView<Reclamation> tableauReclamations;
    @FXML
    private TableColumn<Reclamation, String> colonneId;
    @FXML
    private TableColumn<Reclamation, TypeReclamation> colonneType;
    @FXML
    private TableColumn<Reclamation, String> colonneEntite;
    @FXML
    private TableColumn<Reclamation, String> colonneTypeEntite;
    @FXML
    private TableColumn<Reclamation, String> colonneDeclarant;
    @FXML
    private TableColumn<Reclamation, String> colonneDate;

    private GestionnaireDonnees gestionnaireDonnees = GestionnaireDonnees.getInstance();

    @FXML
    private void initialize() {
        // Initialiser la combobox de type de réclamation
        comboTypeReclamation.setItems(FXCollections.observableArrayList(TypeReclamation.values()));
        if (!comboTypeReclamation.getItems().isEmpty()) {
            comboTypeReclamation.getSelectionModel().selectFirst();
        }

        // Initialiser la combobox de type d'entité et ajouter un listener pour mettre à
        // jour les options d'entité
        comboTypeEntite.setItems(FXCollections.observableArrayList(TypeEntite.values()));
        if (!comboTypeEntite.getItems().isEmpty()) {
            comboTypeEntite.getSelectionModel().selectFirst();
            mettreAJourOptionsEntite(comboTypeEntite.getValue());
        }

        // Ajouter un listener pour mettre à jour les options d'entité lorsque le type
        // d'entité change
        comboTypeEntite.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mettreAJourOptionsEntite(newVal);
            }
        });

        // Charger les utilisateurs dans la combobox de déclarant
        comboDeclarant.setItems(FXCollections.observableArrayList(gestionnaireDonnees.getUtilisateurs()));
        if (!comboDeclarant.getItems().isEmpty()) {
            comboDeclarant.getSelectionModel().selectFirst();
        }

        // Configurer les colonnes du tableau
        colonneId.setCellValueFactory(new PropertyValueFactory<>("idReclamation"));
        colonneType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colonneEntite.setCellValueFactory(new PropertyValueFactory<>("entiteConcernee"));
        colonneTypeEntite.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTypeEntite().toString()));
        colonneDeclarant.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getDeclarant().getNomComplet()));
        colonneDate.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getDateDeclarationFormatee()));

        // Charger les réclamations existantes
        chargerReclamations();
    }

    private void mettreAJourOptionsEntite(TypeEntite typeEntite) {
        if (typeEntite == TypeEntite.STATION) {
            // Cast en Object[] pour éviter l'avertissement varargs
            Station[] stations = Station.values();
            comboEntite.setItems(FXCollections.observableArrayList((Object[]) stations));
        } else {
            // Cast en Object[] pour éviter l'avertissement varargs
            MoyenTransport[] moyensTransport = MoyenTransport.values();
            comboEntite.setItems(FXCollections.observableArrayList((Object[]) moyensTransport));
        }

        if (!comboEntite.getItems().isEmpty()) {
            comboEntite.getSelectionModel().selectFirst();
        }
    }

    private void chargerReclamations() {
        tableauReclamations.setItems(FXCollections.observableArrayList(gestionnaireDonnees.getReclamations()));
    }

    @FXML
    private void handleAjouterReclamation(ActionEvent event) {
        try {
            TypeReclamation type = comboTypeReclamation.getValue();
            TypeEntite typeEntite = comboTypeEntite.getValue();
            Utilisateur declarant = comboDeclarant.getValue();
            Object entiteSelectionnee = comboEntite.getValue();
            String description = zoneDescription.getText();

            // Valider les champs requis
            if (type == null || typeEntite == null || declarant == null || entiteSelectionnee == null
                    || description.isEmpty()) {
                afficherMessage("Veuillez remplir tous les champs obligatoires", true);
                return;
            }

            // Obtenir le nom de l'entité à partir de l'objet sélectionné
            String nomEntite = entiteSelectionnee.toString();

            // Générer un ID unique pour la réclamation
            String idReclamation = UUID.randomUUID().toString().substring(0, 8);

            // Créer et ajouter la réclamation
            Reclamation reclamation = new Reclamation(idReclamation, type, declarant, nomEntite, typeEntite,
                    description);
            gestionnaireDonnees.ajouterReclamation(reclamation);

            // Effacer les champs et afficher un message de succès
            effacerChamps();
            afficherMessage("Réclamation enregistrée avec succès!", false);

            // Recharger le tableau des réclamations
            chargerReclamations();

            // Vérifier l'avertissement de suspension - compter par nom d'entité ET type
            int nombre = gestionnaireDonnees.getNombreReclamationsPourEntite(nomEntite, typeEntite);
            if (nombre >= 3) {
                Alert alerte = new Alert(Alert.AlertType.WARNING);
                alerte.setTitle("Avertissement de suspension");
                alerte.setHeaderText("Suspension de " + typeEntite + " requise");
                alerte.setContentText("Le/La " + typeEntite.toString().toLowerCase() + " '" + nomEntite +
                        "' a reçu " + nombre + " réclamations et devrait être suspendu(e) pour enquête.");
                alerte.showAndWait();
            }

        } catch (Exception e) {
            afficherMessage("Erreur: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void effacerChamps() {
        if (!comboTypeReclamation.getItems().isEmpty()) {
            comboTypeReclamation.getSelectionModel().selectFirst();
        }
        if (!comboTypeEntite.getItems().isEmpty()) {
            comboTypeEntite.getSelectionModel().selectFirst();
            mettreAJourOptionsEntite(comboTypeEntite.getValue());
        }
        if (!comboDeclarant.getItems().isEmpty()) {
            comboDeclarant.getSelectionModel().selectFirst();
        }
        if (!comboEntite.getItems().isEmpty()) {
            comboEntite.getSelectionModel().selectFirst();
        }
        zoneDescription.clear();
    }

    private void afficherMessage(String message, boolean estErreur) {
        etiquetteMessage.setText(message);
        etiquetteMessage.getStyleClass().clear();
        etiquetteMessage.getStyleClass().add(estErreur ? "message-erreur" : "message-succes");
    }

    @FXML
    private void handleRafraichir(ActionEvent event) {
        chargerReclamations();
    }
}
