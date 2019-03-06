package it.algos.wiki.web;

import com.vaadin.flow.component.notification.Notification;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.service.AMailService;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:36
 * <p>
 * Collegamento al server wiki <ul>
 * <li> Ogni utilizzo del bot deve essere preceduto da un login </li>
 * <li> Il login deve essere effettuato tramite le API </li>
 * <li> Il login deve essere effettuato con nickname e password </li>
 * <li> Il server wiki rimanda indietro la conferma IN DUE posti: i cookies ed il testo </li>
 * <li> Controlla che l'accesso abbia un risultato positivo </li>
 * <li> Due modalità di controllo:</li>
 * <li> a) semplice legge SOLO i cookies e non il testo</li>
 * <li> b) completa legge anche il testo e LO CONFRONTA con i cookies per ulteriore controllo </li>
 * <li> Mantiene il lguserid </li>
 * <li> Mantiene il lgusername </li>
 * <li> Mantiene il lgtoken </li>
 * <li> Mantiene il sessionid </li>
 * </ul>
 * <p>
 *
 * @see https://www.mediawiki.org/wiki/OAuth/Owner-only_consumers
 * @see https://en.wikipedia.org/wiki/Help:Creating_a_bot
 * <p>
 * Request 1
 * <p>
 * URL: https://it.wikipedia.org/w/api.php?action=login&format=xml
 * POST parameters:
 * lgname=BOTUSERNAME
 * lgpassword=BOTPASSWORD
 * <p>
 * If the password is correct, this will return a "NeedToken" result and a "token" parameter in XML form, as documented at mw:API:Login.
 * Other output formats are available. It will also return HTTP cookies as described below.
 * <p>
 * Request 2
 * <p>
 * URL: https://it.wikipedia.org/w/api.php?action=login&format=xml
 * POST parameters:
 * lgname=BOTUSERNAME
 * lgpassword=BOTPASSWORD
 * lgtoken=TOKEN
 * <p>
 * Indipendentemente da quanto scritto sopra (preso dal sito mediawiki API):
 * PreliminaryRequest:
 * urlDomain = "&action=query&meta=tokens&type=login"
 * GET
 * no testoPOST
 * no upload cookies
 * nella response -> logintoken
 * nei cookies -> itwikisession
 * elabora logintoken -> lgtoken
 * <p>
 * UrlRequest:
 * urlDomain = "&action=login"
 * POST -> lgname, lgpassword, lgtoken
 * upload cookies -> itwikisession
 * nella response -> lguserid, lgusername, success
 * nei cookies -> itwiki_BPsession
 * elabora -> memorizza in WLogin: itwiki_BPsession, lguserid, lgusername, success
 */
