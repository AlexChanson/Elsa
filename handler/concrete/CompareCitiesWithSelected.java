package handler.concrete;


import beans.ComDepReg;
import beans.Commune;
import core.RequestMalformedException;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import handler.Utility;
import request.Utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompareCitiesWithSelected implements Handler<RequestResult> {

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

        BasicVirtualTable<ComDepReg> bvt = new BasicVirtualTable<>(ComDepReg.class);

        ArrayList<ComDepReg> filtered = bvt.getStream().filter(finalPred).collect(Collectors.toCollection(ArrayList::new));

        String result = Utility.gson.toJson(filtered);

        return new RequestResult() {
            @Override
            public String toJson() {
                return result;
            }
        };
    }
}
