package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.LibWiki;
import it.algos.wiki.WikiLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:36
 *
 * @see https://www.mediawiki.org/wiki/OAuth/Owner-only_consumers
 * @see https://en.wikipedia.org/wiki/Help:Creating_a_bot
 *
 *
 * Request 1
 *
 *     URL: https://it.wikipedia.org/w/api.php?action=login&format=xml
 *     POST parameters:
 *         lgname=BOTUSERNAME
 *         lgpassword=BOTPASSWORD
 *
 * If the password is correct, this will return a "NeedToken" result and a "token" parameter in XML form, as documented at mw:API:Login. Other output formats are available. It will also return HTTP cookies as described below.
 *
 * Request 2
 *
 *     URL: https://it.wikipedia.org/w/api.php?action=login&format=xml
 *     POST parameters:
 *         lgname=BOTUSERNAME
 *         lgpassword=BOTPASSWORD
 *         lgtoken=TOKEN
 *
 *
 *
 *
 *
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AQueryLogin extends AQueryWiki {

    public static final String FIRST_NEW_TOKEN = "tokens";

    public static final String LOGIN_TOKEN = "logintoken";

    private final static String FIRST_REQUEST_GET = TAG_QUERY + "meta=tokens&type=login";

    private final static String SECOND_REQUEST_POST = "https://it.wikipedia.org/w/api.php?action=login&format=xml";

    private String logintoken;

    private String lgname = "Biobot";

    private String lgpassword = "fulvia68@lhgfmeb8ckefkniq85qmhul18r689nbq";

    private String lgtoken;

    // ci metto tutti i cookies restituiti da URLConnection.responses
    protected LinkedHashMap cookies;

    /**
     * Request principale
     * Quella base usa solo il GET
     * In alcune request (non tutte) si aggiunge anche il POST
     */
    public WikiLogin urlRequest() {
        WikiLogin login = null;

        //--recupera il logintoken necessario per la seconda request
        primaRequest();

        secondaRequest();

        return login;
    } // fine del metodo


    /**
     * Prima request.
     * Richiede un token, specializzato per il login
     * Request di tipo GET
     * Risposta in formato testo JSON
     * Recupera i cookies passati nella risposta
     * Recupera il logintoken necessario per la seconda request
     */
    public void primaRequest() {
        //--la prima request non ha bisogno del POST
        super.isUsaPost = false;
        super.isUploadCookies = false;
        super.isDownloadCookies = true;

        String textJSON = super.urlRequest(FIRST_REQUEST_GET);

        HashMap<String, Object> mappa = LibWiki.creaMappaLogin(textJSON);
        logintoken = LibWiki.getLoginToken(mappa);
    } // fine del metodo


    /**
     * Seconda request.
     * Request di tipo POST
     */
    public void secondaRequest() {
        String risposta;
//        URLConnection urlConn;
//        String tag = getTagIniziale();
//        String indirizzoWebCompleto = "";
        //--la seconda request ha bisogno del POST
        super.isUsaPost = true;
        super.isUploadCookies = true;
        super.isDownloadCookies = true;

        if (text.isValid(logintoken)) {
            lgtoken = logintoken;
//            super.urlRequest(SECOND_REQUEST_POST);
            risposta = super.urlRequest(TAG_API );
//            risposta = super.urlRequest(FIRST_REQUEST_GET);
            int a = 87;

        }// end of if cycle

        try { // prova ad eseguire il codice
//            urlLoginConnection=
//            urlConn = urlLoginConnection.esegue();
//            risposta = urlRequest.esegue(urlConn);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch
    } // fine del metodo


    /**
     * Crea il testo del POST della request
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected String elaboraPost() {
        String testoPost = "";

        testoPost += "lgname=";
        testoPost += lgname;
        testoPost += "&lgpassword=";
        testoPost += lgpassword;
        testoPost += "&lgtoken=";
        testoPost += lgtoken;

        return testoPost;
    }// end of method


    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    protected void uploadCookies(URLConnection urlConn) {
        HashMap cookies = this.cookies;
        Object[] keyArray;
        Object[] valArray;
        Object sessionObj = null;
        String sesionTxt = "";
        String sep = "=";
        Object valObj = null;
        String valTxt = "";

        // controllo di congruità
        if (urlConn != null) {
            if (cookies != null && cookies.size() > 0) {

                keyArray = cookies.keySet().toArray();
                if (keyArray.length > 0) {
                    sessionObj = keyArray[0];
                }// fine del blocco if
                if (sessionObj != null && sessionObj instanceof String) {
                    sesionTxt = (String) sessionObj;
                }// fine del blocco if

                valArray = cookies.values().toArray();
                if (valArray.length > 0) {
                    valObj = valArray[0];
                }// fine del blocco if
                if (valObj != null && valObj instanceof String) {
                    valTxt = (String) valObj;
                }// fine del blocco if

//               String  txtCookies=" itwikiUserName=Gac; itwikiUserID=399; centralauth_User=Gac; centralauth_Session=aa5f3ad00ae724ef5c6ba7096732f950";
//                urlConn.setRequestProperty("Cookie", txtCookies);

                urlConn.setRequestProperty("Cookie", sesionTxt + sep + valTxt);

            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo



    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametri
     * Sovrascritto nelle sottoclassi specifiche
     *
     * @param urlConn connessione
     */
    @Override
    protected void downlodCookies(URLConnection urlConn) {
        LinkedHashMap mappa = new LinkedHashMap();
        String headerName;
        String cookie;
        String name;
        String value;

        if (urlConn != null) {
            for (int i = 1; (headerName = urlConn.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookie = urlConn.getHeaderField(i);
                    cookie = cookie.substring(0, cookie.indexOf(";"));
                    name = cookie.substring(0, cookie.indexOf("="));
                    value = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
                    mappa.put(name, value);
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        this.cookies = mappa;
    } // fine del metodo

}// end of class
