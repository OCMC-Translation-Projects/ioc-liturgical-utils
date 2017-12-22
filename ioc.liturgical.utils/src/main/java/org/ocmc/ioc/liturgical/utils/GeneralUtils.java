package org.ocmc.ioc.liturgical.utils;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.list.SetUniqueList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A collection of static methods for various purposes.
 * @author mac002
 *
 */
public class GeneralUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(GeneralUtils.class);

    public final static String QUOTE = "\"";
	
	/**
	 * If the supplied domain is truly a domain, e.g. gr_gr_cog,
	 * ensures that all parts are lowercase except the middle
	 * which must be upper.
	 * @param domain e.g. gr_gr_cog
	 * @return e.g. gr_GR_cog
	 */
	public static String toAlwbFormat(String domain) {
		String result = domain;
		try {
			String [] parts = domain.split("_");
			if (parts.length == 3) {
				result = 
						parts[0].toLowerCase() 
						+ "_"
						+ parts[1].toUpperCase() 
						+ "_"
						+ parts[2].toLowerCase()
						;
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}

	/**
	 * Attempts to convert the string to a JsonObject
	 * by parsing the string as json.
	 * @param json json string to be made into an object
	 * @return JsonObject made from the string.  Null if parse fails.
	 */
	public static JsonObject getJsonObject(String json) {
		JsonObject result = null;
		try {
			result = new JsonParser().parse(json).getAsJsonObject();
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			result = null;
		}
		return result;
	}
	
	/**
	 * Creates a string consisting of the value repeated the number of times
	 * indicated by the size parameter.
	 * @param value value to use 
	 * @param size number of times to repeat it
	 * @return a string filled size times with the value
	 */
	public static String fillString(String value, int size) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < size; i++) {
			sb.append(value);
		}
		return sb.toString();
	}
	
	/**
	 * Pads a number
	 * @param firstChar a character to use at the start of the padded number
	 * @param width how digits the padded number should have
	 * @param value the value to be padded
	 * @return a padded number
	 */
	public static String padNumber(String firstChar, int width, int value) {
		return firstChar + String.format("%0" + width + "d", value);
	}
	
	/**
	 * Extracts the value from a key-value pair from
	 * an ares file
	 * @param line a line containing a key-value pair
	 * @return the value
	 */
	public static String valueFromPair(String line) {
		String result = null;
		try {
			if (! line.trim().startsWith("/")) {
				String [] parts = line.split("=");
				if (parts.length == 2) {
					result = parts[1].trim();
					parts = result.split("//");
					if (parts.length == 2) {
						result = parts[0];
					}
 				}
			}
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Checks an ares key-value line to see if the value is a key (i.e. a pointer)
	 * If so, it will return the value as key. 
	 * @param line line with key-value
	 * @param domain the domain
	 * @return value as key if found, otherwise returns null
	 * Example:  priest = actors_gr_GR_cog.priest, will return actors_gr_GR_cog
	 */
	public static String valueAsKeyFromPair(String line, String domain) {
		String result = valueFromPair(line);
		if (result != null) {
			result = result.trim();
			if (result.startsWith("\"")) {
				result = null;
			} else {
				if (result.length() > 3) {
					String [] parts = result.split("_"+domain); // throws domain away.  We are left with left key and right key
					if (parts.length > 1) {
						result = parts[0] + "_" + domain;
					} else {
						result = null;
					}
				} else {
					result = null;
				}
			}
		}
		return result;
	}
	
	/**
	 * Convert a Set of Strings to an Array of Strings
	 * @param set the set
	 * @return the string array
	 */
	public static String[] setToStringArray(Set<String> set) {
		return set.toArray(new String[set.size()]);
	}

	/**
	 * Convert a List of Strings to an Array of Strings
	 * @param list the list
	 * @return the string array
	 */
	public static String[] listToStringArray(List<String> list) {
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * Convert a list of lists to a two dimensional string array
	 * @param multiList - the list of lists where the 2nd dimension lists are the same size
	 * @return a multi array
	 */
	public static String[][] multiListToMultiArray(List<List<String>> multiList) {
		String[][] array = new String[multiList.size()][];
		String[] blankArray = new String[0];
		for(int i=0; i < multiList.size(); i++) {
		    array[i] = multiList.get(i).toArray(blankArray);
		}
		return array;
	}
	
	/**
	 * Combine two lists, with no duplicates
	 * @param list1 first list
	 * @param list2 second list
	 * @return combination of lists
	 */
	public static List<String> setUnique(List<String> list1, List<String> list2) {
		return SetUniqueList.decorate(ListUtils.union(list1, list2));
	}
	
	/**
	 * Converts a comma delimited String into a List&lt;String&gt;
	 * @param delimitedString string that is delimited
	 * @return the List. 
	 */
	public static List<String> stringToList(String delimitedString) {
		List<String> result = new ArrayList<String>();
		try {
			for (String s : Arrays.asList(delimitedString.split(","))) {
				result.add(s.trim());
			}
		} catch (Exception e) {
			result = new ArrayList<String>();
		}
		return result;
	}

	
	/**
	 * Normalizes the string to form NFC. 
	 * This is the canonical form used throughout the application
	 * and the database.
	 * @param s the string
	 * @return normalized string
	 */
	public static String toNfc(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFC);
	}
	/**
	 * Removes diacritics from UTF-8
	 * @param text the text
	 * @return lower case version of text with all diacritic marks removed
	 */
	public static String normalize(String text) {
		String result = text;
		try {
			while(result.length() > 1 && ! Character.isAlphabetic(result.charAt(0))) {
				if (result.length() > 1) {
					result = result.substring(1,result.length());
				}
			}
			result = Normalizer.normalize(result.toLowerCase(), Normalizer.Form.NFD);
			result = result.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Converts a List&lt;JsonObject&gt; to a JsonArray containing the JsonObject occurrences.
	 * @param list the list
	 * @return the list as JsonObjects
	 */
	public static JsonArray listToJsonArray(List<JsonObject> list) {
		JsonArray result = new JsonArray();
		for (JsonObject json : list) {
			result.add(json);
		}
		return result;
	}
	
	/**
	 * Remove leading and trailing quote from a quoted string
	 * @param s the string
	 * @return string with no leading or trailing quotes 
	 */
	public static String trimQuotes(String s) {
		String result = s;
		if (s.length() > 2) {
			if (s.startsWith(QUOTE) || s.startsWith("“")) {
				result = s.substring(1, s.length());
			}
			if (s.endsWith(QUOTE) || s.endsWith("”")) {
				result = result.substring(0, s.length()-2);
			}
		} else if (s.compareTo(QUOTE+QUOTE) == 0) {
			result = "";
		}
		return result;
	}
	
	/**
	 * Wrap the string in quotes
	 * @param s the string
	 * @return string wrapped in quotes
	 */
	public static String wrapQuotes(String s) {
		if (s.length() > 0) {
			return QUOTE + escapeQuotes(s) + QUOTE;
		} else {
			return QUOTE+QUOTE;
		}
	}
	
	
	public static String escapeQuotes(String text) {
		return text.replaceAll(QUOTE, "\\\\"+QUOTE);
	}
	
	public static String unescapeQuotes(String text) {
			return text.replaceAll("\\\\"+QUOTE,QUOTE);
	}
	
	public static String getMacAddress() {
		StringBuilder sb = new StringBuilder();
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
		} catch (Exception e) {
			sb = new StringBuilder();
			sb.append("unknown");
		}
		return sb.toString();
	}
	
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getCanonicalHostName();
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			return null;
		}
	}
	
	public static InputStream getResource(Class c, String path) {
		return c.getResourceAsStream(path);
	}
	
    public static String done(long x, long y) {
   	   int percentage = (int)(x * 100.0 / y + 0.5);
       	return String.format("%,d%% done.", percentage);
       }

	 /**
     * Computes the percentage of compression
     * @param x x
     * @param y y
     * @return compression percentage
     */
    public static String compression(int x, int y) {
    	   int percentage =  100 - (int)(x * 100.0 / y + 0.5);
        	return String.format("%,d%%", percentage);
        }

	/**
	 * For the given resource name (without a .ares extension),
	 * extract the domain portion of the resource.
	 * In other words, strip off the topic part.
	 * @param resource the resource
	 * @return the domain portion of the resource name
	 */
	public static String domainFromResource(String resource) {
		String result = "";
		try {
			String[] parts = resource.split("_");
			if (parts.length > 3) {
				result = parts[1] + "_" 
						+ parts[2] + "_"
						+ parts[3];
			} else if (parts.length == 3) {
				result = resource;
			} else {
				result = null;
			}
		} catch (Exception e) {}
		return result;
	}
	
	/**
	 * For the given resource, replace the domain portion with a new one
	 * @param resource - the resource name to be converted
	 * @param newDomain - the new domain to use
	 * @return - the converted resource
	 */
	public static String changeResourceDomain(String resource, String newDomain) {
		String result = "";
		try {
			String[] parts = resource.split("_");
			if (parts.length > 3) {
				result = parts[0] + "_" + newDomain;
			}
		} catch (Exception e) {}
		return result;
	}
	
	/**
	 * Setting a Java object to another makes a reference.
	 * So modifying one value changes the other and vice versa.
	 * This method copies the value of each string into a new list
	 * and returns that list.
	 * @param list the list to copy
	 * @return a new list that can be modified without affecting the original
	 */
	public static List<String> copy(List<String> list) {
		List<String> result = new ArrayList<String>();
		list.forEach(i -> result.add(i));
		return result;
	}


}
