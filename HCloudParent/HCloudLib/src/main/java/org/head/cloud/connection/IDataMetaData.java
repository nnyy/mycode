
package org.head.cloud.connection;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public interface IDataMetaData {	
	public Connection getConn();
	public void close();
	public DatabaseMetaData getDataMeta() throws SQLException;
	public <T> List<T> getDataBases();
	public Map<String, List<String>>  getTablesMeta();
	public List<String> getTablesMeta(String db);
	public <T> List<T> getColumnMetaData(String db,String tableName);
	public <T> List<T> getPrimaryKeyMetaData( String tableName,String dbName);
	public <T> List<T> getExportKeys(String tableName,String dbName);
	public <T> List<T> getImportKeys(String tableName,String dbName);
	public void release(ResultSet rs, Statement stmt);
}
