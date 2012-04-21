package org.jenkinsci.plugins.repoclient.client.artifactory;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The FileInfo class representing file meta information. Instances of this class will automatically be
 * deserialized from JSON via the JacksonConverter.
 *
 * @see http://wiki.jfrog.org/confluence/display/RTF/Artifactory%27s+REST+API#Artifactory%27sRESTAPI-FileInfo
 *
 * @author mrumpf
 */
public class FileInfo
{
	private String uri;
	private String downloadUri;
	private String metadataUri;
	private String repo;
	private String path;
	private String remoteUrl;
	private String created;
	private String createdBy;
	private String lastModified;
	private String modifiedBy;
	private String lastUpdated;
	private String size;
	private String mimeType;
	
	private Checksums checksums;
	private Checksums originalChecksums;
	
	public static class Checksums
	{
		private String md5;
		private String sha1;

		public String getMd5() {
			return md5;
		}
		public void setMd5(String md5) {
			this.md5 = md5;
		}
		public String getSha1() {
			return sha1;
		}
		public void setSha1(String sha1) {
			this.sha1 = sha1;
		}
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDownloadUri() {
		return downloadUri;
	}

	public void setDownloadUri(String downloadUri) {
		this.downloadUri = downloadUri;
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

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Checksums getChecksums() {
		return checksums;
	}

	public void setChecksums(Checksums checksums) {
		this.checksums = checksums;
	}

	public Checksums getOriginalChecksums() {
		return originalChecksums;
	}

	public void setOriginalChecksums(Checksums originalChecksums) {
		this.originalChecksums = originalChecksums;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
