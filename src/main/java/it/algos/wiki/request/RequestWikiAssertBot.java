package it.algos.wiki.request;


import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;

import java.util.HashMap;

/**
 * Created by gac on 22 nov 2015.
 * .
 */
public class RequestWikiAssertBot extends RequestWikiAssert {


    /* parametro API specifico */
    private static String API_BOT = "bot";

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     */
    public RequestWikiAssertBot() {
        this.doInit();
    }// fine del metodo costruttore completo


    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     */
    public RequestWikiAssertBot(HashMap<String, Object> cookiesMappa) {
        this.cookiesMappa = cookiesMappa;
        this.doInit();
    }// fine del metodo costruttore completo


//    /**
//     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
//     * PUO essere sovrascritto nelle sottoclassi specifiche
//     */
//    protected void doInit() {
//        super.needCookies = true;
//        super.doInit();
//    } // fine del metodo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        return "https://it.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&assert=bot";
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

        if (errorMessage.equals(TipoRisultato.assertbotfailed.toString())) {
            valida = false;
            risultato = TipoRisultato.assertbotfailed;
        } else {
            valida = true;
            risultato = TipoRisultato.loginBot;
        }// end of if/else cycle

        this.testoResponse = rispostaRequest;
    } // end of getter method


} // fine della classe
