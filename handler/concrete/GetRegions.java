package handler.concrete;

import beans.Region;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import handler.Utility;

import java.util.List;


public class GetRegions implements Handler<RequestResult> {
    @Override
    public RequestResult handle(Command command) {
        if (command.getParameter("type") != null && command.getParameter("type").equals("getRegions"))
            return process();
        return null;
    }

    private RequestResult process(){
        List<Region> regions = (new BasicVirtualTable<Region>(Region.class)).getAll();
        RequestResult result = new RequestResult() {
            @Override
            public String toJson() {
                return Utility.gson.toJson(regions);
            }
        };
        return result;
    }
}
