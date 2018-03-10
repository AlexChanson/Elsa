package server;

import beans.Token;
import com.google.gson.Gson;
import dao.BasicVirtualTable;
import handler.Command;
import handler.PipelineFactory;
import handler.RequestResult;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;

class Connection implements Runnable {

    private Socket socket;

    //Web directory definition
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
                // Handle API Request
                if (path.startsWith("/api")){
                    // Verify API Key
                    BasicVirtualTable<Token> tok = new BasicVirtualTable<>(Token.class);
                    String token = requete.getParameter("key");
                    if (tok.find(token) != null){
                        // Redirect to api
                        Gson gson = new Gson();
                        Command cmd = gson.fromJson(requete.getBody(), Command.class);
                        try {
                            RequestResult result = PipelineFactory.getPipeline().handle(cmd);
                            ans.setCode(HttpAns._200);
                            ans.setType(HttpAns._json);
                            ans.setLen(result.toJson().getBytes().length);
                            out.print(ans.build());
                            out.print("\n");
                            out.print(result.toJson());
                        }catch (Exception e){
                            // Exception thrown in the pipeline returning 500 error code to client
                            ans.setCode(HttpAns._500);
                            out.print(ans.build());
                        }

                    }else {
                        // Api Key is not valid
                        ans.setCode(HttpAns._403);
                        ans.setType(HttpAns._json);
                        out.print(ans.build());
                        out.print("\n");
                        out.print("{\"error\":\"Invalid API Key !\"}");
                    }
                }else {
                    //Handle other post requests
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

    @Deprecated
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
