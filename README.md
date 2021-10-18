# distributedSystemsAssignment1
How to use the program:

  - Start server with "java ConversionServer 3500"
    (3500 is an example port number)

  - Start Client with "java ConversionClient 3500 localhost"
    (3500 as the same as the server example port number, localhost as the generic host name)

  - On the client, input the arguments or type 'help' for assistance at any time.
    Arguments are entered as follows: 'currentType' 'newType' 'number', where currentType is the number system
      of the number value and the newType is the type you want it converted to.
  
  - Currently, the only options for currentType and newType are binary or decimal.

  - To disconnect from the server, enter the command 'exit' 

Description:

The main functionality of this program is as number-system converter, it is able to convert binary values to decimal and decimal values to binary. After starting the program,
giving the port number and host name as the startup arguments, the program asks for the input values, and makes the user aware of the ‘help’ command if they require assistance in
using the program. User input is taken through the client console, and is to be entered in the form of three variables; current type, new type, and the value. The client is
capable of sending several requests to the server one after the other, and can disconnect from the server using the ‘exit’ command. There are two additional features for this
program. The first feature tells the user how many lines of code the program needed to read to complete the conversion, this does not include the error checking the program does,
only the lines read in the conversion methods. The second feature is a timer to show the user how long it took the program to work through those lines of code in milliseconds.
