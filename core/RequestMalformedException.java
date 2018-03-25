package core;

public class RequestMalformedException extends Exception{

    public RequestMalformedException() {
    }

    public RequestMalformedException(String message) {
        super(message);
    }
}
