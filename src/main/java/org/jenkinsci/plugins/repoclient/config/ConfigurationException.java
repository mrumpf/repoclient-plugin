package org.jenkinsci.plugins.repoclient.config;

/**
 * Wrapper for configuration exceptions.
 *
 * @author mrumpf
 *
 */
public class ConfigurationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}
}
