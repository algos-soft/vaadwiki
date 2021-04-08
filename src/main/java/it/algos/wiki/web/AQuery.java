package it.algos.wiki.web;

import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:35
 * Legge (scrive) una pagina internet di tipo HTTP oppure di tipo HTTPS.
 */
@Component("AQuery")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public abstract class AQuery {

    public final static String CSRF_TOKEN = "csrftoken";

    public final static String TOKENS = "tokens";

    public final static String TAG_BOT = "&assert=bot";

    /**
     * Costante di codifica testo. Sia per il titolo del urlDomain sia per il testo del POST <br>
     */
    protected final static String ENCODE = "UTF-8";

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public PreferenzaService pref;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public AArrayService array;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ADateService date;

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    @Autowired
    protected ApplicationContext appContext;

    /**
     * Flag di preferenza per le request che hanno bisogno di inviare i cookies nella request <br>
     */
    protected boolean isUploadCookies;

    /**
     * Flag di preferenza per le request che usano il POST <br>
     */
    protected boolean isUsaPost;

    /**
     * Flag di preferenza per le request che hanno bisogno dei cookies recuperati dal login <br>
     */
    protected boolean isUsaBot;

    /**
     * Flag di preferenza per le request che scaricano e memorizzano i cookies ricevuti nella connessione <br>
     */
    protected boolean isDownloadCookies;


    /**
     * Indirizzo web usato nella urlRequest <br>
     */
    protected String urlDomain;

    // ci metto tutti i cookies restituiti da URLConnection.responses
    protected HashMap cookies;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQuery() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest) <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest).urlResponse() <br>
     *
     * @param urlDomain indirizzo web usato nella urlRequest
     */
    public AQuery(String urlDomain) {
        this.urlDomain = urlDomain;
    }// end of constructor


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(AQueryxxx.class, urlRequest) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    protected void initIstanzaDopoInitDiSpringBoot() {
        fixPreferenze();
    }// end of method


    /**
     * Le preferenze vengono (eventualmente) sovrascritte nella sottoclasse <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        this.isUploadCookies = false;
        this.isUsaPost = false;
        this.isUsaBot = false;
        this.isDownloadCookies = false;
    }// end of method


    /**
     * Request preliminare. Facoltativa. Può esserci in alcune sottoclassi (AQueryLogin, ...) <br>
     * <p>
     * La stringa urlDomain per la request viene controllata ed elaborata <br>
     * Crea la connessione base di tipo GET <br>
     * Invia la request senza testo POST e senza invio di cookies <br>
     * <p>
     * Risposta in formato testo JSON <br>
     * Recupera i cookies allegati alla risposta e li memorizza nei parametri per poterli usare nella urlRequest <br>
     * Dalla urlResponse recupera le info necessarie per la urlRequest successiva <br>
     *
     * @param urlDomain indirizzo web usato nella urlRequest
     */
    public void preliminaryRequest(String urlDomain) {
        String urlResponse = "";
        URLConnection urlConn;

        try { // prova ad eseguire il codice
            urlDomain = fixUrlPreliminaryDomain(urlDomain);
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn);
            addPostConnection(urlConn);
            urlResponse = sendRequest(urlConn);
            downlodPreliminaryCookies(urlConn);
            elaboraPreliminayResponse(urlResponse);
        } catch (Exception unErrore) { // intercetta l'errore
//            logger.error(unErrore.toString());
        }// fine del blocco try-catch
    } // fine del metodo


    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene controllata ed elaborata <br>
     * Crea la connessione base di tipo GET
     * Alcune request (non tutte) hanno bisogno di inviare i cookies nella request <br>
     * In alcune request (non tutte) si aggiunge anche il POST <br>
     * Alcune request (non tutte) scaricano e memorizzano i cookies ricevuti nella connessione <br>
     * Invia la request con (eventuale) testo POST e con i cookies <br>
     * <p>
     * Risposta in formato testo JSON <br>
     * Recupera i cookies allegati alla risposta e li memorizza in WikiLogin per poterli usare in query successive <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     */
    public String urlRequest() {
        return urlRequest(urlDomain);
    } // fine del metodo


    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene controllata ed elaborata <br>
     * Crea la connessione base di tipo GET
     * Alcune request (non tutte) hanno bisogno di inviare i cookies nella request <br>
     * In alcune request (non tutte) si aggiunge anche il POST <br>
     * Alcune request (non tutte) scaricano e memorizzano i cookies ricevuti nella connessione <br>
     * Invia la request con (eventuale) testo POST e con i cookies <br>
     * <p>
     * Risposta in formato testo JSON <br>
     * Recupera i cookies allegati alla risposta e li memorizza in WikiLogin per poterli usare in query successive <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param urlDomain indirizzo web usato nella urlRequest
     */
    public String urlRequest(String urlDomainGrezzo) {
        String urlResponse = "";
        URLConnection urlConn;
        String urlDomain;

        try { // prova ad eseguire il codice
            urlDomain = fixUrlDomain(urlDomainGrezzo);
            urlConn = creaGetConnection(urlDomain);
            uploadCookies(urlConn);
            addPostConnection(urlConn);
            urlResponse = sendRequest(urlConn);
            downlodSecondaryCookies(urlConn);
        } catch (Exception unErrore) { // intercetta l'errore
//            logger.error(unErrore.toString()+". Probabili problemi di connessione");
            if (unErrore instanceof IOException) {
                try { // prova ad eseguire il codice
//                    logger.info("Riprovo. Se non esce subito un altro errore, vuol dire che questa volta il collegamento ha funzionato");
                    urlDomain = fixUrlDomain(urlDomainGrezzo);
                    urlConn = creaGetConnection(urlDomain);
                    uploadCookies(urlConn);
                    addPostConnection(urlConn);
                    urlResponse = sendRequest(urlConn);
                    downlodSecondaryCookies(urlConn);
                } catch (Exception unErrore2) { // intercetta l'errore
//                    logger.error("Questo url non ha funzionato: " + urlDomainGrezzo);
                }// fine del blocco try-catch
            }// end of if cycle
        }// fine del blocco try-catch

        return elaboraResponse(urlResponse);
    } // fine del metodo


    /**
     * Controlla la stringa della preliminary request
     * <p>
     * Controlla che sia valida <br>
     * Inserisce un tag specifico iniziale <br>
     * In alcune query (AQueryWiki e sottoclassi) codifica i caratteri del wikiTitle <br>
     * Sovrascritto nelle sottoclassi specifiche <br>
     *
     * @param urlDomain stringa della request originale
     *
     * @return stringa della request modificata
     */
    public String fixUrlPreliminaryDomain(String urlDomain) {
        return urlDomain;
    } // fine del metodo


    /**
     * Controlla la stringa della request
     * <p>
     * Controlla che sia valida <br>
     * Inserisce un tag specifico iniziale <br>
     * In alcune query (AQueryWiki e sottoclassi) codifica i caratteri del wikiTitle <br>
     * Sovrascritto nelle sottoclassi specifiche <br>
     *
     * @param urlDomain stringa della request originale
     *
     * @return stringa della request modificata
     */
    public String fixUrlDomain(String urlDomain) {
        return urlDomain;
    } // fine del metodo


    /**
     * Crea la connessione base (GET)
     * <p>
     * Regola i parametri della connessione
     *
     * @param urlDomain stringa della request
     *
     * @return connessione con la request
     */
    protected URLConnection creaGetConnection(String urlDomain) throws Exception {
        URLConnection urlConn = null;

        if (text.isValid(urlDomain)) {
            urlConn = new URL(urlDomain).openConnection();
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
        }// end of if cycle

        return urlConn;
    } // fine del metodo


    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    protected void uploadCookies(URLConnection urlConn) {
    } // fine del metodo

    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
