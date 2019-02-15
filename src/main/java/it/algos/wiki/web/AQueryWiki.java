package it.algos.wiki.web;

import it.algos.vaadwiki.service.AWikiService;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:36
 */
@Component("AQueryWiki")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public abstract class AQueryWiki extends AQuery {

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'urlDomain' completo
     */
    protected final static String TAG_WIKI = "https://it.wikipedia.org/wiki/";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'urlDomain' completo
     */
    protected final static String TAG_API = "https://it.wikipedia.org/w/api.php?";

    /**
     * Tag aggiunto nel 'urlDomain', specificando il numero di valori in risposta
     * Fino a 500 può essere usato anche senza cookies
     * Con i cookies (di admin o di bot) arriva a 5000
     */
    protected final static String TAG_LIMIT = "&cmlimit=5000";


    /**
     * Tag aggiunto per costruire un 'urlDomain' completo
     * Viene usato in tutte le urlRequest delle sottoclassi di AQueryWiki
     * La urlRequest funzionerebbe anche senza questo tag, ma la urlResponse sarebbe meno 'leggibile'
     */
    protected final static String TAG_BASE = TAG_API + "&format=json&formatversion=2";

    /**
     * Tag aggiunto per costruire un 'urlDomain' completo
     * Viene usato in molte (non tutte) urlRequest delle sottoclassi di AQueryWiki
     */
    protected final static String TAG_QUERY = TAG_BASE + "&action=query";

    @Autowired
    protected WLogin wLogin;

    @Autowired
    protected AWikiService wikiService;

    /**
     * Property per controllare se nella urlresponse esiste il tag 'batchcomplete=true'
     */
    protected boolean isUrlResponseValida = false;

    protected String itwikiSession;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Può essere usato anche per creare l'istanza come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryWiki() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, titoloWiki).urlResponse() <br>
     *
     * @param titoloWiki della pagina (necessita di codifica) usato nella urlRequest
     */
    public AQueryWiki(String titoloWiki) {
        super(titoloWiki);
    }// end of constructor


    /**
     * Le preferenze vengono (eventualmente) sovrascritte nella sottoclasse <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();

        if (wLogin != null) {
            cookies = wLogin.getCookies();
        }// end of if cycle
    }// end of method


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
        try { // prova ad eseguire il codice
            return text.isValid(titoloWiki) ? URLEncoder.encode(titoloWiki, ENCODE) : VUOTA;
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

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
        isUrlResponseValida = LibWiki.isResponseValid(urlResponse);
        return urlResponse;
    } // fine del metodo

}// end of class
