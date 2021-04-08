package it.algos.wiki.web;

import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.enumeration.EAQueryCat;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 01-feb-2019
 * Time: 06:40
 * UrlRequest:
 * urlDomain = "&list=categorymembers&cmprop=title&cmtitle=Category:"
 * GET request
 * No POST text
 * Upload cookies
 * Bot needed
 */
@Component("AQueryCat")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public abstract class AQueryCat extends AQueryGet {

    /**
     * Tag aggiunto nel 'urlDomain', specificando che si richiede una lista delle pagine nella categoria
     */
    private static String TAG_LIST = TAG_QUERY + "&list=categorymembers";

    /**
     * Tag aggiunto nel 'urlDomain', specificando namespace
     */
    private static String TAG_NAMESPACE = "&cmnamespace=0";

    /**
     * Tag aggiunto nel 'urlDomain', specificando il tipo di categoria
     */
    private static String TAG_TYPE = "&cmtype=";

    /**
     * Tag aggiunto nel 'urlDomain'
     */
    private static String TAG_TYPE_PAGE = "page";

    /**
     * Tag aggiunto nel 'urlDomain'
     */
    private static String TAG_TYPE_CAT = "subcat";

    /**
     * Tag aggiunto nel 'urlDomain'
     */
    private static String TAG_TYPE_ALL = "page|subcat";

    /**
     * Tag aggiunto nel 'urlDomain', specificando il numero di valori in risposta
     * Fino a 500 può essere usato anche senza cookies
     * Con i cookies (di admin o di bot) arriva a 5000
     */
    private static String TAG_LIMIT = "&cmlimit=";

    /**
     * Tag aggiunto nel 'urlDomain', specificando il titolo della categoria
     */
    private static String TAG_TITLE = "&cmtitle=Category:";

    /**
     * Tag aggiunto nel 'urlDomain', specificando le informazioni da includere nella urlResponse
     * Tipicamente solo 'title' oppure anche 'ids'
     */
    private static String TAG_PROP_TITLE = "&cmprop=title";

    private static String TAG_PROP_PAGEID = "&cmprop=ids";

    /**
     * Tag aggiunto nel 'urlDomain', specificando il successivo inizio della lista
     */
    private static String TAG_CONTINUE = "&cmcontinue=";

    public ArrayList<String> listaTitle;

    public ArrayList<Long> listaPageid;

    protected boolean usaTitle;

    /**
     * Tag completo 'urlDomain' per la richiesta di una lista di categoria
     */
    protected String tagCat;

    @Autowired
    protected ApplicationContext appContext;

    protected EAQueryCat type = EAQueryCat.info;

    protected int limit;

    private String textContinue;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryCat.class) <br>
     */
    @Deprecated
    public AQueryCat() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryCat.class, urlRequest) <br>
     * Usa: appContext.getBean(AQueryCat.class, urlRequest).urlResponse() <br>
     *
     * @param titoloCat della categoria (necessita di codifica) usato nella urlRequest
     */
    public AQueryCat(String titoloCat) {
        super(titoloCat);
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
        tagCat = TAG_LIST + (usaTitle ? TAG_PROP_TITLE : TAG_PROP_PAGEID) + TAG_TITLE;
        urlRequestCat();
    }// end of method


    /**
     * Le preferenze vengono (eventualmente) sovrascritte nella sottoclasse <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.isUsaBot = true;
        super.isUploadCookies = true;
        this.limit = 5000;
        this.usaTitle = true;
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
    public void urlRequestCat() {
        long inizio = System.currentTimeMillis();
        String message = "";
        listaTitle = new ArrayList<>();
        listaPageid = new ArrayList<>();
        int numVoci = appContext.getBean(AQueryCatInfo.class, urlDomain).numVoci;
        int numCicliPrevisti = 0;
        int k = 0;
        int size;

        try { // prova ad eseguire il codice
            numCicliPrevisti = (numVoci / limit) + 1;
        } catch (Exception unErrore) { // intercetta l'errore
//            logger.error(unErrore.toString());
        }// fine del blocco try-catch

        do {
            super.urlRequest();
        } while (text.isValid(textContinue));

        size = usaTitle ? listaTitle.size() : listaPageid.size();
        if (pref.isBool(FlowCost.USA_DEBUG)) {
            message += "DOWNLOAD categoria ";
            message += urlDomain;
            message += " (" + text.format(size) + " pagine in ";
            message += date.deltaText(inizio);
            message += "), con AQueryCat, loggato come " + wLogin.getLgusername() + ", upload cookies, urlRequest di tipo GET";
//            logger.info(message);

            message = "Download categoria: previste ";
            message += text.format(numVoci);
            message += " voci - Recuperate ";
            message += text.format(size);
            if (numVoci > size) {
                message += " - Mancano " + (numVoci - size) + " voci";
            }// end of if cycle
//            logger.info(message);

        }// end of if cycle

//        return lista != null && lista.size() > 0 ? lista : null;
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
        urlDomain = urlDomain.startsWith(tagCat) ? urlDomain : tagCat + urlDomain;

        if (isUsaBot) {
            urlDomain += TAG_BOT;
        }// end of if cycle

        urlDomain += TAG_LIMIT + limit;

        if (type != null) {
            switch (type) {
                case all:
                    urlDomain += TAG_TYPE + TAG_TYPE_ALL;
                    break;
                case pagine:
                    urlDomain += TAG_TYPE + TAG_TYPE_PAGE;
                    break;
                case categorie:
                    urlDomain += TAG_TYPE + TAG_TYPE_CAT;
                    break;
                case info:
                    break;
                default:
//                    logger.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        if (text.isValid(textContinue)) {
            urlDomain = urlDomain + TAG_CONTINUE + textContinue;
        }// end of if

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
        } else {
        }// end of if/else cycle

        return urlResponse;
    } // fine del metodo


    protected void elaboraListaResponse(JSONArray listaCat) {
        if (usaTitle) {
            for (Object jsonObj : listaCat) {
                listaTitle.add((String) ((JSONObject) jsonObj).get("title"));
            }// end of for cycle
        } else {
            for (Object jsonObj : listaCat) {
                listaPageid.add((Long) ((JSONObject) jsonObj).get("pageid"));
            }// end of for cycle
        }// end of if/else cycle
    } // fine del metodo


}// end of class
