package handler;

import java.util.HashMap;

public class Command {
    String api_key;
    HashMap<String, Object> parameters = new HashMap<>();

    public Command() {
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public String getApiKey() {
        return api_key;
    }
}
