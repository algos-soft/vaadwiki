package it.algos.wiki.web;

import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URLConnection;
import java.util.HashMap;

import static it.algos.wiki.LibWiki.CODE;
import static it.algos.wiki.LibWiki.INFO;


/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 08-feb-2019
 * Time: 16:50
 * <p>
 * Query di controllo per verificare che il collegamento sia abilitato come bot o come admin
 * <p>
 * urlDomain = "&assert=bot"
 * GET
 * no testoPOST
 * upload cookies
 * nella response -> true
 * nei cookies -> nessun cookies ritornato
 */
@Component("AQueryBot")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class AQueryBot extends AQueryGet {


    /**
     * Tag di controllo errore
     */
    private static String CODE_FAILED = "assertbotfailed";

    /**
     * Tag di controllo errore
     */
    private static String INFO_FAILED = "Assertion that the user has the \"bot\" right failed.";


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryBot() {
        super();
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
        super.isDownloadCookies = false;
    }// end of method


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
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    @Override
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
        return TAG_QUERY + TAG_BOT;
    }// end of method


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraResponse(String urlResponse) {
        HashMap<String, Object> mappa = null;

        if (LibWiki.isResponseValid(urlResponse)) {
            isUrlResponseValida = true;
        } else {
            isUrlResponseValida = false;
            mappa = LibWiki.creaMappaError(urlResponse);

            if (mappa == null) {
//                logger.info("AQueryBot - Nessuna risposta. Forse manca la linea.");
                return urlResponse;
            }// end of if cycle

            if (mappa.get(CODE) != null && mappa.get(CODE).equals(CODE_FAILED)) {
//                logger.info("AQueryBot - " + CODE_FAILED);
                return urlResponse;
            }// end of if cycle

            if (mappa.get(INFO) != null && mappa.get(INFO).equals(INFO_FAILED)) {
//                logger.info("AQueryBot - " + INFO_FAILED);
                return urlResponse;
            }// end of if cycle

//            logger.warn("AQueryBot - Errore generico");
            return urlResponse;
        }// end of if/else cycle

        return urlResponse;
    } // fine del metodo


    /**
     * Esito del controllo
     *
     * @return true se sono collegato (cookies) come bot o admin
     */
    public boolean isBot() {
        return isUrlResponseValida;
    } // fine del metodo

}// end of class
