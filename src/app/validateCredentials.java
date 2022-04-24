/**
 * Class to validate credentials
 */

package app;

import java.io.IOException;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class validateCredentials {
	// Function to sanitize username
	public static String formatUsername(String username) {
		// Trim off whitespace and non-alphanumeric characters
		String formattedUsername = username.trim()
				.replaceAll("[^A-Za-z0-9]", "");
		// Return the trimmed result
		return formattedUsername;
	}
	
	// Function to securely validate the password
	public static boolean validatePassword(char[] password) {
		// If password is too long or short, return false
		if (password == null || password.length < 8 || password.length > 20) {return false;}
		
		// Booleans for password containing characters
		boolean upper = false;
		boolean lower = false;
		boolean digit = false;
		boolean symbol = false;
		
		// Logic to check password for certain character types
		for (char ch : password) {
			if (Character.isUpperCase(ch)) {upper = true;} // Check if it has an uppercase
			else if (Character.isLowerCase(ch)) {lower = true;} // Check if it has a lowercase
			else if (Character.isDigit(ch)) {digit = true;} // Check if it has a digit
			else {symbol = true;} // If none of the above is true, the current character must be a symbol
			
			// Exit the loop if all conditions are met
			if (upper && lower && digit && symbol) {return true;}
		}
		
		// Return to result of validation
		return upper && lower && digit && symbol;
	}
	
	// Function to "0 out" the char array containing the password, making it more difficult for attackers to acquire it by looking at the heap
	public static void clearPassword(char[] password) {
		// Fill the array with 0s
		Arrays.fill(password, '0');
		// Try to set the array to null
		password = null;
		// Request the the system run the garbage collector, as it will remove the nullified variable
		System.gc();
	}
	
	// Function to check if password is correct by trying to decrypt the UID
	public static boolean checkLoginSuccessful(String UID, char[] password) throws IOException, ParseException {
		JSONObject data = fileManager.readFile(UID);
		// First the UID field
		String UIDString = (String) data.get("UID");
		// Try to decrypt it and see if it matches the current UID
		if(encryptionManager.decrypt(UIDString, password) != null) {
			return true;
		} else {
			return false;
		}
	}
}
