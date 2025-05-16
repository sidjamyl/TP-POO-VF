package transport.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class TransportTitle implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titleId;
    private User owner;
    private LocalDate purchaseDate;
    private PaymentMethod paymentMethod;
    private double price;

    public TransportTitle(String titleId, User owner, PaymentMethod paymentMethod) {
        this.titleId = titleId;
        this.owner = owner;
        this.purchaseDate = LocalDate.now();
        this.paymentMethod = paymentMethod;
        calculatePrice();
    }

    protected abstract void calculatePrice();

    public abstract boolean isValid();

    public String getTitleId() {
        return titleId;
    }

    public User getOwner() {
        return owner;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public String getFormattedPurchaseDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return purchaseDate.format(formatter);
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public double getPrice() {
        return price;
    }

    protected void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Title ID: " + titleId + ", Owner: " + owner.getFullName() +
                ", Purchase Date: " + getFormattedPurchaseDate() + ", Price: " + price + " DA";
    }

    public enum PaymentMethod {
        CASH("Espèces"),
        DAHABIA("Dahabia"),
        BARIDIMOB("BaridiMob");

        private final String display;

        PaymentMethod(String display) {
            this.display = display;
        }

        @Override
        public String toString() {
            return display;
        }
    }
}
