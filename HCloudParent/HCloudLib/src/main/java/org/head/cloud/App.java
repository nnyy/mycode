package org.head.cloud;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.head.cloud.connection.IDataMetaData;
import org.head.cloud.connection.IGenerateSql;
import org.head.cloud.connection.command.ExecuteSql;
import org.head.cloud.connection.factory.MetaDataFactory;
import org.head.cloud.connection.impl.MySqlGenerateSql;
import org.head.cloud.connection.impl.OracleGenerateSql;
import org.head.cloud.connection.impl.SQLServerGenerateSql;
import org.head.cloud.db.DataBaseType;
import org.head.cloud.util.AlterSql;
import org.head.cloud.util.Column;
import org.head.cloud.util.FieldValue;
import org.head.cloud.util.ForeignKey;
import org.head.cloud.util.Table;
import org.head.cloud.util.TableSql;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws SQLException {
	//	 TestCreate();
		String[] tables = new String[] {"useradd","userfrieds","usertb","depttb","bonus","emp","dept","testtable"};
		IGenerateSql iGenerateSql = new MySqlGenerateSql();
		String dbName = "stu";
		DataBaseType destType = DataBaseType.PHOENIX;
		IDataMetaData im = MetaDataFactory.getDataMeta("mysql");
		for (String table : tables) {
			if (table != null) {
				exe(table, "phoenix", "mysql", dbName, im, iGenerateSql, tables, destType);
			}
		}
		im.close();

	}

	private static void TestCreate() throws Exception {
		IDataMetaData im = MetaDataFactory.getDataMeta("mysql");
		List<String> tables = im.getTablesMeta("stu");
		List<Table> tableList = new ArrayList<>();
		ExecuteSql executeSql = new ExecuteSql();
		tables.forEach(table -> {
			Table tb = new Table();
			tb.setTableName("myschema"+"."+table);
			tb.setDest(DataBaseType.PHOENIX);
			tb.setSoure(DataBaseType.MYSQL);
			List<Column> cols = im.getColumnMetaData("stu", table);
			tb.setCols(cols);
			tb.setFkeys(im.getImportKeys(table, "stu"));
			tb.setPks(im.getPrimaryKeyMetaData(table, "stu"));
			try {
				tb.setValueLists(executeSql.executeQuery("mysql",DataBaseType.MSSQL, "select * from " + table, cols));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tableList.add(tb);
		});
		im.close();

		List<TableSql> tableSqlList = new ArrayList<>();
		AlterSql as = new AlterSql();
		as.setDestConnName("phoenix");
		MySqlGenerateSql mysqlgl = new MySqlGenerateSql();
		//OracleGenerateSql mysqlgl=new OracleGenerateSql();
		tableList.forEach(tb -> {
			TableSql ts = new TableSql();
			String ddl = mysqlgl.generateDDLSQL(tb.getDest(), tb.getSoure(), tb.getCols(), tb.getTableName(),
					tb.getPks());
			ts.setCreateSql(ddl);
			ts.setDestConnName("phoenix");
			//List<List<FieldValue>> outList = new ArrayList<>();
			String insertSql = mysqlgl.generateDMLSQL(tb.getDest(), tb.getCols(),  tb.getTableName());
			ts.setInsertSql(insertSql);
			tableSqlList.add(ts);
			List<String> alterSql = mysqlgl.generateAlterDMLSQL(tb.getDest(), tb.getFkeys());
			as.addLists(alterSql);
		});

		tableSqlList.forEach(item -> {
			try {
				executeSql.executeDDL(item.getDestConnName(), item.getCreateSql());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// executeSql.executeBatchUpdate(item.getDestConnName(), item.getInsertSql(),
			// item.getValueList());
		});

		tableSqlList.forEach(item -> {
			try {
				executeSql.executeBatchUpdate(item.getDestConnName(), item.getInsertSql(), item.getValueList());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	//	executeSql.executeUpdateBatch(as.getLists(), as.getConnName());
	}

	public static void exe(String table, String destConnName, String sourceConnName, String dbName, IDataMetaData im,
			IGenerateSql iGenerateSql, String[] tables, DataBaseType destType) throws SQLException {
		List<ForeignKey> fkeys = im.getImportKeys(table, dbName);
		if (fkeys != null && !fkeys.isEmpty()) {
			for (ForeignKey fkey : fkeys) {
				exe(fkey.getPkTableName(), destConnName, sourceConnName, dbName, im, iGenerateSql, tables, destType);
			}
		}
		boolean flag = false;
		for (int i = 0, j = tables.length; i < j; i++) {
			if (null != tables[i] && tables[i].equals(table)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			ExecuteSql executeSql = new ExecuteSql();
			List<Column> cols = im.getColumnMetaData(dbName, table);
			List<List<FieldValue>> rowsValues = executeSql.executeQuery(sourceConnName,DataBaseType.PHOENIX, "select * from " + table, cols);
			String sql = iGenerateSql.generateDMLSQL(DataBaseType.PHOENIX, cols, "myschema."+table);
			executeSql.executeBatchUpdate(destConnName, sql, rowsValues);
			for (int i = 0, j = tables.length; i < j; i++) {
				if (null != tables[i] && tables[i].equals(table)) {
					tables[i] = null;
					break;
				}
			}
		}

	}
}
