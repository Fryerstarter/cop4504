/*
----CLIENT SIDE----
COP4504 - Project 1
written by - Chris Kile
Note- port IP is hardcoded below. You will need to change this to your IP. If you google "what is my ip" 
it will come up
Note - only A through C work at the moment, and Q.

TODO:
	1. better IP system
	2. IP verification

*/


import java.io.*;
import java.net.*;

public class Client
{
	public static void main(String args[])
	{
		while(true)
		{
			try
			{
				String ip = "73.104.15.60";
				//setup for user input from console
				InputStreamReader stdInReader = new InputStreamReader(System.in);
				BufferedReader stdIn = new BufferedReader(stdInReader);
				
				
				
				//flag to determine if q or Q for quittin time
				//boolean quit = false;
				
				
				//make connection
				System.out.println("Making connection....");
				Socket socket = new Socket(ip, 9090);
				System.out.println("Setting up reader/writer...");
				//setup for socket toServer
				BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				//setup for socket fromServer
				PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
				
				while(true)
				{
					String userInput = "";
					String serverOutput = "";
					PrintMenu();
					userInput = stdIn.readLine();
					toServer.println(userInput);
					if(userInput.equals("Q")||userInput.equals("q"))
					{
						toServer.close();
						fromServer.close();
						stdInReader.close();
						stdIn.close();
						socket.close();
						System.exit(0);
					}else
					{
						System.out.println("Sending to server...");
						//toServer.println(userInput);
						while(!fromServer.ready())
						{
						}
						System.out.println("Receiving from server...");
						serverOutput = fromServer.readLine();
						if(serverOutput.equals("-1"))
						{
							System.out.println("Error on input.");
						}else
						{
							System.out.println(serverOutput);
						}
						
					}
				}
			}catch(IOException e)
			{
				System.out.println(e.toString());
			}
		
		}
	//end main
	}
	
	public static void PrintMenu()
	{
		System.out.println("-----------------------------");
		System.out.println("A. Date and Time");
		System.out.println("B. Uptime");
		System.out.println("C. Memory Use");
		System.out.println("D. NetStat");
		System.out.println("E. Current Users");
		System.out.println("F. Running Processes");
		System.out.println("Q. Quit");
		System.out.println("-----------------------------");
		System.out.println("");
	//end PrintMenu
	}
//end client
}
