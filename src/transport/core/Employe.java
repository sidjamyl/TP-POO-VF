package transport.core;

public class Employe extends UtilisateurRegulier {
    private static final long serialVersionUID = 1L;

    private String matricule; // C'est l'identifiant employé
    private TypeFonction fonction;

    public Employe(String matricule, String prenom, String nom, String dateDeNaissance,
            boolean handicape, TypeFonction fonction) {
        // Utiliser le matricule comme ID utilisateur (avec préfixe)
        super("E" + matricule, prenom, nom, dateDeNaissance, handicape);
        this.matricule = matricule;
        this.fonction = fonction;

        // Si non handicapé, ils obtiennent la catégorie PARTENAIRE
        // Sinon, ils gardent la catégorie SOLIDARITE (qui est meilleure à 50% contre
        // 40%)
        if (!estHandicape()) {
            setCategorie(CategorieUtilisateur.PARTENAIRE);
        }
    }

    public String getMatricule() {
        return matricule;
    }

    public TypeFonction getFonction() {
        return fonction;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + fonction;
    }
}
