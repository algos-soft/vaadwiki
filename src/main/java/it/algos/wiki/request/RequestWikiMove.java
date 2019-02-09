package it.algos.wiki.request;


import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;
import it.algos.wiki.WikiLoginOld;

import java.io.PrintWriter;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by gac on 27 nov 2015.
 * <p>
 * Action=move requires POST requests
 * GET requests will cause an error
 * <p>
 * Double request
 * <p>
 * <deprecated> First for obtaining movetoken
 * <deprecated> To move a page, a move token is required.
 * <deprecated> This token is equal to the edit token and the same for all pages, but changes at every login.
 * <deprecated> Moves tokens can be obtained via action=tokens with type=move (MW 1.20+), or by using the following method:
 * <deprecated> Obtaining a move token
 * <deprecated> api.php?action=query&prop=info&intoken=move&titles=Main%20Page
 * <p>
 * Gets tokens required by data-modifying actions.
 * If you request one of these actions without providing a token, the API returns an error code such as notoken.
 * This module does not use a prefix.
 * The csrf (cross-site request forgery) token corresponds to the majority of older tokens, like edit and move, that were retrieved using the API action tokens (deprecated in MediaWiki 1.24).
 * api.php?action=query&meta=tokens
 * <tokens csrftoken="00112233445566778899aabbccddeeff+\" />
 * <p>
 * Second with parameters:
 * -    from: Title of the page you want to move. Cannot be used together with fromid
 * -    fromid: Page ID of the page you want to move. Cannot be used together with from
 * -    to: Title you want to rename the page to
 * -    token: A move token previously retrieved through prop=info. Take care to urlencode the '+' as '%2B'.
 * -    reason: Reason for the move (optional)
 * -    movetalk: Move the talk page, if it exists
 * -    movesubpages: Move subpages, if applicable
 * -    noredirect: Don't create a redirect. Requires the suppressredirect right, which by default is granted only to bots and sysops
 * -    watch: Add the page and the redirect to your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.17)
 * -    unwatch: Remove the page and the redirect from your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.17)
 * -    watchlist: Unconditionally add or remove the page from your watchlist, use preferences or do not change watch (see API:Edit)
 * -    ignorewarnings: Ignore any warnings
 * <p>
 * Link: https://www.mediawiki.org/wiki/API:Move
 */
public class RequestWikiMove extends RequestWiki {

    private static final String TAG_MOVE = "&intoken=move";
    private static final String FROM = "&from=";
    private static final String TO = "&to=";
    private static final String REASON = "&reason=";
    private static final String TOKEN = "&token=";

    private static final String SUMMARY = "e ha modificato i wikilinks in entrata";

    private String oldTitle;
    private String newTitle;

    /**
     * Costruttore
     * Rinvia al costruttore completo
     *
     * @param oldTitle della pagina da spostare
     * @param newTitle definitivo della pagina
     * @deprecated
     */
    public RequestWikiMove(String oldTitle, String newTitle) {
        this(oldTitle, newTitle, SUMMARY);
    }// fine del metodo costruttore

    /**
     * Costruttore
     * Rinvia al costruttore completo for testing purpose only
     *
     * @param oldTitle della pagina da spostare
     * @param newTitle definitivo della pagina
     * @param summary  oggetto della modifica
     * @deprecated
     */
    public RequestWikiMove(String oldTitle, String newTitle, String summary) {
        this.doInit(oldTitle, newTitle, summary, null);
    }// fine del metodo costruttore completo

    /**
     * Costruttore for testing purpose only
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     * @param oldTitle  della pagina da spostare
     * @param newTitle  definitivo della pagina
     * @param summary   oggetto della modifica
     * @param loginTest del collegamento
     * @deprecated
     */
    public RequestWikiMove(String oldTitle, String newTitle, String summary, WikiLoginOld loginTest) {
        this.doInit(oldTitle, newTitle, summary, loginTest);
    }// fine del metodo costruttore completo


    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void doInit(String oldTitle, String newTitle, String summary, WikiLoginOld loginTest) {
        this.oldTitle = oldTitle;
        this.newTitle = newTitle;
        super.summary = summary;
        super.needPost = true;
        super.needLogin = true;
        super.needToken = true;
        super.needBot = true;
        super.needContinua = false;

        if (loginTest != null) {
            wikiLoginOld = loginTest;
        } else {
//            wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        }// end of if/else cycle

        if (wikiLoginOld == null) {
//            wikiLogin = VaadApp.WIKI_LOGIN;
        }// end of if cycle

        if (needLogin && wikiLoginOld == null) {
            risultato = TipoRisultato.noLogin;
            valida = false;
            return;
        }// end of if cycle

        super.doInit();
    } // fine del metodo

//    /**
//     * Costruisce la stringa della request
//     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda della sottoclasse)
//     * PUO essere sovrascritto nelle sottoclassi specifiche
//     *
//     * @return domain
//     */
//    protected String getDomain2() {
//        String domain;
//        String tag = "&prop=info&movetalk";
//
//        //@todo NON serve il titolo per ottenere il token
//        domain = API_BASE + tag;
//
//        return domain;
//    } // fine del metodo


    //--Costruisce il domain per l'URL dal pageid della pagina
    //--@return domain
    protected String getDomain() {
        String domain = "";
        String from = "";
        String to = "";
        String reason = "";
        String tag = "https://it.wikipedia.org/w/api.php?format=json&formatversion=2&action=move";

        try { // prova ad eseguire il codice
            from = URLEncoder.encode(oldTitle, "UTF-8");
            to = URLEncoder.encode(newTitle, "UTF-8");
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        domain += tag;
        domain += FROM;
        domain += from;
        domain += TO;
        domain += to;

        return domain;
    } // fine del metodo

    /**
     * Crea il POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void creaPost(URLConnection urlConn) throws Exception {
        PrintWriter out;

        out = new PrintWriter(urlConn.getOutputStream());
        String testoPost = "";

        if (csrfToken != null && !csrfToken.equals("")) {
            testoPost += "&token=" + csrfToken;
        }// end of if cycle

        // now we send the data POST
        out.print(testoPost);
        out.close();

    } // fine del metodo

    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void elaboraRisposta(String rispostaRequest) {
        super.elaboraRisposta(rispostaRequest);
        String errorMessage = LibWiki.getError(rispostaRequest);

        risultato = TipoRisultato.spostata;

        if (errorMessage.equals(TipoRisultato.noto.toString())) {
            risultato = TipoRisultato.noto;
            valida = false;
        }// end of if cycle

        if (errorMessage.equals(TipoRisultato.invalidtitle.toString())) {
            risultato = TipoRisultato.invalidtitle;
            valida = false;
        }// end of if cycle

        if (errorMessage.equals(TipoRisultato.selfmove.toString())) {
            risultato = TipoRisultato.selfmove;
            valida = false;
        }// end of if cycle

        if (errorMessage.equals(TipoRisultato.articleexists.toString())) {
            risultato = TipoRisultato.articleexists;
            valida = false;
        }// end of if cycle

        if (errorMessage.equals(TipoRisultato.protectedtitle.toString())) {
            risultato = TipoRisultato.protectedtitle;
            valida = false;
        }// end of if cycle

        if (errorMessage.equals(TipoRisultato.missingtitle.toString())) {
            risultato = TipoRisultato.missingtitle;
            valida = false;
        }// end of if cycle

    } // end of getter method

} // fine della classe
