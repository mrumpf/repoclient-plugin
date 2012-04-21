package org.jenkinsci.plugins.repoclient.client.nexus;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author mrumpf
 */
public class DataContainer
{
	private List<ContentItem> data;

	public List<ContentItem> getData() {
		return data;
	}

	public void setData(List<ContentItem> contentItem) {
		this.data = contentItem;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
