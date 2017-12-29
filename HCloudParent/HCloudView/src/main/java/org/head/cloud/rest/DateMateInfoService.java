package org.head.cloud.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.head.cloud.connection.IDataMetaData;
import org.head.cloud.connection.factory.DataSourceFactory;
import org.head.cloud.connection.factory.MetaDataFactory;
import org.head.cloud.service.CommService;
import org.head.cloud.util.Column;
import org.head.cloud.util.ForeignKey;

import com.alibaba.fastjson.JSON;

@Path("/hcloud/meta/")
public class DateMateInfoService extends CommService {

	@Path("tables")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=utf-8")
	public List<Map<String, Object>> tableInfo(@FormParam("connName") String connName) {
		List<Map<String, Object>> nodeList = new ArrayList<>();
		boolean f = DataSourceFactory.isInit(connName);
		if (!f) {
			DataSourceFactory.initFactory(connName);
		}
		if (StringUtils.isNotEmpty(connName)) {
			IDataMetaData metaData = MetaDataFactory.getDataMeta(connName);
			Map<String, List<String>> tables = metaData.getTablesMeta();
			metaData.close();
			Map<String, Object> root = new HashMap<>();
			root.put("id", "-1");
			root.put("isParent", true);
			root.put("asyncFilter", 0);
			root.put("open", true);
			root.put("name", connName);
			root.put("checked", true);
			nodeList.add(root);
			Map<String, Object> subNode = null;
			for (Map.Entry<String, List<String>> entry : tables.entrySet()) {
				subNode = new HashMap<>();
				subNode.put("pId", "-1");
				subNode.put("asyncFilter", 1);
				subNode.put("name", entry.getKey());
				String id = UUID.randomUUID().toString();
				subNode.put("id", id);
				subNode.put("isParent", true);
				subNode.put("open", true);
				subNode.put("connName", connName);
				nodeList.add(subNode);
				for (String tb : entry.getValue()) {
					Map<String, Object> map = new HashMap<String, Object>();
					String iid = UUID.randomUUID().toString();
					map.put("id", iid);
					map.put("name", tb);
					map.put("pId", id);
					map.put("asyncFilter", 2);
					map.put("db", entry.getKey());
					map.put("isParent", true);
					map.put("open", false);
					map.put("connName", connName);
					nodeList.add(map);
				}

			}
		}
		return nodeList;
	}

	@Path("dbs")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=utf-8")
	public String getDataBaseInfo(@FormParam("connName") String connName) {
		if (StringUtils.isNotEmpty(connName)) {
			IDataMetaData metaData = MetaDataFactory.getDataMeta(connName);
			List<String> dbs = metaData.getDataBases();
			return JSON.toJSONString(dbs);
		}
		return "";
	}

	@Path("tbs")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=utf-8")
	public String getTables(@FormParam("connName") String connName, @FormParam("db") String db) {
		if (StringUtils.isNotEmpty(connName) && StringUtils.isNotEmpty(db)) {
			IDataMetaData metaData = MetaDataFactory.getDataMeta(connName);
			List<String> dbs = metaData.getTablesMeta(db);
			return JSON.toJSONString(dbs);
		}
		return "";

	}

	@Path("cols")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=utf-8")
	public List<Map<String, Object>> asynLoadInfo(@FormParam("connName") String connName,
			@FormParam("asyncFilter") Integer asyncFilter, @FormParam("name") String name, @FormParam("id") String id,
			@FormParam("db") String db) {
		if (asyncFilter == 0) {
			return getDbs(name, id);
		} else if (asyncFilter == 1) {
			return gettbs(connName, name, id);
		} else if (asyncFilter == 2) {
			return getcols(connName, db, name, id);
		} else {
			return null;
		}
	}

	public List<Map<String, Object>> getDbs(String connName, String pid) {
		IDataMetaData metaData = MetaDataFactory.getDataMeta(connName);
		List<String> dbs = metaData.getDataBases();
		metaData.close();
		List<Map<String, Object>> nodes = new ArrayList<>();
		for (String db : dbs) {
			Map<String, Object> node = new HashMap<>();
			String iid = UUID.randomUUID().toString();
			node.put("id", iid);
			node.put("pId", pid);
			node.put("isParent", true);
			node.put("open", false);
			node.put("name", db);
			node.put("asyncFilter", 1);
			node.put("connName", connName);
			nodes.add(node);
		}
		return nodes;

	}

	public List<Map<String, Object>> gettbs(String connName, String db, String pid) {
		IDataMetaData metaData = MetaDataFactory.getDataMeta(connName);
		List<String> tbs = metaData.getTablesMeta(db);
		metaData.close();
		List<Map<String, Object>> nodes = new ArrayList<>();
		for (String tb : tbs) {
			Map<String, Object> node = new HashMap<>();
			String iid = UUID.randomUUID().toString();
			node.put("id", iid);
			node.put("pId", pid);
			node.put("isParent", true);
			node.put("open", false);
			node.put("name", tb);
			node.put("asyncFilter", 2);
			node.put("db", db);
			node.put("connName", connName);
			nodes.add(node);
		}
		return nodes;
	}

	public List<Map<String, Object>> getcols(String connName, String db, String table, String pid) {
		IDataMetaData metaData = MetaDataFactory.getDataMeta(connName);
		List<Column> cols = metaData.getColumnMetaData(db, table);
		List<String> pkeys = metaData.getPrimaryKeyMetaData(table, db);
		List<ForeignKey> fkeys = metaData.getImportKeys(table, db);
		metaData.close();
		cols.forEach(col -> {
			pkeys.forEach(pk -> {
				if (col.getColumnName().toLowerCase().equals(pk.toLowerCase())) {
					col.setIsPrimarykey(1);
				}
			});

			fkeys.forEach(fk -> {
				if (col.getColumnName().toLowerCase().equals(fk.getForeignKeyColumnName().toLowerCase())) {
					col.setIsForeignKey(1);
				}
			});
		});
		List<Map<String, Object>> nodes = new ArrayList<>();
		for (Column col : cols) {
			Map<String, Object> node = new HashMap<>();
			String iid = UUID.randomUUID().toString();
			node.put("id", iid);
			if (col.getIsPrimarykey() == 1)
				node.put("name", col.getColumnName() + "(主键)");
			else if (col.getIsForeignKey() == 1)
				node.put("name", col.getColumnName() + "(外键)");
			else
				node.put("name", col.getColumnName());
			node.put("pId", pid);
			node.put("asyncFilter", 3);
			node.put("isParent", false);
			node.put("open", false);
			node.put("connName", connName);
			nodes.add(node);
		}
		return nodes;
	}

}
