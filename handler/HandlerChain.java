package handler;

import core.RequestMalformedException;

import java.util.ArrayList;

public class HandlerChain<T> implements Handler<T> {

    private ArrayList<Handler<T>> handlers;

    public HandlerChain(){
        handlers = new ArrayList<>();
    }

    public void addHandler(Handler<T> handler){
        this.handlers.add(handler);
    }

    @Override
    public T handle(Command command) throws RequestMalformedException{
        T result = null;
        for (Handler<T> h : this.handlers){
            result = h.handle(command);
            if (result != null)
                return result;
        }
        return null;
    }
}
