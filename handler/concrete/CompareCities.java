package handler.concrete;

import beans.Commune;
import core.CitySimilarity;
import core.RequestMalformedException;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import handler.Utility;

public class CompareCities implements Handler<RequestResult> {

    class ComparisonResult{
        private final Commune comm1;
        private final Commune comm2;
        private final double sc;

        public ComparisonResult(Commune c1, Commune c2, double score){
            comm1 = c1;
            comm2 = c2;
            sc = score;
        }
    }


    @Override
    public RequestResult handle(Command command) throws RequestMalformedException {

        //System.out.println("Handling 2 cities comparison request...");

        String citycode1 = (String) command.getParameter("commune1");
        String citycode2 = (String) command.getParameter("commune2");

        //System.out.println("city1: "+citycode1);
        //System.out.println("city2: "+citycode2);

        if (citycode1 == null){
            throw new RequestMalformedException("missing argument: commune1");
        }
        else if (citycode2 == null){
            throw new RequestMalformedException("missing argument: commune2");
        }


        BasicVirtualTable<Commune> bvt = new BasicVirtualTable<>(Commune.class);

        Commune c1 = bvt.find(citycode1);
        Commune c2 = bvt.find(citycode2);

        //System.out.println(c1.toString());
        //System.out.println(c2.toString());

        CitySimilarity comparator = new CitySimilarity();

        ComparisonResult res = new ComparisonResult(c1,c2, comparator.calculateDistance(c1,c2));

        String buffer = Utility.gson.toJson(res);

        return new RequestResult() {
            @Override
            public String toJson() {
                return buffer;
            }
        };
    }
}
