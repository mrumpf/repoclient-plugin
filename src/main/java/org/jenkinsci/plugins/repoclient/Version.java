package org.jenkinsci.plugins.repoclient;

import java.io.Serializable;
import java.util.StringTokenizer;

public class Version implements Serializable, Comparable<Version> {
	private static final long serialVersionUID = 1L;
	private Integer major;
	private Integer minor;
	private Integer bugfix;
	private Integer hotfix;
	private Integer build = null;
	private ArtifactId artifactId;

	public Version(ArtifactId artId, String version) {
		artifactId = artId;
		final StringTokenizer strtok = new StringTokenizer(version.trim(), ".");
		if (strtok.countTokens() >= 4 && strtok.countTokens() <= 5) {
			major = Integer.decode(strtok.nextToken());
			minor = Integer.decode(strtok.nextToken());
			bugfix = Integer.decode(strtok.nextToken());
			hotfix = Integer.decode(strtok.nextToken());
			if (strtok.hasMoreTokens()) {
				build = Integer.decode(strtok.nextToken());
			}
		} else {
			// TODO Throw Exception
		}

	}

	public ArtifactId getComponent() {
		return artifactId;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getBugfix() {
		return bugfix;
	}

	public int getHotfix() {
		return hotfix;
	}

	public int getBuild() {
		return build;
	}

	@Override
	public String toString() {
		return major + "." + minor + "." + bugfix + "." + hotfix
				+ (build != null ? ("." + build) : "");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bugfix == null) ? 0 : bugfix.hashCode());
		result = prime * result + ((build == null) ? 0 : build.hashCode());
		result = prime * result
				+ ((artifactId == null) ? 0 : artifactId.hashCode());
		result = prime * result + ((hotfix == null) ? 0 : hotfix.hashCode());
		result = prime * result + ((major == null) ? 0 : major.hashCode());
		result = prime * result + ((minor == null) ? 0 : minor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Version other = (Version) obj;
		if (bugfix == null) {
			if (other.bugfix != null)
				return false;
		} else if (!bugfix.equals(other.bugfix))
			return false;
		if (build == null) {
			if (other.build != null)
				return false;
		} else if (!build.equals(other.build))
			return false;
		if (artifactId == null) {
			if (other.artifactId != null)
				return false;
		} else if (!artifactId.equals(other.artifactId))
			return false;
		if (hotfix == null) {
			if (other.hotfix != null)
				return false;
		} else if (!hotfix.equals(other.hotfix))
			return false;
		if (major == null) {
			if (other.major != null)
				return false;
		} else if (!major.equals(other.major))
			return false;
		if (minor == null) {
			if (other.minor != null)
				return false;
		} else if (!minor.equals(other.minor))
			return false;
		return true;
	}

	public int compareTo(Version o2) {
		int result = ((Integer) getMajor()).compareTo((Integer) o2.getMajor());
		if (result == 0) {
			result += ((Integer) getMinor()).compareTo((Integer) o2.getMinor());
		}
		if (result == 0) {
			result += ((Integer) getBugfix()).compareTo((Integer) o2
					.getBugfix());
		}
		if (result == 0) {
			result += ((Integer) getHotfix()).compareTo((Integer) o2
					.getHotfix());
		}
		if (result == 0) {
			result += ((Integer) getBuild()).compareTo((Integer) o2.getBuild());
		}
		return result;
	}
}
