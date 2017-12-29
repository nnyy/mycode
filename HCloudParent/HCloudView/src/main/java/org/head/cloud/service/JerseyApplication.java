package org.head.cloud.service;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.alibaba.fastjson.support.jaxrs.FastJsonFeature;

public class JerseyApplication extends ResourceConfig {
	public JerseyApplication() {
		packages("org.head.cloud");
		register(LoggingFeature.class);
		register(FastJsonFeature.class);
	}

}
