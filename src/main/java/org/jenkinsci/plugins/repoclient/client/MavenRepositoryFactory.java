package org.jenkinsci.plugins.repoclient.client;

import org.jenkinsci.plugins.repoclient.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the factory class for creating MavenRepository instances.
 *
 * @author mrumpf
 *
 */
public class MavenRepositoryFactory {

	/** The supported Maven repository types. */
	public static enum RepoType {
		ARTIFACTORY, NEXUS
	};

	private static final Logger logger = LoggerFactory
			.getLogger(MavenRepositoryFactory.class.getName());

	private MavenRepositoryFactory() {
		// do not allow instances of this class
	}

	/**
	 * Creates a Maven repository by using the default config resolution process.
	 * 
	 * @return a Maven repository instance. 
	 */
	public static MavenRepositoryClient createMavenRepositoryClient() {
		Configuration config = new Configuration();
        return createMavenRepositoryClient(config);
	}

	/**
	 * Creates a Maven repository by using the specified configuration instance (used for testing).
	 * 
	 * @return a Maven repository instance. 
	 */
	public static MavenRepositoryClient createMavenRepositoryClient(
			Configuration config) {
		if (config == null) {
			config = new Configuration();
		}
		String type = config.getString(MavenRepositoryClient.MVN_REPO_TYPE);
		MavenRepositoryClient client = new MavenRepositoryClient();
		if (logger.isDebugEnabled()) {
			logger.debug("Created client: " + client);
		}
		return client;
	}
}
