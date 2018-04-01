package handler;

import java.util.HashMap;

public class Command {
    private int user_id;
    HashMap<String, Object> parameters = new HashMap<>();

    public Command() {
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public int getUID() {
        return user_id;
    }
}
