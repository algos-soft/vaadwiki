package it.algos.wiki.request;


import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.LibWiki;
import it.algos.wiki.Page;
import it.algos.wiki.TipoRisultato;
import it.algos.wiki.WrapTime;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 04 dic 2015.
 * .
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class RequestWikiReadPages extends RequestWiki {


    //--tag per la costruzione della stringa della request
//    private static String TAG_MULTIPAGES = TAG_PROP + "&pageids=";
    private static String TAG_MULTIPAGES = TAG_PROP + "&titles=";


    //--lista delle pagine costruite con la risposta
    private ArrayList<Page> listaPages;


//    /**
//     * Costruttore
//     * <p>
//     * Le sottoclassi non invocano il costruttore
//     * Prima regolano alcuni parametri specifici
//     * Poi invocano il metodo doInit() della superclasse astratta
//     *
//     * @param listaPageIds elenco di pageids (long)
//     * @deprecated
//     */
//    public RequestWikiReadMultiPages(long[] listaPageIds) {
//        super(listaPageIds);
//    }// fine del metodo costruttore

//    /**
//     * Costruttore
//     * <p>
//     * Le sottoclassi non invocano il costruttore
//     * Prima regolano alcuni parametri specifici
//     * Poi invocano il metodo doInit() della superclasse astratta
//     *
//     * @param arrayPageIds elenco di pageids (ArrayList)
//     * @deprecated
//     */
//    public RequestWikiReadMultiPages(ArrayList<Long> arrayPageIds) {
//        super(arrayPageIds);
//    }// fine del metodo costruttore


//    /**
//     * Costruttore completo
//     * <p>
//     * Le sottoclassi non invocano il costruttore
//     * Prima regolano alcuni parametri specifici
//     * Poi invocano il metodo doInit() della superclasse astratta
//     */
//    public RequestWikiReadMultiPages() {
//        super("");
//    }// fine del metodo costruttore completo


    /**
     * Executed on container startup
     * Setup non-UI logic here
     * <p>
     * This method is called prior to the servlet context being initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p>
     * Regola alcuni parametri specifici della sottoclasse
     */
    @PostConstruct
    protected void inizia() {
        super.needPost = false;
        super.needLogin = true;
        super.needToken = true;
        super.needContinua = true;
        super.needBot = true;
    }// fine del metodo


    /**
     * Effettua la request per pagine indicate
     */
    public void esegue(ArrayList<Long> arrayPageIds) {
        esegue(array.toStringaPipe(arrayPageIds));
    } // fine del metodo


    /**
     * Effettua la request per pagine indicate
     */
    public void esegue(String stringaPageIds) {
        try { // prova ad eseguire il codice
            stringaPageIds= URLEncoder.encode(stringaPageIds, "UTF-8");;
        } catch (Exception unErrore) { // intercetta l'errore
//            logger.error(unErrore.toString());
        }// fine del blocco try-catch
        super.stringaPageIds = stringaPageIds;
        super.doRequest();
    } // fine del metodo


//    /**
//     * Metodo iniziale invocato DOPO che la sottoclasse ha regolato alcuni parametri specifici
//     * PUO essere sovrascritto nelle sottoclassi specifiche
//     *
//     * @param stringaPageIds stringa (separata da pipe oppure da virgola) delle pageids
//     */
//    @Deprecated
//    public void doInit(String stringaPageIds) {
//        this.stringaPageIds = stringaPageIds;
//        tipoRicerca = TipoRicerca.listaPageids;
//        super.doInit();
//    } // fine del metodo


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda della sottoclasse)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        return super.getDomain() + API_QUERY + TAG_MULTIPAGES + stringaPageIds;
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void elaboraRisposta(String rispostaRequest) {
        HashMap<String, ArrayList<WrapTime>> mappa;
        super.elaboraRisposta(rispostaRequest);
        JSONArray arrayPages = LibWiki.getArrayPagesJSON(rispostaRequest);
        Page page;
        String tooBigText = "This result was truncated because it would otherwise be larger than the limit of 12,582,912 bytes.";
        String resulTwarnings = LibWiki.getWarningResult(rispostaRequest);

        //--recupera i valori dei parametri info
        if (arrayPages != null && arrayPages.size() > 0) {
            if (resulTwarnings.equals(tooBigText)) {
                risultato = TipoRisultato.tooBig;
            } else {
                risultato = TipoRisultato.letta;
            }// end of if/else cycle
            listaPages = new ArrayList<Page>();
//            for (int k = 0; k < arrayPages.size(); k++) {
//                page = new Page((JSONObject) arrayPages.get(k));
//                listaPages.add(page);
//            }// end of for cycle
        }// end of if cycle

    } // fine del metodo


    public ArrayList<Page> getListaPages() {
        return listaPages;
    }// end of getter method

//    public void setListaPages(ArrayList<Page> listaPages) {
//        this.listaPages = listaPages;
//    }//end of setter method

} // fine della classe
