/*
----SERVER SIDE----
COP4504 - Project 1
written by - Chris Kile

Note- port is hardcoded below as 9090
Note- only A through C work at the moment, and Q

TODO:
	1. Netsat
	2. Current Users
	3. running processes

*/


import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.management.*;
import java.util.concurrent.TimeUnit;

public class Server
{
	public static void main(String args[])
	{
		int port = 9090;
		//start of try block for port connection, locks port
		try
		{
			//create ServerSocket to listen on
			ServerSocket server = new ServerSocket(port);
			
			//start of main while loop, should never exit
			while(true)
			{
				String clientInput = "";
				//String clientOutput = "";
				
				System.out.println("Listening on port " + port);
				System.out.println("");
				
				//accept incoming request
				Socket socket = server.accept();
				System.out.println("Client accepted.");
				System.out.println("");
				
				System.out.println("Setting up reader/writer...");
				//setup for Server to Client
				BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				//setup for Client to Server
				PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
				System.out.println("Data stream established...");
				System.out.println("");
				System.out.println("Waiting for client input...");
				
				//start of try block for input handling
				try
				{
					while((clientInput = fromSocket.readLine()) != null)
					{
						System.out.println("Client sent: " + clientInput);
						ArrayList<String> outputList = new ArrayList<String>();
						
						if(clientInput.equals("Q") || clientInput.equals("q"))
						{
							socket.close();
						}else
						{
							outputList = ProcessInput(clientInput);
							System.out.println("Sending");
							outputList.add("-2");
							
							for(int i = 0; i < outputList.size(); i++){
								//System.out.println("Printing line " + i);
								toSocket.println(outputList.get(i));
							}
							
						}
						
					}
				}catch(IOException e)
				{
					System.out.println("Client disconnected.");
					System.out.println("");
					socket.close();
				}
			}
		}catch(IOException e)
		{
			System.out.println("Error: port " + port + " is not working.");
		}
	
	//end main
	}

	public static ArrayList<String> ExecuteCommand(String command){
		ArrayList<String> returnList = new ArrayList<String>();
		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s;


			while ((s = output.readLine()) != null) {
				returnList.add(s);
			}
		}catch(Exception e){

		}
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
