package ymt_odev.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBDataUpdater extends DatabaseManager{
    protected Connection connection;
    protected DatabaseConnection dbConnection;
    public DBDataUpdater() {
    }

    public boolean updateDataWithCondition(String tableName, String[] columns, String[] condition, String[] values, String[] whereClause, Object[] whereParams) {
        if (tableName == null || tableName.isBlank()) return false;
        if (columns.length != values.length) return false;
        if (condition.length != values.length) return false;
        if (whereClause.length != whereParams.length) return false;
        PreparedStatement ps;
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < condition.length; i++) {
            query.append(columns[i]).append(" = ? ");
            if (i < condition.length - 1) {
                query.append(", ");
            }
        }
        query.append(" WHERE ");
        for (int i = 0; i < whereClause.length; i++) {
            query.append(whereClause[i]).append(" = ? ");
            if (i < whereClause.length - 1) {
                query.append(" AND ");
            }
        }
        try {
            dbConnection = DatabaseConnection.getInstance();
            connection = dbConnection.getConnection();
        ps = connection.prepareStatement(query.toString());

        for (int i = 0; i < values.length; i++) {
            ps.setObject(i + 1, values[i]);
        }
        for (int i = 0; i < whereParams.length; i++) {
            ps.setObject(i + condition.length + 1, whereParams[i]);
        }
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
        }catch (SQLException e){
            System.out.println("Update sırasında hata: "+ e.getMessage());
        }

        return false;
    }
    public boolean updateBatchData(String query, Object[][] batchValues) {
        return false;
    }

}
