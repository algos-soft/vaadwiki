package it.algos.wiki.web;

import it.algos.wiki.LibWiki;
import it.algos.wiki.Page;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 09-feb-2019
 * Time: 20:34
 * <p>
 * UrlRequest:
 * urlDomain = "&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles="
 * GET request
 * No POST text
 * Upload cookies
 * Bot needed
 */
@Component("AQueryPages")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AQueryPages extends AQueryGet {


    /**
     * Tag completo 'urlDomain' per il controllo
     */
    protected final static String TAG_BOT = "&assert=bot";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    protected final static String TAG_PAGES = TAG_QUERY  + TAG_BOT + TAG_SLOTS + TAG_INFO;

    //--stringa (separata da pipe oppure da virgola) dei titles
    private String stringaTitles;

    private ArrayList<String> arrayTitles;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryPages() {
        super();
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest) <br>
     * Usa: appContext.getBean(AQueryxxx.class, urlRequest).pageResponse() <br>
     *
     * @param arrayTitles lista (titles) di pagine da scaricare dal server wiki
     */
    public AQueryPages(ArrayList<String> arrayTitles) {
        this.arrayTitles = arrayTitles;
    }// end of constructor


    /**
     * Le preferenze vengono (eventualmente) sovrascritte nella sottoclasse <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        this.isUsaBot = true;
        this.isUsaPost = false;
        this.isUploadCookies = true;
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
//        String titoloWikiCodificato = super.fixUrlDomain(titoloWikiGrezzo);
        return titoloWikiGrezzo.startsWith(TAG_PAGES) ? titoloWikiGrezzo : TAG_PAGES + titoloWikiGrezzo;
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
     * Pages della response
     *
     * @return lista delle pagine costruite con la risposta
     */
    public ArrayList<Page> pagesResponse() {
        return pagesResponse(arrayTitles);
    }// end of method


    /**
     * Pages della response
     *
     * @param arrayTitles lista (titles) di pagine da scaricare dal server wiki
     *
     * @return lista delle pagine costruite con la risposta
     */
    public ArrayList<Page> pagesResponse(ArrayList<String> arrayTitles) {
        this.arrayTitles = arrayTitles;
        return pagesResponse(wikiService.multiPages(arrayTitles));
//        return pagesResponse(array.toStringaPipe(arrayTitles));
    }// end of method


    /**
     * Pages della response
     *
     * @param stringaTitles (separata da pipe oppure da virgola) dei titles
     *
     * @return lista delle pagine costruite con la risposta
     */
    public ArrayList<Page> pagesResponse(String stringaTitles) {
        ArrayList<Page> listaPages = null;
        String contenutoCompletoPaginaWebInFormatoJSON = "";

        if (text.isValid(stringaTitles)) {
            try { // prova ad eseguire il codice
                contenutoCompletoPaginaWebInFormatoJSON = super.urlRequest(stringaTitles);
                listaPages = wikiService.getListaPages(contenutoCompletoPaginaWebInFormatoJSON);
            } catch (Exception unErrore) { // intercetta l'errore
                String errore = unErrore.getMessage();
            }// fine del blocco try-catch
        }// end of if cycle

        return listaPages;
    }// end of method


    /**
     * Effettua la request per pagine indicate
     */
    public void esegue(ArrayList<Long> arrayPageIds) {
//        esegue(array.toStringaPipe(arrayPageIds));
    } // fine del metodo


    /**
     * Effettua la request per pagine indicate
     */
    public void esegue(String stringaTitles) {
//        try { // prova ad eseguire il codice
//            stringaPageIds= URLEncoder.encode(stringaPageIds, "UTF-8");;
//        } catch (Exception unErrore) { // intercetta l'errore
//            log.error(unErrore.toString());
//        }// fine del blocco try-catch
//        super.stringaPageIds = stringaPageIds;
//        super.doRequest();
    } // fine del metodo


}// end of class
