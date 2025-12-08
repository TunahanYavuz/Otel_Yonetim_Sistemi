package ymt_odev.Services;

import ymt_odev.Database.DatabaseManager;
import ymt_odev.Database.DBDataUpdater;
import ymt_odev.Users.Customer;
import ymt_odev.Users.User;

/**
 * Kullanıcı profil ve kimlik doğrulama servisi
 */
public class UserService {

    /**
     * Müşteri profil bilgilerini günceller
     */
    public static boolean updateCustomerProfile(int customerId, String firstName, String lastName,
                                                 String email, String phone, String tcKimlik) {
        DatabaseManager updater = new DBDataUpdater();

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

    /**
     * Şifre değiştirir (müşteri veya personel)
     */
    public static boolean changePassword(User user, String oldPassword, String newPassword) {
        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }

        DatabaseManager updater = new DBDataUpdater();
        String tableName = user.getRole().equals("CUSTOMER") ? "Customers" : "Staff";

        String[] columns = new String[]{"password"};
        String[] values = new String[]{newPassword};
        String[] whereClause = new String[]{"id"};
        Object[] whereParams = new Object[]{user.getId()};

        return updater.updateDataWithCondition(
                tableName,
                columns,
                columns,
                values,
                whereClause,
                whereParams
        );
    }

    /**
     * Personel profil bilgilerini günceller
     */
    public static boolean updateStaffProfile(int staffId, String firstName, String lastName,
                                             String email, String phone, String tcKimlik) {
        DatabaseManager updater = new DBDataUpdater();

        String[] columns = new String[]{"name", "email", "phone", "tcKimlik"};
        String[] values = new String[]{firstName + " " + lastName, email, phone, tcKimlik};
        String[] whereClause = new String[]{"id"};
        Object[] whereParams = new Object[]{staffId};

        return updater.updateDataWithCondition(
                "Staff",
                columns,
                columns,
                values,
                whereClause,
                whereParams
        );
    }
}

