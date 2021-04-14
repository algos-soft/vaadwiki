package it.algos.vaadflow14.backend.enumeration;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 15-feb-2021
 * Time: 20:42
 */
public enum AETypeColor implements AIType {

    normale("black"),
    nero("black"),
    blu("blue"),
    verde("green"),
    rosso("red");

    private String value;


    /**
     * Costruttore completo con parametri.
     *
     * @param value del colore
     */
    AETypeColor(String value) {
        this.value = value;
    }

    @Override
    public String getTag() {
        return "color";
    }

    @Override
    public String get() {
        return value;
    }
}
