package it.algos.wiki.request;


import it.algos.wiki.TipoRicerca;

import java.net.URLEncoder;

/**
 * Query standard per leggere il risultato di una pagina
 * NON legge le categorie
 * Usa il titolo della pagina
 * Non necessita di Login
 */
public class QueryReadTitle extends QueryPage {

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryReadTitle(String title) {
        super(title, TipoRicerca.title);
    }// fine del metodo costruttore


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain = "";

        try { // prova ad eseguire il codice
            domain = API_BASE + TAG_PROP + TAG_TITOLO + URLEncoder.encode(title, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return domain;
    } // fine del metodo

}// end of class
