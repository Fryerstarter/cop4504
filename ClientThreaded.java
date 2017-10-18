import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.util.concurrent.*;

public class ClientThreaded
{
	public static void main(String[] args) {
		IPInfo ip = new IPInfo();
		String command;
		ip.setPort(9090);
		int numJobs = 10;
		if (args.length == 1) {
			ip.setIP(args[0]);
			numJobs = 10;
			command = "A";
		} else if (args.length == 2) {
			ip.setIP(args[0]);
			numJobs = Integer.parseInt(args[1]);
			command = "A";
		} else if (args.length == 3) {
			ip.setIP(args[0]);
			numJobs = Integer.parseInt(args[1]);
			command = args[2];
		} else {
			ip.setIP("73.104.15.60");
			numJobs = 50;
			command = "A";
		}

		command = command.toUpperCase();

		//Set up the Queue for printing. Add header for csv formatting
		ConcurrentLinkedQueue<String> printQueue = new ConcurrentLinkedQueue<String>();
		printQueue.add("ThreadName,Delay");
		Thread printThread = new Thread(new Printer(printQueue, command));
		printThread.start();

		//start primary threads
		Thread[] threads = new Thread[numJobs];

		for (int i = 0; i < numJobs; i++) {
			Thread newThread = new Thread(new ThreadProcess(printQueue, command));
			newThread.setName("Thread" + i);
			threads[i] = newThread;

		}

		for (int i = 0; i < numJobs; i++) {
			threads[i].start();
		}

		for(int i = 0; i < numJobs; i++){
			try{
				threads[i].join();
			}catch(Exception e){

			}
		}
		//join the print thread
		try {
			printThread.join();
		} catch (Exception e) {
		}

	}
}

class ThreadProcess implements Runnable
{
	ConcurrentLinkedQueue<String> queue;
	String command;
	
	ThreadProcess(ConcurrentLinkedQueue<String> queue, String command)
	{
		this.queue = queue;
		this.command = command;
	}
	
	public void run()
	{
		this.Connect();
	}
	
	public void Connect()
	{
		
		try
		{
			IPInfo ip = new IPInfo();
			InputStreamReader stdInReader = new InputStreamReader(System.in);
			BufferedReader stdIn = new BufferedReader(stdInReader);
			long startTime = 0, finishTime = 0, delay1 = 0, delay2 = 0;
			
			//make connection
			//System.out.println("Making connection....");
			System.out.println("Setting up " + Thread.currentThread().getName());
			Socket socket = new Socket(ip.getIP(), ip.getPort());
			
			//setup for socket toServer
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//setup for socket fromServer
			PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
			
			
			String userInput = "";
			String serverOutput = "";
			
			//userInput = stdIn.readLine();
			ArrayList<String> firstCom = new ArrayList<String>();

			startTime = System.currentTimeMillis();
			

			firstCom = toServer(fromServer, toServer, command);
			
			finishTime = System.currentTimeMillis();
			delay1 = finishTime - startTime;
			

			System.out.println("Time added.");
			String passer = Thread.currentThread().getName() + "," + delay1;
			
			//System.out.println(passer);
			queue.add(passer);
			fromServer.close();
			toServer.close();
			socket.close();
		
		}catch(IOException e)
		{
			System.out.println(e.toString());
			//fromServer.close();
			//toServer.close();
			//socket.close();
			
		}
		
		
		
			
	//END CONNECT
	}
	
	public ArrayList<String> toServer(BufferedReader fromServer, PrintWriter toServer, String input)
	{
		String serverOutput;
		ArrayList<String> inputList = new ArrayList<String>();
		try{
			
			toServer.println(input);
			while(!fromServer.ready())
				{
				}
			
			do{
				serverOutput = fromServer.readLine();
				
				inputList.add(serverOutput);
			}while(!serverOutput.equals("-2"));
			
			if(inputList.get(0).equals("-1"))
			{
				System.out.println("Error on input.");
			}
		}catch(IOException e)
		{
			
		}
		return inputList;
	}
	
	
	
	
}

class IPInfo
{
	private static String ip;
	private static int port;
	
	
	public IPInfo()
	{
	}
	
	
	public IPInfo(String ip)
	{
		this.ip = ip;
	}
	
	public IPInfo(int port)
	{
		this.port = port;
	}
	
	public IPInfo(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}
	
	public String getIP(){return ip;}
	
	public void setIP(String ip) {this.ip = ip;}
	
	public int getPort() {return port;}
	
	public void setPort(int port) {this.port = port;}
	
	
	
}

class Printer implements Runnable
{
	ConcurrentLinkedQueue<String> queue;
	String filename;
	String command;
	int i;
	
	Printer(ConcurrentLinkedQueue<String> queue, String command)
	{
		this.queue = queue;
		this.command = command;
		i = 0;
	}
	
	public void run()
	{
		String str;
		long startTime = System.currentTimeMillis();
		long currentTime=startTime;
		while(currentTime - startTime < 1000){

			try{
				Thread.sleep(50);
				filename = "TestData"+command+".csv";
				FileWriter fw = new FileWriter(filename, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw);
				currentTime = System.currentTimeMillis();
				while((str = queue.poll())!= null)
				{
					System.out.println("Printing... " + i);
					System.out.println(str);
					out.println(str);
					startTime=System.currentTimeMillis();
					i++;
				}
				out.close();
				}catch(Exception e)
				{
					System.out.println("Error saving to testdata");
					
				}
			
		}
	}
}