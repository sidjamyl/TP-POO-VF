package transport.core;

/**
 * Represents the different functions an employee can have in the transport
 * system.
 */
public enum TypeFonction {
    /**
     * Agent de station (station agent)
     */
    AGENT("Agent de station"),

    /**
     * Conducteur (driver)
     */
    CONDUCTEUR("Conducteur");

    private final String displayName;

    TypeFonction(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
