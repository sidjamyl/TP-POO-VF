package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import transport.core.*;
import transport.core.TitreTransport.MethodePaiement;

public class ControlleurAchatBillet {

    @FXML
    private ComboBox<Utilisateur> comboUtilisateur;
    @FXML
    private ComboBox<String> comboTypeTitre;
    @FXML
    private ComboBox<MethodePaiement> comboMethodePaiement;
    @FXML
    private Label etiquettePrix;
    @FXML
    private Label etiquetteCategorieUtilisateur;
    @FXML
    private Label etiquetteMessage;

    private GestionnaireDonnees gestionnaireDonnees = GestionnaireDonnees.getInstance();

    @FXML
    private void initialize() {
        // Initialiser la combobox du type de titre
        comboTypeTitre.setItems(FXCollections.observableArrayList("Billet", "Carte de Navigation"));
        comboTypeTitre.setValue("Billet");

        // Initialiser la combobox de méthode de paiement
        comboMethodePaiement.setItems(FXCollections.observableArrayList(
                MethodePaiement.ESPECES, MethodePaiement.DAHABIA, MethodePaiement.BARIDIMOB));
        comboMethodePaiement.setValue(MethodePaiement.ESPECES);

        // Charger les utilisateurs dans la combobox
        rafraichirListeUtilisateurs();

        // Mettre à jour le prix lorsque le type de titre ou l'utilisateur change
        comboTypeTitre.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mettreAJourAperçuPrix();
        });

        comboUtilisateur.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mettreAJourCategorieUtilisateur();
            mettreAJourAperçuPrix();
        });
    }

    private void rafraichirListeUtilisateurs() {
        comboUtilisateur.setItems(FXCollections.observableArrayList(gestionnaireDonnees.getUtilisateurs()));
        if (!comboUtilisateur.getItems().isEmpty()) {
            comboUtilisateur.getSelectionModel().selectFirst();
            mettreAJourCategorieUtilisateur();
        }
    }

    private void mettreAJourCategorieUtilisateur() {
        Utilisateur utilisateurSelectionne = comboUtilisateur.getValue();
        if (utilisateurSelectionne instanceof UtilisateurRegulier) {
            UtilisateurRegulier utilisateur = (UtilisateurRegulier) utilisateurSelectionne;
            etiquetteCategorieUtilisateur.setText("Catégorie: " + utilisateur.getCategorie());
        } else {
            etiquetteCategorieUtilisateur.setText("Catégorie: N/A");
        }
    }

    private void mettreAJourAperçuPrix() {
        Utilisateur utilisateurSelectionne = comboUtilisateur.getValue();
        String typetitreTitre = comboTypeTitre.getValue();

        if (utilisateurSelectionne == null) {
            etiquettePrix.setText("Prix: N/A");
            return;
        }

        if (typetitreTitre.equals("Billet")) {
            etiquettePrix.setText("Prix: 50 DA");
        } else if (typetitreTitre.equals("Carte de Navigation")) {
            if (utilisateurSelectionne instanceof UtilisateurRegulier) {
                UtilisateurRegulier utilisateur = (UtilisateurRegulier) utilisateurSelectionne;
                double prixBase = 5000.0;
                double reduction = utilisateur.getCategorie().getPourcentageReduction() / 100.0;
                double prixFinal = prixBase * (1 - reduction);
                etiquettePrix.setText(String.format("Prix: %.2f DA", prixFinal));
            } else {
                etiquettePrix.setText("Prix: 5000 DA");
            }
        }
    }

    @FXML
    private void handleAchat(ActionEvent event) {
        try {
            Utilisateur utilisateurSelectionne = comboUtilisateur.getValue();
            String typeTitreTitre = comboTypeTitre.getValue();
            MethodePaiement methodePaiement = comboMethodePaiement.getValue();

            if (utilisateurSelectionne == null) {
                afficherMessage("Veuillez sélectionner un utilisateur", true);
                return;
            }

            // Générer un ID séquentiel pour le titre
            String idTitre = gestionnaireDonnees.getProchainIdTitre();

            TitreTransport titre;

            if (typeTitreTitre.equals("Billet")) {
                titre = new Billet(idTitre, utilisateurSelectionne, methodePaiement);
            } else {
                if (!(utilisateurSelectionne instanceof UtilisateurRegulier)) {
                    afficherMessage("Seuls les utilisateurs réguliers peuvent avoir des cartes de navigation", true);
                    return;
                }

                try {
                    titre = new CarteNavigation(idTitre, (UtilisateurRegulier) utilisateurSelectionne, methodePaiement);
                } catch (ExceptionReductionImpossible e) {
                    afficherMessage("Impossible de créer la carte de navigation: " + e.getMessage(), true);
                    return;
                } catch (ExceptionTransport e) {
                    afficherMessage("Erreur: " + e.getMessage(), true);
                    return;
                }
            }

            // Ajouter le titre au gestionnaire de données
            gestionnaireDonnees.ajouterTitre(titre);

            // Afficher un message de succès et le prix
            afficherMessage("Achat réussi! ID du titre: " + idTitre, false);
            etiquettePrix.setText(String.format("Prix: %.2f DA", titre.getPrix()));

        } catch (Exception e) {
            afficherMessage("Erreur: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjoutUtilisateur(ActionEvent event) {
        ControlleurPrincipal.ouvrirNouvelleFenetre("/transport/ui/VueGestionUtilisateurs.fxml",
                "Ajouter un utilisateur");

        // Rafraîchir la liste des utilisateurs après la fermeture de la fenêtre
        rafraichirListeUtilisateurs();
    }

    private void afficherMessage(String message, boolean estErreur) {
        etiquetteMessage.setText(message);
        etiquetteMessage.setStyle(estErreur ? "-fx-text-fill: -fx-orange-vif;" : "-fx-text-fill: -fx-vert-emeraude;");
    }
}
