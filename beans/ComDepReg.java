package beans;

import dao.FlyweightManager;

public class ComDepReg {
    public final Commune commune;
    public final DepartementSimple departement;
    public final Region region;

    public ComDepReg(String code_insee,
                     String nom,

                     String num_dep,
                     String nom_dept,

                     int num_reg,
                     String nom_reg,
                     double moy_sal_h,
                     int pib, int pop,
                     int actifs,
                     int actifs_sal,
                     int actifs_nonsal,
                     int inst_pub,
                     int etudiants,

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
        commune = new Commune(
                code_insee,
                nom,
                num_dep,
                superficie,
                actifs_2010,
                med_r_f_uc_2010,
                pop_2015,
                evolution_pop,
                indice_demo,
                score_urbanite,
                nb_actifs_2015,
                nb_actifs_sal_2015,
                nb_actifs_nonSal_2015,
                dyn_demo_insee,
                nb_inst_pub,
                score_croiss_pop,
                env_demo,
                fidelite,
                nb_etudiants,
                nb_etablissements);
        region = FlyweightManager.regionGetFlyweight(
                            num_reg,
                            nom_reg,
                            moy_sal_h,
                            pib,
                            pop,
                            actifs,
                            actifs_sal,
                            actifs_nonsal,
                            inst_pub,
                            etudiants);
        departement = FlyweightManager.deptGetFlyweight(
                num_dep,
                nom_dept,
                num_reg);
    }

    public Commune getCommune() {
        return commune;
    }

    public DepartementSimple getDepartement() {
        return departement;
    }

    public Region getRegion() {
        return region;
    }

    @Override
    public String toString() {
        return "ComDepReg{" +
                "commune=" + commune.toString() +
                ", departement=" + departement.toString() +
                ", region=" + region.toString() +
                '}';
    }
}
