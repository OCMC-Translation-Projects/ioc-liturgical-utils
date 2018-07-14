package org.ocmc.ioc.liturgical.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class NlpUtilsTest {

	@Test
	public void test() {
		String s = "Behold, the days come, saith the LORD, that I will make a new covenant with the house of Israel, and with the house of Judah:";
//		String t = "Behold, the days come,” says the LORD, “that I will make a new covenant with the house of Israel, and with the house of Judah:";
		String t = "Therefore howl ye for Moab on all sides; cry out against the shorn men [in] a gloomy place. I will weep for thee,";
		int d = NlpUtils.getLevensteinDistance(s, t);
		System.out.println(d);
		assertTrue(d > 0);
	}

}
