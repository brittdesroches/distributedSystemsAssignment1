/**
 * @author Brittney Desroches, 100649514
 * Assignment 1 for Distributed Systems
 *
 * This program is a number-system converter, specifically for binary and decimal 
 * This is the client side of this application
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConversionClient{
	
	public static void main (String[] args){
		//check for correct number of arguments
		if(args.length != 2){
			System.out.println("Incorrect arguments input.");
			System.out.println("Start server as such: java ConversionClient.java 'port#' 'hostName'");
			return;
		}
		
		try{
			//set up socket object
			Socket client;
			
			//set up read/write
			BufferedReader reader;
			PrintWriter writer;
			
			//get port# and hostName value from args
			int port = Integer.parseInt(args[0]);
			String hostName = args[1];
			
			//open new client
			client = new Socket(hostName, port);
			
			//create PrintWriter to send variables to server
			OutputStream out = client.getOutputStream();
			writer = new PrintWriter(out, true);
			
			//create BufferedReader to get messages from server			
			InputStream in = client.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));
			Scanner sysIn = new Scanner(System.in);
			
			while(true){
				//print instructions for user
				System.out.println("Enter your arguments, or type the command: help , for assistance.\n");
				//obtain user input
				String nextStep = sysIn.nextLine();
				
				if(nextStep.equalsIgnoreCase("help")){ //if user asks for help menu
					//send for help menu
					writer.println("help");
					String helpMenu = "";
					
					//print out menu
					while(!(helpMenu = reader.readLine()).isEmpty()){
						System.out.println("\n"+helpMenu);
					}	
					System.out.println("");
					
				}else if(nextStep.equalsIgnoreCase("exit")){ //if user asks to exit their client
					//send to tell server to close connection
					writer.println("exit");
					
					//get goodbye message and print it
					String exitMsg = reader.readLine();
					System.out.println("\n" + exitMsg );
					return;
				}else{ 
					//send the value(s) to server to be handled
					writer.println(nextStep);
					
					//write to console answer, number of operations, and time to complete
					//if no conversion was completed, error message will print instead
					String answer = reader.readLine();
					String feat = reader.readLine();
					System.out.println("\nAnswer: " + answer + "\n" + feat + "\n");
				}			
			}
			
		}catch(Exception e){
			System.out.println("Connection failed, Exception: " + e.getMessage());
			e.printStackTrace();
			System.out.println("\nStart server as such: java ConversionClient.java 'port#' 'hostName'");
		}	
	}
}
