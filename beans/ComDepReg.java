package beans;

public class ComDepReg {
    public final Commune commune;
    public final Departement departement;
    public final Region region;

    public ComDepReg(Commune commune, Departement departement, Region region) {
        this.commune = commune;
        this.departement = departement;
        this.region = region;
    }
}
