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
        //TODO Exception not compatible with stream format change to regular for loop

        return this.handlers.stream()
                .map(x -> {
                    try {
                        return x.handle(command);
                    } catch (RequestMalformedException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter( x -> x != null)
                .findFirst()
                .orElse(null);
    }
}
