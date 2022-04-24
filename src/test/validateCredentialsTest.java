/**
 * Tests for the credential validator
 */

package test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import app.fileManager;
import app.validateCredentials;

class validateCredentialsTest {
	
	// Test username validation tool
	@Test
	void usernameValidTest(){
		String testUsername1 = "],[.[[..[],[..";
		String testUsername2 = "123 123 123";
		String testUsername3 = "john.doe.123";
		assertTrue(validateCredentials.formatUsername(testUsername1).equals(""));
		assertTrue(validateCredentials.formatUsername(testUsername2).equals("123123123"));
		assertTrue(validateCredentials.formatUsername(testUsername3).equals("johndoe123"));
	}
	
	// Test password validation tool
	@Test
	void passwordValidTest() {
		char[] testPassword1 = "password".toCharArray();
		char[] testPassword2 = "Password".toCharArray();
		char[] testPassword3 = "Password123".toCharArray();
		char[] testPassword4 = "Password_123".toCharArray();
		assertFalse(validateCredentials.validatePassword(testPassword1));
		assertFalse(validateCredentials.validatePassword(testPassword2));
		assertFalse(validateCredentials.validatePassword(testPassword3));
		assertTrue(validateCredentials.validatePassword(testPassword4));
	}
	
	// Test if password can be successfully 0'd
	@Test
	void passwordClearTest() {
		char[] testPassword = "Password_123".toCharArray();
		assertTrue(new String(testPassword).equals("Password_123"));
		validateCredentials.clearPassword(testPassword);
		assertFalse(new String(testPassword).equals("Password_123"));
	}
	
	// Test if login can be validated successfully
	@Test
	void validateLoginTest() throws Exception {
		String testUID = "JohnDoe";
		char[] testPassword = "password".toCharArray();
		fileManager.createFile(testUID, testPassword);
		assertTrue(validateCredentials.checkLoginSuccessful(testUID, testPassword));
		assertFalse(validateCredentials.checkLoginSuccessful(testUID, "incorrect".toCharArray()));
	}
	
	// Clean up after
	@AfterAll
	static void cleanup() throws IOException {
		fileManager.deleteFile("JohnDoe");
	}
}