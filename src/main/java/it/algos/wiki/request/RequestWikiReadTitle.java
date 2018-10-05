package it.algos.wiki.request;

import java.net.URLEncoder;

/**
 * Created by gac on 20 nov 2015.
 * .
 */
public class RequestWikiReadTitle extends RequestWikiRead {


    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestWikiReadTitle(String wikiTitle) {
        this.wikiTitle = wikiTitle;
        super.doInit();
    }// fine del metodo costruttore


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
            domain +=  TAG_TITOLO + URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return domain;
    } // fine del metodo


} // fine della classe
