package it.algos.wiki.request;

/**
 * Classe concreta per le Request sul server di Wikipedia per leggere una o più pagine
 * Usa solo il GET
 * La ricerca può essere fatta col pageid, col title oppure con una lista di pageids
 */
public class RequestRead extends ARequest {

    /**
     * Costruttore completo
     *
     * @param wikiPageid pageid della pagina wiki su cui operare
     */
    public RequestRead(long wikiPageid) {
        super(wikiPageid);
    }// fine del metodo costruttore completo


    /**
     * Costruttore completo
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public RequestRead(String wikiTitle) {
        super(wikiTitle);
    }// fine del metodo costruttore completo


} // fine della classe
