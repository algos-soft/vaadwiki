package it.algos.wiki.request;


import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.ATextService;
import it.algos.wiki.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Superclasse astratta per le Request sul server di Wikipedia
 * Fornisce le funzionalità di base
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 * Le request più semplici usano il GET
 * In alcune request (non tutte) è obbligatorio anche il POST
 * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
 * <p>
 * Gets tokens required by data-modifying actions.
 * If you request one of these actions without providing a token, the API returns an error code such as notoken.
 * This module does not use a prefix.
 * The csrf (cross-site request forgery) token corresponds to the majority of older tokens, like edit and move, that were retrieved using the API action tokens (deprecated in MediaWiki 1.24).
 * api.php?action=query&meta=tokens
 * <tokens csrftoken="00112233445566778899aabbccddeeff+\" />
 * <p>
 */
@SpringComponent
public abstract class RequestWiki extends Request {

    /**
     * Il limite massimo ''for users'' è di 500
     * Il limite massimo ''for bots or sysops'' è di 5.000
     * Il limite 'max' equivale a 5.000
     */
    protected static final int LIMITE = 5000;
    protected static final String START_LIMIT_ERROR = "cmlimit may not be over";
    protected static final boolean USA_NEW_META_TOKEN = true; //@todo da passare a true, perché il vecchio intoken=edit è deprecato
    private static final String TAG_PRELINARY = "&action=query&meta=tokens";
    //--codifica dei caratteri
    protected static String ENCODE = "UTF-8";
    //--language selezionato (per adesso solo questo)
    protected static String LANGUAGE = "it";
    //--progetto selezionato (per adesso solo questo)
    protected static String PROJECT = "wikipedia";
    /* suffisso per il formato della risposta */
    protected static String API_FORMAT = "format=" + Cost.FORMAT.toString() + "&formatversion=2";
    /* azione API generica */
    protected static String API_ACTION = "&action=";
    /* azione API specifica */
    protected static String API_QUERY = "query";
    protected static String API_ASSERT = "&assert=bot";
    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
    protected static String API_BASE = Cost.API_HTTP + LANGUAGE + Cost.API_WIKI + PROJECT + Cost.API_PHP + API_FORMAT;
    // tag per la costruzione della stringa della request
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGEID = "&pageids=";
    protected static String CSRF_TOKEN = "csrftoken";


    //--stringa (separata da pipe oppure da virgola) delle pageids
    protected String stringaPageIds;



//    /**
//     * Istanza inietta da Spring come 'singleton'
//     */
//    @Autowired
    public WikiLoginOld wikiLoginOld;


    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    public ATextService text = ATextService.getInstance();

    //--tipo di ricerca della pagina
    //--di default il titolo
    protected TipoRicerca tipoRicerca = TipoRicerca.title;

    protected int limite = LIMITE;

    protected boolean needLogin;
    protected boolean needToken;
    protected boolean needBot;

//    --login del collegamento
//    protected WikiLogin wikiLogin;

    //--titolo della pagina
    protected String wikiTitle;

    //--pageid della pagina
    protected long wikiPageid;

    // oggetto della modifica in scrittura
    protected String summary;

    // ci metto tutti i cookies restituiti da URLConnection.responses
    protected HashMap cookies;
    // token ottenuto dalla preliminaryRequest ed usato per Edit e Move
    protected String csrfToken;

