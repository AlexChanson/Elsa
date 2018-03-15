package handler.concrete;

import beans.Departement;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import handler.Utility;
import java.util.List;


public class GetDepartements implements Handler<RequestResult> {
    @Override
    public RequestResult handle(Command command) {
        if (checkType(command, "getDepartements"))
            return process();
        return null;
    }

    private RequestResult process(){
        List<Departement> Departements = (new BasicVirtualTable<Departement>(Departement.class)).getAll();
        RequestResult result = new RequestResult() {
            @Override
            public String toJson() {
                return Utility.gson.toJson(Departements);
            }
        };
        return result;
    }
}