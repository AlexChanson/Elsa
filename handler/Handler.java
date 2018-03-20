package handler;

import core.RequestMalformedException;

public interface Handler<T> {
    T handle(Command command) throws RequestMalformedException;

    default boolean checkType(Command c, String t){
        Object o = c.getParameter("type");
        return o != null && o.equals(t);
    }
}
