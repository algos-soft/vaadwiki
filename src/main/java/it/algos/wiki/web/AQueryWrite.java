package it.algos.wiki.web;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URLConnection;
import java.net.URLEncoder;
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
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryWrite() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, titoloWiki).urlResponse() <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     */
    public AQueryWrite(String titoloWiki) {
        super(titoloWiki);
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, titoloWiki, newText).urlResponse() <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     * @param newText    da inserire
     */
    public AQueryWrite(String titoloWiki, String newText) {
        super(titoloWiki);
        this.newText = newText;
    }// end of constructor


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
    public String urlRequest() {
        return urlRequest(urlDomain, newText);
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
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     * @param newText    da inserire
     */
    public String urlRequest(String titoloWiki, String newText) {
        this.urlDomain = titoloWiki;
        this.newText = newText;

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

        return "";
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

        testoPost += "&text" + "=";
        testoPost += newText;

        return testoPost;
    } // fine del metodo

}// end of class
