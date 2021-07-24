package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.wiki.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import it.algos.vaadwiki.backend.service.*;
import org.json.simple.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 08-lug-2021
 * Time: 18:35
 * Collegamento al server wiki <ul>
 * <li> Ogni utilizzo del bot deve essere preceduto da un login </li>
 * <li> Il login deve essere effettuato tramite le API </li>
 * <li> Il login deve essere effettuato con nickname e password </li>
 * <li> Controlla che l'accesso abbia un risultato positivo </li>
 * </ul>
 * Due request: GET e POST
 * GET
 * urlDomain = "&meta=tokens&type=login"
 * no testo POST
 * no upload cookies
 * nella response -> logintoken
 * download cookies -> itwikisession
 * elabora logintoken -> lgtoken
 * <p>
 * POST
 * urlDomain = "&action=login"
 * testo POST -> lgname, lgpassword, lgtoken
 * upload cookies -> itwikisession
 * nella response -> lguserid, lgusername, success
 * download cookies -> itwiki_BPsession
 * elabora -> memorizza in WLogin: itwiki_BPsession, lguserid, lgusername, success
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryLogin {


    /**
     * Valori di collegamento <br>
     * Eventualmente inseribili nella cartella 'config' <br>
     */
    public static final String LG_NAME = "Biobot";

    /**
     * Valori di collegamento <br>
     * Eventualmente inseribili nella cartella 'config' <br>
     */
    public static final String LG_PASSWORD = "lhgfmeb8ckefkniq85qmhul18r689nbq";

    /**
     * Tag base delle API per costruire l' 'urlDomain' completo <br>
     */
    protected static final String TAG_API = "https://it.wikipedia.org/w/api.php?";

    /**
     * Tag per la costruzione di un 'urlDomain' completo <br>
     * Viene usato in tutte le urlRequest delle sottoclassi di QueryWiki <br>
     * La urlRequest funzionerebbe anche senza questo tag, ma la urlResponse sarebbe meno 'leggibile' <br>
     */
    protected static final String TAG_FORMAT = TAG_API + "&format=json&formatversion=2";

    /**
     * Tag per la costruzione di un 'urlDomain' completo per una query <br>
     * Viene usato in molte (non tutte) urlRequest delle sottoclassi di QueryWiki <br>
     */
    protected static final String TAG_QUERY = TAG_FORMAT + "&action=query";

    /**
     * Tag per la costruzione costruzione del primo 'urlDomain' completo per la preliminaryRequestGet di login <br>
     */
    protected static final String TAG_PRELIMINARY_REQUEST_GET = TAG_QUERY + "&meta=tokens&type=login";

    /**
     * Tag per la costruzione del secondo 'urlDomain' completo per la secondaryRequestPost di login <br>
     */
    private static final String TAG_SECONDARY_REQUEST_POST = TAG_FORMAT + "&action=login";

    /**
     * Tag per la costruzione del terzo 'urlDomain' completo per la tertiaryRequestAssert di login <br>
     */
    private static final String TAG_TERTIARY_REQUEST_ASSERT = TAG_QUERY + "&assert=bot";

    /**
     * Tag per recuperare il valore della 'session' dai cookies ricevuti dalla preliminaryRequestGet <br>
     */
    private static final String TAG_SESSION = "itwikiSession";

    /**
     * Tag per il testo POS da inviare nella secondaryRequestPost <br>
     */
    private static final String TAG_NAME = "&lgname=";

    /**
     * Tag per il testo POS da inviare nella secondaryRequestPost <br>
     */
    private static final String TAG_PASSWORD = "&lgpassword=";

    /**
     * Tag per il testo POS da inviare nella secondaryRequestPost <br>
     */
    private static final String TAG_TOKEN = "&lgtoken=";

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AWikiApiService wikiApi;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AWikiBotService wikiBot;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ALogService logger;

    /**
     * Token recuperato dalla preliminaryRequestGet <br>
     * Viene convertito in lgtoken necessario per la successiva secondaryRequestPost <br>
     */
    private String logintoken = VUOTA;

    /**
     * Token elaborato da logintoken e necessario per la successiva secondaryRequestPost <br>
     * Property indispensabile inviata nel POST di testo della secondaryRequestPost<br>
     */
    private String lgtoken = VUOTA;

    /**
     * Property indispensabile inviata nel POST di testo della secondaryRequestPost<br>
     */
    private String lgname;

    /**
     * Property indispensabile inviata nel POST di testo della secondaryRequestPost<br>
     */
    private String lgpassword;


    /**
     * Cookies ricevuti dalla preliminaryGetRequest ed elaborati per la successiva urlPostRequest <br>
     */
    private Map cookiesFromPreliminary;

    private Map cookiesFromSecondary;

    private Map cookiesFromTertiary;

    /**
     * Property indispensabile ricevuta nella secondaryResponse <br>
     */
    private long lguserid;

    /**
     * Property indispensabile ricevuta nella secondaryResponse <br>
     */
    private String lgusername;

    /**
     * Property regolata dopo la urlGetRequest <br>
     */
    private String preliminaryResponse;

    /**
     * Property regolata dopo la urlPostRequest <br>
     */
    private String secondaryResponse;

    /**
     * Property regolata dopo la urlAssertRequest <br>
     */
    private String tertiaryResponse;

    /**
     * Property regolata dopo la urlPostRequest <br>
     */
    private boolean loginValido;

    private String testoPost;

    private String assertDomain;

    /**
     * Request al software mediawiki composta di due request <br>
     * <p>
     * La prima request preliminare è di tipo GET, per recuperare token e session <br>
     * urlDomain = "&meta=tokens&type=login" <br>
     * Invia la request senza cookies e senza testo POST <br>
     * Recupera i cookies della connessione (in particolare 'itwikisession') <br>
     * Recupera il logintoken dalla urlResponse <br>
     * <p>
     * La seconda request è di tipo POST <br>
     * urlDomain = "&action=login" <br>
     * Invia la request con i cookies ricevuti (solo 'session') <br>
     * Scrive il testo post con i valori di lgname, lgpassword e lgtoken <br>
     * <p>
     * La response viene elaborata per confermare il login andato a buon fine <br>
     */
    public String urlRequest() {
        //--La prima request è di tipo GET
        this.preliminaryRequestGet();

        // @todo test
        printDopoPreliminary();
        elaboraDopoPreliminary();
        // @todo test

        //--La seconda request è di tipo POST
        //--Indispensabile aggiungere i cookies
        //--Indispensabile aggiungere il testo POST
        this.secondaryRequestPost();

        // @todo test
        printDopoSecondary();
        elaboraDopoSecondary();
        // @todo test

        //--La terza request è di tipo POST
        //--Indispensabile aggiungere i cookies
        //--Indispensabile aggiungere il testo POST
        //--Serve come controllo &assert=bot
         this.tertiaryRequestAssert();

        // @todo test
        printDopoTertiary();
        // @todo test

        return VUOTA;
    }

    /**
     * <br>
     * Request preliminare. Crea la connessione base di tipo GET <br>
     * La request preliminare è di tipo GET, per recuperare token e session <br>
     * <p>
     * Request 1
     * URL: https://en.wikipedia.org/w/api.php?action=query&format=json&meta=tokens&type=login
     * This will return a "logintoken" parameter in JSON form, as documented at API:Login.
     * Other output formats are available. It will also return HTTP cookies as described below.
     * <p>
     * urlDomain = "&meta=tokens&type=login" <br>
     * Invia la request senza cookies e senza testo POST <br>
     * Recupera i cookies della connessione (in particolare 'itwikisession') <br>
     * Recupera il logintoken dalla urlResponse <br>
     */
    public void preliminaryRequestGet() {
        String urlDomain = TAG_PRELIMINARY_REQUEST_GET;
        String urlResponse = VUOTA;
        URLConnection urlConn;

        try {
            urlConn = this.creaGetConnection(urlDomain);
            urlResponse = sendRequest(urlConn);
            downlodCookiesPreliminary(urlConn);
        } catch (Exception unErrore) {
            logger.error(AETypeLog.login, unErrore.getMessage());
        }

        elaboraPreliminaryResponse(urlResponse);
    }

    public void printDopoPreliminary() {
        String request = "preliminaryRequestGet";

        System.out.println(request);
        System.out.println("**********");
        System.out.println(String.format("%s urlDomain: %s", request, TAG_PRELIMINARY_REQUEST_GET));
        System.out.println(String.format("%s uploadCookies: %s", request, "nessuno"));
        System.out.println(String.format("%s sendPost: %s", request, "no"));
        System.out.println(String.format("%s downloadCookies: %s", request, cookiesFromPreliminary));
        System.out.println(String.format("%s response: %s", request, preliminaryResponse));
    }

    public void elaboraDopoPreliminary() {
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        String request = "elaborazione dopo preliminaryRequest";

        System.out.println(request);
        System.out.println("**********");
        System.out.println(String.format("%s ricevuto = %s", LOGIN_TOKEN, logintoken));
        System.out.println(String.format("%s elaborato: %s", TAG_TOKEN, lgtoken));
    }

    public void printDopoSecondary() {
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        String request = "secondaryRequestPost";

        System.out.println(request);
        System.out.println("**********");
        System.out.println(String.format("%s urlDomain: %s", request, TAG_SECONDARY_REQUEST_POST));
        System.out.println(String.format("%s uploadCookies: %s", request, cookiesFromPreliminary));
        System.out.println(String.format("%s %s=%s", request, TAG_NAME, lgname));
        System.out.println(String.format("%s %s=%s", request, TAG_PASSWORD, lgpassword));
        System.out.println(String.format("%s %s=%s", request, TAG_TOKEN, lgtoken));
        System.out.println(String.format("%s sendPost (all): %s", request, testoPost));
        System.out.println(String.format("%s downloadCookies: %s", request, cookiesFromSecondary));
        System.out.println(String.format("%s response: %s", request, secondaryResponse));
    }

    public void elaboraDopoSecondary() {
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        String request = "elaborazione dopo secondaryRequest";

        String tagToken = "centralauth_Token";
        String tagName = "itwikiUserName";
        String tagID = "itwikiUserID";
        String tagSession = "itwikiSession";

        System.out.println(request);
        System.out.println("**********");
        System.out.println(String.format("%s ricevuto nella response = %s", LOGIN_USER_ID, lguserid));
        System.out.println(String.format("%s ricevuto nella response = %s", LOGIN_USER_NAME, lgusername));
        System.out.println(String.format("%s ricevuto nei cookies = %s", tagToken, cookiesFromSecondary.get(tagToken)));
        System.out.println(String.format("%s ricevuto nei cookies = %s", tagName, cookiesFromSecondary.get(tagName)));
        System.out.println(String.format("%s ricevuto nei cookies = %s", tagID, cookiesFromSecondary.get(tagID)));
        System.out.println(String.format("%s ricevuto nei cookies = %s", tagSession, cookiesFromSecondary.get(tagSession)));
    }


    public void printDopoTertiary() {
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        String request = "tertiaryRequestPost";

        System.out.println(request);
        System.out.println("**********");
        System.out.println(String.format("%s urlDomain: %s", request, TAG_TERTIARY_REQUEST_ASSERT));
        System.out.println(String.format("%s uploadCookies: %s", request, cookiesFromSecondary));
        System.out.println(String.format("%s %s=%s", request, TAG_NAME, lgname));
        System.out.println(String.format("%s %s=%s", request, TAG_PASSWORD, lgpassword));
        System.out.println(String.format("%s %s=%s", request, TAG_TOKEN, lgtoken));
        System.out.println(String.format("%s sendPost (all): %s", request, testoPost));
        System.out.println(String.format("%s downloadCookies: %s", request, cookiesFromTertiary));
        System.out.println(String.format("%s response: %s", request, tertiaryResponse));
    }


    /**
     * Crea la connessione base (GET) <br>
     * Regola i parametri della connessione <br>
     *
     * @param urlDomain stringa della request
     *
     * @return connessione con la request
     */
    protected URLConnection creaGetConnection(String urlDomain) throws Exception {
        URLConnection urlConn;

        urlConn = new URL(urlDomain).openConnection();
        urlConn.setDoOutput(true);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");

        return urlConn;
    }

    /**
     * Invia la request (GET oppure POST) <br>
     *
     * @param urlConn connessione con la request
     *
     * @return valore di ritorno della request
     */
    protected String sendRequest(URLConnection urlConn) throws Exception {
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;

        if (urlConn == null) {
            return VUOTA;
        }

        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, "UTF8");

        // read the response
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        return textBuffer.toString();
    }

    /**
     * Grabs cookies from the URL connection provided <br>
     * Cattura i cookies di ritorno e li memorizza nei parametri <br>
     *
     * @param urlConn connessione
     */
    protected void downlodCookiesPreliminary(URLConnection urlConn) {
        cookiesFromPreliminary = new HashMap();
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
                    cookiesFromPreliminary.put(name, value);
                }
            }
        }
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Recupera il token 'logintoken' dalla preliminaryRequestGet <br>
     * Viene convertito in lgtoken necessario per la successiva secondaryRequestPost <br>
     */
    protected void elaboraPreliminaryResponse(final String rispostaDellaQuery) {
        JSONObject jsonAll;
        JSONObject jsonQuery = null;
        JSONObject jsonTokens = null;

        jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        if (jsonAll != null && jsonAll.get(QUERY) != null) {
            jsonQuery = (JSONObject) jsonAll.get(QUERY);
        }

        if (jsonQuery != null && jsonQuery.get(TOKENS) != null) {
            jsonTokens = (JSONObject) jsonQuery.get(TOKENS);
        }

        if (jsonTokens != null && jsonTokens.get(LOGIN_TOKEN) != null) {
            logintoken = (String) jsonTokens.get(LOGIN_TOKEN);
        }

        try {
            lgtoken = URLEncoder.encode(logintoken, ENCODE);
        } catch (Exception unErrore) {
        }

        preliminaryResponse = rispostaDellaQuery;
    }


    /**
     * Request principale. Crea la connessione base di tipo POST <br>
     * <p>
     * Request 2
     * URL: https://en.wikipedia.org/w/api.php?action=login&format=json
     * COOKIES parameters:
     * itwikiSession
     * POST parameters:
     * lgname=BOTUSERNAME
     * lgpassword=BOTPASSWORD
     * lgtoken=TOKEN
     * <p>
     * where TOKEN is the token from the previous result. The HTTP cookies from the previous request must also be passed with the second request.
     * <p>
     * A successful login attempt will result in the Wikimedia server setting several HTTP cookies. The bot must save these cookies and send them back every time it makes a request (this is particularly crucial for editing). On the English Wikipedia, the following cookies should be used: enwikiUserID, enwikiToken, and enwikiUserName. The enwikisession cookie is required to actually send an edit or commit some change, otherwise the MediaWiki:Session fail preview error message will be returned.
     * <p>
     * Crea la connessione base di tipo POST <br>
     * Invia la request con i cookies ricevuti (solo 'session') <br>
     * Scrive il testo post con i valori di lgname, lgpassword e lgtoken <br>
     * <p>
     * Risposta in formato testo JSON <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     * Recupera i cookies allegati alla risposta e li memorizza in WikiLogin per poterli usare in query successive <br>
     */
    public void secondaryRequestPost() {
        String urlDomain = TAG_SECONDARY_REQUEST_POST;
        String urlResponse = VUOTA;
        URLConnection urlConn;

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookiesPreliminary(urlConn);
            addPostConnection(urlConn);
            urlResponse = sendRequest(urlConn);
            downlodCookiesSecondary(urlConn);
        } catch (Exception unErrore) {
        }

        elaboraSecondaryResponse(urlResponse);
    }


    /**
     * Allega i cookies alla request (upload) <br>
     * Serve solo la sessione <br>
     *
     * @param urlConn connessione
     */
    protected void uploadCookiesPreliminary(URLConnection urlConn) {
        String cookiesText;

        if (cookiesFromPreliminary != null) {
            cookiesText = this.creaCookiesText(cookiesFromPreliminary);
            urlConn.setRequestProperty("Cookie", cookiesText);
        }
    }


    /**
     * Costruisce la stringa dei cookies da allegare alla request POST <br>
     * Serve solo 'session' <br>
     *
     * @param cookies mappa dei cookies
     */
    public String creaCookiesText(Map<String, Object> cookies) {
        return cookies != null ? creaCookieText(TAG_SESSION, cookies.get(TAG_SESSION)) : VUOTA;
    }


    /**
     * Costruisce la stringa di un cookie da allegare alla request POST <br>
     *
     * @param key   della mappa
     * @param value della mappa
     *
     * @return testo del cookie
     */
    public String creaCookieText(String key, Object value) {
        String cookiesTxt = VUOTA;

        if (text.isValid(key) && value != null) {
            cookiesTxt += key;
            cookiesTxt += UGUALE_SEMPLICE;
            cookiesTxt += value;
            cookiesTxt += PUNTO_VIRGOLA;
        }

        return cookiesTxt;
    }

    /**
     * Aggiunge il POST della request <br>
     *
     * @param urlConn connessione con la request
     */
    protected void addPostConnection(URLConnection urlConn) throws Exception {
        if (urlConn != null) {
            PrintWriter out = new PrintWriter(urlConn.getOutputStream());
            out.print(elaboraPost());
            out.close();
        }
    }

    /**
     * Crea il testo del POST della request <br>
     */
    protected String elaboraPost() {
        String testoPost = VUOTA;

        lgname = text.isValid(lgname) ? lgname : LG_NAME;
        lgpassword = text.isValid(lgpassword) ? lgpassword : LG_PASSWORD;

        testoPost += TAG_NAME;
        testoPost += lgname;
        testoPost += TAG_PASSWORD;
        testoPost += lgpassword;
        testoPost += TAG_TOKEN;
        testoPost += lgtoken;
        this.testoPost = testoPost;

        return testoPost;
    }


    /**
     * Grabs cookies from the URL connection provided <br>
     * Cattura i cookies di ritorno e li memorizza nei parametri <br>
     *
     * @param urlConn connessione
     */
    protected void downlodCookiesSecondary(URLConnection urlConn) {
        cookiesFromSecondary = new HashMap();
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
                    cookiesFromSecondary.put(name, value);
                }
            }
        }
    }


    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     */
    protected void elaboraSecondaryResponse(String rispostaDellaQuery) {
        JSONObject jsonLogin = null;
        String jsonResult;
        JSONObject jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        if (jsonAll != null && jsonAll.get(LOGIN) != null) {
            jsonLogin = (JSONObject) jsonAll.get(LOGIN);
        }

        if (jsonLogin != null && jsonLogin.get(RESULT) != null) {
            if (jsonLogin.get(RESULT) != null) {
                jsonResult = (String) jsonLogin.get(RESULT);
                loginValido = text.isValid(jsonResult) && jsonResult.equals(JSON_SUCCESS);
            }
            if ((Long) jsonLogin.get(LOGIN_USER_ID) > 0) {
                lguserid = (Long) jsonLogin.get(LOGIN_USER_ID);
            }
            if (text.isValid(jsonLogin.get(LOGIN_USER_NAME))) {
                lgusername = (String) jsonLogin.get(LOGIN_USER_NAME);
            }
        }

        secondaryResponse = jsonLogin.toString();
    }

    //--La terza request è di tipo POST
    //--Indispensabile aggiungere i cookies
    //--Indispensabile aggiungere il testo POST
    //--Serve come controllo &assert=bot

    /**
     * Request finale di controllo. Crea la connessione base di tipo POST <br>
     * <p>
     * Request 3
     * URL: https://it.wikipedia.org/w/api.php?&format=json&formatversion=2&action=query&assert=bot
     * POST parameters:
     * lgname=BOTUSERNAME
     * lgpassword=BOTPASSWORD
     * lgtoken=TOKEN
     * <p>
     * where TOKEN is the token from the previous result. The HTTP cookies from the previous request must also be passed with the second request.
     * <p>
     * A successful login attempt will result in the Wikimedia server setting several HTTP cookies. The bot must save these cookies and send them back every time it makes a request (this is particularly crucial for editing). On the English Wikipedia, the following cookies should be used: enwikiUserID, enwikiToken, and enwikiUserName. The enwikisession cookie is required to actually send an edit or commit some change, otherwise the MediaWiki:Session fail preview error message will be returned.
     * <p>
     * La stringa urlDomain per la request viene controllata ed elaborata <br>
     * Crea la connessione base di tipo POST <br>
     * Invia la request con i cookies ricevuti (solo 'session') <br>
     * Scrive il testo post con i valori di lgname, lgpassword e lgtoken <br>
     * <p>
     * Risposta in formato testo JSON <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     * Recupera i cookies allegati alla risposta e li memorizza in WikiLogin per poterli usare in query successive <br>
     */
    public void tertiaryRequestAssert() {
        String urlDomain = TAG_TERTIARY_REQUEST_ASSERT + "&titles=Piozzano";
        String urlResponse = VUOTA;
        URLConnection urlConn;
        assertDomain = urlDomain;

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookiesSecondary(urlConn);
            addPostConnectionTertiary(urlConn);
            urlResponse = sendRequest(urlConn);
            downlodCookiesTertiary(urlConn);
        } catch (Exception unErrore) {
        }

         elaboraTertiaryResponse(urlResponse);
    }


    /**
     * Allega i cookies alla request (upload) <br>
     * Serve solo la sessione <br>
     *
     * @param urlConn connessione
     */
    protected void uploadCookiesSecondary(URLConnection urlConn) {
        String cookiesText;

        if (cookiesFromSecondary != null) {
            cookiesText = this.creaCookiesText(cookiesFromSecondary);
            urlConn.setRequestProperty("Cookie", cookiesText);
        }
    }

    /**
     * Aggiunge il POST della request <br>
     *
     * @param urlConn connessione con la request
     */
    protected void addPostConnectionTertiary(URLConnection urlConn) throws Exception {
        if (urlConn != null) {
            PrintWriter out = new PrintWriter(urlConn.getOutputStream());
            out.print(elaboraPostTertiary());
            out.close();
        }
    }

    /**
     * Crea il testo del POST della request <br>
     */
    protected String elaboraPostTertiary() {
        String testoPost = VUOTA;

        testoPost += TAG_NAME;
        testoPost += text.isValid(lgname) ? lgname : LG_NAME;
        testoPost += TAG_PASSWORD;
        testoPost += text.isValid(lgpassword) ? lgpassword : LG_PASSWORD;
        testoPost += TAG_TOKEN;
        testoPost += lgtoken;
        this.testoPost = testoPost;

        return testoPost;
    }

    /**
     * Grabs cookies from the URL connection provided <br>
     * Cattura i cookies di ritorno e li memorizza nei parametri <br>
     *
     * @param urlConn connessione
     */
    protected void downlodCookiesTertiary(URLConnection urlConn) {
        cookiesFromTertiary = new HashMap();
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
                    cookiesFromTertiary.put(name, value);
                }
            }
        }
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     */
    protected void elaboraTertiaryResponse(String rispostaDellaQuery) {
        JSONObject jsonLogin = null;
        JSONObject jsonError = null;
        String jsonCode;
        JSONObject jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        if (jsonAll != null && jsonAll.get(JSON_ERROR) != null) {
            jsonError = (JSONObject) jsonAll.get(JSON_ERROR);
        }

        if (jsonError != null && jsonError.get(JSON_CODE) != null) {
            if (jsonError.get(JSON_CODE) != null) {
                jsonCode = (String) jsonError.get(JSON_CODE);
                loginValido = !(text.isValid(jsonCode) && jsonCode.equals(JSON_NO_BOT));
            }
//            if ((Long) jsonLogin.get(LOGIN_USER_ID) > 0) {
//                lguserid = (Long) jsonLogin.get(LOGIN_USER_ID);
//            }
//            if (text.isValid(jsonLogin.get(LOGIN_USER_NAME))) {
//                lgusername = (String) jsonLogin.get(LOGIN_USER_NAME);
//            }
        }

        tertiaryResponse = jsonError.get(JSON_INFO).toString();
    }

    public String getTestoPost() {
        return testoPost;
    }

    public Map getCookiesFromPreliminary() {
        return cookiesFromPreliminary;
    }

    public Map getCookiesFromSecondary() {
        return cookiesFromSecondary;
    }


    public boolean isLoginValido() {
        return loginValido;
    }

    public long getLguserid() {
        return lguserid;
    }

    public String getLgusername() {
        return lgusername;
    }


    public String getCookiesTextUno() {
        return creaCookieText(TAG_SESSION, cookiesFromPreliminary.get(TAG_SESSION));
    }

    public String getCookiesTextDue() {
        return creaCookieText(TAG_SESSION, cookiesFromSecondary.get(TAG_SESSION));
    }

    public String getLgtoken() {
        return lgtoken;
    }

}
