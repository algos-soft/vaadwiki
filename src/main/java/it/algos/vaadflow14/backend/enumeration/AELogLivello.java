package it.algos.vaadflow14.backend.enumeration;


import it.algos.vaadflow14.backend.interfaces.AIEnum;

import static com.vaadin.flow.server.frontend.FrontendUtils.GREEN;
import static com.vaadin.flow.server.frontend.FrontendUtils.RED;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static it.algos.vaadflow14.backend.service.ConsoleColorService.BLUE;
import static it.algos.vaadflow14.backend.service.ConsoleColorService.PURPLE;

/**
 * Created by gac on 22 ago 2015.
 */
public enum AELogLivello implements AIEnum {

    debug(GREEN),
    info(BLUE),
    warn(PURPLE),
    error(RED),
    ;

    public String color;


    AELogLivello(String color) {
        this.color = color;
    }


    /**
     * Stringa di valori (text) da usare per memorizzare la preferenza <br>
     * La stringa è composta da tutti i valori separati da virgola <br>
     * Poi, separato da punto e virgola, viene il valore corrente <br>
     *
     * @return stringa di valori e valore di default
     */
    @Override
    public String getPref() {
        StringBuilder testo = new StringBuilder(VUOTA);


        for (AELogLivello aeLogLivello : AELogLivello.values()) {
            testo.append(aeLogLivello.name());
            testo.append(VIRGOLA);
        }

        testo = new StringBuilder(testo.substring(0, testo.length() - 1));
        testo.append(PUNTO_VIRGOLA);
        testo.append(this.name());

        return testo.toString();
    }

    //@todo Funzionalità ancora da implementare
    //    /**
    //     * Azione memorizzata nelle preferenze <br>
    //     *
    //     * @return azione
    //     */
    //    public static EALogLivello get(PreferenzaService pref) {
    //        EALogLivello eaLogLevl = null;
    //        String livelloStr = VUOTA;
    //
    //        if (pref != null) {
    //            livelloStr = pref.getStr(EAPreferenza.logLevelCorrente);
    //        }// end of if cycle
    //
    //        if (livelloStr.length() > 0) {
    //            eaLogLevl = EALogLivello.valueOf(livelloStr);
    //        }// end of if cycle
    //
    //        return eaLogLevl;
    //    }// end of method

}
