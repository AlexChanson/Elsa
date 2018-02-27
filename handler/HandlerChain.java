package handler;

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
    public T handle(Command command) {

        return this.handlers.stream()
                .map(x -> x.handle(command) )
                .filter( x -> x != null)
                .findFirst()
                .orElse(null);

    }
}
