package it.algos.wiki.request;


import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;
import it.algos.wiki.WrapTime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 01 feb 2016.
 * .
 */
public class RequestTime extends ARequest {

    //--tag per la costruzione della stringa della request
    private static String TAG_PROP_PAGEIDS = "&prop=revisions&rvprop=timestamp&pageids=";

    //--stringa (separata da pipe oppure da virgola) delle pageids
    private String stringaPageIds;



//    /**
//     * Costruttore completo
//     *
//     * @param listaPageIds elenco di pageids (long)
//     */
//    public RequestTime(long[] listaPageIds) {
//        this(LibArray.fromLong(listaPageIds));
//    }// fine del metodo costruttore completo


//    /**
//     * Costruttore completo
//     *
//     * @param arrayPageIds elenco di pageids (ArrayList)
//     */
//    public RequestTime(ArrayList<Long> arrayPageIds) {
//        this(LibArray.toStringaPipe(arrayPageIds));
//    }// fine del metodo costruttore completo


    /**
     * Costruttore completo
     *
     * @param stringaPageIds stringa (separata da pipe oppure da virgola) delle pageids
     */
    public RequestTime(String stringaPageIds) {
        super(stringaPageIds);
    }// fine del metodo costruttore completo


    /**
     * Stringa del browser per la request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected String elaboraDomain() {
        String domainTmp = API_BASE + API_ACTION + API_QUERY;

        if (wikiTitle.contains(",")) {
//            wikiTitle = LibText.sostituisce(wikiTitle, ",", "|");
        }// end of if/else cycle
        domainTmp += TAG_PROP_PAGEIDS + wikiTitle;

        if (needBot) {
            domainTmp += API_ASSERT;
        }// end of if cycle

        domain = domainTmp;
        return domainTmp;
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void elaboraRisposta(String rispostaRequest) {
        HashMap<String, ArrayList<WrapTime>> mappa;
        mappa = LibWiki.creaArrayWrapTime(rispostaRequest);

        if (mappa != null) {
            listaWrapTime = mappa.get(LibWiki.KEY_PAGINE_VALIDE);
            listaWrapTimeMissing = mappa.get(LibWiki.KEY_PAGINE_MANCANTI);
            risultato = TipoRisultato.letta;
            valida = true;
        } else {
            risultato = TipoRisultato.nonTrovata;
            valida = false;
        }// end of if/else cycle

    } // fine del metodo


} // fine della classe
