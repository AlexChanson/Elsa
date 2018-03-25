package core;

import beans.Commune;

public class CitySimilarity {

    public CitySimilarity(){

    }


    public Double calculateDistance(Commune commune1, Commune commune2){

        double actifsDist = ((commune1.getActifs_2010()-commune2.getActifs_2010())^2)/
                Math.max(commune1.getActifs_2010(), commune2.getActifs_2010());

        double actifs2015Dist = ((commune1.getNb_actifs_2015()-commune2.getNb_actifs_2015())^2)/
                Math.max(commune1.getNb_actifs_2015(), commune2.getNb_actifs_2015());

        double publicEtDist = ((commune1.getNb_etablissements()-commune2.getNb_etablissements())^2)/
                Math.max(commune1.getNb_etablissements(), commune2.getNb_etablissements());

        double nbEtuDist = ((commune1.getNb_etudiants()-commune2.getNb_etudiants())^2)/
                Math.max(commune1.getNb_etudiants(), commune2.getNb_etudiants());

        double evPopDist = commune1.getEvolution_pop().equals(commune2.getEvolution_pop())? 0.8 : 1.0;

        double fideliteDist = commune1.getFidelite().equals(commune2.getFidelite())? 0.8 : 1.0;

        double urbaniteDist = Math.pow((commune1.getScore_urbanite()-commune2.getScore_urbanite()), 2)/
                Math.max(commune1.getScore_urbanite(), commune2.getScore_urbanite());

        return (actifsDist+actifs2015Dist+publicEtDist+nbEtuDist+evPopDist+fideliteDist+urbaniteDist)/7;
    }

}
