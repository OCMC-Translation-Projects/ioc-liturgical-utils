package org.ocmc.ioc.liturgical.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class MessageUtils {
	private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);
	
	public final static String domain = "https://hooks.slack.com/services/";
	
	/**
	 * 
	 * @param token use an application specific token
	 * @param message whatever you want to convey
	 * @return the response made by the server
	 */
	public static String sendMessage(
			String token
			, String message
			) {
		String result = "";
		HttpResponse<String> response = null;
		try {
			JsonObject body = new JsonObject();
			body.addProperty("text", message);
			response = Unirest.post(domain + token)
			        .header("content-type", "application/text")
			        .body(body.toString())
			        .asString();
			if (response.getStatus() == 200) {
				result = response.getBody();
			} else {
				result = response.getStatus() + ": " + response.getStatusText();
			}
		} catch (Exception e) {
			result = e.getMessage();
			ErrorUtils.report(logger, e);
		}
		return result;
	}

}
