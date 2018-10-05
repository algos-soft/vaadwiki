package it.algos.wiki.request;

import it.algos.wiki.WikiLogin;

import java.net.URLEncoder;

/**
 * Query standard per scrivere il contenuto di una pagina
 * Usa il titolo della pagina
 * Necessita di Login per scrivere
 */
public class QueryWriteTitle extends QueryWrite {

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWriteTitle(String title, String testoNew) {
        this(title, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryWriteTitle(String title, String testoNew, String summary) {
        this(title, testoNew, summary, null);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryWriteTitle(String title, String testoNew, String summary, WikiLogin login) {
        super(title, testoNew, summary, login);
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
        String titolo = "";

        try { // prova ad eseguire il codice
            titolo = URLEncoder.encode(title, "UTF-8");
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        domain = API_BASE + TAG_EDIT + TAG_PROP + TAG_TITOLO + titolo;

        return domain;
    } // fine del metodo

}// end of class
