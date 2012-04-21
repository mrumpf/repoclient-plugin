package org.jenkinsci.plugins.repoclient.client.nexus;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The FolderInfo class representing folder meta information.
 * 
 * @see http://wiki.jfrog.org/confluence/display/RTF/Artifactory%27s+REST+API#
 *      Artifactory%27sRESTAPI-FolderInfo
 * 
 * @author mrumpf
 */
public class ContentItem {

	private String resourceURI;
	private String relativePath;
	private String text;
	private boolean leaf;
	private String lastModified;
	private long sizeOnDisk;

	public String getResourceURI() {
		return resourceURI;
	}

	public void setResourceURI(String resourceURI) {
		this.resourceURI = resourceURI;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public long getSizeOnDisk() {
		return sizeOnDisk;
	}

	public void setSizeOnDisk(long sizeOnDisk) {
		this.sizeOnDisk = sizeOnDisk;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
