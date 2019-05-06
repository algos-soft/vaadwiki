package it.algos.wiki.request;


import it.algos.wiki.Api;
import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by gac on 08 nov 2015.
 * <p>
 * Rif: https://www.mediawiki.org/wiki/API:Backlinks
 * Lists pages that link to a given page, similar to Special:Whatlinkshere. Ordered by linking page title.
 * <p>
 * Parametrs:
 * bltitle: List pages linking to this title. The title does not need to exist.
 * blnamespace: Only list pages in these namespaces
 * blfilterredir: How to filter redirects (Default: all)
 * - all: List all pages regardless of their redirect flag
 * - redirects: Only list redirects
 * - nonredirects: Don't list redirects
 * bllimit: Maximum amount of pages to list. Maximum limit is halved if blredirect is set. No more than 500 (5000 for bots) allowed. (Default: 10)
 * blredirect: If set, pages linking to bltitle through a redirect will also be listed. See below for more detailed information.
 * blcontinue: Used to continue a previous request
 * <p>
 * Es:
 * https://it.wikipedia.org/w/api.php?action=query&list=backlinks&bltitle=Piozzano&format=jsonfm
 */
public class RequestWikiBacklinks extends RequestWiki {

    protected static String TAG_BACK = "&list=backlinks&bllimit=max";
    protected static String TAG_NS = "&blnamespace=0";
    protected static String TAG_TITOLO = "&bltitle=";


    //--lista di pagine linkate dalla pagina (tutti i namespace)
    private ArrayList<Long> listaAllPageids;
    private ArrayList<String> listaAllTitles;


    //--lista di pagine linkate dalla pagina (namespace=0)
    private ArrayList<Long> listaVociPageids;
    private ArrayList<String> listaVociTitles;

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     * @deprecated
     */
    public RequestWikiBacklinks(String wikiTitle) {
        this.wikiTitle = wikiTitle;
        super.doInit();
    }// fine del metodo costruttore

    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    public void doInit() {
        super.needPost = false;
        super.needLogin = false;
        super.needToken = false;
        super.needContinua = false;
        super.doInit();
    } // fine del metodo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda della sottoclasse)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain = super.getDomain();

        try { // prova ad eseguire il codice
            domain += API_QUERY + TAG_BACK + TAG_TITOLO + URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return domain;
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
        super.elaboraRisposta(rispostaRequest);

        listaAllPageids = LibWiki.creaListaBackLongJson(rispostaRequest);
        listaAllTitles = LibWiki.creaListaBackTxtJson(rispostaRequest);

        listaVociPageids = LibWiki.creaListaBackLongVociJson(rispostaRequest);
        listaVociTitles = LibWiki.creaListaBackTxtVociJson(rispostaRequest);

        if (listaAllPageids != null && listaAllTitles != null) {
            if (listaAllPageids.size() == listaAllTitles.size()) {
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


    public ArrayList<Long> getListaVociPageids() {
        return listaVociPageids;
    }// end of getter method

    public ArrayList<String> getListaVociTitles() {
        return listaVociTitles;
    }// end of getter method

    public ArrayList<Long> getListaAllPageids() {
        return listaAllPageids;
    }// end of getter method

    public ArrayList<String> getListaAllTitles() {
        return listaAllTitles;
    }// end of getter method

} // fine della classe
