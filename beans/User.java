package beans;

import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

import java.sql.Timestamp;

@Entity(tableName = "Users")
public class User {

    transient private int user_id;
    @Key private String email;
    private String nom, prenom, pwd_hash;

    @DaoConstructor
    public User(long user_id, String email, String nom, String prenom, String pwd_hash) {
        this.user_id = Math.toIntExact(user_id);
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.pwd_hash = pwd_hash;
    }

    public User(String email, String nom, String prenom, String pwd_hash) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.pwd_hash = pwd_hash;
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

}
