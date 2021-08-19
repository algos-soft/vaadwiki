package it.algos.vaadwiki.backend.enumeration;


import static it.algos.vaadflow14.backend.application.FlowCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 21-gen-2019
 * Time: 16:35
 */
public enum EDidascalia {
    giornoNato(VUOTA, false, "", ""),
    giornoMorto(VUOTA, false, "", ""),
    annoNato(VUOTA, false, "", ""),
    annoMorto(VUOTA, false, "", ""),
    listaNomi("Persone di nome ", true, "Liste di persone per nome", "prenome"),
    listaCognomi("Persone di cognome ", true, "Liste di persone per cognome", "cognome"),
    listaAttivita("Progetto:Biografie/Attività/", false, "Bio attività", ""),
    listaNazionalita("Progetto:Biografie/Nazionalità/", false, "Bio nazionalità", ""),
    biografie(VUOTA, true, "", "");

    public String pagina;

    public boolean isProfessione;

    public String categoria;

    public String tag;


    EDidascalia(String pagina, boolean isProfessione, String categoria, String tag) {
        this.pagina = pagina;
        this.isProfessione = isProfessione;
        this.categoria = categoria;
        this.tag = tag;
    }//end of constructor

}// end of enum
