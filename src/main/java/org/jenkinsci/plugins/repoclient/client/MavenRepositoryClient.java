package org.jenkinsci.plugins.repoclient.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the base class for all Maven repository clients. Currently there are
 * the following clients available:
 * <ul>
 * <li>NexusClient</li>
 * <li>ArtifactoryClient</li>
 * </ul>
 * 
 * @author mrumpf
 * 
 */
public class MavenRepositoryClient {

	/**
	 * Determines which repository implementation will be created by the
	 * factory: artifactory or nexus.
	 */
	public static final String MVN_REPO_TYPE = "mvn.repo.type";

	/** The hostname of the repository server. */
	public static final String MVN_REPO_HOST_KEY = "mvn.repo.host";
	/** The size of the download buffer. */
	public static final String MVN_REPO_DOWNLOAD_BUFFER_SIZE_KEY = "mvn.repo.downloadbuffersize";
	/** The port of the repository server application. */
	public static final String MVN_REPO_PORT_KEY = "mvn.repo.port";
	/** The user to access the repository server. */
	public static final String MVN_REPO_USER_KEY = "mvn.repo.user";
	/** The password to access the repository server. */
	public static final String MVN_REPO_PWD_KEY = "mvn.repo.password";
	/** The URL scheme of the repository server URL. */
	public static final String MVN_REPO_URLSCHEME_KEY = "mvn.repo.scheme";
	/** The user to access the repository server. */
	public static final String MVN_REPO_CONTEXTPATH_KEY = "mvn.repo.contextpath";
	/** The name of the repository on the server. */
	public static final String MVN_REPO_NAME_KEY = "mvn.repo.name";
	/** The base folder where to search for applications/components/versions. */
	public static final String MVN_REPO_BASEFOLDER_KEY = "mvn.repo.basefolder";

	/** The default size of the download buffer. */
	public static final int MVN_REPO_DOWNLOAD_BUFFER_SIZE_DEFAULT = 8192;

	private static final Logger logger = LoggerFactory
			.getLogger(MavenRepositoryClient.class.getName());

	/**
	 * 
	 * @return a list of groupIds from a Maven repository folder
	 */
	public List<GroupId> getGroupIds() {
		return null;
	}

	/**
	 * 
	 * @return a list of artifactIds from the Maven repository folder
	 */
	public List<ArtifactId> getArtifactIds(GroupId groupId) {
		return null;
	}

	/**
	 * 
	 * @param comp
	 *            the component to get the versions for
	 * @return a list of versions for the specified component
	 */
	public List<Version> getVersions(ArtifactId artifactId) {
		return null;
	}

	/**
	 * 
	 * @param comp
	 *            the component to get the versions for
	 * @return a list of versions for the specified component
	 */
	//public abstract List<xxx> getFiles(Version version);
}
