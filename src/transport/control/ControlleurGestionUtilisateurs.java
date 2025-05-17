package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import transport.core.*;
import java.time.format.DateTimeFormatter;

public class ControlleurGestionUtilisateurs {

    @FXML
    private TextField champPrenom;
    @FXML
    private TextField champNom;
    @FXML
    private DatePicker selecteurDateNaissance;
    @FXML
    private ComboBox<String> choixTypeUtilisateur; // Changé de ChoiceBox à ComboBox
    @FXML
    private AnchorPane panneauEmploye;
    @FXML
    private TextField champIdEmploye;
    @FXML
    private CheckBox caseHandicape;
    @FXML
    private ComboBox<TypeFonction> comboFonction;
    @FXML
    private Label etiquetteMessage;

    @FXML
    private GridPane sectionCategorie;

    @FXML
    private ComboBox<String> comboCategorie;

    @FXML
    private GridPane gridInfos;

    private GestionnaireDonnees gestionnaireDonnees = GestionnaireDonnees.getInstance();

    @FXML
    private void initialize() {
        // Initialiser la ComboBox du type d'utilisateur
        if (choixTypeUtilisateur != null) {
            choixTypeUtilisateur.setItems(FXCollections.observableArrayList("Utilisateur Régulier", "Employé"));
            choixTypeUtilisateur.setValue("Utilisateur Régulier");

            // Afficher/masquer les champs employé selon le type sélectionné
            choixTypeUtilisateur.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && panneauEmploye != null) {
                    boolean estEmploye = newVal.equals("Employé");

                    panneauEmploye.setVisible(estEmploye);
                    panneauEmploye.setManaged(estEmploye);

                    // Ajuster l'espacement vertical en fonction du type d'utilisateur
                    if (gridInfos != null) {
                        if (estEmploye) {
                            gridInfos.setVgap(26.0); // Plus d'espace quand employé est sélectionné
                        } else {
                            gridInfos.setVgap(22.0); // Espacement normal pour utilisateur régulier
                        }
                    }
                }
            });
        }

        // Masquer les champs employé initialement
        if (panneauEmploye != null) {
            panneauEmploye.setVisible(false);
            panneauEmploye.setManaged(false);
        }

        // Masquer la section catégorie si elle existe (les catégories sont assignées
        // automatiquement)
        if (sectionCategorie != null) {
            sectionCategorie.setVisible(false);
            sectionCategorie.setManaged(false);
        }

        // Initialiser la liste déroulante des fonctions
        if (comboFonction != null) {
            comboFonction.setItems(FXCollections.observableArrayList(TypeFonction.values()));
            comboFonction.setValue(TypeFonction.AGENT);
        }
    }

    @FXML
    private void handleSauvegarderUtilisateur(ActionEvent event) {
        try {
            // Valider les champs requis
            if (champPrenom == null || champNom == null || selecteurDateNaissance == null ||
                    champPrenom.getText().isEmpty() || champNom.getText().isEmpty()
                    || selecteurDateNaissance.getValue() == null) {
                afficherMessage("Veuillez remplir tous les champs obligatoires", true);
                return;
            }

            String prenom = champPrenom.getText();
            String nom = champNom.getText();
            String dateNaissance = selecteurDateNaissance.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            boolean handicape = caseHandicape != null && caseHandicape.isSelected();

            Utilisateur utilisateur;

            if (choixTypeUtilisateur.getValue().equals("Utilisateur Régulier")) {
                // Créer un utilisateur régulier
                utilisateur = new UtilisateurRegulier(prenom, nom, dateNaissance, handicape);
            } else {
                // Pour les employés, valider les champs spécifiques
                if (champIdEmploye == null || champIdEmploye.getText().isEmpty() || comboFonction == null) {
                    afficherMessage("Veuillez remplir l'ID employé et la fonction", true);
                    return;
                }

                String idEmploye = champIdEmploye.getText();
                TypeFonction fonction = comboFonction.getValue();

                utilisateur = new Employe(idEmploye, prenom, nom, dateNaissance, handicape, fonction);
            }

            // Ajouter l'utilisateur au gestionnaire de données
            gestionnaireDonnees.ajouterUtilisateur(utilisateur);

            // Effacer les champs et afficher un message de succès
            effacerChamps();
            afficherMessage("Utilisateur ajouté avec succès! ID: " + utilisateur.getId(), false);

        } catch (Exception e) {
            afficherMessage("Erreur: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void effacerChamps() {
        if (champPrenom != null)
            champPrenom.clear();
        if (champNom != null)
            champNom.clear();
        if (selecteurDateNaissance != null)
            selecteurDateNaissance.setValue(null);
        if (champIdEmploye != null)
            champIdEmploye.clear();
        if (caseHandicape != null)
            caseHandicape.setSelected(false);
        if (comboFonction != null)
            comboFonction.setValue(TypeFonction.AGENT);
    }

    private void afficherMessage(String message, boolean estErreur) {
        if (etiquetteMessage != null) {
            etiquetteMessage.setText(message);
            etiquetteMessage
                    .setStyle(estErreur ? "-fx-text-fill: -fx-orange-vif;" : "-fx-text-fill: -fx-vert-emeraude;");
        }
    }
}
