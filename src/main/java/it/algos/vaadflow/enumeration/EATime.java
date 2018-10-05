package it.algos.vaadflow.enumeration;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 27-set-2018
 * Time: 16:23
 */
public enum EATime {

    meseShort("meseShort", "d-MM"),
    meseLong("meseLong", "d-MMM"),
    shortDate("short", "d-MM-yy"),
    normal("normal", "d-MMM-yy"),
    weekShort("weekShort", "EEE d"),
    weekLong("weekLong", "EEEE d"),
    medium("medium", "d-MMMM-yy"),
    lunga("lunga", "d-MMMM-yyy"),
    completa("completa", "EEEE, d-MMMM-yyy"),
    meseCorrente("meseCorrente", "MMMM yyy"),
    ;

    private String tag;
    private String pattern;


    EATime(String tag, String pattern) {
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
