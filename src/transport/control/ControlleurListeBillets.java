package transport.control;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import transport.core.GestionnaireDonnees;
import transport.core.Billet;
import transport.core.TitreTransport;

public class ControlleurListeBillets {

    @FXML
    private TableView<TitreTransport> tableauTitres;
    @FXML
    private TableColumn<TitreTransport, String> colonneId;
    @FXML
    private TableColumn<TitreTransport, String> colonneType;
    @FXML
    private TableColumn<TitreTransport, String> colonneProprietaire;
    @FXML
    private TableColumn<TitreTransport, String> colonneDate;
    @FXML
    private TableColumn<TitreTransport, Double> colonnePrix;
    @FXML
    private TableColumn<TitreTransport, String> colonneValidite;
    @FXML
    private Label etiquetteNombreTitres;

    private GestionnaireDonnees gestionnaireDonnees = GestionnaireDonnees.getInstance();

    @FXML
    private void initialize() {
        // Configurer les colonnes
        colonneId.setCellValueFactory(new PropertyValueFactory<>("idTitre"));

        colonneType.setCellValueFactory(cellData -> {
            TitreTransport titre = cellData.getValue();
            String type = titre instanceof Billet ? "Billet" : "Carte de Navigation";
            return new SimpleStringProperty(type);
        });

        colonneProprietaire.setCellValueFactory(cellData -> {
            String proprietaire = cellData.getValue().getProprietaire().getNomComplet();
            return new SimpleStringProperty(proprietaire);
        });

        colonneDate.setCellValueFactory(cellData -> {
            String date = cellData.getValue().getDateAchatFormatee();
            return new SimpleStringProperty(date);
        });

        colonnePrix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        colonneValidite.setCellValueFactory(cellData -> {
            boolean valide = cellData.getValue().estValide();
            String statut = valide ? "Valide" : "Expiré";
            return new SimpleStringProperty(statut);
        });

        // Charger les titres dans le tableau
        chargerTitres();
    }

    private void chargerTitres() {
        // Obtenir la liste triée des titres (les plus récents d'abord)
        tableauTitres.setItems(FXCollections.observableArrayList(gestionnaireDonnees.getTitres()));

        // Mettre à jour l'étiquette de décompte
        etiquetteNombreTitres.setText("Total: " + tableauTitres.getItems().size() + " titres");
    }

    @FXML
    private void handleRafraichir(ActionEvent event) {
        chargerTitres();
    }
}
