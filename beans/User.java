package beans;

import dao.DaoConstructor;
import dao.Entity;
import dao.Key;
import handler.Utility;
import request.Utilities;

import java.sql.Timestamp;

@Entity(tableName = "Users")
public class User {

    transient private int user_id;
    @Key private String email;
    private String nom, prenom, pwd_hash, api_key;

    @DaoConstructor
    public User(long user_id, String email, String nom, String prenom, String pwd_hash, String api_key) {
        this.user_id = Math.toIntExact(user_id);
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.pwd_hash = pwd_hash;
        this.api_key = api_key;
    }

    public User(String email, String nom, String prenom, String pwd_hash) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.pwd_hash = Utility.hashSHA256(pwd_hash);
        this.api_key = Utility.hashSHA256(email);
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

    public String getApi_key() {
        return api_key;
    }
}
