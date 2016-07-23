# TCP-File-Transfer

This is a Java application that :

- uses TCP for file transfer.
	
- has a user interface buit using Java AWT and Java Swing.
	
- is built using Java Socket Programming.
	
- allows multiple users to connect to a server at once. It accomplishes this through multithreading.
	
- mandates the server to specify working directory as a command line argument with the freedom to specify a custom 	  port number as the second argument. The default port number has been set to 3333.
	
- server assigns connection IDs to the clients connected.
	
- mandates client to specify working directory as a command line argument. It allows user to specify host address and   if not specified, defaults it to Localhost. The user can also specify the port number as the third argument.
	
- Displays files and directories present in the server working directory and allows the client to select files and     download them onto the client system.
	
- Allows the client to upload files to the server working directory. This allows 2 clients to transfer files through   the server.
	
- You may call it ShareIt, Xender or something similar for laptops and PCs :wink: 
	

## Getting Started
```
git clone https://github.com/mansimarkaur/TCP-file-transfer
cd TCP-file-transfer
```
For *server* :
```
javac TCPServer.java
java TCPServer <server directory name>
```

For *client* :
```
javac TCPClient.java
java TCPClient <client directory name> 
```
Enter file name and click on **upload**. :arrow_up:

Choose file to be downloaded from the panel and click on **download**. :arrow_down:

**Server and Client can interact only through the same port number**

## Prerequisites

If you do not have Java installed, refer to this [Java installation guide](https://www.java.com/en/download/help/download_options.xml)

*For newbies, remember to set path variables in Windows and OSX*
