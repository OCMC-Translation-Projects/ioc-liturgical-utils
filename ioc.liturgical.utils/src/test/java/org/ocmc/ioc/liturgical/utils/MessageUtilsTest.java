package org.ocmc.ioc.liturgical.utils;


import java.time.Instant;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageUtilsTest {
	
	private static String token = "";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		token = System.getenv("token");
	}
	
	@Test
	public void testSendMessage() {
		String response = MessageUtils.sendMessage(token, "Greetings from Junit at " + Instant.now().toString());
		System.out.println(response);
		assertTrue(response != null);
	}


}
