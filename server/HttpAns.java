package server;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Alexandre Chanson
 * This class is used to build the HTTP Answer Header
 */
public class HttpAns {
    public static final String
            _404 = "HTTP/1.0 404 NOT FOUND",
            _403 = "HTTP/1.0 403 Forbidden",
            _200 = "HTTP/1.0 200 OK",
            _500 = "HTTP/1.0 500 Internal Server Error",
            _html = "text/html",
            _jpeg = "image/jpeg",
            _js = "application/javascript",
            _mp4 = "application/mp4",
            _png = "image/png",
            _json = "application/json",
            _css = "text/css";

    private ArrayList<String> header = new ArrayList<>();
    private int len = -1;

    public HttpAns() {
        header.add(_200);
        header.add("Date: " + new Date());
        header.add("Server: Elsa/1.0");
        header.add("Content-type: text/html");
    }


    public String build(){
        StringBuilder r = new StringBuilder();
        header.set(1, "Date: " + new Date());
        for (String s : header) {
            r.append(s).append('\n');
        }
        r.append("Content-length: ").append(len).append('\n');
        return r.toString();
    }

    public HttpAns setCode(String code){
        header.set(0, code);
        return this;
    }

    public HttpAns setLen(int n){
        len = n;
        return this;
    }

    public HttpAns setType(String type){
        header.set(3, "Content-type: " + type);
        return this;
    }

    public HttpAns setCookie(Cookie c){
        header.add("Set-Cookie: "+c.name+"="+c.value);
        return this;
    }

    public HttpAns setCompressed(){
        header.add("Content-Encoding: gzip");
        return this;
    }
}
