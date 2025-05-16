package transport.core;

import java.time.LocalDate;
import transport.core.RegularUser.UserCategory;

public class NavigationCard extends TransportTitle {
    private static final long serialVersionUID = 1L;
    private static final double BASE_PRICE = 5000.0; // Prix de base modifié à 5000.0

    private LocalDate expirationDate;

    public NavigationCard(String cardId, RegularUser owner, PaymentMethod paymentMethod) throws TransportException {
        super(cardId, owner, paymentMethod);

        // Vérifier si l'utilisateur a droit à une réduction
        if (owner.getCategory() == UserCategory.REGULAR) {
            throw new ReductionImpossibleException(". Vous n'avez droit à aucune réduction.");
        }

        this.expirationDate = getPurchaseDate().plusMonths(12); // Modifié pour une validité d'un an (12 mois)

        // Force price calculation again to ensure discount is applied
        calculatePrice();
    }

    @Override
    protected void calculatePrice() {
        if (!(getOwner() instanceof RegularUser)) {
            setPrice(BASE_PRICE);
            return;
        }

        RegularUser user = (RegularUser) getOwner();
        UserCategory category = user.getCategory();

        // Apply discount based on user category
        double discount = category.getDiscountPercentage() / 100.0;
        double finalPrice = BASE_PRICE * (1 - discount);

        // Debug output to console
        System.out.println("NavigationCard price calculation:");
        System.out.println("- User Category: " + category);
        System.out.println("- Discount Percentage: " + category.getDiscountPercentage() + "%");
        System.out.println("- Base Price: " + BASE_PRICE + " DA");
        System.out.println("- Final Price: " + finalPrice + " DA");

        setPrice(finalPrice);
    }

    @Override
    public boolean isValid() {
        // Card is valid if the current date is before or equal to the expiration date
        return !LocalDate.now().isAfter(expirationDate);
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String toString() {
        return "Navigation Card: " + super.toString() + ", Expires: " + expirationDate +
                ", Valid: " + (isValid() ? "Yes" : "No");
    }
}
