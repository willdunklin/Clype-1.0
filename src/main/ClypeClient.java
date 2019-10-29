package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import data.ClypeData;
import data.FileClypeData;
import data.MessageClypeData;

/**
 * Client class for the Clype program. 
 * 
 * Communicates with the central Clype server in order to communicate with other Clients
 * 
 * @author Will Dunklin
 *
 */
public class ClypeClient {
	/**
	 * The name of the client user
	 */
	private String userName;
	/**
	 * The name of the computer representing the server
	 */
	private String hostName;
	/**
	 * The port the server is hosted under
	 */
	private int port;
	/**
	 * Boolean signifier showing if the connection is closed
	 */
	private boolean closeConnection;
	/**
	 * Data to be sent to the server
	 */
	private ClypeData dataToSendToServer;
	/**
	 * Data to be received from the server
	 */
	private ClypeData dataToReceiveFromServer;
	/**
	 * The default port is 7000
	 */
	private static final int defaultPort = 7000;
	/**
	 * Input reader to read client user's instructions
	 */
	private static Scanner inFromStd;
	/**
	 * Secure key used to encrypt data using the Vignère cipher
	 */
	private static final String key = "WSPLXZXJQSMCACZYCQDUFSBWJAFUMYDZVPMPX";
	/**
	 * Receives serialized data from ClypeServer
	 */
	private ObjectInputStream inFromServer;
	/**
	 * Sends serialized data to ClypeServer
	 */
	private ObjectOutputStream outToServer;
	
	/**
	 * Initializes ClypeClient instance variables
	 * @param userName The name of the client user
	 * @param hostName The name of the computer representing the server
	 * @param port The port the server is hosted on
	 * @throws IllegalArgumentException If the input data is malformatted
	 */
	public ClypeClient(String userName, String hostName, int port) throws IllegalArgumentException {
		if(userName == null || hostName == null || port < 1024)
			throw new IllegalArgumentException("The ClypeClient cannot be intitalized with those vairables");
		this.userName = userName;
		this.hostName = hostName;
		this.port = port;
		this.closeConnection = false;
		this.dataToSendToServer = null;
		this.dataToReceiveFromServer = null;
		this.inFromServer = null;
		this.outToServer = null;
	}

	/**
	 * Defaults {@link #port} to the {@link #defaultPort}
	 * @param userName The name of the client user
	 * @param hostName The name of the computer representing the server
	 */
	public ClypeClient(String userName, String hostName) {
		this(userName, hostName, defaultPort);
	}

	/**
	 * Defaults {@link #port} to {@link #defaultPort}
	 * and {@link #hostName} to "localhost"
	 * @param userName The name of the client user
	 */
	public ClypeClient(String userName) {
		this(userName, "localhost");
	}
	
	/**
	 * Defaults {@link #port} to the defaultPort,
	 * {@link #hostName} name to "localhost"
	 * and {@link #userName} to "Anon"
	 */
	public ClypeClient() {
		this("Anon");
	}
	
	/**
	 * Main method called on start
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		ClypeClient client;
		if(args.length == 0) {
			client = new ClypeClient();
		} else {
			String[] part1 = args[0].split("@");
			if(part1.length == 1) {
				client = new ClypeClient(part1[0]);
			} else {
				String[] part2 = part1[1].split(":");
				if(part2.length == 1) {
					client = new ClypeClient(part1[0], part2[0]);
				} else {
					try {
						client = new ClypeClient(part1[0], part2[0], Integer.parseInt(part2[1]));
					} catch(NumberFormatException nfe) {
						throw new NumberFormatException("Port entered incorrectly.");
					}
				}
			}
		}
		client.start();
	}
	
	/**
	 * Starts the client
	 */
	public void start() {
		try {
			System.out.println("Welcome to Clype 1.0");
			Socket server = new Socket(hostName, port);
			outToServer = new ObjectOutputStream(server.getOutputStream());
			inFromServer = new ObjectInputStream(server.getInputStream());
			inFromStd = new Scanner(System.in);
			while(!closeConnection) {
				readClientData();
				sendData();
				receiveData();
				printData();
			}
			server.close();
			outToServer.close();
			inFromServer.close();
			inFromStd.close();
		} catch(UnknownHostException uhe) {
			System.err.println("Unknown host cannot connect.");
		} catch(IOException ioe) {
			System.err.println("An error occurred.");
		}
	}
	
