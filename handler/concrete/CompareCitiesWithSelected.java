package handler.concrete;


import core.RequestMalformedException;
import handler.Command;
import handler.Handler;
import handler.RequestResult;

public class CompareCitiesWithSelected implements Handler<RequestResult> {

    @Override
    public RequestResult handle(Command command) throws RequestMalformedException {

        String citycode1 = (String) command.getParameter("commune1");
        String citycode2 = (String) command.getParameter("commune2");




        return null;
    }
}
