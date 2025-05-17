package transport.core;

import java.time.LocalDate;

public class Billet extends TitreTransport {
    private static final long serialVersionUID = 1L;
    private static final double PRIX_STANDARD = 50.0; // Prix standard d'un billet
    private static final int VALIDITE_JOURS = 1; // Validité d'un jour

    private LocalDate dateExpiration;

    public Billet(String idBillet, Utilisateur proprietaire, MethodePaiement methodePaiement) {
        super(idBillet, proprietaire, methodePaiement);
        // Définir la date d'expiration à la fin de la journée actuelle
        this.dateExpiration = getDateAchat().plusDays(VALIDITE_JOURS);
    }

    @Override
    protected void calculerPrix() {
        // Prix standard pour tous les utilisateurs
        setPrix(PRIX_STANDARD);
    }

    @Override
    public boolean estValide() {
        // Le billet est valide si la date actuelle est antérieure ou égale à la date
        // d'expiration
        return !LocalDate.now().isAfter(dateExpiration);
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    @Override
    public String toString() {
        return "Billet: " + super.toString() + ", Expire le: " + dateExpiration +
                ", Valide: " + (estValide() ? "Oui" : "Non");
    }
}
