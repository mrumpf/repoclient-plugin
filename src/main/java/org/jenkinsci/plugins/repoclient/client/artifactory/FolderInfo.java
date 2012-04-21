package org.jenkinsci.plugins.repoclient.client.artifactory;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The FolderInfo class representing folder meta information. Instances of this class will automatically be
 * deserialized from JSON via the JacksonConverter.
 * 
 * @see http://wiki.jfrog.org/confluence/display/RTF/Artifactory%27s+REST+API#
 *      Artifactory%27sRESTAPI-FolderInfo
 * 
 * @author mrumpf
 */
public class FolderInfo {
	private String uri;
	private String metadataUri;
	private String repo;
	private String path;
	private String created;
	private String createdBy;
	private String lastModified;
	private String modifiedBy;
	private String lastUpdated;

	private Folder[] children;

	public static class Folder {
		private String uri;
		private boolean isFolder;

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public boolean isFolder() {
			return isFolder;
		}

		public void setFolder(boolean isFolder) {
			this.isFolder = isFolder;
		}
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMetadataUri() {
		return metadataUri;
	}

	public void setMetadataUri(String metadataUri) {
		this.metadataUri = metadataUri;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Folder[] getChildren() {
		return children;
	}

	public void setChildren(Folder[] children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
