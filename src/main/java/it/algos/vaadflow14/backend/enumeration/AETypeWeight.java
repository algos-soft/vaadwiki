package it.algos.vaadflow14.backend.enumeration;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 15-feb-2021
 * Time: 20:46
 */
public enum AETypeWeight implements AIType {

    normal("normal"),
    bold("bold"),
    bolder("bolder"),
    lighter("lighter"),
    w100("100"),
    w200("200"),
    w300("300"),
    w400("400"),
    w500("500"),
    w600("600"),
    w700("700"),
    w800("800"),
    w900("900");

    private String value;

    /**
     * Costruttore completo con parametri.
     *
     * @param value del 'grassetto''
     */
    AETypeWeight(String value) {
        this.value = value;
    }

    @Override
    public  String getTag() {
        return "font-weight";
    }

    @Override
    public String get() {
        return value;
    }

}
