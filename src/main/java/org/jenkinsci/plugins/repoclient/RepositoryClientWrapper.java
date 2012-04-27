package org.jenkinsci.plugins.repoclient;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.CopyOnWriteList;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Repository Client Wrapper
 * 
 * @author Michael Rumpf
 */
public class RepositoryClientWrapper extends BuildWrapper {

	private static final Logger logger = Logger
			.getLogger(RepositoryClientWrapper.class);

	@Extension
	public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

	public RepositoryClientWrapper() {
	}

	@Override
	public Environment setUp(AbstractBuild build, Launcher launcher,
			BuildListener listener) throws IOException, InterruptedException {

		return new EnvironmentImpl(listener);
	}

	// private impl of Environment where we set / check the ENV data
	class EnvironmentImpl extends Environment {

		private BuildListener listener;

		public EnvironmentImpl(BuildListener listener) {

			this.listener = listener;
		}

		// gets written to the hudson console / log
		private void console(String str) {
			listener.getLogger().println("console");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see hudson.model.Environment#buildEnvVars(java.util.Map)
		 */
		@Override
		public void buildEnvVars(Map<String, String> env) {
			listener.getLogger().println("buildEnvVars");
		}
	}

	@Extension
	public static final class DescriptorImpl extends BuildWrapperDescriptor {

		private final CopyOnWriteList<Repository> repos = new CopyOnWriteList<Repository>();

		public DescriptorImpl() {
			super(RepositoryClientWrapper.class);
			load();
		}

		@Override
		public String getDisplayName() {
			return "Maven Repository Client";
		}

		public Repository[] getRepos() {
			return repos.toArray(new Repository[repos.size()]);
		}

		@Override
		public BuildWrapper newInstance(StaplerRequest req, JSONObject formData)
				throws FormException {
			return req.bindJSON(RepositoryClientWrapper.class, formData);
		}

		@Override
		public boolean isApplicable(AbstractProject<?, ?> item) {
			return true;
		}

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
	}

}
