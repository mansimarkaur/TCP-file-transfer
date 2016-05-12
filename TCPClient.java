import java.io.*;
import java.net.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class TCPClient extends JFrame implements ActionListener {
	JPanel panel;
	JLabel title, subT, msg, error;
	Font font;
	JTextField txt;
	JButton up, down;
	String dirName;
	Socket clientSocket;
	InputStream inFromServer;
	OutputStream outToServer;
	BufferedInputStream bis;
	PrintWriter pw;
	String name, file, path;
	String hostAddr;
	int portNumber;
	int c;
	int size = 9022386;
	JList<String> filelist;

	public TCPClient(String dir, String host, int port) {
		super("TCP CLIENT");

		// set dirName to the one that's entered by the user
		dirName = dir;

		// set hostAddr to the one that's passed by the user
		hostAddr = host;

		// set portNumber to the one that's passed by the user
		portNumber = port;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel(null);

		font = new Font("Roboto", Font.BOLD, 60);
		title = new JLabel("TCP CLIENT");
		title.setFont(font);
		title.setBounds(300, 50, 400, 50);
		panel.add(title);

		subT = new JLabel("Enter File Name :");
		subT.setBounds(100, 450, 200, 50);
		panel.add(subT);

		txt = new JTextField();
		txt.setBounds(400, 450, 500, 50);
		panel.add(txt);

		up = new JButton("Upload");
		up.setBounds(250, 550, 200, 50);
		panel.add(up);

		down = new JButton("Download");
		down.setBounds(550, 550, 200, 50);
		panel.add(down);

		error = new JLabel("");
		error.setBounds(200, 650, 600, 50);
		panel.add(error);

		up.addActionListener(this);
		down.addActionListener(this);

		try {
			clientSocket = new Socket(hostAddr, portNumber);
			inFromServer = clientSocket.getInputStream();
			pw = new PrintWriter(clientSocket.getOutputStream(), true);
			outToServer = clientSocket.getOutputStream();
			ObjectInputStream oin = new ObjectInputStream(inFromServer);
			String s = (String) oin.readObject();
			System.out.println(s);

			int len = Integer.parseInt((String) oin.readObject());
			System.out.println(len);

			String[] names = new String[len];

			for(int i = 0; i < len; i++) {
				String filename = (String) oin.readObject();
				System.out.println(filename);
				names[i] = filename;
			}

			// String[] names = {"sahil","mansimar","sahil","mansimar","sahil","mansimar","sahil","mansimar","sahil","mansimar","sahil","mansimar","sahil","mansimar","sahil","mansimar"};
			String[] col = {"Name"};
			Object[][] data = {
				{"Sahil"},
				{"Mansimar"}
			};

			JList table = new JList<>(names);
			JScrollPane scroll = new JScrollPane(table);
			scroll.setBounds(400, 150, 200, 200);
			panel.add(scroll);

		} catch (Exception exc) {
			System.out.println("Exception: " + exc.getMessage());
			error.setText("Exception:" + exc.getMessage());
			panel.revalidate();
		}

		getContentPane().add(panel);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == up) {
			try {
				name = txt.getText();

				FileInputStream file = null;
				BufferedInputStream bis = null;

				boolean fileExists = true;
				path = dirName + name;

				try {
					file = new FileInputStream(path);
					bis = new BufferedInputStream(file);
				} catch (FileNotFoundException excep) {
					fileExists = false;
					System.out.println("FileNotFoundException:" + excep.getMessage());
					error.setText("FileNotFoundException:" + excep.getMessage());
					panel.revalidate();
				}

				if (fileExists) {
					// send file name to server
					pw.println(name);

					System.out.println("Upload begins");
					error.setText("Upload begins");
					panel.revalidate();

					// send file data to server
					sendBytes(bis, outToServer);
					System.out.println("Completed");
					error.setText("Completed");
					panel.revalidate();

					// close all file buffers
					bis.close();
					file.close();
					outToServer.close();
				}
			} catch (Exception exc) {
				System.out.println("Exception: " + exc.getMessage());
				error.setText("Exception:" + exc.getMessage());
				panel.revalidate();
			}
		}
		else if (event.getSource() == down) {
			try {
				File directory = new File(dirName);

				if (!directory.exists()) {
					directory.mkdir();
				}
				boolean complete = true;
				byte[] data = new byte[size];
				name = txt.getText();
				file = new String("*" + name + "*");
				pw.println(file); //lets the server know which file is to be downloaded

				ObjectInputStream oin = new ObjectInputStream(inFromServer);
				String s = (String) oin.readObject();

				if(s.equals("Success")) {
					File f = new File(directory, name);
					FileOutputStream fileOut = new FileOutputStream(f);
					DataOutputStream dataOut = new DataOutputStream(fileOut);

					//empty file case
					while (complete) {
						c = inFromServer.read(data, 0, data.length);
						if (c == -1) {
							complete = false;
							System.out.println("Completed");
							error.setText("Completed");
							panel.revalidate();

						} else {
							dataOut.write(data, 0, c);
							dataOut.flush();
						}
					}
					fileOut.close();
				}
				else {
					System.out.println("Requested file not found on the server.");
					error.setText("Requested file not found on the server.");
					panel.revalidate();
				}
			} catch (Exception exc) {
				System.out.println("Exception: " + exc.getMessage());
				error.setText("Exception:" + exc.getMessage());
				panel.revalidate();
			}
		}
	}

	private static void sendBytes(BufferedInputStream in , OutputStream out) throws Exception {
		int size = 9022386;
		byte[] data = new byte[size];
		int bytes = 0;
		int c = in.read(data, 0, data.length);
		out.write(data, 0, c);
		out.flush();
	}

	public static void main(String args[]) {
		// if at least three argument are passed, consider the first one as directory path,
		// the second one as host address and the third one as port number
		// If host address is not present, default it to "localhost"
		// If port number is not present, default it to 3333
		// If directory path is not present, show error
		if(args.length >= 3){
			TCPClient tcp = new TCPClient(args[0], args[1], Integer.parseInt(args[2]));
			tcp.setSize(1000, 900);
			tcp.setVisible(true);
		}
		else if(args.length == 2){
			TCPClient tcp = new TCPClient(args[0], args[1], 3333);
			tcp.setSize(1000, 900);
			tcp.setVisible(true);
		}
		else if(args.length == 1){
			TCPClient tcp = new TCPClient(args[0], "localhost", 3333);
			tcp.setSize(1000, 900);
			tcp.setVisible(true);
		}
		else {
			System.out.println("Please enter the client directory address as first argument while running from command line.");
		}
	}
}
