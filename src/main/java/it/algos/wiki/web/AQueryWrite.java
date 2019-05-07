package it.algos.wiki.web;

import it.algos.wiki.LibWiki;
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
 * Date: mer, 06-feb-2019
 * Time: 18:15
 * Making edits, and, indeed, any POST request, is a multi-step process.
 * <p>
 * 1. Log in, via one of the methods described in API:Login.
 * 2. GET a CSRF token:
 * 3. Send a POST request, with the CSRF token, to take action on a page:
 */
@Component("AQueryWrite")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AQueryWrite extends AQueryPost {


    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    private final static String FIRST_REQUEST_GET = TAG_QUERY + "&meta=tokens";

    private final static String TAG_SECOND_REQUEST_POST = TAG_BASE + TAG_BOT + "&action=edit&title=";

    // contenuto della pagina in scrittura
    private String newText;

    private String csrftoken;


    /**
     * Costruttore base senza parametri <br>
     * NON usato <br>
     * Viene usato solo il costruttore col titolo della categoria <br>
     * Questo costruttore (deprecato) rimane SOLO per non mandare in errore (in rosso) la injection di Spring
     * Se Spring trova un solo costruttore (quello col parametro) cerca di iniettare il parametro
     * che però è una stringa e va in errore (solo visivo, in realtà compila lo stesso)
     */
    @Deprecated
    public AQueryWrite() {
    }// end of constructor


    /**
     * Costruttore con parametri. È OBBLIGATORIO titoloWiki e  newText <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryWrite.class, titoloWiki, newText).status <br>
     * Usa: appContext.getBean(AQueryWrite.class, titoloWiki, newText) <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     * @param newText    da inserire
     */
    public AQueryWrite(String titoloWiki, String newText) {
        super(titoloWiki);
        this.newText = newText;
    }// end of constructor

    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se ci sono superclassi e sottoclassi, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    protected void inizia() {
        urlRequestWrite();
    }// end of method


//    /**
//     * Request principale <br>
//     * <p>
//     * La stringa del urlDomain per la request viene elaborata <br>
//     * Si crea la connessione <br>
//     * La request base usa solo il GET <br>
//     * In alcune request (non tutte) si aggiunge anche il POST <br>
//     * Alcune request (non tutte) scaricano e memorizzano i cookies ricevuti nella connessione <br>
//     * Alcune request (non tutte) hanno bisogno di inviare i cookies nella request <br>
//     * Si invia la connessione <br>
//     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
//     */
//    public String urlRequest() {
//        return urlRequest(urlDomain, newText);
//    }// end of method


//    /**
//     * Request principale <br>
//     * <p>
//     * La stringa del urlDomain per la request viene elaborata <br>
//     * Si crea la connessione <br>
//     * La request base usa solo il GET <br>
//     * In alcune request (non tutte) si aggiunge anche il POST <br>
//     * Alcune request (non tutte) scaricano e memorizzano i cookies ricevuti nella connessione <br>
//     * Alcune request (non tutte) hanno bisogno di inviare i cookies nella request <br>
//     * Si invia la connessione <br>
//     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
//     *
//     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
//     */
//    @Override
//    public String urlRequest(String titoloWiki) {
//        if (text.isValid(newText)) {
//            return urlRequest(titoloWiki, newText);
//        } else {
//            return "";
//        }// end of if/else cycle
//    }// end of method


    /**
     * Request principale <br>
     * <p>
     * La stringa del urlDomain per la request viene elaborata <br>
     * Si crea la connessione <br>
     * La request base usa solo il GET <br>
     * In alcune request (non tutte) si aggiunge anche il POST <br>
     * Alcune request (non tutte) scaricano e memorizzano i cookies ricevuti nella connessione <br>
     * Alcune request (non tutte) hanno bisogno di inviare i cookies nella request <br>
     * Si invia la connessione <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     */
    public boolean urlRequestWrite() {
        boolean status = false;

        //--La prima request è di tipo GET
        //--regole qui le preferenze perché sono diverse tra la preliminaryRequest e la urlRequest
        super.isUploadCookies = true;
        super.isUsaPost = false;
        super.isUsaBot = true;
        super.isDownloadCookies = true;
        super.preliminaryRequest(FIRST_REQUEST_GET);


        //--La seconda request è di tipo POST ed usa i cookies
        super.isUploadCookies = true;
        super.isUsaPost = true;
        super.urlRequest();

        return status;
    }// end of method


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
        return FIRST_REQUEST_GET;
    } // fine del metodo


    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametri
     * Sovrascritto nelle sottoclassi specifiche
     *
     * @param urlConn connessione
     */
    protected LinkedHashMap downlodPreliminaryCookies(URLConnection urlConn) {
        return super.downlodCookies(urlConn);
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
        csrftoken = wikiService.getToken(textJSON);

        try { // prova ad eseguire il codice
            if (text.isValid(csrftoken)) {
                csrftoken = URLEncoder.encode(csrftoken, ENCODE);
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

    } // fine del metodo


    /**
     * Controlla la stringa della request
     * <p>
     * Controlla che sia valida <br>
     * Inserisce un tag specifico iniziale <br>
     * In alcune query (AQueryWiki e sottoclassi) codifica i caratteri del wikiTitle <br>
     * Sovrascritto nelle sottoclassi specifiche <br>
     *
     * @param titoloWikiGrezzo della pagina (necessita di codifica per eliminare gli spazi vuoti) usato nella urlRequest
     *
     * @return stringa del titolo completo da inviare con la request
     */
    public String fixUrlDomain(String titoloWikiGrezzo) {
        String urlDomain = super.fixUrlDomain(this.urlDomain);
        urlDomain = TAG_SECOND_REQUEST_POST + urlDomain;
        return urlDomain;
    } // fine del metodo

    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    @Override
    protected void uploadCookies(URLConnection urlConn) {
        HashMap<String, Object> mappa = null;
        String txtCookies = "";

        if (isUploadCookies && wLogin != null) {
            mappa = wLogin.getCookies();
            txtCookies = LibWiki.creaCookiesText(mappa);
            urlConn.setRequestProperty("Cookie", txtCookies);
        }// end of if cycle
    } // fine del metodo

    /**
     * Crea il testo del POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraPost() {
        String testoPost = "";

        if (text.isValid(csrftoken)) {
            testoPost += "token" + "=";
            testoPost += csrftoken;
        }// end of if cycle

        testoPost += "&bot=true";
        testoPost += "&minor=true";
//        if (!testoSummary.equals("")) {
//            testoPost += "&summary=" + testoSummary;
//        }// end of if cycle

        testoPost += "&text" + "=";
        testoPost += newText;

        return testoPost;
    } // fine del metodo


    public void setNewText(String newText) {
        this.newText = newText;
    }

}// end of class
