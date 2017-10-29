/*
----Server Side Threaded----
COP4504 - Project 1
Group 11
Christopher Kile
Paul Ski
Ben
Davensly Dugar

-----WORK IN PROGRESS-----
*/


import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.management.*;
import java.util.concurrent.TimeUnit;

public class ServerThreaded
{
	public static void main(String args[]){
		int port = 9090;
		ArrayList<Thread> clientList = new ArrayList<Thread>();
		try{
			System.out.println("The server is listening on port: " + port);
			ServerSocket server = new ServerSocket(port);
			
			while(true){
				Socket socket = server.accept();
				System.out.println("Client accepted. Creating thread");

				Thread newThread = new Thread(new ServerThread(socket));
				newThread.start();
				clientList.add(newThread);
				
				
				
				
			}
		}catch(Exception e){
		
		}
		
	}
}
	
	
	
class ServerThread implements Runnable{
	Socket client;
	
	public ServerThread(Socket client){
		this.client = client;
	}
		
	public void run(){
		
		//start of try block for port connection, locks port
		try
		{


				BufferedReader fromSocket = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String clientInput;
				//setup for Client to Server
				PrintWriter toSocket = new PrintWriter(client.getOutputStream(), true);
				//start of try block for input handling
				try
				{
					while((clientInput = fromSocket.readLine()) != null)
					{
						//System.out.println("Client sent: " + clientInput);
						ArrayList<String> outputList = new ArrayList<String>();
						
						if(clientInput.equals("Q") || clientInput.equals("q"))
						{
							client.close();
						}else
						{
							outputList = Util.ProcessInput(clientInput);
							//System.out.println("Sending Response");
							outputList.add("-2");
							
							for(int i = 0; i < outputList.size(); i++){

								toSocket.println(outputList.get(i));
							}
							
						}
						
					}
				}catch(IOException e)
				{
					System.out.println("Client disconnected.");
					System.out.println("");
					client.close();
				}finally{
					fromSocket.close();
					toSocket.close();
				}
			
		}catch(IOException e)
		{
			System.out.println("Error: port is not working.");
		
		} 

	}
	
	public void setSocket(Socket client){
		this.client = client;
		
	}
	
}

class Util{

	public static ArrayList<String> ExecuteCommand(String command){
		ArrayList<String> returnList = new ArrayList<String>();
		try {
			System.out.println("Attempting to execute the command");
			//execute the command
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s;

			//adds each line of output to the array list to return to client
			while ((s = output.readLine()) != null) {
				System.out.println(s);
				returnList.add(s);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Returning List");
		return returnList;

	}
	
	public static ArrayList<String> ProcessInput(String clientInput)
	{
		ArrayList<String> outputList = new ArrayList<String>();
		
		if(clientInput.equals("A") || clientInput.equals("a"))
		{

			outputList= ExecuteCommand("date");
		}else if(clientInput.equals("B") || clientInput.equals("b"))
		{
			//output = "B";
			outputList= ExecuteCommand("uptime");
		}else if(clientInput.equals("C") || clientInput.equals("c"))
		{
			//output = "C";

			outputList= ExecuteCommand("free -m");
		}else if(clientInput.equals("D") || clientInput.equals("d"))
		{
			//output = "D";
			System.out.println("Client entered D");
			outputList = ExecuteCommand("netstat");
		}else if(clientInput.equals("E") || clientInput.equals("e"))
		{
			outputList = ExecuteCommand("users");
		}else if(clientInput.equals("F") || clientInput.equals("f"))
		{
			outputList = ExecuteCommand("ps -A");
		}else if(clientInput.equals("Q") || clientInput.equals("q"))
		{
			//output = "Q";
		}else
		{
			outputList.add("-1");
		}
		return outputList;
	//end ProcessInput
	}
	
	/*
	public static String commander(String input){
		
	}
	*/
//end server
}