package ymt_odev.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Veritabanından veri seçme işlemlerini yöneten sınıf
 */
public class DBDataSelection extends DatabaseManager {

    /**
     * Tablo'dan veri seçer ve ResultSet döndürür
     * @param tableName Tablo adı
     * @param columns Seçilecek kolonlar
     * @return ResultSet - null ise hata var demektir
     */
    @Override
    public ResultSet selectData(String tableName, String[] columns) {
        connection = getDbConnection();

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

        } catch (SQLException e) {
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
    @Override
    public ResultSet selectDataWithCondition(String tableName, String[] columns, String[] condition, String[] values) {
        connection = getDbConnection();

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

            if (condition.length > 0 && values.length > 0 && condition.length == values.length) {
                query.append(" WHERE ");
                for (int i = 0; i < condition.length; i++) {
                    query.append(condition[i]).append(" = ?");
                    if (i < condition.length - 1) {
                        query.append(" AND ");
                    }
                }
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }

            return preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Select hatası: " + e.getMessage());
            return null;
        }
    }

    /**
     * LIKE sorgusu ile veri seçer (yakınlık araması)
     * @param tableName Tablo adı
     * @param condition Koşul kolonu
     * @param value Aranacak değer
     * @return ResultSet - null ise hata var demektir
     */
    @Override
    public ResultSet selectDataWithProximity(String tableName, String condition, String value) {
        connection = getDbConnection();

        if (connection == null) {
            System.err.println("Veritabanı bağlantısı yok");
            return null;
        }

        try {
            String query = "SELECT * FROM " + tableName +
                          " INNER JOIN Customers ON Customers.id = " + tableName + ".customerId " +
                          "WHERE " + condition + " LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + value + "%");

            return preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.err.println("Select hatası: " + e.getMessage());
            return null;
        }
    }
}
