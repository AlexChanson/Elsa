package handler.concrete;

import handler.Command;
import handler.Handler;
import handler.RequestResult;

public class CompareCities implements Handler<RequestResult> {



    @Override
    public RequestResult handle(Command command) {

        String citycode1 = (String) command.getParameter("commune1");
        String citycode2 = (String) command.getParameter("commune2");


        return null;
    }
}