//    protected void uploadCookies(URLConnection urlConn) {
//        HashMap cookies = this.cookies;
//        Object[] keyArray;
//        Object[] valArray;
//        Object sessionObj = null;
//        String sesionTxt = "";
//        String sep = "=";
//        Object valObj = null;
//        String valTxt = "";
//
//        // controllo di congruità
//        if (urlConn != null && isUploadCookies) {
//            if (cookies != null && cookies.size() > 0) {
//
//                keyArray = cookies.keySet().toArray();
//                if (keyArray.length > 0) {
//                    sessionObj = keyArray[0];
//                }// fine del blocco if
//                if (sessionObj != null && sessionObj instanceof String) {
//                    sesionTxt = (String) sessionObj;
//                }// fine del blocco if
//
//                valArray = cookies.values().toArray();
//                if (valArray.length > 0) {
//                    valObj = valArray[0];
//                }// fine del blocco if
//                if (valObj != null && valObj instanceof String) {
//                    valTxt = (String) valObj;
//                }// fine del blocco if
//
////               String  txtCookies=" itwikiUserName=Gac; itwikiUserID=399; centralauth_User=Gac; centralauth_Session=aa5f3ad00ae724ef5c6ba7096732f950";
////                urlConn.setRequestProperty("Cookie", txtCookies);
//
//                urlConn.setRequestProperty("Cookie", sesionTxt + sep + valTxt);
//
//            }// fine del blocco if
//        }// fine del blocco if
//    } // fine del metodo


    /**
     * Aggiunge il POST della request
     * In alcune request (non tutte) è obbligatorio anche il POST
     *
     * @param urlConn connessione con la request
     */
    protected void addPostConnection(URLConnection urlConn) throws Exception {
        if (urlConn != null && isUsaPost) {
            PrintWriter out = new PrintWriter(urlConn.getOutputStream());
            out.print(elaboraPost());
            out.close();
        }// end of if cycle
    } // fine del metodo


    /**
     * Crea il testo del POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraPost() {
        return VUOTA;
    } // fine del metodo


    /**
     * Invia la request (GET oppure POST)
     * Testo della risposta
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
        }// end of if cycle

        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, "UTF8");

        // read the response
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        return textBuffer.toString();
    } // fine del metodo


    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametri
     * Sovrascritto nelle sottoclassi specifiche
     *
     * @param urlConn connessione
     */
    protected LinkedHashMap downlodPreliminaryCookies(URLConnection urlConn) {
        return downlodCookies(urlConn);
    } // fine del metodo


    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametri
     * Sovrascritto nelle sottoclassi specifiche
     *
     * @param urlConn connessione
     */
    protected LinkedHashMap downlodSecondaryCookies(URLConnection urlConn) {
        return downlodCookies(urlConn);
    } // fine del metodo


    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametri
     * Sovrascritto nelle sottoclassi specifiche
     *
     * @param urlConn connessione
     */
    protected LinkedHashMap downlodCookies(URLConnection urlConn) {
        LinkedHashMap mappa = new LinkedHashMap();
        String headerName;
        String cookie;
        String name;
        String value;

        if (urlConn != null && isDownloadCookies) {
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

        return mappa;
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected void elaboraPreliminayResponse(String urlResponse) {
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraResponse(String urlResponse) {
        return urlResponse;
    } // fine del metodo


    /**
     * Contenuto della response
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @return response
     */
    public String urlResponse() {
        return urlRequest(urlDomain);
    }// end of method


    public void setUrlDomain(String urlDomain) {
        this.urlDomain = urlDomain;
    }// end of method


    public void setAppContext(ApplicationContext appContext) {
        this.appContext = appContext;
    }

}// end of class
