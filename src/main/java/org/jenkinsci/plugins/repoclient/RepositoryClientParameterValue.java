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

public class RepositoryClientParameterValue extends StringParameterValue {

	private final String groupId;
	private final String artifactId;
	private final String pattern;
	private final Repository repo;

	@DataBoundConstructor
	public RepositoryClientParameterValue(String repoName, String groupId,
			String artifactId, String version, String pattern, Repository repo) {
		super(repoName, version);
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.pattern = pattern;
		this.repo = repo;
	}

	@Override
	public BuildWrapper createBuildWrapper(AbstractBuild<?, ?> build) {
		List<String> files = MavenRepositoryClient.getFiles(repo.getBaseurl(),
				groupId, artifactId, value, repo.getUsername(),
				repo.getPassword());
		StringBuffer sb = new StringBuffer();
		for (String file : files) {
			sb.append(file);
			sb.append(',');
		}
		final String urls = sb.toString();
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
						env.put("repoclient_" + groupId + "." + artifactId
								+ "_repoName", getName());
						env.put("repoclient_" + groupId + "." + artifactId
								+ "_artifactId", artifactId);
						env.put("repoclient_" + groupId + "." + artifactId
								+ "_groupId", groupId);
						env.put("repoclient_" + groupId + "." + artifactId
								+ "_pattern", pattern);
						env.put("repoclient_" + groupId + "." + artifactId
								+ "_version", value);
						env.put("repoclient_" + groupId + "." + artifactId
								+ "_urls", urls);
					}
				};
			}
		};
	}
}
