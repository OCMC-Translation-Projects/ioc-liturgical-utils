package org.ocmc.ioc.liturgical.utils;

public class CypherUtils {
	
	public final static String ID_DELIMITER = "~";
	public final static String ROOT = "Root";
	
	
	/**
	 * Example: replace(n.id, 'a', 'b')
	 * @param nodeHandle e.g. n
	 * @param prop e.g. id
	 * @param from e.g. a
	 * @param to e.g. b
	 * @return a replace clause for a Cypher query
	 */
	public static String getReplaceClause(
			String nodeHandle
			, String prop
			, String from
			, String to
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("replace(");
		sb.append(nodeHandle);
		sb.append(".");
		sb.append(prop);
		sb.append(", '");
		sb.append(from);
		sb.append("', '");
		sb.append(to);
		sb.append("') ");
		return sb.toString();
	}
		

	public static String getQueryToReplaceLibrary(
			String label
			, String fromLibrary
			, String toLibrary
			) {
		StringBuffer sb = new StringBuffer();
		String replaceClause = getReplaceClause(
				"n"
				, "library"
				, fromLibrary
				, toLibrary
				);
		sb.append("match (n:");
		sb.append(label);
		sb.append(") where n.id starts with '");
		sb.append(fromLibrary);
		sb.append("set n.id = ");
		sb.append(replaceClause);
		sb.append(" '~' + n.topic + '~' + n.key ");
		sb.append(", n.library = ");
		sb.append(replaceClause);
		sb.append("return n.id");
		return sb.toString();
	}

	public static String getQueryToReplaceSpecifiedTopic(
			String label
			, String library
			, String fromTopic
			, String toTopic
			) {
		StringBuffer sb = new StringBuffer();
		String replaceClause = getReplaceClause(
				"n"
				, "topic"
				, fromTopic
				, toTopic
				);
		sb.append("match (n:");
		sb.append(label);
		sb.append(") where n.id starts with '");
		sb.append(library);
		sb.append("~' and n.topic starts with '");
		sb.append(fromTopic);
		sb.append("' ");
		sb.append("set n.id = n.library + '~' + ");
		sb.append(replaceClause);
		sb.append(" + '~' + n.key ");
		sb.append(", n.topic = ");
		sb.append(replaceClause);
		sb.append("return n.id");
		return sb.toString();
	}

	/**
	 * Uses the Root Label to match n.topic = fromTopic
	 * and changes its value as well as the topic part of
	 * the n.id.
	 * 
	 * The main use case of this method is to handle 
	 * replacement of a topic whose value is an ID
	 * from another node.
	 * 
	 * Caution: only use this if you want to change all 
	 * topic values irrespective of the value of n.library and n.key.
	 * 
	 * @param fromTopic topic from
	 * @param toTopic topic to
	 * @return cypher query to run
	 */
	public static String getQueryToGloballyReplaceTopic(
			String fromTopic
			, String toTopic
			) {
		StringBuffer sb = new StringBuffer();
		String replaceClause = getReplaceClause(
				"n"
				, "topic"
				, fromTopic
				, toTopic
				);
		sb.append("match (n:");
		sb.append(ROOT);
		sb.append(") where n.topic starts with '");
		sb.append(fromTopic);
		sb.append("' set n.id = n.library + '~' + ");
		sb.append(replaceClause);
		sb.append(" + '~' + n.key ");
		sb.append(", n.topic = ");
		sb.append(replaceClause);
		sb.append("return n.id");
		return sb.toString();
	}

