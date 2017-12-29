package org.head.cloud.connection.impl;

import java.sql.JDBCType;
import java.util.List;
import java.util.UUID;

import org.head.cloud.db.DataBaseType;
import org.head.cloud.util.Column;

public class SQLServerGenerateSql extends AbstractGenrateSql {

	@Override
	public String generateDDLSQL(DataBaseType destDbtype, DataBaseType sourceDbType, List<Column> cols,
			String tableName, List<String> pkeys) {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("CREATE TABLE ");
		strBuf.append(tableName + "(");
		if (destDbtype == sourceDbType) {
			StringBuffer subStrBuf = new StringBuffer();
			cols.forEach(col -> {
				subStrBuf.append(col.getColumnName() + " " + col.getColumnSize());
				if (col.getColumnSize() > 0) {
					if (col.getDecimalDigits() > 0) {
						subStrBuf.append("(" + col.getColumnSize() + "," + col.getDecimalDigits() + ")");
					} else {
						subStrBuf.append("(" + col.getColumnSize() + ")");
					}
				}
				if (col.getNullLable() == 0) {
					subStrBuf.append(" NOT NULL");
				}

				subStrBuf.append(",");
			});
			if (null != pkeys && !pkeys.isEmpty()) {
				subStrBuf.append("PRIMARY KEY(");
				StringBuffer pkBuf = new StringBuffer();
				pkeys.forEach(pk -> {
					pkBuf.append(pk + ",");
				});
				subStrBuf.append(pkBuf.substring(0, pkBuf.lastIndexOf(",")));
				subStrBuf.append(")");
			} else {
				String str = subStrBuf.substring(0, subStrBuf.lastIndexOf(","));
				subStrBuf.delete(0, subStrBuf.length());
				subStrBuf.append(str);
			}
			strBuf.append(subStrBuf);
			strBuf.append(")");

		} else {
			StringBuffer subStrBuf = new StringBuffer();
			switch (destDbtype) {
			case MYSQL:
				cols.forEach(col -> {
					JDBCType colType = JDBCType.valueOf(col.getSqlType());
					switch (colType) {
					case BIT:
						subStrBuf.append(col.getColumnName() + " BIT");
						break;
					case TINYINT:
						subStrBuf.append(col.getColumnName() + " TINYINT");
						break;
					case SMALLINT:
						subStrBuf.append(col.getColumnName() + " SMALLINT");
						break;
					case INTEGER:
						subStrBuf.append(col.getColumnName() + " INT");
						break;
					case BIGINT:
						subStrBuf.append(col.getColumnName() + " BIGINT");
						break;
					case FLOAT:
						subStrBuf.append(col.getColumnName() + " FLOAT(" + col.getColumnSize() + ","
								+ col.getDecimalDigits() + ")");
						break;
					case REAL:
						subStrBuf.append(col.getColumnName() + " REAL");
						break;
					case DOUBLE:
						subStrBuf.append(col.getColumnName() + " DOUBLE(" + col.getColumnSize() + ","
								+ col.getDecimalDigits() + ")");
						break;
					case NUMERIC:
						if (col.getDecimalDigits() > 0) {
							subStrBuf.append(col.getColumnName() + " NUMERIC(" + col.getColumnSize() + ","
									+ col.getDecimalDigits() + ")");
						} else {
							subStrBuf.append(col.getColumnName() + " NUMERIC(" + col.getColumnSize() + ")");
						}
						break;
					case DECIMAL:
						subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ","
								+ col.getDecimalDigits() + ")");
						break;
					case CHAR:
						subStrBuf.append(col.getColumnName() + " CHAR(" + col.getColumnSize() + ")");
						break;
					case VARCHAR:
						if (col.getColumnSize() > 21000) {
							subStrBuf.append(col.getColumnName() + " LONGTEXT");
						} else {
							subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						}

						break;
					case LONGVARCHAR:
						subStrBuf.append(col.getColumnName() + "  LONGTEXT");
						break;
					case DATE:
						subStrBuf.append(col.getColumnName() + "  DATE");
						break;
					case TIME:
						subStrBuf.append(col.getColumnName() + "  TIME");
						break;
					case TIMESTAMP:
						subStrBuf.append(col.getColumnName() + "  DATETIME");
						break;
					case BINARY:
						subStrBuf.append(col.getColumnName() + "  BINARY(" + col.getColumnSize() + ")");
						break;
					case VARBINARY:
						if (col.getColumnSize() > 65000) {
							subStrBuf.append(col.getColumnName() + "  LONGBLOB");
						} else {
							subStrBuf.append(col.getColumnName() + "  VARBINARY(" + col.getColumnSize() + ")");
						}

						break;
					case LONGVARBINARY:
						subStrBuf.append(col.getColumnName() + "  LONGBLOB");
						break;
					case BLOB:
						subStrBuf.append(col.getColumnName() + "  BLOB");
						break;
					case CLOB:
						subStrBuf.append(col.getColumnName() + "  LONGTEXT");
						break;
					case NCHAR:
						subStrBuf.append(col.getColumnName() + " NCHAR(" + col.getColumnSize() + ")");
						break;
					case NVARCHAR:
						if (col.getColumnSize() > 21000) {
							subStrBuf.append(col.getColumnName() + " LONGTEXT");
						} else {
							subStrBuf.append(col.getColumnName() + " NVARCHAR(" + col.getColumnSize() + ")");
						}
						break;
					case LONGNVARCHAR:
						subStrBuf.append(col.getColumnName() + "  LONGTEXT");
						break;
					case BOOLEAN:
						subStrBuf.append(col.getColumnName() + "  BOOLEAN");
						break;
					default:
						subStrBuf.append(col.getColumnName() + "  VARCHAR(2000)");
						break;
					}
					if (col.getNullLable() == 0) {
						subStrBuf.append(" not null");
					}
					subStrBuf.append(",");
				});
				if (null != pkeys && !pkeys.isEmpty()) {
					subStrBuf.append(" PRIMARY KEY(");
					StringBuffer pkBuf = new StringBuffer();
					pkeys.forEach(pk -> {
						pkBuf.append("`" + pk + "`,");
					});
					subStrBuf.append(pkBuf.substring(0, pkBuf.lastIndexOf(",")));
					subStrBuf.append(")");
				} else {
					String str = subStrBuf.substring(0, subStrBuf.lastIndexOf(","));
					subStrBuf.delete(0, subStrBuf.length());
					subStrBuf.append(str);
				}
				strBuf.append(subStrBuf);
				strBuf.append(")");
				break;
			case ORACLE:
				cols.forEach(col -> {
					JDBCType colType = JDBCType.valueOf(col.getSqlType());
					switch (colType) {
					case BIT:
						subStrBuf.append(col.getColumnName() + " NUMBER(" + col.getColumnSize() + ")");
						break;
					case TINYINT:
						subStrBuf.append(col.getColumnName() + " NUMBER(" + col.getColumnSize() + ")");
						break;
					case SMALLINT:
						subStrBuf.append(col.getColumnName() + " NUMBER(" + col.getColumnSize() + ")");
						break;
					case INTEGER:
						subStrBuf.append(col.getColumnName() + " INTEGER");
						break;
					case BIGINT:
						subStrBuf.append(col.getColumnName() + " NUMBER(" + col.getColumnSize() + ")");
						break;
					case FLOAT:
						subStrBuf.append(col.getColumnName() + " FLOAT(" + col.getColumnSize() + ")");
						break;
					case REAL:
						subStrBuf.append(col.getColumnName() + " REAL");
						break;
					case DOUBLE:
						subStrBuf.append(col.getColumnName() + " NUMBER(" + col.getColumnSize() + ","
								+ col.getDecimalDigits() + ")");
						break;
					case NUMERIC:
						if (col.getDecimalDigits() > 0) {
							subStrBuf.append(col.getColumnName() + " NUMBER(" + col.getColumnSize() + ","
									+ col.getDecimalDigits() + ")");
						} else {
							subStrBuf.append(col.getColumnName() + " NUMBER(" + col.getColumnSize() + ")");
						}
						break;
					case DECIMAL:
						subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ","
								+ col.getDecimalDigits() + ")");
						break;
					case CHAR:
						subStrBuf.append(col.getColumnName() + " CHAR(" + col.getColumnSize() + ")");
						break;
					case VARCHAR:
						if (col.getColumnSize() > 4000) {
							subStrBuf.append(col.getColumnName() + " CLOB");
						} else {
							subStrBuf.append(col.getColumnName() + " VARCHAR2(" + col.getColumnSize() + ")");
						}
						break;
					case LONGVARCHAR:
						subStrBuf.append(col.getColumnName() + " CLOB");
						break;
					case DATE:
						subStrBuf.append(col.getColumnName() + "  DATE");
						break;
					case TIME:
						subStrBuf.append(col.getColumnName() + "  DATE");
						break;
					case TIMESTAMP:
						subStrBuf.append(col.getColumnName() + "  DATE");
						break;
					case BINARY:
						if (col.getColumnSize() > 2000) {
							subStrBuf.append(col.getColumnName() + "  BLOB");
						} else {
							subStrBuf.append(col.getColumnName() + "  RAW(" + col.getColumnSize() + ")");
						}
						break;
					case VARBINARY:
						if (col.getColumnSize() > 2000) {
							subStrBuf.append(col.getColumnName() + "  BLOB");
						} else {
							subStrBuf.append(col.getColumnName() + "  RAW(" + col.getColumnSize() + ")");
						}
						break;
					case LONGVARBINARY:
						subStrBuf.append(col.getColumnName() + "  BLOB");
						break;
					case BLOB:
						subStrBuf.append(col.getColumnName() + "  BLOB");
						break;
					case CLOB:
						subStrBuf.append(col.getColumnName() + "  CLOB");
						break;
					case NCHAR:
						subStrBuf.append(col.getColumnName() + " NCHAR(" + col.getColumnSize() + ")");
						break;
					case NVARCHAR:
						if (col.getColumnSize() > 4000) {
							subStrBuf.append(col.getColumnName() + "  NCLOB");
						} else {
							subStrBuf.append(col.getColumnName() + " NVARCHAR2(" + col.getColumnSize() + ")");
						}
						break;
					case LONGNVARCHAR:
						subStrBuf.append(col.getColumnName() + "  NCLOB");
						break;
					case BOOLEAN:
						subStrBuf.append(col.getColumnName() + " NUMBER(1)");
						break;
					default:
						subStrBuf.append(col.getColumnName() + "  VARCHAR2(2000)");
						break;
					}
					if (col.getNullLable() == 0) {
						subStrBuf.append(" not null");
					}
					subStrBuf.append(",");
				});
				if (null != pkeys && !pkeys.isEmpty()) {
					subStrBuf.append(" PRIMARY KEY(");
					StringBuffer pkBuf = new StringBuffer();
					pkeys.forEach(pk -> {
						pkBuf.append(pk + ",");
					});
					subStrBuf.append(pkBuf.substring(0, pkBuf.lastIndexOf(",")));
					subStrBuf.append(")");
				} else {
					String str = subStrBuf.substring(0, subStrBuf.lastIndexOf(","));
					subStrBuf.delete(0, subStrBuf.length());
					subStrBuf.append(str);
				}
				strBuf.append(subStrBuf);
				strBuf.append(")");
				break;
			case DB2:
				cols.forEach(col -> {
					JDBCType colType = JDBCType.valueOf(col.getSqlType());
					switch (colType) {
					case BIT:
						subStrBuf.append(col.getColumnName() + " SMALLINT");
						break;
					case TINYINT:
						subStrBuf.append(col.getColumnName() + " SMALLINT");
						break;
					case SMALLINT:
						subStrBuf.append(col.getColumnName() + " SMALLINT");
						break;
					case INTEGER:
						subStrBuf.append(col.getColumnName() + " INTEGER");
						break;
					case BIGINT:
						subStrBuf.append(col.getColumnName() + " BIGINT");
						break;
					case FLOAT:
						subStrBuf.append(col.getColumnName() + " FLOAT(" + col.getColumnSize() + ")");
						break;
					case REAL:
						subStrBuf.append(col.getColumnName() + " REAL");
						break;
					case DOUBLE:
						subStrBuf.append(col.getColumnName() + " DOUBLE(" + col.getColumnSize() + ")");
						break;
					case NUMERIC:
						if (col.getDecimalDigits() > 0) {
							subStrBuf.append(col.getColumnName() + " NUMERIC(" + col.getColumnSize() + ","
									+ col.getDecimalDigits() + ")");
						} else {
							subStrBuf.append(col.getColumnName() + " NUMERIC(" + col.getColumnSize() + ")");
						}
						break;
					case DECIMAL:
						subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ","
								+ col.getDecimalDigits() + ")");
						break;
					case CHAR:
						subStrBuf.append(col.getColumnName() + " CHAR(" + col.getColumnSize() + ")");
						break;
					case VARCHAR:
						subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						break;
					case LONGVARCHAR:
						subStrBuf.append(col.getColumnName() + " CLOB");
						break;
					case DATE:
						subStrBuf.append(col.getColumnName() + "  DATE");
						break;
					case TIME:
						subStrBuf.append(col.getColumnName() + "  TIME");
						break;
					case TIMESTAMP:
						subStrBuf.append(col.getColumnName() + "  TIMESTAMP");
						break;
					case BINARY:
						// CHAR(n) FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  CHAR(" + col.getColumnSize() + ") FOR BIT DATA");
						break;
					case VARBINARY:
						// VARCHAR(n) FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  VARCHAR(" + col.getColumnSize() + ") FOR BIT DATA");
						break;
					case LONGVARBINARY:
						// LONG VARCHAR FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  LONG VARCHAR FOR BIT DATA");
						break;
					case BLOB:
						subStrBuf.append(col.getColumnName() + "  BLOB");
						break;
					case CLOB:
						subStrBuf.append(col.getColumnName() + "  CLOB");
						break;
					case NCHAR:
						subStrBuf.append(col.getColumnName() + " NCHAR(" + col.getColumnSize() + ")");
						break;
					case NVARCHAR:
						subStrBuf.append(col.getColumnName() + " VARGRAPHIC(" + col.getColumnSize() + ")");
						break;
					case LONGNVARCHAR:
						subStrBuf.append(col.getColumnName() + "  DBCLOB(" + col.getColumnSize() + ")");
						break;
					case BOOLEAN:
						subStrBuf.append(col.getColumnName() + " SMALLINT");
						break;
					default:
						subStrBuf.append(col.getColumnName() + "  VARCHAR(2000)");
						break;
					}
					if (col.getNullLable() == 0) {
						subStrBuf.append(" not null");
					}
					subStrBuf.append(",");
				});
				if (null != pkeys && !pkeys.isEmpty()) {
					subStrBuf.append(" PRIMARY KEY(");
					StringBuffer pkBuf = new StringBuffer();
					pkeys.forEach(pk -> {
						pkBuf.append(pk + ",");
					});
					subStrBuf.append(pkBuf.substring(0, pkBuf.lastIndexOf(",")));
					subStrBuf.append(")");
				} else {
					String str = subStrBuf.substring(0, subStrBuf.lastIndexOf(","));
					subStrBuf.delete(0, subStrBuf.length());
					subStrBuf.append(str);
				}
				strBuf.append(subStrBuf);
				strBuf.append(")");
				break;
			case SYSBASE:
				cols.forEach(col -> {
					JDBCType colType = JDBCType.valueOf(col.getSqlType());
					switch (colType) {
					case BIT:
						subStrBuf.append(col.getColumnName() + " BIT");
						break;
					case TINYINT:
						subStrBuf.append(col.getColumnName() + " TINYINT");
						break;
					case SMALLINT:
						subStrBuf.append(col.getColumnName() + " SMALLINT");
						break;
					case INTEGER:
						subStrBuf.append(col.getColumnName() + " INTEGER");
						break;
					case BIGINT:
						subStrBuf.append(col.getColumnName() + " BIGINT");
						break;
					case FLOAT:
						subStrBuf.append(col.getColumnName() + " FLOAT(" + col.getColumnSize() + ")");
						break;
					case REAL:
						subStrBuf.append(col.getColumnName() + " REAL");
						break;
					case DOUBLE:
						subStrBuf.append(col.getColumnName() + " DOUBLE PRECISION");
						break;
					case NUMERIC:
						if (col.getDecimalDigits() > 0) {
							subStrBuf.append(col.getColumnName() + " NUMERIC(" + col.getColumnSize() + ","
									+ col.getDecimalDigits() + ")");
						} else {
							subStrBuf.append(col.getColumnName() + " NUMERIC(" + col.getColumnSize() + ")");
						}
						break;
					case DECIMAL:
						subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ","
								+ col.getDecimalDigits() + ")");
						break;
					case CHAR:
						subStrBuf.append(col.getColumnName() + " CHAR(" + col.getColumnSize() + ")");
						break;
					case VARCHAR:
						subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						break;
					case LONGVARCHAR:
						subStrBuf.append(col.getColumnName() + " TEXT");
						break;
					case DATE:
						subStrBuf.append(col.getColumnName() + "  DATE");
						break;
					case TIME:
						subStrBuf.append(col.getColumnName() + "  TIME");
						break;
					case TIMESTAMP:
						subStrBuf.append(col.getColumnName() + "  DATETIME");
						break;
					case BINARY:
						// CHAR(n) FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  BINARY(" + col.getColumnSize() + ")");
						break;
					case VARBINARY:
						// VARCHAR(n) FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  VARBINARY(" + col.getColumnSize() + ") FOR BIT DATA");
						break;
					case LONGVARBINARY:
						// LONG VARCHAR FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  IMAGE");
						break;
					case BLOB:
						subStrBuf.append(col.getColumnName() + "  IMAGE");
						break;
					case CLOB:
						subStrBuf.append(col.getColumnName() + "  TEXT");
						break;
					case NCHAR:
						subStrBuf.append(col.getColumnName() + " NCHAR(" + col.getColumnSize() + ")");
						break;
					case NVARCHAR:
						subStrBuf.append(col.getColumnName() + " NVARCHAR(" + col.getColumnSize() + ")");
						break;
					case LONGNVARCHAR:
						subStrBuf.append(col.getColumnName() + "  TEXT");
						break;
					case BOOLEAN:
						subStrBuf.append(col.getColumnName() + "  TINYINT");
						break;
					default:
						subStrBuf.append(col.getColumnName() + "  VARCHAR(2000)");
						break;
					}
					if (col.getNullLable() == 0) {
						subStrBuf.append(" not null");
					}
					subStrBuf.append(",");
				});
				if (null != pkeys && !pkeys.isEmpty()) {
					subStrBuf.append(" PRIMARY KEY(");
					StringBuffer pkBuf = new StringBuffer();
					pkeys.forEach(pk -> {
						pkBuf.append(pk + ",");
					});
					subStrBuf.append(pkBuf.substring(0, pkBuf.lastIndexOf(",")));
					subStrBuf.append(")");
				} else {
					String str = subStrBuf.substring(0, subStrBuf.lastIndexOf(","));
					subStrBuf.delete(0, subStrBuf.length());
					subStrBuf.append(str);
				}
				strBuf.append(subStrBuf);
				strBuf.append(")");
				break;
			case INFORMIX:
				break;
			case POSTGRESQL:
				break;
			case PHOENIX:
				cols.forEach(col -> {
					JDBCType colType = JDBCType.valueOf(col.getSqlType());
					switch (colType) {
					case BIT:
						subStrBuf.append(col.getColumnName() + " TINYINT");
						break;
					case TINYINT:
						subStrBuf.append(col.getColumnName() + " TINYINT");
						break;
					case SMALLINT:
						subStrBuf.append(col.getColumnName() + " SMALLINT");
						break;
					case INTEGER:
						subStrBuf.append(col.getColumnName() + " INTEGER");
						break;
					case BIGINT:
						subStrBuf.append(col.getColumnName() + " BIGINT");
						break;
					case FLOAT:
						subStrBuf.append(col.getColumnName() + " FLOAT");
						break;
					case REAL:
						subStrBuf.append(col.getColumnName() + " FLOAT");
						break;
					case DOUBLE:
						subStrBuf.append(col.getColumnName() + " DOUBLE");
						break;
					case NUMERIC:
						if (col.getDecimalDigits() > 0) {
							subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ","
									+ col.getDecimalDigits() + ")");
						} else {
							subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ")");
						}
						break;
					case DECIMAL:
						subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ","
								+ col.getDecimalDigits() + ")");
						break;
					case CHAR:
						subStrBuf.append(col.getColumnName() + " CHAR(" + col.getColumnSize() + ")");
						break;
					case VARCHAR:
						subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						break;
					case LONGVARCHAR:
						subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						break;
					case DATE:
						subStrBuf.append(col.getColumnName() + "  DATE");
						break;
					case TIME:
						subStrBuf.append(col.getColumnName() + "  TIME");
						break;
					case TIMESTAMP:
						subStrBuf.append(col.getColumnName() + "  TIMESTAMP");
						break;
					case BINARY:
						// CHAR(n) FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  BINARY(" + col.getColumnSize() + ")");
						break;
					case VARBINARY:
						// VARCHAR(n) FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  VARBINARY(" + col.getColumnSize() + ")");
						break;
					case LONGVARBINARY:
						// LONG VARCHAR FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  VARBINARY(" + col.getColumnSize() + ")");
						break;
					case BLOB:
						subStrBuf.append(col.getColumnName() + "  VARBINARY(" + col.getColumnSize() + ")");
						break;
					case CLOB:
						subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						break;
					case NCHAR:
						subStrBuf.append(col.getColumnName() + " CHAR(" + col.getColumnSize() + ")");
						break;
					case NVARCHAR:
						subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						break;
					case LONGNVARCHAR:
						subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						break;
					case BOOLEAN:
						subStrBuf.append(col.getColumnName() + "  BOOLEAN");
						break;
					default:
						subStrBuf.append(col.getColumnName() + "  VARCHAR(2000)");
						break;
					}
					if (col.getNullLable() == 0) {
						subStrBuf.append(" not null");
					}
					subStrBuf.append(",");
				});
				if (null != pkeys && !pkeys.isEmpty()) {
					subStrBuf.append("CONSTRAINT ");
					subStrBuf.append("\"" + UUID.randomUUID().toString() + "\"");
					subStrBuf.append(" PRIMARY KEY(");
					StringBuffer pkBuf = new StringBuffer();
					pkeys.forEach(pk -> {
						pkBuf.append(pk + ",");
					});
					subStrBuf.append(pkBuf.substring(0, pkBuf.lastIndexOf(",")));
					subStrBuf.append(")");
				} else {
					String str = subStrBuf.substring(0, subStrBuf.lastIndexOf(","));
					subStrBuf.delete(0, subStrBuf.length());
					subStrBuf.append(str);
				}
				strBuf.append(subStrBuf);
				strBuf.append(")");
				break;
			case HIVE:
				cols.forEach(col -> {
					JDBCType colType = JDBCType.valueOf(col.getSqlType());
					switch (colType) {
					case BIT:
						subStrBuf.append(col.getColumnName() + " TINYINT");
						break;
					case TINYINT:
						subStrBuf.append(col.getColumnName() + " TINYINT");
						break;
					case SMALLINT:
						subStrBuf.append(col.getColumnName() + " SMALLINT");
						break;
					case INTEGER:
						subStrBuf.append(col.getColumnName() + " INTEGER");
						break;
					case BIGINT:
						subStrBuf.append(col.getColumnName() + " BIGINT");
						break;
					case FLOAT:
						subStrBuf.append(col.getColumnName() + " FLOAT");
						break;
					case REAL:
						subStrBuf.append(col.getColumnName() + " DOUBLE");
						break;
					case DOUBLE:
						subStrBuf.append(col.getColumnName() + " DOUBLE");
						break;
					case NUMERIC:
						if (col.getDecimalDigits() > 0) {
							subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ","
									+ col.getDecimalDigits() + ")");
						} else {
							subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ")");
						}
						break;
					case DECIMAL:
						subStrBuf.append(col.getColumnName() + " DECIMAL(" + col.getColumnSize() + ","
								+ col.getDecimalDigits() + ")");
						break;
					case CHAR:
						subStrBuf.append(col.getColumnName() + " CHAR(" + col.getColumnSize() + ")");
						break;
					case VARCHAR:
						subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						break;
					case LONGVARCHAR:
						subStrBuf.append(col.getColumnName() + " String");
						break;
					case DATE:
						subStrBuf.append(col.getColumnName() + "  DATE");
						break;
					case TIME:
						subStrBuf.append(col.getColumnName() + "  TIMESTAMP");
						break;
					case TIMESTAMP:
						subStrBuf.append(col.getColumnName() + "  TIMESTAMP");
						break;
					case BINARY:
						// CHAR(n) FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  BINARY(" + col.getColumnSize() + ")");
						break;
					case VARBINARY:
						// VARCHAR(n) FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  BINARY(" + col.getColumnSize() + ")");
						break;
					case LONGVARBINARY:
						// LONG VARCHAR FOR BIT DATA
						subStrBuf.append(col.getColumnName() + "  BINARY(" + col.getColumnSize() + ")");
						break;
					case BLOB:
						subStrBuf.append(col.getColumnName() + "  BINARY(" + col.getColumnSize() + ")");
						break;
					case CLOB:
						subStrBuf.append(col.getColumnName() + " String");
						break;
					case NCHAR:
						subStrBuf.append(col.getColumnName() + " CHAR(" + col.getColumnSize() + ")");
						break;
					case NVARCHAR:
						subStrBuf.append(col.getColumnName() + " VARCHAR(" + col.getColumnSize() + ")");
						break;
					case LONGNVARCHAR:
						subStrBuf.append(col.getColumnName() + " String");
						break;
					case BOOLEAN:
						subStrBuf.append(col.getColumnName() + "  BOOLEAN");
						break;
					default:
						subStrBuf.append(col.getColumnName() + "  VARCHAR(2000)");
						break;
					}
					if (col.getNullLable() == 0) {
						subStrBuf.append(" not null");
					}
					subStrBuf.append(",");
				});
				if (null != pkeys && !pkeys.isEmpty()) {
					subStrBuf.append(" PRIMARY KEY(");
					StringBuffer pkBuf = new StringBuffer();
					pkeys.forEach(pk -> {
						pkBuf.append(pk + ",");
					});
					subStrBuf.append(pkBuf.substring(0, pkBuf.lastIndexOf(",")));
					subStrBuf.append(")");
				} else {
					String str = subStrBuf.substring(0, subStrBuf.lastIndexOf(","));
					subStrBuf.delete(0, subStrBuf.length());
					subStrBuf.append(str);
				}
				strBuf.append(subStrBuf);
				strBuf.append(")");
				break;
			default:
				break;
			}

		}
		return strBuf.toString();
	}

	// @Override
	// public String generateDMLSQL(DataBaseType dataBaseType, List<Column> cols,
	// List<List<Object>> values,
	// String tableName, List<List<FieldValue>> outvalueList) {
	// StringBuffer strBuf = null;
	// if (dataBaseType == DataBaseType.PHOENIX) {
	// strBuf = new StringBuffer("UPSERT INTO ");
	// } else {
	// strBuf = new StringBuffer("INSERT INTO ");
	// }
	// strBuf.append(tableName);
	// strBuf.append("(");
	// String fields = "", value = "";
	//
	// for (Column col : cols) {
	// fields = fields + col.getColumnName() + ",";
	// value = value + "?,";
	// }
	// fields = fields.substring(0, fields.lastIndexOf(","));
	// value = value.substring(0, value.lastIndexOf(","));
	// strBuf.append(fields + ")");
	// strBuf.append(" VALUES(");
	// strBuf.append(value + ")");
	// values.forEach(row -> {
	// List<FieldValue> rowValue=new ArrayList<>();
	// for (int i = 0, j = row.size(); i < j; i++) {
	// FieldValue fieldValue = new FieldValue(i + 1, row.get(i), 0, 0);
	// rowValue.add(fieldValue);
	// }
	// outvalueList.add(rowValue);
	// });
	//
	// return strBuf.toString();
	// }
	//
	// @Override
	// public List<String> generateAlterDMLSQL(DataBaseType destType,
	// List<ForeignKey> fkeys) {
	// List<String> alertSqlList = new ArrayList<>();
	// switch (destType) {
	// case MYSQL:
	// if (null != fkeys && !fkeys.isEmpty())
	// fkeys.forEach(fkey -> {
	// StringBuffer buf = new StringBuffer("ALTER TABLE ");
	// buf.append(fkey.getForeignKeyTable());
	// buf.append(" CONSTRAINT `");
	// buf.append(fkey.getForeignKeyName());
	// buf.append("` FOREIGN KEY(`");
	// buf.append(fkey.getForeignKeyColumnName());
	// buf.append("`) REFERENCES `");
	// buf.append(fkey.getPkTableName());
	// buf.append("`(");
	// buf.append(fkey.getPkColumnName());
	// buf.append("`");
	// alertSqlList.add(buf.toString());
	// });
	//
	// break;
	// case MSSQL:
	// case ORACLE:
	// case DB2:
	// case SYSBASE:
	// case POSTGRESQL:
	// case INFORMIX:
	// if (null != fkeys && !fkeys.isEmpty())
	// fkeys.forEach(fkey -> {
	// StringBuffer buf = new StringBuffer("ALTER TABLE ");
	// buf.append(fkey.getForeignKeyTable());
	// buf.append(" CONSTRAINT ");
	// buf.append(fkey.getForeignKeyName());
	// buf.append(" FOREIGN KEY(");
	// buf.append(fkey.getForeignKeyColumnName());
	// buf.append(") REFERENCES ");
	// buf.append(fkey.getPkTableName());
	// buf.append("(");
	// buf.append(fkey.getPkColumnName());
	// buf.append("");
	// alertSqlList.add(buf.toString());
	// });
	// break;
	//
	// default:
	// break;
	// }
	//
	// return alertSqlList;
	// }

}
