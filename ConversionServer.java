import java.io.*;
import java.net.*;
import java.text.DecimalFormat;

public class ConversionServer{

	public static void main(String[] args) throws Exception{
		//server only needs 1 argument to start (port number)
		//correct users on how to start server
		if(args.length != 1){
			System.out.println("Missing port argument.");
			System.out.println("Start server as such: java ConversionServer.java 'port#'");
			return;
		}
		
		try{
			//set up server and client sockets
			ServerSocket server;
			Socket client;
			
			//get port number from server start up command
			int port = Integer.parseInt(args[0]);
			
			//Server is up and listening
			server = new ServerSocket(port);
			System.out.println("ConversionServer is listening to port " + port);
			
			//infinite loop to keep server open for clients
			while(true){
				//accept client
				client = server.accept();
				System.out.println("Connection accepted.");
				
				//start new request
				Conversion c = new Conversion(client);
				c.start();
			}
		}catch(Exception e){
			System.out.println("Server failed, Exception: " + e.getMessage());
			e.printStackTrace();
			System.out.println("\nStart server as such: java ConversionServer.java 'port#'");
		}
	}
}

class Conversion extends Thread{
	//set up variables
	Socket client;
	BufferedReader reader;
	PrintWriter writer;
	int ops;
	
	//null constructor
	public Conversion (){}

	//main constructor
    public Conversion (Socket clientSocket){
       //set up the Socket instance and the input/output methods
        try{
			client = clientSocket;
            InputStream in = client.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));
            OutputStream out = client.getOutputStream();
			writer = new PrintWriter(out, true);
        }catch(Exception e){
			System.out.println("Setup failed, exception: " + e.getMessage());
			e.printStackTrace();
        }
    }
	
	//main functional method
	public void run(){
		try{
			//read the type the value is and the type the client wants it to be
			String nextStep = "";
			
			//while the client is running
			while(!(client.isClosed())){
				//get input, split by whitespace, rest ops
				nextStep = reader.readLine();
				String[] vars = nextStep.split("\\s+");
				ops = 0;
			
				//check if this request is for the help menu, to close connection, or for a conversion
				if(vars[0].equals("help")){
					String help = 
					"Input formats:\n Binary to Decimal: binary decimal #\n Decimal to Binary: decimal binary #\n To exit client: exit\n";
					writer.println(help);
				}else if(vars[0].equals("exit")){
					System.out.println("Client disconnecting.");
					writer.println("See you next time! Bye!");
					client.close();
				}else if(vars.length == 3){
					//start time for time computation in milliseconds
					long start = System.currentTimeMillis();
					
					//assign the proper variables
					String current = vars[0];
					String newType = vars[1];
					String val = vars[2];
					
					//determine which method to send the value to and as what types
					//programed to allow Hex and Octal to be added
					if(current.equalsIgnoreCase("binary")){
						boolean check = isBinary(val);
						if(check == true){
							if(newType.equalsIgnoreCase("decimal")){
								//get result and write to client
								int result = bToD(val);
								writer.println(result);
							}else
								writer.println("Please check the second type entered.");
						}else{
							writer.println("The entered value does not match the current type.");
						}
						
					}else if(current.equalsIgnoreCase("decimal")){
						boolean check = isNumeric(val);
						if(check == true){
							if(newType.equalsIgnoreCase("binary")){
								//val is required as an int for dToB
								int value = Integer.parseInt(val);
								//get result and send to client
								String result = dToB(value);
								writer.println(result);
							}else
								writer.println("Please check the second type entered.");
						}else{
							writer.println("The entered value does not match the current type.");
						}
					}else
						writer.println("Please check the first type entered.");
					
					//end time for time comp.
					long end = System.currentTimeMillis();
					//full duration the server worked on this request
					long duration = (end - start);
					//return the number of operations the request took along with how long it took
					writer.println("Your request took "+ ops + " operations to complete, and completed in " + duration + " ms");
				}
				else{
					//if the request does not satisfy the above options, remind user how to use server
					writer.println("Error. Issue with arguments. Please use the 'help' command for program instructions.\n");
				}
			}	
		}catch(Exception e){
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	//check that the user inputs a fully numeric number
	public boolean isNumeric(String sValue){
		try{
			//try parsing the string to an int, if it works, its numeric
			Integer.parseInt(sValue);
			return true;
		}catch(NumberFormatException e){
			//if exception occurs, not numeric
			return false;
		}
	}
	
	//check for binary number
	public boolean isBinary(String sValue){
		//move through the characters of sValue
		for(int i = 0; i < sValue.length(); i++){
			if(sValue.charAt(i) != '1' && sValue.charAt(i) != '0'){
				//if the value is not 1 or 0, 
				return false;
			}
		} 
		//if sValue passed the test above, its binary
		return true;
	}
	
	//binary to decimal method
	public int bToD(String bValue){
		//start opCounter, opCounter counts the number of operations required to get the new value
		int opCounter = 0;
		//initialize sigBit to value of the least significant bit
		int sigBit = 1;
		//initialize the sum decValue to 0
		int decValue = 0;
		
		//from least significant bit to most significant bit
		for(int place = bValue.length()-1; place >= 0; place--){
			opCounter++;
			//check the char value for a one
			if(bValue.charAt(place) == '1'){
				//add the significant bit value to the dec number
				decValue += sigBit;
				opCounter ++;
			}
			//get the value of the next significant bit
			sigBit = sigBit*2;
			opCounter += 2;
		}
		opCounter ++;//last for loop check
		ops = opCounter;
		
		return decValue;
	}
	
	//decimal to binary method
	public String dToB(int dValue){
		int opCounter = 0;
		//initialize the strings for the binary value and reverse binary value
		String rBinValue = "";
		String binValue = "";
		
		//while dValue is greater than the base case (1) 
		while (dValue != 0){
			opCounter ++;//checked and entered while loop
			//does dValue have a remainder
			if(dValue%2 == 0){
				//append a 0 if there is no remainder
				rBinValue += "0";
			}else{
				//append a 1 if there is a remainder
				rBinValue += "1";
			}
			opCounter += 2;//no matter what, two ops are done here
			
			//since dValue is an integer, divide by two to get the next value
			//if there is a decimal, it will be chopped off
			dValue = dValue/2;
			opCounter ++;
		}
		opCounter ++;//last while loop check
		int place = rBinValue.length()-1;
		char ch;
		//reverse the string obtained above to get the binary value
		while(place >= 0){
			ch = rBinValue.charAt(place);
			binValue += ch;
			place--;
			opCounter += 3;
		}
		opCounter ++;//last while loop check
		ops = opCounter;
		
		return binValue;
	}	
}