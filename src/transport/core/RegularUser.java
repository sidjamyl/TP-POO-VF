package transport.core;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class RegularUser extends User {
    private static final long serialVersionUID = 1L;
    private static int sequentialCounter = 1000; // Starting point for sequential IDs

    private UserCategory category;

    // Changed to public constructor
    public RegularUser(String firstName, String lastName, String dateOfBirth, boolean handicaped) {
        // Generate sequential ID automatically for regular users
        super(String.format("U%04d", ++sequentialCounter), firstName, lastName, dateOfBirth, handicaped);
        calculateCategory();
    }

    // This constructor is only for Employee subclass that will provide its own ID
    protected RegularUser(String id, String firstName, String lastName, String dateOfBirth, boolean handicaped) {
        super(id, firstName, lastName, dateOfBirth, handicaped);
        calculateCategory();
    }

    // Calculate user category automatically based on criteria
    private void calculateCategory() {
        // Priority 1: Handicap status (Solidarity card)
        if (isHandicaped()) {
            this.category = UserCategory.SOLIDARITY;
            return;
        }

        // Priority 2: Employee status (Partner card) - will be set in Employee subclass

        // Priority 3: Age-based categories
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthDate = LocalDate.parse(getDateOfBirth(), formatter);
        LocalDate today = LocalDate.now();

        int age = Period.between(birthDate, today).getYears();

        if (age < 25) {
            this.category = UserCategory.JUNIOR;
        } else if (age >= 65) { // Changed from 60 to 65 as per requirements
            this.category = UserCategory.SENIOR;
        } else {
            this.category = UserCategory.REGULAR;
        }
    }

    public UserCategory getCategory() {
        return category;
    }

    // Changed from protected to public
    public void setCategory(UserCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + category + ")";
    }

    public enum UserCategory {
        REGULAR(0),
        JUNIOR(30), // 30% pour les usagers de moins de 25 ans
        SENIOR(25), // 25% pour les usagers de plus de 65 ans
        SOLIDARITY(50), // 50% pour les Personnes à mobilité réduite
        PARTNER(40); // 40% pour les employés de l'entreprise

        private final int discountPercentage;

        UserCategory(int discountPercentage) {
            this.discountPercentage = discountPercentage;
        }

        public int getDiscountPercentage() {
            return discountPercentage;
        }
    }
}
