package org.head.cloud.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.head.cloud.connection.command.ExecuteSql;
import org.head.cloud.connection.command.ExportCommand;
import org.head.cloud.service.CommService;

import com.alibaba.fastjson.JSON;

@Path("/hcloud/data/export/")
public class DataExportService extends CommService {

	@Path("toexcel")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json;charset=utf-8")
	public String exportToExcel(@FormParam("sql") String sql, @FormParam("connName") String connName) {
		Map<String,String> r=new HashMap<String,String>();
		ExecuteSql exe = new ExecuteSql();
		if (StringUtils.isNotEmpty(sql) && StringUtils.isNotEmpty(connName)) {
			try {
				Map<String, Object> rst = exe.executeQuery(connName, sql);
				ExportCommand exportcmd = new ExportCommand();
				String fileName =exportcmd.exportToExcel(rst, response);
				r.put("rst", fileName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				r.put("rst", "-1");
			}
		}
		return JSON.toJSONString(r);
	}

	@GET
	@Path("down")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response outFile(@QueryParam("fileName") String fileName) {
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
}
