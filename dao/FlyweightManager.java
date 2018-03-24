package dao;

import beans.Departement;
import beans.Region;

import java.util.HashMap;

public class FlyweightManager {
    private static HashMap<Integer, Region> regions;
    private static HashMap<String, Departement> departements;

    static {
        regions = new HashMap<>();
        departements = new HashMap<>();
    }

    public static Region getFlyweight(int reg_num,
                                      String nom,
                                      double moy_sal_h,
                                      int pib, int pop,
                                      int actifs,
                                      int actifs_sal,
                                      int actifs_nonsal,
                                      int inst_pub,
                                      int etudiants){
        Region reg = regions.get(reg_num);
        if (reg == null){
            reg = new Region(reg_num,
            nom,
            moy_sal_h,
            pib, pop,
            actifs,
            actifs_sal,
            actifs_nonsal,
            inst_pub,
            etudiants);
            regions.put(reg_num, reg);
        }
        return reg;
    }

    public static Departement deptGetFlyweight(String dept_num,
                                               String nom,
                                               int num_reg,
                                               double moy_sal_h,
                                               int pop_2015,
                                               int actifs,
                                               int actifs_sal,
                                               int actifs_nonsal,
                                               int inst_pub,
                                               int etudiants){
        Departement dept = departements.get(dept_num);

        if (dept == null){
            dept = new Departement(dept_num,
                        nom,
                        num_reg,
                        moy_sal_h,
                        pop_2015,
                        actifs,
                        actifs_sal,
                        actifs_nonsal,
                        inst_pub,
                        etudiants);
            departements.put(dept_num, dept);
        }

        return dept;
    }

}
