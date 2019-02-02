package it.algos.wiki.request;


import it.algos.wiki.LibWiki;

import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by gac on 22 nov 2015.
 * <p>
 * To make sure your bot doesn't edit while logged out, the action API provides an assert parameter which you can add to any request.
 * <p>
 * assert=user: Checks that your account is logged in
 * assert=bot: Checks that your account has the "bot" user right
 * If the assertion fails, error codes of assertuserfailed or assertbotfailed will be returned.
 * <p>
 * Checking if you are logged in
 * If you simply want to check whether you are logged into the action API, you can issue a request with parameters action=query&assert=user.
 * This will return a simple blank response ({} in the JSON format) if you are indeed logged in, or the assertuserfailed error if not.
 * Normally, you will not need to do this. Instead, add the assert=user parameter to all requests that should be made by a logged-in user.
 * <p>
 * @see https://www.mediawiki.org/wiki/API:Assert
 * <p>
 * Se si è loggati come bot, si è automaticamente loggati anche come user
 */
public abstract class RequestWikiAssert extends RequestWiki {

    /* parametro API specifico */
    private static String API_ASSERT = "&assert=";

    protected HashMap<String, Object> cookiesMappa;

    /**
     * Costruttore completo
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     */
    public RequestWikiAssert() {
//        this.doInit();
    }// fine del metodo costruttore completo


    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    public void doInit() {
        super.needCookies = true;
        super.doInit();
    } // fine del metodo

    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    protected void uploadCookies(URLConnection urlConn) {
//        cookies = cookiesMappa;
//        String txtCookies = this.getStringCookies(cookiesMappa);
//
//        super.uploadCookies(urlConn);
        urlConn.setRequestProperty("Cookie", LibWiki.creaCookiesText(cookiesMappa));

    } // fine del metodo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        return super.getDomain() + API_QUERY + API_ASSERT;
    } // fine del metodo

    /**
     * Restituisce i cookies
     */
    protected String getStringCookies(HashMap<String, Object> mappa) {
        String cookies = "";
        String sep = ";";
        String userName = "";
        long userId = 0;
        String token = "";
        String session = "";
        String cookieprefix = "";

        if (mappa != null) {
            if (mappa.get(RequestWikiLogin.COOKIE_PREFIX) != null && mappa.get(RequestWikiLogin.COOKIE_PREFIX) instanceof String) {
                cookieprefix = (String) mappa.get(RequestWikiLogin.COOKIE_PREFIX);
            }// end of if cycle
            if (mappa.get(RequestWikiLogin.USER_NAME) != null && mappa.get(RequestWikiLogin.USER_NAME) instanceof String) {
                userName = (String) mappa.get(RequestWikiLogin.USER_NAME);
            }// end of if cycle
            if (mappa.get(RequestWikiLogin.USER_ID) != null && mappa.get(RequestWikiLogin.USER_ID) instanceof Long) {
                userId = (Long) mappa.get(RequestWikiLogin.USER_ID);
            }// end of if cycle
            if (mappa.get(RequestWikiLogin.SECOND_TOKEN) != null && mappa.get(RequestWikiLogin.SECOND_TOKEN) instanceof String) {
                token = (String) mappa.get(RequestWikiLogin.SECOND_TOKEN);
            }// end of if cycle
            if (mappa.get(RequestWikiLogin.SESSION_ID) != null && mappa.get(RequestWikiLogin.SESSION_ID) instanceof String) {
                session = (String) mappa.get(RequestWikiLogin.SESSION_ID);
            }// end of if cycle

            cookies = cookieprefix;
            cookies += "UserName=";
            cookies += userName;
            cookies += sep;
            cookies += cookieprefix;
            cookies += "UserID=";
            cookies += userId;
            cookies += sep;
            cookies += cookieprefix;
            cookies += "Token=";
            cookies += token;
            cookies += sep;
            cookies += cookieprefix;
            cookies += "Session=";
            cookies += session;
        }// end of if cycle

        return cookies;
    } // fine della closure

} // fine della classe
