package it.algos.vaadwiki.enumeration;

import it.algos.vaadflow.enumeration.EANewText;
import it.algos.vaadflow.enumeration.IAEnum;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 10-gen-2020
 * Time: 17:22
 */
public enum EAElabora implements IAEnum {
    ordinaNormaliNoLoss, parametriRipuliti, parametriModificati,
    ;


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

        for (EAElabora eaElabora : EAElabora.values()) {
            testo += eaElabora.name();
            testo += VIRGOLA;
        }// end of for cycle

        testo = testo.substring(0, testo.length() - 1);
        testo += PUNTO_VIRGOLA;
        testo += name();

        return testo;
    }// end of method

}// end of enumeration class
