package org.head.cloud.util;

import java.io.Serializable;

public class ConnectionParams implements Serializable {

	/**
	 * jdbc连接参数类
	 */
	private static final long serialVersionUID = 1L;
	private String driverClass;
	private String url;
	private String user;
	private String pwd;
	private String dbType;

	public ConnectionParams() {
	}

	public ConnectionParams(String driver, String url, String user, String pwd,String dbtype) {
		this.driverClass = driver;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.dbType=dbtype;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return driverClass + "_" + url + "_" + user + "_" + pwd;
	}

}
