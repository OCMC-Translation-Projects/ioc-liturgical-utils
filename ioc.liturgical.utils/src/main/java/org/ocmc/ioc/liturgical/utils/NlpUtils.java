package org.ocmc.ioc.liturgical.utils;

import org.apache.commons.lang3.StringUtils;

public class NlpUtils {
	
	// wrapper for Apache StringUtils.getLevensteinDistance
	public static int getLevensteinDistance(CharSequence s, CharSequence t) {
		return StringUtils.getLevenshteinDistance(s, t);
	}
}
