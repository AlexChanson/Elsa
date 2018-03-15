package handler.concrete;

import beans.Commune;
import beans.CommuneSimple;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import handler.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetCityNames implements Handler<RequestResult>{
    @Override
    public RequestResult handle(Command command) {
        if (checkType(command, "getCityNames"))
            return process();
        return null;
    }

    private RequestResult process() {
        List<CommuneSimple> communeSimples = new ArrayList<>();
        (new BasicVirtualTable<Commune>(Commune.class))
                .getAll().forEach(commune -> communeSimples.add(new CommuneSimple(commune)));
        String buffer = Utility.gson.toJson(communeSimples);
        return new RequestResult() {
            @Override
            public String toJson() {
                return buffer;
            }
        };
    }
}
