package ymt_odev.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBDataDeleter extends DatabaseManager {

    protected Connection connection;
    protected DatabaseConnection dbConnection;

    /**
     * Belirtilen tablodan veri siler
     * @param tableName Tablo adı
     * @param whereClause WHERE koşulu (örn: "id = ?")
     * @param params Parametreler
     * @return Başarılı ise true
     */
    public boolean deleteData(String tableName, String whereClause, Object[] params) {
        if (tableName == null || tableName.isBlank() || whereClause == null) return false;

        String query = "DELETE FROM " + tableName + " WHERE " + whereClause;
        dbConnection = DatabaseConnection.getInstance();
        connection = dbConnection.getConnection();
        PreparedStatement ps = null;

        if (connection == null) return false;

        try {
            ps = connection.prepareStatement(query.toString());
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("Delete hatası: " + e.getMessage());
            return false;
        } finally {
            closeResources(ps, connection);
        }
    }

}
