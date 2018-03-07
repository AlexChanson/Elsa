package handler;

public abstract class RequestResult {
    String type;

    public abstract String toJson();

    public String getType() {
        return type;
    }
}
