package org.jenkinsci.plugins.repoclient.client.artifactory;

import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.jenkinsci.plugins.repoclient.ArtifactId;
import org.jenkinsci.plugins.repoclient.GroupId;
import org.jenkinsci.plugins.repoclient.Version;
import org.jenkinsci.plugins.repoclient.config.Configuration;

/**
 * 
 * @author mrumpf
 *
 */
public class ArtifactoryClientTest extends TestCase {

	private Configuration createConfig() {
		Properties props = new Properties();
		props.put("mvn.repo.type", "artifactory");
		props.put("mvn.repo.scheme", "http");
		props.put("mvn.repo.host", "q4de3gsy232.gdc-leinf01.t-systems.com");
		props.put("mvn.repo.port", "8080");
		props.put("mvn.repo.contextpath", "artifactory");
		props.put("mvn.repo.path", "releases/com/daimler/ams");
		props.put("mvn.repo.user", "ams-read");
		props.put("mvn.repo.password", "dSyNlQGcxS");
		props.put("mvn.repo.downloadbuffersize", "8192");
		return new Configuration("test", props);
	}

	public void testGetGroupIds() {
		ArtifactoryClient artifactory = new ArtifactoryClient(createConfig());
		final List<GroupId> apps = artifactory.getGroupIds();
		assertTrue("Number of GroupIds is 0: " + apps, apps.size() > 0);
	}

	public void testGetAppArtifactIds() {
		ArtifactoryClient artifactory = new ArtifactoryClient(createConfig());
		final List<GroupId> apps = artifactory.getGroupIds();
		final List<ArtifactId> comps = artifactory.getArtifactIds(apps
				.get(0));
		assertTrue("Number of ArtifactIds is 0: " + comps, comps.size() > 0);
	}

	public void testGetAppVersions() {
		ArtifactoryClient artifactory = new ArtifactoryClient(createConfig());
		final List<GroupId> apps = artifactory.getGroupIds();
		final List<ArtifactId> comps = artifactory.getArtifactIds(apps
				.get(0));
		final List<Version> vers = artifactory.getVersions(comps
				.get(0));
		assertTrue("Number of versions is 0: " + vers, vers.size() > 0);
	}

	public void testGetFiles() {
		ArtifactoryClient artifactory = new ArtifactoryClient(createConfig());
		final List<GroupId> apps = artifactory.getGroupIds();
		final List<ArtifactId> comps = artifactory.getArtifactIds(apps
				.get(1));
		final List<Version> vers = artifactory.getVersions(comps
				.get(0));
//		final List<Artifact> files = artifactory.getFiles(vers
//				.get(0));
//		assertTrue("File size 3 expected: " + files.size(), files.size() == 3);
//
//		for (Artifact repositoryFile : files) {
//			if (repositoryFile.isBinary()) {
//				assertTrue("Binary archive " + repositoryFile + " has size 0",
//						repositoryFile.getSize() > 0);
//			} else if (repositoryFile.isSource()) {
//				assertTrue("Source archive " + repositoryFile + " has size 0",
//						repositoryFile.getSize() > 0);
//			} else if (repositoryFile.isDoc()) {
//				assertTrue("Doc archive " + repositoryFile
//						+ " found in folder with less than 3 files: " + files,
//						files.size() == 3);
//			} else {
//				assertTrue("Unknown type, neither doc, source, nor bin", false);
//			}
//		}
	}

	public void testDownloadReleasePackages() {
		Configuration config = createConfig();
		ArtifactoryClient artifactory = new ArtifactoryClient(config);
		final List<GroupId> apps = artifactory.getGroupIds();
		System.err.println("apps: " + apps);
		final List<ArtifactId> comps = artifactory.getArtifactIds(apps
				.get(0));
		System.err.println("comps: " + comps);
		final List<Version> vers = artifactory.getVersions(comps
				.get(0));
		System.err.println("vers: " + vers);
//		final List<Artifact> repoFiles = artifactory.getFiles(vers
//				.get(0));
//		List<Artifact> files = artifactory.downloadReleasePackages(config, vers
//				.get(0));
//		for (int i = 0; i < files.size(); i++) {
//			final Artifact repositoryFile = repoFiles.get(i);
//			final Artifact file = files.get(0);
//			if (repositoryFile.isBinary()) {
//				assertTrue("Binary archive does not exist", file.getLocalFile().exists());
//				assertTrue("Binary archive " + repositoryFile + " has size 0",
//						repositoryFile.getSize() > 0);
//			} else if (repositoryFile.isSource()) {
//				assertTrue("Source archive does not exist", file.getLocalFile().exists());
//				assertTrue("Source archive " + repositoryFile + " has size 0",
//						repositoryFile.getSize() > 0);
//			} else if (repositoryFile.isDoc()) {
//				assertTrue("Doc archive does not exist", file.getLocalFile().exists());
//				assertTrue("Doc archive " + repositoryFile + " has size 0",
//						repositoryFile.getSize() > 0);
//			} else {
//				assertTrue("Unknown type, neither doc, source, nor bin", false);
//			}
//		}
	}

	public void testFilenames() {
		ArtifactoryClient artifactory = new ArtifactoryClient(createConfig());
		final List<GroupId> apps = artifactory.getGroupIds();
		final List<ArtifactId> comps = artifactory.getArtifactIds(apps
				.get(0));
		final List<Version> vers = artifactory.getVersions(comps
				.get(0));
//		final List<Artifact> files = artifactory.getFiles(vers
//				.get(0));
//		assertTrue("Expected file size 2, but is " + files.size(),
//				files.size() == 2);
	}
}
