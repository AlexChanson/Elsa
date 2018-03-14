package beans;

import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "Tokens")
public class Token {

    @Key
    private long user_id;
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

    @Override
    public String toString() {
        return token;
    }
}
