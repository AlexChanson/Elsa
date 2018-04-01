package handler.concrete;

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
        final int userID = c.getUID();
        Collection<String> lines = (Collection<String>) c.getParameter("csv");
        BasicVirtualTable<UserCommune> destination = new BasicVirtualTable<>(UserCommune.class);
        int i = 0;
        for (String line : lines) {
            String[] e = line.split(";");
            if (!destination.addBean(new UserCommune(e[0], userID, parseInt(e[1]), parseInt(e[2]), parseInt(e[3]), parseInt(e[4]))))
                i++;
        }
        final int temp = i;
        return new RequestResult() {
            @Override
            public String toJson() {
                return String.format("{\"status\":\"done\", \"errors\": %d }", temp);
            }
        };
    }
}
