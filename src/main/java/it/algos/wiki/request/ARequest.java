package it.algos.wiki.request;


import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadwiki.application.WikiCost;
import it.algos.wiki.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Superclasse astratta per le Request sul Web e su MediaWiki
 * Fornisce le funzionalità di base
 * Nelle sottoclassi vengono implementate le funzionalità specifiche
 * Le request più semplici usano il GET
 * In alcune request (non tutte) è obbligatorio anche il POST
 * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
 */
@SpringComponent
public abstract class ARequest {


    /**
     * Istanza inietta da Spring come 'singleton'
     */
    @Autowired
    private WikiLoginOld wikiLoginOld;

    //--richiesta preliminary per ottenere il token per Edit e Move
    protected static final boolean USA_NEW_META_TOKEN = true; //il vecchio intoken=edit è deprecato
    //--codifica dei caratteri
    protected static String ENCODE = "UTF-8";
    //--language selezionato (per adesso solo questo)
    protected static String LANGUAGE = "it";
    //--progetto selezionato (per adesso solo questo)
    protected static String PROJECT = "wikipedia";
    protected static String TAG_PRELINARY = "&action=query&meta=tokens";


    //--suffisso per il formato della risposta */
    protected static String API_FORMAT = "format=" + Cost.FORMAT.toString() + "&formatversion=2";

    //--azione API generica */
    protected static String API_ACTION = "&action=";

    //--azione API specifica */
    protected static String API_QUERY = "query";
    protected static String API_ASSERT = "&assert=bot";

    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
    protected static String API_BASE = Cost.API_HTTP + LANGUAGE + Cost.API_WIKI + PROJECT + Cost.API_PHP + API_FORMAT;

    // tag per la costruzione della stringa della request
    protected static String TAG_PROP = Cost.CONTENT_ALL;
    protected static String TAG_TITOLO = "&titles=";
    protected static String TAG_PAGEID = "&pageids=";
    protected static String CSRF_TOKEN = "csrftoken";

    //--validità generale della request (webUrl esistente e letto)
    protected boolean valida;

    //--validità specifica della request
    protected TipoRisultato risultato = TipoRisultato.erroreGenerico;

    //--indirizzo internet da leggere
    protected String domain;

    //--flag di controllo per la gestione del flusso
    protected boolean needPreliminary;
    protected boolean needAssert;
    protected boolean needContinua;
    protected boolean needPost;
    protected boolean needLogin;
    protected boolean needToken;
    protected boolean needBot;

    //--token per la continuazione della query
    protected String tokenContinua = "";

    //--contenuto testuale completo della risposta (la seconda, se ci sono due request)
    protected String testoResponse;

    //--tipo di ricerca della pagina
    //--di default il titolo
    protected TipoRicerca tipoRicerca = TipoRicerca.title;

//    --login del collegamento
//    protected WikiLogin wikiLogin;

    //--titolo della pagina
    protected String wikiTitle;

    //--parametro provvisorio utilizzato da RequestMove e RequestWrite
    //--viene specificato/precisato meglio nella sottoclasse nel metodo elaboraParametri
    //--può essere il nuovo titolo della pagina (RequestMove), oppure il nuovo testo della pagina (RequestWrite)
    protected String newTitleNewText;

    //--pageid della pagina
    protected long wikiPageid;

    // oggetto della modifica in scrittura
    protected String summary;

    // ci metto tutti i cookies restituiti da URLConnection.responses
    protected HashMap cookies;

    // token ottenuto dalla preliminaryRequest ed usato per Edit e Move
    protected String csrfToken;

    // liste di pagine
    protected ArrayList<Long> listaPaginePageids;
    protected ArrayList<String> listaPagineTitles;

    // liste di pagine della categoria (namespace=0)
    protected ArrayList<Long> listaVociPageids;
    protected ArrayList<String> listaVociTitles;

    // liste di sottocategorie della categoria (namespace=14)
    protected ArrayList<Long> listaCatPageids;
    protected ArrayList<String> listaCatTitles;

