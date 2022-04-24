/**
 * Class to edit data
 */

package app;

// Imports
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class dataManager {
	// Function to get the titles for a JSONObject
	@SuppressWarnings("unchecked")
	private static Integer[] getIDArray(JSONObject docs) {
		// Get the key set from the docs, containing all the ids
		Set<Integer> IDSet = docs.keySet();
		// Format it into an array
		String[] IDArray = new String[IDSet.size()];
		// Create an array for the integer equivalents of the values
		Integer[] convertedArray = new Integer[IDSet.size()];
		// For the set into an array
		IDSet.toArray(IDArray);
		// Iteratively turn the string ids into integers
		for(int i = 0; i<IDArray.length; i++) {
			convertedArray[i] = Integer.valueOf(IDArray[i]);
		}
		// Return the ID array
		return convertedArray;
	}
	
	// Function to get an available document ID
	public static Integer getAvailableID(String UID) throws IOException, ParseException {
		// Get the array for IDs from the file
		Integer[] IDArray = getIDArray(getDocs(UID));
		// Get the length of the array
	    int n = IDArray.length;
	    // If there are multiple elements, do a quick bubble sort to find the biggest one
	    if(n > 0) {
		    for (int i = 0; i < n - 1; i++) {
		    	for (int j = 0; j < n - i - 1; j++) {
		    		if (IDArray[j] < IDArray[j + 1]) {
		                int temp = IDArray[j];
		                IDArray[j] = IDArray[j + 1];
		                IDArray[j + 1] = temp;
		    		}
		    	}
		    }
		    // Return the next number
			return IDArray[0]+1;
	    } else {
	    	// If there are no elements, the first ID can be 0
	    	return 0;
	    }
	}
	
	// User-facing function to get all the documents as a JSONObject
	public static JSONObject getDocs(String UID) throws IOException, ParseException {
		// Get docs from file
		JSONObject docs = (JSONObject) fileManager.readFile(UID).get("Docs");
		// return them as a JSONObject
		return docs;
	}
	
	// Function to get doc based on ID
	private static String getDoc(JSONObject docs, String id) {
		// If the docs object contains the given ID
		if(docs.containsKey(id)) {
			// find and return it
			String doc = (String) docs.get(id);
			return doc;
		} else {
			// If not then return nothing
			return null;
		}
	}
	
	// User-facing function to decrypt a document
	public static String decryptDoc(JSONObject docs, int id, char[] password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// Decrypt a specified document
		String decryptedDoc = encryptionManager.decrypt(getDoc(docs, String.valueOf(id)), password);
		// Return it as a string
		return decryptedDoc;
	}
	
	// User-facing function to delete a document
	@SuppressWarnings("unchecked")
	public static void deleteDoc(JSONObject docs, int id) {
		try {
			// First, remove the selected document
			docs.remove(String.valueOf(id));
			// Next, iteratively shift all the documents left to organize the list
			for(int i=id+1; i<=docs.size(); i++) {
				// Get stringified versions of the next ID and the current position
				String strID = String.valueOf(i);
				String newID = String.valueOf(i-1);
				// Copy the contents of the next element
				String copyVal = (String) docs.get(strID);
				// Remove the next element
				docs.remove(strID);
				// Place the element at the end with the updated ID
				docs.put(newID, copyVal);
				// This will eventually make all the IDs be in order again when it has done the whole list
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Function to write all docs to file
	@SuppressWarnings("unchecked")
	private static boolean writeDocs(JSONObject newDocs, String UID) {
		try {
			// Get the file
			JSONObject docs = fileManager.readFile(UID);
			// Remove the current docs
			docs.remove("Docs");
			// Add the new docs
			docs.put("Docs",newDocs);
			// Write to file
			fileManager.writeFile(UID, docs);
			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	// User-facing function to reload documents
	public static void reloadDocs(JSONObject docs, String UID) throws IOException, ParseException {
		// Write to file and get new docs
		writeDocs(docs, UID);
		docs = getDocs(UID);
	}
	
	// Function to update a single document
	@SuppressWarnings("unchecked")
	private static boolean writeDoc(JSONObject docs, int id, String newData) {
		// Check if the docs have the specified ID
		if(docs.containsKey(String.valueOf(id))) {
			// If it does, modify the JSONObject
			docs.put(String.valueOf(id), newData);
			return true;
		} else {
			// If it doesn't return false
			return false;
		}
	}
	
	// User-facing function to update a single document
	public static void encryptDoc(JSONObject docs, int id, char[] password, String data) {
		try {
			// If it does, write the data to an encrypted String
			String encryptedData = encryptionManager.encrypt(data.getBytes(), password);
			// Replace the value in the JSON object with the new data
			docs.put(String.valueOf(id), encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
