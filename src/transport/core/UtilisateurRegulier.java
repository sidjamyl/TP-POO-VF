package transport.core;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class UtilisateurRegulier extends Utilisateur {
    private static final long serialVersionUID = 1L;
    private static int compteurSequentiel = 1000; // Point de départ pour les IDs séquentiels

    private CategorieUtilisateur categorie;

    // Constructeur public
    public UtilisateurRegulier(String prenom, String nom, String dateDeNaissance, boolean handicape) {
        // Générer automatiquement l'ID séquentiel pour les utilisateurs réguliers
        super(String.format("U%04d", ++compteurSequentiel), prenom, nom, dateDeNaissance, handicape);
        calculerCategorie();
    }

    // Ce constructeur est uniquement pour la sous-classe Employe qui fournira son
    // propre ID
    protected UtilisateurRegulier(String id, String prenom, String nom, String dateDeNaissance, boolean handicape) {
        super(id, prenom, nom, dateDeNaissance, handicape);
        calculerCategorie();
    }

    // Calculer automatiquement la catégorie de l'utilisateur selon les critères
    private void calculerCategorie() {
        // Priorité 1: Statut de handicap (carte Solidarité)
        if (estHandicape()) {
            this.categorie = CategorieUtilisateur.SOLIDARITE;
            return;
        }

        // Priorité 2: Statut d'employé (carte Partenaire) - sera défini dans la
        // sous-classe Employe

        // Priorité 3: Catégories basées sur l'âge
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateNaissance = LocalDate.parse(getDateDeNaissance(), formatter);
        LocalDate aujourdhui = LocalDate.now();

        int age = Period.between(dateNaissance, aujourdhui).getYears();

        if (age < 25) {
            this.categorie = CategorieUtilisateur.JUNIOR;
        } else if (age >= 65) { // Changé de 60 à 65 selon les exigences
            this.categorie = CategorieUtilisateur.SENIOR;
        } else {
            this.categorie = CategorieUtilisateur.REGULIER;
        }
    }

    public CategorieUtilisateur getCategorie() {
        return categorie;
    }

    // Changé de protected à public
    public void setCategorie(CategorieUtilisateur categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + categorie + ")";
    }

    public enum CategorieUtilisateur {
        REGULIER(0),
        JUNIOR(30), // 30% pour les usagers de moins de 25 ans
        SENIOR(25), // 25% pour les usagers de plus de 65 ans
        SOLIDARITE(50), // 50% pour les personnes à mobilité réduite
        PARTENAIRE(40); // 40% pour les employés de l'entreprise

        private final int pourcentageReduction;

        CategorieUtilisateur(int pourcentageReduction) {
            this.pourcentageReduction = pourcentageReduction;
        }

        public int getPourcentageReduction() {
            return pourcentageReduction;
        }
    }
}
