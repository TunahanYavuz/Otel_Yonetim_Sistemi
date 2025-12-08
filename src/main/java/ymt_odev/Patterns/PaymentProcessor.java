package ymt_odev.Patterns;

import ymt_odev.Database.DBDataInsertion;
import ymt_odev.Database.DatabaseManager;

/**
 * Strategy Pattern - Ödeme işlemlerini yöneten context sınıfı
 */
public class PaymentProcessor {
    private PaymentStrategy strategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Seçilen strateji ile ödeme işler
     */
    public boolean processPayment(int reservationId, double amount, String customerInfo) {
        if (strategy == null) {
            System.err.println("Ödeme stratejisi belirlenmedi!");
            return false;
        }

        boolean success = strategy.processPayment(amount, customerInfo);

        if (success) {
            // Ödeme kaydını veritabanına ekle
            recordPayment(reservationId, amount, strategy.getPaymentMethod());
        }

        return success;
    }

    /**
     * Ödeme kaydını veritabanına ekler
     */
    private void recordPayment(int reservationId, double amount, String paymentMethod) {
        DatabaseManager inserter = new DBDataInsertion();

        String[] columns = new String[]{
                "reservationId", "amount", "paymentMethod", "paymentType", "transactionId"
        };

        String transactionId = "TXN" + System.currentTimeMillis();

        Object[] values = new Object[]{
                reservationId, amount, paymentMethod, "DEPOSIT", transactionId
        };

        inserter.insertData("Payments", columns, values);
    }

    /**
     * Factory method - Ödeme yöntemine göre strateji oluşturur
     */
    public static PaymentStrategy createPaymentStrategy(String paymentMethod) {
        switch (paymentMethod.toLowerCase()) {
            case "kredi kartı":
            case "credit card":
                return new CreditCardPayment();
            case "nakit":
            case "cash":
                return new CashPayment();
            case "havale":
            case "transfer":
                return new BankTransferPayment();
            default:
                return new CashPayment();
        }
    }
}

