package it.algos.wiki.request;


import it.algos.wiki.*;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by gac on 14 nov 2015.
 * <p>
 * Double request
 * First for obtaining movetoken
 * <p>
 * Second with parameters:
 * from: Title of the page you want to move. Cannot be used together with fromid
 * fromid: Page ID of the page you want to move. Cannot be used together with from
 * to: Title you want to rename the page to
 * token: A move token previously retrieved through prop=info. Take care to urlencode the '+' as '%2B'.
 * reason: Reason for the move (optional)
 * movetalk: Move the talk page, if it exists
 * movesubpages: Move subpages, if applicable
 * noredirect: Don't create a redirect. Requires the suppressredirect right, which by default is granted only to bots and sysops
 * watch: Add the page and the redirect to your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.17)
 * unwatch: Remove the page and the redirect from your watchlist. Deprecated. Use the watchlist argument (deprecated in 1.17)
 * watchlist: Unconditionally add or remove the page from your watchlist, use preferences or do not change watch (see API:Edit)
 * ignorewarnings: Ignore any warnings
 */
public class QueryMove extends QueryWiki {

    protected static String TAG_MOVE = "&intoken=move";
    protected static String FROM = "&from=";
    protected static String TO = "&to=";
    protected static String REASON = "&reason=";
    protected static String TOKEN = "&token=";

    private String oldTitle;
    private String newTitle;
    private String token;

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryMove(String oldTitle, String newTitle) {
        this(oldTitle, newTitle, "e ha modificato i wikilinks in entrata");
    }// fine del metodo costruttore

    /**
     * Costruttore
     * Rinvia al costruttore completo
     */
    public QueryMove(String oldTitle, String newTitle, String summary) {
        this(oldTitle, newTitle, summary, null);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     * Rinvia al medodo iniziale della superclasse
     */
    public QueryMove(String oldTitle, String newTitle, String summary, WikiLogin login) {
        this.doInit(oldTitle, newTitle, summary, login);
    }// fine del metodo costruttore

    protected void doInit(String oldTitle, String newTitle, String summary, WikiLogin login) {
        super.tipoRicerca = TipoRicerca.title;
        super.tipoRequest = TipoRequest.write;
        this.oldTitle = oldTitle;
        this.newTitle = newTitle;
        super.summary = summary;
        super.serveLogin = true;

        if (oldTitle == null || oldTitle.equals("") || newTitle == null || newTitle.equals("")) {
            risultato = TipoRisultato.erroreGenerico;
            valida = false;
            return;
        }// end of if cycle

        if (login == null) {
//            wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        } else {
            wikiLogin = login;
        }// end of if/else cycle

        if (serveLogin && wikiLogin == null) {
            risultato = TipoRisultato.noLogin;
            valida = false;
            return;
        }// end of if cycle

        domain = this.getDomain();
        super.doInit();
    } // fine del metodo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain;
        String tag = "&prop=info&movetalk";

        //@todo NON serve il titolo per ottenere il token
        domain = API_BASE + TAG_EDIT + tag;

        return domain;
    } // fine del metodo

    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void regolaRisultato(String risultatoRequest) {
        HashMap mappa = null;
        super.regolaRisultato(risultatoRequest);

        if (risultatoRequest != null) {
            mappa = LibWiki.creaMappaQuery(risultatoRequest);
        }// fine del blocco if

        //@todo Nella primaRequest serve SOLO il token

        if (mappa != null) {
            if (mappa.get(PagePar.csrftoken.toString()) != null && mappa.get(PagePar.csrftoken.toString()) instanceof String && !mappa.get(PagePar.csrftoken.toString()).equals("")) {
                token = (String) mappa.get(PagePar.csrftoken.toString());
                setRisultato(TipoRisultato.letta); //@todo va aggiunto un valore alla enum -> tokenValido
                valida = true;
            } else {
                setRisultato(TipoRisultato.nonTrovata);
                valida = false;
            }// end of if/else cycle
        }// fine del blocco if
    } // fine del metodo

    //--Costruisce il domain per l'URL dal pageid della pagina
    //--@return domain
    protected String getSecondoDomain() {
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

        if (from.equals("") || to.equals("")) {
            setRisultato(TipoRisultato.erroreGenerico);
            valida = false;
            return "";
        }// end of if cycle

        if (token.equals("")) {
            setRisultato(TipoRisultato.nonTrovata);
            valida = false;
            return "";
        }// end of if cycle

        if (summary != null && !summary.equals("")) {
            try { // prova ad eseguire il codice
                reason = URLEncoder.encode(summary, "UTF-8");
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if

        domain += tag;
        domain += FROM;
        domain += from;
        domain += TO;
        domain += to;
        domain += REASON;
        domain += reason;

        return domain;
    } // fine del metodo


    /**
     * Restituisce il testo del POST per la seconda Request
     * Aggiunge il token provvisorio ricevuto dalla prima Request
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return post
     */
    @Override
    protected String getSecondoPost() {
        String testoPost = "";

        testoPost += "&token=" + token;

        return testoPost;
    } // fine della closure

    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void regolaRisultatoSecondo(String risultatoRequest) {
        super.regolaRisultatoSecondo(risultatoRequest);
        this.elaboraSecondaRequest(risultatoRequest);
    } // end of getter method

    /**
     * Elabora la risposta alla seconda Request
     *
     * @param testoRisposta alla prima Request
     */
    protected void elaboraSecondaRequest(String testoRisposta) {
        boolean noChange;
        HashMap mappa = LibWiki.creaMappaMove(testoRisposta);

        if (testoRisposta.equals("")) {
            risultato = TipoRisultato.erroreGenerico;
            return;
        }// end of if cycle

        risultato = TipoRisultato.spostata;
        valida = true;
        if (mappa != null && mappa.get(LibWiki.CODE) != null) {
            valida = false;

            if (mappa.get(LibWiki.CODE).equals(TipoRisultato.selfmove.toString())) {
                risultato = TipoRisultato.selfmove;
            }// end of if cycle

            if (mappa.get(LibWiki.CODE).equals(TipoRisultato.articleexists.toString())) {
                risultato = TipoRisultato.articleexists;
            }// end of if cycle

            if (mappa.get(LibWiki.CODE).equals(TipoRisultato.protectedtitle.toString())) {
                risultato = TipoRisultato.protectedtitle;
            }// end of if cycle
        }// end of if cycle
    } // fine del metodo

} // fine della classe
