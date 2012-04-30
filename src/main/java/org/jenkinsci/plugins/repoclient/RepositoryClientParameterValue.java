package org.jenkinsci.plugins.repoclient;

import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.StringParameterValue;
import hudson.tasks.BuildWrapper;

import java.io.IOException;
import java.util.Map;

import org.kohsuke.stapler.DataBoundConstructor;

public class RepositoryClientParameterValue extends StringParameterValue {

	private final String groupId;
	private final String artifactId;
	private final String pattern;
	private final Repository repo;

	@DataBoundConstructor
	public RepositoryClientParameterValue(String repoName, String groupId, String artifactId, String version, String pattern, Repository repo) {
		super(repoName, version);
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.pattern = pattern;
		this.repo = repo;
	}

    @Override
    public BuildWrapper createBuildWrapper(AbstractBuild<?, ?> build) {
    	return new BuildWrapper() {
            /**
             * This method just makes the build fail for various reasons.
             */
            @Override
            public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
                return new Environment() {
                    @Override
                    public void buildEnvVars(Map<String, String> env) {
                        env.put("repoclient.repoName",
                                getName());
                        env.put("repoclient.artifactId",
                                artifactId);
                        env.put("repoclient.groupId",
                                groupId);
                        env.put("repoclient.pattern",
                                pattern);
                        env.put("repoclient.version",
                                value);
                    }
                };
            }
        };
    }
}
