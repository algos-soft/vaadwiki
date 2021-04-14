package it.algos.vaadflow14.backend.enumeration;


import it.algos.vaadflow14.backend.interfaces.AIEnum;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mar, 07-apr-2020
 * Time: 20:49
 */
public enum AETypeIconaEdit implements AIEnum {
    edit, search, play;


    /**
     * Stringa di valori (text) da usare per memorizzare la preferenza <br>
     * La stringa è composta da tutti i valori separati da virgola <br>
     * Poi, separato da punto e virgola, viene il valore corrente <br>
     *
     * @return stringa di valori e valore di default
     */
    @Override
    public String getPref() {
        String testo = VUOTA;

        for (AETypeIconaEdit eaEditIcona : AETypeIconaEdit.values()) {
            testo += eaEditIcona.name();
            testo += VIRGOLA;
        }

        testo = testo.substring(0, testo.length() - 1);
        testo += PUNTO_VIRGOLA;
        testo += name();

        return testo;
    }

}
