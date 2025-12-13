package ymt_odev.Services;

import ymt_odev.Database.DBDataInsertion;
import ymt_odev.Database.DBDataSelection;
import ymt_odev.Database.DatabaseManager;
import ymt_odev.LoyaltyLevel;
import ymt_odev.Users.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Müşteri yönetim servisi (Personel için)
 */
public class CustomerService {

    /**
     * Tüm müşterileri listeler
     */
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        DatabaseManager selector = new DBDataSelection();

        try {
            ResultSet rs = selector.selectData("Customers", new String[]{"*"});

            while (rs != null && rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }

            if (rs != null) rs.close();

        } catch (SQLException e) {
            System.err.println("Müşteri listesi hatası: " + e.getMessage());
        }

        return customers;
    }

    /**
     * Müşteri arama (ad, email, tc)
     */
    public static List<Customer> searchCustomers(String searchTerm) {
        List<Customer> customers = new ArrayList<>();
        DatabaseManager selector = new DBDataSelection();

        try {
            // Email ile arama
            ResultSet rs = selector.selectDataWithCondition(
                    "Customers",
                    new String[]{"*"},
                    new String[]{"email"},
                    new String[]{searchTerm}
            );

            if (rs != null && rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
                rs.close();
                return customers;
            }

            // TC ile arama
            rs = selector.selectDataWithCondition(
                    "Customers",
                    new String[]{"*"},
                    new String[]{"tcKimlik"},
                    new String[]{searchTerm}
            );

            if (rs != null && rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
                rs.close();
                return customers;
            }

            // İsim ile aramada basit LIKE kullanılamadığından tüm müşterileri alıp filtreleriz
            rs = selector.selectData("Customers", new String[]{"*"});

            while (rs != null && rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String fullName = name + " " + surname;

                if (fullName.toLowerCase().contains(searchTerm.toLowerCase())) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }

            if (rs != null) rs.close();

        } catch (SQLException e) {
            System.err.println("Müşteri arama hatası: " + e.getMessage());
        }

        return customers;
    }

    /**
     * Yeni müşteri kaydı (Personel tarafından)
     */
    public static boolean registerCustomer(String firstName, String lastName, String email,
                                           String phone, String tcKimlik, String password) {
        DatabaseManager inserter = new DBDataInsertion();

        String[] columns = new String[]{
                "name", "surname", "email", "phone", "tcKimlik", "password", "loyaltyLevel", "totalBookings"
        };

        Object[] values = new Object[]{
                firstName, lastName, email, phone, tcKimlik, password, LoyaltyLevel.BRONZE.toString(), 0
        };

        return inserter.insertData("Customers", columns, values);
    }

    /**
     * Müşteriyi ID ile getirir
     */
    public static Customer getCustomerById(int customerId) {
        DatabaseManager selector = new DBDataSelection();

        try {
            ResultSet rs = selector.selectDataWithCondition(
                    "Customers",
                    new String[]{"*"},
                    new String[]{"id"},
                    new String[]{String.valueOf(customerId)}
            );

            if (rs != null && rs.next()) {
                Customer customer = mapResultSetToCustomer(rs);
                rs.close();
                return customer;
            }

        } catch (SQLException e) {
            System.err.println("Müşteri getirme hatası: " + e.getMessage());
        }

        return null;
    }

    /**
     * ResultSet'ten Customer nesnesine dönüştürme
     */
    private static Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt("id"),
                rs.getString("email"), // username olarak email kullanılıyor
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("tcKimlik"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("createdDate"),
                rs.getString("loyaltyLevel"),
                rs.getInt("totalBookings"),
                rs.getBoolean("isActive")
        );
    }

    /**
     * Müşteri bilgilerini günceller
     */
    public static boolean updateCustomer(int customerId, String firstName, String lastName,
                                         String email, String phone, String tcKimlik) {
        ymt_odev.Database.DBDataUpdater updater = new ymt_odev.Database.DBDataUpdater();

        String[] columns = new String[]{"name", "surname", "email", "phone", "tcKimlik"};
        String[] values = new String[]{firstName, lastName, email, phone, tcKimlik};
        String[] whereClause = new String[]{"id"};
        Object[] whereParams = new Object[]{customerId};

        return updater.updateDataWithCondition(
                "Customers",
                columns,
                columns,
                values,
                whereClause,
                whereParams
        );
    }
}

