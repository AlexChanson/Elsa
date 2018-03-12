package handler;

import handler.concrete.GetDepartements;
import handler.concrete.GetRegions;

public class PipelineFactory {


    public static Handler<RequestResult> getPipeline(){
        HandlerChain<RequestResult> chain = new HandlerChain<>();
        chain.addHandler(new GetRegions());
        chain.addHandler(new GetDepartements());
        return chain;
    }
}
