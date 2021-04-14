package it.algos.vaadflow14.backend.enumeration;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 15-feb-2021
 * Time: 20:54
 */
public enum AETypeSize implements AIType {

    medium("medium"),
    xxSmall("xx-small"),
    xSmall("x-small"),
    small("small"),
    smaller("smaller"),
    large("large"),
    xLarge("x-large"),
    xxLarge("xx-large"),
    ;

    private String value;

    /**
     * Costruttore completo con parametri.
     *
     * @param value del colore
     */
    AETypeSize(String value) {
        this.value = value;
    }

    @Override
    public  String getTag() {
        return "font-size";
    }

    @Override
    public String get() {
        return value;
    }

}
