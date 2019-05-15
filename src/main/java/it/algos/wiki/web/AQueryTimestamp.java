package it.algos.wiki.web;

import it.algos.wiki.LibWiki;
import it.algos.wiki.WrapTime;
import org.json.simple.JSONArray;
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
    private final static String TAG_PROP_PAGEIDS = "&prop=revisions&rvprop=timestamp&pageids=";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    protected final static String TAG_TIMESTAMP = TAG_QUERY + TAG_BOT + TAG_SLOTS + TAG_PROP_PAGEIDS;


    //--stringa (separata da pipe oppure da virgola) dei titles
    private String stringaTitles;

    //    private ArrayList<String> arrayTitles;
    private ArrayList<Long> arrayPageid;

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
     * Usa: appContext.getBean(AQueryTimestamp.class, urlRequest) <br>
     * Usa: appContext.getBean(AQueryTimestamp.class, urlRequest).timestampResponse() <br>
     *
     * @param arrayPageid lista (pageid) di pagine da scaricare dal server wiki
     */
    public AQueryTimestamp(ArrayList<Long> arrayPageid) {
        this.arrayPageid = arrayPageid;
    }// end of constructor


//    /**
//     * Costruttore con parametri <br>
//     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
//     * Usa: appContext.getBean(AQueryTimestamp.class, urlRequest) <br>
//     * Usa: appContext.getBean(AQueryTimestamp.class, urlRequest).timestampResponse() <br>
//     *
//     * @param arrayTitles lista (titles) di pagine da scaricare dal server wiki
//     */
//    public AQueryTimestamp(ArrayList<String> arrayTitles) {
//        this.arrayTitles = arrayTitles;
//    }// end of constructor
//


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
        return timestampResponse(arrayPageid);
    }// end of method


    /**
     * Lista di wrapper con wikititle e timestamp
     *
     * @param arrayPageid lista (pageid) di pagine da controllare sul server wiki
     *
     * @return lista dei wrapper costruiti con la risposta
     */
    public ArrayList<WrapTime> timestampResponse(ArrayList<Long> arrayPageid) {
        this.arrayPageid = arrayPageid;
        return timestampResponse(wikiService.multiPages(arrayPageid));
    }// end of method


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraResponse(String urlResponse) {
        HashMap<String, ArrayList<WrapTime>> mappa;

        super.elaboraResponse(urlResponse);
        if (super.isUrlResponseValida) {
            mappa = LibWiki.creaArrayWrapTime(urlResponse);
            if (mappa != null) {
                listaWrapTime = mappa.get(LibWiki.KEY_PAGINE_VALIDE);
            }// end of if cycle
        }// end of if cycle

        return urlResponse;
    } // fine del metodo

    /**
     * Lista di wrapper con wikititle e timestamp
     *
     * @param stringaPageids (separata da pipe oppure da virgola) dei pageids
     *
     * @return lista dei wrapper costruiti con la risposta
     */
    public ArrayList<WrapTime> timestampResponse(String stringaPageids) {
//        ArrayList<WrapTime> listaWrapper = null;
//        String contenutoCompletoPaginaWebInFormatoJSON = "";
//        HashMap<String, ArrayList<WrapTime>> mappa;
//
//        if (text.isValid(stringaPageids)) {
//            try { // prova ad eseguire il codice
//                contenutoCompletoPaginaWebInFormatoJSON = super.urlRequest(stringaPageids);
//
//                mappa = LibWiki.creaArrayWrapTime(contenutoCompletoPaginaWebInFormatoJSON);
//                if (mappa != null) {
//                    listaWrapTime = mappa.get(LibWiki.KEY_PAGINE_VALIDE);
////                    listaWrapTimeMissing = mappa.get(LibWiki.KEY_PAGINE_MANCANTI);
////                    risultato = TipoRisultato.letta;
////                    valida = true;
//                } else {
////                    risultato = TipoRisultato.nonTrovata;
////                    valida = false;
//                }// end of if/else cycle
//
//
////                listaPages = wikiService.getListaPages(contenutoCompletoPaginaWebInFormatoJSON);
//            } catch (Exception unErrore) { // intercetta l'errore
//                String errore = unErrore.getMessage();
//            }// fine del blocco try-catch
//        }// end of if cycle
        super.urlRequest(stringaPageids);
        return listaWrapTime;
    }// end of method

}// end of class
