package org.jenkinsci.plugins.repoclient.client;

import hudson.ProxyConfiguration;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.jenkinsci.plugins.repoclient.RepositoryClientParameterDefinition;

/**
 * 
 * @author mrumpf
 * 
 */
public class MavenRepositoryClient {
	private static final String FILES_TO_IGNORE = "^maven-metadata.*$|^archetype-catalog.*$";
	private static final String NONE = "";
	private static final Pattern PATTERN = Pattern.compile(
			"href=[\n\r ]*\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);

	private static final Logger logger = Logger
			.getLogger(RepositoryClientParameterDefinition.class);

	public static List<String> getVersions(String baseurl, String groupId,
			String artifactId, String username, String password) {

		String url = concatUrl(baseurl, groupId, artifactId);

		logger.debug("Getting versions from " + url);

		String responseBody = doHttpRequest(username, password, url);

		logger.debug("Response " + responseBody);

		List<String> versions = new ArrayList<String>();
		versions.add(NONE);

		Content c = unmarshal(responseBody);
		if (c != null) {
			for (ContentItem ci : c.getContentItems()) {
				String ver = ci.getText();
				if (!ver.matches(FILES_TO_IGNORE)) {
					versions.add(ver);
				}
			}
		} else {
			logger.warn("Falling back to HTML parsing as the Nexus XML structure was not found");
			if (responseBody != null) {
				Matcher matcher = PATTERN.matcher(responseBody);
				while (matcher.find()) {
					String ver = matcher.group(1);
					// remove trailing slash
					if (ver.endsWith("/")) {
						ver = ver.substring(0, ver.length() - 1);
					}
					// extract the version only
					if (ver.toLowerCase().startsWith("http")) {
						int idx = ver.lastIndexOf('/');
						ver = ver.substring(0, idx);
					}
					if (!"..".equals(ver) && ver.toLowerCase().indexOf("subversion") == -1) {
						versions.add(ver);
					}
				}
			}
		}

		return versions;
	}

	public static List<String> getFiles(String baseurl, String groupId,
			String artifactId, String version, String username,
			String password, String pattern) {

		String url = concatUrl(baseurl, groupId, artifactId, version);

		logger.debug("Getting files from " + url);

		String responseBody = doHttpRequest(username, password, url);

		Content c = unmarshal(responseBody);

		List<String> files = new ArrayList<String>();
		if (c != null) {
			for (ContentItem ci : c.getContentItems()) {
				String file = ci.getText();
				files.add(url + file);
			}
		}
		return files;
	}

	private static String doHttpRequest(String username, String password,
			String url) {
		HttpClient client = new HttpClient();

		setProxy(client);

		// establish a connection within 10 seconds
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		if (username != null) {
			Credentials creds = new UsernamePasswordCredentials(username,
					password);
			if (creds != null) {
				client.getState().setCredentials(AuthScope.ANY, creds);
			}
		}

		String responseBody = null;
		HttpMethod method = null;
		try {
			method = new GetMethod(url);
			method.setRequestHeader("Accept", "application/xml");
			method.setFollowRedirects(true);

			client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();
		} catch (HttpException he) {
			logger.error("Http error connecting to '" + url + "'", he);
		} catch (IOException ioe) {
			logger.error("Unable to connect to '" + url + "'", ioe);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return responseBody;
	}

	private static void setProxy(HttpClient client) {
		ProxyConfiguration pc;
		try {
			pc = ProxyConfiguration.load();
			if (pc != null) {
				logger.info("Using proxy " + pc.name + ":" + pc.port);
				client.getHostConfiguration().setProxy(pc.name, pc.port);
			}
		} catch (IOException e) {
			logger.error("Unable to determine proxy", e);
		}
	}

	public static String concatUrl(String baseurl, String groupId,
			String artifactId) {
		return concatUrl(baseurl, groupId, artifactId, "");
	}

	public static String concatUrl(String baseurl, String groupId,
			String artifactId, String version) {
		StringBuffer sb = new StringBuffer();
		sb.append(baseurl);
		if (!baseurl.endsWith("/")) {
			sb.append("/");
		}
		if (groupId.startsWith("/")) {
			sb.append(groupId.substring(1).replace('.', '/'));
		} else {
			sb.append(groupId.replace('.', '/'));
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
		if (version.startsWith("/")) {
			sb.append(version.substring(1));
		} else {
			sb.append(version);
		}
		if (!version.isEmpty() && !version.endsWith("/")) {
			sb.append("/");
		}
		return sb.toString();
	}

	private static Content unmarshal(String xml) {
		Content content = null;
		if (xml != null) {
			try {
				StringReader sr = new StringReader(xml);
				JAXBContext jaxbContext = JAXBContext
						.newInstance(Content.class);

				Unmarshaller jaxbUnmarshaller = jaxbContext
						.createUnmarshaller();
				content = (Content) jaxbUnmarshaller.unmarshal(sr);

			} catch (JAXBException e) {
				logger.error("Unmarshaling of XML data failed", e);
			}
		}
		return content;
	}

	public static boolean testConnection(String url, String username,
			String password) {

		boolean result = false;
		HttpClient client = new HttpClient();

		setProxy(client);

		// establish a connection within 10 seconds
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		if (username != null) {
			Credentials creds = new UsernamePasswordCredentials(username,
					password);
			if (creds != null) {
				client.getState().setCredentials(AuthScope.ANY, creds);
			}
		}

		HttpMethod method = null;
		try {
			method = new GetMethod(url);
			method.setRequestHeader("Accept", "application/xml");
			method.setFollowRedirects(true);

			int statusCode = client.executeMethod(method);
			result = (statusCode == HttpStatus.SC_OK);
		} catch (HttpException he) {
			logger.error("Http error connecting to '" + url + "'", he);
		} catch (IOException ioe) {
			logger.error("Unable to connect to '" + url + "'", ioe);
		} catch (Exception e) {
			logger.error("Unknown exception while connecting to '" + url + "'", e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return result;
	}
}
