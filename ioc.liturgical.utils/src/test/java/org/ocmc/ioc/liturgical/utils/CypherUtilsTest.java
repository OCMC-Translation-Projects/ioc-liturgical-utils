package org.ocmc.ioc.liturgical.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class CypherUtilsTest {

	@Test
	public void testGetQueryRenameLibrary() {
		String s = CypherUtils.getQueryToReplaceLibrary(
				"DELETE"
				, "en_sys_nouns"
				, "en_sys_verbs"
				);
		System.out.println("testGetQueryRenameLibrary");
		System.out.println(s);
		assertTrue(s.length() > 0);
	}

	@Test
	public void testGetQueryRenameTopic() {
		String s = CypherUtils.getQueryToReplaceSpecifiedTopic(
				"DELETE"
				, "en_sys_verbs"
				, "en_uk_lash~people~"
				, "en_uk_lash~actors~"
				);
		System.out.println("testGetQueryRenameTopic");
		System.out.println(s);
		assertTrue(s.length() > 0);
	}

	@Test
	public void testGetQueryReplaceTopic() {
		String s = CypherUtils.getQueryToGloballyReplaceTopic(
				"en_uk_lash~actors~Priest"
				, "en_uk_lash~people~Priest"
				);
		System.out.println("testGetQueryReplaceTopic");
		System.out.println(s);
		assertTrue(s.length() > 0);
	}

	@Test
	public void testGetQueryReplaceLibraryAndTopic() {
		String s = CypherUtils.getQueryToReplaceLibraryAndTopic(
				"en_uk_lash"
				, "en_uk_arch"
				, "actors"
				, "people"
				);
		System.out.println("testGetQueryReplaceLibaryAndTopic");
		System.out.println(s);
		assertTrue(s.length() > 0);
	}

	@Test
	public void testGetQueryRenameKey() {
		String s = CypherUtils.getQueryToReplaceSpecifiedKey(
				"DELETE"
				, "en_sys_verbs"
				, "en_uk_lash~people~Priest"
				, "something"
				, "someone"
				);
		System.out.println("testGetQueryRenameKey");
		System.out.println(s);
		assertTrue(s.length() > 0);
	}

	@Test
	public void testGetQueryToGloballyReplaceLibrary() {
		String s = CypherUtils.getQueryToGloballyReplaceLibrary(
				"en_sys_verbs"
				, "en_sys_nouns"
				);
		System.out.println("testGetQueryToGloballyReplaceLibrary");
		System.out.println(s);
		assertTrue(s.length() > 0);
	}
}
