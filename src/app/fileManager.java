/**
 * Class to manage data in the application
 */

package app;

// Imports
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class fileManager {
	// Global variables for dataManager
    private static final Charset charset = StandardCharsets.UTF_8;

	// Function to check if the data folder exists
	public static boolean createDirectory() {
		// Create a new file object representing a directory called "data" located in the parent directory
		File dataDir = new File("./data");
		// mkdirs() will return true if the folder was created and false if it exists
		boolean folderCreated = dataDir.mkdirs();
		// If the folder was created, notify the user
		if (folderCreated){
			return false;
		} else {
			// If the folder exists already, notify the user
			return true;
		}
	}
	
	// Function to delete a file
	public static boolean deleteFile(String UID) throws IOException {
		Boolean fileExists = new File("./data/"+UID+".json").isFile();
		if(fileExists) {
			// Get the specified file
			Path dataFile = Paths.get("./data/"+UID+".json");
			// Get the contents of the file
			String fileContent = new String(Files.readAllBytes(dataFile), charset);
			// Replace the contents of the file so it can be discarded safely
			fileContent = fileContent.replaceAll(".", "0");
			// Write it back to the file
			Files.write(dataFile, fileContent.getBytes(charset));
			// Finally, delete the file
			Files.delete(dataFile);
			return true;
		} else {
			// If the file does not exist return false
			return false;
		}
	}
	
	// Function to read in JSON file for manipulation, returns a JSONObject
	public static JSONObject readFile(String UID) throws IOException, ParseException {
		// First create a parser to parse the JSON file
        JSONParser jsonParser = new JSONParser();
        // Next, get the file
		File dataFile = new File("./data/"+UID+".json");
		// Read from the file
        FileReader fileReader = new FileReader(dataFile);
        // Parse the file and store as JSON object
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
        fileReader.close();
        return jsonObject;
	}
	
	// Function to write to the file
	public static boolean writeFile(String UID, JSONObject data) {
		try {
			// Path to the file
			File dataFile = new File("./data/"+UID+".json");
			// Get the file to write
			FileWriter fileWriter;
			fileWriter = new FileWriter(dataFile);
			// Write to the file
			fileWriter.write(data.toJSONString());
			// Close the output stream
			fileWriter.flush();
			fileWriter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Function to create file if it doesn't exist based on user provided credentials
	@SuppressWarnings("unchecked")
	public static boolean createFile(String UID, char[] password) throws Exception {
		// Create the filename and path 
		File dataFile = new File("./data/"+UID+".json");
		if(dataFile.isFile()) {
			// If the file exists, skip this part
			return true;
		} else {
			// If the file does not exist, create it
			dataFile.createNewFile();
			// Add the file data structure for later use
			JSONObject data = new JSONObject();
			data.put("UID", encryptionManager.encrypt(UID.getBytes(), password));
			JSONObject docs = new JSONObject();
			data.put("Docs", docs);
			writeFile(UID, data);
			return false;
		}
	}
}