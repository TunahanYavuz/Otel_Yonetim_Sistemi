package ymt_odev.Database;

import java.sql.*;

public class DBDataSelection extends DatabaseManager {

    protected Connection connection;
    protected DatabaseConnection dbConnection;

    /**
     * Tablo'dan veri seçer ve ResultSet döndürür
     * @param tableName Tablo adı
     * @param columns Seçilecek kolonlar
     * @return ResultSet - null ise hata var demektir
     */
    public ResultSet selectData(String tableName, String[] columns) {
        dbConnection = DatabaseConnection.getInstance();
        connection = dbConnection.getConnection();

        if (connection == null) {
            System.err.println("Veritabanı bağlantısı yok");
            return null;
        }

        try {
            StringBuilder query = new StringBuilder("SELECT ");
            for (int i = 0; i < columns.length; i++) {
                query.append(columns[i]);
                if (i < columns.length - 1) {
                    query.append(", ");
                }
            }
            query.append(" FROM ").append(tableName);

            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            return preparedStatement.executeQuery();

        } catch (Exception e) {
            System.err.println("Select hatası: " + e.getMessage());
            return null;
        }
    }

    /**
     * Koşullu veri seçer ve ResultSet döndürür
     * @param tableName Tablo adı
     * @param columns Seçilecek kolonlar
     * @param condition Koşul kolonları (WHERE kısmı)
     * @param values Koşul değerleri
     * @return ResultSet - null ise hata var demektir
     */
    public ResultSet selectDataWithCondition(String tableName, String[] columns, String[] condition, String[] values) {
        dbConnection = DatabaseConnection.getInstance();
        connection = dbConnection.getConnection();

        if (connection == null) {
            System.err.println("Veritabanı bağlantısı yok");
            return null;
        }

        try {
            PreparedStatement preparedStatement = getPreparedStatement(tableName, columns, condition, values);
            return preparedStatement.executeQuery();

        } catch (Exception e) {
            System.err.println("Select hatası: " + e.getMessage());
            return null;
        }
    }



    private PreparedStatement getPreparedStatement(String tableName, String[] columns, String[] condition, String[] values) throws SQLException {
        PreparedStatement preparedStatement;
        StringBuilder query = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(" FROM ").append(tableName);
        if (condition.length > 0 && values.length > 0 && condition.length == values.length) {
            query.append(" WHERE ");
            for (int i = 0; i < condition.length; i++) {
                query.append(condition[i]).append(" = ?");
                if (i < condition.length - 1) {
                    query.append(" AND ");
                }
            }
        }
        preparedStatement = connection.prepareStatement(query.toString());

        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
        return preparedStatement;
    }
}
