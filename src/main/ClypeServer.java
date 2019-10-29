package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import data.ClypeData;

/**
 * Class that runs Clype's internal server.
 * 
 * Communicates between one or more ClypeClients and provides the user data
 * 
 * @author Will Dunklin
 *
 */
public class ClypeServer {
	/**
	 * The port the server is hosted on
	 */
	private int port;
	/** 
	 * Boolean signifier showing if the connection is closed
	 */
	private boolean closeConnection;
	/**
	 * Data to be received from a client
	 */
	private ClypeData dataToReceiveFromClient;
	/**
	 * Data to be sent to a client
	 */
	private ClypeData dataToSendToClient;
	/**
	 * The default port is 7000
	 */
	private static final int defaultPort = 7000;
	/**
	 * Receives serialized data from ClypeClient
	 */
	private ObjectInputStream inFromClient;
	/**
	 * Sends serialized data to ClypeClient
	 */
	private ObjectOutputStream outToClient;
	
	/**
	 * Initializes ClypeServer instance variables
	 * @param port The port the server is hosted on
	 * @throws IllegalArgumentException If the port is invalid
	 */
	public ClypeServer(int port) throws IllegalArgumentException {
		if(port < 1024)
			throw new IllegalArgumentException("ClypeServer cannot be intitalized with that port");
		this.port = port;
		this.dataToSendToClient = null;
		this.dataToReceiveFromClient = null;
		this.inFromClient = null;
		this.outToClient = null;
	}

	/**
	 * Defaults {@link #port} to {@link #defaultPort}
	 */
	public ClypeServer() {
		this(defaultPort);
	}
	
	/**
	 * Main method called on start
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		ClypeServer server;
		if(args.length == 0) {
			server = new ClypeServer();
		} else {
			String[] part1 = args[0].split("@");
			try {
				server = new ClypeServer(Integer.parseInt(part1[0]));
			} catch(NumberFormatException nfe) {
				throw new NumberFormatException("Port entered incorrectly.");
			}
		}
		server.start();
	}
	
	/**
	 * Starts the server
	 */
	public void start() {
		System.out.println("Starting server");
		try {
			ServerSocket sskt = new ServerSocket(port);
			System.out.println("Waiting to accept client");
			Socket client = sskt.accept();
			System.out.println("Accepted client");
			outToClient = new ObjectOutputStream(client.getOutputStream());
			inFromClient = new ObjectInputStream(client.getInputStream());
			while(!closeConnection) {
				receiveData();
				sendData();
			}
			sskt.close();
			client.close();
			outToClient.close();
			inFromClient.close();
		} catch (IOException ioe) {
			System.err.println("An error occurred.");
		}
	}
	
	/**
	 * Receives data from client
	 */
	public void receiveData() {
		try {
			dataToReceiveFromClient = (ClypeData) inFromClient.readObject();
			dataToSendToClient = dataToReceiveFromClient;
			
			System.out.println(dataToReceiveFromClient.toString());
			
			if(dataToReceiveFromClient.getType() == ClypeData.exit)
				closeConnection = true;
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Class was not found.");
		} catch (IOException ioe) {
			if(ioe.getMessage().equals("Connection reset"))
				closeConnection = true;
			System.err.println("An error occurred.");
		}
	}
	
	/**
	 * Sends data to client
	 */
	public void sendData() {
		try {
			outToClient.writeObject(dataToSendToClient);
		} catch (IOException ioe) {
			System.err.println("An error occurred. "+ioe.getMessage());
		}
	}

	/**
	 * Gets the port the server is hosted on
	 * @return port  
	 */
	public int getPort() {
		return this.port;
	}
	
	/**
	 * Generates unique hash based on states of instance variables
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (closeConnection ? 1231 : 1237);
		result = prime * result + ((dataToReceiveFromClient == null) ? 0 : dataToReceiveFromClient.hashCode());
		result = prime * result + ((dataToSendToClient == null) ? 0 : dataToSendToClient.hashCode());
		result = prime * result + port;
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
		ClypeServer other = (ClypeServer) obj;
		if (closeConnection != other.closeConnection)
			return false;
		if (dataToReceiveFromClient == null) {
			if (other.dataToReceiveFromClient != null)
				return false;
		} else if (!dataToReceiveFromClient.equals(other.dataToReceiveFromClient))
			return false;
		if (dataToSendToClient == null) {
			if (other.dataToSendToClient != null)
				return false;
		} else if (!dataToSendToClient.equals(other.dataToSendToClient))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	/**
	 * Prints class and instance variables
	 */
	@Override
	public String toString() {
		return "ClypeServer [port=" + port + ", closeConnection=" + closeConnection + ", dataToReceiveFromClient=" + 
				dataToReceiveFromClient + ", dataToSendToClient=" + dataToSendToClient + ", hashCode()=" + hashCode() + "]";
	}
	
}
