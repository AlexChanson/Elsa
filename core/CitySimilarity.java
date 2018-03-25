package core;

import beans.Commune;

public class CitySimilarity {

    public CitySimilarity(){

    }


    public Double calculateDistance(Commune commune1, Commune commune2){

        double actifsDist = Math.pow((commune1.getActifs_2010()-commune2.getActifs_2010())/
                ((double)Math.max(commune1.getActifs_2010(), commune2.getActifs_2010())),2.0);

        double actifs2015Dist = Math.pow((commune1.getNb_actifs_2015()-commune2.getNb_actifs_2015())/(double)
                Math.max(commune1.getNb_actifs_2015(), commune2.getNb_actifs_2015()),2);

        double publicEtDist = Math.pow((commune1.getNb_etablissements()-commune2.getNb_etablissements())/(double)
                Math.max(commune1.getNb_etablissements(), commune2.getNb_etablissements()),2);

        double nbEtuDist = Math.pow((commune1.getNb_etudiants()-commune2.getNb_etudiants())/(double)
                Math.max(commune1.getNb_etudiants(), commune2.getNb_etudiants()),2);

        double evPopDist = commune1.getEvolution_pop().equals(commune2.getEvolution_pop())? 0.8 : 1.0;

        double fideliteDist = commune1.getFidelite().equals(commune2.getFidelite())? 0.8 : 1.0;

        double urbaniteDist = Math.pow((commune1.getScore_urbanite()-commune2.getScore_urbanite())/
                Math.max(commune1.getScore_urbanite(), commune2.getScore_urbanite()),2);

        /*
        System.out.println(actifsDist);
        System.out.println(actifs2015Dist);
        System.out.println(publicEtDist);
        System.out.println(nbEtuDist);
        System.out.println(evPopDist);
        System.out.println(fideliteDist);
        System.out.println(urbaniteDist);*/

        return (actifsDist+actifs2015Dist+publicEtDist+nbEtuDist+evPopDist+fideliteDist+urbaniteDist)/7;
    }

}
