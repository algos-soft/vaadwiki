package it.algos.vaadwiki.enumeration;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 21-gen-2019
 * Time: 16:35
 */
public enum EADidascalia {
    giornoNato(VUOTA),
    giornoMorto(VUOTA),
    annoNato(VUOTA),
    annoMorto(VUOTA),
    listaNomi("Persone di nome "),
    listaCognomi("Persone di cognome"),
    listaAttivita("Progetto:Biografie/Attività/"),
    listaNazionalita("Progetto:Biografie/Nazionalità/"),
    biografie(VUOTA);

    public String pagina;


    EADidascalia(String pagina) {
        this.pagina = pagina;
    }//end of constructor

}// end of enum
