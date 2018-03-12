import com.google.gson.internal.LinkedTreeMap;
import handler.Command;
import handler.Utility;

/**
 * Fait par Alexandre le 12/03/2018.
 */
public class Tests {
    public static void main(String[] args) {
        Command cmd = new Command();
        cmd.getParameters().put("type", "getRegions");
        System.out.println(Utility.gson.toJson(cmd));

        String demo = "{\"parameters\":{\n" +
                "\t\"type\": \"clusterize\",\n" +
                "\t\n" +
                "\t\"commune_filters\": [\"pop<20000\", \"hopitaux>= 2\"],\n" +
                "\t\"region_filters\": [\"nb_etudiants>5000\"],\n" +
                "\t\n" +
                "\t\"on_regions\": [25,26,27,30],\n" +
                "\t\"on_deps\": [41,52,35],\n" +
                "\t\n" +
                "\t\"weights\": {\n" +
                "\t\t\"nb_actifs\": 0.4,\n" +
                "\t\t\"nb_inst_pub\": 0.4,\n" +
                "\t\t\"score_croiss_pop\": 0.2\n" +
                "\t}\n" +
                "\t\n" +
                "}}";

        Command c = Utility.gson.fromJson(demo, Command.class);
        System.out.println((LinkedTreeMap)c.getParameter("weights"));
    }
}
