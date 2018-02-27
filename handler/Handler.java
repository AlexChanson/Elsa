package handler;

interface Handler<T> {
    public T handle(Command command);
}
