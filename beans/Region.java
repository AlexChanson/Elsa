package beans;

import dao.Column;
import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "Regions")
public class Region {

    @Key
    @Column(name = "NUM_REGION")
    private int num;
    private String nom;
    private double moy_sal_h;
    private int pib, pop, actifs, actifs_sal, actifs_nonsal, inst_pub, etudiants;

    @DaoConstructor
    public Region(int num,
                  String nom,
                  double moy_sal_h,
                  int pib, int pop,
                  int actifs,
                  int actifs_sal,
                  int actifs_nonsal,
                  int inst_pub,
                  int etudiants) {
        this.num = num;
        this.nom = nom;
        this.moy_sal_h = moy_sal_h;
        this.pib = pib;
        this.pop = pop;
        this.actifs = actifs;
        this.actifs_sal = actifs_sal;
        this.actifs_nonsal = actifs_nonsal;
        this.inst_pub = inst_pub;
        this.etudiants = etudiants;
    }

    public int getNum() {
        return num;
    }

    public String getNom() {
        return nom;
    }

    public double getMoy_sal_h() {
        return moy_sal_h;
    }

    public int getPib() {
        return pib;
    }

    public int getPop() {
        return pop;
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

    @Override
    public String toString() {
        return "Region{" +
                "num=" + num +
                ", nom='" + nom + '\'' +
                ", moy_sal_h=" + moy_sal_h +
                ", pib=" + pib +
                ", pop=" + pop +
                ", actifs=" + actifs +
                ", actifs_sal=" + actifs_sal +
                ", actifs_nonsal=" + actifs_nonsal +
                ", inst_pub=" + inst_pub +
                ", etudiants=" + etudiants +
                '}';
    }
}
