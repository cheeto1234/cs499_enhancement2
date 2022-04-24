/**
 * Tests for the encryption manager
 */

package test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import app.encryptionManager;

class encryptionManagerTest {

	// Test if randomness generation is working by comparing 2 encrypted strings
	@Test
	void randomnessTest() throws Exception {
		String testString = "This is a test.";
		String testPassword = "password";
		String encryptedString1 = encryptionManager.encrypt(testString.getBytes(), testPassword.toCharArray());
		String encryptedString2 = encryptionManager.encrypt(testString.getBytes(), testPassword.toCharArray());
		assertFalse(encryptedString1 == encryptedString2);
	}

	// Test if a string is able to be decrypted by the manager
	@Test
	void decryptabilityTest() throws Exception {
		String testString = "This is a test.";
		String testPassword = "password";
		String encryptedString = encryptionManager.encrypt(testString.getBytes(), testPassword.toCharArray());
		assertFalse(encryptedString == testString);
		String decryptedString = encryptionManager.decrypt(encryptedString, testPassword.toCharArray());
		assertTrue(decryptedString.equals(testString));
	}
	
	// Test if a string is attempted to be decrypted with an incorrect password
	@Test
	void wrongPasswordTest() throws Exception {
		String testString = "This is a test.";
		String testPassword = "password";
		String testIncorrectPassword = "thisIsNotThePassword";
		String encryptedString = encryptionManager.encrypt(testString.getBytes(), testPassword.toCharArray());
		assertFalse(encryptedString == testString);
		String decryptedString = encryptionManager.decrypt(encryptedString, testIncorrectPassword.toCharArray());
		assertTrue(decryptedString == null);
	}
}
