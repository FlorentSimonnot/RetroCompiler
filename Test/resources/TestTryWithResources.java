import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

public class TestTryWithResources {

	static String readFirstLineFromFile(String path) throws IOException {
	    try(BufferedReader br = new BufferedReader(new FileReader("main/src/tests/resources/TestTryWithResources.java"))){
	        return br.readLine();
	    }
	}
	
	static void multipleTryWithResources() throws IOException {
		try(var socket = new Socket();
			var inputStream = new DataInputStream(socket.getInputStream()); 
			var outputStream = new DataOutputStream(socket.getOutputStream());
		){
			System.out.println("bouh");
		}
	}
	
	static void tryWithResourcesInAnotherTrywithResources() throws IOException {
		try(var socket = new Socket()){
			try(var inputStream = new DataInputStream(socket.getInputStream())){
				try(var outputStream = new DataOutputStream(socket.getOutputStream())) {
					System.out.println("C");
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println(TestTryWithResources.readFirstLineFromFile("main/src/tests/resources/TestTryWithResources.java"));
		
	}

}