    /**
     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    public void doInit() {
        if (needLogin) {
            if (isLoggato()) {
                super.doInit();
            } else {
                valida = false;
                risultato = TipoRisultato.noLogin;
            }// end of if/else cycle
        } else {
            super.doInit();
        }// end of if/else cycle
    } // fine del metodo

    /**
     * Metodo iniziale
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void doRequest() {

        if (wikiLoginOld == null) {
//            wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        }// end of if cycle

        if (wikiLoginOld == null) {
//            wikiLogin = VaadApp.WIKI_LOGIN;
        }// end of if cycle

        if (needLogin) {
            if (wikiLoginOld == null) {
                return;
            }// end of if cycle
        }// end of if cycle

        if (needToken) {
            try { // prova ad eseguire il codice
                if (preliminaryRequest()) {
                    super.doRequest();
                } else {
                    valida = false;
                    if (risultato != TipoRisultato.mustbeposted) {
                        risultato = TipoRisultato.noPreliminaryToken;
                    }// end of if cycle
                }// end of if/else cycle
            } catch (Exception unErrore) { // intercetta l'errore
                String errore = unErrore.getMessage();
            }// fine del blocco try-catch
        } else {
            super.doRequest();
        }// end of if/else cycle
    } // fine del metodo

    /**
     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected boolean preliminaryRequest() throws Exception {
        URLConnection urlConn;
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;
        String risposta = "";

        //--connessione
        urlConn = creaConnessionePreliminary();

        //--manda i cookies ottenuti dal login
//        LibWiki.uploadCookies(urlConn,cookies);

//        if (needCookies) {
//            this.uploadCookies(urlConn);
//        }// end of if cycle

        //--POST
        this.creaPostPreliminary(urlConn);

        //--GET
        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        //--cookies
        //--recupera i cookies ritornati e li memorizza nei parametri
        //--in modo da poterli rinviare nella seconda richiesta
        if (needCookies) {
//            this.downlodCookies(urlConn);
        }// end of if cycle

        // read the request
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();
        risposta = textBuffer.toString();

        // controlla il valore di ritorno della request e regola il risultato
        return elaboraRispostaPreliminary(risposta);
    } // fine del metodo

    /**
     * Crea la connessione preliminary
     * <p>
     * Regola i parametri della connessione
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected URLConnection creaConnessionePreliminary() throws Exception {
        URLConnection urlConn = null;
        String domain = this.getDomainPreliminary();
        String txtCookies = "";
        HashMap<String, Object> cookiesMap;
//        domain+="&assert=bot";
        if (domain != null && !domain.equals("")) {
            urlConn = new URL(domain).openConnection();
            urlConn.setDoOutput(true);
        }// end of if cycle


        // regola le property
        if (wikiLoginOld != null) {
            cookiesMap = wikiLoginOld.getCookies();
            txtCookies = LibWiki.creaCookiesText(cookiesMap);
//            txtCookies=" itwikiUserName=Gac; itwikiSession=qm3mhhgg3i7qnbopdl0lrvtjddtpuac1; itwikiUserID=399; centralauth_User=Gac; centralauth_Session=aa5f3ad00ae724ef5c6ba7096732f950";
//            txtCookies=" itwikiUserName=Gac; itwikiUserID=399; centralauth_User=Gac; centralauth_Session=aa5f3ad00ae724ef5c6ba7096732f950";

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
        if (USA_NEW_META_TOKEN) {
            return API_BASE + TAG_PRELINARY;
        } else {
            return API_BASE + API_ACTION + "query&prop=info|revisions&intoken=edit&rvprop=timestamp" + TAG_TITOLO + wikiTitle;
        }// end of if/else cycle
    } // end of getter method

//    /**
//     * Grabs cookies from the URL connection provided.
//     * Cattura i cookies ritornati e li memorizza nei parametri
//     *
//     * @param urlConn connessione
//     */
//    private void downlodCookies(URLConnection urlConn) {
//        LinkedHashMap mappa = new LinkedHashMap();
//        String headerName;
//        String cookie;
//        String name;
//        String value;
//
//        if (urlConn != null) {
//            for (int i = 1; (headerName = urlConn.getHeaderFieldKey(i)) != null; i++) {
//                if (headerName.equals("Set-Cookie")) {
//                    cookie = urlConn.getHeaderField(i);
//                    cookie = cookie.substring(0, cookie.indexOf(";"));
//                    name = cookie.substring(0, cookie.indexOf("="));
//                    value = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
//                    mappa.put(name, value);
//                }// fine del blocco if
//            } // fine del ciclo for-each
//        }// fine del blocco if
//
//        this.cookies = mappa;
//    } // fine del metodo

    /**
     * Crea la connessione
     * <p>
     * Regola i parametri della connessione
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected URLConnection creaConnessione() throws Exception {
        URLConnection urlConn = super.creaConnessione();
        HashMap<String, Object> mappa = null;
        String txtCookies = "";

        // regola le property
        if (wikiLoginOld != null) {
//            txtCookies = wikiLogin.getStringCookies();
            mappa = wikiLoginOld.getCookies();
//            txtCookies=" itwikiUserName=Gac; itwikiUserID=399; centralauth_User=Gac; centralauth_Session=aa5f3ad00ae724ef5c6ba7096732f950";
            txtCookies = LibWiki.creaCookiesText(mappa);
            urlConn.setRequestProperty("Cookie", txtCookies);
        }// end of if cycle

        return urlConn;
    } // fine del metodo

    /**
     * Crea il POST della request preliminary
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void creaPostPreliminary(URLConnection urlConn) throws Exception {
    } // fine del metodo

    /**
     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected boolean isLoggato() {
        return true;
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
        String domain = API_BASE;

        if (needBot) {
            domain += API_ASSERT;
        }// end of if cycle
        domain += API_ACTION;

        return domain;
    } // fine del metodo

    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected boolean elaboraRispostaPreliminary(String rispostaRequest) {
        String token = LibWiki.getToken(rispostaRequest);

        if (token.equals("")) {
            return false;
        } else {
            csrfToken = token;
            return true;
        }// end of if/else cycle

    } // end of getter method

    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    @Override
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


} // fine della classe
