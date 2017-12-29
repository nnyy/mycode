package org.head.cloud.connection.factory;

import org.head.cloud.connection.IGenerateSql;
import org.head.cloud.connection.impl.DB2GenerateSql;
import org.head.cloud.connection.impl.HiveGenerateSql;
import org.head.cloud.connection.impl.InformixGenerateSql;
import org.head.cloud.connection.impl.MySqlGenerateSql;
import org.head.cloud.connection.impl.OracleGenerateSql;
import org.head.cloud.connection.impl.PhoenixGenerateSql;
import org.head.cloud.connection.impl.PostgreSQLGenerateSql;
import org.head.cloud.connection.impl.SQLServerGenerateSql;
import org.head.cloud.connection.impl.SysbaseGenerateSql;
import org.head.cloud.db.DataBaseType;

public class GenerateSqlFactory {

	public static IGenerateSql createGenerateSql(DataBaseType dataBaseType) {
		IGenerateSql generateSql = null;
		switch (dataBaseType) {
		case MYSQL:
			generateSql = new MySqlGenerateSql();
			break;
		case MSSQL:
			generateSql = new SQLServerGenerateSql();
			break;
		case ORACLE:
			generateSql = new OracleGenerateSql();
			break;
		case DB2:
			generateSql = new DB2GenerateSql();
			break;
		case INFORMIX:
			generateSql = new InformixGenerateSql();
			break;
		case SYSBASE:
			generateSql = new SysbaseGenerateSql();
			break;
		case POSTGRESQL:
			generateSql = new PostgreSQLGenerateSql();
			break;
		case PHOENIX:
			generateSql = new PhoenixGenerateSql();
			break;
		case HIVE:
			generateSql = new HiveGenerateSql();
			break;
		default:
			break;
		}
		return generateSql;
	}
}
