package org.head.cloud.connection;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface IExportCommand{
	
	public String exportToExcel(Map<String,Object> params,HttpServletResponse response);
	
}