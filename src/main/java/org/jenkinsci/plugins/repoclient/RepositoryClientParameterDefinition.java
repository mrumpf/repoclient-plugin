package org.jenkinsci.plugins.repoclient;

import hudson.Extension;
import hudson.model.ParameterValue;
import hudson.model.SimpleParameterDefinition;
import hudson.model.ParameterDefinition;
import hudson.model.StringParameterValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.Exported;

public class RepositoryClientParameterDefinition extends
		SimpleParameterDefinition {

	private final String groupId;
	private final String repoName;
	private final String artifactId;
	private final String pattern;

	@DataBoundConstructor
	public RepositoryClientParameterDefinition(String artifactId,
			String description, String repoName, String name,
			String groupId, String pattern) {
		super(name, description);
		this.repoName = repoName;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.pattern = pattern;
		System.err.println("RepositoryClientParameterDefinition: "
				+ this);
	}

	@Override
	public ParameterDefinition copyWithDefaultValue(ParameterValue defaultValue) {
		System.err.println("copyWithDefaultValue: " + defaultValue);
		if (defaultValue instanceof StringParameterValue) {
			StringParameterValue value = (StringParameterValue) defaultValue;
			return new RepositoryClientParameterDefinition("", getName(),
					"", "", value.value, getDescription());
		} else {
			return this;
		}
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
	public static class DescriptorImpl extends ParameterDescriptor {
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

		public Repository[] getRepos() {
			return RepositoryClientWrapper.DESCRIPTOR.getRepos();
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
