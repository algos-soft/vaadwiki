package it.algos.wiki;


import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.request.RequestWikiAssert;
import it.algos.wiki.request.RequestWikiAssertBot;
import it.algos.wiki.request.RequestWikiAssertUser;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:31
 * <p>
 * Wrapper coi dati della connessione.
 * </p>
 * Se arriva la richiesta per la costruzione dell'istanza senza nickname e password:
 * la classe prima cerca nei cookies,
 * poi (se non li trova) cerca nelle preferenze
 * e poi (se non le trova) presenta un dialogo di controllo
 * <p>
 * Questa classe: <ul>
 * <li> Ogni utilizzo del bot deve essere preceduto da un login </li>
 * <li> Il login deve essere effettuato tramite le API </li>
 * <li> Il login deve essere effettuato con lingua e progetto </li>
 * <li> Il login deve essere effettuato con nickname e password </li>
 * <li> Il server wiki rimanda indietro la conferma IN DUE posti: i cookies ed il testo </li>
 * <li> Controlla che l'accesso abbia un risultato positivo </li>
 * <li> Due modalità di controllo: semplice legge SOLO i cookies e non il testo</li>
 * <li> Completa legge anche il testo e LO CONFRONTA con i cookies per ulteriore controllo </li>
 * <li> Mantiene il nome della wiki su cui operare </li>
 * <li> Mantiene il lguserid </li>
 * <li> Mantiene il lgusername </li>
 * <li> Mantiene il lgtoken </li>
 * <li> Mantiene il sessionid </li>
 * <li> Mantiene il cookieprefix </li>
 * </ul>
 * <p>
 * Tipicamente esiste un solo oggetto di questo tipo per il bot
 * L'istanza viene creata all'avvio del programma e mantenuta disponibile nel servletContext
 * <p>
 * <p>
 * -----
 * La prima request non serve che mandi il post
 * <p>
 * action=login is deprecated
 * action=query&meta=tokens&type=login instead
 * <p>
 * // * -domain: lgname e lgpassword
 * -post: nullo
 * -cookies: niente
 * <p>
 * il cookie ritorna: itwikisession
 * il testo ritorna: logintoken
 * <p>
 * La seconda request
 * -domain: action=login
 * -post: lgtoken, lgname e lgpassword
 * -cookie: itwikisession
 *
 * @author Guido Andrea Ceresa
 * @author gac
 * @see //www.mediawiki.org/wiki/API:Login
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class WikiLogin {


    // key to store the Login object in the session
    public static final String WIKI_LOGIN_KEY_IN_SESSION = "wikilogin";
    private static final String FIRST_RESULT = "result";
    private static final String SECOND_RESULT = "result";
    private static final String FIRST_TOKEN = "token";
    private static final String FIRST_NEW_TOKEN = "tokens";
    private static final String LOGIN_TOKEN = "logintoken";
    private static final String SECOND_TOKEN = "lgtoken";
    private static final String COOKIE_PREFIX = "cookieprefix";
    private static final String SESSION_ID = "sessionid";
    private static final String COOKIE_SESSION = "itwikiSession";
    private static final String USER_ID = "lguserid";
    private static final String USER_NAME = "lgusername";

    private static final boolean USA_NEW_LOGIN_TOKEN = false;

    //--codifica dei caratteri
    private static String INPUT = "UTF8";
    private static String ENCODE = "UTF-8";
    private static String LOGIN = "query&meta=tokens&type=login";

    // lingua di default
    private static String LINGUA_DEFAULT = "it";

    // progetto di default
    private static Progetto PROGETTO_DEFAULT = Progetto.wikipedia;
    // utente di default
    private static String UTENTE_DEFAULT = "Gacbot@Gacbot";
    // password di default
    private static String PASSWORD_DEFAULT = "tftgv0vhl16c0qnmfdqide3jqdp1i5m7";
    // lingua della wiki su cui si opera (solo due lettere)
    private String lingua = LINGUA_DEFAULT;
    // progetto della wiki su cui si opera (da una Enumeration)
    private Progetto progetto = PROGETTO_DEFAULT;
    private String firstDomain;
    private String secondDomain;
    // nome utente (parametro in entrata o hardcoded)
    private String lgname = UTENTE_DEFAULT;

    // password dell'utente  (parametro in entrata o hardcoded)
    private String lgpassword = PASSWORD_DEFAULT;

    // risultato  (parametro di ritorno del primo collegamento)
    private ErrLogin firstResult;

    // risultato  (parametro di ritorno definitivo del secondo collegamento)
    private String result;

    // id utente   (parametro di ritorno)
    private long lguserid;

    // nome utente (parametro di ritorno)
    private String lgusername;

    // token di controllo provvisorio (parametro di ritorno dal primo collegamento)
    private String token;

    // token di controllo definitivo (parametro in entrata al secondo collegamento)
    // parametro usato dai collegamenti successivi al login
    private String lgtoken;

    // prefisso dei cookies (parametro di ritorno)
    private String cookieprefix;

    // sessione (parametro di ritorno)
    private String sessionId;

    // i collegamenti per completare il login sono due
    // siccome la chiamata al metodo è ricorsiva
    // occorre essere sicuri che effettui solo 2 chiamate
    private boolean primoCollegamento = true;

    // controllo di validità del collegamento effettuato 2 volte con risultato positivo
    private boolean valido = false;

    // errore di collegamento (vuoto se collegamento valido)
    private ErrLogin risultato;

    // mappa dei parametri
    // ci metto i valori della enumeration ParLogin
    // la Enumeration non può essere una classe interna, perchgé in groovy non funziona (in java si)
    private HashMap<String, Object> par;

    // mappa dei cookies
    // ci metto tutti i cookies restituiti da URLConnection.responses
    private HashMap<String, Object> cookies;

    // flag di controllo per il collegamento come user (equivale al flag valido)
    private boolean user = false;

    // flag di controllo per il collegamento come bot
    private boolean bot = false;


    /**
     *
     */
    public WikiLogin() {
    }// fine del metodo costruttore

    /**
     * Costruttore parziale con lingua e progetto standard
     *
     * @param lgname     nickName in entrata per il collegamento
     * @param lgpassword password in entrata per il collegamento
     */
    @Deprecated
    public WikiLogin(String lgname, String lgpassword) {
        this(LINGUA_DEFAULT, PROGETTO_DEFAULT, lgname, lgpassword);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     *
     * @param lingua     della wikipedia utilizzata
     * @param progetto   della wikipedia foundation utilizzato
     * @param lgname     nickName in entrata per il collegamento
     * @param lgpassword password in entrata per il collegamento
     */
    @Deprecated
    public WikiLogin(String lingua, Progetto progetto, String lgname, String lgpassword) {
        this.setLingua(lingua);
        this.setProgetto(progetto);
        this.setLgname(lgname);
        this.setLgpassword(lgpassword);

        this.setUp();

        // procedura di accesso e registrazione con le API
        // Logging in through the API requires submitting a login query and constructing a cookie
        // In MediaWiki 1.15.3+, you must confirm the login by resubmitting the login request with the token returned.
        try { // prova ad eseguire il codice
            this.preliminaryRequest();
            this.urlRequest();
            this.checkValidita();
        } catch (Exception unErrore) { // intercetta l'errore
            this.setRisultato(ErrLogin.generico);
        }// fine del blocco try-catch

//        LibSession.setAttribute(WikiLogin.WIKI_LOGIN_KEY_IN_SESSION, this);
    }// fine del metodo costruttore completo

    @PostConstruct
    private void iniziaCollegamento() {
        this.setLingua(lingua);
        this.setProgetto(progetto);
        this.setLgname(lgname);
        this.setLgpassword(lgpassword);

        this.setUp();

        // procedura di accesso e registrazione con le API
        // Logging in through the API requires submitting a login query and constructing a cookie
        // In MediaWiki 1.15.3+, you must confirm the login by resubmitting the login request with the token returned.
        try { // prova ad eseguire il codice
            this.preliminaryRequest();
            this.urlRequest();
            this.checkValidita();
        } catch (Exception unErrore) { // intercetta l'errore
            this.setRisultato(ErrLogin.generico);
        }// fine del blocco try-catch

        if (isValido()) {
            log.info("login effettuato come: " + lgname);
        } else {
            String info = risultato != null ? risultato.messaggio : "";
            log.warn("login non valido: " + info);
        }// end of if/else cycle
    } // fine del metodo

    /**
     * Metodo iniziale
     * Regola il domain del primo collegamento effettuato per ricevere il token di conferma
     */
    private void setUp() {
        String domain = "";
        String lingua;
        Progetto progetto;

        lingua = this.getLingua();
        progetto = this.getProgetto();

        if (USA_NEW_LOGIN_TOKEN) {
            if (!lingua.equals("") && progetto != null) {
                domain += Cost.API_HTTP;
                domain += lingua;
                domain += Cost.API_WIKI;
                domain += progetto;
                domain += Cost.API_ACTION;
                domain += LOGIN;
                domain += Cost.API_FORMAT;
                this.firstDomain = domain;
            }// fine del blocco if

            if (!lingua.equals("") && progetto != null) {
                domain = Cost.API_HTTP;
                domain += lingua;
                domain += Cost.API_WIKI;
                domain += progetto;
                domain += Cost.API_ACTION;
                domain += LOGIN;
                domain += Cost.API_FORMAT;
                this.secondDomain = domain;
            }// fine del blocco if
        } else {
            if (!lingua.equals("") && progetto != null) {
                domain += Cost.API_HTTP;
                domain += lingua;
                domain += Cost.API_WIKI;
                domain += progetto;
                domain += Cost.API_ACTION;
                domain += Cost.API_LOGIN;
                domain += Cost.API_FORMAT;
                this.firstDomain = domain;
            }// fine del blocco if

            if (!lingua.equals("") && progetto != null) {
                domain = Cost.API_HTTP;
                domain += lingua;
                domain += Cost.API_WIKI;
                domain += progetto;
                domain += Cost.API_ACTION;
                domain += Cost.API_LOGIN;
                domain += Cost.API_FORMAT;
                this.secondDomain = domain;
            }// fine del blocco if
        }// end of if/else cycle

    } // fine del metodo

    /**
     * POST request.
     * <p>
     * domain: solo la richiesta del token
     * post: nessuno
     * cookies inviati: nessuno
     * cookies ricevuti: la sessione
     * risposta: token
     */
    private void preliminaryRequest() throws Exception {
        URLConnection urlConn;
        PrintWriter out;
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;
        String risposta;

        //--connessione
        urlConn = new URL(firstDomain).openConnection();
        urlConn.setDoOutput(true);

        //--POST
        //--The body of the POST can be empty.
        out = new PrintWriter(urlConn.getOutputStream());
        out.close();

        // regola l'entrata
        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        //--This request will also return a session cookie in the HTTP header
        //--recupera i cookies ritornati e li memorizza nei parametri
        //--in modo da poterli rinviare nella seconda richiesta
        this.downlopadCookies(urlConn);

        // read the request
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        // controlla il valore di ritorno della request e regola il risultato
        risposta = textBuffer.toString();
        this.risultatoPreliminaryRequest(risposta);
    } // fine del metodo


    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza in una mappa
     *
     * @param urlConn connessione
     */
    private void downlopadCookies(URLConnection urlConn) {
        String headerName;
        String cookie;
        String name;
        String value;
        HashMap<String, Object> mappa = new HashMap<String, Object>();

        // controllo di congruità
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

        this.setCookies(mappa);
    } // fine del metodo


    /**
     * Controllo del collegamento (success or error)
     * Regola il parametro collegato
     * Memorizza l'errore di collegamento
     * <p>
     * Costruisce la mappa dei dati dalla risposta alla prima Request
     * Restituisce il parametro risultato
     * <p>
     * Regola i parametri dopo la prima Request (solo se positiva)
     * I parametri dovrebbero essere 4
     *
     * @param testoRisposta della prima Request
     */
    private void risultatoPreliminaryRequest(String testoRisposta) {
        HashMap<String, Object> mappa = null;
        ErrLogin firstresult;
        JSONObject obj;

        // Costruisce la mappa dei dati dalla risposta alla prima Request
        // Restituisce il parametro risultato
        if (!testoRisposta.equals("")) {
            mappa = LibWiki.creaMappaLogin(testoRisposta);
            this.setPar(mappa);
        }// fine del blocco if

        if (mappa != null && mappa.size() > 0) {
            if (mappa.get(FIRST_RESULT) != null && mappa.get(FIRST_RESULT) instanceof String) {
                firstresult = ErrLogin.get((String) mappa.get(FIRST_RESULT));
                this.setFirstResult(firstresult);
            }// fine del blocco if

            if (mappa.get(FIRST_TOKEN) != null && mappa.get(FIRST_TOKEN) instanceof String) {
                this.setToken((String) mappa.get(FIRST_TOKEN));
            }// fine del blocco if

            if (mappa.get(FIRST_NEW_TOKEN) != null && mappa.get(FIRST_NEW_TOKEN) instanceof JSONObject) {
                obj = (JSONObject) mappa.get(FIRST_NEW_TOKEN);
                if (obj.get(LOGIN_TOKEN) != null && obj.get(LOGIN_TOKEN) instanceof String) {
                    token = (String) obj.get(LOGIN_TOKEN);
                    this.setToken(token);
                }// fine del blocco if
            }// fine del blocco if

            if (mappa.get(COOKIE_PREFIX) != null && mappa.get(COOKIE_PREFIX) instanceof String) {
                this.setCookieprefix((String) mappa.get(COOKIE_PREFIX));
            }// fine del blocco if

            if (mappa.get(SESSION_ID) != null && mappa.get(SESSION_ID) instanceof String) {
                this.setSessionId((String) mappa.get(SESSION_ID));
            }// fine del blocco if
        }// fine del blocco if

        HashMap<String, Object> mappaCookies = getCookies();
        if (mappaCookies.get(COOKIE_SESSION) != null && mappaCookies.get(COOKIE_SESSION) instanceof String) {
            this.setSessionId((String) mappaCookies.get(COOKIE_SESSION));
        }// end of if cycle

    } // fine del metodo


    /**
     * POST request.
     * Send a login request with POST, including the login as returned from previous request, and with the session cookie set in the header.
     * <p>
     * domain: solo la richiesta del token
     * post: lgname, lgpassword, lgtoken
     * cookies inviati: session
     * cookies ricevuti: session (la stessa inviata), userID, userName, forceHTTPS,
     * centralauth_User, centralauth_Token, centralauth_Session,
     * WMF-Last-Access, GeoIP
     * risposta: success, lguserid, lgusername, lgtoken (diverso), cookieprefix, sessionid (la stessa inviata)
     */
    private void urlRequest() throws Exception {
        URLConnection urlConn;
        PrintWriter out;
        String testoPost;
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;
        String risposta;

        //--connessione
        urlConn = new URL(secondDomain).openConnection();
        urlConn.setDoOutput(true);

        //--rimanda i cookies arrivati con la prima richiesta
        urlConn.setRequestProperty("Cookie", cookieSession());

        // now we send the data POST
        out = new PrintWriter(urlConn.getOutputStream());
        testoPost = this.getPost();
        out.print(testoPost);
        out.close();

        // regola l'entrata
        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, INPUT);

        //--recupera i cookies ritornati e li memorizza nei parametri
        //--in modo da poterli rinviare nella seconda richiesta
        this.downlopadCookies(urlConn);

        // read the request
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        // controlla il valore di ritorno della request e regola il risultato
        risposta = textBuffer.toString();
        this.risultatoRisposta(risposta);
    } // fine del metodo


    /**
     * Costruisce il cookie della sessione
     */
    private String cookieSession() {
        String sessionKey = "itwikiSession";
        String sep = "=";
        String sessionValue = this.getSessionId();

        HashMap<String, Object> mappa = getCookies();
        String value = (String) mappa.get(sessionKey);
        return sessionKey + sep + value;
    } // fine del metodo


    /**
     * Restituisce il testo del POST per la seconda Request
     * Aggiunge il token provvisorio ricevuto dalla prima Request
     *
     * @return post
     */
    private String getPost() {
        String testoPost = "";

        testoPost += "lgname=";
        testoPost += this.getLgname();
        testoPost += "&lgpassword=";
        testoPost += this.getLgpassword();
        testoPost += "&lgtoken=";
        try { // prova ad eseguire il codice
            testoPost += URLEncoder.encode(this.getToken(), ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return testoPost;
    } // fine del metodo


    /**
     * Controllo del collegamento (success or error)
     * Regola il parametro collegato
     * Memorizza l'errore di collegamento
     *
     * @param testoRisposta della seconda Request
     */
    private void risultatoRisposta(String testoRisposta) {
        HashMap<String, Object> mappa = null;
        ErrLogin risultato;

        // pulisce il parametro prima di controllare
        this.setValido(false);

        // Costruisce la mappa dei dati dalla risposta alla seconda Request
        // Restituisce il parametro risultato
        mappa = this.elaboraSecondaRisposta(testoRisposta);

        // mette da parte i parametri restituiti dal server
        this.regolaParametriSecondaRequest(mappa);

        risultato = this.getRisultato();
        if (risultato == ErrLogin.success) {
            this.setValido(true);
        }// fine del blocco if
    } // fine della closure


    /**
     * Costruisce la mappa dei dati dalla risposta alla seconda Request
     * Restituisce il parametro risultato
     *
     * @param testoRisposta della seconda Request
     */
    private HashMap<String, Object> elaboraSecondaRisposta(String testoRisposta) {
        HashMap<String, Object> mappa = null;
        ErrLogin risultato = ErrLogin.generico;

        if (!testoRisposta.equals("")) {
            mappa = LibWiki.creaMappaLogin(testoRisposta);
        }// fine del blocco if

        return mappa;
    } // fine del metodo


    /**
     * Regola i parametri dopo la seconda Request
     * I parametri dovrebbero essere 6
     */
    private void regolaParametriSecondaRequest(HashMap<String, Object> mappa) {
        ErrLogin firstresult;
        JSONObject obj;

        if (mappa != null && mappa.size() > 0) {
            if (mappa.get(SECOND_RESULT) != null && mappa.get(SECOND_RESULT) instanceof String) {
                risultato = ErrLogin.get((String) mappa.get(SECOND_RESULT));
                this.setRisultato(risultato);
            }// fine del blocco if

            if (mappa.get(USER_ID) != null && mappa.get(USER_ID) instanceof Long) {
                this.setLguserid((Long) mappa.get(USER_ID));
            }// fine del blocco if

            if (mappa.get(USER_NAME) != null && mappa.get(USER_NAME) instanceof String) {
                this.setLgusername((String) mappa.get(USER_NAME));
            }// fine del blocco if

            if (mappa.get(SECOND_TOKEN) != null && mappa.get(SECOND_TOKEN) instanceof String) {
                this.setToken((String) mappa.get(SECOND_TOKEN));
            }// fine del blocco if

            if (mappa.get(FIRST_NEW_TOKEN) != null && mappa.get(FIRST_NEW_TOKEN) instanceof JSONObject) {
                obj = (JSONObject) mappa.get(FIRST_NEW_TOKEN);
                if (obj.get(LOGIN_TOKEN) != null && obj.get(LOGIN_TOKEN) instanceof String) {
                    token = (String) obj.get(LOGIN_TOKEN);
                    this.setToken(token);
                }// fine del blocco if
            }// fine del blocco if

            if (mappa.get(COOKIE_PREFIX) != null && mappa.get(COOKIE_PREFIX) instanceof String) {
                this.setCookieprefix((String) mappa.get(COOKIE_PREFIX));
            }// fine del blocco if

            if (mappa.get(SESSION_ID) != null && mappa.get(SESSION_ID) instanceof String) {
                this.setSessionId((String) mappa.get(SESSION_ID));
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo


    /**
     * Controllo finale per verificare se è loggato come utente o come bot
     */
    public void checkValidita() {
        RequestWikiAssert request;

        request = new RequestWikiAssertUser(cookies);
        if (request.isValida()) {
            this.setUser(true);
        } else {
            this.setUser(false);
        }// end of if/else cycle

        request = new RequestWikiAssertBot(cookies);
        if (request.isValida()) {
            this.setBot(true);
        } else {
            this.setBot(false);
        }// end of if/else cycle
    } // fine del metodo

    /**
     * Restituisce i cookies
     *
     * @deprecated
     */
    public String getStringCookies() {
        String cookies = "";
        String sep = ";";
        String userName;
        long userId;
        String token;
        String session;
        String cookieprefix;

        cookieprefix = this.getCookieprefix();
        userName = this.getLgusername();
        userId = this.getLguserid();
        token = this.getToken();
        session = this.getSessionId();

        cookies = cookieprefix;
        cookies += "UserName=";
        cookies += userName;
        cookies += sep;
        cookies += cookieprefix;
        cookies += "UserID=";
        cookies += userId;
        cookies += sep;
//        cookies += cookieprefix;
//        cookies += "Token=";
//        cookies += token;
//        cookies += sep;
        cookies += cookieprefix;
        cookies += "Session=";
        cookies += session;

        return cookies;
    } // fine del metodo

    /**
     * Restituisce i cookies
     *
     * @return stringa per le property della request
     */
    public String getCookiesText() {
        return LibWiki.creaCookiesText(cookies);
    } // fine del metodo

    public String getLingua() {
        return lingua;
    }// end of getter method

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }//end of setter method

    public Progetto getProgetto() {
        return progetto;
    }// end of getter method

    public void setProgetto(Progetto progetto) {
        this.progetto = progetto;
    }//end of setter method

    public String getLgname() {
        return lgname;
    }// end of getter method

    public void setLgname(String lgname) {
        this.lgname = lgname;
    }//end of setter method

    public String getLgpassword() {
        return lgpassword;
    }// end of getter method

    public void setLgpassword(String lgpassword) {
        this.lgpassword = lgpassword;
    }//end of setter method

    public ErrLogin getFirstResult() {
        return firstResult;
    }// end of getter method

    public void setFirstResult(ErrLogin firstResult) {
        this.firstResult = firstResult;
    }//end of setter method

    public String getResult() {
        return result;
    }// end of getter method

    public void setResult(String result) {
        this.result = result;
    }//end of setter method

    public long getLguserid() {
        return lguserid;
    }// end of getter method

    public void setLguserid(long lguserid) {
        this.lguserid = lguserid;
    }//end of setter method

    public String getLgusername() {
        return lgusername;
    }// end of getter method

    public void setLgusername(String lgusername) {
        this.lgusername = lgusername;
    }//end of setter method

    public String getToken() {
        return token;
    }// end of getter method

    public void setToken(String token) {
        this.token = token;
    }//end of setter method

    public String getLgtoken() {
        return lgtoken;
    }// end of getter method

    public void setLgtoken(String lgtoken) {
        this.lgtoken = lgtoken;
    }//end of setter method

    public String getCookieprefix() {
        return cookieprefix;
    }// end of getter method

    public void setCookieprefix(String cookieprefix) {
        this.cookieprefix = cookieprefix;
    }//end of setter method

    public String getSessionId() {
        return sessionId;
    }// end of getter method

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }//end of setter method

    public boolean isPrimoCollegamento() {
        return primoCollegamento;
    }// end of getter method

    public void setPrimoCollegamento(boolean primoCollegamento) {
        this.primoCollegamento = primoCollegamento;
    }//end of setter method

    public boolean isValido() {
        return valido;
    }// end of getter method

    public void setValido(boolean valido) {
        this.valido = valido;
    }//end of setter method

    public ErrLogin getRisultato() {
        return risultato;
    }// end of getter method

    public void setRisultato(ErrLogin risultato) {
        this.risultato = risultato;
    }//end of setter method

    public HashMap<String, Object> getPar() {
        return par;
    }// end of getter method

    public void setPar(HashMap<String, Object> par) {
        this.par = par;
    }//end of setter method

    public HashMap<String, Object> getCookies() {
        return cookies;
    }// end of getter method

    public void setCookies(HashMap<String, Object> cookies) {
        this.cookies = cookies;
    }//end of setter method

    public boolean isBot() {
        return bot;
    }// end of getter method

    public void setBot(boolean bot) {
        this.bot = bot;
    }//end of setter method

    public boolean isUser() {
        return user;
    }// end of getter method

    public void setUser(boolean user) {
        this.user = user;
    }//end of setter method


} //fine della classe
