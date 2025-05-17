package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class TitreTransport implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idTitre;
    private Utilisateur proprietaire;
    private LocalDate dateAchat;
    private MethodePaiement methodePaiement;
    private double prix;

    public TitreTransport(String idTitre, Utilisateur proprietaire, MethodePaiement methodePaiement) {
        this.idTitre = idTitre;
        this.proprietaire = proprietaire;
        this.dateAchat = LocalDate.now();
        this.methodePaiement = methodePaiement;
        calculerPrix();
    }

    protected abstract void calculerPrix();

    public abstract boolean estValide();

    public String getIdTitre() {
        return idTitre;
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public String getDateAchatFormatee() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateAchat.format(formatter);
    }

    public MethodePaiement getMethodePaiement() {
        return methodePaiement;
    }

    public double getPrix() {
        return prix;
    }

    protected void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "ID Titre: " + idTitre + ", Propriétaire: " + proprietaire.getNomComplet() +
                ", Date d'achat: " + getDateAchatFormatee() + ", Prix: " + prix + " DA";
    }

    public enum MethodePaiement {
        ESPECES("Espèces"),
        DAHABIA("Dahabia"),
        BARIDIMOB("BaridiMob");

        private final String affichage;

        MethodePaiement(String affichage) {
            this.affichage = affichage;
        }

        @Override
        public String toString() {
            return affichage;
        }
    }
}
