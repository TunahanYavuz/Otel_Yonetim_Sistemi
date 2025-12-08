package ymt_odev.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBDataSelection kullanım örnekleri - Yeni ResultSet tabanlı yapı
 */
public class DataSelectionExample {

    public static void main(String[] args) {
        DBDataSelection selector = new DBDataSelection();

        // Örnek 1: Tüm müşterileri getir
        System.out.println("=== Tüm Müşteriler ===");
        ResultSet resultSet = selector.selectData(
                "Customers",
                new String[]{"id", "name", "email", "loyaltyLevel"}
        );

        try {
            if (resultSet != null && resultSet.next()) {
                int count = 0;
                do {
                    count++;
                    System.out.println("ID: " + resultSet.getObject("id") +
                                     ", İsim: " + resultSet.getObject("name") +
                                     ", Email: " + resultSet.getObject("email") +
                                     ", Seviye: " + resultSet.getObject("loyaltyLevel"));
                } while (resultSet.next());
                System.out.println("Toplam " + count + " müşteri bulundu.");
            } else {
                System.out.println("Müşteri bulunamadı.");
            }
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
            System.err.println("Hata: " + e.getMessage());
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Örnek 2: Belirli seviyedeki müşterileri getir
        System.out.println("=== Gold Seviyesindeki Müşteriler ===");
        ResultSet customerResult = selector.selectDataWithCondition(
                "Customers",
                new String[]{"id", "name", "email", "phone"},
                new String[]{"loyaltyLevel"},
                new String[]{"Gold"}
        );

        try {
            if (customerResult != null && customerResult.next()) {
                int count = 0;
                do {
                    count++;
                    System.out.println("- " + customerResult.getObject("name") +
                                     " (" + customerResult.getObject("email") + ")" +
                                     " Tel: " + customerResult.getObject("phone"));
                } while (customerResult.next());
                System.out.println("Gold müşteri sayısı: " + count);
            } else {
                System.out.println("Gold seviyesinde müşteri bulunamadı.");
            }
            if (customerResult != null) customerResult.close();
        } catch (SQLException e) {
            System.err.println("Hata: " + e.getMessage());
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Örnek 3: Login kontrolü
        System.out.println("=== Login Kontrolü ===");
        String email = "test@example.com";
        String password = "123456";

        ResultSet loginResult = selector.selectDataWithCondition(
                "Customers",
                new String[]{"id", "name", "email", "loyaltyLevel"},
                new String[]{"email", "password"},
                new String[]{email, password}
        );

        try {
            if (loginResult != null && loginResult.next()) {
                System.out.println("✅ Login başarılı!");
                System.out.println("Hoş geldiniz, " + loginResult.getObject("name"));
                System.out.println("Seviyeniz: " + loginResult.getObject("loyaltyLevel"));
            } else {
                System.out.println("❌ Email veya şifre hatalı!");
            }
            if (loginResult != null) loginResult.close();
        } catch (SQLException e) {
            System.err.println("Hata: " + e.getMessage());
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Örnek 4: Müsait odaları getir
        System.out.println("=== Müsait Odalar ===");
        ResultSet roomsResult = selector.selectDataWithCondition(
                "Rooms",
                new String[]{"id", "roomNumber", "roomType", "price"},
                new String[]{"status"},
                new String[]{"Available"}
        );

        try {
            if (roomsResult != null && roomsResult.next()) {
                int count = 0;
                do {
                    count++;
                    System.out.println("Oda " + roomsResult.getObject("roomNumber") +
                                     " - " + roomsResult.getObject("roomType") +
                                     " - ₺" + roomsResult.getObject("price"));
                } while (roomsResult.next());
                System.out.println("Müsait oda sayısı: " + count);
            } else {
                System.out.println("Müsait oda bulunamadı.");
            }
            if (roomsResult != null) roomsResult.close();
        } catch (SQLException e) {
            System.err.println("Hata: " + e.getMessage());
        }
    }
}
