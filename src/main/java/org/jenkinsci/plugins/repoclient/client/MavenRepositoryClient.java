package org.jenkinsci.plugins.repoclient.client;

import hudson.ProxyConfiguration;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.jenkinsci.plugins.repoclient.RepositoryClientParameterDefinition;

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
	private static final Logger logger = Logger
			.getLogger(RepositoryClientParameterDefinition.class);

	public static List<String> getVersions(String baseurl, String groupId,
			String artifactId, String username, String password) {

		// create a singular HttpClient object
		HttpClient client = new HttpClient();

		String url = concatUrl(baseurl, groupId, artifactId);
		setProxy(client);

		// establish a connection within 5 seconds
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		// set the default credentials
		Credentials creds = new UsernamePasswordCredentials(username, password);
		if (creds != null) {
			client.getState().setCredentials(AuthScope.ANY, creds);
		}

		// execute the method
		String responseBody = null;
		HttpMethod method = null;
		try {
			// create a method object
			method = new GetMethod(url);
			method.setRequestHeader("Accept", "application/xml");
			method.setFollowRedirects(true);

			client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();
		} catch (HttpException he) {
			System.err.println("Http error connecting to '" + url + "'");
			System.err.println(he.getMessage());
		} catch (IOException ioe) {
			System.err.println("Unable to connect to '" + url + "'");
		} finally {
			// clean up the connection resources
			if (method != null) {
				method.releaseConnection();
			}
		}

		Content c = unmarshal(responseBody);

		List<String> versions = new ArrayList<String>();
		for (ContentItem ci : c.getContentItems()) {
			String ver = ci.getText();
			// TODO: Create regex pattern for versions
			if (!ver.startsWith("maven-metadata")) {
				versions.add(ver);
			}
		}
		return versions;
	}

	private static void setProxy(HttpClient client) {
		ProxyConfiguration pc;
		try {
			pc = ProxyConfiguration.load();
			if (pc != null) {
				client.getHostConfiguration().setProxy(pc.name, pc.port);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String concatUrl(String baseurl, String groupId,
			String artifactId) {
		StringBuffer sb = new StringBuffer();
		sb.append(baseurl);
		if (!baseurl.endsWith("/")) {
			sb.append("/");
		}
		if (groupId.startsWith("/")) {
			sb.append(groupId.substring(1));
		} else {
			sb.append(groupId);
		}
		if (!groupId.endsWith("/")) {
			sb.append("/");
		}
		if (artifactId.startsWith("/")) {
			sb.append(artifactId.substring(1));
		} else {
			sb.append(artifactId);
		}
		if (!artifactId.endsWith("/")) {
			sb.append("/");
		}
		return sb.toString();
	}

	private static Content unmarshal(String xml) {
		Content content = null;
		try {
			StringReader sr = new StringReader(xml);
			JAXBContext jaxbContext = JAXBContext.newInstance(Content.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			content = (Content) jaxbUnmarshaller.unmarshal(sr);
			System.out.println(content);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return content;
	}
}
