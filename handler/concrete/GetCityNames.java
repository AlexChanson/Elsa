package handler.concrete;

import beans.Commune;
import beans.CommuneSimple;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import handler.Utility;

import java.util.ArrayList;
import java.util.List;

public class GetCityNames implements Handler<RequestResult>{
    @Override
    public RequestResult handle(Command command) {
        if (command.getParameter("type") != null && command.getParameter("type").equals("getCityNames"))
            return process();
        return null;
    }

    private RequestResult process() {
        List<Commune> communes = (new BasicVirtualTable<Commune>(Commune.class)).getAll();
        List<CommuneSimple> communeSimples = new ArrayList<>();
        communes.forEach(commune -> communeSimples.add(new CommuneSimple(commune)));
        return new RequestResult() {
            @Override
            public String toJson() {
                return Utility.gson.toJson(communeSimples);
            }
        };
    }
}
