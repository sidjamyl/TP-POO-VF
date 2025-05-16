package transport.core;

public class Employee extends RegularUser {
    private static final long serialVersionUID = 1L;

    private String employeeId; // This is the matricule
    private TypeFonction fonction;

    public Employee(String employeeId, String firstName, String lastName, String dateOfBirth,
            boolean handicaped, TypeFonction fonction) {
        // Use employee ID as the user ID (with prefix)
        super("E" + employeeId, firstName, lastName, dateOfBirth, handicaped);
        this.employeeId = employeeId;
        this.fonction = fonction;

        // If not handicapped, they get the PARTNER category
        // Otherwise, they keep the SOLIDARITY category (which is better at 50% vs 40%)
        if (!isHandicaped()) {
            setCategory(UserCategory.PARTNER);
        }
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public TypeFonction getFonction() {
        return fonction;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + fonction;
    }
}
