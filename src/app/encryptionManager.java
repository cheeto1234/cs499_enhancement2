/**
 * Class to handle encryption and decryption operations 
 */

package app;


// Imports
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class encryptionManager {
	// Declare universal constants for the encryption algorithm
	private static final String encryptionAlgo = "AES/GCM/NoPadding";
    private static final int tagLength = 128;
    private static final int IVLength = 12;
    private static final int saltLength = 16;
    private static final Charset charset = StandardCharsets.UTF_8;
	
	// In order to securely encrypt the data, we must utilize a unique random number called a nonce (number-once)
	// We can generate this number using the SecureRandom library
	private static byte[] generateNonce(int byteNum) {
		// Here we will generate a byte array according to the byteNum argument
		byte[] nonce = new byte[byteNum];
		// Populate the byte array with random values
		new SecureRandom().nextBytes(nonce);
		// Return the populated byte array
		return nonce;
	}
	
	// A function to derive an AES secret key using a password and salt
	private static SecretKey generateKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
		// We can use a secret key factory to derive our secret key from the given password
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		// Here we will use a key length of 256 and an iteration count of 65536
		KeySpec keySpec = new PBEKeySpec(password, salt, 65536, 256);
		// We can now generate the key
		SecretKey secretKey = new SecretKeySpec(keyFactory.generateSecret(keySpec).getEncoded(), "AES");
		// Return the key
        return secretKey;
	}
	
	// A function to encrypt a set of data
	public static String encrypt(byte[] text, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// Generate the salt (16 byte nonce)
		byte[] salt = generateNonce(saltLength);
		// Generate the IV (12 byte nonce)
		byte[] IV = generateNonce(IVLength);
		// Get the secret key from the password
		SecretKey secretKey = generateKey(password, salt);
		// Initialize the cipher use the AES-GCM algorithm
		Cipher cipher = Cipher.getInstance(encryptionAlgo);
		// Provide the parameter spec for the cipher
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(tagLength, IV));
		// Create the ciphertext byte array
		byte[] cipherText = cipher.doFinal(text);
		// Prefix the IV and salt to the ciphertext so it can be decrypted in the future without storing the values
		byte[] cipherTextFormatted = ByteBuffer.allocate(IV.length + salt.length + cipherText.length)
				.put(IV)
				.put(salt)
				.put(cipherText)
				.array();
		// Return a base-64 encoded version of the completed ciphertext string
		return Base64.getEncoder().encodeToString(cipherTextFormatted);
	}
	
	// A function to decrypt a set of data
	public static String decrypt(String text, char[] password) {
		try {
			// First we must decode the base-64 string
			byte[] decodedString = Base64.getDecoder().decode(text);
			// We also need to get the salt and IV values from the ciphertext
			ByteBuffer decodedBuffer = ByteBuffer.wrap(decodedString);
			// First get the IV
			byte[] IV = new byte[IVLength];
			decodedBuffer.get(IV);
			// Next, the salt
			byte[] salt = new byte[saltLength];
			decodedBuffer.get(salt);
			// Finally, the ciphertext itself
			byte[] cipherText = new byte[decodedBuffer.remaining()];
			decodedBuffer.get(cipherText);
			// After deriving these values, we can get the secret key from the password and the salt
			SecretKey secretKey = generateKey(password, salt);
			// Following this, we must initialize the cipher to decrypt the data
			Cipher cipher = Cipher.getInstance(encryptionAlgo);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(tagLength, IV));
			// Finally, we can now decrypt the data
			byte[] decryptedData = cipher.doFinal(cipherText);
			return new String(decryptedData, charset);
		} catch (Exception e) {
			return null;
		}
	}
}