package it.algos.wiki.request;


import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;
import it.algos.wiki.WrapTime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 21 set 2015.
 * Query per leggere il timestamp di molte pagine tramite una lista di pageIds
 * Legge solamente
 * Mantiene una lista di pageIds e timestamps
 */
public class QueryTimestamp extends QueryWiki {

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP_PAGEIDS = "&prop=revisions&rvprop=timestamp&pageids=";


    //--lista di wrapper con pagesid e timestamp
    ArrayList<WrapTime> listaWrapTime;
    ArrayList<WrapTime> listaWrapTimeMissing;

    //--lista di errori  (titolo della voce)
    ArrayList listaErrori;

//    /**
//     * La stringa (unica) può avere come separatore il pipe oppure la virgola
//     */
//    public QueryTimestamp(String stringaPageIds) {
//        super(stringaPageIds, TipoRicerca.listaPageids, TipoRequest.read);
//    }// fine del metodo costruttore
//
//    public QueryTimestamp(String[] listaPageIds) {
//        super(array.fromStringToStringaPipe(listaPageIds), TipoRicerca.listaPageids, TipoRequest.read);
//    }// fine del metodo costruttore
//
//    public QueryTimestamp(ArrayList arrayPageIds) {
//        super(array.toStringaPipe(arrayPageIds), TipoRicerca.listaPageids, TipoRequest.read);
//    }// fine del metodo costruttore
//
//    public QueryTimestamp(List arrayPageIds) {
//        super(array.toStringaPipe((ArrayList)arrayPageIds), TipoRicerca.listaPageids, TipoRequest.read);
//    }// fine del metodo costruttore


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    protected String getDomain() {
        String domain = "";

        try { // prova ad eseguire il codice
            domain = API_BASE + TAG_PROP_PAGEIDS + stringaPageIds;
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return domain;
    } // fine del metodo

    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void regolaRisultato(String risultatoRequest) {
        HashMap<String, ArrayList<WrapTime>> mappa;

        mappa = LibWiki.creaArrayWrapTime(risultatoRequest);
        if (mappa != null) {
            this.setListaWrapTime(mappa.get(LibWiki.KEY_PAGINE_VALIDE));
            this.setListaWrapTimeMissing(mappa.get(LibWiki.KEY_PAGINE_MANCANTI));
        }// end of if cycle

        if (mappa != null) {
            risultato = TipoRisultato.letta;
        }// end of if cycle
        valida = (getListaWrapTimeMissing().size() < 1);

        this.continua = LibWiki.creaCatContinue(risultatoRequest);
    } // fine del metodo


    public ArrayList<WrapTime> getListaWrapTime() {
        return listaWrapTime;
    }// end of getter method

    public void setListaWrapTime(ArrayList<WrapTime> listaWrapTime) {
        this.listaWrapTime = listaWrapTime;
    }//end of setter method

    public ArrayList<WrapTime> getListaWrapTimeMissing() {
        return listaWrapTimeMissing;
    }// end of getter method

    public void setListaWrapTimeMissing(ArrayList<WrapTime> listaWrapTimeMissing) {
        this.listaWrapTimeMissing = listaWrapTimeMissing;
    }//end of setter method

    public ArrayList getListaErrori() {
        return listaErrori;
    }// end of getter method

    public void setListaErrori(ArrayList listaErrori) {
        this.listaErrori = listaErrori;
    }//end of setter method

} // fine della classe
