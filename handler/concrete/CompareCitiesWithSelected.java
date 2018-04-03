package handler.concrete;


import beans.ComDepReg;
import beans.UserCommune;
import core.CitySimilarity;
import core.RequestMalformedException;
import dao.BasicVirtualTable;
import dao.UserComVTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import handler.Utility;
import request.Utilities;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompareCitiesWithSelected implements Handler<RequestResult> {

    private final int maxResults;

    public class DoubleComparisonResult{
        private final double similarityToA;
        private final double similarityToB;
        private final ComDepReg comDepReg;

        public DoubleComparisonResult(double similarityToA, double similarityToB, ComDepReg comDepReg) {
            this.similarityToA = similarityToA;
            this.similarityToB = similarityToB;
            this.comDepReg = comDepReg;
        }

        public double getSimilarityToA() {
            return similarityToA;
        }

        public double getSimilarityToB() {
            return similarityToB;
        }

        public ComDepReg getComDepReg() {
            return comDepReg;
        }
    }

    public class ComparisonResult{
        private final double similarity;
        private final ComDepReg comDepReg;

        public ComparisonResult(double similarity, ComDepReg comDepReg) {
            this.similarity = similarity;
            this.comDepReg = comDepReg;
        }

        public double getSimilarity() {
            return similarity;
        }

        public ComDepReg getComDepReg() {
            return comDepReg;
        }
    }

    public class Means{
        long total_city_nb;

        double mean_pop;
        double mean_nb_etablissement;
        double mean_nb_actifs2010;
        double mean_dyn_demo_insee;
        double mean_indice_demo;
        double mean_actifs2015;
        double mean_nb_etudiants;
        double mean_score_urbanite;
        double mean_superficie;
        double mean_score_croissance_pop;
        double mean_actifs_sal2015;
        double mean_actifs_nonsal2015;

        public Means(long total_city_nb,
                     double mean_pop,
                     double mean_nb_etablissement,
                     double mean_nb_actifs2010,
                     double mean_dyn_demo_insee,
                     double mean_indice_demo,
                     double mean_actifs2015,
                     double mean_nb_etudiants,
                     double mean_score_urbanite,
                     double mean_superficie,
                     double mean_score_croissance_pop,
                     double mean_actifs_sal2015,
                     double mean_actifs_nonsal2015) {
            this.total_city_nb = total_city_nb;
            this.mean_pop = mean_pop;
            this.mean_nb_etablissement = mean_nb_etablissement;
            this.mean_nb_actifs2010 = mean_nb_actifs2010;
            this.mean_dyn_demo_insee = mean_dyn_demo_insee;
            this.mean_indice_demo = mean_indice_demo;
            this.mean_actifs2015 = mean_actifs2015;
            this.mean_nb_etudiants = mean_nb_etudiants;
            this.mean_score_urbanite = mean_score_urbanite;
            this.mean_superficie = mean_superficie;
            this.mean_score_croissance_pop = mean_score_croissance_pop;
            this.mean_actifs_sal2015 = mean_actifs_sal2015;
            this.mean_actifs_nonsal2015 = mean_actifs_nonsal2015;
        }
    }

    public class FinalResult{
        private final ArrayList<ComparisonResult> withA;
        private final ArrayList<ComparisonResult> withB;
        private final ComDepReg cityA;
        private final ComDepReg cityB;
        private final Map<Integer, Long> countByRegion;
        private final Means means;
        private final List<UserCommune> userCommunesA;
        private final List<UserCommune> userCommunesB;

        public FinalResult(ArrayList<ComparisonResult> withA,
                           ArrayList<ComparisonResult> withB,
                           ComDepReg cityA,
                           ComDepReg cityB,
                           Map<Integer, Long> countByRegion,
                           Means means, List<UserCommune> userCommunesA,
                           List<UserCommune> userCommunesB) {
            this.withA = withA;
            this.withB = withB;
            this.cityA = cityA;
            this.cityB = cityB;
            this.countByRegion = countByRegion;
            this.means = means;
            this.userCommunesA = userCommunesA;
            this.userCommunesB = userCommunesB;
        }
    }

    public CompareCitiesWithSelected(int maxResults) {
        this.maxResults = maxResults;
    }

    @Override
    public RequestResult handle(Command command) throws RequestMalformedException {

        String citycode1 = (String) command.getParameter("commune1");
        String citycode2 = (String) command.getParameter("commune2");


        if (citycode1 == null){
            throw new RequestMalformedException("missing argument: commune1");
        }
        else if (citycode2 == null){
            throw new RequestMalformedException("missing argument: commune2");
        }

        List<String> filters = (List<String>) command.getParameter("filters");

        if (filters == null){
            throw new RequestMalformedException("missing argument: filters");
        }
        else if (filters.isEmpty()){
            throw new RequestMalformedException("need at least one filter");
        }

        BasicVirtualTable<ComDepReg> bvt = new BasicVirtualTable<>(ComDepReg.class);

        ComDepReg cityA = bvt.find(citycode1);
        ComDepReg cityB = bvt.find(citycode2);

        if (cityA == null){
            throw new RequestMalformedException("commune1 not found");
        }
        if (cityB == null){
            throw new RequestMalformedException("commune2 not found");
        }

        // parse strings into predicates
        Stream<Predicate<ComDepReg>> sp = filters.stream().map(x -> Utilities.parsePredicate(x));
        ArrayList<Predicate<ComDepReg>> predsList = new ArrayList<>();
        int i = 0;
        for (Predicate<ComDepReg> pred: (Iterable<Predicate<ComDepReg>>) sp::iterator) {
            // check for parsing problem
            if (pred == null){
                throw new RequestMalformedException("error in filters for filter: '" + filters.get(i) +"'");
            }
            predsList.add(pred);
            i++;
        }
        // turn multiple predicates into one
        Predicate<ComDepReg> finalPred = Utilities.predicateAndSum(predsList);


        CitySimilarity cs = new CitySimilarity();

        // get tuples then filter then calculate 2 similarity scores
        ArrayList<DoubleComparisonResult> filtered = bvt
                .getStream()
                .filter(x -> !(x.getCode_insee().equals(citycode1) || x.getCode_insee().equals(citycode2)) )
                .filter(finalPred)
                .map(x -> new DoubleComparisonResult(
                        cs.calculateDistance(x.getCommune(), cityA.getCommune()),
                        cs.calculateDistance(x.getCommune(), cityB.getCommune()), x))
                .collect(Collectors.toCollection(ArrayList::new));


        // count everything
        long filteredNb = filtered.size();

        long tot_nb_pop_2015 = 0;
        long tot_nb_etablissement = 0;
        long tot_nb_actifs2010 = 0;
        long tot_dyn_demo_insee = 0;
        double tot_indice_demo = 0;
        long tot_nb_actifs2015 = 0;
        long tot_nb_etudiants = 0;
        double tot_score_urbanite = 0;
        double tot_superficie = 0;
        double tot_score_croissance_pop = 0;
        long tot_actifs_sal2015 = 0;
        long tot_actifs_nonsal2015 = 0;


        for (DoubleComparisonResult x : filtered) {
            ComDepReg c = x.getComDepReg();
            tot_nb_pop_2015 += c.getPop_2015();
            tot_nb_etablissement += c.getNb_etablissements();
            tot_nb_actifs2010 += c.getActifs_2010();
            tot_dyn_demo_insee += c.getDyn_demo_insee();
            tot_indice_demo += c.getIndice_demo();
            tot_nb_actifs2015 += c.getNb_actifs_2015();
            tot_nb_etudiants += c.getNb_etudiants();
            tot_score_urbanite += c.getScore_urbanite();
            tot_superficie += c.getSuperficie();
            tot_score_croissance_pop += c.getScore_croiss_pop();
            tot_actifs_sal2015 += c.getNb_actifs_sal_2015();
            tot_actifs_nonsal2015 += c.getNb_actifs_nonSal_2015();
        }

        // calculating means
        Means means = null;
        if (filteredNb > 0){
            means = new Means(
                    filteredNb,
                    (double)tot_nb_pop_2015/filteredNb,
                    (double)tot_nb_etablissement/filteredNb,
                    (double)tot_nb_actifs2010/filteredNb,
                    (double)tot_dyn_demo_insee/filteredNb,
                    (double)tot_indice_demo/filteredNb,
                    (double)tot_nb_actifs2015/filteredNb,
                    (double)tot_nb_etudiants/filteredNb,
                    tot_score_urbanite/filteredNb,
                    tot_superficie/filteredNb,
                    tot_score_croissance_pop/filteredNb,
                    (double)tot_actifs_sal2015/filteredNb,
                    (double)tot_actifs_nonsal2015/filteredNb
            );
        }
        else {
            means = new Means(
                    filteredNb,
                    (double)tot_nb_pop_2015,
                    (double)tot_nb_etablissement,
                    (double)tot_nb_actifs2010,
                    (double)tot_dyn_demo_insee,
                    tot_indice_demo,
                    (double)tot_nb_actifs2015,
                    (double)tot_nb_etudiants,
                    tot_score_urbanite,
                    tot_superficie,
                    tot_score_croissance_pop,
                    (double)tot_actifs_sal2015,
                    (double)tot_actifs_nonsal2015
            );
        }


        // number of results by region
        Map<Integer, Long> countByRegion = filtered.stream()
                .collect(Collectors.groupingBy(x -> x.getComDepReg().getNum_reg(), Collectors.counting()));

        // calculating similarity lists
        ArrayList<ComparisonResult> withCityA = filtered.stream()
                .map(x -> new ComparisonResult(x.similarityToA, x.getComDepReg()))
                .sorted(Comparator.comparingDouble(ComparisonResult::getSimilarity))
                .limit(maxResults)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<ComparisonResult> withCityB = filtered.stream()
                .map(x -> new ComparisonResult(x.similarityToB, x.getComDepReg()))
                .sorted(Comparator.comparingDouble(ComparisonResult::getSimilarity))
                .limit(maxResults)
                .collect(Collectors.toCollection(ArrayList::new));


        // accessing user data
        UserComVTable userCommuneBasicVirtualTable = new UserComVTable();
        List<UserCommune> userCommunesA = userCommuneBasicVirtualTable.findAll(
                command.getUID(), "USER_ID",
                citycode1, "CODE_INSEE");

        List<UserCommune> userCommunesB = userCommuneBasicVirtualTable.findAll(
                command.getUID(), "USER_ID",
                citycode2, "CODE_INSEE");

        // translating to json
        String result = Utility.gson.toJson(
                new FinalResult(withCityA,
                                withCityB,
                                cityA,
                                cityB,
                                countByRegion,
                                means,
                                userCommunesA,
                                userCommunesB));

        return new RequestResult() {
            @Override
            public String toJson() {
                return result;
            }
        };
    }
}
