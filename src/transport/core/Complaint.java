package transport.core;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    private String complaintId;
    private ComplaintType type;
    private User reporter;
    private String entityConcerned;
    private EntityType entityType;
    private String description;
    private LocalDateTime reportDate;

    public Complaint(String complaintId, ComplaintType type, User reporter,
            String entityConcerned, EntityType entityType, String description) {
        this.complaintId = complaintId;
        this.type = type;
        this.reporter = reporter;
        this.entityConcerned = entityConcerned;
        this.entityType = entityType;
        this.description = description;
        this.reportDate = LocalDateTime.now();
    }

    // Getters
    public String getComplaintId() {
        return complaintId;
    }

    public ComplaintType getType() {
        return type;
    }

    public User getReporter() {
        return reporter;
    }

    public String getEntityConcerned() {
        return entityConcerned;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public String getFormattedReportDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return reportDate.format(formatter);
    }

    @Override
    public String toString() {
        return "Complaint ID: " + complaintId +
                ", Type: " + type +
                ", Entity: " + entityConcerned + " (" + entityType + ")" +
                ", Reporter: " + reporter.getFullName() +
                ", Date: " + getFormattedReportDate();
    }

    public enum ComplaintType {
        TECHNICAL("Technical Issue"),
        SERVICE("Service Quality");

        private final String display;

        ComplaintType(String display) {
            this.display = display;
        }

        @Override
        public String toString() {
            return display;
        }
    }

    public enum EntityType {
        STATION("Station"),
        TRANSPORT("Transport Means");

        private final String display;

        EntityType(String display) {
            this.display = display;
        }

        @Override
        public String toString() {
            return display;
        }
    }

    // New enums for predefined stations and transport means
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

        private final String name;

        Station(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum TransportMeans {
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

        private final String name;

        TransportMeans(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
