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
	3. Data export
	4. multi-threaded client test program

*/


import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;

public class Client
{
	public static void main(String args[])
	{
		while(true)
		{
			try
			{
            String ip;
            if(args.length > 0){
               ip = args[0];
            }
            else{
				   ip = "73.104.15.60";
            }
				//setup for user input from console
				InputStreamReader stdInReader = new InputStreamReader(System.in);
				BufferedReader stdIn = new BufferedReader(stdInReader);
				long startTime = 0, finishTime = 0, delay = 0;
				
				
				//flag to determine if q or Q for quitting time
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
					startTime = System.currentTimeMillis();
					toServer.println(userInput);
					if(userInput.equals("Q")||userInput.equals("q"))
					{
						//terminates connection to server
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
						ArrayList<String> inputList = new ArrayList<String>();
						do{
							serverOutput = fromServer.readLine();
							inputList.add(serverOutput);
						}while(!serverOutput.equals("-2"));
						finishTime = System.currentTimeMillis();
						if(inputList.get(0).equals("-1"))
						{
							System.out.println("Error on input.");
						}else
						{
							for(int i = 0; i < inputList.size() - 1; i++)
							{
								System.out.println(inputList.get(i));
							}
							System.out.println("Delay is: " + (finishTime - startTime));
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
