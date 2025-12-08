package ymt_odev.Patterns;

/**
 * Strategy Pattern - Nakit ödeme stratejisi
 */
public class CashPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount, String customerInfo) {
        System.out.println("Nakit ödeme alındı: " + amount + " TL");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Nakit";
    }
}

