package request;

import beans.ComDepReg;
import beans.Commune;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    private static HashMap<String, Function<Commune, Double>> commDoubleGetters;
    private static HashMap<String, Function<Commune, Long>> commLongGetters;
    private static HashMap<String, Function<Commune, String>> commStringGetters;

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

        commStringGetters = new HashMap<>();
        commStringGetters.put("code_insee", Commune::getCode_insee);
        commStringGetters.put("num_dep", Commune::getNum_dep);
        commStringGetters.put("ev_pop", Commune::getEvolution_pop);
        commStringGetters.put("env_demo", Commune::getEnv_demo);
        commStringGetters.put("fidelite", Commune::getFidelite);
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

    public static Function<Commune, String> stringCommuneGetter(String attribute){
        if (attribute != null){
            attribute = attribute.toLowerCase();
            return commStringGetters.get(attribute);
        }
        return null;
    }

    public static <T, V> Predicate<T> makeEqualPredicate(Function<T, V> getter, V val ){
        return x -> getter.apply(x).equals(val);
    }

    public static <T> Predicate<T> makeLessThanPredicate(Function<T, Double> getter, double val){
        return x -> getter.apply(x) < val;
    }

    public static <T> Predicate<T> makeLessThanPredicate(Function<T, Integer> getter, int val){
        return x -> getter.apply(x) < val;
    }

    public static <T> Predicate<T> makeGreaterThanPred(Function<T, Integer> getter, int val){
        return x -> getter.apply(x) > val;
    }

    public static <T> Predicate<T> makeGreaterThanPred(Function<T, Double> getter, double val){
        return x -> getter.apply(x) > val;
    }

    public static <T, V> Predicate<T> predicateOperator(String op,
                                                        Function<T, V> getter,
                                                        V val){
        switch (op){
            case "==":
            case "=":
                return makeEqualPredicate(getter, val);
            case "!=":
            case "/=":
                return makeEqualPredicate(getter, val).negate();
        }
        return null;
    }

    public static <T> Predicate<T> predicateOperator(String op,
                                                        Function<T, Integer> getter,
                                                        int val){
        switch (op){
            case "==":
            case "=":
                return makeEqualPredicate(getter, val);
            case "!=":
            case "/=":
                return makeEqualPredicate(getter, val).negate();
            case "<":
                return makeLessThanPredicate(getter, val);
            case ">":
                return makeGreaterThanPred(getter, val);
            case ">=":
                return makeLessThanPredicate(getter, val).negate();
            case "<=":
                return makeGreaterThanPred(getter, val).negate();
            default:
                return null;
        }
    }

    public static <T> Predicate<T> predicateOperator(String op,
                                                     Function<T, Double> getter,
                                                     double val){
        switch (op){
            case "==":
            case "=":
                return makeEqualPredicate(getter, val);
            case "!=":
            case "/=":
                return makeEqualPredicate(getter, val).negate();
            case "<":
                return makeLessThanPredicate(getter, val);
            case ">":
                return makeGreaterThanPred(getter, val);
            case ">=":
                return makeLessThanPredicate(getter, val).negate();
            case "<=":
                return makeGreaterThanPred(getter, val).negate();
            default:
                return null;
        }
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
                    case "=":
                        break;
                    case "!=":
                    case "/=": break;
                }
            }

        }

        return null;
    }

    /**
     * @param preds a predicate list
     * @param <T>
     * @return a single predicate returning true if all predicates are returning true
     */
    public static <T> Predicate<T> predicateAndSum(ArrayList<Predicate<T>> preds){
        return preds.stream().reduce(x -> true, (p1,p2) -> p1.and(p2) );
    }


}
