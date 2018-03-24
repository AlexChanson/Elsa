package handler.concrete;

import beans.Token;
import beans.UserCommune;
import core.RequestMalformedException;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import java.util.Collection;
import static java.lang.Integer.parseInt;


public class LoadCSV implements Handler<RequestResult> {
    @Override
    public RequestResult handle(Command command) throws RequestMalformedException {
        if (checkType(command, "loadCSV"))
            return process(command);
        return null;
    }

    private RequestResult process(Command c) throws RequestMalformedException{
        if (c.getParameter("csv") == null)
            throw new RequestMalformedException();
        final int userID = (int) (new BasicVirtualTable<Token>(Token.class)).find(c.getApiKey(), "token").getUser_id();
        Collection<String> lines = (Collection<String>) c.getParameter("csv");
        BasicVirtualTable<UserCommune> destination = new BasicVirtualTable<>(UserCommune.class);
        lines.forEach(line -> {
            String[] e = line.split(";");
            destination.add(new UserCommune(e[0], userID, parseInt(e[1]), parseInt(e[2]), parseInt(e[3]), parseInt(e[4])));
        });
        return new RequestResult() {
            @Override
            public String toJson() {
                return "{\"status\":\"success\"}";
            }
        };
    }
}
