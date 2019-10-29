package data;

import java.io.Serializable;
import java.util.Date;

/**
 * Abstract class that is parent to MessageClypeData and FileClypeData and contains basic information about transferred data
 * 
 * @author Will Dunklin
 *
 */
public abstract class ClypeData implements Serializable{

	/**
	 * Unique identifier for ClypeData during serialization
	 */
	private static final long serialVersionUID = 2118161826944087701L;
	/**
	 * The name of the sending user
	 */
	private String userName;
	/**
	 * The type of data being sent
	 */
	private int type;
	/**
	 * The time and date the data was sent
	 */
	private Date date;
	
	/**
	 * Constant variables representing the type values
	 */
	public static final int list = 0, exit = 1, file = 2, text = 3;
	
	/**
	 * Initializes ClypeData instance variables
	 * @param userName The name of the sending user
	 * @param type The type of data being sent
	 */
	ClypeData(String userName, int type) {
		this.userName =  userName;
		this.type = type;
		this.date = new Date();
	}
	
	/**
	 * Defaults {@link #userName} to "Anon"
	 * @param type The type of data that is getting sent
	 */
	ClypeData(int type) {
		this("Anon", type);
	}
	
	/**
	 * Defaults {@link #userName} to "Anon"
	 * and {@link #type} to exit
	 */
	ClypeData() {
		this(exit);
	}

	/**
	 * Gets the name of the sending user
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Gets the type
	 * @return type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the type of data that is getting sent
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Abstract method implemented in MessgaeClypeData and FileClypeData
	 * Returns a String containing the data of a message or file
	 */
	public abstract String getData();
	
	/**
	 * Abstract method implemented in MessgaeClypeData and FileClypeData
	 * Returns a String containing the data of a message or file that has been encrypted
	 * @param key The decryption key
	 */
	public abstract String getData(String key);
	
	/**
	 * A method that encrypts a given String with an encryption key using the Vignère cipher
	 * @param inputStringToEncrypt The input String
	 * @param key The encryption key
	 * @return encryptedText The encrypted String
	 */
	protected static String encrypt(String inputStringToEncrypt, String key) {
		String encryptedText = "";
		int i = 0;
		char[] k = key.toCharArray();
		for(char m : inputStringToEncrypt.toCharArray()) {
			if(Character.isLetter(m)) {
				char encrypted = (char) encryptChar(m, k[i % k.length]);
				encryptedText += (Character.isUpperCase(m) ? Character.toUpperCase(encrypted): encrypted);
				++i;
			} else {
				encryptedText += m;
			}
		}
		return encryptedText;
	}
	
	/**
	 * A method that decrypts a given String with an decryption key using the Vignère cipher
	 * @param inputStringToDecrypt The input String
	 * @param key The decryption key
	 * @return decryptedText The decrypted String
	 */
	protected static String decrypt(String inputStringToDecrypt, String key) {
		String decryptedText = "";
		int i = 0;
		char[] k = key.toCharArray();
		for(char c : inputStringToDecrypt.toCharArray()) {
			if(Character.isLetter(c)) {
				char decrypted = (char) decryptChar(c, k[i % k.length]);
				decryptedText += (Character.isUpperCase(c) ? Character.toUpperCase(decrypted): decrypted);
				++i;
			} else {
				decryptedText += c;
			}
		}
		return decryptedText;
	}
	
	/**
	 * Encrypts a specific character using the Vignère cipher algorithm
	 * @param m The input character getting encrypted
	 * @param k The key character
	 * @return result of the Vignère cipher encryption for m and k
	 */
	private static int encryptChar(char m, char k) {
		return ((letterToInt(m) + letterToInt(k)) % 26) + 97;
	}
	
	/**
	 * Decrypts a specific character using the Vignère cipher algorithm
	 * @param m The input character getting decrypted
	 * @param k The key character
	 * @return The result of the Vignère cipher decryption for m and k
	 */
	private static int decryptChar(char m, char k) {
		int result = (letterToInt(m) - letterToInt(k)) % 26;
		if(result < 0)
			result += 26;
		return result + 97;
	}
	
	/**
	 * Converts a letter to its position in the alphabet
	 * @param c Character being converted
	 * @return The converted letter
	 */
	private static int letterToInt(int c) {
		if(c >= 65 && c < 91)
			return c - 65;
		if(c >= 97 && c < 123)
			return c - 97;
		return 0;
	}
}
