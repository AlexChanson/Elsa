package beans;

import dao.DaoConstructor;
import dao.Entity;

@Entity(tableName = "Commune_user")
public class UserCommune {
    private String CODE_INSEE;
    private int USER_ID, ANNEE, POPULATION, ACTIFS, CHOMEURS;

    @DaoConstructor
    public UserCommune(String CODE_INSEE, int USER_ID, int ANNEE, int POPULATION, int ACTIFS, int CHOMEURS) {
        this.CODE_INSEE = CODE_INSEE;
        this.USER_ID = USER_ID;
        this.ANNEE = ANNEE;
        this.POPULATION = POPULATION;
        this.ACTIFS = ACTIFS;
        this.CHOMEURS = CHOMEURS;
    }

    public String getCODE_INSEE() {
        return CODE_INSEE;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public int getANNEE() {
        return ANNEE;
    }

    public int getPOPULATION() {
        return POPULATION;
    }

    public int getACTIFS() {
        return ACTIFS;
    }

    public int getCHOMEURS() {
        return CHOMEURS;
    }
}
