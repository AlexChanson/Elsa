package beans;

import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "Depts")
public class Departement {
    @Key(columnName = "NUM_DEPT")
    private String num;
    private String nom;
    private int num_reg;
    private double moy_sal_h;
    private int pop_2015, actifs, actifs_sal, actifs_nonsal, inst_pub, etudiants;

    @DaoConstructor
    public Departement(String num, String nom, int num_reg, double moy_sal_h, int pop_2015, int actifs, int actifs_sal, int actifs_nonsal, int inst_pub, int etudiants) {
        this.num = num;
        this.nom = nom;
        this.num_reg = num_reg;
        this.moy_sal_h = moy_sal_h;
        this.pop_2015 = pop_2015;
        this.actifs = actifs;
        this.actifs_sal = actifs_sal;
        this.actifs_nonsal = actifs_nonsal;
        this.inst_pub = inst_pub;
        this.etudiants = etudiants;
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

    public double getMoy_sal_h() {
        return moy_sal_h;
    }

    public int getPop_2015() {
        return pop_2015;
    }

    public int getActifs() {
        return actifs;
    }

    public int getActifs_sal() {
        return actifs_sal;
    }

    public int getActifs_nonsal() {
        return actifs_nonsal;
    }

    public int getInst_pub() {
        return inst_pub;
    }

    public int getEtudiants() {
        return etudiants;
    }
}
