package org.head.cloud.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.head.cloud.service.CommService;
import org.head.cloud.util.ParamFile;

import com.alibaba.fastjson.JSON;

@Path("/hcloud/info/")
public class ConnectionInfoService extends CommService {

	@Context
	HttpServletRequest request;

	@Path("conninfo")
	@POST
	@Produces("application/json;charset=UTF-8")
	public String getAllConnectionInfo() {
		List<Object> conKeys = ParamFile.getKeys();
		return JSON.toJSONString(conKeys);
	}

	@Path("delconn")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=UTF-8")
	public String delConn(@FormParam("connName") String connName) {
		boolean f = ParamFile.delPro(connName);
		if (f)
			return "1";

		return "0";

	}

}
