package beans;

import dao.Column;
import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "Depts")
public class DepartementSimple {
    @Column(name = "NUM_DEPT")
    @Key
    private String num;
    private String nom;
    private int num_reg;

    @DaoConstructor
    public DepartementSimple(String num,
                       String nom,
                       int num_reg) {
        this.num = num;
        this.nom = nom;
        this.num_reg = num_reg;
    }

    public String getNum() {
        return num;
    }

    public String getNom() {
        return nom;
    }

    public int getNum_reg() {
        return num_reg;
    }

    @Override
    public String toString() {
        return "DepartementSimple{" +
                "num='" + num + '\'' +
                ", nom='" + nom + '\'' +
                ", num_reg=" + num_reg +
                '}';
    }
}
