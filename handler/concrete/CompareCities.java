package handler.concrete;

import beans.Commune;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;

public class CompareCities implements Handler<RequestResult> {



    @Override
    public RequestResult handle(Command command) {

        String citycode1 = (String) command.getParameter("commune1");
        String citycode2 = (String) command.getParameter("commune2");

        System.out.println("city1: "+citycode1);
        System.out.println("city2: "+citycode2);

        BasicVirtualTable<Commune> bvt = new BasicVirtualTable<>(Commune.class);

        Commune c1 = bvt.find(citycode1);
        Commune c2 = bvt.find(citycode2);

        System.out.println(c1.toString());
        System.out.println(c2.toString());


        return null;
    }
}
