package org.jenkinsci.plugins.repoclient.client.artifactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.jenkinsci.plugins.repoclient.ArtifactId;
import org.jenkinsci.plugins.repoclient.GroupId;
import org.jenkinsci.plugins.repoclient.Version;
import org.jenkinsci.plugins.repoclient.client.MavenRepositoryClient;
import org.jenkinsci.plugins.repoclient.client.artifactory.FolderInfo.Folder;
import org.jenkinsci.plugins.repoclient.config.Configuration;
import org.restlet.data.ChallengeScheme;
import org.restlet.engine.Engine;
import org.restlet.resource.ClientResource;

/**
 * A helper class with all Artifactory operations.
 * 
 * @author mrumpf
 */
public final class ArtifactoryClient extends MavenRepositoryClient {
	public static final int BINARY = 0;
	public static final int SOURCE = 1;
	public static final int DOC = 2;

	private static final String MVN_REPO_HOST_DEFAULT = "q4de3gsy232.gdc-leinf01.t-systems.com";
	private static final int MVN_REPO_PORT_DEFAULT = 443;
	private static final String MVN_REPO_USER_DEFAULT = "ams-read";
	private static final String MVN_REPO_PWD_DEFAULT = "dSyNlQGcxS";
	private static final String MVN_REPO_URLSCHEME_DEFAULT = "https";
	private static final String MVN_REPO_CONTEXTPATH_DEFAULT = "artifactory";
	private static final String MVN_REPO_NAME_DEFAULT = "releases";
	private static final String MVN_REPO_BASEFOLDER_DEFAULT = "com/daimler/ams";

	private static final String REST_PATH_PREFIX = "api/storage";

	private static final Logger logger = Logger
			.getLogger(ArtifactoryClient.class.getName());

	private Configuration mConfig;

	static {
		Engine.getInstance().getRegisteredConverters()
				.add(new ArtifactoryConverter());
	}

	/**
	 * Constructor.
	 * 
	 * @param config
	 *            the configuration to read the settings from.
	 * 
	 */
	public ArtifactoryClient(Configuration config) {
		mConfig = config;
	}

	private ClientResource createResource(String folder) {
		String url = mConfig.getString(MVN_REPO_URLSCHEME_KEY,
				MVN_REPO_URLSCHEME_DEFAULT)
				+ "://"
				+ mConfig.getString(MVN_REPO_HOST_KEY, MVN_REPO_HOST_DEFAULT)
				+ ":"
				+ mConfig.getInteger(MVN_REPO_PORT_KEY, MVN_REPO_PORT_DEFAULT)
				+ "/"
				+ mConfig.getString(MVN_REPO_CONTEXTPATH_KEY,
						MVN_REPO_CONTEXTPATH_DEFAULT)
				+ "/"
				+ REST_PATH_PREFIX
				+ "/"
				+ mConfig.getString(MVN_REPO_NAME_KEY, MVN_REPO_NAME_DEFAULT)
				+ "/" + folder;
		ClientResource c = new ClientResource(url);
		// Send an authenticated request using the Basic authentication scheme.
		c.setChallengeResponse(ChallengeScheme.HTTP_BASIC,
				mConfig.getString(MVN_REPO_USER_KEY, MVN_REPO_USER_DEFAULT),
				mConfig.getString(MVN_REPO_PWD_KEY, MVN_REPO_PWD_DEFAULT));
		if (logger.isDebugEnabled()) {
			logger.debug("Created resource for url: " + url);
		}
		return c;
	}