    //--lista di wrapper con pagesid e timestamp
    protected ArrayList<WrapTime> listaWrapTime;
    protected ArrayList<WrapTime> listaWrapTimeMissing;


    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    public AArrayService array = AArrayService.getInstance();

    //--url del collegamento
    private String preliminatyDomain;
    private String urlDomain;

    /**
     * Costruttore
     */
    public ARequest() {
    }// fine del metodo costruttore


    /**
     * Costruttore completo
     *
     * @param wikiPageid pageid della pagina wiki su cui operare
     */
    public ARequest(long wikiPageid) {
        this.wikiPageid = wikiPageid;
        this.doInit();
    }// fine del metodo costruttore completo


    /**
     * Costruttore completo
     *
     * @param wikiTitle titolo della pagina wiki su cui operare
     */
    public ARequest(String wikiTitle) {
        this.wikiTitle = wikiTitle;
        this.doInit();
    }// fine del metodo costruttore completo

    /**
     * Costruttore completo
     *
     * @param wikiTitle       titolo della pagina wiki su cui operare
     * @param newTitleNewText nuovo titolo della pagina (RequestMove), oppure nuovo testo della pagina (RequestWrite)
     * @param summary         oggetto dello spostamento (RequestMove) o modifica (RequestWrite)
     */
    public ARequest(String wikiTitle, String newTitleNewText, String summary) {
        this.wikiTitle = wikiTitle;
        this.newTitleNewText = newTitleNewText;
        this.summary = summary;
        this.doInit();
    }// fine del metodo costruttore completo

    /**
     * Gestione del flusso
     */
    public void doInit(long wikiPageid) {
        this.wikiPageid = wikiPageid;
        this.doInit();
    }// fine del metodo


    /**
     * Gestione del flusso
     */
    public void doInit(String wikiTitle) {
        this.wikiTitle = wikiTitle;
        this.doInit();
    }// fine del metodo

