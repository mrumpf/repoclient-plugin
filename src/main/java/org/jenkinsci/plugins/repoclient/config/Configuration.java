package org.jenkinsci.plugins.repoclient.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The configuration class provides access to all the configuration properties.
 *
 * @author mrumpf
 *
 */
public class Configuration {

	private Properties props = new Properties();

	private String configName;

	/**
	 * Default constructor loading the properties file specified by the "config" VM parameter or
	 * "test" if the parameter has not been set.
	 */
	public Configuration() {
	    this(System.getProperty("config", "test"));
	}

	/**
	 * Constructor where the name of the configuration can be specified. The name will be used to load the
	 * corresponding property file.
	 */
	public Configuration(String cfgName) {
		configName = cfgName;
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(configName + ".properties");
		try {
			props.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ConfigurationException("Could not load properties file: "
					+ configName + ".properties", e);
		}
		// TODO: try to find file directly
	}

	/**
	 * Constructor used for testing.
	 *
	 * @param cfgName the name of the configuration
	 * @param p the Properties instance
	 */
	public Configuration(String cfgName, Properties p) {
		configName = cfgName;
		props = p;
	}

	public String getConfigName() {
		return configName;
	}

	public String getString(String key) {
		return props.getProperty(key);
	}

	public String getString(String key, String def) {
		return props.getProperty(key, def);
	}

	public Integer getInteger(String key) {
		Integer i = null;
		try {
			String val = props.getProperty(key);
			if (val != null) {
				i = Integer.valueOf(val);
			}
		} catch (NumberFormatException ex) {
			throw new ConfigurationException(
					"Could not convert property value to an integer: " + key,
					ex);
		}
		return i;
	}

	public Integer getInteger(String key, Integer def) {
		Integer i = null;
		try {
			i = Integer.valueOf(props.getProperty(key, def.toString()));
		} catch (NumberFormatException ex) {
			throw new ConfigurationException(
					"Could not convert property value to an integer: " + key,
					ex);
		}
		return i;
	}
}
