package org.jenkinsci.plugins.repoclient.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * 
 * @author mrumpf
 *
 */
public class VersionTest {

	@Test
	public void testEmptyVersion() {
		try {
			new Version("");
		} catch (IllegalArgumentException ex) {
			// expected
		}
	}

	@Test
	public void testBlankVersion() {
		try {
			new Version("   ");
		} catch (IllegalArgumentException ex) {
			// expected
		}
	}

	@Test
	public void testNullVersion() {
		try {
			new Version(null);
		} catch (IllegalArgumentException ex) {
			// expected
		}
	}

	@Test
	public void testMajorAsLetter() {
		Version v = new Version("a");
		checkVersion(v, "0", "0", "0", "0", "a");
	}

	@Test
	public void testLongVersion() {
		Version v = new Version("1.2.3.4.5.6.7.8.9.0");
		checkVersion(v, "1", "2", "3", "4");
	}

	@Test
	public void testMajorOnly() {
		Version v = new Version("1");
		checkVersion(v, "1", "0", "0", "0");
	}

	@Test
	public void testMinorAsLetter() {
		Version v = new Version("0.b");
		checkVersion(v, "0", "0", "0", "0", "b");
	}

	@Test
	public void testMinorOnly() {
		Version v = new Version("1.2");
		checkVersion(v, "1", "2", "0", "0");
	}

	@Test
	public void testBugfixAsLetter() {
		Version v = new Version("0.1.c");
		checkVersion(v, "0", "1", "0", "0", "c");
	}

	@Test
	public void testBugfixOnly() {
		Version v = new Version("1.2.3");
		checkVersion(v, "1", "2", "3", "0");
	}

	@Test
	public void testHotfixAsLetter() {
		Version v = new Version("0.1.2.d");
		checkVersion(v, "0", "1", "2", "0", "d");
	}

	@Test
	public void testHotfixOnly() {
		Version v = new Version("1.2.3.4");
		checkVersion(v, "1", "2", "3", "4");
	}

	@Test
	public void testMajorWithBlanks() {
		Version v = new Version("  1  ");
		checkVersion(v, "1", "0", "0", "0");
	}

	@Test
	public void testTrailingDot() {
		Version v = new Version("1.2.3.");
		checkVersion(v, "1", "2", "3", "0", null);
	}

	@Test
	public void testBuildnumberDash() {
		Version v = new Version("1.2.3.4-5");
		checkVersion(v, "1", "2", "3", "4");
	}

	@Test
	public void testBlanksInside() {
		Version v = new Version(" 1. 2. 3. 4#qualifier");
		checkVersion(v, "1", "2", "3", "4", "qualifier");
	}

	@Test
	public void testQualifierDash() {
		Version v = new Version("1.2.3.4-qualifier");
		checkVersion(v, "1", "2", "3", "4", "qualifier");
	}

	@Test
	public void testQualifierHash() {
		Version v = new Version("1.2.3.4#qualifier");
		checkVersion(v, "1", "2", "3", "4", "qualifier");
	}

	@Test
	public void testQualifierAmpersand() {
		Version v = new Version("1.2.3.4&qualifier");
		checkVersion(v, "1", "2", "3", "4", "qualifier");
	}

	@Test
	public void testQualifierWithoutSeparator() {
		Version v = new Version("1.2.3.4qualifier");
		checkVersion(v, "1", "2", "3", "4", "ualifier");
	}

	@Test
	public void testDotQualifier() {
		Version v = new Version("1.2.3.4.yyy.xxx.qualifier");
		checkVersion(v, "1", "2", "3", "4", "yyy.xxx.qualifier");
	}

	@Test
	public void testCompareHotfix() {
		Version v1 = new Version("1.2.3.0.yyy.xxx.qualifier");
		Version v2 = new Version("1.2.3.4.yyy.xxx.qualifier");
		assertTrue(v1.compareTo(v2) == -1);
	}

	@Test
	public void testCompareBugfix() {
		Version v1 = new Version("1.2.0.0.yyy.xxx.qualifier");
		Version v2 = new Version("1.2.3.4.yyy.xxx.qualifier");
		assertTrue(v1.compareTo(v2) == -1);
	}

	@Test
	public void testCompareMinor() {
		Version v1 = new Version("1.0.0.0.yyy.xxx.qualifier");
		Version v2 = new Version("1.2.3.4.yyy.xxx.qualifier");
		assertTrue(v1.compareTo(v2) == -1);
	}

