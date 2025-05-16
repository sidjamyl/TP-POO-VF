package transport.core;

import java.time.LocalDate;

public class Ticket extends TransportTitle {
    private static final long serialVersionUID = 1L;
    private static final double BASE_PRICE = 50.0;

    public Ticket(String ticketId, User owner, PaymentMethod paymentMethod) {
        super(ticketId, owner, paymentMethod);
    }

    @Override
    protected void calculatePrice() {
        setPrice(BASE_PRICE);
    }

    @Override
    public boolean isValid() {
        // Ticket is valid only on the purchase date
        return getPurchaseDate().equals(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Ticket: " + super.toString() + ", Valid: " + (isValid() ? "Yes" : "No");
    }
}
