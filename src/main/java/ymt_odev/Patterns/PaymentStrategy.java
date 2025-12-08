package ymt_odev.Patterns;

/**
 * Strategy Pattern - Ödeme stratejisi arayüzü
 */
public interface PaymentStrategy {
    boolean processPayment(double amount, String customerInfo);
    String getPaymentMethod();
}

