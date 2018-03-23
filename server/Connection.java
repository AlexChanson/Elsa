package server;

import beans.Token;
import beans.User;
import com.google.gson.Gson;
import core.RequestMalformedException;
import dao.BasicVirtualTable;
import handler.Command;
import handler.PipelineFactory;
import handler.RequestResult;
import handler.Utility;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

class Connection implements Runnable {

    //Web directory definition
    public static String wwwDir = "~/www";
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
    private static BasicVirtualTable<User> userTable;
    private static BasicVirtualTable<Token> tokenTable;
    static {
        userTable = new BasicVirtualTable<>(User.class);
        tokenTable = new BasicVirtualTable<>(Token.class);
    }
    private static Gson gson = new Gson();


    private Socket socket;
    private PrintStream out;

    Connection(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    // This the 'main' method the request is handled here the order of each call is very important
    public void run() {
        try {
            out = new PrintStream(socket.getOutputStream());
            HttpReq requete = new HttpReq();

            requete.doParse(socket.getInputStream());
            System.out.printf("DEBUG '%s' FROM '%s'%n", requete.getHead(), socket.getRemoteSocketAddress().toString());

            // Préparation de la réponse
            HttpAns ans = new HttpAns();
            String path = requete.getPath().toLowerCase();

            if (requete.isGet()){
                boolean fileOK = false;
                if (path.equals("/"))
                    path = "/index.html";

                if (Files.exists(Paths.get(wwwDir + path))){
                    fileOK = true;
                    ans.setLen(Math.toIntExact(new File(wwwDir + path).length()));
                    ans.setType(Files.probeContentType(Paths.get(wwwDir + path)));
                }else {
                    ans.setCode(HttpAns._404);
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
                    if (tok.find(token, "token") != null){
                        // Redirect to api
                        Command cmd = gson.fromJson("{ \"parameters\" : " + requete.getBody() + " }", Command.class);
                        try {
                            RequestResult result = PipelineFactory.getPipeline().handle(cmd);
                            if (requete.getHeader("Accept-Encoding") != null && requete.getHeader("Accept-Encoding").toLowerCase().contains("gzip")){
                                byte[] data = Utility.compress(result.toJson());
                                ans.setCode(HttpAns._200).setType(HttpAns._json).setLen(data.length).setCompressed();
                                out.print(ans.build());
                                out.print("\r\n");
                                out.write(data);
                            }else {
                                String temp = result.toJson();
                                ans.setCode(HttpAns._200).setType(HttpAns._json).setLen(temp.length());
                                printResponse(ans.build(), temp);
                            }

                        }catch(RequestMalformedException rqe){
                            String err = rqe.getMessage();
                            ans.setCode(HttpAns._400).setLen(err.length()).setType(HttpAns._json);
                            printResponse(ans.build(), err);

                        }
                        catch (Exception e){
                            // Exception thrown in the pipeline returning 500 error code to client
                            String err = "{\"error\":\"" + e.toString().replace("\n", "\t")+ "\"}";
                            ans.setCode(HttpAns._500).setLen(err.length()).setType(HttpAns._json);
                            printResponse(ans.build(), err);
                        }

                    }else {
                        // Api Key is not valid
                        String err = "{\"error\":\"invalid_api_key\"}";
                        ans.setCode(HttpAns._403).setType(HttpAns._json).setLen(err.length());
                        printResponse(ans.build(), err);
                    }
                }else if (path.startsWith("/connect")){
                    handleConnection(out, requete, ans);
                }else if (path.startsWith("/create")){
                    handleAccountCreation(out, requete, ans);
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

    private static void handleConnection(PrintStream out, HttpReq requete, HttpAns ans) {
        HashMap<String, String> params = Utility.gson.fromJson(requete.getBody(), HashMap.class);

        String email = params.get("email");
        String password = params.get("password");
        System.out.printf("Connection requested from '%s' with password '%s' %n", email, password);

        User jeanPierre = userTable.find(email);

        if (jeanPierre != null && Utility.hashSHA256(password).equals(jeanPierre.getPwd_hash())){
            Token token = tokenTable.find(jeanPierre.getUser_id());
            String body = "{\"api_key\":\""+token+"\" }";
            ans.setType(HttpAns._json).setLen(body.length()).setCode(HttpAns._200);
            out.print(ans.build() + "\r\n" + body);
        } else {
            String body = "{\"error\":\"bad_login\"}";
            ans.setType(HttpAns._json).setLen(body.length()).setCode(HttpAns._200);
            out.print(ans.build() + "\r\n" + body);
        }
    }

    private static void handleAccountCreation(PrintStream out, HttpReq requete, HttpAns ans) {
        HashMap<String, String> params = Utility.gson.fromJson(requete.getBody(), HashMap.class);
        String email = params.get("email");
        String nom = params.get("nom");
        String prenom = params.get("prenom");
        String password = params.get("password");

        User jeanPierre = new User(email, nom, prenom, Utility.hashSHA256(password));

        (new BasicVirtualTable<User>(User.class)).add(jeanPierre);
        (new BasicVirtualTable<Token>(Token.class)).add(new Token(jeanPierre.getUser_id(), Utility.hashSHA256(jeanPierre.getEmail())));

        String body = "{\"status\":\"success\"}";

        ans.setType(HttpAns._json).setLen(body.length()).setCode(HttpAns._200);
        out.print(ans.build() + "\r\n" + body);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printResponse(String header, String body){
        out.print(header);
        out.write(0x0d);
        out.write(0x0a);
        out.print(body);
    }
}
