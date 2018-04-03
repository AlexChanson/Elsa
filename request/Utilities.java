package request;

import beans.ComDepReg;
import beans.Commune;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public final static Pattern predPattern = Pattern.compile(
            "\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*(=|==|<=|>=|>|<|!=|/=)\\s*('[^\"']*'|\"[^\"']*\"|[0-9]+\\.[0-9]+|[0-9]+)\\s*");

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
        commStringGetters.put("ev_pop", combine(Commune::getEvolution_pop,String::toLowerCase));
        commStringGetters.put("env_demo", combine(Commune::getEnv_demo, String::toLowerCase));
        commStringGetters.put("fidelite", combine(Commune::getFidelite, String::toLowerCase));
    }

    public static <T1, T2, T3> Function<T1, T3> combine(
            Function<T1, T2> first,
            Function<T2, T3> second) {
        return first.andThen(second);
    }

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

        return commLongGetters.get(attribute);
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

    public static <T> Predicate<T> makeLessThanPredicate(Function<T, Long> getter, long val){
        return x -> getter.apply(x) < val;
    }

    public static <T> Predicate<T> makeGreaterThanPred(Function<T, Long> getter, long val){
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
                                                        Function<T, Long> getter,
                                                        long val){
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

    public static Predicate<ComDepReg> promoteCommPredicate(Predicate<Commune> pred){
        return x -> pred.test(x.getCommune());
    }

    public static Predicate<ComDepReg> parsePredicate(String pred){

        Matcher m = predPattern.matcher(pred);
        if (m.matches()){

            String attr = m.group(1);
            String op = m.group(2);
            String val = m.group(3);

            boolean isNumber = true;

            // remove quotes for strings
            if (val.startsWith("\"") || val.startsWith("'")){
                isNumber = false;
                val = val.substring(1,val.length()-1).toLowerCase(); // set value to lowercase
            }

            // check for floating number
            boolean isFloating = val.contains(".");

            if (isNumber ){
                if (isFloating){
                    Function<Commune, Double> getter = doubleCommuneGetter(attr);
                    Predicate<Commune> finalPred = predicateOperator(op, getter, Double.parseDouble(val));
                    if (getter == null || finalPred == null){
                        return null;
                    }
                    return promoteCommPredicate(finalPred);
                }
                else {
                    Function<Commune, Long> getter = longCommuneGetter(attr);

                    Predicate<Commune> finalPred = predicateOperator(op, getter, Long.parseLong(val));
                    if (getter == null || finalPred == null){
                        return null;
                    }
                    return promoteCommPredicate(finalPred);

                }
            }else {
                Function<Commune, String> getter = stringCommuneGetter(attr);
                Predicate<Commune> finalPred = predicateOperator(op, getter, val);
                if (getter == null || finalPred == null){
                    return null;
                }
                return promoteCommPredicate(finalPred);
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
