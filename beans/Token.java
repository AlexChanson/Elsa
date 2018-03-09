package beans;

import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "Tokens")
public class Token {

    private int user_id;
    @Key(columnName = "token")
    private String token;

    @DaoConstructor
    public Token(int user_id, String token) {
        this.user_id = user_id;
        this.token = token;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getToken() {
        return token;
    }
}