	public static String getQueryToReplaceLibraryAndTopic(
			String fromLibrary
			, String toLibrary
			, String fromTopic
			, String toTopic
			) {
		StringBuffer sb = new StringBuffer();
		String replaceLibraryClause = getReplaceClause(
				"n"
				, "library"
				, fromLibrary
				, toLibrary
				);
		String replaceTopicClause = getReplaceClause(
				"n"
				, "topic"
				, fromTopic
				, toTopic
				);
		sb.append("match (n:");
		sb.append(ROOT);
		sb.append(") where n.id starts with '");
		sb.append(fromLibrary);
		sb.append("~");
		sb.append(fromTopic);
		sb.append("' set n.id = ");
		sb.append(replaceLibraryClause);
		sb.append(" + '~' + ");
		sb.append(replaceTopicClause);
		sb.append(" + '~' + n.key ");
		sb.append(", n.library = ");
		sb.append(replaceLibraryClause);
		sb.append(", n.topic = ");
		sb.append(replaceTopicClause);
		sb.append(" ");
		sb.append(getLabelRenameClause("n", fromLibrary, toLibrary));
		sb.append(" return n.id");
		return sb.toString();
	}

	
	/**
	 * Uses the Root Label to match n.library = fromLibrary
	 * and changes its value as well as the library part of
	 * the n.id.
	 * 
	 * Caution: only use this if you want to change all 
	 * key values irrespective of the other labels.
	 * @param fromLibrary from library
	 * @param toLibrary to library
	 * @return the query to replace a matching library for all docs
	 */
	public static String getQueryToGloballyReplaceLibrary(
			String fromLibrary
			, String toLibrary
			) {
		StringBuffer sb = new StringBuffer();
		String replaceLibraryClause = getReplaceClause(
				"n"
				, "library"
				, fromLibrary
				, toLibrary
				);
		sb.append("match (n:");
		sb.append(ROOT);
		sb.append(") where n.library starts with '");
		sb.append(fromLibrary);
		sb.append("' set n.id = ");
		sb.append(replaceLibraryClause);
		sb.append(" + '~' + n.topic + '~' + n.key ");
		sb.append(", n.library = ");
		sb.append(replaceLibraryClause);
		sb.append(" ");
		sb.append(getLabelRenameClause("n", fromLibrary, toLibrary));
		sb.append(" return n.id");
		return sb.toString();
	}
	
	/**
	 * For (f)-[r]-&gt;(t) renames the type of r. 
	 * @param fromNodeLabel from node label
	 * @param fromNodeId from node id
	 * @param fromType from type
	 * @param toType to type
	 * @return the query to run
	 */
	public static String getQueryToRenameRelationshipType(
			String fromNodeLabel
			, String fromNodeId
			, String fromType
			, String toType
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("match (f:");
		sb.append(fromNodeLabel);
		sb.append(" {id: '");
		sb.append(fromNodeId);
		sb.append("'})-[r:");
		sb.append(fromType);
		sb.append("]->(t) ");
		sb.append("create (f)-[r2:");
		sb.append(toType);
		sb.append("]->(t) ");
		sb.append("set r2 = r ");
		sb.append("delete r ");
		sb.append("return f.id ");
		return sb.toString();
	}
	
	/**
	 * Get the clause to replace a label
	 * on a node.  
	 * @param nodeHandle the node handle
	 * @param from the from label
	 * @param to the to label
	 * @return the Cypher to use
	 */
	public static String getLabelRenameClause(
			String nodeHandle
			, String from
			, String to
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("remove ");
		sb.append(nodeHandle);
		sb.append(":");
		sb.append(from);
		sb.append(" set ");
		sb.append(nodeHandle);
		sb.append(" :");
		sb.append(to);
		return sb.toString();
	}
	
	/**
	 * Uses the Root Label to match n.key = fromKey
	 * and changes its value as well as the key part of
	 * the n.id.
	 * 
	 * The main use case of this method is to handle 
	 * replacement of a key whose value is an ID
	 * from another node.
	 * 
	 * Caution: only use this if you want to change all 
	 * key values irrespective of the value of n.library and n.topic.
	 * 
	 * @param fromKey topic from
	 * @param toKey topic to
	 * @return cypher query to run
	 */
	public static String getQueryToGloballyReplaceKey(
			String fromKey
			, String toKey
			) {
		StringBuffer sb = new StringBuffer();
		String replaceClause = getReplaceClause(
				"n"
				, "key"
				, fromKey
				, toKey
				);
		sb.append("match (n:");
		sb.append(ROOT);
		sb.append(") where n.key starts with '");
		sb.append(fromKey);
		sb.append("' set n.id = n.library + '~' + n.topic + '~' + ");
		sb.append(replaceClause);
		sb.append(", n.key = ");
		sb.append(replaceClause);
		sb.append("return n.id");
		return sb.toString();
	}

	public static String getQueryToReplaceSpecifiedKey(
			String label
			, String library
			, String topic
			, String fromKey
			, String toKey
			) {
		StringBuffer sb = new StringBuffer();
		String replaceClause = getReplaceClause(
				"n"
				, "key"
				, fromKey
				, toKey
				);
		sb.append("match (n:");
		sb.append(label);
		sb.append(") where n.id = '");
		sb.append(library);
		sb.append("~");
		sb.append(topic);
		sb.append("~");
		sb.append(fromKey);
		sb.append("' ");
		sb.append("set n.id = ");
		sb.append("n.library + '~' + n.topic + '~' + ");
		sb.append(replaceClause);
		sb.append(", n.key = ");
		sb.append(replaceClause);
		sb.append("return n.id");
		return sb.toString();
	}

	
}
