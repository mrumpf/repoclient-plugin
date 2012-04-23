package org.jenkinsci.plugins.repoclient.client;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.maven.wagon.providers.http.httpclient.conn.params.ConnRoutePNames;

public class Main {

	public static void main(String[] args) {

//		Credentials creds = new UsernamePasswordCredentials("reader",
//				"7asJdx2TOZSym7jrO0JU");
		Credentials creds = new UsernamePasswordCredentials("ams-read", "dSyNlQGcxS");
		// create a singular HttpClient object
		HttpClient client = new HttpClient();

		HttpHost proxy = new HttpHost("www-le.dienste.telekom.de", 80);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		// establish a connection within 5 seconds
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		// set the default credentials
		if (creds != null) {
			client.getState().setCredentials(AuthScope.ANY, creds);
		}

		String url = "https://q4de3gsy232.gdc-leinf01.t-systems.com/artifactory/amsweb/com/daimler/ams/ppg/ppg-web:maven-metadata.xml";
		//String url = "https://seu75.gdc-leinf01.t-systems.com/nexus/content/groups/clusterfc/commons-beanutils/commons-beanutils/";
		HttpMethod method = null;

		// create a method object
		method = new GetMethod(url);
		method.setRequestHeader("Accept", "application/xml");
		method.setFollowRedirects(true);

		// execute the method
		String responseBody = null;
		try {
			client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();
		} catch (HttpException he) {
			System.err.println("Http error connecting to '" + url + "'");
			System.err.println(he.getMessage());
			System.exit(-4);
		} catch (IOException ioe) {
			System.err.println("Unable to connect to '" + url + "'");
			System.exit(-3);
		}

		// write out the request headers
		System.out.println("*** Request ***");
		System.out.println("Request Path: " + method.getPath());
		System.out.println("Request Query: " + method.getQueryString());
		Header[] requestHeaders = method.getRequestHeaders();
		for (int i = 0; i < requestHeaders.length; i++) {
			System.out.print(requestHeaders[i]);
		}

		// write out the response headers
		System.out.println("*** Response ***");
		System.out.println("Status Line: " + method.getStatusLine());
		Header[] responseHeaders = method.getResponseHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			System.out.print(responseHeaders[i]);
		}

		// write out the response body
		System.out.println("*** Response Body ***");
		System.out.println(responseBody);
		unmarshal(responseBody);

		// clean up the connection resources
		method.releaseConnection();
	}

	public static Content unmarshal(String xml) {
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
