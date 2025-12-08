package ymt_odev.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Factory pattern ile Database işlemlerini yöneten sınıf
 */
public class DatabaseManager implements IDatabaseCommands {

    protected Connection connection;
    protected DatabaseConnection dbConnection;
    private IDatabaseCommands manager;

    public DatabaseManager() {
        // Boş constructor - alt sınıflar için
    }

    /**
     * Belirtilen tipte bir database manager oluşturur
     * @param databaseManager "Insert", "Select", veya "Delete"
     */
    public void CreateManager(String databaseManager){
        if (databaseManager.equalsIgnoreCase("Insert")){
            this.manager = new DBDataInsertion();
        }
        else if (databaseManager.equalsIgnoreCase("Select")){
            this.manager = new DBDataSelection();
        }
        else if (databaseManager.equalsIgnoreCase("Delete")) {
            this.manager = new DBDataDeleter();
        } else if (databaseManager.equalsIgnoreCase("Update")) {
            this.manager = new DBDataUpdater();
        }
    }
    public IDatabaseCommands getManager(){
        return this.manager;
    }

    @Override
    public boolean insertData(String tableName, String[] columns, Object[] values) {
        if (manager instanceof DBDataInsertion)
            return this.manager.insertData(tableName, columns, values);
        return false;
    }

    @Override
    public boolean insertBatchData(String query, Object[][] batchValues) {
        if (manager instanceof DBDataInsertion)
            return this.manager.insertBatchData(query, batchValues);
        return false;
    }
    public boolean insertHotelGuest(String name, String surname, String phone, String email, String tcKimlik, String password, String loyaltyLevel, int totalBookings ) {
        if (manager instanceof DBDataInsertion)
            ((DBDataInsertion) manager).insertHotelGuest(name, surname, phone, email, tcKimlik, password, loyaltyLevel, totalBookings);
        return false;
    }

    @Override
    public boolean updateDataWithCondition(String tableName, String[] columns, String[] condition, String[] values, String[] whereClause, Object[] whereParams) {
        if (manager instanceof DBDataUpdater)
            return this.manager.updateDataWithCondition(tableName, columns, condition, values, whereClause, whereParams);;
        return false;
    }

    @Override
    public boolean deleteData(String tableName, String whereClause, Object[] params) {
        if (manager instanceof DBDataDeleter)
            return this.manager.deleteData(tableName, whereClause, params);;
        return false;
    }

    @Override
    public ResultSet selectData(String tableName, String[] columns) {
        if (manager != null && manager instanceof DBDataSelection)
            return this.manager.selectData(tableName, columns);
        return null;
    }

    @Override
    public ResultSet selectDataWithCondition(String tableName, String[] columns, String[] condition, String[] values) {
        if (manager != null && manager instanceof DBDataSelection)
            return this.manager.selectDataWithCondition(tableName, columns, condition, values);
        return null;
    }
    protected void closeResources(PreparedStatement preparedStatement, Connection connection) {
        if (preparedStatement != null) try {
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("prepared statement kapatma hatası: " + e.getMessage());
        }
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        dbConnection.closeConnection(connection);
    }
}
