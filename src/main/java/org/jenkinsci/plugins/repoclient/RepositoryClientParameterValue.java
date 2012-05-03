package org.jenkinsci.plugins.repoclient;

import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.StringParameterValue;
import hudson.tasks.BuildWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.repoclient.client.MavenRepositoryClient;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * 
 * @author mrumpf
 * 
 */
public class RepositoryClientParameterValue extends StringParameterValue {

	private static final String PREFIX = "repoclient_";

	private final String groupid;
	private final String artifactid;
	private final String pattern;
	private final Repository repo;

	@DataBoundConstructor
	public RepositoryClientParameterValue(String reponame, String groupid,
			String artifactid, String version, String pattern, Repository repo) {
		super(reponame, version);
		this.groupid = groupid;
		this.artifactid = artifactid;
		this.pattern = pattern;
		this.repo = repo;
		System.err.println("this=" + this);
	}

	@Override
	public BuildWrapper createBuildWrapper(AbstractBuild<?, ?> build) {
		List<String> files = MavenRepositoryClient.getFiles(repo.getBaseurl(),
				groupid, artifactid, value, repo.getUsername(),
				repo.getPassword(), pattern);
		StringBuffer sb = new StringBuffer();
		for (String file : files) {
			if (file.matches(pattern)) {
				if (sb.length() > 0) {
					sb.append(',');
				}
				sb.append(file);
			}
		}
		final String urllist = sb.toString();
		return new BuildWrapper() {
			/**
			 * This method just makes the build fail for various reasons.
			 */
			@Override
			public Environment setUp(AbstractBuild build, Launcher launcher,
					BuildListener listener) throws IOException,
					InterruptedException {
				return new Environment() {
					@Override
					public void buildEnvVars(Map<String, String> env) {
						String allurls = env.get(PREFIX + "urls");
						if (allurls == null) {
							allurls = "";
						}
						if (urllist != null) {
							if (!allurls.isEmpty()) {
								allurls += ",";
							}
							allurls += urllist;
						}
						env.put(PREFIX + "urls", allurls);

						env.put(PREFIX + groupid + "." + artifactid
								+ "_repoName", getName());
						env.put(PREFIX + groupid + "." + artifactid
								+ "_artifactid", artifactid);
						env.put(PREFIX + groupid + "." + artifactid
								+ "_groupid", groupid);
						env.put(PREFIX + groupid + "." + artifactid
								+ "_pattern", pattern);
						env.put(PREFIX + groupid + "." + artifactid
								+ "_version", value);
						env.put(PREFIX + groupid + "." + artifactid
								+ "_urls", urllist);
					}
				};
			}
		};
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("RepositoryClientParameterValue@[");
		sb.append(", reponame=");
		sb.append(getName());
		sb.append(", groupid=");
		sb.append(groupid);
		sb.append(", artifactid=");
		sb.append(artifactid);
		sb.append(" ,pattern=");
		sb.append(pattern);
		sb.append(", version=");
		sb.append(value);
		sb.append(", repo=");
		sb.append(repo);
		sb.append(" (baseurl=");
		sb.append(repo.getBaseurl());
		sb.append(", username=");
		sb.append(repo.getUsername());
		sb.append(", password=");
		sb.append(repo.getPassword());
		sb.append(')');
		sb.append(']');
		return sb.toString();
	}
}
