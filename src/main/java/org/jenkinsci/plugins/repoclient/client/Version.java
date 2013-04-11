package org.jenkinsci.plugins.repoclient.client;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class provides sorting algorithms for version strings.
 * 
 * Maven:
 * <ul>
 * <li>Reference: <a
 * href="http://mojo.codehaus.org/versions-maven-plugin/version-rules.html"
 * >Version number rules</a></li>
 * <li>Pattern: MajorVersion [ . MinorVersion [ . IncrementalVersion ] ] [ -
 * BuildNumber | Qualifier ]</li>
 * </ul>
 * 
 * SemVer:
 * <ul>
 * <li>Reference: <a href="http://semver.org/">SemVer</a></li>
 * <li>Pattern: Major . Minor . Patch [ - pre-release | + build-version ]</li>
 * </ul>
 * 
 * Others:
 * <ul>
 * <li>Reference: <a href="http://semver.org/">SemVer</a></li>
 * <li>Pattern: Major . Minor . Bugfix . Hotfix [ . Buildnumber ]</li>
 * </ul>
 * 
 * This implementation can handle all the different formats. It looks for the
 * first character which is not a number and tries to parse the number before
 * that character. This proceeds recursively until the text before cannot be
 * parsed into a number. This unparsable fragment is used as the qualifier.
 * 
 * @author mrumpf
 * 
 */
public class Version implements Serializable, Comparable<Version> {
	private static final long serialVersionUID = 1L;
	private static final String ZERO_VERSION = "0.0.0.0";
	private static final int MAJOR_IDX = 0;
	private static final int MINOR_IDX = 1;
	private static final int BUGFIX_IDX = 2;
	private static final int HOTFIX_IDX = 3;
	private String original = null;
	private String version = null;
	private String qualifier = null;
	private List<String> digitStrings = new ArrayList<String>();
	private List<Integer> digits = new ArrayList<Integer>();

	/**
	 * Strip the qualifier, which is the first occurrence of a character which
	 * is not a number or a dot. The right side of this character is the
	 * qualifier and the left side is the version.
	 * 
	 * @param ver
	 *            the complete version string
	 */
	public Version(String ver) {
		if (ver == null || ver.isEmpty())
			throw new IllegalArgumentException(
					"Version must not be null or empty");

		original = stripBlanks(ver);

		String v = stripVersion(original);
		if (v == null) {
			qualifier = original;
			version = ZERO_VERSION;
		} else {
			if (v.length() < original.length()) {
				qualifier = original.substring(v.length() + 1);
				version = original.substring(0, v.length());
			}
			else {
				version = v;
			}
		}

		// split the version part into single digits
		final StringTokenizer strtok = new StringTokenizer(version, ".");
		while (strtok.hasMoreTokens()) {
			String digit = strtok.nextToken();
			digitStrings.add(digit);
			Integer val = null;
			try {
				val = Integer.valueOf(digit);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Digit must be a number");
			}
			// add the null value to indicate that the digit could not be parsed
			digits.add(val);
		}
	}

	/* private */String stripVersion(String ver) {
		// find the first character which is not a number
		// a dot is used as a separator
		int idx = -1;
		char[] chars = ver.toCharArray();
		int j = 0;
		for (char c : chars) {
			if ((c < '0' || '9' < c)) {
				idx = j;
				break;
			}
			j++;
		}

		String version = null;
		if (idx != -1 && ver.length() > idx) {
			String fragment = ver.substring(0, idx);
			try {
				Integer.valueOf(fragment);
				if (idx < ver.length()) {
					String check = stripVersion(ver.substring(idx + 1));
					if (check != null) {
						version = fragment + "." + check;
					} else {
						version = fragment;
					}
				} else {
					version = fragment;
				}
			} catch (NumberFormatException ex) {
				// expected
			}
		} else {
			version = ver;
		}
		return version;
	}

	/* private */String stripBlanks(String ver) {
		StringBuilder version = new StringBuilder();
		char[] chars = ver.toCharArray();
		for (char c : chars) {
			if (' ' != c) {
				version.append(c);
			}
		}

		return version.toString();
	}

	/**
	 * Returns the value at the specified index or 0 in case the index does not
	 * exist.
	 * 
	 * @param idx
	 *            the index of the digit array
	 * @return the major version digit or "0" (zero)
	 */
	public Integer getValue(int idx) {
		return (idx < digitStrings.size() ? digits.get(idx) : 0);
	}

	/**
	 * Returns the major version digit or "0" (zero).
	 * 
	 * @return the major version digit or "0" (zero)
	 */
	public String getMajor() {
		return (MAJOR_IDX < digitStrings.size() ? digitStrings.get(MAJOR_IDX)
				: "0");
	}

	/**
	 * Returns the minor version digit or "0" (zero).
	 * 
	 * @return the minor version digit or "0" (zero)
	 */
	public String getMinor() {
		return (MINOR_IDX < digitStrings.size() ? digitStrings.get(MINOR_IDX)
				: "0");
	}

	/**
	 * Returns the bugfix version digit or "0" (zero).
	 * 
	 * @return the bugfix version digit or "0" (zero)
	 */
	public String getBugfix() {
		return (BUGFIX_IDX < digitStrings.size() ? digitStrings.get(BUGFIX_IDX)
				: "0");
	}

	/**
	 * Returns the hotfix version digit or "0" (zero).
	 * 
	 * @return the hotfix version digit or "0" (zero)
	 */
	public String getHotfix() {
		return (HOTFIX_IDX < digitStrings.size() ? digitStrings.get(HOTFIX_IDX)
				: "0");
	}

	/**
	 * Returns the qualifier or null if no qualifier is present.
	 * 
	 * @return the qualifier or null if no qualifier is present
	 */
	public String getQualifier() {
		return qualifier;
	}

	/**
	 * Returns the version or 0.0.0.0 if there if only a qualifier (e.g.
	 * "qualifier").
	 * 
	 * @return the version or 0.0.0.0 if there if only a qualifier (e.g.
	 *         "qualifier")
	 */
	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return original;
	}

	@Override
	public int hashCode() {
		int result = 1;
		final int prime = 31;
		for (int d : digits) {
			result = prime * result + d;
		}
		result = prime * result
				+ ((qualifier == null) ? 0 : qualifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Version other = (Version) obj;
		if (this.compareTo(other) == 0) {
			result = other.getQualifier().equals(getQualifier());
		}
		return result;
	}

	/**
	 * Returns -1 when this version is smaller than the specified one, 0 when
	 * both are the same and +1 when this version is larger than the specified
	 * one.
	 * 
	 * @param ver
	 *            the version to compare
	 * @return -1, 0, +1
	 */
	public int compareTo(Version ver) {
		int result = 0;
		for (int i = 0; i < digits.size(); i++) {
			result = getValue(i).compareTo(ver.getValue(i));
			if (result != 0) {
				break;
			}
		}
		if (result == 0) {
			result = Collator.getInstance().compare(qualifier,
					ver.getQualifier());
		}
		return result;
	}
}
