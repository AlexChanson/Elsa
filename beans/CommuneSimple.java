package beans;

public class CommuneSimple {
    private String code_insee, nom, num_dep;

    public CommuneSimple(String code_insee, String nom, String num_dep) {
        this.code_insee = code_insee;
        this.nom = nom;
        this.num_dep = num_dep;
    }

    public CommuneSimple(Commune c) {
        this.code_insee = c.code_insee;
        this.nom = c.nom;
        this.num_dep = c.num_dep;
    }
}
