package it.algos.wiki.web;

import com.vaadin.flow.component.notification.Notification;
import it.algos.wiki.LibWiki;
import it.algos.wiki.WikiLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
 *
 * @see https://www.mediawiki.org/wiki/OAuth/Owner-only_consumers
 * @see https://en.wikipedia.org/wiki/Help:Creating_a_bot
 * <p>
 * <p>
 * Request 1
 * <p>
 * URL: https://it.wikipedia.org/w/api.php?action=login&format=xml
 * POST parameters:
 * lgname=BOTUSERNAME
 * lgpassword=BOTPASSWORD
 * <p>
 * If the password is correct, this will return a "NeedToken" result and a "token" parameter in XML form, as documented at mw:API:Login. Other output formats are available. It will also return HTTP cookies as described below.
 * <p>
 * Request 2
 * <p>
 * URL: https://it.wikipedia.org/w/api.php?action=login&format=xml
 * POST parameters:
 * lgname=BOTUSERNAME
 * lgpassword=BOTPASSWORD
 * lgtoken=TOKEN
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

    private final static String FIRST_REQUEST_GET = TAG_QUERY + "meta=tokens&type=login";

    private final static String SECOND_REQUEST_POST = TAG_BASE + "action=login";


    private String itwiki_BPsession;

    private String loginnotify_prevlogins;

    private long lguserid;

    private String logintoken;

    private String lgname;

    private String lgusername;

    private String lgpassword;

    private String lgtoken;

//    @Autowired
    private WikiLogin wikiLogin;

    public AQueryLogin() {
    }// end of constructor

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryLogin(WikiLogin wikiLogin) {
        this(wikiLogin, LG_NAME, LG_PASSWORD);
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, lgname, lgpassword) <br>
     *
     * @param lgname     del bot da utilizzare
     * @param lgpassword del bot da utilizzare
     */
    public AQueryLogin(WikiLogin wikiLogin, String lgname, String lgpassword) {
        this.wikiLogin = wikiLogin;
        this.lgname = lgname;
        this.lgpassword = lgpassword;
        urlRequest();
    }// end of constructor


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
    public void urlRequest() {

        //--La prima request è di tipo GET
        super.preliminaryRequest(FIRST_REQUEST_GET);

        //--La seconda request è di tipo POST ed usa i cookies
        super.isUploadCookies = true;
        super.isUsaPost = true;
        super.urlRequest(SECOND_REQUEST_POST);

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
            regolaWikiLoginSingleton();
            Notification.show("Loggato come " + lgusername, 3000, Notification.Position.BOTTOM_START);
        } else {
            Notification.show("Non sono riuscito a loggarmi ", 4000, Notification.Position.BOTTOM_START);
        }// end of if/else cycle

        return urlResponse;
    } // fine del metodo


    /**
     * Regola i parametri di wikiLogin
     */
    protected void regolaWikiLoginSingleton() {
        if (wikiLogin != null) {
            wikiLogin.setValido(true);
            wikiLogin.setLguserid(lguserid);
            wikiLogin.setLgname(lgusername);
            wikiLogin.setSessionId(itwiki_BPsession);
        } else {
            Notification.show("Non trovo wikiLogin", 4000, Notification.Position.BOTTOM_START);
            log.warn("Non trovo wikiLogin");
        }// end of if/else cycle
    } // fine del metodo

}// end of class