	/**
	 * Sends data to the server
	 */
	public void sendData() {
		try {
			outToServer.writeObject(dataToSendToServer);
		} catch (IOException ioe) {
			System.err.println("An error occurred.");
		}
	}
	
	/**
	 * Receives data from the server
	 */
	public void receiveData() {
		try {
			dataToReceiveFromServer = (ClypeData) inFromServer.readObject();
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Class was not found.");
		} catch (IOException ioe) {
			System.err.println("An error occurred.");
		}
	}
	
	/**
	 * Prints the collected data
	 */
	public void printData() {
		if(dataToReceiveFromServer != null && dataToReceiveFromServer.getData() != null && dataToReceiveFromServer.getType() != ClypeData.exit) {
			if(dataToReceiveFromServer.getType() == ClypeData.text)
				System.out.print(dataToReceiveFromServer.getUserName() + ": ");
			System.out.println(dataToReceiveFromServer.getData(key));
		}
	}
	
	/**
	 * Reads data entered from the client user
	 */
	public void readClientData() {
		String line = null;
		if(inFromStd.hasNext())
			line = inFromStd.nextLine();
		Scanner lineScan = new Scanner(line);
		
		String token = null;
		if(lineScan.hasNext())
			token = lineScan.next();
		
		if(token.equals("DONE")) {
			closeConnection = true;
			dataToSendToServer = new MessageClypeData(userName, "", ClypeData.exit);
		}
		else if(token.equals("SENDFILE")) {
			if(lineScan.hasNext()) {
				String fileName = lineScan.next();
				dataToSendToServer = new FileClypeData(userName, fileName, ClypeData.file);
				try {
					((FileClypeData) dataToSendToServer).readFileContents(key);
				} catch(IOException ioe){
					System.err.println("An error occurred.");
				}
			}
		}
		else if(token.equals("LISTUSERS")) {
			dataToSendToServer = new MessageClypeData(userName, userName, key, ClypeData.list);
		}
		else {
			dataToSendToServer = new MessageClypeData(userName, line, key, ClypeData.text);
		}
		lineScan.close();
	}

	/**
	 * Gets the name of the client user
	 * @return userName  
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Gets the name of the computer representing the server
	 * @return hostName 
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Gets the port the server is hosted under
	 * @return port 
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Generates unique hash based on states of instance variables
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (closeConnection ? 1231 : 1237);
		result = prime * result + ((dataToReceiveFromServer == null) ? 0 : dataToReceiveFromServer.hashCode());
		result = prime * result + ((dataToSendToServer == null) ? 0 : dataToSendToServer.hashCode());
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + port;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		ClypeClient other = (ClypeClient) obj;
		if (closeConnection != other.closeConnection)
			return false;
		if (dataToReceiveFromServer == null) {
			if (other.dataToReceiveFromServer != null)
				return false;
		} else if (!dataToReceiveFromServer.equals(other.dataToReceiveFromServer))
			return false;
		if (dataToSendToServer == null) {
			if (other.dataToSendToServer != null)
				return false;
		} else if (!dataToSendToServer.equals(other.dataToSendToServer))
			return false;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		if (port != other.port)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	/**
	 * Prints class and instance variables
	 */
	@Override
	public String toString() {
		return "ClypeClient [userName=" + userName + ", hostName=" + hostName + ", port=" + port + ", closeConnection="
				+ closeConnection + ", dataToSendToServer=" + dataToSendToServer + ", dataToReceiveFromServer="
				+ dataToReceiveFromServer + ", getUserName()=" + getUserName() + ", getHostName()=" + getHostName()
				+ ", getPort()=" + getPort() + ", hashCode()=" + hashCode() + "]";
	}	
	    
}
