package it.algos.wiki.request;


import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;

import java.util.HashMap;

/**
 * Created by gac on 22 nov 2015.
 * .
 */
public class RequestWikiAssertUser extends RequestWikiAssert {


    /* parametro API specifico */
    private static String API_USER = "user";

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     */
    public RequestWikiAssertUser() {
        this.doInit();
    }// fine del metodo costruttore completo


    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     */
    public RequestWikiAssertUser(HashMap<String, Object> cookiesMappa) {
        this.cookiesMappa = cookiesMappa;
        this.doInit();
    }// fine del metodo costruttore completo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        return "https://it.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&assert=user";
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
        String errorMessage = LibWiki.getError(rispostaRequest);

        if (errorMessage.equals(TipoRisultato.assertuserfailed.toString())) {
            valida = false;
            risultato = TipoRisultato.assertuserfailed;
        } else {
            valida = true;
            risultato = TipoRisultato.loginUser;
        }// end of if/else cycle

        this.testoResponse = rispostaRequest;
    } // end of getter method

} // fine della classe
