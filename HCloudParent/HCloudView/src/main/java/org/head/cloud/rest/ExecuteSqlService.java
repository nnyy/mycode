package org.head.cloud.rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.head.cloud.connection.command.ExecuteSql;
import org.head.cloud.service.CommService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Path("/hcloud/sql/")
public class ExecuteSqlService extends CommService {

	@Path("execute")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=utf-8")
	public Map<String, Object> executeSql(@FormParam("sql") String sql, @FormParam("connName") String connName) {
		String tempSql = parseStr(sql).toLowerCase();
		Map<String, Object> rst = null;
		try {
			if (StringUtils.isNotEmpty(tempSql)) {
				ExecuteSql exe = new ExecuteSql();
				if (tempSql.startsWith("select")) {
					rst = exe.executeQuery(connName, tempSql);
				} else if (tempSql.startsWith("create") || tempSql.startsWith("drop") || tempSql.startsWith("alter")) {
					int r = exe.executeDDL(connName, tempSql);
					rst = new HashMap<>();
					List<String> cols = new ArrayList<String>();
					cols.add("result");
					rst.put("cols", cols);
					List<List<Object>> values = new ArrayList<>();
					List<Object> rr = new ArrayList<>();
					if (r == 0) {
						rr.add("语句执行成功");
					} else {
						rr.add("语句执行失败");
					}
					values.add(rr);
					rst.put("values", values);
				} else {
					int r = exe.executeUpdate(connName, tempSql);
					rst = new HashMap<>();
					List<String> cols = new ArrayList<String>();
					cols.add("result");
					rst.put("cols", cols);
					List<List<Object>> values = new ArrayList<>();
					List<Object> rr = new ArrayList<>();
					if (r>0) {
						rr.add("语句执行成功,返回结果" + r);
					} else {
						rr.add("语句执行失败,返回结果" + r);
					}
					values.add(rr);
					rst.put("values", values);
				}
			}
		} catch (SQLException ex) {
			rst = new HashMap<>();
			List<String> cols = new ArrayList<String>();
			cols.add("result");
			rst.put("cols", cols);
			List<List<Object>> values = new ArrayList<>();
			List<Object> rr = new ArrayList<>();
			rr.add("语句执行异常,日志为:" + ex.getMessage());
			values.add(rr);
			rst.put("values", values);
		}
		return rst;
	}



	private String parseStr(String strHtml) {
		String txtcontent = strHtml.replaceAll("</?[^>]+>", ""); // 剔出<html>的标签
		txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");// 去除字符串中的空格,回车,换行符,制表符
		txtcontent = txtcontent.replaceAll("&nbsp;", "");
		return txtcontent;
	}

}
