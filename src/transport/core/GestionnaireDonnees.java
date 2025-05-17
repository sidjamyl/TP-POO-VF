package transport.core;

import java.io.*;
import java.util.*;
import transport.core.Reclamation.TypeEntite;

public class GestionnaireDonnees {
    private static final String REPERTOIRE_DONNEES = "donnees/";
    private static final String FICHIER_UTILISATEURS = REPERTOIRE_DONNEES + "utilisateurs.dat";
    private static final String FICHIER_TITRES = REPERTOIRE_DONNEES + "titres.dat";
    private static final String FICHIER_RECLAMATIONS = REPERTOIRE_DONNEES + "reclamations.dat";

    private List<Utilisateur> utilisateurs;
    private List<TitreTransport> titres;
    private List<Reclamation> reclamations;
    private int compteurIdTitre = 1000; // Compteur pour les IDs séquentiels de titres

    private static GestionnaireDonnees instance;

    private GestionnaireDonnees() {
        utilisateurs = new ArrayList<>();
        titres = new ArrayList<>();
        reclamations = new ArrayList<>();

        // Créer le répertoire de données s'il n'existe pas
        File repertoire = new File(REPERTOIRE_DONNEES);
        if (!repertoire.exists()) {
            repertoire.mkdirs();
        }

        chargerDonnees();
    }

    public static GestionnaireDonnees getInstance() {
        if (instance == null) {
            instance = new GestionnaireDonnees();
        }
        return instance;
    }

    // Méthodes utilisateur
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurs.add(utilisateur);
        sauvegarderUtilisateurs();
    }

    public List<Utilisateur> getUtilisateurs() {
        return new ArrayList<>(utilisateurs);
    }

    public Utilisateur getUtilisateurParId(String id) {
        return utilisateurs.stream()
                .filter(utilisateur -> utilisateur.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Méthodes titre
    public void ajouterTitre(TitreTransport titre) {
        titres.add(titre);
        sauvegarderTitresTransport();
    }

    public List<TitreTransport> getTitres() {
        // Trier par date d'achat (les plus récents d'abord)
        List<TitreTransport> titresTries = new ArrayList<>(titres);
        titresTries.sort((t1, t2) -> t2.getDateAchat().compareTo(t1.getDateAchat()));
        return titresTries;
    }

    public TitreTransport getTitreParId(String id) {
        return titres.stream()
                .filter(titre -> titre.getIdTitre().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Méthode pour obtenir le prochain ID séquentiel de titre
    public synchronized String getProchainIdTitre() {
        return "T" + (++compteurIdTitre);
    }

    // Méthodes réclamation
    public void ajouterReclamation(Reclamation reclamation) {
        reclamations.add(reclamation);
        sauvegarderReclamations();
    }

    public List<Reclamation> getReclamations() {
        return new ArrayList<>(reclamations);
    }

    // Méthodes améliorées pour les réclamations
    public int getNombreReclamationsPourEntite(String entite, TypeEntite typeEntite) {
        return (int) reclamations.stream()
                .filter(r -> r.getEntiteConcernee().equals(entite) && r.getTypeEntite() == typeEntite)
                .count();
    }

    // Conserver la compatibilité avec les versions antérieures
    public int getNombreReclamationsPourEntite(String entite) {
        return (int) reclamations.stream()
                .filter(r -> r.getEntiteConcernee().equals(entite))
                .count();
    }

    // Méthodes de persistance des données
    @SuppressWarnings("unchecked")
    private void chargerDonnees() {
        // Charger utilisateurs
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_UTILISATEURS))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                // Cast sécurisé avec vérification des génériques
                utilisateurs = (List<Utilisateur>) obj;
            }
        } catch (FileNotFoundException e) {
            utilisateurs = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des utilisateurs: " + e.getMessage());
            utilisateurs = new ArrayList<>();
        }

        // Charger titres de transport
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_TITRES))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                // Cast sécurisé avec vérification des génériques
                titres = (List<TitreTransport>) obj;

                // Mettre à jour le compteur d'ID en fonction des IDs existants
                for (TitreTransport titre : titres) {
                    String id = titre.getIdTitre();
                    if (id != null && id.startsWith("T")) {
                        try {
                            int idNumerique = Integer.parseInt(id.substring(1));
                            if (idNumerique > compteurIdTitre) {
                                compteurIdTitre = idNumerique;
                            }
                        } catch (NumberFormatException e) {
                            // Ignorer les IDs non numériques
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            titres = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des titres: " + e.getMessage());
            titres = new ArrayList<>();
        }

        // Charger réclamations
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_RECLAMATIONS))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                // Cast sécurisé avec vérification des génériques
                reclamations = (List<Reclamation>) obj;
            }
        } catch (FileNotFoundException e) {
            reclamations = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des réclamations: " + e.getMessage());
            reclamations = new ArrayList<>();
        }
    }

    public void sauvegarderUtilisateurs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_UTILISATEURS))) {
            oos.writeObject(utilisateurs);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des utilisateurs: " + e.getMessage());
        }
    }

    public void sauvegarderTitresTransport() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_TITRES))) {
            oos.writeObject(titres);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des titres de transport: " + e.getMessage());
        }
    }

    public void sauvegarderReclamations() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_RECLAMATIONS))) {
            oos.writeObject(reclamations);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des réclamations: " + e.getMessage());
        }
    }

    public void sauvegarderToutesDonnees() {
        sauvegarderUtilisateurs();
        sauvegarderTitresTransport();
        sauvegarderReclamations();
    }
}
