package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Class that handles file messages that get transferred between CypeClients via the ClypeServer
 * 
 * @author Will Dunklin
 *
 */
public class FileClypeData extends ClypeData {

	/**
	 * The name of the file being sent
	 */
	private String fileName;
	/**
	 * The String representation of the contents being sent
	 */
	private String fileContents;
	
	/**
	 * Initializes FileClypeData instance variables
	 * @param userName The name of the sending user
	 * @param fileName The name of the file being sent
	 * @param type The type of data being sent
	 */
	public FileClypeData(String userName, String fileName, int type) {
		super(userName, type);
		this.fileName = fileName;
		this.fileContents = null;
	}
	
	/**
	 * Defaults {@link #userName} to be "Anon"
	 * and {@link #type} to 2
	 */
	public FileClypeData() {
		super("Anon", 2);
	}
	
	/**
	 * Sets the name of the file being sent
	 * @param fileName The name of the file being sent
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Gets the name of the file being sent
	 * @return fileName 
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets the String representation of the contents being sent
	 * @return fileContents
	 */
	public String getData() {
		return this.fileContents;
	}
	
	/**
	 * Gets the String representation of the contents being sent and decrypts it
	 * @param key The decryption key
	 * @return fileContents
	 */
	public String getData(String key) {
		return decrypt(this.fileContents, key);
	}
	
	/**
	 * Master class to writeReadContents containing both encrypted and plain options
	 * @param key The encryption key (null for no decryption)
	 * @throws IOException Throws exception from BufferedReader when a line is not read correctly
	 */
	private void readFile(String key) throws IOException{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileName), "UTF-8"));
			String contents = "";
			String line = "";
			
			while (line != null) {
				line = br.readLine();
				if(line != null)
					contents += line + System.lineSeparator();
			}
			
			if(key == null)
				this.fileContents = contents;
			else
				this.fileContents = encrypt(contents, key);
			
			br.close();
		} catch(FileNotFoundException fnfe) {
			System.err.println("Could not find file");
		}
	}
	
	/**
	 * Reads the contents of the file and does not encrypt
	 * @throws IOException Throws exception from BufferedReader when a line is not read correctly
	 */
	public void readFileContents() throws IOException {
		readFile(null);
	}
	
	/**
	 * Reads the contents of the file and does encrypt using a key
	 * @param key The encryption key
	 * @throws IOException Throws exception from BufferedReader when a line is not read correctly
	 */
	public void readFileContents(String key) throws IOException {
		readFile(key);
	}

	/**
	 * Master class to writeFileContents containing both decrypted and plain options
	 * @param key The decryption key (null for no decryption)
	 */
	private void writeFile(String key) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.fileName), "UTF-8"));

			if(key == null)
				bw.write(this.fileContents);
			else
				bw.write(decrypt(this.fileContents, key));
			
			bw.close();
		} catch(IOException ioe) {
			System.err.println("Could not write file");
		}
	}
	
	/**
	 * Writes a file containing the fileContents and does not decrypt
	 */
	public void writeFileContents() {
		writeFile(null);
	}
	
	/**
	 * Writes a file containing the fileContents and does decrypt using a key
	 * @param key The decryption key
	 */
	public void writeFileContents(String key) {
		writeFile(key);
	}

	/**
	 * Generates unique hash based on states of instance variables
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileContents == null) ? 0 : fileContents.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
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
		FileClypeData other = (FileClypeData) obj;
		if (fileContents == null) {
			if (other.fileContents != null)
				return false;
		} else if (!fileContents.equals(other.fileContents))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		return true;
	}

	/**
	 * Prints class and instance variables
	 */
	@Override
	public String toString() {
		return "FileClypeData [fileName=" + fileName + ", fileContents=" + fileContents + ", getFileName()="
				+ getFileName() + ", getData()=" + getData() + ", hashCode()=" + hashCode() + ", getUserName()="
				+ getUserName() + ", getType()=" + getType() + ", getDate()=" + getDate() + "]";
	}
	
}
