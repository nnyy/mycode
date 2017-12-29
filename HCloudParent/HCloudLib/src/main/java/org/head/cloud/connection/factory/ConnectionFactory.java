package org.head.cloud.connection.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.head.cloud.connection.IConnection;
import org.head.cloud.util.ConnectionParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionFactory {
	static Logger LOGGER=LoggerFactory.getLogger(ConnectionFactory.class);
	public static IConnection createConnection(ConnectionParams connectionParams) {

		return new IConnection() {

			private Connection conn;
			private int dbtype;

			@Override
			public Connection getConn() {
				return conn;
			}

			@Override
			public IConnection createConn() {
				// TODO Auto-generated method stub
				try {
					Class.forName(connectionParams.getDriverClass());
					conn = DriverManager.getConnection(connectionParams.getUrl(), connectionParams.getUser(),
							connectionParams.getPwd());
					dbtype = Integer.valueOf(connectionParams.getDbType());
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					LOGGER.error(e.getMessage());
				}
				return this;
			}

			@Override
			public void closeConn() {
				// TODO Auto-generated method stub
				try {
					this.conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					LOGGER.error(e.getMessage());
				}

			}

			@Override
			public void release(Statement st, ResultSet rt, Connection conn) {
				try {
					if (st != null) {
						st.close();
					}
					if (rt != null) {
						rt.close();
					}
					if (conn != null) {
						conn.close();
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					LOGGER.error(e.getMessage());
				}

				// TODO Auto-generated method stub

			}

			@Override
			public int getDbtype() {
				// TODO Auto-generated method stub
				return dbtype;
			}
		}.createConn();
	}
}
