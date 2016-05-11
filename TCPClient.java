import java.io.*;
import java.net.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class TCPClient extends JFrame implements ActionListener {
 JPanel panel;
 JLabel title, subT, msg;
 Font font;
 JTextField txt;
 JButton up, down;
 String dirName = "c:/FTP CLIENT DIRECTORY";
 Socket clientSocket;
 InputStream inFromServer;
 OutputStream outToServer;
 BufferedInputStream bis;
 PrintWriter pw;
 String name, file;
 int c;
 int size = 9022386;
 public TCPClient() {
  super("TCP CLIENT");
  panel = new JPanel(null);
  font = new Font("Roboto", Font.BOLD, 60);
  title = new JLabel("TCP CLIENT");
  title.setFont(font);
  title.setBounds(300, 50, 400, 50);
  panel.add(title);
  subT = new JLabel("Enter File Name :");
  subT.setBounds(100, 150, 200, 50);
  panel.add(subT);
  txt = new JTextField();
  txt.setBounds(400, 150, 500, 50);
  panel.add(txt);
  up = new JButton("Upload");
  up.setBounds(250, 250, 200, 50);
  panel.add(up);
  down = new JButton("Download");
  down.setBounds(550, 250, 200, 50);
  panel.add(down);
  up.addActionListener(this);
  down.addActionListener(this);
  getContentPane().add(panel);
  try {
   clientSocket = new Socket("192.168.1.101", 3333);
   inFromServer = clientSocket.getInputStream();
   pw = new PrintWriter(clientSocket.getOutputStream(), true);
   outToServer = clientSocket.getOutputStream();
  } catch (Exception exc) {
   System.out.println("Exception: " + exc.getMessage());
  }
 }
 public void actionPerformed(ActionEvent event) {
  if (event.getSource() == up) {
   try {
    name = txt.getText();
    pw.println(name);
    FileInputStream file = null;
    BufferedInputStream bis = null;
    boolean fileExists = true;
    name = "C:/FTP CLIENT DIRECTORY/" + name;
    try {
     file = new FileInputStream(name);
     bis = new BufferedInputStream(file);
    } catch (FileNotFoundException excep) {
     fileExists = false;
     System.out.println("FileNotFoundException:" + excep.getMessage());
    }
    if (fileExists) {
     msg = new JLabel("Upload begins");
     msg.setBounds(300, 350, 400, 50);
     panel.add(msg);
     //System.out.println("Upload begins");
     sendBytes(bis, outToServer);
     System.out.println("Completed");
     bis.close();
     file.close();
     outToServer.close();
    }
   } catch (Exception exc) {
    System.out.println("Exception: " + exc.getMessage());
   }
  }
  if (event.getSource() == down) {
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
    File f = new File(directory, name);
    FileOutputStream fileOut = new FileOutputStream(f);
    DataOutputStream dataOut = new DataOutputStream(fileOut);
    //empty file case
    while (complete) {
     c = inFromServer.read(data, 0, data.length);
     if (c == -1) {
      complete = false;
      System.out.println("Completed");
     } else {
      dataOut.write(data, 0, c);
      dataOut.flush();
     }
    }
    fileOut.close();
   } catch (Exception exc) {
    System.out.println("Exception: " + exc.getMessage());
   }
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
 public static void main(String args[]) {
  TCPClient tcp = new TCPClient();
  tcp.setSize(1000, 900);
  tcp.setVisible(true);
 }
}