package test;

import java.io.IOException;

import data.*;

public class TestClypeData {
	public static void main(String[] args) {
		try {
			String key = "WSPLXZX";
			MessageClypeData mcd1 = new MessageClypeData("Anon", "Message", key, 3);
			System.out.println("Data for encrypted message (mcd1):" + mcd1.getData());
			System.out.println("Data for decrypted message (mcd1):" + mcd1.getData(key));
			
			System.out.println();
			System.out.println();
			
			FileClypeData fcd1 = new FileClypeData("Anon", "test.txt", 2);
			fcd1.readFileContents();
			System.out.println("Data for file test.txt (fcd1): " + fcd1.getData());
		
			System.out.println();
			
			FileClypeData fcd2 = new FileClypeData("Anon", "test.txt", 2);
			fcd2.readFileContents(key);
			System.out.println("Data for encrypted file test.txt (fcd2): " + fcd2.getData());
			System.out.println("Data for decrypted file test.txt (fcd2): " + fcd2.getData(key));

			System.out.println();
		
			fcd2.setFileName("new.txt");
			fcd2.writeFileContents(key);
			fcd2.readFileContents();
			System.out.println("Data for file new.txt (fcd2): " + fcd2.getData());

			System.out.println();

			fcd2.writeFileContents();
			fcd2.readFileContents(key);
			System.out.println("Data for encrypted file new.txt (fcd2): " + fcd2.getData());
			System.out.println("Data for decrypted file new.txt (fcd2): " + fcd2.getData(key));
		
		} catch(IOException ioe) {
			System.err.println("There was an IO error");
		}

	}
}
