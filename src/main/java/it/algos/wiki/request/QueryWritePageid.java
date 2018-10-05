package it.algos.wiki.request;

/**
 * Query standard per scrivere il contenuto di una pagina
 * Usa il pageid della pagina
 * Necessita di Login per scrivere
 */
public class QueryWritePageid extends QueryWrite {
    /**
     * Costruttore completo
     * Rinvia al costruttore completo
     */
    public QueryWritePageid(String titlepageid, String testoNew) {
        this(titlepageid, testoNew, "");
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore completo
     */
    public QueryWritePageid(long pageid, String testoNew) {
        this(pageid, testoNew, "");
    }// fine del metodo costruttore


    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryWritePageid(String titlepageid, String testoNew, String summary) {
        super(titlepageid, testoNew, summary);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al costruttore della superclasse
     */
    public QueryWritePageid(long pageid, String testoNew, String summary) {
        super(pageid, testoNew, summary);
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

        domain = API_BASE + TAG_EDIT + TAG_PROP + TAG_PAGESID + pageid;

        return domain;
    } // fine del metodo

}// end of class
