package org.head.cloud.rest;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.aopalliance.reflect.Class;
import org.apache.commons.lang.StringUtils;
import org.head.cloud.connection.IDataMetaData;
import org.head.cloud.connection.IGenerateSql;
import org.head.cloud.connection.command.ExecuteSql;
import org.head.cloud.connection.factory.GenerateSqlFactory;
import org.head.cloud.connection.factory.MetaDataFactory;
import org.head.cloud.db.DataBaseType;
import org.head.cloud.service.CommService;
import org.head.cloud.util.AlterSql;
import org.head.cloud.util.Column;
import org.head.cloud.util.ConnectionParams;
import org.head.cloud.util.FieldValue;
import org.head.cloud.util.ForeignKey;
import org.head.cloud.util.JAVAType;
import org.head.cloud.util.ParamFile;
import org.head.cloud.util.Table;
import org.head.cloud.util.TableSql;
import org.head.cloud.websocket.HCloudWebSocketClient;
import org.head.cloud.websocket.HCloudWebSocketClient.CustomMessageHandler;

import com.alibaba.fastjson.JSON;

@Path("/hcloud/impandexp/")
public class ImportAndExportService extends CommService {

	ExecuteSql executeSql = new ExecuteSql();

	private HCloudWebSocketClient client;

	private String sessionid;

