package handler;

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
        staticDispatch.registerHandler("loadCSV", new LoadCSV());
    }

    public static Handler<RequestResult> getPipeline(){
        HandlerChain<RequestResult> chain = new HandlerChain<>();
        chain.addHandler(staticDispatch);
        return chain;
    }
}
