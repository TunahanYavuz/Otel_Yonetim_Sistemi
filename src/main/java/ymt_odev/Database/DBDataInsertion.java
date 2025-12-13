package ymt_odev.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Veritabanına veri ekleme işlemlerini yöneten sınıf
 */
public class DBDataInsertion extends DatabaseManager {

    @Override
    public boolean insertData(String tableName, String[] columns, Object[] values) {
        if (columns.length != values.length) return false;

        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");

        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(") VALUES (");

        for (int i = 0; i < values.length; i++) {
            query.append("?");
            if (i < values.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        connection = getDbConnection();
        PreparedStatement preparedStatement = null;

        if (connection == null) return false;

        try {
            preparedStatement = connection.prepareStatement(query.toString());

            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Insert hatası: " + e.getMessage());
            return false;
        } finally {
            closeResources(preparedStatement, connection);
        }
    }
}
