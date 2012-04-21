package org.jenkinsci.plugins.repoclient.client.nexus;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jenkinsci.plugins.repoclient.ArtifactId;
import org.jenkinsci.plugins.repoclient.GroupId;
import org.jenkinsci.plugins.repoclient.Version;
import org.jenkinsci.plugins.repoclient.client.MavenRepositoryClient;
import org.jenkinsci.plugins.repoclient.config.Configuration;
import org.restlet.data.ChallengeScheme;
import org.restlet.engine.Engine;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The NexusClient is not using the nexus-rest-client package as this package
 * requires an older version of the RESTlet libraries and those are not
 * compliant with the version used by the ArtifactoryClient.
 * 
 * @author Michael Rumpf
 * 
 */
public class NexusClient extends MavenRepositoryClient {

	private static final String MVN_REPO_HOST_DEFAULT = "seu75.gdc-leinf01.t-systems.com";
	private static final int MVN_REPO_PORT_DEFAULT = 443;
	private static final String MVN_REPO_USER_DEFAULT = "reader";
	private static final String MVN_REPO_PWD_DEFAULT = "7asJdx2TOZSym7jrO0JU";
	private static final String MVN_REPO_URLSCHEME_DEFAULT = "https";
	private static final String MVN_REPO_CONTEXTPATH_DEFAULT = "nexus";
	private static final String MVN_REPO_NAME_DEFAULT = "releases";
	private static final String MVN_REPO_BASEFOLDER_DEFAULT = "com/daimler/ams/";

	private static final String REST_PATH_PREFIX = "service/local/repositories";
	private static final String REST_PATH_SUFFIX = "content";

	static {
		Engine.getInstance().getRegisteredConverters()
				.add(new NexusConverter());
	}

	private static final Logger logger = LoggerFactory
			.getLogger(NexusClient.class);

	private Configuration mConfig;

	public NexusClient(Configuration config) {
		mConfig = config;
	}

	private ClientResource createResource(String folder) {
		String f = folder;
		if (folder.startsWith("/")) {
			f = folder.substring(1);
		}
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
				+ "/" + REST_PATH_SUFFIX + "/" + f;
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		logger.info("url=" + url);
		ClientResource c = new ClientResource(url);
		// Send an authenticated request using the Basic authentication scheme.
		c.setChallengeResponse(ChallengeScheme.HTTP_BASIC,
				mConfig.getString(MVN_REPO_USER_KEY, MVN_REPO_USER_DEFAULT),
				mConfig.getString(MVN_REPO_PWD_KEY, MVN_REPO_PWD_DEFAULT));
		return c;
	}

	private List<ContentItem> getContentItems(String folder) {
		final ClientResource r = createResource(folder);
		return r.get(DataContainer.class).getData();
	}

	@Override
	public List<GroupId> getGroupIds() {
		String folder = mConfig.getString(MVN_REPO_BASEFOLDER_KEY,
				MVN_REPO_BASEFOLDER_DEFAULT);
		final List<ContentItem> children = getContentItems(folder);
		List<GroupId> apps = new ArrayList<GroupId>();
		for (ContentItem child : children) {
			apps.add(new GroupId(child.getText()));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Found GroupIds: " + apps);
		}
		return apps;
	}

	@Override
	public List<ArtifactId> getArtifactIds(GroupId groupId) {
		final String file = mConfig.getString(MVN_REPO_BASEFOLDER_KEY,
				MVN_REPO_BASEFOLDER_DEFAULT) + "/" + groupId.getName();
		final List<ContentItem> folders = getContentItems(file);

		for (ContentItem folder : folders) {
			final ArtifactId artifactId = new ArtifactId(groupId, folder.getText());
			if (!groupId.getArtifactIds().contains(artifactId)) {
				groupId.addArtifactId(artifactId);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Found ArtifactIds: " + groupId.getArtifactIds());
		}
		return groupId.getArtifactIds();
	}

	@Override
	public List<Version> getVersions(ArtifactId artifactId) {
		final String file = mConfig.getString(MVN_REPO_BASEFOLDER_KEY,
				MVN_REPO_BASEFOLDER_DEFAULT)
				+ "/"
				+ artifactId.getApplication().getName() + "/" + artifactId.getName();
		final List<ContentItem> folders = getContentItems(file);

		for (ContentItem folder : folders) {
			final Version ver = new Version(artifactId, folder.getText());
			if (!artifactId.getVersions().contains(ver)) {
				artifactId.addVersion(ver);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Found versions: " + artifactId.getVersions());
		}
		return artifactId.getVersions();
	}

//	@Override
//	public List<Artifact> getFiles(Version version) {
//		final String suffix = version.getComponent().getApplication().getName()
//				+ "/" + version.getComponent().getName() + "/"
//				+ version.toString() + "/";
//		final String restFolder = mConfig.getString(MVN_REPO_BASEFOLDER_KEY,
//						MVN_REPO_BASEFOLDER_DEFAULT) + "/" + suffix;
//		final List<ContentItem> files = getContentItems(restFolder);
//		final List<Artifact> filenames = new ArrayList<Artifact>();
//		for (ContentItem file : files) {
//			int idx = file.getResourceURI().lastIndexOf("/");
//			final String filename = file.getResourceURI().substring(idx + 1);
//			// ignore the pom file which gets uploaded for Maven Builds
//			if (filename.endsWith(".zip")) {
//				final Artifact f = new Artifact(file.getResourceURI(),
//						filename, file.getSizeOnDisk());
//				filenames.add(f);
//			}
//		}
//		if (logger.isDebugEnabled()) {
//			logger.debug("Found files: " + filenames);
//		}
//		return filenames;
//	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
