package it.algos.wiki.request;


import it.algos.wiki.Api;
import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;

import java.util.ArrayList;

/**
 * Created by gac on 02 feb 2016.
 * .
 */
public class RequestLinks extends ARequest {

    protected static String TAG_BACK = "&list=backlinks&bllimit=max";
    protected static String TAG_NS = "&blnamespace=0";
    protected static String TAG_TITOLO_BACK_LINKS = "&bltitle=";


    //--lista di pagine linkate dalla pagina (tutti i namespace)
    private ArrayList<Long> listaAllPageids;
    private ArrayList<String> listaAllTitles;


    //--lista di pagine linkate dalla pagina (namespace=0)
    private ArrayList<Long> listaVociPageids;
    private ArrayList<String> listaVociTitles;


    /**
     * Costruttore completo
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestLinks(String wikiTitle) {
        super(wikiTitle);
    }// fine del metodo costruttore completo

    /**
     * Stringa del browser per la request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected String elaboraDomain() {
        String domainTmp = API_BASE + API_ACTION + API_QUERY + TAG_BACK;

        if (wikiTitle != null && !wikiTitle.equals("")) {
            domainTmp += TAG_TITOLO_BACK_LINKS + titleEncoded();
        }// end of if/else cycle

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
        listaPaginePageids = LibWiki.creaListaBackLongJson(rispostaRequest);
        listaPagineTitles = LibWiki.creaListaBackTxtJson(rispostaRequest);

        listaVociPageids = LibWiki.creaListaBackLongVociJson(rispostaRequest);
        listaVociTitles = LibWiki.creaListaBackTxtVociJson(rispostaRequest);

        if (listaPaginePageids != null && listaPagineTitles != null) {
            if (listaPaginePageids.size() == listaPagineTitles.size()) {
                risultato = TipoRisultato.letta;
                valida = true;
            }// end of if cycle
        } else {
            valida = false;
            if (Api.esiste(wikiTitle)) {
                risultato = TipoRisultato.letta;
            } else {
                risultato = TipoRisultato.nonTrovata;
            }// end of if/else cycle
        }// end of if/else cycle

    } // fine del metodo


    public ArrayList<Long> getListaAllPageids() {
        return listaAllPageids;
    }// end of getter method

    public ArrayList<String> getListaAllTitles() {
        return listaAllTitles;
    }// end of getter method

    public ArrayList<Long> getListaVociPageids() {
        return listaVociPageids;
    }// end of getter method

    public ArrayList<String> getListaVociTitles() {
        return listaVociTitles;
    }// end of getter method

} // fine della classe
