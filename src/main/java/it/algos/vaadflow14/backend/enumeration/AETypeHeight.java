package it.algos.vaadflow14.backend.enumeration;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 20-feb-2021
 * Time: 17:40
 */
public enum AETypeHeight implements AIType, AIPref {

    normal("1"),
    number16("1.6"),
    number20("2"),
    px6("6px"),
    px10("10px"),
    px14("14px"),
    px18("18px"),
    px22("22px"),
    cento80("80%"),
    cento120("120%"),
    ;

    private String value;

    /**
     * Costruttore completo con parametri.
     *
     * @param value del colore
     */
    AETypeHeight(String value) {
        this.value = value;
    }

    @Override
    public String getTag() {
        return "font-size";
    }

    @Override
    public String get() {
        return value;
    }

    /**
     * Stringa di valori (text) da usare per memorizzare la preferenza <br>
     * La stringa è composta da tutti i valori separati da virgola <br>
     * Poi, separato da punto e virgola viene il valore selezionato di default <br>
     *
     * @return stringa di valori e valore di default
     */
    @Override
    public String getPref() {
        String testo = VUOTA;

        for (AETypeHeight eaTypeHeight : AETypeHeight.values()) {
            testo += eaTypeHeight.get();
            testo += VIRGOLA;
        }

        testo = testo.substring(0, testo.length() - 1);
        testo += PUNTO_VIRGOLA;
        testo += name();

        return testo;
    }
}
