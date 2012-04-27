package org.jenkinsci.plugins.repoclient;

import hudson.Extension;
import hudson.model.ParameterValue;
import hudson.model.SimpleParameterDefinition;
import hudson.model.ParameterDefinition;
import hudson.model.StringParameterValue;
import hudson.util.CopyOnWriteList;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

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
			String description, String repoName, String name, String groupId,
			String pattern) {
		super(name, description);
		this.repoName = repoName;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.pattern = pattern;
		System.err.println("RepositoryClientParameterDefinition: " + this);
	}

	@Override
	public ParameterDefinition copyWithDefaultValue(ParameterValue defaultValue) {
		System.err.println("copyWithDefaultValue: " + defaultValue);
		if (defaultValue instanceof StringParameterValue) {
			StringParameterValue value = (StringParameterValue) defaultValue;
			return new RepositoryClientParameterDefinition("", getName(), "",
					"", value.value, getDescription());
		} else {
			return this;
		}
	}

	@Exported
	public List<String> getChoices() {
		Repository r = DESCRIPTOR.getRepo(repoName);
		List<String> versions = MavenRepositoryClient.getVersions(r.getBaseurl(), groupId, artifactId, r.getUsername(), r.getPassword());
		return versions;
	}

	@Exported
	public String getArtifactId() {
		System.err.println("getArtifactId: " + artifactId);
		return artifactId;
	}

	@Exported
	public String getRepoName() {
		System.err.println("getRepoName: " + repoName);
		return repoName;
	}

	@Exported
	public String getGroupId() {
		System.err.println("getGroupId: " + groupId);
		return groupId;
	}

	@Exported
	public String getPattern() {
		System.err.println("getPattern: " + pattern);
		return pattern;
	}

	private StringParameterValue checkValue(StringParameterValue value) {
		return value;
	}

	@Override
	public ParameterValue createValue(StaplerRequest req, JSONObject jo) {
		StringParameterValue value = req.bindJSON(StringParameterValue.class,
				jo);
		value.setDescription(getDescription());
		System.err.println("createValue: " + value);
		return checkValue(value);
	}

	public StringParameterValue createValue(String value) {
		System.err.println("createValue: " + value);
		return checkValue(new StringParameterValue(getName(), value,
				getDescription()));
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
			Repository result = null;
			for (Repository r : repos) {
				if (repoName.equals(r.getName())) {
					result = r;
				}
			}
			return result;
		}

		public Repository[] getRepos() {
			return repos.toArray(new Repository[repos.size()]);
		}

		// @Override
		// public BuildWrapper newInstance(StaplerRequest req, JSONObject
		// formData)
		// throws FormException {
		// return req.bindJSON(RepositoryClientWrapper.class, formData);
		// }
		//

		// Save the form data
		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) {
			repos.replaceBy(req.bindParametersToList(Repository.class, "repo."));
			save();
			return true;
		}

		public FormValidation doTestConnection(
				@QueryParameter("baseurl") final String baseurl,
				@QueryParameter("username") final String username,
				@QueryParameter("password") final String password)
				throws IOException, ServletException {
			try {
				logger.warn("TODO");
				return FormValidation.ok("Success");
			} catch (Exception e) {
				return FormValidation.error("Client error : " + e.getMessage());
			}
		}

		@Override
		public String getDisplayName() {
			System.err.println("getDisplayName");
			return "Maven Repository Artifact - Version List";
			// Messages.ChoiceParameterDefinition_DisplayName();
		}

		@Override
		public String getHelpFile() {
			System.err.println("getHelpFile");
			return "/client.html";
		}

		private StringParameterValue checkValue(StringParameterValue value) {
			return value;
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
