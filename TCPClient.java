import java.io.*;
import java.net.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class TCPClient extends JFrame implements ActionListener{
	JPanel panel;
	JLabel title,subT;
	Font font;
	JTextField txt;
	JButton up,down;
	String dirName = "c:/FTP CLIENT DIRECTORY";
	Socket clientSocket; 
	InputStream inFromServer ;
	OutputStream outToServer;
	PrintWriter pw;
	String name, file;
	int c;
	int size = 9022386;
	public TCPClient(){
		super("TCP CLIENT");
		panel = new JPanel(null);
		font = new Font("Roboto",Font.BOLD,60);
		title = new JLabel("TCP CLIENT");
		title.setFont(font);
		title.setBounds(300,100,400,100);
		panel.add(title);
		subT = new JLabel("Enter File Name :");
		subT.setBounds(100,300,200,100);
		panel.add(subT);
		txt = new JTextField();
		txt.setBounds(400,300,500,100);
		panel.add(txt);
		up = new JButton("Upload");
		up.setBounds(250,500,200,100);
		panel.add(up);
		down = new JButton("Download");
		down.setBounds(550,500,200,100);
		panel.add(down);
		up.addActionListener(this);
		down.addActionListener(this);
		getContentPane().add(panel);
	//192.168.1.100
		try{
			clientSocket = new Socket("localhost", 3333);
			inFromServer = clientSocket.getInputStream();
			pw = new PrintWriter(clientSocket.getOutputStream(),true);
			outToServer = clientSocket.getOutputStream();
		}
		catch(Exception exc){
			System.out.println("Exception: "+exc.getMessage());
		}
	}
	public void actionPerformed(ActionEvent event){
		if(event.getSource() == up){
			try{
				name = txt.getText();
				pw.println(name);
				//System.out.println("Upload begins");
				//name = "c:/FTP CLIENT DIRECTORY/"+name;
				//FileInputStream fileIn = new FileInputStream(name);
				/*byte[] data = new byte[size];
				int bytes = 0;
				while((bytes = fileIn.read(data)) != -1){
					outToServer.write(data,0,bytes);
				}
				fileIn.close();
				System.out.println("Completed");*/

				FileInputStream file = null;
		        BufferedInputStream bis = null;
		        boolean fileExists = true;
		        name = "C:/FTP CLIENT DIRECTORY/"+name;
		        //System.out.println(filename);
		        try{
		          file = new FileInputStream(name);
		          bis = new BufferedInputStream(file);
		        }
		        catch(FileNotFoundException excep){
		          fileExists = false;
		          System.out.println("FileNotFoundException:"+excep.getMessage());
		        }
		        if(fileExists){
		          System.out.println("Upload begins");
		          sendBytes(bis,outToServer);
		          System.out.println("Completed");
		          bis.close();
		          file.close();
		          outToServer.close();
		        }
			}
			catch(Exception exc){
				System.out.println("Exception: "+exc.getMessage());
			}
		}
		if(event.getSource() == down){
			try{
				File directory = new File(dirName);
				if(!directory.exists()){
					directory.mkdir();
				}
				//int j = 0;
				boolean complete = true;
				byte [] data  = new byte [size];
				name = txt.getText();
				file = new String("*"+name+"*");
				pw.println(file); //lets the server know which file is to be downloaded
				File f = new File(directory,name);
				FileOutputStream fileOut = new FileOutputStream(f);
				DataOutputStream dataOut = new DataOutputStream(fileOut);
				//empty file case
				while(complete){
					c = inFromServer.read(data,0,data.length);
					if(c == -1){
						complete =  false;
						System.out.println("Completed");
					}

					else{
						dataOut.write(data,0,c);
			    		dataOut.flush();
					}
				}
				fileOut.close();
			}
			catch(Exception exc){
				System.out.println("Exception: "+exc.getMessage());
			}
		}
	}

	private static void sendBytes(BufferedInputStream in, OutputStream out)throws Exception{
    int size = 9022386;
    byte[] data = new byte[size];
    int bytes = 0;
    int c = in.read(data,0,data.length);
    out.write(data,0,c);
    out.flush();
  }
	
	//ClientSocket.close();
	public static void main(String args[]){
		TCPClient tcp = new TCPClient();
		tcp.setSize(1000,700);
		tcp.setVisible(true);
	}
}