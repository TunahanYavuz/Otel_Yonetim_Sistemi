package ymt_odev.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract base class for database operations.
 * Alt sınıflar için ortak işlevsellik sağlar.
 */
public abstract class DatabaseManager {

    protected Connection connection;
    protected DatabaseConnection dbConnection;

    /**
     * Veritabanı bağlantısını alır
     */
    protected Connection getDbConnection() {
        dbConnection = DatabaseConnection.getInstance();
        return dbConnection.getConnection();
    }

    /**
     * Kaynakları temizler
     */
    protected void closeResources(PreparedStatement preparedStatement, Connection connection) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                System.err.println("PreparedStatement kapatma hatası: " + e.getMessage());
            }
        }
        if (dbConnection != null) {
            dbConnection.closeConnection(connection);
        }
    }

    // Alt sınıflar tarafından implement edilecek metodlar
    public boolean insertData(String tableName, String[] columns, Object[] values) {
        throw new UnsupportedOperationException("Bu işlem bu sınıf tarafından desteklenmiyor");
    }

    public boolean updateDataWithCondition(String tableName, String[] columns, String[] condition,
                                           String[] values, String[] whereClause, Object[] whereParams) {
        throw new UnsupportedOperationException("Bu işlem bu sınıf tarafından desteklenmiyor");
    }

    public boolean deleteData(String tableName, String whereClause, Object[] params) {
        throw new UnsupportedOperationException("Bu işlem bu sınıf tarafından desteklenmiyor");
    }

    public ResultSet selectData(String tableName, String[] columns) {
        throw new UnsupportedOperationException("Bu işlem bu sınıf tarafından desteklenmiyor");
    }

    public ResultSet selectDataWithCondition(String tableName, String[] columns, String[] condition, String[] values) {
        throw new UnsupportedOperationException("Bu işlem bu sınıf tarafından desteklenmiyor");
    }

    public ResultSet selectDataWithProximity(String tableName, String condition, String value) {
        throw new UnsupportedOperationException("Bu işlem bu sınıf tarafından desteklenmiyor");
    }
}
