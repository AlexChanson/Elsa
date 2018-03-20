package handler;

import core.RequestMalformedException;

import java.util.HashMap;


public class Dispatcher implements Handler<RequestResult>{
    private HashMap<String, Handler<RequestResult>> registeredHandlers = new HashMap<>();

    @Override
    public RequestResult handle(Command command) throws RequestMalformedException{
        Handler<RequestResult> selected = registeredHandlers.get((String)command.getParameter("type"));
        if (selected == null)
            return null;
        else
            return selected.handle(command);
    }

    public void registerHandler(String type, Handler<RequestResult> handler){
        registeredHandlers.put(type, handler);
    }
}
