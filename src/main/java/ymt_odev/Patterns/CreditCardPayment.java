package ymt_odev.Patterns;

/**
 * Strategy Pattern - Kredi kartı ödeme stratejisi
 */
public class CreditCardPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount, String customerInfo) {
        System.out.println("Kredi kartı ile ödeme işleniyor: " + amount + " TL");
        // Gerçek ödeme işlemi simülasyonu
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Kredi Kartı";
    }
}

