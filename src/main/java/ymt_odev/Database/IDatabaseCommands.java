package ymt_odev.Database;

import java.sql.ResultSet;

public interface IDatabaseCommands {
    boolean insertData(String tableName, String[] columns, Object[] values);
    boolean insertBatchData(String query, Object[][] batchValues);
    boolean updateDataWithCondition(String tableName, String[] columns, String[] condition, String[] values, String[] whereClause, Object[] whereParams);
    boolean deleteData(String tableName, String whereClause, Object[] params);
    ResultSet selectData(String tableName, String[] columns);
    ResultSet selectDataWithCondition(String tableName, String[] columns, String[] condition, String[] values);
}
