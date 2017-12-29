package org.head.cloud.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public interface IConnection {
	
	public IConnection createConn();
	public void closeConn();
	public void release(Statement st,ResultSet rt,Connection conn );
	public Connection getConn();
	public int getDbtype();

}
