package org.jenkinsci.plugins.repoclient.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


public class GroupId implements Serializable, Comparable<GroupId> {
	private static final long serialVersionUID = 1L;

	private String name;
	private TreeSet<ArtifactId> artifactIds = new TreeSet<ArtifactId>();

	public GroupId(String n) {
		name = n;
	}

	public String getName() {
		return name;
	}

    public List<ArtifactId> getArtifactIds() {
		final List<ArtifactId> l = new ArrayList<ArtifactId>();
		l.addAll(artifactIds);
		return Collections.unmodifiableList(l);
	}

	public void addArtifactId(ArtifactId artifactId) {
		artifactIds.add(artifactId);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		GroupId other = (GroupId) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(GroupId o) {
		return getName().compareTo(o.getName());
	}
}
