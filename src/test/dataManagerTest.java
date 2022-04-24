/**
 * Tests for the data manager
 */

package test;

// Imports
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import app.dataManager;
import app.fileManager;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class dataManagerTest {
	// Universal globals for test
	private static final String testUID = "test-user-2";
	private static final char[] testPassword = {'p','a','s','s','w','o','r','d'};
	private static final String testString = "Hello World!";
	
	// Get docs test
	@Test
	@Order(1)
	void testGetDocs() throws Exception {
		fileManager.createFile(testUID, testPassword);
		JSONObject testDocs = new JSONObject();
		JSONObject docs = dataManager.getDocs(testUID);
		assertTrue(docs.equals(testDocs));
	}
	
	// Test get available ID
	@Test
	@Order(2)
	void testGetNewIDEmpty() throws IOException, ParseException{
		assertTrue(dataManager.getAvailableID(testUID) == 0);
	}
	
	// Add a document test
	@Test
	@Order(3)
	void testAddDoc() throws IOException, ParseException {
		JSONObject docs = dataManager.getDocs(testUID);
		dataManager.encryptDoc(docs, dataManager.getAvailableID(testUID), testPassword, testString);
		assertTrue(docs.size() == 1);
		dataManager.reloadDocs(docs, testUID);
		dataManager.encryptDoc(docs, dataManager.getAvailableID(testUID), testPassword, "Hello World! Again!");
		dataManager.reloadDocs(docs, testUID);
		assertTrue(docs.size() == 2);
	}
	
	// Get an available ID test
	@Test
	@Order(4)
	void testGetNewIDWithOthers() throws IOException, ParseException {
		assertTrue(dataManager.getAvailableID(testUID) == 2);
	}
	
	// Read a document test
	@Test
	@Order(5)
	void testReadDoc() throws Exception{
		JSONObject docs = dataManager.getDocs(testUID);
		assertTrue(dataManager.decryptDoc(docs, 0, testPassword).equals(testString));
	}
	
	// Test deleting a document
	@Test
	@Order(6)
	void testDeleteDoc() throws Exception{
		JSONObject docs = dataManager.getDocs(testUID);
		dataManager.deleteDoc(docs, 1);
		dataManager.reloadDocs(docs, testUID);
		assertTrue(dataManager.getAvailableID(testUID) == 1);
	}
	
	// Clean up after testing
	@AfterAll
	@Order(7)
	static void cleanup() throws IOException {
		fileManager.deleteFile(testUID);
	}
}