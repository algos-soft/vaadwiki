package it.algos.vaadflow.enumeration;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 27-set-2018
 * Time: 16:23
 */
public enum EATime {

    meseShort("meseShort", "d-MM", "5-10"),
    meseLong("meseLong", "d-MMM", "5-ott"),
    shortDate("short", "d-MM-yy", "5-10-14"),
    normal("normal", "d-MMM-yy", "5-ott-14"),
    weekShort("weekShort", "EEE d", "dom 5"),
    weekLong("weekLong", "EEEE d", "domenica 5"),
    medium("medium", "d-MMMM-yy", "5-ottobre-14"),
    lunga("lunga", "d-MMMM-yyy", "5-ottobre-2014"),
    completa("completa", "EEEE, d-MMMM-yyy", "domenica, 5-ottobre-2014"),
    meseCorrente("meseCorrente", "MMMM yyy", "ottobre 2014"),
    standard("standard", "d MMM yyyy", "20 gen 2019"),
    ;

    private String tag;

    private String pattern;


    EATime(String tag, String pattern, String nonUsato) {
        this.tag = tag;
        this.pattern = pattern;
    }// end of constructor


    public String getTag() {
        return tag;
    }// end of method


    public void setTag(String tag) {
        this.tag = tag;
    }// end of method


    public String getPattern() {
        return pattern;
    }// end of method


    public void setPattern(String pattern) {
        this.pattern = pattern;
    }// end of method

}// end of enumeration
