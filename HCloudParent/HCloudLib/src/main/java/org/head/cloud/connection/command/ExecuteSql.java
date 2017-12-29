package org.head.cloud.connection.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.exception.NullArgumentException;
import org.head.cloud.connection.ICommand;
import org.head.cloud.connection.IConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.head.cloud.connection.factory.DataSourceFactory;
import org.head.cloud.connection.handler.FiedValueTypeHandler;
import org.head.cloud.db.DataBaseType;
import org.head.cloud.util.Column;
import org.head.cloud.util.FieldValue;
import org.head.cloud.util.JAVAType;

@Named
public class ExecuteSql implements ICommand {
	static Logger LOGGER = LoggerFactory.getLogger(ExecuteSql.class);

	@Override
	public Map<String, Object> executeQuery(String connName, String selectsql) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		IConnection iCon = DataSourceFactory.getConnection(connName);
		Connection conn = iCon.getConn();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(selectsql);
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			List<String> cols = new ArrayList<>();
			List<Integer> colsType = new ArrayList<>();
			int colsNum = resultSetMetaData.getColumnCount();
			for (int i = 0; i < colsNum; i++) {
				String colLable = resultSetMetaData.getColumnLabel(i + 1);
				if(StringUtils.isEmpty(colLable)) {
				cols.add(resultSetMetaData.getColumnName(i + 1));
				}else {
					cols.add(colLable);
				}
				colsType.add(resultSetMetaData.getColumnType(i + 1));
			}
			result.put("cols", cols);
			List<List<Object>> values = new ArrayList<>();
			List<Object> value = null;
			// rs.getRow()
			while (rs.next()) {
				value = new ArrayList<>();
				for (int i = 0, j = cols.size(); i < j; i++) {
					JDBCType coltype = JDBCType.valueOf(colsType.get(i));
					switch (coltype) {
					case BINARY:
					case VARBINARY:
					case LONGVARBINARY:
						InputStream in = rs.getBinaryStream(cols.get(i));
						if (null != in) {
							byte[] bytearray = new byte[in.available()];
							in.read(bytearray);
							value.add("(BLOB)" + bytearray.length + "个字节");
						} else {
							value.add(null);
						}
						break;
					case BLOB:
						Blob b = rs.getBlob(cols.get(i));
						if (null != b) {
							value.add("(BLOB)" + b.length() + "byte个字节");
						} else {
							value.add(null);
						}
						break;
					case ROWID:
						RowId rowId = rs.getRowId(cols.get(i));
						byte[] rowbuf = rowId.getBytes();
						value.add(new String(rowbuf, "utf-8"));
						break;
					case CLOB:
						Clob c = rs.getClob(cols.get(i));
						if (null != c) {
							value.add("(CLOB)" + c.length() + "个字节");
						} else {
							value.add(null);
						}
						break;
					case NCLOB:
						NClob nc = rs.getNClob(cols.get(i));
						if (null != nc) {
							value.add("(NCLOB)" + nc.length() + "个字节");
						} else {
							value.add(null);
						}
						break;
					case DATE:
						Date date = rs.getDate(cols.get(i));
						if (null != date) {
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							value.add(df.format(date));
						} else {
							value.add(null);
						}
						break;
					case TIME:
						Time time = rs.getTime(cols.get(i));
						if (null != time) {
							SimpleDateFormat f_sqlTime = new SimpleDateFormat("hh:mm:ss");
							value.add(f_sqlTime.format(time));
						} else {
							value.add(null);
						}
						break;
					case TIMESTAMP:
						Timestamp timestamp = rs.getTimestamp(cols.get(i));
						if (null != timestamp) {
							SimpleDateFormat f_sqlTime1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							value.add(f_sqlTime1.format(timestamp));
						} else {
							value.add(null);
						}
						break;
					case BIT:
						value.add(rs.getBoolean(cols.get(i)));
						break;
					default:
						value.add(rs.getObject(cols.get(i)));
						break;
					}

				}
				values.add(value);
			}
			result.put("values", values);
			iCon.release(statement, rs, null);
			return result;
		} catch (SQLException | IOException e) {
			LOGGER.error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			DataSourceFactory.recoveryConnection(connName, iCon);
		}
	}

	@Override
	public int executeDDL(String connName, String ddlSql) throws SQLException {
		// TODO Auto-generated method stub
		IConnection iCon = DataSourceFactory.getConnection(connName);
		Connection conn = iCon.getConn();
		try {
			conn.setAutoCommit(false);
			Statement statement = conn.createStatement();
			int r = statement.executeUpdate(ddlSql);
			iCon.release(statement, null, null);
			conn.commit();
			return r;
		} catch (SQLException e) {
			conn.rollback();
			LOGGER.error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			DataSourceFactory.recoveryConnection(connName, iCon);
		}
	}

	@Override
	public int executeUpdate(String connName, String updateSql) throws SQLException {
		// TODO Auto-generated method stub
		IConnection iCon = DataSourceFactory.getConnection(connName);
		Connection conn = iCon.getConn();
		try {
			conn.setAutoCommit(false);
			Statement statement = conn.createStatement();
			int r = statement.executeUpdate(updateSql);
			iCon.release(statement, null, null);
			conn.commit();
			return r;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				LOGGER.error(e1.getMessage());
				throw new SQLException(e1.getMessage());
			}
			LOGGER.error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			DataSourceFactory.recoveryConnection(connName, iCon);
		}
	}

	@Override
	public List<List<Object>> executeQuery(String connName, String selectsql, List<Column> cols) throws SQLException {
		// TODO Auto-generated method stub
		if (cols == null) {
			throw new NullArgumentException();
		}
		IConnection iCon = DataSourceFactory.getConnection(connName);
		Connection conn = iCon.getConn();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(selectsql);
			List<List<Object>> values = new ArrayList<>();
			List<Object> value = null;
			while (rs.next()) {
				value = new ArrayList<>();
				for (Column col : cols) {
					value.add(rs.getObject(col.getColumnName()));
				}
				values.add(value);
			}
			iCon.release(statement, rs, null);
			return values;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			DataSourceFactory.recoveryConnection(connName, iCon);
		}

	}

	public List<List<FieldValue>> executeQuery(String connName, DataBaseType destDbType, String selectsql,
			List<Column> cols) throws SQLException {
		// TODO Auto-generated method stub
		if (cols == null) {
			throw new NullArgumentException();
		}
		IConnection iCon = DataSourceFactory.getConnection(connName);
		Connection conn = iCon.getConn();
		// selectsql.contains("ttb");
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(selectsql);
			List<List<FieldValue>> values = new ArrayList<>();
			List<FieldValue> value = null;
			while (rs.next()) {
				value = new ArrayList<>();
				for (int i = 0, j = cols.size(); i < j; i++) {
					FieldValue fieldValue = new FieldValue();
					Column col = cols.get(i);
					fieldValue.setDestDbType(destDbType);
					fieldValue.setIndex(i + 1);
					JDBCType sourejdbcType = JDBCType.valueOf(col.getSqlType());
					switch (sourejdbcType) {
					case CHAR:
					case VARCHAR:
					case LONGVARCHAR:
					case LONGNVARCHAR:
					case NCHAR:
					case NVARCHAR:
						fieldValue.setValue(rs.getString(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.STRING);
						break;
					case INTEGER:
						fieldValue.setValue(rs.getInt(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.INT);
						break;
					case NUMERIC:
					case DECIMAL:
						fieldValue.setValue(rs.getBigDecimal(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.BIGDECIMAL);
						break;
					case REAL:
						fieldValue.setValue(rs.getFloat(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.FLOAT);
						break;
					case FLOAT:
						fieldValue.setValue(rs.getFloat(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.FLOAT);
						break;
					case DOUBLE:
						fieldValue.setValue(rs.getDouble(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.DOUBLE);
						break;
					case BIGINT:
						fieldValue.setValue(rs.getLong(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.LONG);
						break;
					case BIT:
						fieldValue.setValue(rs.getBoolean(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.BOOLEAN);
						break;
					case TINYINT:
						fieldValue.setValue(rs.getByte(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.BOOLEAN);
						break;
					case SMALLINT:
						fieldValue.setValue(rs.getShort(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.BOOLEAN);
						break;
					case BINARY:
					case VARBINARY:
					case LONGVARBINARY:
						InputStream in = rs.getBinaryStream(col.getColumnName());
						if (null != in) {
							byte[] buf = new byte[in.available()];
							in.read(buf);
							fieldValue.setValue(buf);
							fieldValue.setJavaType(JAVAType.BYTEARRAY);
							in.close();
						} else {
							fieldValue.setValue(null);
							fieldValue.setJavaType(JAVAType.OBJECT);
						}
						break;
					case CLOB:
					case NCLOB:
						Clob c = rs.getClob(col.getColumnName());
						if (null != c) {
							Reader r = c.getCharacterStream();
							char[] cs = new char[(int) c.length()];
							r.read(cs);
							r.close();
							fieldValue.setValue(cs);
							fieldValue.setJavaType(JAVAType.CHARARRAY);
						} else {
							fieldValue.setValue(null);
							fieldValue.setJavaType(JAVAType.OBJECT);
						}
						break;
					case BLOB:
						Blob b = rs.getBlob(col.getColumnName());
						if (null != b) {
							InputStream ins = b.getBinaryStream();
							byte[] buff = new byte[(int) b.length()];
							ins.read(buff);
							fieldValue.setValue(buff);
							fieldValue.setJavaType(JAVAType.BYTEARRAY);
							ins.close();
						} else {
							fieldValue.setValue(null);
							fieldValue.setJavaType(JAVAType.OBJECT);
						}
						break;
					case DATE:
						fieldValue.setValue(rs.getDate(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.DATE);
						break;
					case TIME:
						fieldValue.setValue(rs.getTime(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.DATE);
						break;
					case TIMESTAMP:
						fieldValue.setValue(rs.getTimestamp(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.TIMESTAMP);
						break;
					default:
						fieldValue.setValue(rs.getObject(col.getColumnName()));
						fieldValue.setJavaType(JAVAType.OBJECT);
						break;
					}
					value.add(fieldValue);
				}
				values.add(value);
			}
			iCon.release(statement, rs, null);
			return values;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			DataSourceFactory.recoveryConnection(connName, iCon);
		}

	}

	@Override
	public int executeUpdate(String connName, String Sql, List<FieldValue> valueList) throws SQLException {
		// TODO Auto-generated method stub
		IConnection iCon = DataSourceFactory.getConnection(connName);
		Connection conn = iCon.getConn();
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(Sql);
			FiedValueTypeHandler tHandler = new FiedValueTypeHandler();
			for (int i = 0, j = valueList.size(); i < j; i++) {
				FieldValue fv = valueList.get(i);
				tHandler.setParament(ps, fv);
			}
			int rst = ps.executeUpdate();
			iCon.release(ps, null, null);
			conn.commit();
			return rst;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				LOGGER.error(e1.getMessage());
				throw new SQLException(e1.getMessage());
			}
			LOGGER.error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			DataSourceFactory.recoveryConnection(connName, iCon);
		}
	}

	public int[] executeBatchUpdate(String connName, String sql, List<List<FieldValue>> rows) throws SQLException {
		IConnection iCon = DataSourceFactory.getConnection(connName);
		Connection conn = iCon.getConn();
		FiedValueTypeHandler tHandler = new FiedValueTypeHandler();
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);
			for (int i = 0; i < rows.size(); i++) {
				List<FieldValue> row = rows.get(i);
				for (int k = 0, j = row.size(); k < j; k++) {
					FieldValue fv = row.get(k);
					tHandler.setParament(ps, fv);
				}
				ps.addBatch();
			}
			int[] flag = ps.executeBatch();
			iCon.release(ps, null, null);
			conn.commit();
			return flag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				LOGGER.error(e1.getMessage());
				throw new SQLException(e.getMessage());
			}
			LOGGER.error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			DataSourceFactory.recoveryConnection(connName, iCon);
		}

	}

	@Override
	public int executeUpdateBatch(List<String> sqls, String connName) throws SQLException {
		// TODO Auto-generated method stub
		IConnection iCon = DataSourceFactory.getConnection(connName);
		Connection conn = iCon.getConn();
		try {
			conn.setAutoCommit(false);
			Statement statement = conn.createStatement();
			sqls.forEach(sql -> {
				try {
					statement.addBatch(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					LOGGER.error(e.getMessage());
				}
			});
			int[] rst = statement.executeBatch();
			iCon.release(statement, null, null);
			conn.commit();
			return rst.length;
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				LOGGER.error(e1.getMessage());
				throw new SQLException(e1.getMessage());
			}
			LOGGER.error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			DataSourceFactory.recoveryConnection(connName, iCon);
		}
	}

}
