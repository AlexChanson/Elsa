package handler.concrete;


import core.RequestMalformedException;
import handler.Command;
import handler.Handler;

public class CompareCitiesWithSelected implements Handler {

    @Override
    public Object handle(Command command) throws RequestMalformedException {

        String citycode1 = (String) command.getParameter("commune1");
        String citycode2 = (String) command.getParameter("commune2");




        return null;
    }
}
