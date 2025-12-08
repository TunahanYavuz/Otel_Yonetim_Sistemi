package ymt_odev.Patterns;

/**
 * Strategy Pattern - Banka transferi ödeme stratejisi
 */
public class BankTransferPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount, String customerInfo) {
        System.out.println("Banka transferi işleniyor: " + amount + " TL");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Banka Transferi";
    }
}

