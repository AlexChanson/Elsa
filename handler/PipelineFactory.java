package handler;

import handler.concrete.GetRegions;

public class PipelineFactory {


    public static Handler<RequestResult> getPipeline(){
        return new GetRegions();
    }
}
