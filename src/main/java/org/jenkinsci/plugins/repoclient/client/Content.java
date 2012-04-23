package org.jenkinsci.plugins.repoclient.client;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * This represents a content-item of a Maven repository repsonse with mime-type
 * application/xml.
 *
 * <pre>
 * <content>
 *   <data>
 *     <content-item>
 *       <resourceURI>https://seu75.gdc-leinf01.t-systems.com/nexus/content/groups/clusterfc/commons-beanutils/commons-beanutils/maven-metadata.xml.md5</resourceURI>
 *       <relativePath>/groups/clusterfc/commons-beanutils/commons-beanutils/maven-metadata.xml.md5</relativePath>
 *       <text>maven-metadata.xml.md5</text> <leaf>true</leaf>
 *       <lastModified>2012-04-22 20:08:56.0 CEST</lastModified>
 *       <sizeOnDisk>33</sizeOnDisk>
 *     </content-item>
 *     <!-- ... -->
 *   </data>
 * </content>
 * </pre>
 * 
 * @author mrumpf
 */

@XmlRootElement
public class Content {
	private List<ContentItem> contentitems;

	public List<ContentItem> getContentItems() {
		return contentitems;
	}

	@XmlElementWrapper(name = "data")
	@XmlElement(name = "content-item")
	public void setContentItems(List<ContentItem> contentitems) {
		this.contentitems = contentitems;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Content[");
		sb.append(contentitems);
		sb.append("]");
		return sb.toString();
	}
}