    /**
     * Gestione del flusso
     */
    protected void doInit() {
//        --Regola alcuni (eventuali) parametri specifici della sottoclasse
        elaboraParametri();

        if (needLogin) {
            if (!checkLogin()) {
                valida = false;
                return;
            }// end of if cycle
        }// end of if cycle


        //--procedura di accesso e registrazione con le API
        try { // prova ad eseguire il codice
            if (needPreliminary) {
                this.preliminaryRequest();
            }// end of if cycle

            //--elabora il domain
            String urlDomain = elaboraDomain();
            this.urlRequest(urlDomain);
            if (needContinua) {
                while (!tokenContinua.equals("")) {
                    urlDomain = elaboraDomain();
                    this.urlRequest(urlDomain);
                } // fine del blcco while
            }// end of if cycle

            if (needAssert) {
                this.checkValidita();
            }// end of if cycle

        } catch (Exception unErrore) { // intercetta l'errore
            valida = false;
            risultato = TipoRisultato.nonTrovata;
        }// fine del blocco try-catch

    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    protected void elaboraParametri() {
        needPreliminary = false;
        needAssert = false;
        needContinua = false;
        needPost = false;
        needLogin = false;
        needToken = false;
        needBot = false;
    }// fine del metodo


    /**
     * Alcune request (su mediawiki) richiedono anche una tokenRequestOnly preliminare
     * La prima request per recuperare il crfToken, usa il GET
     */
    private void preliminaryRequest() throws Exception {
        URLConnection urlConn;
        String urlDomainPreliminary = API_BASE + TAG_PRELINARY;
        String risposta;

        //--crea la connessione, elaborando il Domain
        urlConn = this.creaUrlConnection(urlDomainPreliminary);

//        //--invia i cookies di supporto, se richiesti
//        if (needCookies) {
//            this.uploadCookies(urlConn);
//        }// end of if cycle

//        //--now we send the data POST
//        //--crea una connessione di tipo POST, se richiesta
//        if (needPost) {
//            this.creaPostConnection(urlConn);
//        }// end of if cycle

        //--Invia la request (GET oppure POST)
        risposta = sendConnection(urlConn);

        // controlla il valore di ritorno della request e regola il risultato
        elaboraRispostaPeliminary(risposta);
    } // fine del metodo


    /**
     * Request principale
     * Quella base usa il GET
     * Altre usano il POST
     * In alcune request (non tutte) è obbligatorio anche il POST
     *
     * @param urlDomain stringa della request
     */
    private void urlRequest(String urlDomain) throws Exception {
        URLConnection urlConn;
        String risposta;

        //--crea la connessione
        urlConn = this.creaUrlConnection(urlDomain);

        //--now we send the data POST
        //--crea una connessione di tipo POST, se richiesta
        if (needPost) {
            this.creaPostConnection(urlConn);
        }// end of if cycle

        //--Invia la request (GET oppure POST)
        risposta = sendConnection(urlConn);

        // controlla il valore di ritorno della request e regola il risultato
        elaboraRisposta(risposta);
    } // fine del metodo


    /**
     * Stringa del browser per la request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraDomain() {
        String domainTmp = API_BASE + API_ACTION + API_QUERY + Cost.CONTENT_ALL;

        if (wikiTitle != null && !wikiTitle.equals("")) {
            domainTmp += TAG_TITOLO + titleEncoded();
        } else {
            domainTmp += TAG_PAGEID + wikiPageid;
        }// end of if/else cycle

        if (needBot) {
            domainTmp += API_ASSERT;
        }// end of if cycle

        domain = domainTmp;
        return domainTmp;
    } // fine del metodo


    /**
     * Crea la connessione
     * <p>
     * Regola i parametri della connessione
     * Invia i cookies di supporto, se richiesti
     *
     * @param urlDomain stringa della request
     *
     * @return connessione con la request
     */
    private URLConnection creaUrlConnection(String urlDomain) throws Exception {
        URLConnection urlConn = null;

        if (urlDomain != null && !urlDomain.equals("")) {
            urlConn = new URL(urlDomain).openConnection();
            urlConn.setDoOutput(true);
//            urlConn.setRequestProperty("Accept-Encoding", "GZIP");
//            urlConn.setRequestProperty("Content-Encoding", "GZIP");
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
        }// end of if cycle

        //--invia i cookies di supporto, se richiesti
        if (needLogin) {
            if (wikiLoginOld != null && urlConn != null) {
                urlConn.setRequestProperty("Cookie", wikiLoginOld.getCookiesText());
            }// end of if cycle
        }// end of if cycle

        return urlConn;
    } // fine del metodo


//    /**
//     * Allega i cookies alla request (upload)
//     * Serve solo la sessione
//     *
//     * @param urlConn connessione con la request
//     */
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
//        if (urlConn != null) {
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
//                urlConn.setRequestProperty("Cookie", sesionTxt + sep + valTxt);
//            }// fine del blocco if
//        }// fine del blocco if
//    } // fine del metodo

    /**
     * Crea il testo del POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraPost() {
        String testoPost = "";

        if (csrfToken != null && !csrfToken.equals("")) {
            testoPost += "&token=" + csrfToken;
        }// end of if cycle

        return testoPost;
    } // fine del metodo


    /**
     * Crea il POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     *
     * @param urlConn connessione con la request
     */
    private void creaPostConnection(URLConnection urlConn) throws Exception {
        PrintWriter out = new PrintWriter(urlConn.getOutputStream());
        out.print(elaboraPost());
        out.close();
    } // fine del metodo

    /**
     * Invia la request (GET oppure POST)
     * Testo della risposta
     *
     * @param urlConn connessione con la request
     *
     * @return valore di ritorno della request
     */
    private String sendConnection(URLConnection urlConn) throws Exception {
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;

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
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void elaboraRispostaPeliminary(String rispostaRequest) {
        HashMap<String, Object> mappa = LibWiki.creaMappaQuery(rispostaRequest);

        if (mappa != null) {
            if (mappa.get(CSRF_TOKEN) != null && mappa.get(CSRF_TOKEN) instanceof String) {
                csrfToken = (String) mappa.get(CSRF_TOKEN);
            }// end of if cycle
        }// fine del blocco if

    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void elaboraRisposta(String rispostaRequest) {
        HashMap<String, Object> mappa = LibWiki.creaMappaQuery(rispostaRequest);
        tokenContinua = "";
        testoResponse = null;

        if (mappa != null) {
            if (mappa.get(PagePar.title.toString()) == null) {
                risultato = TipoRisultato.nonTrovata;
                valida = false;
                return;
            }// end of if cycle

            if (mappa.get(PagePar.missing.toString()) != null && (Boolean) mappa.get(PagePar.missing.toString())) {
                risultato = TipoRisultato.nonTrovata;
                valida = false;
                return;
            }// end of if cycle

            if (mappa.get(PagePar.missing.toString()) != null && !(Boolean) mappa.get(PagePar.missing.toString())) {
                valida = true;
                risultato = TipoRisultato.esistente;
                if (mappa.get(PagePar.content.toString()) != null) {
                    risultato = TipoRisultato.letta;
                    testoResponse = rispostaRequest;
                }// end of if cycle
                return;
            }// end of if cycle


        }// fine del blocco if

    } // fine del metodo

    /**
     * Controllo del login
     */
    private boolean checkLogin() {
        boolean status = true;

        if (wikiLoginOld == null) {
//            wikiLogin = (WikiLogin) LibSession.getAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION);
        }// end of if cycle

        if (wikiLoginOld == null) {
            wikiLoginOld = WikiCost.WIKI_LOGIN;
        }// end of if cycle

        if (needLogin) {
            if (wikiLoginOld == null) {
                risultato = TipoRisultato.noLogin;
                return false;
            }// end of if cycle
        }// end of if cycle

        return status;
    } // fine del metodo


    /**
     * Controllo finale per verificare le condizioni necessarie a questa request per essere considerata valida
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    protected void checkValidita() {
    } // fine del metodo

    /**
     * Encode del titolo
     */
    protected String titleEncoded() {
        String titolo = "";

        try { // prova ad eseguire il codice
            titolo = URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
            String errore = unErrore.getMessage();
        }// fine del blocco try-catch

        return titolo;
    } // fine del metodo

    public boolean isValida() {
        return valida;
    }// end of getter method

    protected void setValida(boolean valida) {
        this.valida = valida;
    }//end of setter method

    public TipoRisultato getRisultato() {
        return risultato;
    }// end of getter method

    protected void setRisultato(TipoRisultato risultato) {
        this.risultato = risultato;
    }//end of setter method

    public String getTestoResponse() {
        return testoResponse;
    }// end of getter method

    public void setTestoResponse(String testoResponse) {
        this.testoResponse = testoResponse;
    }//end of setter method

    public ArrayList<Long> getListaVociPageids() {
        return listaVociPageids;
    }// end of getter method

    public ArrayList<String> getListaVociTitles() {
        return listaVociTitles;
    }// end of getter method

    public ArrayList<Long> getListaCatPageids() {
        return listaCatPageids;
    }// end of getter method

    public ArrayList<String> getListaCatTitles() {
        return listaCatTitles;
    }// end of getter method

    public ArrayList<Long> getListaPaginePageids() {
        return listaPaginePageids;
    }// end of getter method

    public ArrayList<String> getListaPagineTitles() {
        return listaPagineTitles;
    }// end of getter method

    public ArrayList<Long> getListaAllPageids() {
        return array.somma(getListaVociPageids(), getListaCatPageids());
    }// end of getter method

    public ArrayList<String> getListaAllTitles() {
        return array.somma(getListaVociTitles(), getListaCatTitles());
    }// end of getter method

    public ArrayList<WrapTime> getListaWrapTime() {
        return listaWrapTime;
    }// end of getter method

    public ArrayList<WrapTime> getListaWrapTimeMissing() {
        return listaWrapTimeMissing;
    }// end of getter method

} // fine della classe
