/**
 * Tests for the data manager
 */

package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import app.fileManager;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class fileManagerTest {
	private static final String testUID = "test-user";
	private static final char[] testPassword = {'p','a','s','s','w','o','r','d'};
	
	// Test if file can be create successfully
	@Test
	@Order(1)
	void creationTest() throws Exception {
		Path path = FileSystems.getDefault().getPath("./data", testUID+".json");
		assertFalse(Files.exists(path));
		assertFalse(fileManager.createFile(testUID, testPassword));
		assertTrue(fileManager.createFile(testUID, testPassword));
	}
	
	// Test if file can be written
	@SuppressWarnings("unchecked")
	@Test
	@Order(2)
	void writeableTest() {
		JSONObject data = new JSONObject();
		data.put("UID", testUID);
		JSONObject docs = new JSONObject();
		data.put("Docs",docs);
		docs.put(1, "random text");
		docs.put(2, "more random text");
		assertTrue(fileManager.writeFile(testUID, data));
	}
	
	// Test if file can be read
	@SuppressWarnings("unchecked")
	@Test
	@Order(3)
	void readableTest() throws IOException, ParseException {
		JSONObject testData = new JSONObject();
		testData.put("UID", testUID);
		JSONObject testDocs = new JSONObject();
		testData.put("Docs",testDocs);
		testDocs.put(1, "random text");
		testDocs.put(2, "more random text");
		JSONObject data = fileManager.readFile(testUID);
		String dataStr = data.toJSONString();
		String testDataStr = testData.toJSONString();
		assertEquals(testDataStr, dataStr);
	}
	
	// Test if file can be deleted successfully
	@Test
	@Order(4)
	void deletionTest() throws IOException {
		Path path = FileSystems.getDefault().getPath("./data", testUID+".json");
		assertTrue(Files.exists(path));
		assertTrue(fileManager.deleteFile(testUID));
	}
}