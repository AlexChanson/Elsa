package request;

import beans.ComDepReg;

import java.util.HashMap;
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

            switch (op){
                case "==":
                case "=": break;
                case "<=": break;
                case ">=": break;
                case "<": break;
                case ">": break;
                case "!=":
                case "/=": break;
            }


        }

        return null;
    }


}
