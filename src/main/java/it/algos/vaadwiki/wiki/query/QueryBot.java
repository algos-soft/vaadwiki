package it.algos.vaadwiki.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import static it.algos.vaadwiki.backend.service.AWikiBotService.*;
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
 * Date: mer, 14-lug-2021
 * Time: 18:54
 * <p>
 * isUploadCookies = true;
 * isUsaPost = false;
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryBot {

    /**
     * Tag di controllo errore
     */
    private static final String CODE_FAILED = "assertbotfailed";

    /**
     * Tag di controllo errore
     */
    private static final String INFO_FAILED = "Assertion that the user has the \"bot\" right failed.";

    /**
     * Tag per recuperare il valore della 'session' dai cookies ricevuti dalla preliminaryRequestGet <br>
     */
    private static final String TAG_SESSION = "itwikiSession";

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;


    /**
     * Cookies ricevuti dalla preliminaryGetRequest ed elaborati per la successiva urlPostRequest <br>
     */
    private Map cookies;

    private boolean bot;
    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(QueryBot.class) <br>
     */
    public QueryBot(Map cookies) {
        super();
        this.cookies = cookies;
    }// end of constructor

    /**
     * Request unica al software mediawiki di tipo GET senza testo POST <br>
     * Invia la request con i cookies ricevuti (solo 'session') <br>
     * <p>
     * La response viene elaborata per confermare il login andato a buon fine <br>
     */
    public String urlRequest() {
        String urlDomain = WIKI + WIKI_QUERY_BOT;
        String urlResponse;
        URLConnection urlConn;

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn);
            urlResponse = sendRequest(urlConn);
            this.elaboraResponse(urlResponse);
        } catch (Exception unErrore) {
        }

        return VUOTA;
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Recupera il token 'logintoken' dalla preliminaryRequestGet <br>
     * Viene convertito in lgtoken necessario per la successiva secondaryRequestPost <br>
     */
    protected void elaboraResponse(String rispostaDellaQuery) {
        JSONObject jsonAll;
        JSONObject jsonQuery = null;
        JSONObject jsonTokens = null;

        jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);
int a=87;
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

  public boolean isBot() {
        return bot;
  }

  }