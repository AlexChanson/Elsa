package beans;

import dao.Column;
import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "Communes_alex")
public class Commune {

    @Column(name = "CODE_INSEE")
    @Key
    private final String code_insee;

    private final String nom;
    private final String num_dep;
    private final double superficie;
    private final int actifs_2010;
    private final int med_r_f_uc_2010;
    private final int pop_2015;
    private final String evolution_pop;
    private final double indice_demo;
    private final double score_urbanite;
    private final int nb_actifs_2015;
    private final int nb_actifs_sal_2015;
    private final int nb_actifs_nonSal_2015;
    private final int dyn_demo_insee;
    private final int nb_inst_pub;
    private final double score_croiss_pop;
    private final String env_demo;
    private final String fidelite;
    private final int nb_etudiants;
    private final long nb_etablissements;

    @DaoConstructor
    public Commune(String code_insee,
                   String nom,
                   String num_dep,
                   double superficie,
                   int actifs_2010,
                   int med_r_f_uc_2010,
                   int pop_2015,
                   String evolution_pop,
                   double indice_demo,
                   double score_urbanite,
                   int nb_actifs_2015,
                   int nb_actifs_sal_2015,
                   int nb_actifs_nonSal_2015,
                   int dyn_demo_insee,
                   int nb_inst_pub,
                   double score_croiss_pop,
                   String env_demo,
                   String fidelite,
                   int nb_etudiants,
                   long nb_etablissements) {
        this.code_insee = code_insee;
        this.nom = nom;
        this.num_dep = num_dep;
        this.superficie = superficie;
        this.actifs_2010 = actifs_2010;
        this.med_r_f_uc_2010 = med_r_f_uc_2010;
        this.pop_2015 = pop_2015;
        this.evolution_pop = evolution_pop;
        this.indice_demo = indice_demo;
        this.score_urbanite = score_urbanite;
        this.nb_actifs_2015 = nb_actifs_2015;
        this.nb_actifs_sal_2015 = nb_actifs_sal_2015;
        this.nb_actifs_nonSal_2015 = nb_actifs_nonSal_2015;
        this.dyn_demo_insee = dyn_demo_insee;
        this.nb_inst_pub = nb_inst_pub;
        this.score_croiss_pop = score_croiss_pop;
        this.env_demo = env_demo;
        this.fidelite = fidelite;
        this.nb_etudiants = nb_etudiants;
        this.nb_etablissements = nb_etablissements;
    }

    @Override
    public String toString() {
        return "Commune{" +
                "code_insee='" + getCode_insee() + '\'' +
                ", nom='" + getNom() + '\'' +
                ", num_dep='" + getNum_dep() + '\'' +
                ", superficie=" + getSuperficie() +
                ", actifs_2010=" + getActifs_2010() +
                ", med_r_f_uc_2010=" + getMed_r_f_uc_2010() +
                ", pop_2015=" + getPop_2015() +
                ", evolution_pop='" + getEvolution_pop() + '\'' +
                ", indice_demo=" + getIndice_demo() +
                ", score_urbanite=" + getScore_urbanite() +
                ", nb_actifs_2015=" + getNb_actifs_2015() +
                ", nb_actifs_sal_2015=" + getNb_actifs_sal_2015() +
                ", nb_actifs_nonSal_2015=" + getNb_actifs_nonSal_2015() +
                ", dyn_demo_insee=" + getDyn_demo_insee() +
                ", nb_inst_pub=" + getNb_inst_pub() +
                ", score_croiss_pop=" + getScore_croiss_pop() +
                ", env_demo='" + getEnv_demo() + '\'' +
                ", fidelite='" + getFidelite() + '\'' +
                ", nb_etudiants=" + getNb_etudiants() +
                ", nb_etablissements=" + getNb_etablissements() +
                '}';
    }

    public String getCode_insee() {
        return code_insee;
    }

    public String getNom() {
        return nom;
    }

    public String getNum_dep() {
        return num_dep;
    }

    public double getSuperficie() {
        return superficie;
    }

    public int getActifs_2010() {
        return actifs_2010;
    }

    public int getMed_r_f_uc_2010() {
        return med_r_f_uc_2010;
    }

    public int getPop_2015() {
        return pop_2015;
    }

    public String getEvolution_pop() {
        return evolution_pop;
    }

    public double getIndice_demo() {
        return indice_demo;
    }

    public double getScore_urbanite() {
        return score_urbanite;
    }

    public int getNb_actifs_2015() {
        return nb_actifs_2015;
    }

    public int getNb_actifs_sal_2015() {
        return nb_actifs_sal_2015;
    }

    public int getNb_actifs_nonSal_2015() {
        return nb_actifs_nonSal_2015;
    }

    public int getDyn_demo_insee() {
        return dyn_demo_insee;
    }

    public int getNb_inst_pub() {
        return nb_inst_pub;
    }

    public double getScore_croiss_pop() {
        return score_croiss_pop;
    }

    public String getEnv_demo() {
        return env_demo;
    }

    public String getFidelite() {
        return fidelite;
    }

    public int getNb_etudiants() {
        return nb_etudiants;
    }

    public long getNb_etablissements() {
        return nb_etablissements;
    }
}
