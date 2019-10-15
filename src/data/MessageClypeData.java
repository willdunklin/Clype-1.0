package data;

/**
 * Class that handles text messages that get transferred between CypeClients via the ClypeServer.
 * 
 * Additionally handles the miscellaneous user input such as logging out and listing users
 *
 * @author Will Dunklin
 *
 */
public class MessageClypeData extends ClypeData {

	/**
	 * The text component of the message
	 */
	private String message;
	
	/**
	 * Initializes MessageClypeData instance variables
	 * @param userName The name of the sending user
	 * @param message The text component of the message
	 * @param type The type of data being sent
	 */
	public MessageClypeData(String userName, String message, int type) {
		super(userName, type);
		this.message = message;
	}

	/**
	 * Initializes MessageClypeData instance variables
	 * @param userName The name of the sending user
	 * @param message The text component of the message
	 * @param key The encryption key
	 * @param type The type of data being sent
	 */
	public MessageClypeData(String userName, String message, String key, int type) {
		this(userName, encrypt(message, key), type);
	}
	
	/**
	 * Defaults {@link #userName} to "Anon",
	 * {@link #message} to ""
	 * and {@link #type} to 3
	 */
	public MessageClypeData() {
		this("Anon", "", 3);
	}
	
	/**
	 * Gets the text component of the message
	 * @return message
	 */
	public String getData() {
		return this.message;
	}
	
	/**
	 * Gets the text component of the message and decrypts it
	 * @param key The decryption key
	 * @return message
	 */
	public String getData(String key) {
		return decrypt(this.message, key);
	}

	/**
	 * Generates unique hash based on states of instance variables
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	/**
	 * Checks if instance variables are equal to the tested object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageClypeData other = (MessageClypeData) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	/**
	 * Prints class and instance variables
	 */
	@Override
	public String toString() {
		return "MessageClypeData [message=" + message + ", getData()=" + getData() + ", hashCode()=" + hashCode()
				+ ", getUserName()=" + getUserName() + ", getType()=" + getType() + ", getDate()=" + getDate() + "]";
	}
	
}
