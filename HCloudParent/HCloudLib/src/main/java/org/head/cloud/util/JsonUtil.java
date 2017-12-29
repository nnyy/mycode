package org.head.cloud.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	static ObjectMapper objectMapper = null;
	static {
		objectMapper = new ObjectMapper();
	}

	public static String objectToStr(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}

	public static <T> T jsonToObject(String json, Class<T> classzz) {
		try {
			return objectMapper.readValue(json, classzz);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}
}
