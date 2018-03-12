package beans;

import dao.Entity;
import dao.Key;

@Entity(tableName = "Communes_alex")
public class Commune {

    @Key(columnName = "CODE_INSEE")
    String code_insee;

    String nom;
    String num_dep;
    double superficie;
    int actifs_2010;
    int med_r_f_uc_2010;
    int pop_2015;
    String evolution_pop;
    double indice_demo;
    double score_urbanite;
    int nb_actifs_2015;
    int nb_actifs_sal_2015;
    int nb_actifs_nonSal_2015;
    int dyn_demo_insee;
    long nb_inst_pub;
    double score_croiss_pop;
    String env_demo;
    String fidelite;
    int nb_etudiants;
    long nb_etablissements;

}
