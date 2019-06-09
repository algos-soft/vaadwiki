package it.algos.vaadflow.enumeration;

import java.util.ArrayList;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Thu, 16-May-2019
 * Time: 16:37
 */
public enum EAColor {
    white("White", "#FFFFFF"),
    silver("Silver", "#C0C0C0"),
    gray("Gray", "#808080"),
    black("Black", "#000000"),
    red("Red", "#FF0000"),
    maroon("Maroon", "#800000"),
    yellow("Yellow", "#FFFF00"),
    olive("Olive", "#808000"),
    lime("Lime", "#00FF00"),
    green("Green", "#008000"),
    aqua("Aqua", "#00FFFF"),
    teal("Teal", "#008080"),
    blue("Blue", "#0000FF"),
    navy("Navy", "#000080"),
    fuchsia("Fuchsia", "#FF00FF"),
    purple("Purple", "#800080"),
    ;

    private String tag;

    private String esadecimale;


    EAColor(String tag, String esadecimale) {
        this.tag = tag;
        this.esadecimale = esadecimale;
    }// end of constructor


    public static ArrayList<EAColor> getColors() {
        ArrayList<EAColor> lista = new ArrayList<>();

        for (EAColor color : EAColor.values()) {
            lista.add(color);
        }// end of for cycle

        return lista;
    }// end of static method


    public String getTag() {
        return tag;
    }// end of method


    public String getEsadecimale() {
        return esadecimale;
    }
//    ArrayList<String> items = new ArrayList();
//        for (EAColor color : EAColor.values()) {
//        items.add(color.name());
//    }// end of for cycle

}// end of enum class
