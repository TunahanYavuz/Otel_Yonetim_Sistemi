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
        // Önce rezervasyonun var olup olmadığını kontrol et
        if (!isReservationExists(reservationId)) {
            System.err.println("❌ Hata: Rezervasyon bulunamadı. ID: " + reservationId);
            return;
        }

        DatabaseManager inserter = new DBDataInsertion();

        String[] columns = new String[]{
                "reservationId", "amount", "paymentMethod", "paymentType", "transactionId"
        };

        String transactionId = "TXN" + System.currentTimeMillis();

        Object[] values = new Object[]{
                reservationId, amount, paymentMethod, "DEPOSIT", transactionId
        };

        boolean success = inserter.insertData("Payments", columns, values);
        if (!success) {
            System.err.println("❌ Ödeme kaydedilemedi. ReservationId: " + reservationId);
        }
    }

    /**
     * Rezervasyonun var olup olmadığını kontrol eder
     */
    private boolean isReservationExists(int reservationId) {
        ymt_odev.Database.DBDataSelection selector = new ymt_odev.Database.DBDataSelection();
        try {
            java.sql.ResultSet rs = selector.selectDataWithCondition(
                    "Reservations",
                    new String[]{"id"},
                    new String[]{"id"},
                    new String[]{String.valueOf(reservationId)}
            );
            boolean exists = rs != null && rs.next();
            if (rs != null) rs.close();
            return exists;
        } catch (Exception e) {
            System.err.println("Rezervasyon kontrol hatası: " + e.getMessage());
            return false;
        }
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

