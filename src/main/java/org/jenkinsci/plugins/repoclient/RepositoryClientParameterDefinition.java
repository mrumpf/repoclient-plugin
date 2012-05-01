package org.jenkinsci.plugins.repoclient;

import hudson.Extension;
import hudson.model.ParameterValue;
import hudson.model.SimpleParameterDefinition;
import hudson.model.Hudson;
import hudson.model.ParameterDefinition;
import hudson.model.StringParameterValue;
import hudson.util.CopyOnWriteList;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jenkinsci.plugins.repoclient.client.MavenRepositoryClient;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.Exported;

public class RepositoryClientParameterDefinition extends
		SimpleParameterDefinition {

	private static final Logger logger = Logger
			.getLogger(RepositoryClientParameterDefinition.class);

	private final String groupId;
	private final String repoName;
	private final String artifactId;
	private final String pattern;

	@DataBoundConstructor
	public RepositoryClientParameterDefinition(String artifactId,
			String description, String repoName, String groupId, String pattern) {
		super(groupId + "." + artifactId, description);
		this.repoName = repoName;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.pattern = pattern;
	}

	@Override
	public ParameterDefinition copyWithDefaultValue(ParameterValue defaultValue) {
		if (defaultValue instanceof StringParameterValue) {
			StringParameterValue value = (StringParameterValue) defaultValue;
			return new RepositoryClientParameterDefinition("", getName(), "",
					value.value, getDescription());
		} else {
			return this;
		}
	}

	@Exported
	public List<String> getChoices() {
		Repository r = DESCRIPTOR.getRepo(repoName);
		List<String> versions = null;
		if (r != null) {
			versions = MavenRepositoryClient.getVersions(r.getBaseurl(),
					groupId, artifactId, r.getUsername(), r.getPassword());
		}
		return versions;
	}

	@Exported
	public String getArtifactId() {
		return artifactId;
	}

	@Exported
	public String getRepoName() {
		return repoName;
	}

	@Exported
	public String getGroupId() {
		return groupId;
	}

	@Exported
	public String getPattern() {
		return pattern;
	}

	@Override
	public ParameterValue createValue(StaplerRequest req, JSONObject jo) {
		return new RepositoryClientParameterValue(repoName, groupId,
				artifactId, jo.getString("value"), pattern,
				DESCRIPTOR.getRepo(repoName));
	}

	@Override
	public ParameterValue createValue(String version) {
		// this should never be called
		throw new RuntimeException("Not implemented");
	}

	@Extension
	public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

	public static class DescriptorImpl extends ParameterDescriptor {
		private final CopyOnWriteList<Repository> repos = new CopyOnWriteList<Repository>();

		public DescriptorImpl() {
			super(RepositoryClientParameterDefinition.class);
			load();
		}

		public Repository getRepo(String repoName) {
			logger.debug("getRepo(" + repoName + ")");
			Repository result = null;
			for (Repository r : repos) {
				if (repoName.equals(r.getName())) {
					if (result != null) {
						logger.warn("Repository " + repoName
								+ " found multiple times");
					}
					result = r;
				}
			}
			if (result == null) {
				logger.warn("Repository " + repoName + " not found");
			}
			return result;
		}

		public Repository[] getRepos() {
			return repos.toArray(new Repository[repos.size()]);
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) {
			if (formData.has("repo")) {
				repos.replaceBy(JSONArray.toList(formData.getJSONArray("repo"),
						Repository.class));
			} else {
				repos.clear();
			}
			save();
			return true;
		}

		public FormValidation doTestConnection(
				@QueryParameter final String baseurl,
				@QueryParameter final String username,
				@QueryParameter final String password) throws IOException,
				ServletException {
			try {
				if (MavenRepositoryClient.testConnection(baseurl, username,
						password)) {
					return FormValidation.ok("Success");
				} else {
					return FormValidation.error("Connection test failed");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Client error: " + e.getMessage(), e);
				return FormValidation.error("Client error : " + e.getMessage());
			}
		}

		@Override
		public String getDisplayName() {
			return "Maven Repository Artifact";
			// Messages.ChoiceParameterDefinition_DisplayName();
		}

		@Override
		public String getHelpFile() {
			return "/client.html";
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("RepositoryClientParameterDefinition@[");
		sb.append("name=");
		sb.append(getName());
		sb.append(", description=");
		sb.append(getDescription());
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", repoName=");
		sb.append(repoName);
		sb.append(", artifactId=");
		sb.append(artifactId);
		sb.append(" ,pattern=");
		sb.append(pattern);
		sb.append(']');
		return sb.toString();
	}
}
