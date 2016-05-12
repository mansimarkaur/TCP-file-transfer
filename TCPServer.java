import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
	public static void main(String args[]) throws Exception {
		// if at least two argument are passed, consider the first one as directory path
		// and the second one as port number
		// If port number is not present, default it to 3333
		// If directory path is not present, show error
		if(args.length == 0) {
			System.out.println("Please enter the server directory address as first argument while running from command line.");
		}
		else {
			int id = 1;
			System.out.println("Server started...");
			System.out.println("Waiting for connections...");

			ServerSocket welcomeSocket;

			// port number is passed by the user
			if(args.length >= 2){
				welcomeSocket = new ServerSocket(Integer.parseInt(args[1]));
			}
			else{
				welcomeSocket = new ServerSocket(3333);
			}

			while (true) {
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println("Client with ID " + id + " connected from " + connectionSocket.getInetAddress().getHostName() + "...");
				Thread server = new ThreadedServer(connectionSocket, id, args[0]);
				id++;
				server.start();
			}
		}
	}
}

class ThreadedServer extends Thread {
	int n;
	int m;
	String name, f, ch, fileData;
	String filename;
	Socket connectionSocket;
	int counter;
	String dirName;

	public ThreadedServer(Socket s, int c, String dir) {
		connectionSocket = s;
		counter = c;

		// set dirName to the one that's entered by the user
		dirName = dir;
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			InputStream inFromClient = connectionSocket.getInputStream();
			PrintWriter outPw = new PrintWriter(connectionSocket.getOutputStream());
			OutputStream output = connectionSocket.getOutputStream();

			ObjectOutputStream oout = new ObjectOutputStream(output);
			oout.writeObject("Server says Hi!");

			File ff = new File(dirName);
			ArrayList<String> names = new ArrayList<String>(Arrays.asList(ff.list()));
			int len = names.size();
			oout.writeObject(String.valueOf(names.size()));

			for(String name: names) {
				oout.writeObject(name);
			}

			name = in.readLine();
			ch = name.substring(0, 1);

			if (ch.equals("*")) {
				n = name.lastIndexOf("*");
				filename = name.substring(1, n);
				FileInputStream file = null;
				BufferedInputStream bis = null;
				boolean fileExists = true;
				System.out.println("Request to download file " + filename + " recieved from " + connectionSocket.getInetAddress().getHostName() + "...");
				filename = dirName + filename;
				//System.out.println(filename);
				try {
					file = new FileInputStream(filename);
					bis = new BufferedInputStream(file);
				} 
				catch (FileNotFoundException excep) {
					fileExists = false;
					System.out.println("FileNotFoundException:" + excep.getMessage());
				}
				if (fileExists) {
					oout = new ObjectOutputStream(output);
					oout.writeObject("Success");
					System.out.println("Download begins");
					sendBytes(bis, output);
					System.out.println("Completed");
					bis.close();
					file.close();
					oout.close();
					output.close();
				}
				else {
					oout = new ObjectOutputStream(output);
					oout.writeObject("FileNotFound");
					bis.close();
					file.close();
					oout.close();
					output.close();
				}
			} 
			else{
				try {
					boolean complete = true;
					System.out.println("Request to upload file " + name + " recieved from " + connectionSocket.getInetAddress().getHostName() + "...");
					File directory = new File(dirName);
					if (!directory.exists()) {
						System.out.println("Dir made");
						directory.mkdir();
					}

					int size = 9022386;
					byte[] data = new byte[size];
					File fc = new File(directory, name);
					FileOutputStream fileOut = new FileOutputStream(fc);
					DataOutputStream dataOut = new DataOutputStream(fileOut);

					while (complete) {
						m = inFromClient.read(data, 0, data.length);
						if (m == -1) {
							complete = false;
							System.out.println("Completed");
						} else {
							dataOut.write(data, 0, m);
							dataOut.flush();
						}
					}
					fileOut.close();
				} catch (Exception exc) {
					System.out.println(exc.getMessage());
				}
			}
		} 
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private static void sendBytes(BufferedInputStream in , OutputStream out) throws Exception {
		int size = 9022386;
		byte[] data = new byte[size];
		int bytes = 0;
		int c = in .read(data, 0, data.length);
		out.write(data, 0, c);
		out.flush();
	}
}
