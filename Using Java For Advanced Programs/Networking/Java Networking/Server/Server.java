import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class Server extends Thread{
	public final String[] commands = {":GET_ADDRESS",":GET_PORT",":SPEAK",":QUIT",":REVERSE"};
	ServerSocket serverSocket;
	final int TIMEOUT = 300000;
	int port;
	
	public Server(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(TIMEOUT);
	}
	
	public void run(){
		while (true){
			try {
				System.out.println("Waiting for client...");
				Socket connection = serverSocket.accept();
				System.out.println("Client connected");
				DataInputStream inputData = new DataInputStream(connection.getInputStream());
				DataOutputStream outputData = new DataOutputStream(connection.getOutputStream());
				String input = inputData.readUTF();
				if (input.equals(commands[0])){
					outputData.writeUTF("The server address is " + connection.getInetAddress().getHostAddress());
				} else if(input.equals(commands[1])) {
					System.out.println("The server port is " + connection.getPort());
				} else if(input.equals(commands[2])){
					System.out.println("Here's Johnny!!!");
				} else if(input.equals(commands[3])) {
					System.out.println("Goodbye!");
					connection.close();
					break;
				} else if(input.equals(commands[4])) {
					String s = inputData.readUTF();
					StringBuilder builder = new StringBuilder(s);
					builder.reverse();
					String output = builder.toString();
					outputData.writeUTF(output);
				} else if (input.contains("-c")){
					String output = commands.toString();
					outputData.writeUTF(output);
				} else {
					outputData.writeUTF("Invalid Command");
				}
			} catch (SocketTimeoutException e){
				System.out.println("Client Timed Out");
			} catch (IOException e){
				System.out.println("IO Exception.");
				break;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		Thread server = new Server(Integer.parseInt(args[0]));
		server.run();
	}

}