	@Test
	public void testCompareMajor() {
		Version v1 = new Version("0.0.0.0.yyy.xxx.qualifier");
		Version v2 = new Version("1.2.3.4.yyy.xxx.qualifier");
		assertTrue(v1.compareTo(v2) == -1);
	}

	@Test
	public void testCompareLess() {
		Version v1 = new Version("0");
		Version v2 = new Version("1");
		assertTrue(v1.compareTo(v2) == -1);
	}

	@Test
	public void testCompareGreater() {
		Version v1 = new Version("2");
		Version v2 = new Version("1");
		assertTrue(v1.compareTo(v2) == 1);
	}

	@Test
	public void testCompareSame() {
		Version v1 = new Version("1.2.3.4.yyy.xxx.qualifier");
		Version v2 = new Version("1.2.3.4.yyy.xxx.qualifier");
		assertTrue("version compare should return 0", v1.compareTo(v2) == 0);
	}

	@Test
	public void testEqual() {
		Version v = new Version("1.2.3.4.yyy.xxx.qualifier");
		assertEquals("Versions are not equal", v, v);
	}

	@Test
	public void testNotEqual() {
		Version v1 = new Version("1.2.3.4.yyy.xxx.qualifier");
		Version v2 = new Version("1.2.3.4.yyy.xxx.qualifie");
		assertNotSame("Versions are not equal", v1, v2);
	}

	@Test
	public void testSort() {
		List<Version> versions = new ArrayList<Version>();
		versions.add(new Version("0.quali"));
		versions.add(new Version("0.qualifier"));
		versions.add(new Version("1.1.qual"));
		versions.add(new Version("1.2.qual"));
		versions.add(new Version("1.2.3.4.yyy.xxx.qualifie"));
		versions.add(new Version("1.2.3.4.yyy.xxx.qualifier"));
		versions.add(new Version("2.2.0.99.96"));
		versions.add(new Version("2.2.0.99.104"));
		versions.add(new Version("2.2.0.99.110"));
		versions.add(new Version("3.0.1.0-12"));
		versions.add(new Version("3.0.1.0-14"));
		List<Version> versionsSorted = new ArrayList<Version>();
		versionsSorted.addAll(versions);
		Collections.sort(versionsSorted);
		for (int i = 0; i < versions.size(); i++) {
			assertEquals("sorted version is not the same as unsorted (sorted: "
					+ versionsSorted + ", versions: " + versions + ")",
					versions.get(i), versionsSorted.get(i));
		}
	}

	@Test
	public void testSortUnsorted() {
		List<Version> versions = new ArrayList<Version>();
		versions.add(new Version("0.quali"));
		versions.add(new Version("0.qualifier"));
		versions.add(new Version("1.1.qual"));
		versions.add(new Version("1.2.qual"));
		versions.add(new Version("1.2.3.4.yyy.xxx.qualifie"));
		versions.add(new Version("1.2.3.4.yyy.xxx.qualifier"));
		versions.add(new Version("2.2.0.99.96"));
		versions.add(new Version("2.2.0.99.104"));
		versions.add(new Version("2.2.0.99.110"));
		versions.add(new Version("3.0.1.0-12"));
		versions.add(new Version("3.0.1.0-14"));
		List<Version> versionsSorted = new ArrayList<Version>();
		versionsSorted.addAll(versions);
		Collections.shuffle(versionsSorted);

		Collections.sort(versionsSorted);
		for (int i = 0; i < versions.size(); i++) {
			assertEquals("sorted version is not the same as unsorted (sorted: "
					+ versionsSorted + ", versions: " + versions + ")",
					versions.get(i), versionsSorted.get(i));
		}
	}

	private void checkVersion(Version v, String major, String minor,
			String bugfix, String hotfix) {
		checkVersion(v, major, minor, bugfix, hotfix, null);
	}

	private void checkVersion(Version v, String major, String minor,
			String bugfix, String hotfix, String qualifier) {
		assertEquals("Major version does not match", major, v.getMajor());
		assertEquals("Minor version does not match", minor, v.getMinor());
		assertEquals("Bugfix version does not match", bugfix, v.getBugfix());
		assertEquals("Hotfix version does not match", hotfix, v.getHotfix());
		assertEquals("Qualifier does not match", qualifier, v.getQualifier());
	}
}
