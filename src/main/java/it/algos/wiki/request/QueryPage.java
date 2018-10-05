package it.algos.wiki.request;


import it.algos.wiki.Cost;
import it.algos.wiki.TipoRequest;
import it.algos.wiki.TipoRicerca;

/**
 * Query standard per leggere/scrivere il risultato di una pagina
 * NON legge le categorie
 * Usa il titolo della pagina o il pageid (a seconda della sottoclasse concreta utilizzata)
 * Legge o scrive (a seconda della sottoclasse concreta utilizzata)
 * Legge le informazioni base della pagina (oltre al risultato)
 * Legge una sola Pagina con le informazioni base
 * Necessita di Login per scrivere, non per leggere solamente
 */
public abstract class QueryPage extends QueryWiki {

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGEID = "&pageids=";

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flgas
     */
    public QueryPage(String titlepageid, TipoRicerca tipoRicerca) {
        super(titlepageid, tipoRicerca, TipoRequest.read);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse, specificando i flgas
     */
    public QueryPage(int pageid) {
        super(pageid, TipoRicerca.pageid, TipoRequest.read);
    }// fine del metodo costruttore

}// end of class
