package org.head.cloud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

public class ParamFile {
	static Properties prop;
	static String path = System.getProperty("java.io.tmpdir") + "/connect.properties";
	static {
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream input = new FileInputStream(file);
			prop = new Properties();
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized int writeParamtofile(String key, ConnectionParams connectionParams) {
		try {
			if (!prop.containsKey(key)) {
				String jsonStr = JSON.toJSONString(connectionParams); // JsonUtil.objectToStr(connectionParams);
				prop.setProperty(key, jsonStr);
				FileOutputStream stream = new FileOutputStream(path);
				prop.store(stream, "新连接");
				return 1;
			} else {
				return 2;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static synchronized List<Object> getKeys() {
		Set<Object> set = prop.keySet();
		List<Object> keys = new ArrayList<>();
		keys.addAll(set);
		return keys;

	}

	public static synchronized ConnectionParams getValueByKey(String key) {
		String str = prop.getProperty(key, "");
		ConnectionParams cp = null;
		if (StringUtils.isNotEmpty(str)) {
			cp = JSON.parseObject(str, ConnectionParams.class);
		}
		return cp;
	}

	public static synchronized boolean delPro(String key) {
		Object obj = prop.remove(key);
		if (obj != null) {
			return true;
		}
		return false;
	}

}
