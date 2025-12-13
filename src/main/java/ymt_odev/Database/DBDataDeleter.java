package ymt_odev.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Veritabanından veri silme işlemlerini yöneten sınıf
 */
public class DBDataDeleter extends DatabaseManager {

    /**
     * Belirtilen tablodan veri siler
     * @param tableName Tablo adı
     * @param whereClause WHERE koşulu (örn: "id = ?")
     * @param params Parametreler
     * @return Başarılı ise true
     */
    @Override
    public boolean deleteData(String tableName, String whereClause, Object[] params) {
        if (tableName == null || tableName.isBlank() || whereClause == null) return false;

        String query = "DELETE FROM " + tableName + " WHERE " + whereClause;
        connection = getDbConnection();
        PreparedStatement ps = null;

        if (connection == null) return false;

        try {
            ps = connection.prepareStatement(query);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Delete hatası: " + e.getMessage());
            return false;
        } finally {
            closeResources(ps, connection);
        }
    }
}
