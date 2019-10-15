package test;
import main.ClypeServer;

public class TestClypeServer {
	public static void main(String[] args) {
		ClypeServer cs1 = new ClypeServer(5555);
		System.out.println("ClypeServer methods (cs1): hashCode()=" + 
				cs1.hashCode());
		System.out.println();
		System.out.println();

		System.out.println("ClypeServer (cs1) toString()= \"" + cs1 + "\"");
		System.out.println();

		ClypeServer cs2 = new ClypeServer();
		
		System.out.println("ClypeServer (cs2) toString()= \"" + cs2 + "\"");
		System.out.println();
		
		ClypeServer cs3 = new ClypeServer();
		
		System.out.println("ClypeServer (cs3) toString()= \"" + cs3 + "\"");
		System.out.println();
		System.out.println();
		
		System.out.println("ClypeServer Equality test 1 (cs1/2) " +
				cs1.equals(cs2));
		System.out.println();

		System.out.println("ClypeServer Equality test 2 (cs2/3) " +
				cs3.equals(cs2));
		System.out.println();
		
	}
}
