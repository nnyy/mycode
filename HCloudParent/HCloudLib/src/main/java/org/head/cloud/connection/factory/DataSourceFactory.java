
package org.head.cloud.connection.factory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang.StringUtils;
import org.head.cloud.connection.IConnection;
import org.head.cloud.util.ConnectionParams;
import org.head.cloud.util.ParamFile;

import com.alibaba.fastjson.JSON;
import com.google.inject.OutOfScopeException;

public class DataSourceFactory {
	static int _INITNUM_ = 5;
	static Map<String, LinkedList<IConnection>> _CONNMAP_ = new HashMap<String, LinkedList<IConnection>>();

	public static synchronized void initFactory(String connName) {
		LinkedList<IConnection> list = new LinkedList<>();
		ConnectionParams cp = ParamFile.getValueByKey(connName);
		if (null != cp)
			for (int i = 0; i < _INITNUM_; i++) {
				list.add(ConnectionFactory.createConnection(cp));
				_CONNMAP_.put(connName, list);
			}
		else {
			throw new RuntimeErrorException(new Error("无法创建目标数据连接"));
		}
	}

	public static synchronized void initFactory(String connName, ConnectionParams cp) {
		LinkedList<IConnection> list = new LinkedList<>();
		for (int i = 0; i < _INITNUM_; i++) {
			list.add(ConnectionFactory.createConnection(cp));
		}
		_CONNMAP_.put(connName, list);
	}

	public static synchronized void initFactory(String connName, ConnectionParams cp, int connNum) {
		if (connNum == 0) {
			connNum = _INITNUM_;
		}
		LinkedList<IConnection> list = new LinkedList<>();
		for (int i = 0; i < _INITNUM_; i++) {
			list.add(ConnectionFactory.createConnection(cp));
		}
		_CONNMAP_.put(connName, list);

	}

	public static synchronized IConnection getConnection(String connName) {
		if (StringUtils.isEmpty(connName)) {
			throw new NullPointerException("连接名称不能空");
		} else {
			LinkedList<IConnection> listConns = _CONNMAP_.get(connName);
			if (listConns == null) {
				initFactory(connName);
				listConns = _CONNMAP_.get(connName);
			} else if (listConns.isEmpty()) {
				ConnectionParams cp = ParamFile.getValueByKey(connName);
				for (int i = 0; i < _INITNUM_; i++) {
					listConns.add(ConnectionFactory.createConnection(cp));
				}
			}
			return listConns.removeFirst();
		}
	}

	public static synchronized void recoveryConnection(String connName, IConnection conn) {
		LinkedList<IConnection> list = _CONNMAP_.get(connName);
		list.add(conn);
	}

	public static synchronized boolean isInit(String connName) {
		LinkedList<IConnection> listConns = _CONNMAP_.get(connName);
		if (listConns == null)
			return false;
		else
			return true;

	}

}
