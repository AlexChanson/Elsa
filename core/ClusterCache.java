package core;


import handler.Command;
import handler.Handler;
import java.util.HashMap;

public class ClusterCache<T> implements Handler<T> {

    int seed;
    private Handler<T> handler;
    private HashMap<ClusterKey, T> clusters;

    public ClusterCache(int seed, Handler<T> handler){
        this.seed = seed;
        this.handler = handler;
        clusters = new HashMap<>();
    }


    @Override
    public T handle(Command command) {
        T res = clusters.get(command);
        if (res == null){
            res = handler.handle(command);
        }

        return res;
    }
}
