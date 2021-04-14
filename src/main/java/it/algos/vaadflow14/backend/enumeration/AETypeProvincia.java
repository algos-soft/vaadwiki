package it.algos.vaadflow14.backend.enumeration;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: dom, 05-lug-2020
 * Time: 07:47
 */
public enum AETypeProvincia {

    provincia("Provincia", VUOTA),
    autonoma("Provincia autonoma", "TAA"),
    metropolitana("Città metropolitana", VUOTA),
    friuli("Associazione di comuni", "FVG"),
    consorzio("Libero consorzio comunale", "SIC"),
    regione("Regione autonoma", "VAO"),
    ;

    private String tag;

    private String siglaRegione;


    /**
     * Costruttore completo con parametri.
     *
     * @param tag della provincia
     */
    AETypeProvincia(String tag, String siglaRegione) {
        this.tag = tag;
        this.siglaRegione = siglaRegione;
    }


    /**
     * Stringa di valori (text) da usare per memorizzare la preferenza <br>
     * La stringa è composta da tutti i valori separati da virgola <br>
     * Poi, separato da punto e virgola, viene il valore corrente <br>
     *
     * @return stringa di valori e valore di default
     */
    public static String getPref() {
        String testo = VUOTA;

        for (AETypeProvincia eaType : AETypeProvincia.values()) {
            testo += eaType.name();
            testo += VIRGOLA;
        }

        return testo;
    }


    public static AETypeProvincia findBySigla(String siglaRegione) {
        AETypeProvincia type = null;

        for (AETypeProvincia eaType : AETypeProvincia.values()) {
            if (eaType.getSiglaRegione().equals(siglaRegione)) {
                type = eaType;
            }
        }

        return type;
    }


    public static List<AETypeProvincia> getItems() {
        List<AETypeProvincia> lista = new ArrayList<>();

        for (AETypeProvincia eaType : AETypeProvincia.values()) {
            lista.add(eaType);
        }

        return lista;
    }


    public static List<String> getStringItems() {
        List<String> lista = new ArrayList<>();

        for (AETypeProvincia eaType : AETypeProvincia.values()) {
            lista.add(eaType.tag);
        }

        return lista;
    }


    /**
     * Seleziona una enumeration dal tag <br>
     *
     * @param tag di riferimento
     *
     * @return enumeration selezionata
     */
    public static AETypeProvincia getTipo(String tag) {
        for (AETypeProvincia tipoTmp : values()) {
            if (tipoTmp.tag.toLowerCase().equals(tag.toLowerCase())) {
                return tipoTmp;
            }
        }

        return null;
    }


    public String getTag() {
        return tag;
    }


    public String getSiglaRegione() {
        return siglaRegione;
    }


    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return tag;
    }
}
