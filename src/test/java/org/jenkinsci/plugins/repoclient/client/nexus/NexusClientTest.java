package org.jenkinsci.plugins.repoclient.client.nexus;

import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.jenkinsci.plugins.repoclient.ArtifactId;
import org.jenkinsci.plugins.repoclient.GroupId;
import org.jenkinsci.plugins.repoclient.Version;
import org.jenkinsci.plugins.repoclient.config.Configuration;

public class NexusClientTest extends TestCase {

	private Configuration createConfig() {
		Properties props = new Properties();
		props.put("mvn.repo.type", "nexus");
		props.put("mvn.repo.scheme", "https");
		props.put("mvn.repo.host", "seu75.gdc-leinf01.t-systems.com");
		props.put("mvn.repo.port", "443");
		props.put("mvn.repo.contextpath", "nexus");
		props.put("mvn.repo.name", "releases");
		props.put("mvn.repo.basefolder", "com/daimler/ams");
		props.put("mvn.repo.user", "reader");
		props.put("mvn.repo.password", "7asJdx2TOZSym7jrO0JU");
		props.put("mvn.repo.downloadbuffersize", "8192");
		return new Configuration("test", props);
	}

	public void testGetApplications() {
		NexusClient nexus = new NexusClient(createConfig());
		List<GroupId> apps = nexus.getGroupIds();
		assertTrue("Number of applications is 0: " + apps, apps.size() > 0);
	}

	public void testGetAppComponents() {
		NexusClient nexus = new NexusClient(createConfig());
		final List<GroupId> apps = nexus.getGroupIds();
		final List<ArtifactId> comps = nexus.getArtifactIds(apps
				.get(0));
		assertTrue("Number of components is 0: " + comps, comps.size() > 0);
	}

	public void testGetAppVersions() {
		NexusClient nexus = new NexusClient(createConfig());
		final List<GroupId> apps = nexus.getGroupIds();
		final List<ArtifactId> comps = nexus.getArtifactIds(apps
				.get(0));
		final List<Version> vers = nexus.getVersions(comps
				.get(0));
		assertTrue("Number of versions is 0: " + vers, vers.size() > 0);
	}

	public void testGetFiles() {
		NexusClient nexus = new NexusClient(createConfig());
		final List<GroupId> apps = nexus.getGroupIds();
		final List<ArtifactId> comps = nexus.getArtifactIds(apps
				.get(0));
		final List<Version> vers = nexus.getVersions(comps
				.get(0));
//		final List<Artifact> files = nexus.getFiles(vers
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
		NexusClient nexus = new NexusClient(config);
		final List<GroupId> apps = nexus.getGroupIds();
		System.err.println("apps: " + apps);
		final List<ArtifactId> comps = nexus.getArtifactIds(apps
				.get(0));
		System.err.println("comps: " + comps);
		final List<Version> vers = nexus.getVersions(comps
				.get(0));
		System.err.println("vers: " + vers);
//		final List<Artifact> repoFiles = nexus.getFiles(vers
//				.get(0));
//		List<Artifact> files = nexus.downloadReleasePackages(config, vers
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
		NexusClient nexus = new NexusClient(createConfig());
		final List<GroupId> apps = nexus.getGroupIds();
		final List<ArtifactId> comps = nexus.getArtifactIds(apps
				.get(0));
		final List<Version> vers = nexus.getVersions(comps
				.get(0));
//		final List<Artifact> files = nexus.getFiles(vers
//				.get(0));
//		assertTrue("Expected file size 3, but is " + files.size(),
//				files.size() == 3);
	}
}
