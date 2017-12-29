package org.head.cloud.connection.impl;

import java.util.ArrayList;
import java.util.List;

import org.head.cloud.connection.IGenerateSql;
import org.head.cloud.db.DataBaseType;
import org.head.cloud.util.Column;
import org.head.cloud.util.FieldValue;
import org.head.cloud.util.ForeignKey;

public class PhoenixGenerateSql extends AbstractGenrateSql {

	@Override
	public String generateDDLSQL(DataBaseType destDbtype, DataBaseType sourceDbType, List<Column> cols,
			String tableName,List<String> pkeys) {
		// TODO Auto-generated method stub
		return null;
	}

}
