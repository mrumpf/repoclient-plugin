package org.jenkinsci.plugins.repoclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ArtifactId implements Serializable, Comparable<ArtifactId> {
	private static final long serialVersionUID = 1L;

	private String name = null;
	private GroupId groupId = null;

	private Set<Version> versions = new TreeSet<Version>();

	public ArtifactId(GroupId gid, String n) {
		name = n;
		groupId = gid;
	}

	public String getName() {
		return name;
	}

	public List<Version> getVersions() {
		final List<Version> l = new ArrayList<Version>();
		l.addAll(versions);
		return Collections.unmodifiableList(l);
	}

	public void addVersion(Version version) {
		versions.add(version);
	}

	@Override
	public String toString() {
		return name;
	}

	public GroupId getApplication() {
		return groupId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ArtifactId other = (ArtifactId) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(ArtifactId o) {
		return getName().compareTo(o.getName());
	}
}
