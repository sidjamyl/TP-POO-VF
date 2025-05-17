package transport.modele;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reclamation {
    private String idReclamation;
    private TypeReclamation type;
    private Usager declarant;
    private String entiteConcernee;
    private TypeEntite typeEntite;
    private String description;
    private LocalDateTime dateDeclaration;

    public Reclamation(String idReclamation, TypeReclamation type, Usager declarant,
            String entiteConcernee, TypeEntite typeEntite, String description) {
        this.idReclamation = idReclamation;
        this.type = type;
        this.declarant = declarant;
        this.entiteConcernee = entiteConcernee;
        this.typeEntite = typeEntite;
        this.description = description;
        this.dateDeclaration = LocalDateTime.now();
    }
    
    // Constructeur pour la désérialisation JSON
    public Reclamation(String idReclamation, TypeReclamation type, Usager declarant,
            String entiteConcernee, TypeEntite typeEntite, String description, LocalDateTime dateDeclaration) {
        this.idReclamation = idReclamation;
        this.type = type;
        this.declarant = declarant;
        this.entiteConcernee = entiteConcernee;
        this.typeEntite = typeEntite;
        this.description = description;
        this.dateDeclaration = dateDeclaration;
    }

    // Getters
    public String getIdReclamation() {
        return idReclamation;
    }

    public TypeReclamation getType() {
        return type;
    }

    public Usager getDeclarant() {
        return declarant;
    }

    public String getEntiteConcernee() {
        return entiteConcernee;
    }

    public TypeEntite getTypeEntite() {
        return typeEntite;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateDeclaration() {
        return dateDeclaration;
    }
    
    public void setDateDeclaration(LocalDateTime dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public String getDateDeclarationFormatee() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateDeclaration.format(formatter);
    }

    @Override
    public String toString() {
        return "ID Réclamation: " + idReclamation +
                ", Type: " + type +
                ", Entité: " + entiteConcernee + " (" + typeEntite + ")" +
                ", Déclarant: " + declarant.getNomComplet() +
                ", Date: " + getDateDeclarationFormatee();
    }

    public enum TypeReclamation {
        TECHNIQUE("Problème technique"),
        SERVICE("Qualité de service");

        private final String affichage;

        TypeReclamation(String affichage) {
            this.affichage = affichage;
        }

        @Override
        public String toString() {
            return affichage;
        }
    }

    public enum TypeEntite {
        STATION("Station"),
        TRANSPORT("Moyen de Transport");

        private final String affichage;

        TypeEntite(String affichage) {
            this.affichage = affichage;
        }

        @Override
        public String toString() {
            return affichage;
        }
    }

    // Nouvelles énumérations pour les stations et moyens de transport prédéfinis
    public enum Station {
        PLACE_DES_MARTYRS("Place des Martyrs"),
        ALI_BOUMENDJEL("Ali Boumendjel"),
        TAFOURAH_GRANDE_POSTE("Tafourah – Grande Poste"),
        KHELIFA_BOUKHALFA("Khelifa Boukhalfa"),
        PREMIER_MAI("1er Mai"),
        AISSAT_IDIR("Aïssat Idir"),
        HAMMA("Hamma"),
        JARDIN_DESSAI("Jardin d'Essai"),
        LES_FUSILLES("Les Fusillés"),
        AMIROUCHE("Amirouche"),
        MER_ET_SOLEIL("Mer et Soleil"),
        HAI_EL_BADR("Haï El Badr"),
        HALTE_DES_ATELIERS("Halte des Ateliers"),
        GUE_DE_CONSTANTINE("Gué de Constantine"),
        AIN_NAADJA("Aïn Naâdja"),
        BACHDJARAH_TENNIS("Bachdjarah Tennis"),
        BACHDJARAH("Bachdjarah"),
        EL_HARRACH_GARE("El Harrach Gare"),
        EL_HARRACH_CENTRE("El Harrach Centre");

        private final String nom;

        Station(String nom) {
            this.nom = nom;
        }

        @Override
        public String toString() {
            return nom;
        }
    }

    public enum MoyenTransport {
        METRO("Métro"),
        TRAMWAY("Tramway"),
        BUS_URBAIN_ETUSA("Bus urbain (ETUSA)"),
        BUS_PRIVE("Bus privé"),
        TRAIN_SNTF("Train de banlieue (SNTF)"),
        TAXI_INDIVIDUEL("Taxi individuel"),
        TAXI_COLLECTIF("Taxi collectif"),
        TELEPHERIQUE("Téléphérique"),
        VELO_LIBRE_SERVICE("Vélo en libre service"),
        NAVETTE_AEROPORTUAIRE("Navette aéroportuaire"),
        MINIBUS("Minibus"),
        BUS_EXPRESS("Bus express"),
        NAVETTE_UNIVERSITAIRE("Navette universitaire"),
        TRANSPORT_SCOLAIRE("Transport scolaire"),
        LOCATION_SCOOTER("Location de scooter"),
        TRANSPORT_APP_MOBILE("Transport par application mobile (Yassir, Heetch...)"),
        NAVETTE_ZONE_INDUSTRIELLE("Navette de zone industrielle");

        private final String nom;

        MoyenTransport(String nom) {
            this.nom = nom;
        }

        @Override
        public String toString() {
            return nom;
        }
    }
}