	private void initWebSocket() {
		int port = this.request.getLocalPort();
		String ctx = this.request.getContextPath();
		String uri = "ws://localhost:" + port + "/" + ctx + "/hcloud/push";
		try {
			client = new HCloudWebSocketClient(new URI(uri + "/10101"));
			client.addMessageHandler(new CustomMessageHandler() {

				@Override
				public void handler(String msg) {
					// TODO Auto-generated method stub
					System.out.println(msg);
				}
			});
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Path("import")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=utf-8")
	public String importData(@FormParam("sconnName") String sconnName, @FormParam("sDb") String sDb,
			@FormParam("dconnName") String dconnName, @FormParam("destDb") String destDb,
			@FormParam("isCreateTb") Integer isCreateTb, @FormParam("dtbs") String dtbs,
			@FormParam("ndtbs") String ndtbs) throws IOException {
		initWebSocket();
		sessionid = request.getSession(false).getId();
		Map<String, String> result = new HashMap<String, String>();
		try {
			String[] sourceTableArray = dtbs.split(",");
			if (isCreateTb == 1) {
				List<AlterSql> alterSql = new ArrayList<>();
				List<TableSql> tableSqlList = createTableSql(sourceTableArray, sDb, destDb, sconnName, dconnName,
						alterSql);
				exeCreateTableSql(tableSqlList);
				if (StringUtils.isNotEmpty(ndtbs)) {
					exeTableInsertSql(tableSqlList, ndtbs.split(","));
				} else {
					exeTableInsertSql(tableSqlList, null);
				}
				ConnectionParams cp = ParamFile.getValueByKey(dconnName);
				DataBaseType dtype = DataBaseType.getDataBaseTypeByIndex(Integer.valueOf(cp.getDbType()));
				if (!dtype.equals(DataBaseType.PHOENIX) && alterSql.size() > 0) {
					exeDbAlterSql(alterSql.get(0));
				}
			} else {
				String[] noinsertTableData = ndtbs.split(",");
				if (noinsertTableData != null && noinsertTableData.length > 0) {
					for (int i = 0; i < sourceTableArray.length; i++) {
						for (int j = 0; j < noinsertTableData.length; j++) {
							if (sourceTableArray[i].equals(noinsertTableData[j])) {
								sourceTableArray[i] = null;
							}
						}
					}
				}
				onlyInserData(dconnName, sconnName, sDb, destDb, sourceTableArray);
			}
			result.put("status", "1");
			result.put("info", "数据库迁移成功！");
		} catch (Exception ex) {
			ex.printStackTrace();
			result.put("status", "0");
			result.put("info", ex.getMessage());
		}
		client.getSession().close();
		return JSON.toJSONString(result);

	}

	@Path("script")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=utf-8")
	public String exportScript(@FormParam("sourceConnName") String sourceConnName,
			@FormParam("sourcedb") String sourcedb, @FormParam("destDb") String destDb,
			@FormParam("selTbs") String selTbs) throws IOException {
		initWebSocket();
		sessionid = request.getSession(false).getId();
		String[] sourceTableArray = selTbs.split(",");
		Map<String, String> result = new HashMap<String, String>();
		try {
			int dbType = Integer.valueOf(destDb);
			DataBaseType destDbType = DataBaseType.getDataBaseTypeByIndex(dbType);
			ConnectionParams cp = ParamFile.getValueByKey(sourceConnName);
			DataBaseType sourceDataType = DataBaseType.getDataBaseTypeByIndex(Integer.valueOf(cp.getDbType()));
			List<Table> tables = getTableMetaData(sourcedb, sourceConnName, sourceDataType, destDbType, null,
					Arrays.asList(sourceTableArray), null);
			List<TableSql> tbSqls = getTableSql(tables, sourceDataType);
			AlterSql alterSql = getAlterSql(tables, sourceDataType, null);
			String fileName = createSqlScriptFile(tbSqls, alterSql, destDbType);
			result.put("status", "1");
			result.put("info", fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.put("status", "0");
			result.put("info", "脚本生成失败！");
		}
		//client.getSession().close();
		return JSON.toJSONString(result);
	}

	@GET
	@Path("downscript")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downScript(@QueryParam("fileName") String fileName) {
		String filePath = System.getProperty("java.io.tmpdir") + "/" + fileName;
		File file = new File(filePath);
		try {
			FileInputStream in = new FileInputStream(file);
			return Response.ok(in).header("Content-disposition", "attachment;filename=" + file.getName())
					.header("Cache-Control", "no-cache").build();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String createSqlScriptFile(List<TableSql> sqls, AlterSql alterSql, DataBaseType destDbType)
			throws Exception {
		String fileName = System.currentTimeMillis() + ".sql";
		String path = System.getProperty("java.io.tmpdir"), filePath = path + "/" + fileName;
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStream out = new FileOutputStream(file);
		BufferedOutputStream bufOut = new BufferedOutputStream(out);
		for (TableSql sql : sqls) {
			String cSql = sql.getCreateSql() + "\r\n", tempISql = sql.getInsertSql();
			bufOut.write(cSql.getBytes("utf-8"));
			client.sendMessage(
					sessionid + ";" + cSql+"写文件成功！");
			Thread.sleep(1000);
			List<List<FieldValue>> dataRows = sql.getValueList();
			for (int i = 0, j = dataRows.size(); i < j; i++) {
				String iSql = tempISql.substring(0, tempISql.indexOf("?"));
				List<FieldValue> row = dataRows.get(i);
				for (FieldValue cell : row) {
					JAVAType javaType = cell.getJavaType();
					Object value = cell.getValue();
					switch (javaType) {
					case STRING:
						iSql = iSql + "'" + value + "',";
						break;
					case BIGDECIMAL:
					case LONG:
					case INT:
					case BYTE:
					case SHORT:
					case FLOAT:
					case DOUBLE:
						iSql = iSql + value + ",";
						break;
					case BOOLEAN:
						boolean f = (boolean) value;
						int data = f ? 1 : 0;
						iSql = iSql + data + ",";
						break;
					case DATE:
						if (destDbType.equals(DataBaseType.ORACLE)) {
							iSql = iSql + "to_date('" + value + "','YYYY-MM-DD HH24:MI:SS'),";
						} else {
							iSql = iSql + "'" + value + "',";
						}
						break;
					case TIME:
						if (destDbType.equals(DataBaseType.ORACLE)) {
							iSql = iSql + "to_date('" + value + "','YYYY-MM-DD HH24:MI:SS'),";
						} else {
							iSql = iSql + "'" + value + "',";
						}
						break;
					case TIMESTAMP:
						if (destDbType.equals(DataBaseType.ORACLE)) {
							iSql = iSql + "to_date('" + value + "','YYYY-MM-DD HH24:MI:SS'),";
						} else {
							iSql = iSql + "'" + value + "',";
						}
						break;
					case BYTEARRAY:
						BigInteger bigInteger = new BigInteger((byte[]) value);
						String hexStr = bigInteger.toString(16);
						if (destDbType.equals(DataBaseType.ORACLE)) {
							iSql = iSql + "to_blob('" + hexStr + "'),";
						} else {
							iSql = iSql + "0x" + hexStr + ",";
						}
						break;
					case CHARARRAY:
						String str = new String((char[]) value);
						if (destDbType.equals(DataBaseType.ORACLE)) {
							iSql = iSql + "to_clob('" + str + "'),";
						} else {
							iSql = iSql + "'" + str + "',";
						}
						break;
					default:
						iSql = iSql + " null,";
						break;
					}
				}
				iSql = iSql.substring(0, iSql.lastIndexOf(",")) + ")\r\n";
				bufOut.write(iSql.getBytes("utf-8"));
				client.sendMessage(
						sessionid + ";" + iSql+"写文件成功！");
				Thread.sleep(1000);
			}
		}

		alterSql.getLists().forEach(sql -> {
			sql = sql + "\r\n";
			try {
				bufOut.write(sql.getBytes("utf-8"));
				client.sendMessage(
						sessionid + ";" + sql+"写文件成功！");
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		bufOut.flush();
		bufOut.close();

		return fileName;

	}

	private List<TableSql> createTableSql(String[] sourceTabls, String sourceDb, String destDb, String sourceConnName,
			String destConnName, List<AlterSql> alterSql) throws SQLException {
		List<String> tablesList = Arrays.asList(sourceTabls);
		ConnectionParams cp = ParamFile.getValueByKey(sourceConnName);
		DataBaseType sourceDataType = DataBaseType.getDataBaseTypeByIndex(Integer.valueOf(cp.getDbType()));
		cp = ParamFile.getValueByKey(destConnName);
		DataBaseType destDataType = DataBaseType.getDataBaseTypeByIndex(Integer.valueOf(cp.getDbType()));
		List<Table> listTable = getTableMetaData(sourceDb, sourceConnName, sourceDataType, destDataType, destConnName,
				tablesList, destDb);
		List<TableSql> tableSqls = getTableSql(listTable, sourceDataType);
		alterSql.add(getAlterSql(listTable, sourceDataType, destConnName));
		return tableSqls;
	}

	private List<Table> getTableMetaData(String sourceDB, String sourceConnName, DataBaseType sourceDbType,
			DataBaseType destDbType, String destConnName, List<String> sourceTabls, String destDb) throws SQLException {
		List<Table> listTbs = new ArrayList<>();
		IDataMetaData im = MetaDataFactory.getDataMeta(sourceConnName);
		for (String tb : sourceTabls) {
			Table table = new Table();
			if (sourceDbType.equals(DataBaseType.PHOENIX)) {
				if (destDbType.equals(DataBaseType.PHOENIX)) {
					table.setTableName(tb);
				} else {
					String[] strs = tb.split(".");
					table.setTableName(strs[1]);
				}
			} else {
				if (destDbType.equals(DataBaseType.PHOENIX)) {
					table.setTableName(destDb + "." + tb);
				} else {
					table.setTableName(tb);
				}
			}
			table.setDest(destDbType);
			table.setSoure(sourceDbType);
			table.setDestConnName(destConnName);
			List<Column> cols = im.getColumnMetaData(sourceDB, tb);
			table.setCols(cols);
			table.setFkeys(im.getImportKeys(tb, sourceDB));
			table.setPks(im.getPrimaryKeyMetaData(tb, sourceDB));
			List<List<FieldValue>> valueList = executeSql.executeQuery(sourceConnName, destDbType,
					"select * from " + tb, cols);
			table.setValueLists(valueList);
			listTbs.add(table);
		}
		im.close();
		return listTbs;
	}

	private List<TableSql> getTableSql(List<Table> tbs, DataBaseType sourceDbType) {
		IGenerateSql iGenerateSql = GenerateSqlFactory.createGenerateSql(sourceDbType);
		List<TableSql> tableSqlList = new ArrayList<>();
		tbs.forEach(tb -> {
			TableSql ts = new TableSql();
			String ddl = iGenerateSql.generateDDLSQL(tb.getDest(), tb.getSoure(), tb.getCols(), tb.getTableName(),
					tb.getPks());
			ts.setCreateSql(ddl);
			ts.setTbName(tb.getTableName());
			ts.setDestConnName(tb.getDestConnName());
			String insertSql = iGenerateSql.generateDMLSQL(tb.getDest(), tb.getCols(), tb.getTableName());
			ts.setInsertSql(insertSql);
			ts.setValueList(tb.getValueLists());
			tableSqlList.add(ts);
		});
		return tableSqlList;
	}

	private AlterSql getAlterSql(List<Table> tbs, DataBaseType sourceDbType, String destConnName) {
		AlterSql as = new AlterSql();
		IGenerateSql iGenerateSql = GenerateSqlFactory.createGenerateSql(sourceDbType);
		as.setDestConnName(destConnName);
		tbs.forEach(tb -> {
			List<String> alterSql = iGenerateSql.generateAlterDMLSQL(tb.getDest(), tb.getFkeys());
			as.addLists(alterSql);
		});
		return as;
	}

	private void exeCreateTableSql(List<TableSql> tbs) throws Exception {
		for (TableSql ts : tbs) {
			executeSql.executeDDL(ts.getDestConnName(), ts.getCreateSql());
			client.sendMessage(sessionid + ";" + "表" + ts.getTbName() + "创建成功," + ts.getCreateSql());
			Thread.sleep(2000);
		}
	}

	private void exeTableInsertSql(List<TableSql> tbs, String[] noInsertTableDataArray) throws Exception {
		for (TableSql ts : tbs) {
			if (noInsertTableDataArray != null && noInsertTableDataArray.length > 0) {
				for (int i = 0, j = noInsertTableDataArray.length; i < j; i++) {
					if (!ts.getTbName().equals(noInsertTableDataArray[i])) {
						executeSql.executeBatchUpdate(ts.getDestConnName(), ts.getInsertSql(), ts.getValueList());
						client.sendMessage(
								sessionid + ";" + "表" + ts.getTbName() + "insert语句执行成功," + ts.getInsertSql());
						break;
					}
				}
			} else {
				executeSql.executeBatchUpdate(ts.getDestConnName(), ts.getInsertSql(), ts.getValueList());
				client.sendMessage(sessionid + ";" + "表" + ts.getTbName() + ":insert语句执行成功," + ts.getInsertSql());
			}
			Thread.sleep(1000);
		}

	}

	private void exeDbAlterSql(AlterSql alterSql) throws SQLException, InterruptedException {
		String connName = alterSql.getDestConnName();
		List<String> alterSqlList = alterSql.getLists();
		for (String sql : alterSqlList) {
			executeSql.executeDDL(connName, sql);
			client.sendMessage(sessionid + ";" + sql + "语句执行成功");
			Thread.sleep(1000);
		}
	}

	private void onlyInserData(String destConnName, String sourceConnName, String sourceDbName, String destDbName,
			String[] tables) throws Exception {
		IDataMetaData im = MetaDataFactory.getDataMeta(sourceConnName);
		ConnectionParams cp = ParamFile.getValueByKey(sourceConnName);
		DataBaseType sourceDataType = DataBaseType.getDataBaseTypeByIndex(Integer.valueOf(cp.getDbType()));
		cp = ParamFile.getValueByKey(destConnName);
		DataBaseType destDbType = DataBaseType.getDataBaseTypeByIndex(Integer.valueOf(cp.getDbType()));
		IGenerateSql iGenerateSql = GenerateSqlFactory.createGenerateSql(sourceDataType);
		for (String table : tables) {
			if (table != null) {
				exeInsert(table, destConnName, sourceConnName, sourceDbName, im, iGenerateSql, tables, destDbType,
						destDbName);
				Thread.sleep(1000);
			}
		}
		im.close();

	}

	private void exeInsert(String table, String destConnName, String sourceConnName, String sourceDbName,
			IDataMetaData im, IGenerateSql iGenerateSql, String[] tables, DataBaseType destType, String destDbname)
			throws SQLException, InterruptedException {
		List<ForeignKey> fkeys = im.getImportKeys(table, sourceDbName);
		if (fkeys != null && !fkeys.isEmpty()) {
			for (ForeignKey fkey : fkeys) {
				exeInsert(fkey.getPkTableName(), destConnName, sourceConnName, sourceDbName, im, iGenerateSql, tables,
						destType, destDbname);
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
			List<Column> cols = im.getColumnMetaData(sourceDbName, table);
			List<List<FieldValue>> rowsValues = executeSql.executeQuery(sourceConnName, destType,
					"select * from " + table, cols);
			String sql = "";
			if (destType.equals(DataBaseType.PHOENIX)) {
				sql = iGenerateSql.generateDMLSQL(DataBaseType.PHOENIX, cols, destDbname + "." + table);
			} else {
				sql = iGenerateSql.generateDMLSQL(destType, cols, table);
			}
			executeSql.executeBatchUpdate(destConnName, sql, rowsValues);
			client.sendMessage(sessionid + ";" + "表" + table + "insert语句执行成功," + sql);
			Thread.sleep(1000);
			for (int i = 0, j = tables.length; i < j; i++) {
				if (null != tables[i] && tables[i].equals(table)) {
					tables[i] = null;
					break;
				}
			}
		}

	}

}
