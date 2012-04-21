package org.jenkinsci.plugins.repoclient.config;

public class ConfigurationFactory {

	private static Configuration sConfiguration;

	private ConfigurationFactory() {
		// do not allow instances of this class
	}

	public static synchronized Configuration getConfiguration() {
		if (sConfiguration == null) {
			// TODO ???
			sConfiguration = new Configuration();
		}
		return sConfiguration;
	}
}
