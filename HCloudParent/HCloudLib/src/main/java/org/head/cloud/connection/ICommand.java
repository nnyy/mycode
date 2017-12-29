package org.head.cloud.connection;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.head.cloud.db.DataBaseType;
import org.head.cloud.util.Column;
import org.head.cloud.util.FieldValue;
import org.head.cloud.util.Table;

public interface ICommand {

	public Map<String, Object> executeQuery(String connName, String selectsql) throws SQLException;

	public List<List<Object>> executeQuery(String connName, String selectsql, List<Column> cols) throws SQLException;

	public int executeDDL(String connName, String ddlSql) throws SQLException;

	public int executeUpdate(String connName, String updateSql) throws SQLException;
	
	public int executeUpdate(String connName,String Sql,List<FieldValue> valueList) throws SQLException;
	
	public int  executeUpdateBatch(List<String> params,String connName) throws SQLException;
	
	public List<List<FieldValue>> executeQuery(String connName,DataBaseType destDbType, String selectsql ,List<Column> cols) throws SQLException;

}
