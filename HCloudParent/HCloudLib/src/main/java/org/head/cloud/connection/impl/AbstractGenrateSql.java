package org.head.cloud.connection.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.head.cloud.connection.IGenerateSql;
import org.head.cloud.db.DataBaseType;
import org.head.cloud.util.Column;
import org.head.cloud.util.FieldValue;
import org.head.cloud.util.ForeignKey;

public abstract class AbstractGenrateSql implements IGenerateSql {

	@Override
	public String generateDMLSQL(DataBaseType dataBaseType, List<Column> cols, String tableName) {
		StringBuffer strBuf = null;
		if (dataBaseType == DataBaseType.PHOENIX) {
			strBuf = new StringBuffer("UPSERT  INTO ");
		} else {
			strBuf = new StringBuffer("INSERT INTO ");
		}
		strBuf.append(tableName);
		strBuf.append("(");
		String fields = "", value = "";

		for (Column col : cols) {
			fields = fields + col.getColumnName() + ",";
			value = value + "?,";
		}
		fields = fields.substring(0, fields.lastIndexOf(","));
		value = value.substring(0, value.lastIndexOf(","));
		strBuf.append(fields + ")");
		strBuf.append(" VALUES(");
		strBuf.append(value + ")");
		// values.forEach(row -> {
		// List<FieldValue> rowValue = new ArrayList<>();
		// for (int i = 0, j = row.size(); i < j; i++) {
		// FieldValue fieldValue = new FieldValue(i + 1, row.get(i),
		// cols.get(i).getSqlType(), 0);
		// rowValue.add(fieldValue);
		// }
		// outvalueList.add(rowValue);
		// });

		return strBuf.toString();
	}

	@Override
	public List<String> generateAlterDMLSQL(DataBaseType destType, List<ForeignKey> fkeys) {
		// TODO Auto-generated method stub
		List<String> alertSqlList = new ArrayList<>();
		switch (destType) {
		case MYSQL:
			if (null != fkeys && !fkeys.isEmpty())
				fkeys.forEach(fkey -> {
					StringBuffer buf = new StringBuffer("ALTER TABLE ");
					buf.append(fkey.getForeignKeyTable());
					buf.append(" ADD  CONSTRAINT `");
					buf.append(fkey.getForeignKeyName());
					buf.append("`  FOREIGN KEY(`");
					buf.append(fkey.getForeignKeyColumnName());
					buf.append("`) REFERENCES `");
					buf.append(fkey.getPkTableName());
					buf.append("`(`");
					buf.append(fkey.getPkColumnName());
					buf.append("`)");
					alertSqlList.add(buf.toString());
				});

			break;
		case ORACLE:
			if (null != fkeys && !fkeys.isEmpty())
				fkeys.forEach(fkey -> {
					StringBuffer buf = new StringBuffer("ALTER TABLE ");
					buf.append(fkey.getForeignKeyTable());
					buf.append(" ADD  CONSTRAINT ");
					buf.append(fkey.getForeignKeyName());
					buf.append("  FOREIGN KEY(");
					if (fkey.getForeignKeyColumnName().length() > 30) {
						Random random = new Random();
						int num = random.nextInt(9);
						buf.append(fkey.getForeignKeyColumnName().substring(0, 28) + num);
					} else {
						buf.append(fkey.getForeignKeyColumnName());
					}
					buf.append(") REFERENCES  ");
					buf.append(fkey.getPkTableName());
					buf.append("(");
					buf.append(fkey.getPkColumnName());
					buf.append(")");
					alertSqlList.add(buf.toString());
				});
			break;
		case DB2:
		case MSSQL:
		case SYSBASE:
		case POSTGRESQL:
		case INFORMIX:
			if (null != fkeys && !fkeys.isEmpty())
				fkeys.forEach(fkey -> {
					StringBuffer buf = new StringBuffer("ALTER TABLE ");
					buf.append(fkey.getForeignKeyTable());
					buf.append(" ADD  CONSTRAINT ");
					buf.append(fkey.getForeignKeyName());
					buf.append("  FOREIGN KEY(");
					buf.append(fkey.getForeignKeyColumnName());
					buf.append(") REFERENCES  ");
					buf.append(fkey.getPkTableName());
					buf.append("(");
					buf.append(fkey.getPkColumnName());
					buf.append(")");
					alertSqlList.add(buf.toString());
				});
			break;

		default:
			break;
		}

		return alertSqlList;
	}
}
