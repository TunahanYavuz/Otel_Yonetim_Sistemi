package ymt_odev.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBDataInsertion extends DatabaseManager {


    public boolean insertData(String tableName, String[] columns, Object[] values) {
        if (columns.length != values.length) return false;
        StringBuilder query = new StringBuilder("Insert Into ");
        query.append(tableName).append(" (");

        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(") Values (");

        for (int i = 0; i < values.length; i++) {
            query.append("?");
            if (i < values.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        dbConnection = DatabaseConnection.getInstance();
        connection = dbConnection.getConnection();
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
            e.printStackTrace();
            return false;
        } finally {
            closeResources(preparedStatement, connection);
        }

    }



    public boolean insertHotelGuest(String name, String surname, String phone, String email, String tcKimlik, String password, String loyaltyLevel, int totalBookings ) {
        String query = "INSERT INTO Customers (name,email, phone,  tcKimlik, password, loyaltyLevel, totalBookings, createdDate) VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())";

        dbConnection = DatabaseConnection.getInstance();
        connection = dbConnection.getConnection();
        PreparedStatement preparedStatement = null;
        if (connection == null) return false;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name + " "+ surname);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, tcKimlik);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, loyaltyLevel);
            preparedStatement.setInt(7, totalBookings);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("prepared statement hatası: " + e.getMessage());
            return false;
        } finally {
            closeResources(preparedStatement, connection);
        }
    }

    public boolean insertBatchData(String query, Object[][] batchValues) {
        dbConnection = DatabaseConnection.getInstance();
        connection = dbConnection.getConnection();
        PreparedStatement preparedStatement = null;
        if (connection == null) return false;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);

            for (Object[] values : batchValues) {
                for (int i = 0; i < values.length; i++) {
                    preparedStatement.setObject(i + 1, values[i]);
                }
                preparedStatement.addBatch();
            }

            int[] result = preparedStatement.executeBatch();
            connection.commit();
            return result.length > 0;

        }catch (SQLException e) {
            System.out.println("Batch insert hatası: " + e.getMessage());
            try {
                connection.rollback();
            }catch (SQLException ex) {
                System.out.println("Rollback hatası: " + ex.getMessage());
            }
            return false;
        }finally {
            try {
                connection.setAutoCommit(true);
            }catch (SQLException ex) {
                System.out.println("Auto-comit ayarlama hatası: " + ex.getMessage());
            }
            closeResources(preparedStatement, connection);
        }
    }

}
