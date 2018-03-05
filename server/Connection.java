package server;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;

class Connection implements Runnable {

    private Socket socket;
    //Define the www directory (like wamp), where the website root is. Put your index.html in it
    public static String wwwDir = System.getProperty("user.dir") + "/www/";
    static {
        InputStream input = Connection.class.getClassLoader().getResourceAsStream("config.properties");
        if (input != null) {
            try {
                Properties properties = new Properties();
                properties.load(input);
                wwwDir = properties.getProperty("webDir");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Connection(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    // This the 'main' method the request is handled here the order of each call is very important
    public void run() {
        try {
            PrintStream out = new PrintStream(socket.getOutputStream());
            HttpReq requete = new HttpReq();

            requete.doParse(socket.getInputStream());

            // Préparation de la réponse
            HttpAns ans = new HttpAns();
            String path = requete.getPath().toLowerCase();

            if (requete.isGet()){
                boolean fileOK = false;
                if (path.equals("/"))
                    path = "/index.html";

                //ans.setType(getFileType(path));

                if (Files.exists(Paths.get(wwwDir + path))){
                    fileOK = true;
                    ans.setLen(Math.toIntExact(new File(wwwDir + path).length()));
                    ans.setType(Files.probeContentType(Paths.get(wwwDir + path)));
                }

                sendHeader(out, ans);

                if (fileOK){
                    sendBinaryFileStream(new File(wwwDir + path), out);
                }

            } else if (requete.isPost()){
                //TODO handle post stuff
                if (path.startsWith("/api")){
                    //redirect to api
                }else {

                }
            }

            // Close connection
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendHeader(PrintStream out, HttpAns ans) {
        out.print(ans.build());
        out.println();
    }

    private static void sendBinaryFileStream(File f, PrintStream out) {
        FileInputStream fis;
        try {
            // Java 9
            fis = new FileInputStream(f);
            fis.transferTo(out);

            // Java 7 & 8
            /*
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buffer = new byte[4096];
            int n;
            while ((n = bis.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileType(String url){
        String ans = "";
        if (Pattern.matches("\\^(/(\\S)+)+\\.html", url))
            ans = HttpAns._html;
        else if (Pattern.matches("\\^(/(\\S)+)+\\.css", url))
            ans = HttpAns._css;
        else if (Pattern.matches("\\^(/(\\S)+)+\\.js", url))
            ans = HttpAns._js;
        else if (Pattern.matches("\\^(/(\\S)+)+\\.jpeg", url) || Pattern.matches("\\^(/(\\S)+)+\\.jpg", url))
            ans = HttpAns._jpeg;
        else if (Pattern.matches("\\^(/(\\S)+)+\\.mp4", url))
            ans = HttpAns._mp4;
        else if (Pattern.matches("\\^(/(\\S)+)+\\.png", url))
            ans = HttpAns._png;
        System.err.println("Content type = " + ans);
        return ans;
    }
}
