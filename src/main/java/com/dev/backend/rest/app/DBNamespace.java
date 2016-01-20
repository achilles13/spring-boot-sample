package com.dev.backend.rest.app;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.env.Environment;

public class DBNamespace {

	private static final long serialVersionUID = 4221613810877942656L;
	protected final Map<String, String> defaultValues = new HashMap<String, String>();
	protected final Map<String, String> map = new HashMap<String, String>();
	protected final Map<String, String> origionalValues = new HashMap<String, String>();

	private final String namespacePrefix = "db.";

	public static final String DRIVER_CLASS = "connection.driver_class";
	public static final String CONN_URL = "connection.url";
	public static final String USER_NAME = "connection.username";
	public static final String PASSWORD = "connection.password";
	public static final String CONN_POOL = "connection.pool";
	public static final String DIALECT = "dialect";
	public static final String SESSION_CONTEXT_CLASS = "session.context.class";
	public static final String SHOWSQL = "showsql";
	public static final String HBM2DLL_AUTO = "hbm2ddl.auto";
	public static final String CACHE_PROVIDER_CLASS = "cache.provider_class";

	private final List<String> keys = new ArrayList<String>();

	public DBNamespace(Environment environment) {
		keys.add(DRIVER_CLASS);
		keys.add(CONN_URL);
		keys.add(USER_NAME);
		keys.add(PASSWORD);
		keys.add(CONN_POOL);
		keys.add(DIALECT);
		keys.add(SESSION_CONTEXT_CLASS);
		keys.add(SHOWSQL);
		keys.add(HBM2DLL_AUTO);
		keys.add(CACHE_PROVIDER_CLASS);
		defaultValues.put(PASSWORD, " ");


		for (String entry : keys) {
			final String key = namespacePrefix + entry;
			String strValue = environment.getProperty(key, defaultValues.get(entry));
			if (strValue != null) {
				map.put(entry, strValue);
			}
		}

	}

	public String get(String key) {
		return this.map.get(key);
	}

	public String get(String key, String defaultValue) {
		String ret = get(key);
		if (ret == null) {
			return defaultValue;
		}
		return ret;
	}

	public Map<String, String> getProperties() {
		return this.map;
	}

	public Map<String, String> getDefaultProperties() {
		return this.defaultValues;
	}

	public String getRequired(String key) {
		String ret = this.map.get(key);

		if (ret == null) {
			throw new RuntimeException("Missing Required Param: " +namespacePrefix + key);
		}

		return ret;
	}
}
