package test;
import main.ClypeClient;

public class TestClypeClient {
	public static void main(String[] args) {
		ClypeClient cc1 = new ClypeClient("Will");
		cc1.start();
		//LISTUSERS DONE
		System.out.println();
		
		ClypeClient cc2 = new ClypeClient("Will");
		cc2.start();
		//SENDFILE dne.txt DONE	
		System.out.println();
		
		ClypeClient cc3 = new ClypeClient("Will");
		cc3.start();
		//SENDFILE document.txt DONE
		System.out.println();
		
		ClypeClient cc4 = new ClypeClient("Will");
		cc4.start();
		//Hello! DONE
		System.out.println();
	}
}