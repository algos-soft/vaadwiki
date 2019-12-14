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
    giornoNato(VUOTA, false),
    giornoMorto(VUOTA, false),
    annoNato(VUOTA, false),
    annoMorto(VUOTA, false),
    listaNomi("Persone di nome ", true),
    listaCognomi("Persone di cognome ", true),
    listaAttivita("Progetto:Biografie/Attività/", true),
    listaNazionalita("Progetto:Biografie/Nazionalità/", true),
    biografie(VUOTA, true);

    public String pagina;

    public boolean isProfessione;


    EADidascalia(String pagina, boolean isProfessione) {
        this.pagina = pagina;
        this.isProfessione = isProfessione;
    }//end of constructor

}// end of enum
