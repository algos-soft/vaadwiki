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
public class AQueryLogin {


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
     * Tag aggiuntivo nella costruzione di un 'urlDomain' completo <br>
     * Viene usato in tutte le urlRequest delle sottoclassi di AQueryWiki <br>
     * La urlRequest funzionerebbe anche senza questo tag, ma la urlResponse sarebbe meno 'leggibile' <br>
     */
    protected static final String TAG_FORMAT = TAG_API + "&format=json&formatversion=2";

    /**
     * Tag aggiuntivo nella costruzione di un 'urlDomain' completo per una query <br>
     * Viene usato in molte (non tutte) urlRequest delle sottoclassi di AQueryWiki <br>
     */
    protected static final String TAG_QUERY = TAG_FORMAT + "&action=query";

    /**
     * Tag aggiuntivo nella costruzione del primo 'urlDomain' completo per la preliminaryRequestGet query di login <br>
     */
    protected static final String TAG_PRELIMINARY_REQUEST_GET = TAG_QUERY + "&meta=tokens&type=login";

    /**
     * Tag aggiuntivo nella costruzione del secondo 'urlDomain' completo per una query di login <br>
     */
    private static final String TAG_SECONDARY_REQUEST_POST = TAG_FORMAT + "&action=login";

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
    private Map cookies;

    /**
     * Property indispensabile ricevuta nella secondaryResponse <br>
     */
    private long lguserid;

    /**
     * Property indispensabile ricevuta nella secondaryResponse <br>
     */
    private String lgusername;

    /**
     * Property regolata dopo la urlPostRequest <br>
     */
    private boolean loginValido;

    /**
     * Request al software mediawiki composta di due request <br>
     * <p>
     * La prima request preliminare è di tipo GET, per recuperare token e session <br>
     * urlDomain = "&meta=tokens&type=login" <br>
     * Invia la request senza cookies e senza testo POST <br>
     * Recupera i cookies della connessione (in particolare 'session') <br>
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

        //--La seconda request è di tipo POST
        //--Indispensabile regolare i cookies
        //--Indispensabile aggiungere il testo POST
        return this.secondaryRequestPost();
    }

    /**
     * <br>
     * Request preliminare. Crea la connessione base di tipo GET <br>
     * La request preliminare è di tipo GET, per recuperare token e session <br>
     * <p>
     * urlDomain = "&meta=tokens&type=login" <br>
     * Invia la request senza cookies e senza testo POST <br>
     * Recupera i cookies della connessione (in particolare 'session') <br>
     * Recupera il logintoken dalla urlResponse <br>
     */
    public void preliminaryRequestGet() {
        String urlDomain = TAG_PRELIMINARY_REQUEST_GET;
        String urlResponse;
        URLConnection urlConn;

        try {
            urlConn = this.creaGetConnection(urlDomain);
            urlResponse = sendRequest(urlConn);
            this.downlodCookies(urlConn);
            this.elaboraPreliminaryResponse(urlResponse);
        } catch (Exception unErrore) {
            logger.error(AETypeLog.login, unErrore.getMessage());
        }
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
     * Elabora la risposta <br>
     * <p>
     * Recupera il token 'logintoken' dalla preliminaryRequestGet <br>
     * Viene convertito in lgtoken necessario per la successiva secondaryRequestPost <br>
     */
    protected void elaboraPreliminaryResponse(String rispostaDellaQuery) {
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
    }

    /**
     * Grabs cookies from the URL connection provided <br>
     * Cattura i cookies di ritorno e li memorizza nei parametri <br>
     *
     * @param urlConn connessione
     */
    protected void downlodCookies(URLConnection urlConn) {
        cookies = new HashMap();
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
                    cookies.put(name, value);
                }
            }
        }
    }

    /**
     * Request principale. Crea la connessione base di tipo POST <br>
     * La seconda request è di tipo POST <br>
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
    public String secondaryRequestPost() {
        String urlDomain = TAG_SECONDARY_REQUEST_POST;
        String urlResponse = VUOTA;
        URLConnection urlConn;

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn);
            addPostConnection(urlConn);
            urlResponse = sendRequest(urlConn);
            downlodCookies(urlConn);
        } catch (Exception unErrore) {
        }

        return elaboraSecondaryResponse(urlResponse);
    }


    /**
     * Allega i cookies alla request (upload) <br>
     * Serve solo la sessione <br>
     *
     * @param urlConn connessione
     */
    protected void uploadCookies(URLConnection urlConn) {
        String cookiesText;

        if (cookies != null) {
            cookiesText = this.creaCookiesText(cookies);
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

        testoPost += TAG_NAME;
        testoPost += text.isValid(lgname) ? lgname : LG_NAME;
        testoPost += TAG_PASSWORD;
        testoPost += text.isValid(lgpassword) ? lgpassword : LG_PASSWORD;
        testoPost += TAG_TOKEN;
        testoPost += lgtoken;

        return testoPost;
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     */
    protected String elaboraSecondaryResponse(String rispostaDellaQuery) {
        JSONObject jsonLogin = null;
        String jsonResult ;
        JSONObject jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        if (jsonAll != null && jsonAll.get(LOGIN) != null) {
            jsonLogin = (JSONObject) jsonAll.get(LOGIN);
        }

        if (jsonLogin != null && jsonLogin.get(RESULT) != null) {
            if (jsonLogin.get(RESULT) != null) {
                jsonResult = (String) jsonLogin.get(RESULT);
                loginValido = text.isValid(jsonResult) && jsonResult.equals(SUCCESS);
            }
            if ((Long)jsonLogin.get(LOGIN_USER_ID) >0) {
                lguserid = (Long) jsonLogin.get(LOGIN_USER_ID);
            }
            if (text.isValid(jsonLogin.get(LOGIN_USER_NAME))) {
                lgusername = (String) jsonLogin.get(LOGIN_USER_NAME);
            }
        }
//        List<Long>  listaPageIdsCategoria = wikiBot.getLongCat("Nati nel 1167");

        return VUOTA;
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

    public Map getCookies() {
        return cookies;
    }
    public String getCookiesText() {
        return creaCookieText(TAG_SESSION, cookies.get(TAG_SESSION));
    }

}
