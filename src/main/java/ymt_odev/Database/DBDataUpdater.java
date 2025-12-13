package ymt_odev.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Veritabanında veri güncelleme işlemlerini yöneten sınıf
 */
public class DBDataUpdater extends DatabaseManager {

    @Override
    public boolean updateDataWithCondition(String tableName, String[] columns, String[] condition,
                                           String[] values, String[] whereClause, Object[] whereParams) {
        if (tableName == null || tableName.isBlank()) return false;
        if (columns.length != values.length) return false;
        if (condition.length != values.length) return false;
        if (whereClause.length != whereParams.length) return false;

        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]).append(" = ?");
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(" WHERE ");
        for (int i = 0; i < whereClause.length; i++) {
            query.append(whereClause[i]).append(" = ?");
            if (i < whereClause.length - 1) {
                query.append(" AND ");
            }
        }

        connection = getDbConnection();
        PreparedStatement ps = null;

        if (connection == null) return false;

        try {
            ps = connection.prepareStatement(query.toString());

            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            for (int i = 0; i < whereParams.length; i++) {
                ps.setObject(i + values.length + 1, whereParams[i]);
            }

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Update hatası: " + e.getMessage());
            return false;
        } finally {
            closeResources(ps, connection);
        }
    }
}
