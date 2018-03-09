package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpReq {
    private Map<String, String> header;
    private Map<String, String> urlParams;
    private String head;
    private String body;

    public HttpReq() {
        header = new HashMap<>();
        urlParams = new HashMap<>();
        body = "";
    }

    // You should read https://www.tutorialspoint.com/http/http_requests.htm first
    public void doParse(InputStream input){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line = br.readLine();
            head = line;

            // Decoding parameters from url
            int n = getPath().indexOf('?');
            if (n != -1){
                String params = getPath().substring(n+1);
                String[] temp = params.split("&");
                for (String s : temp) {
                    String[] p = s.split("=");
                    urlParams.put(p[0], p[1]);
                }
            }


            // Reading the header
            while (!line.equals("")) { // The empty line "" is the delimiter between header and body
                line = br.readLine();
                if (line.length() > 0 && line.indexOf(':') != -1){ // Is line format ok
                    String[] splited = split(line);                // Custom split to remove : & space ignore all : but the first
                    this.header.put(splited[0], splited[1]);       // Puts the result in the map
                }

            }
            // Reading the body if any
            if (header.get("Content-Length") != null){
                int len = Integer.parseInt(header.get("Content-Length"));
                byte[] buffer = new byte[len];
                input.read(buffer, 0, len);
                body = new String(buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] split(String line) {
        int sep = line.indexOf(':');
        return new String[] {line.substring(0, sep), line.substring(sep+1)};
    }

    boolean isPost(){
        return head != null && head.toLowerCase().contains("post");
    }

    boolean isGet(){
        return head != null && head.toLowerCase().contains("get");
    }

    boolean isPut(){
        return head != null && head.toLowerCase().contains("put");
    }

    boolean isOptions(){
        return head != null && head.toLowerCase().contains("options");
    }

    boolean isDelete(){
        return head != null && head.toLowerCase().contains("delete");
    }

    boolean isFirefox(){
        return header.containsKey("User-Agent") && header.get("User-Agent").contains("Firefox");
    }

    String getPath(){
        return head.split(" ")[1];
    }

    String getVersion(){
        return head.split(" ")[2];
    }

    Map<String,String> getParameters(){
        return urlParams;
    }

    String getParameter(String key){
        return urlParams.get(key);
    }

    @Override
    public String toString() {
        String ret = "Requette HTTP :";
        ret += head + '\n';
        ret += header.toString() + '\n';
        ret += body;
        return ret;
    }

    public void print(){
        System.out.println(" ### ### Debug ### ###");
        System.out.println(this.toString());
        System.out.println(" ### ### ### ### ###");
    }

    public String getBody() {
        return body;
    }
}
