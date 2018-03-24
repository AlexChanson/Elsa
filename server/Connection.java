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
import java.util.Arrays;
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
    private OutputStream out;
    private HttpReq requete;
    private HttpAns ans;

    Connection(Socket socket) {
        this.socket = socket;
        requete = new HttpReq();
        ans = new HttpAns();
        new Thread(this).start();
    }

    public void run() {
        try {
            out = socket.getOutputStream();
            requete.doParse(socket.getInputStream());
            System.out.printf("DEBUG '%s' FROM '%s'%n", requete.getHead(), socket.getRemoteSocketAddress().toString());
            String path = requete.getPath().toLowerCase();

            if (requete.isGet()){
                path = path.equals("/") ? wwwDir + "/index.html" : wwwDir + path;
                boolean fileOK = Files.exists(Paths.get(path));
                byte[] compressed = null;

                if (fileOK){
                    if (requete.supportsGzip()){
                        compressed = Utility.compress(new File(path));
                        ans.setLen(compressed.length).setCompressed();
                    }else
                        ans.setLen(Math.toIntExact(new File(path).length()));
                    ans.setType(Files.probeContentType(Paths.get(path)));
                }else
                    ans.setCode(HttpAns._404);
                sendHeader();
                if (fileOK){
                    if (compressed == null)
                        (new FileInputStream(path)).transferTo(out);
                    else
                        out.write(compressed);
                }

            } else if (requete.isPost()){
                if (path.startsWith("/api"))
                    handleAPICall();
                else if (path.startsWith("/connect"))
                    handleConnection();
                else if (path.startsWith("/create"))
                    handleAccountCreation();
                else {
                    //Handle other post requests
                }
            }
            // Close connection
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAPICall() throws IOException {
        // Verify API Key
        BasicVirtualTable<Token> tok = new BasicVirtualTable<>(Token.class);
        String token = requete.getParameter("key");
        if (tok.find(token, "token") != null){
            // Redirect to pipeline
            Command cmd = gson.fromJson("{\"api_key\":\"" + token + "\", \"parameters\" : " + requete.getBody() + "}", Command.class);
            try {
                RequestResult result = PipelineFactory.getPipeline().handle(cmd);
                if (requete.supportsGzip()){
                    byte[] data = Utility.compress(result.toJson());
                    ans.setCode(HttpAns._200).setType(HttpAns._json).setLen(data.length).setCompressed().printTo(out);
                    printDelimitter();
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
                String err = String.format("{\"exception\":\"%s\", \"stacktrace\":\"%s\"}", e.toString(), Arrays.toString(e.getStackTrace()));
                ans.setCode(HttpAns._500).setLen(err.length()).setType(HttpAns._json);
                printResponse(ans.build(), err);
            }

        }else {
            // Api Key is not valid
            String err = "{\"error\":\"invalid_api_key\"}";
            ans.setCode(HttpAns._403).setType(HttpAns._json).setLen(err.length());
            printResponse(ans.build(), err);
        }
    }

    private void handleConnection() throws IOException{
        HashMap<String, String> params = Utility.gson.fromJson(requete.getBody(), HashMap.class);
        String email = params.get("email");
        String password = params.get("password");
        System.out.printf("Connection requested from '%s' with password '%s' %n", email, password);

        User jeanPierre = userTable.find(email);

        if (jeanPierre != null && Utility.hashSHA256(password).equals(jeanPierre.getPwd_hash())){
            Token token = tokenTable.find(jeanPierre.getUser_id());
            String body = "{\"api_key\":\""+token+"\" }";
            ans.setType(HttpAns._json).setLen(body.length()).setCode(HttpAns._200);
            printResponse(ans.build(), body);
        } else {
            String body = "{\"error\":\"bad_login\"}";
            ans.setType(HttpAns._json).setLen(body.length()).setCode(HttpAns._200);
            printResponse(ans.build(), body);
        }
    }

    private void handleAccountCreation() throws IOException{
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
        printResponse(ans.build(), body);
    }

    private void sendHeader() throws IOException{
        ans.printTo(out);
        printDelimitter();
    }

    private void printResponse(String header, String body) throws IOException{
        out.write(header.getBytes());
        printDelimitter();
        out.write(body.getBytes());
    }

    private void printDelimitter() throws IOException{
        out.write(new byte[]{0x0d, 0x0a});
    }
}
