package org.head.cloud.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.head.cloud.connection.factory.DataSourceFactory;
import org.head.cloud.service.CommService;
import org.head.cloud.util.ConnectionParams;
import org.head.cloud.util.ParamFile;

import com.alibaba.fastjson.JSON;

@Path("/hcloud/conn/")
public class SaveConnectInfoService extends CommService {

	@Path("save")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=UTF-8")
	public String saveParams(@FormParam("connName") String connName, @FormParam("dbtype") String dbtype,
			@FormParam("driverClass") String driverClass, @FormParam("url") String url, @FormParam("user") String user,
			@FormParam("pwd") String pwd) {
		ConnectionParams cp = new ConnectionParams(driverClass, url, user, pwd, dbtype);
		// 写入配置文件
		int f = ParamFile.writeParamtofile(connName, cp);
		// 创建数据库连接池

		 Map<String, String> rst = new HashMap<String, String>();
		if (f == 1) {
			DataSourceFactory.initFactory(connName, cp);
		//	request.gstatusetSession(false).setAttribute("LastConn", cp);
			rst.put("status", "1");
			rst.put("connname", connName);
		} else if (f == 2) {
			rst.put("status", "2");
		} else {
			rst.put("status", "0");
		}
		 return JSON.toJSONString(rst);
	}
}
