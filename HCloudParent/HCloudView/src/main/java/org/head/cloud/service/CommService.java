package org.head.cloud.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

public abstract class CommService {
	@Context
	protected HttpServletRequest request;
	@Context
	protected HttpServletResponse response;

	protected void outJson(String jsonStr) {
		try {
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding("utf-8");
			out.write(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