	/**
	 * 
	 * @return a list of applications from the Artifactory folder
	 *         com/daimler/ams
	 */
	public List<GroupId> getGroupIds() {
		final String file = mConfig.getString(MVN_REPO_BASEFOLDER_KEY,
				MVN_REPO_BASEFOLDER_DEFAULT) + "/";
		final FolderInfo fi = getFolderInfo(file);

		final Set<GroupId> s = new TreeSet<GroupId>(
				new Comparator<GroupId>() {
					public int compare(GroupId o1, GroupId o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
		for (int i = 0; i < fi.getChildren().length; i++) {
			final String sub = fi.getChildren()[i].getUri().substring(1);
			s.add(new GroupId(sub));
		}
		final List<GroupId> l = new ArrayList<GroupId>();
		l.addAll(s);
		if (logger.isDebugEnabled()) {
			logger.debug("Found applications: " + l);
		}
		return l;
	}

	/**
	 * 
	 * @param app
	 *            the application to get the components for
	 * @return a list of components for the specified application
	 */
	@Override
	public List<ArtifactId> getArtifactIds(GroupId app) {
		final String file = mConfig.getString(MVN_REPO_BASEFOLDER_KEY,
				MVN_REPO_BASEFOLDER_DEFAULT) + "/" + app.getName();
		final FolderInfo fi = getFolderInfo(file);

		final Folder[] folders = fi.getChildren();
		for (Folder folder : folders) {
			final ArtifactId artifactId = new ArtifactId(app, folder.getUri()
					.substring(1));
			if (!app.getArtifactIds().contains(artifactId)) {
				app.addArtifactId(artifactId);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Found components: " + app.getArtifactIds());
		}
		return app.getArtifactIds();
	}

	/**
	 * 
	 * @param artifactId
	 *            the component to get the versions for
	 * @return a list of versions for the specified component
	 */
	@Override
	public List<Version> getVersions(ArtifactId artifactId) {
		final String file = mConfig.getString(MVN_REPO_BASEFOLDER_KEY,
				MVN_REPO_BASEFOLDER_DEFAULT)
				+ "/"
				+ artifactId.getApplication().getName() + "/" + artifactId.getName();
		final FolderInfo fi = getFolderInfo(file);

		final Folder[] folders = fi.getChildren();
		for (Folder folder : folders) {
			final Version ver = new Version(artifactId, folder.getUri().substring(1));
			if (!artifactId.getVersions().contains(ver)) {
				artifactId.addVersion(ver);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Found versions: " + artifactId.getVersions());
		}
		return artifactId.getVersions();
	}

	/**
	 * 
	 * @param version
	 *            the version to get the files for
	 * @return a list of artifacts for the specified version
	 */
//	@Override
//	public List<Artifact> getFiles(Version version) {
//		final String suffix = version.getComponent().getApplication().getName()
//				+ "/" + version.getComponent().getName() + "/"
//				+ version.toString() + "/";
//		final String restFolder = mConfig.getString(MVN_REPO_BASEFOLDER_KEY,
//				MVN_REPO_BASEFOLDER_DEFAULT) + "/" + suffix;
//		final String folder = mConfig.getString(MVN_REPO_NAME_KEY,
//				MVN_REPO_NAME_DEFAULT)
//				+ "/"
//				+ mConfig.getString(MVN_REPO_BASEFOLDER_KEY,
//						MVN_REPO_BASEFOLDER_DEFAULT) + "/" + suffix;
//		final FolderInfo fi = getFolderInfo(restFolder);
//		final Folder[] files = fi.getChildren();
//		final List<Artifact> filenames = new ArrayList<Artifact>();
//		for (Folder file : files) {
//			final String filename = file.getUri().substring(1);
//			// ignore the pom file which gets uploaded for Maven Builds
//			if (!filename.endsWith(".pom")) {
//				String url = mConfig.getString(MVN_REPO_URLSCHEME_KEY,
//						MVN_REPO_URLSCHEME_DEFAULT)
//						+ "://"
//						+ mConfig.getString(MVN_REPO_HOST_KEY,
//								MVN_REPO_HOST_DEFAULT)
//						+ ":"
//						+ mConfig.getInteger(MVN_REPO_PORT_KEY,
//								MVN_REPO_PORT_DEFAULT)
//						+ "/"
//						+ mConfig.getString(MVN_REPO_CONTEXTPATH_KEY,
//								MVN_REPO_CONTEXTPATH_DEFAULT);
//
//				final FileInfo fileInfo = getFileInfo(restFolder + filename);
//				final Artifact f = new Artifact(url + "/" + folder + filename,
//						filename, new Long(fileInfo.getSize()));
//				filenames.add(f);
//			}
//		}
//		if (logger.isDebugEnabled()) {
//			logger.debug("Found files: " + filenames);
//		}
//		return filenames;
//	}

	/**
	 * 
	 * @param folder
	 *            the folder to get meta information for
	 * @return meta information for the given folder
	 */
	private FolderInfo getFolderInfo(String folder) {
		final ClientResource r = createResource(folder);
		return r.get(FolderInfo.class);
	}

	/**
	 * 
	 * @param file
	 *            the file to get meta information for
	 * @return meta information for the given file
	 */
	private FileInfo getFileInfo(String file) {
		final ClientResource r = createResource(file);
		return r.get(FileInfo.class);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
