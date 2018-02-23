package server;

import java.net.*;
import java.io.*;

/**
 * @author Alexandre C
 */
public class Server {

    /**
     * @param args takes an optional port number default is 808à
     * @throws IOException
     */
	public static void main(String[] args) throws IOException {
		// You can change the port of the server here
        int port = 8080;

        if(args.length > 0){
            try {
                port = Integer.parseInt(args[0]);
            }catch (NumberFormatException ignored){}
        }

		ServerSocket ss = new ServerSocket(8080);
		while (true) new Connection(ss.accept());
	}



}
