package handler.concrete;


import beans.CommuneSimple;
import com.google.gson.stream.JsonWriter;
import dao.BasicVirtualTable;
import handler.Command;
import handler.Handler;
import handler.RequestResult;
import handler.Utility;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class GetCityNames implements Handler<RequestResult>{
    @Override
    public RequestResult handle(Command command) {
        if (checkType(command, "getCityNames")) {
            try {
                return process();
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private RequestResult process() throws IOException {

        BasicVirtualTable<CommuneSimple> bvt = new BasicVirtualTable<>(CommuneSimple.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.beginArray();
        bvt.getStream().forEach(comm -> Utility.gson.toJson(comm, CommuneSimple.class, writer));
        writer.endArray();
        String buffer = new String(out.toByteArray());
        return new RequestResult() {
            @Override
            public String toJson() {
                return buffer;
            }
        };
    }
}
