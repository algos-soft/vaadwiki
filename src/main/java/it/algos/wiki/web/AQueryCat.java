package it.algos.wiki.web;

import it.algos.vaadflow.application.FlowCost;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 01-feb-2019
 * Time: 06:40
 */
@Component("AQueryCat")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AQueryCat extends AQueryGet {


    /**
     * Tag aggiunto nel 'urlDomain', specificando che si richiede una lista delle voci nella categoria
     */
    private static String TAG_LIST = TAG_QUERY + "&list=categorymembers";

    /**
     * Tag aggiunto nel 'urlDomain', specificando namespace ed il tipo di categoria
     */
    private static String TYPE_VOCI = "&cmnamespace=0&cmtype=page";

    /**
     * Tag aggiunto nel 'urlDomain', specificando il numero di valori in risposta
     * Fino a 500 può essere usato anche senza cookies
     * Con i cookies (di admin o di bot) arriva a 5000
     */
    private static String TAG_LIMIT = "&cmlimit=5000";

    /**
     * Tag aggiunto nel 'urlDomain', specificando il titolo della categoria
     */
    private static String TAG_TITLE = "&cmtitle=Category:";

    /**
     * Tag aggiunto nel 'urlDomain', specificando le informazioni da includere nella urlResponse
     * Tipicamente solo 'title' oppure anche 'ids'
     */
    private static String TAG_INFO = "&cmprop=title";

    /**
     * Tag aggiunto nel 'urlDomain', specificando il successivo inizio della lista
     */
    private static String TAG_CONTINUE = "&cmcontinue=";

    /**
     * Tag completo 'urlDomain' per la richiesta di una lista di categoria
     */
    private static String TAG_CAT = TAG_LIST + TAG_INFO + TAG_TITLE;

    @Autowired
    protected ApplicationContext appContext;

//    @Autowired
//    private WikiLoginOld wikiLoginOldCat;

    private String textContinue;

    private ArrayList<String> listaTitles;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryCat() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest) <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest).urlResponse() <br>
     *
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     */
    public AQueryCat(String titoloCat) {
        super(titoloCat);
    }// end of constructor


    /**
     * Le preferenze vengono (eventualmente) sovrascritte nella sottoclasse <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.isUploadCookies = true;
        super.isUsaPost = false;
        super.isUsaBot = true;
    }// end of method


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
    public ArrayList<String> urlRequestTitle() {
        return urlRequestTitle(urlDomain);
    }// end of method


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
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     */
    public ArrayList<String> urlRequestTitle(String titoloCat) {
        long inizio = System.currentTimeMillis();
        String message = "";
        listaTitles = new ArrayList<>();

        super.urlRequest(titoloCat);
        while (text.isValid(textContinue)) {
            super.urlRequest(titoloCat);
        } // fine del blocco while

        if (pref.isBool(FlowCost.USA_DEBUG)) {
            message += "Download categoria ";
            message += titoloCat;
            message += " (" + text.format(listaTitles.size()) + " voci in ";
            message += date.deltaText(inizio);
            message += "), con AQueryCat, loggato come " + wLogin.getLgusername() + ", upload cookies, urlRequest di tipo GET";
            log.info(message);
        }// end of if cycle

        return listaTitles != null && listaTitles.size() > 0 ? listaTitles : null;
    }// end of method


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
    @Override
    public String fixUrlDomain(String titoloWikiGrezzo) {
        String urlDomain = super.fixUrlDomain(titoloWikiGrezzo);
        urlDomain = urlDomain.startsWith(TAG_CAT) ? urlDomain : TAG_CAT + urlDomain;

        if (isUsaBot) {
            urlDomain += TAG_BOT + TAG_LIMIT;
        }// end of if cycle

        if (text.isValid(textContinue)) {
            urlDomain = urlDomain + TAG_CONTINUE + textContinue;
        }// end of if cycle
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
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraResponse(String urlResponse) {
        super.elaboraResponse(urlResponse);
        HashMap<String, Object> mappa = null;
        JSONArray listaCat;

        if (super.isUrlResponseValida) {
            mappa = LibWiki.creaMappaCat(urlResponse);
            textContinue = (String) mappa.get(LibWiki.CMCONTINUE);
            listaCat = (JSONArray) mappa.get(LibWiki.CATEGORY_MEMBERS);
            elaboraListaResponse(listaCat);
            int a = 87;
        } else {
            int a = 87;
        }// end of if/else cycle

        return urlResponse;
    } // fine del metodo


    protected void elaboraListaResponse(JSONArray listaCat) {
        if (listaTitles != null) {
            for (Object jsonObj : listaCat) {
                listaTitles.add((String) ((JSONObject) jsonObj).get("title"));
            }// end of for cycle
        }// end of if cycle
    } // fine del metodo

}// end of class
