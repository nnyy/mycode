package org.head.cloud.connection;

import java.util.List;
import java.util.Map;

import org.head.cloud.db.DataBaseType;
import org.head.cloud.util.Column;
import org.head.cloud.util.FieldValue;
import org.head.cloud.util.ForeignKey;

public interface IGenerateSql {

	public String generateDDLSQL(DataBaseType destDbtype, DataBaseType sourceDbType, List<Column> cols,
			String tableName, List<String> pkeys);

	public String generateDMLSQL(DataBaseType dataBaseType,List<Column> cols, String tableName);
	
	public List<String> generateAlterDMLSQL(DataBaseType destType,List<ForeignKey> fkeys);

}
