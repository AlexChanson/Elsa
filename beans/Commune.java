package beans;

import dao.Column;
import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "Communes_alex")
public class Commune {

    @Column(name = "CODE_INSEE")
    @Key
    public final String code_insee;

    public final String nom;
    public final String num_dep;
    public final double superficie;
    public final int actifs_2010;
    public final int med_r_f_uc_2010;
    public final int pop_2015;
    public final String evolution_pop;
    public final double indice_demo;
    public final double score_urbanite;
    public final int nb_actifs_2015;
    public final int nb_actifs_sal_2015;
    public final int nb_actifs_nonSal_2015;
    public final int dyn_demo_insee;
    public final int nb_inst_pub;
    public final double score_croiss_pop;
    public final String env_demo;
    public final String fidelite;
    public final int nb_etudiants;
    public final long nb_etablissements;

    @DaoConstructor
    public Commune(String code_insee, String nom, String num_dep, double superficie, int actifs_2010, int med_r_f_uc_2010, int pop_2015, String evolution_pop, double indice_demo, double score_urbanite, int nb_actifs_2015, int nb_actifs_sal_2015, int nb_actifs_nonSal_2015, int dyn_demo_insee, int nb_inst_pub, double score_croiss_pop, String env_demo, String fidelite, int nb_etudiants, long nb_etablissements) {
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
                "code_insee='" + code_insee + '\'' +
                ", nom='" + nom + '\'' +
                ", num_dep='" + num_dep + '\'' +
                ", superficie=" + superficie +
                ", actifs_2010=" + actifs_2010 +
                ", med_r_f_uc_2010=" + med_r_f_uc_2010 +
                ", pop_2015=" + pop_2015 +
                ", evolution_pop='" + evolution_pop + '\'' +
                ", indice_demo=" + indice_demo +
                ", score_urbanite=" + score_urbanite +
                ", nb_actifs_2015=" + nb_actifs_2015 +
                ", nb_actifs_sal_2015=" + nb_actifs_sal_2015 +
                ", nb_actifs_nonSal_2015=" + nb_actifs_nonSal_2015 +
                ", dyn_demo_insee=" + dyn_demo_insee +
                ", nb_inst_pub=" + nb_inst_pub +
                ", score_croiss_pop=" + score_croiss_pop +
                ", env_demo='" + env_demo + '\'' +
                ", fidelite='" + fidelite + '\'' +
                ", nb_etudiants=" + nb_etudiants +
                ", nb_etablissements=" + nb_etablissements +
                '}';
    }
}
