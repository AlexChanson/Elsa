package request;

import beans.ComDepReg;
import beans.Commune;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    private static HashMap<String, String> nameToBDDName = new HashMap<>();
    private static HashMap<String, String> BDDNameToName = new HashMap<>();

    public final static Pattern predPattern = Pattern.compile(
            "\\s*([a-zA-Z_]*)\\s*([!=/><]+)\\s*('\\w*'|\"\\w*\"|[0-9]+\\.[0-9]+|[0-9]+)\\s*");


    public static String getBDDName(String name){
        return BDDNameToName.get(name);
    }

    public static String getName(String bddName){
        return nameToBDDName.get(bddName);
    }

    public static void addNameMapping(String name, String bddName){
        nameToBDDName.put(name, bddName);
        BDDNameToName.put(bddName, name);
    }

    public static Function<Commune, Double> doubleCommuneGetter(String attribute){
        if (attribute == null){
            return null;
        }
        attribute = attribute.toLowerCase();

        switch (attribute){
            case "superficie":
                return x -> x.superficie;
            case "indice_demo":
                return x -> x.indice_demo;
            case "score_croissance_pop":
                return x -> x.score_croiss_pop;
            case "score_urbanite":
                return x -> x.score_urbanite;
        }

        return null;
    }

    public static Function<Commune, Long> longCommuneGetter(String attribute){
        if (attribute == null){
            return null;
        }
        attribute = attribute.toLowerCase();

        switch (attribute){
            case "actifs":
                return x -> (long) x.actifs_2010;
            case "etablissements":
                return x -> x.nb_etablissements;
        }

        return null;
    }

    public static Predicate<ComDepReg> parsePredicate(String pred){

        Matcher m = predPattern.matcher(pred);
        if (m.matches()){

            String attr = m.group(1);
            String op = m.group(2);
            String val = m.group(3);

            boolean isNumber = true;

            if (val.startsWith("\"") || val.startsWith("'")){
                isNumber = false;
                val = val.substring(1,val.length()-2);
            }

            boolean isFloating = val.contains(".");

            System.out.println("attr= "+attr);
            System.out.println("op= "+op);
            System.out.println("val= "+val+" is number= "+String.valueOf(isNumber)+" is floating= "+String.valueOf(isFloating));

            if (isNumber ){
                if (isFloating){
                    switch (op){
                        case "==":
                        case "=":
                            return comDepReg -> false;
                        case "!=":
                        case "/=":
                            break;
                        case "<=": break;
                        case ">=": break;
                        case "<": break;
                        case ">": break;
                    }
                }
                else {
                    switch (op){
                        case "==":
                        case "=":
                            return comDepReg -> false;
                        case "!=":
                        case "/=":
                            break;
                        case "<=": break;
                        case ">=": break;
                        case "<": break;
                        case ">": break;
                    }
                }
            }else {
                switch (op){
                    case "==":
                    case "=": break;
                    case "!=":
                    case "/=": break;
                }
            }

        }

        return null;
    }


}
