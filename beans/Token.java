package beans;

import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "Tokens")
public class Token {

    private long user_id;
    @Key
    private String token;

    @DaoConstructor
    public Token(long user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getToken() {
        return token;
    }
}
