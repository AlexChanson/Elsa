package request;

import beans.ComDepReg;
import beans.Commune;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    private static HashMap<String, Function<Commune, Double>> commDoubleGetters;
    private static HashMap<String, Function<Commune, Long>> commLongGetters;

    static {
        commDoubleGetters = new HashMap<>();
        commDoubleGetters.put("superficie", Commune::getSuperficie);
        commDoubleGetters.put("indice_demographique", Commune::getIndice_demo);
        commDoubleGetters.put("score_urbanite", Commune::getScore_urbanite);
        commDoubleGetters.put("score_croissance_pop", Commune::getScore_croiss_pop);

        commLongGetters = new HashMap<>();
        commLongGetters.put("etablissements", Commune::getNb_etablissements);
        commLongGetters.put("actifs2010", intToLongGetter(Commune::getActifs_2010));
        commLongGetters.put("actifs2015", intToLongGetter(Commune::getNb_actifs_2015));
        commLongGetters.put("etudiants", intToLongGetter(Commune::getNb_etudiants));
        commLongGetters.put("population2015", intToLongGetter(Commune::getPop_2015));
    }

    public final static Pattern predPattern = Pattern.compile(
            "\\s*([a-zA-Z_]*)\\s*([!=/><]+)\\s*('\\w*'|\"\\w*\"|[0-9]+\\.[0-9]+|[0-9]+)\\s*");

    public static Function<Commune, Long> intToLongGetter(Function<Commune, Integer> f){
        return x -> Long.valueOf(f.apply(x));
    }

    public static Function<Commune, Double> doubleCommuneGetter(String attribute){
        if (attribute == null){
            return null;
        }
        attribute = attribute.toLowerCase();

        return commDoubleGetters.get(attribute);
    }

    public static Function<Commune, Long> longCommuneGetter(String attribute){
        if (attribute == null){
            return null;
        }
        attribute = attribute.toLowerCase();

        switch (attribute){
            case "actifs":
                return x -> (long) x.getActifs_2010();
            case "etablissements":
                return x -> x.getNb_etablissements();
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
