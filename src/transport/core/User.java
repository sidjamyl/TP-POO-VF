package transport.core;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private boolean handicaped; // Nouveau champ pour indiquer si l'utilisateur est handicapé

    public User(String id, String firstName, String lastName, String dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.handicaped = false; // Par défaut, pas de handicap
    }

    // Nouveau constructeur avec handicap
    public User(String id, String firstName, String lastName, String dateOfBirth, boolean handicaped) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.handicaped = handicaped;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    // Getter et setter pour le handicap
    public boolean isHandicaped() {
        return handicaped;
    }

    public void setHandicaped(boolean handicaped) {
        this.handicaped = handicaped;
    }
}
