package core;

import beans.Commune;

public class CitySimilarity {

    public CitySimilarity(){

    }


    public Double calculateDistance(Commune commune1, Commune commune2){

        double divisor = Math.max(commune1.getActifs_2010(), commune2.getActifs_2010());
        double actifsDist = divisor == 0? 0 : Math.pow((commune1.getActifs_2010()-commune2.getActifs_2010())/
                divisor,2.0);

        divisor = Math.max(commune1.getNb_actifs_2015(), commune2.getNb_actifs_2015());
        double actifs2015Dist = divisor == 0? 0 : Math.pow((commune1.getNb_actifs_2015()-commune2.getNb_actifs_2015())/divisor,2);

        divisor = Math.max(commune1.getNb_etablissements(), commune2.getNb_etablissements());
        double publicEtDist = divisor == 0? 0 : Math.pow((commune1.getNb_etablissements()-commune2.getNb_etablissements())/
                divisor,2);

        divisor = Math.max(commune1.getNb_etudiants(), commune2.getNb_etudiants());
        double nbEtuDist = divisor == 0? 0 : Math.pow((commune1.getNb_etudiants()-commune2.getNb_etudiants())/divisor,2);

        double evPopDist = commune1.getEvolution_pop().equals(commune2.getEvolution_pop())? 0.0 : 1.0;

        double fideliteDist = commune1.getFidelite().equals(commune2.getFidelite())? 0.0 : 1.0;

        divisor = Math.max(commune1.getScore_urbanite(), commune2.getScore_urbanite());
        double urbaniteDist = divisor == 0? 0 : Math.pow((commune1.getScore_urbanite()-commune2.getScore_urbanite())/
                divisor,2);

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
