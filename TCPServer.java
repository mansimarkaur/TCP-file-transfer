import java.io.*;
import java.net.*;

public class TCPServer {
 public static void main(String args[]) throws Exception {
  int id = 1;
  System.out.println("Server started...");
  System.out.println("Waiting for connections...");
  ServerSocket welcomeSocket = new ServerSocket(3333);
  while (true) {
   Socket connectionSocket = welcomeSocket.accept();
   System.out.println("Client with ID " + id + " connected from " + connectionSocket.getInetAddress().getHostName() + "...");
   Thread server = new ThreadedServer(connectionSocket, id);
   id++;
   server.start();
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
 String dirName = "c:/FTP SERVER DIRECTORY";
 public ThreadedServer(Socket s, int c) {
  connectionSocket = s;
  counter = c;
 }
 public void run() {
  try {
   BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
   InputStream inFromClient = connectionSocket.getInputStream();
   PrintWriter out = new PrintWriter(connectionSocket.getOutputStream());
   OutputStream output = connectionSocket.getOutputStream();
   name = in .readLine();
   //System.out.println(name);
   ch = name.substring(0, 1);
   if (ch.equals("*")) {
    n = name.lastIndexOf("*");
    filename = name.substring(1, n);
    FileInputStream file = null;
    BufferedInputStream bis = null;
    boolean fileExists = true;
    System.out.println("Request to download file " + filename + " recieved from " + connectionSocket.getInetAddress().getHostName() + "...");
    filename = "C:/FTP SERVER DIRECTORY/" + filename;
    //System.out.println(filename);
    try {
     file = new FileInputStream(filename);
     bis = new BufferedInputStream(file);
    } catch (FileNotFoundException excep) {
     fileExists = false;
     System.out.println("FileNotFoundException:" + excep.getMessage());
    }
    if (fileExists) {
     System.out.println("Download begins");
     sendBytes(bis, output);
     System.out.println("Completed");
     bis.close();
     file.close();
     output.close();
    }
   } else {
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
   connectionSocket.close();
  } catch (Exception ex) {
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