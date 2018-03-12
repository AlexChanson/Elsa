package beans;

import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

import java.sql.Timestamp;

@Entity(tableName = "Users")
public class User {
    @Key(columnName = "user_id")
    private int user_id;
    private String email, nom, prenom, pwd_hash;
    private Timestamp last_login;

    @DaoConstructor
    public User(int user_id, String email, String nom, String prenom, String pwd_hash, Timestamp last_login) {
        this.user_id = user_id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.pwd_hash = pwd_hash;
        this.last_login = last_login;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPwd_hash() {
        return pwd_hash;
    }

    public Timestamp getLast_login() {
        return last_login;
    }
}
