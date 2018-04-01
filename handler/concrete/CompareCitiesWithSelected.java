package handler.concrete;


import beans.ComDepReg;
import beans.Commune;
import core.CitySimilarity;
import core.RequestMalformedException;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import handler.Utility;
import request.Utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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

    public class FinalResult{
        private final ArrayList<ComparisonResult> withA;
        private final ArrayList<ComparisonResult> withB;
        private final ComDepReg cityA;
        private final ComDepReg cityB;

        public FinalResult(ArrayList<ComparisonResult> withA, ArrayList<ComparisonResult> withB, ComDepReg cityA, ComDepReg cityB) {
            this.withA = withA;
            this.withB = withB;
            this.cityA = cityA;
            this.cityB = cityB;
        }
    }

    public CompareCitiesWithSelected(int maxResults) {
        this.maxResults = maxResults;
    }

    @Override
    public RequestResult handle(Command command) throws RequestMalformedException {

        System.out.println("Handling 2 cities comparison with filtered request...");

        String citycode1 = (String) command.getParameter("commune1");
        String citycode2 = (String) command.getParameter("commune2");

        System.out.println("city1: "+citycode1);
        System.out.println("city2: "+citycode2);

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

        Stream<Predicate<ComDepReg>> sp = filters.stream().map(x -> Utilities.parsePredicate(x));

        ArrayList<Predicate<ComDepReg>> predsList = new ArrayList<>();
        int i = 0;
        for (Predicate<ComDepReg> pred: (Iterable<Predicate<ComDepReg>>) sp::iterator) {
            if (pred == null){
                throw new RequestMalformedException("error in filters for filter: '" + filters.get(i) +"'");
            }
            predsList.add(pred);
            i++;
        }

        Predicate<ComDepReg> finalPred = Utilities.predicateAndSum(predsList);



        CitySimilarity cs = new CitySimilarity();

        ArrayList<DoubleComparisonResult> filtered = bvt
                .getStream()
                .filter(x -> !(x.getCode_insee().equals(citycode1) || x.getCode_insee().equals(citycode2)) )
                .filter(finalPred)
                .map(x -> new DoubleComparisonResult(
                        cs.calculateDistance(x.getCommune(), cityA.getCommune()),
                        cs.calculateDistance(x.getCommune(), cityB.getCommune()), x))
                .collect(Collectors.toCollection(ArrayList::new));

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


        String result = Utility.gson.toJson(new FinalResult(withCityA, withCityB, cityA, cityB));

        return new RequestResult() {
            @Override
            public String toJson() {
                return result;
            }
        };
    }
}
