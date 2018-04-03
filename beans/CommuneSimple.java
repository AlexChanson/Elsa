package beans;

import dao.DaoConstructor;
import dao.Entity;
import dao.Key;

@Entity(tableName = "CommuneSimple")
public class CommuneSimple {
    @Key
    private final String code_insee;
    private final String nom;
    private final String num_dep;

    @DaoConstructor
    public CommuneSimple(String code_insee, String nom, String num_dep) {
        this.code_insee = code_insee;
        this.nom = nom;
        this.num_dep = num_dep;
    }

    public CommuneSimple(Commune c) {
        this.code_insee = c.getCode_insee();
        this.nom = c.getNom();
        this.num_dep = c.getNum_dep();
    }
}
