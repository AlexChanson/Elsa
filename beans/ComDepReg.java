package beans;

import dao.Column;
import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "ComDepReg")
public class ComDepReg {

    @Column(name = "CODE_INSEE")
    @Key
    private final String code_insee;
    private final String nom;
    private final String num_dep;

    // Departements attributes
    private final String nom_dept;
    private final int num_reg;

    // Region attributes
    private final String nom_region;
    private final double moy_sal_h;
    private final int pib_reg,
            pop_reg,
            reg_nb_actifs,
            reg_nb_actifs_sal,
            reg_nb_actifs_nonsal,
            reg_nb_inst_pub,
            reg_nb_etudiants;

    // Commune attributes
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
    public ComDepReg(String code_insee, String nom, String num_dep, String nom_dept, int num_reg, String nom_region, double moy_sal_h, int pib_reg, int pop_reg, int reg_nb_actifs, int reg_nb_actifs_sal, int reg_nb_actifs_nonsal, int reg_nb_inst_pub, int reg_nb_etudiants, double superficie, int actifs_2010, int med_r_f_uc_2010, int pop_2015, String evolution_pop, double indice_demo, double score_urbanite, int nb_actifs_2015, int nb_actifs_sal_2015, int nb_actifs_nonSal_2015, int dyn_demo_insee, int nb_inst_pub, double score_croiss_pop, String env_demo, String fidelite, int nb_etudiants, long nb_etablissements) {
        this.code_insee = code_insee;
        this.nom = nom;
        this.num_dep = num_dep;
        this.nom_dept = nom_dept;
        this.num_reg = num_reg;
        this.nom_region = nom_region;
        this.moy_sal_h = moy_sal_h;
        this.pib_reg = pib_reg;
        this.pop_reg = pop_reg;
        this.reg_nb_actifs = reg_nb_actifs;
        this.reg_nb_actifs_sal = reg_nb_actifs_sal;
        this.reg_nb_actifs_nonsal = reg_nb_actifs_nonsal;
        this.reg_nb_inst_pub = reg_nb_inst_pub;
        this.reg_nb_etudiants = reg_nb_etudiants;
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
        return "ComDepReg{" +
                "code_insee='" + code_insee + '\'' +
                ", nom='" + nom + '\'' +
                ", num_dep='" + num_dep + '\'' +
                ", nom_dept='" + nom_dept + '\'' +
                ", num_reg=" + num_reg +
                ", nom_region='" + nom_region + '\'' +
                ", moy_sal_h=" + moy_sal_h +
                ", pib_reg=" + pib_reg +
                ", pop_reg=" + pop_reg +
                ", reg_nb_actifs=" + reg_nb_actifs +
                ", reg_nb_actifs_sal=" + reg_nb_actifs_sal +
                ", reg_nb_actifs_nonsal=" + reg_nb_actifs_nonsal +
                ", reg_nb_inst_pub=" + reg_nb_inst_pub +
                ", reg_nb_etudiants=" + reg_nb_etudiants +
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

    public Commune getCommune(){
        return new Commune(code_insee,
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
    }

    public Region getRegion(){
        return new Region(num_reg,
                nom_region,
                moy_sal_h,
                pib_reg,
                pop_reg,
                reg_nb_actifs,
                reg_nb_actifs_sal,
                reg_nb_actifs_nonsal,
                reg_nb_inst_pub,
                reg_nb_etudiants);
    }

    public DepartementSimple getDepartement(){
        return new DepartementSimple(
                num_dep,
                nom_dept,
                num_reg
        );
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

    public String getNom_dept() {
        return nom_dept;
    }

    public int getNum_reg() {
        return num_reg;
    }

    public String getNom_region() {
        return nom_region;
    }

    public double getMoy_sal_h() {
        return moy_sal_h;
    }

    public int getPib_reg() {
        return pib_reg;
    }

    public int getPop_reg() {
        return pop_reg;
    }

    public int getReg_nb_actifs() {
        return reg_nb_actifs;
    }

    public int getReg_nb_actifs_sal() {
        return reg_nb_actifs_sal;
    }

    public int getReg_nb_actifs_nonsal() {
        return reg_nb_actifs_nonsal;
    }

    public int getReg_nb_inst_pub() {
        return reg_nb_inst_pub;
    }

    public int getReg_nb_etudiants() {
        return reg_nb_etudiants;
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
