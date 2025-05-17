package transport.core;

import java.time.LocalDate;
import transport.core.UtilisateurRegulier.CategorieUtilisateur;

public class CarteNavigation extends TitreTransport {
    private static final long serialVersionUID = 1L;
    private static final double PRIX_BASE = 5000.0;

    private LocalDate dateExpiration;

    public CarteNavigation(String idCarte, UtilisateurRegulier proprietaire, MethodePaiement methodePaiement)
            throws ExceptionTransport {
        super(idCarte, proprietaire, methodePaiement);

        // Vérifier si l'utilisateur a droit à une réduction
        if (proprietaire.getCategorie() == CategorieUtilisateur.REGULIER) {
            throw new ExceptionReductionImpossible(". Vous n'avez droit à aucune réduction.");
        }

        this.dateExpiration = getDateAchat().plusMonths(12); // Validité d'un an

        // Recalcul du prix pour appliquer la réduction
        calculerPrix();
    }

    @Override
    protected void calculerPrix() {
        if (!(getProprietaire() instanceof UtilisateurRegulier)) {
            setPrix(PRIX_BASE);
            return;
        }

        UtilisateurRegulier utilisateur = (UtilisateurRegulier) getProprietaire();
        CategorieUtilisateur categorie = utilisateur.getCategorie();

        // Appliquer la réduction selon la catégorie
        double reduction = categorie.getPourcentageReduction() / 100.0;
        double prixFinal = PRIX_BASE * (1 - reduction);

        // Sortie de débogage
        System.out.println("Calcul du prix de la carte de navigation:");
        System.out.println("- Catégorie utilisateur: " + categorie);
        System.out.println("- Pourcentage de réduction: " + categorie.getPourcentageReduction() + "%");
        System.out.println("- Prix de base: " + PRIX_BASE + " DA");
        System.out.println("- Prix final: " + prixFinal + " DA");

        setPrix(prixFinal);
    }

    @Override
    public boolean estValide() {
        // La carte est valide si la date actuelle est antérieure ou égale à la date
        // d'expiration
        return !LocalDate.now().isAfter(dateExpiration);
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    @Override
    public String toString() {
        return "Carte de navigation: " + super.toString() + ", Expire le: " + dateExpiration +
                ", Valide: " + (estValide() ? "Oui" : "Non");
    }
}
