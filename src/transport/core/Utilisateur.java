package transport.core;

import java.io.Serializable;

public abstract class Utilisateur implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String prenom;
    private String nom;
    private String dateDeNaissance;
    private boolean handicape;

    public Utilisateur(String id, String prenom, String nom, String dateDeNaissance) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.dateDeNaissance = dateDeNaissance;
        this.handicape = false; // Par défaut, pas de handicap
    }

    // Constructeur avec handicap
    public Utilisateur(String id, String prenom, String nom, String dateDeNaissance, boolean handicape) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.dateDeNaissance = dateDeNaissance;
        this.handicape = handicape;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return getNomComplet();
    }

    public boolean estHandicape() {
        return handicape;
    }

    public void setHandicape(boolean handicape) {
        this.handicape = handicape;
    }
}
