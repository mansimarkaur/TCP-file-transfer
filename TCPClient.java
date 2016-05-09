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
				name = "c:/FTP CLIENT DIRECTORY/"+name;
				FileInputStream fileIn = new FileInputStream(name);
				byte[] data = new byte[size];
				int bytes = 0;
				while((bytes = fileIn.read(data)) != -1){
					for(int i = 0; i<bytes; i++){
						//System.out.println(i);
						//System.out.println(data[i]);
					}
					outToServer.write(data,0,bytes);
				}
				fileIn.close();
			}
			catch(Exception exc){
				System.out.println("Exception: "+exc.getMessage());
			}
		}
		if(event.getSource() == down){
			/*try{
				
			    //InputStream is = clientSocket.getInputStream();
			    File directory = new File(dirName);
				if(!directory.exists()){
					directory.mkdir();
				}
			    name = txt.getText();
			    file = "*"+name+"*";
			    pw.println(file);
			    System.out.println(name);
			    int bytesRead = 0;
			    int current = 0;
			    byte [] data  = new byte [size];
			    File f = new File(directory,name);
			    FileOutputStream fileOut = new FileOutputStream(f);
		      	BufferedOutputStream bufferOut = new BufferedOutputStream(fileOut);
			    bytesRead = inFromServer.read(data,0,data.length);
			    System.out.println(bytesRead);			    
			    current = bytesRead;
			    int i = 0;
			    do {
			        bytesRead = inFromServer.read(data, current, (data.length-current));
			        //System.out.println(bytesRead);
			        //System.out.println(i++);
			        if(bytesRead >= 0) current += bytesRead;
			    } while(bytesRead > -1);
			    System.out.println(i++);
			    bufferOut.write(data, 0 , current);
			    System.out.println(i++);
			    bufferOut.flush();
			    System.out.println("File " + name + " downloaded (" + current + " bytes read)");
			}
			catch(Exception exc){
				System.out.println("Exception: "+exc.getMessage());
			}*/
      
			try{
				File directory = new File(dirName);
				if(!directory.exists()){
					directory.mkdir();
				}
				int j = 0;
				boolean complete = true;
				byte [] data  = new byte [size];
				name = txt.getText();
				file = new String("*"+name+"*");
				pw.println(file); //lets the server know which file is to be downloaded
				File f = new File(directory,name);
				FileOutputStream fileOut = new FileOutputStream(f);
				DataOutputStream dataOut = new DataOutputStream(fileOut);
				//empy file 
				// if((c = inFromServer.read()) == -1){
				// 	System.out.println("File empty");
				// 	complete = false;
				// }
				while(complete){
					c = inFromServer.read(data,0,data.length);
					//c = inFromServer.read();
					if(c == -1){
						complete =  false;
						System.out.println("Completed");
					}

					else{
						dataOut.write(data,0,c+1);
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
	
	//ClientSocket.close();
	public static void main(String args[]){
		TCPClient tcp = new TCPClient();
		tcp.setSize(1000,700);
		tcp.setVisible(true);
	}
}