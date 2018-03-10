package server;

import java.awt.*;
import java.net.*;
import java.io.*;
/**
 * @author Alexandre C
 */
public class Server {
    public static String jdbc = null, dbuser = null, dbpass = null;
    public static final boolean local = false;

    /**
     * @param args takes an optional port number default is 8080
     * @throws IOException
     */
	public static void main(String[] args) throws IOException {
		// You can change the port of the server here
        int port = 8080;

        if(args.length > 0){
            try {
                port = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.err.println("Usage : java -jar elsa.jar 8080 /my/web/directory jdbc://my.db.server.com:3306/myDB dbUser 1234password");
            }
        }
        if (args.length > 1)
            Connection.wwwDir = args[1];
        if (args.length > 2)
            jdbc = args[2];
        if (args.length > 3)
            dbuser = args[3];
        if (args.length > 4)
            dbpass = args[4];

		ServerSocket ss = new ServerSocket(port);

		if (local){
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("http://localhost:"+port));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

		while (true) new Connection(ss.accept());
	}



}
