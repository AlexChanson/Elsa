package handler;

public class RequestError extends RequestResult{
    private String type, errMessage;

    public RequestError(String type, String errMessage) {
        this.type = type;
        this.errMessage = errMessage;
    }

    @Override
    public String toJson() {
        return "{\"status\":\"error\",\"type\":\""+type+"\",\"message\":\""+errMessage+"\"}";
    }
}
