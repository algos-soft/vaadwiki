package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import static it.algos.vaadflow14.wiki.AWikiApiService.*;
import org.json.simple.*;
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
 * elabora -> memorizza in BotLogin: itwiki_BPsession, lguserid, lgusername
 * <p>
 * Tipicamente viene chiamato una volta sola (SCOPE_PROTOTYPE) all'inizio del programma <br>
 * Memorizza i dati per le successive query come bot in BotLogin (SCOPE_SINGLETON) <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryLogin extends AQuery {

    /**
     * Valori di controllo ricevuto <br>
     */
    public static final int LG_ID = 124123;


    /**
     * Valore di collegamento iniziale <br>
     * Eventualmente inseribile nella cartella 'config' <br>
     */
    public static final String LG_NAME = "Biobot";

    /**
     * Valore di collegamento iniziale <br>
     * Eventualmente inseribile nella cartella 'config' <br>
     */
    public static final String LG_PASSWORD = "lhgfmeb8ckefkniq85qmhul18r689nbq";

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

    /**
     * Cookies ricevuti dalla secondaryRequestPost ed memorizzati in BotLogin <br>
     */
    private Map cookiesFromSecondary;


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
     * Property regolata dopo la urlPostRequest <br>
     */
    private boolean loginValido;


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

        //--La seconda request è di tipo POST
        //--Indispensabile aggiungere i cookies
        //--Indispensabile aggiungere il testo POST
        return this.secondaryRequestPost();
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
            cookiesFromPreliminary = downlodCookies(urlConn);
        } catch (Exception unErrore) {
            logger.error(AETypeLog.login, unErrore.getMessage());
        }

        elaboraPreliminaryResponse(urlResponse);
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
    public String secondaryRequestPost() {
        String urlDomain = TAG_SECONDARY_REQUEST_POST;
        String urlResponse = VUOTA;
        URLConnection urlConn;

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, cookiesFromPreliminary);
            addPostConnection(urlConn);
            urlResponse = sendRequest(urlConn);
            cookiesFromSecondary = downlodCookies(urlConn);
        } catch (Exception unErrore) {
        }

        return elaboraSecondaryResponse(urlResponse);
    }


    /**
     * Crea il testo del POST della request <br>
     */
    @Override
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

        //--controllo finale tramite una query GET coi cookies che controlla assert=bot
        if (loginValido) {
            loginValido = new QueryAssert().urlRequest(cookiesFromSecondary);
        }

        //--trasferisce nella istanza singleton BotLogin i cookies per essere utilizzati in tutte le query
        if (loginValido) {
            botLogin.setBot(true);
            botLogin.setLguserid(lguserid);
            botLogin.setLgusername(lgusername);
            botLogin.setCookies(cookiesFromSecondary);
        }

        secondaryResponse = jsonLogin.toString();

        return loginValido ? JSON_SUCCESS : JSON_ERROR;
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

}
