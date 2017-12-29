package org.head.cloud.context;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang.StringUtils;
import org.head.cloud.connection.factory.DataSourceFactory;
import org.head.cloud.util.ConnectionParams;
import org.head.cloud.util.ParamFile;

import com.alibaba.fastjson.JSON;

/**
 * Application Lifecycle Listener implementation class InitConnectionListener
 *
 */
@WebListener
public class InitConnectionListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public InitConnectionListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {

//		List<Object> keys = ParamFile.getKeys();
//		if (!keys.isEmpty()) {
//			for (Object key : keys) {
//				String value = ParamFile.getValueByKey((String) key);
//				if (StringUtils.isNotEmpty(value)) {
//					ConnectionParams cp=JSON.parseObject(value, ConnectionParams.class);
//					DataSourceFactory.initFactory(value, cp);
//				}
//			}
//		}
	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.io.tmpdir"));
	}

}
