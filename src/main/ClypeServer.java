package main;

import data.*;

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
	 * Initializes ClypeServer instance variables
	 * @param port The port the server is hosted on
	 */
	public ClypeServer(int port) {
		this.port = port;
		this.dataToSendToClient = null;
		this.dataToReceiveFromClient = null;
	}

	/**
	 * Defaults {@link #port} to {@link #defaultPort}
	 */
	public ClypeServer() {
		this(defaultPort);
	}
	
	/**
	 * Starts the server
	 */
	public void start() {
		
	}
	
	/**
	 * Receives data from client
	 */
	public void receiveData() {
		
	}
	
	/**
	 * Sends data to client
	 */
	public void sendData() {
		
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
