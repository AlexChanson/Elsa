package handler;

public interface Handler<T> {
    T handle(Command command);
}
