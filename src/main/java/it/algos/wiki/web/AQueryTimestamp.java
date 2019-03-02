package it.algos.wiki.web;

import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;
import it.algos.wiki.WrapTime;
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
 * Date: sab, 02-mar-2019
 * Time: 14:33
 */
@Component("AQueryTimestamp")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AQueryTimestamp extends AQueryGet {


    //--tag per la costruzione della stringa della request
    private final static String TAG_PROP_TITLES = "&prop=revisions&rvprop=timestamp&titles=";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    protected final static String TAG_TIMESTAMP = TAG_QUERY + TAG_BOT + TAG_SLOTS + TAG_PROP_TITLES;


    //--stringa (separata da pipe oppure da virgola) dei titles
    private String stringaTitles;

    private ArrayList<String> arrayTitles;

    //--lista di wrapper con wikititle e timestamp
    private ArrayList<WrapTime> listaWrapTime;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(AQueryxxx.class) <br>
     */
    public AQueryTimestamp() {
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
    public AQueryTimestamp(ArrayList<String> arrayTitles) {
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
        return titoloWikiGrezzo.startsWith(TAG_TIMESTAMP) ? titoloWikiGrezzo : TAG_TIMESTAMP + titoloWikiGrezzo;
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


    public ArrayList<WrapTime> urlRequest(ArrayList<String> arrayTitles) {
        return null;
    } // fine del metodo


    /**
     * Lista di wrapper con wikititle e timestamp
     *
     * @return lista dei wrapper costruiti con la risposta
     */
    public ArrayList<WrapTime> timestampResponse() {
        return timestampResponse(arrayTitles);
    }// end of method


    /**
     * Lista di wrapper con wikititle e timestamp
     *
     * @param arrayTitles lista (titles) di pagine da controllare sul server wiki
     *
     * @return lista dei wrapper costruiti con la risposta
     */
    public ArrayList<WrapTime> timestampResponse(ArrayList<String> arrayTitles) {
        this.arrayTitles = arrayTitles;
        return timestampResponse(wikiService.multiPages(arrayTitles));
    }// end of method


    /**
     * Lista di wrapper con wikititle e timestamp
     *
     * @param stringaTitles (separata da pipe oppure da virgola) dei titles
     *
     * @return lista dei wrapper costruiti con la risposta
     */
    public ArrayList<WrapTime> timestampResponse(String stringaTitles) {
        ArrayList<WrapTime> listaWrapper = null;
        String contenutoCompletoPaginaWebInFormatoJSON = "";
        HashMap<String, ArrayList<WrapTime>> mappa;

        if (text.isValid(stringaTitles)) {
            try { // prova ad eseguire il codice
                contenutoCompletoPaginaWebInFormatoJSON = super.urlRequest(stringaTitles);

                mappa = LibWiki.creaArrayWrapTime(contenutoCompletoPaginaWebInFormatoJSON);
                if (mappa != null) {
                    listaWrapTime = mappa.get(LibWiki.KEY_PAGINE_VALIDE);
//                    listaWrapTimeMissing = mappa.get(LibWiki.KEY_PAGINE_MANCANTI);
//                    risultato = TipoRisultato.letta;
//                    valida = true;
                } else {
//                    risultato = TipoRisultato.nonTrovata;
//                    valida = false;
                }// end of if/else cycle


//                listaPages = wikiService.getListaPages(contenutoCompletoPaginaWebInFormatoJSON);
            } catch (Exception unErrore) { // intercetta l'errore
                String errore = unErrore.getMessage();
            }// fine del blocco try-catch
        }// end of if cycle

        return listaWrapTime;
    }// end of method

}// end of class
