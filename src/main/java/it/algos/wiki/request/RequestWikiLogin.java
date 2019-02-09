package it.algos.wiki.request;


import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;

import java.io.PrintWriter;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by gac on 20 nov 2015.
 * <p>
 * Logging in
 * <p>
 * Approved bots need to be logged in to make edits.
 * Although a bot can make read requests without logging in, bots that have completed testing should log in for all activities.
 * Bots logged in from an account with the bot flag can obtain more results per query from the Mediawiki API (api.php).
 * Most bot frameworks should handle login and cookies automatically, but if you are not using an existing framework, you will need to follow these steps.
 * <p>
 * For security, login data must be passed using the HTTP POST method.
 * Because parameters of HTTP GET requests are easily visible in URL, logins via GET are disabled.
 * <p>
 * To log a bot in using the MediaWiki API, 2 POST requests are needed:
 * <p>
 * Request 1
 * <p>
 * URL: http://en.wikipedia.org/w/api.php?action=login&format=xml
 * POST parameters:
 * lgname=BOTUSERNAME
 * lgpassword=BOTPASSWORD
 * <p>
 * If the password is correct, this will return a "NeedToken" result and a "token" parameter in XML form, as documented at API:Login.
 * Other output formats are available. It will also return HTTP cookies as described below.
 * <p>
 * Request 2
 * <p>
 * URL: http://en.wikipedia.org/w/api.php?action=login&format=xml
 * POST parameters:
 * lgname=BOTUSERNAME
 * lgpassword=BOTPASSWORD
 * lgtoken=TOKEN
 * <p>
 * where TOKEN is the token from the previous result.
 * The HTTP cookies from the previous request must also be passed with the second request.
 * <p>
 * A successful login attempt will result in the Wikimedia server setting several HTTP cookies.
 * The bot must save these cookies and send them back every time it makes a request (this is particularly crucial for editing).
 * On the English Wikipedia, the following cookies should be used: enwikiUserID, enwikiToken, and enwikiUserName.
 * The enwikisession cookie is required to actually send an edit or commit some change, otherwise the MediaWiki:Session fail preview error message will be returned.
 * <p>
 * Link: https://www.mediawiki.org/wiki/Manual:Creating_a_bot#Logging_in
 */
public class RequestWikiLogin extends RequestWiki {

    public static final String TOKEN = "token";
    public static final String RESULT = "result";
    public static final String SECOND_TOKEN = "lgtoken";
    public static final String COOKIE_PREFIX = "cookieprefix";
    public static final String SESSION_ID = "sessionid";
    public static final String USER_ID = "lguserid";
    public static final String USER_NAME = "lgusername";
    public static final String NOT_EXISTS = "NotExists";
    public static final String WRONG_PASS = "WrongPass";
    public static final String THROTTLED = "Throttled";

    /* azione API specifica */
    private static String API_LOGIN = "login";

    // nome utente (parametro in entrata)
    private String wikiName;

    // password dell'utente (parametro in entrata)
    private String wikiPassword;

    private String token;

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     */
    public RequestWikiLogin(String wikiName, String wikiPassword) {
        this.wikiName = wikiName;
        this.wikiPassword = wikiPassword;
        this.doInit();
    }// fine del metodo costruttore completo

    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    public void doInit() {
        super.needPost = true;
        super.needLogin = false;
        super.needToken = true;
        super.needContinua = false;
        super.needCookies = true;
        super.needBot = false;

        super.doInit();
    } // fine del metodo


    /**
     * Crea la connessione preliminary
     * <p>
     * Regola i parametri della connessione
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected URLConnection creaConnessionePreliminary() throws Exception {
        URLConnection urlConn = super.creaConnessione();
        String txtCookies = "";

        // regola le property
        if (wikiLoginOld != null) {
            txtCookies = wikiLoginOld.getStringCookies();
            urlConn.setRequestProperty("Cookie", txtCookies);
        }// end of if cycle

        return urlConn;
    } // fine del metodo

    /**
     * Stringa del browser per la request preliminary
     * <p>
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected String getDomainPreliminary() {
        return super.getDomain() + API_LOGIN;
    } // end of getter method

    /**
     * Crea il POST della request preliminary
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void creaPostPreliminary(URLConnection urlConn) throws Exception {
        PrintWriter out = new PrintWriter(urlConn.getOutputStream());
        String testoPost = "";

        testoPost += "lgname=";
        testoPost += wikiName;
        testoPost += "&lgpassword=";
        testoPost += wikiPassword;

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
    @Override
    protected boolean elaboraRispostaPreliminary(String rispostaRequest) {
        HashMap<String, Object> mappa = null;
        String errorMessage = LibWiki.getError(rispostaRequest);

        if (errorMessage.equals(TipoRisultato.mustbeposted.toString())) {
            risultato = TipoRisultato.mustbeposted;
            return false;
        }// end of if cycle

        mappa = LibWiki.creaMappaLogin(rispostaRequest);

        if (mappa != null && mappa.get(TOKEN) != null && mappa.get(TOKEN) instanceof String) {
            token = (String) mappa.get(TOKEN);
        }// fine del blocco if

        return true;
    } // end of getter method

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        return super.getDomain() + API_LOGIN;
    } // fine del metodo

    /**
     * Crea il POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void creaPost(URLConnection urlConn) throws Exception {
        PrintWriter out = new PrintWriter(urlConn.getOutputStream());
        String testoPost = "";

        testoPost += "lgname=";
        testoPost += wikiName;
        testoPost += "&lgpassword=";
        testoPost += wikiPassword;
        testoPost += "&lgtoken=";
        testoPost += token;

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
    @Override
    protected void elaboraRisposta(String rispostaRequest) {
        HashMap<String, Object> mappa = null;
        String risultatoTxt;
        RequestWikiAssert assertUser;
        RequestWikiAssert assertBot;
        TipoRisultato risultatoTmp;

        if (rispostaRequest != null && !rispostaRequest.equals("")) {
            this.testoResponse = rispostaRequest;
            mappa = LibWiki.creaMappaLogin(rispostaRequest);
        }// end of if cycle

        //--Se si è loggati come bot, si è automaticamente loggati anche come user
        if (mappa != null && mappa.get(LibWiki.RESULT) != null && mappa.get(LibWiki.RESULT) instanceof String) {
            risultatoTxt = (String) mappa.get(LibWiki.RESULT);
            if (risultatoTxt.equals(LibWiki.SUCCESS)) {
                valida = true;

                assertBot = new RequestWikiAssertBot(mappa);
                risultatoTmp = assertBot.getRisultato();

                if (risultatoTmp == TipoRisultato.loginBot) {
                    risultato = TipoRisultato.loginBot;
                } else {
                    assertUser = new RequestWikiAssertUser(mappa);
                    risultatoTmp = assertUser.getRisultato();

                    if (risultatoTmp == TipoRisultato.loginUser) {
                        risultato = TipoRisultato.loginUser;
                    } else {
                        risultato = TipoRisultato.noLogin;
                    }// end of if/else cycle
                }// end of if/else cycle
            }// end of if cycle

            if (risultatoTxt.equals(NOT_EXISTS)) {
                risultato = TipoRisultato.notExists;
            }// end of if cycle

            if (risultatoTxt.equals(WRONG_PASS)) {
                risultato = TipoRisultato.wrongPass;
            }// end of if cycle

            if (risultatoTxt.equals(THROTTLED)) {
                risultato = TipoRisultato.throttled;
            }// end of if cycle

        }// fine del blocco if

    } // end of getter method

} // fine della classe
