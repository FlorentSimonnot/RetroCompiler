import java.lang.*;
import java.net.*; 
import java.io.*;

public class TryWithResourcesMultiple{

	static void test() throws IOException{
		try (Socket socket = new Socket();
		     InputStream input = new DataInputStream(socket.getInputStream());
		     OutputStream output = new DataOutputStream(socket.getOutputStream());) {
		}

	}

	public static void main(String[] args) throws IOException{
		new TryWithResourcesMultiple().test();
	}

}
