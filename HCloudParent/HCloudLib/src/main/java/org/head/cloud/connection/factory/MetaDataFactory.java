package org.head.cloud.connection.factory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.head.cloud.connection.IConnection;
import org.head.cloud.connection.IDataMetaData;
import org.head.cloud.db.DataBaseType;
import org.head.cloud.util.Column;
import org.head.cloud.util.ForeignKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaDataFactory {
	static Logger LOGGER = LoggerFactory.getLogger(MetaDataFactory.class);

	public static IDataMetaData getDataMeta(String connName) {
		return new IDataMetaData() {
			// 元数据
			private DatabaseMetaData dataMeta;
			// 自
			private IConnection iConn;

			public DatabaseMetaData getDataMeta() {
				return dataMeta;
			}

			public Connection getConn() {
				return iConn.getConn();
			}

			public IDataMetaData init() {
				iConn = DataSourceFactory.getConnection(connName);
				try {
					dataMeta = iConn.getConn().getMetaData();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					LOGGER.error(e.getMessage());
				}
				return this;
			}

			@Override
			public <T> List<T> getDataBases() {
				// TODO Auto-generated method stub
				List<String> dbs = new ArrayList<String>();
				DataBaseType dbType = DataBaseType.getDataBaseTypeByIndex(iConn.getDbtype());
				try {
					if (dbType == DataBaseType.MSSQL) {
						dbs.add(this.getConn().getCatalog());
					} else if (dbType == DataBaseType.ORACLE) {
						dbs.add(this.dataMeta.getUserName());
					} else if (dbType == DataBaseType.MYSQL) {// oracle
						dbs.add(this.getConn().getCatalog());
					} else if (dbType == DataBaseType.DB2) {
						dbs.add(this.getConn().getCatalog());
					} else if (dbType == DataBaseType.POSTGRESQL) {
						dbs.add(this.getConn().getSchema());
					} else if (dbType == DataBaseType.INFORMIX) {
						dbs.add(this.getConn().getCatalog());
					} else if (dbType == DataBaseType.SYSBASE) {
						dbs.add(this.getConn().getCatalog());
					} else {
						// phoenix
						ResultSet schameRs = this.dataMeta.getSchemas();
						while (schameRs.next()) {
							dbs.add(schameRs.getString(1));
						}
						this.release(schameRs, null);
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
				return (List<T>) dbs;
			}

			@Override
			public Map<String, List<String>> getTablesMeta() {
				List<String> tables = null;
				List<String> dbs = this.getDataBases();
				Map<String, List<String>> result = new HashMap<String, List<String>>();
				if (!dbs.isEmpty()) {
					try {
						DataBaseType dbType = DataBaseType.getDataBaseTypeByIndex(iConn.getDbtype());
						switch (dbType) {
						case MSSQL:
						case MYSQL:
							for (String str : dbs) {
								ResultSet rs = this.dataMeta.getTables(str, null, null, new String[] { "TABLE" });
								List<String> list = new ArrayList<String>();
								while (rs.next()) {
									list.add(rs.getString("TABLE_NAME"));
								}
								this.release(rs, null);
								result.put(str, list);
							}
							break;
						case DB2:
						case SYSBASE:
						case INFORMIX:
							for (String str : dbs) {
								ResultSet rs = this.dataMeta.getTables(str, null, null, new String[] { "TABLE" });
								List<String> list = new ArrayList<String>();
								while (rs.next()) {
									list.add(rs.getString("TABLE_NAME"));
								}
								this.release(rs, null);
								result.put(str, list);
							}
							break;
						case ORACLE:
						case POSTGRESQL:
							for (String str : dbs) {
								ResultSet rs = this.dataMeta.getTables(null, str, null, new String[] { "TABLE" });
								List<String> list = new ArrayList<String>();
								while (rs.next()) {
									list.add(rs.getString("TABLE_NAME"));
								}
								this.release(rs, null);
								result.put(str, list);
							}
							break;
						default:
							for (String str : dbs) {
								ResultSet rs = this.dataMeta.getTables(null, str, null, new String[] { "TABLE" });
								List<String> list = new ArrayList<String>();
								while (rs.next()) {
									list.add(rs.getString("TABLE_NAME"));
								}
								this.release(rs, null);
								result.put(str, list);
							}
							break;
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage());
					}
				}
				return result;
			}

			@Override
			public <T> List<T> getColumnMetaData(String db, String tableName) {
				// TODO Auto-generated method stub
				List<Column> list = new ArrayList<Column>();
				try {
					DataBaseType dbType = DataBaseType.getDataBaseTypeByIndex(iConn.getDbtype());
					switch (dbType) {
					case MSSQL:
					case MYSQL:
						ResultSet rs = this.dataMeta.getColumns(db, "%", tableName, "%");
						while (rs.next()) {
							String cname = rs.getString("COLUMN_NAME");
							Column c = new Column(cname, rs.getInt("DATA_TYPE"), rs.getString("TYPE_NAME"),
									rs.getInt("COLUMN_SIZE"), rs.getInt("NULLABLE"), 0, 0, rs.getInt("DECIMAL_DIGITS"));
							c.setDefaultValue(rs.getString("COLUMN_DEF"));
							list.add(c);
						}
						this.release(rs, null);
						break;
					case ORACLE:
					case POSTGRESQL:
						ResultSet rs1 = this.dataMeta.getColumns(null, db, tableName, "%");
						while (rs1.next()) {
							String cname = rs1.getString("COLUMN_NAME");
							Column c = new Column(cname, rs1.getInt("DATA_TYPE"), rs1.getString("TYPE_NAME"),
									rs1.getInt("COLUMN_SIZE"), rs1.getInt("NULLABLE"), 0, 0,
									rs1.getInt("DECIMAL_DIGITS"));
							c.setDefaultValue(rs1.getString("COLUMN_DEF"));
							list.add(c);
						}
						this.release(rs1, null);
						break;
					case DB2:
					case SYSBASE:
					case INFORMIX:
						ResultSet rs2 = this.dataMeta.getColumns(db, "%", tableName, "%");
						while (rs2.next()) {
							String cname = rs2.getString("COLUMN_NAME");
							Column c = new Column(cname, rs2.getInt("DATA_TYPE"), rs2.getString("TYPE_NAME"),
									rs2.getInt("COLUMN_SIZE"), rs2.getInt("NULLABLE"), 0, 0,
									rs2.getInt("DECIMAL_DIGITS"));
							c.setDefaultValue(rs2.getString("COLUMN_DEF"));
							list.add(c);
						}
						this.release(rs2, null);
						break;
					case PHOENIX:
						ResultSet rs3 = this.dataMeta.getColumns(null, db, tableName, "%");
						while (rs3.next()) {
							String cname = rs3.getString("COLUMN_NAME");
							Column c = new Column(cname, rs3.getInt("DATA_TYPE"), rs3.getString("TYPE_NAME"),
									rs3.getInt("COLUMN_SIZE"), rs3.getInt("NULLABLE"), 0, 0,
									rs3.getInt("DECIMAL_DIGITS"));
							list.add(c);
						}
						this.release(rs3, null);
						break;
					default:
						ResultSet rs4 = this.dataMeta.getColumns(null, db, tableName, "%");
						while (rs4.next()) {
							String cname = rs4.getString("COLUMN_NAME");
							Column c = new Column(cname, rs4.getInt("DATA_TYPE"), rs4.getString("TYPE_NAME"),
									rs4.getInt("COLUMN_SIZE"), rs4.getInt("NULLABLE"), 0, 0,
									rs4.getInt("DECIMAL_DIGITS"));
							c.setDefaultValue(rs4.getString("COLUMN_DEF"));
							list.add(c);
						}
						this.release(rs4, null);
						break;
					}

				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				} finally {
					DataSourceFactory.recoveryConnection(connName, iConn);
				}
				return (List<T>) list;

			}

			@Override
			public <T> List<T> getPrimaryKeyMetaData(String tableName, String dbName) {
				List<String> pkeys = new ArrayList<String>();
				DataBaseType dbType = DataBaseType.getDataBaseTypeByIndex(iConn.getDbtype());
				try {
					ResultSet rs = null;
					switch (dbType) {
					case MSSQL:
					case MYSQL:
						rs = this.dataMeta.getPrimaryKeys(dbName, null, tableName);
						break;
					case ORACLE:
					case POSTGRESQL:
					case PHOENIX:
						rs = this.dataMeta.getPrimaryKeys(null, dbName, tableName);
						break;
					case DB2:
					case SYSBASE:
					case INFORMIX:
						rs = this.dataMeta.getPrimaryKeys(null, dbName, tableName);
						break;
					default:
						rs = this.dataMeta.getPrimaryKeys(null, dbName, tableName);
						break;
					}
					while (rs.next()) {
						pkeys.add(rs.getString("COLUMN_NAME"));
					}
					this.release(rs, null);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					LOGGER.error(e.getMessage());
				}
				return (List<T>) pkeys;
			}

			@Override
			public void release(ResultSet rs, Statement stmt) {
				// TODO Auto-generated method stub
				this.iConn.release(stmt, rs, null);
			}

			@Override
			public <T> List<T> getExportKeys(String tableName, String dbName) {
				// TODO Auto-generated method stub
				List<ForeignKey> fkeys = new ArrayList<>();
				DataBaseType dbType = DataBaseType.getDataBaseTypeByIndex(iConn.getDbtype());
				try {
					ResultSet rs = null;
					switch (dbType) {
					case MSSQL:
					case MYSQL:
						rs = this.dataMeta.getExportedKeys(dbName, null, tableName);
						break;
					case ORACLE:
					case POSTGRESQL:
						rs = this.dataMeta.getExportedKeys(null, dbName, tableName);
						break;
					case DB2:
					case SYSBASE:
					case INFORMIX:
						rs = this.dataMeta.getExportedKeys(dbName, null, tableName);
						break;
					default:
						rs = this.dataMeta.getExportedKeys(null, dbName, tableName);
						break;
					}
					while (rs.next()) {
						ForeignKey fk = new ForeignKey();
						fk.setPkColumnName(rs.getString("PKCOLUMN_NAME"));
						fk.setPkTableName(rs.getString("PKTABLE_NAME"));
						fk.setForeignKeyColumnName(rs.getString("FKCOLUMN_NAME"));
						fk.setForeignKeyTable(rs.getString("FKTABLE_NAME"));
						fk.setForeignKeyName(rs.getString("FK_NAME"));
						fkeys.add(fk);
					}
					this.release(rs, null);
				} catch (Exception e) {
					// TODO: handle exception
					LOGGER.error(e.getMessage());
				}
				return (List<T>) fkeys;
			}

			@Override
			public void close() {
				DataSourceFactory.recoveryConnection(connName, iConn);

			}

			@Override
			public List<String> getTablesMeta(String db) {
				// TODO Auto-generated method stub
				List<String> tables = new ArrayList<>();
				DataBaseType dbType = DataBaseType.getDataBaseTypeByIndex(iConn.getDbtype());
				try {
					switch (dbType) {
					case MSSQL:
					case MYSQL:
						ResultSet rs = this.dataMeta.getTables(db, null, null, new String[] { "TABLE" });
						while (rs.next()) {
							tables.add(rs.getString("TABLE_NAME"));
						}
						this.release(rs, null);
						break;
					case ORACLE:
					case POSTGRESQL:
						ResultSet rs2 = this.dataMeta.getTables(null, db, null, new String[] { "TABLE" });
						while (rs2.next()) {
							tables.add(rs2.getString("TABLE_NAME"));
						}
						this.release(rs2, null);
						break;
					case DB2:
					case SYSBASE:
					case INFORMIX:
						ResultSet rs3 = this.dataMeta.getTables(null, db, null, new String[] { "TABLE" });
						while (rs3.next()) {
							tables.add(rs3.getString("TABLE_NAME"));
						}
						this.release(rs3, null);
						break;
					default:
						ResultSet rs1 = this.dataMeta.getTables(null, db, null, new String[] { "TABLE" });
						while (rs1.next()) {
							tables.add(rs1.getString("TABLE_NAME"));
						}
						this.release(rs1, null);
						break;
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
				return tables;
			}

			/**
			 * @author head72
			 * 
			 * @description 获取表外键
			 * 
			 * @param tableName
			 *            //表名
			 * 
			 * @param dbNam
			 *            //数据名
			 * 
			 */
			@Override
			public <T> List<T> getImportKeys(String tableName, String dbName) {
				// TODO Auto-generated method stub
				List<ForeignKey> fkeys = new ArrayList<>();
				DataBaseType dbType = DataBaseType.getDataBaseTypeByIndex(iConn.getDbtype());
				try {
					ResultSet rs = null;
					switch (dbType) {
					case MSSQL:
					case MYSQL:
						rs = this.dataMeta.getImportedKeys(dbName, null, tableName);
						break;
					case ORACLE:
					case POSTGRESQL:
						rs = this.dataMeta.getImportedKeys(null, dbName, tableName);
						break;
					case DB2:
					case SYSBASE:
					case INFORMIX:
						rs = this.dataMeta.getImportedKeys(null, dbName, tableName);
						break;
					default:
						rs = this.dataMeta.getImportedKeys(null, dbName, tableName);
						break;
					}
					while (rs.next()) {
						ForeignKey fk = new ForeignKey();
						fk.setPkColumnName(rs.getString("PKCOLUMN_NAME"));
						fk.setPkTableName(rs.getString("PKTABLE_NAME"));
						fk.setForeignKeyColumnName(rs.getString("FKCOLUMN_NAME"));
						fk.setForeignKeyTable(rs.getString("FKTABLE_NAME"));
						fk.setForeignKeyName(rs.getString("FK_NAME"));
						fkeys.add(fk);
					}
					this.release(rs, null);
				} catch (Exception e) {
					// TODO: handle exception
					LOGGER.error(e.getMessage());
				}
				return (List<T>) fkeys;
			}
		}.init();
	}

	public static void main(String[] args) {
		IDataMetaData metaData = MetaDataFactory.getDataMeta("sqlserver");
		List<ForeignKey> l1 = metaData.getExportKeys("T_GMP_SYS_APP_RESOURCE", "HeadGmp");
		List<ForeignKey> l2 = metaData.getImportKeys("T_GMP_SYS_APP_RESOURCE", "HeadGmp");
		l1.forEach(l -> System.out.println(l.getForeignKeyColumnName() + "___" + l.getPkColumnName()));

		System.out.println("\r\n");
		l2.forEach(l -> System.out.println(l.getForeignKeyColumnName() + "___" + l.getPkColumnName()));
	}

}