@Component("AQueryLogin")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AQueryLogin extends AQueryWiki {

    public static final String LOGIN_TOKEN = "logintoken";

    public static final String LOGIN_USER_ID = "lguserid";

    public static final String LOGIN_USER_NAME = "lgusername";

    public static final String SESSION_TOKEN = "itwiki_BPsession";

    public static final String LG_NAME = "Biobot";

    public static final String LG_PASSWORD = "fulvia68@lhgfmeb8ckefkniq85qmhul18r689nbq";

    private final static String TAG_FIRST_REQUEST_GET = TAG_QUERY + "&meta=tokens&type=login";

    private final static String TAG_SECOND_REQUEST_POST = TAG_BASE + "&action=login";

    @Autowired
    private UtenteService utenteService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mailService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected LogService logger;

    private String itwiki_BPsession;

    private String loginnotify_prevlogins;

    private long lguserid;

    private String logintoken;

    private String lgname;

    private String lgusername;

    private String lgpassword;

    private String lgtoken;

//    @Autowired
//    @Qualifier(TAG_LOGIN)
//    private ALogin aLogin;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryLogin() {
        this(LG_NAME, LG_PASSWORD);
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, lgname, lgpassword) <br>
     *
     * @param lgname     del bot da utilizzare
     * @param lgpassword del bot da utilizzare
     */
    public AQueryLogin(String lgname, String lgpassword) {
        this.lgname = lgname;
        this.lgpassword = lgpassword;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizia() {
        urlRequest();
    }// end of method


    /**
     * Request al software mediawiki composta di due request <br>
     * <p>
     * La prima request preliminare è di tipo GET con action=login e senza ulteriori parametri
     * Crea la connessione base di tipo GET <br>
     * Invia la request senza testo POST e senza invio di cookies <br>
     * Nella preliminay scarica i cookies passati nella risposta, tra cui session
     * Nella preliminay recupera il logintoken necessario per la urlRequest successiva
     * <p>
     * La seconda request è di tipo POST
     * Nella seconda rinvia i cookies ricevuti (forse solo la session) <br>
     * Nella seconda invia un post con lgname, lgpassword e lgtoken <br>
     * <p>
     * La response viene elaborata per confermare il login andato a buon fine <br>
     */
    public String urlRequest() {

        //--La prima request è di tipo GET
        //--regole qui le preferenze perché sono diverse tra la preliminaryRequest e la urlRequest
        super.isUploadCookies = false;
        super.isUsaPost = false;
        super.isUsaBot = false;
        super.isDownloadCookies = true;
        super.preliminaryRequest(TAG_FIRST_REQUEST_GET);

        //--La seconda request è di tipo POST ed usa i cookies
        //--regole qui le preferenze perché sono diverse tra la preliminaryRequest e la urlRequest
        super.isUploadCookies = true;
        super.isUsaPost = true;
        super.isUsaBot = false;
        super.isDownloadCookies = true;
        super.urlRequest(TAG_SECOND_REQUEST_POST);

        return "";
    } // fine del metodo


    /**
     * Controlla la stringa della request
     * <p>
     * Controlla che sia valida <br>
     * Inserisce un tag specifico iniziale <br>
     * In alcune query (AQueryWiki e sottoclassi) codifica i caratteri del wikiTitle <br>
     * Sovrascritto nelle sottoclassi specifiche <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     *
     * @return stringa del titolo completo da inviare con la request
     */
    @Override
    public String fixUrlDomain(String titoloWiki) {
        return titoloWiki;
    } // fine del metodo


    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    protected void uploadCookies(URLConnection urlConn) {
        HashMap<String, Object> mappa = null;
        String txtCookies = "";

        if (isUploadCookies) {
            mappa = cookies;
            txtCookies = LibWiki.creaCookiesText(mappa);
            urlConn.setRequestProperty("Cookie", txtCookies);
        }// end of if cycle
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
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametrix
     * Sovrascritto nelle sottoclassi specifiche
     *
     * @param urlConn connessione
     */
    @Override
    protected LinkedHashMap downlodPreliminaryCookies(URLConnection urlConn) {
        LinkedHashMap mappa = super.downlodCookies(urlConn);

        itwikiSession = LibWiki.getSessionToken(mappa);
        this.cookies = mappa;

        return mappa;
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void elaboraPreliminayResponse(String textJSON) {
        HashMap<String, Object> mappa = LibWiki.creaMappaLogin(textJSON);
        logintoken = LibWiki.getLoginToken(mappa);

        try { // prova ad eseguire il codice
            lgtoken = URLEncoder.encode(logintoken, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
    } // fine del metodo


    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametrix
     * Sovrascritto nelle sottoclassi specifiche
     *
     * @param urlConn connessione
     */
    @Override
    protected LinkedHashMap downlodSecondaryCookies(URLConnection urlConn) {
        LinkedHashMap mappa = super.downlodCookies(urlConn);

        itwiki_BPsession = LibWiki.getToken(mappa, SESSION_TOKEN);
        this.cookies = mappa;

        return mappa;
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraResponse(String urlResponse) {
        HashMap<String, Object> mappa = LibWiki.creaMappaLogin(urlResponse);

        if (LibWiki.isLoginValid(urlResponse)) {
            lguserid = LibWiki.getValueLong(mappa, LOGIN_USER_ID);
            lgusername = LibWiki.getValueStr(mappa, LOGIN_USER_NAME);

            if (regolaWikiLoginSingleton()) {
                if (checkCollegamentoComeBot()) {
                    log.info("Algos - Bot loggato come " + lgusername);
                    logger.debug("Bot loggato come " + lgusername);
                } else {
                    log.warn("Algos - Non sono riuscito a loggarmi come bot");
                    mailService.send("Login","Non sono riuscito a loggarmi come bot");
                }// end of if/else cycle
            } else {
                log.warn("Algos - Non sono riuscito a loggarmi come bot");
                mailService.send("Login","Non sono riuscito a loggarmi come bot");
            }// end of if/else cycle
        } else {
            log.warn("Algos - Non sono riuscito a loggarmi");
            mailService.send("Login","Non sono riuscito a loggarmi");
        }// end of if/else cycle

        return urlResponse;
    } // fine del metodo


    /**
     * Regola i parametri di wikiLogin
     */
    protected boolean regolaWikiLoginSingleton() {
        boolean status = false;

        if (wLogin != null) {
            wLogin.regola(lguserid, lgusername, itwiki_BPsession);
            wLogin.setCookies(cookies);
//            aLogin.setUtente(utenteService.findByKeyUnica("biobot"));
            status = true;
        } else {
            Notification.show("Loggato come ", 4000, Notification.Position.BOTTOM_START);
            log.warn("Non trovo wLogin");
        }// end of if/else cycle

        return status;
    } // fine del metodo


    /**
     * Controlla di essere loggato come bot
     */
    protected boolean checkCollegamentoComeBot() {
        return appContext.getBean(AQueryBot.class).isBot();
    } // fine del metodo

}// end of class
