package handler;

import core.RequestMalformedException;
import handler.concrete.*;
import handler.concrete.GetCityNames;
import handler.concrete.GetDepartements;
import handler.concrete.GetRegions;
import handler.concrete.LoadCSV;

/**
 * Builds the chain of responsibility for handling requests
 * Thread safe handlers should be singleton
 */
public class PipelineFactory {
    private static Dispatcher staticDispatch;
    static {
        staticDispatch = new Dispatcher();
        staticDispatch.registerHandler("getRegions", new GetRegions());
        staticDispatch.registerHandler("getDepartements", new GetDepartements());
        staticDispatch.registerHandler("getCityNames", new GetCityNames());
        staticDispatch.registerHandler("compareCities", new CompareCities());
        staticDispatch.registerHandler("compareCitiesWithSelected", new CompareCitiesWithSelected());
        staticDispatch.registerHandler("loadCSV", new LoadCSV());
    }

    public static Handler<RequestResult> getPipeline(){
        HandlerChain<RequestResult> chain = new HandlerChain<>();
        chain.addHandler(staticDispatch);
        chain.addHandler(command -> new RequestResult() {
            @Override
            public String toJson() {
                return "{\"error\":\"no handler for this request\"}";
            }
        });
        return chain;
    }
}
