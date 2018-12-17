package it.algos.wiki.request;


import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.Api;
import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;
import it.algos.wiki.WrapCat;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 20 nov 2015.
 * Adeguato a SprinBoot il 10 ago 2018
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RequestWikiCat extends RequestWiki {


    //--stringa per la lista di categoria
    private static String CAT = "&list=categorymembers";

    //--stringa per selezionare il namespace ed il tipo di categoria (page)
    private static String TYPE_VOCI = "&cmnamespace=0&cmtype=page";

    //--stringa per selezionare il numero di valori in risposta
    private static String TAG_LIMIT = "&cmlimit=";

    //--stringa per indicare il titolo della pagina
    private static String TITLE = "&cmtitle=Category:";

    //--stringa per il successivo inizio della lista
    private static String CONTINUE = "&cmcontinue=";


    // liste di pagine della categoria (namespace=0)
    private ArrayList<Long> listaVociPageids;

    private ArrayList<String> listaVociTitles;

    private ArrayList<WrapCat> listaWrapCat;

    private boolean debug = false;

    /**
     * Costruttore
     * <p>
     * Le sottoclassi non invocano il costruttore
     * Prima regolano alcuni parametri specifici
     * Poi invocano il metodo doInit() della superclasse astratta
     *
     *
     * @deprecated
     */
//    public RequestWikiCat() {
//    }// fine del metodo costruttore

//    /**
//     * Costruttore completo
//     * <p>
//     * Le sottoclassi non invocano il costruttore
//     * Prima regolano alcuni parametri specifici
//     * Poi invocano il metodo doInit() della superclasse astratta
//     *
//     * @param wikiTitle titolo della pagina wiki su cui operare
//     * @param limite    di pagine da caricare
//     *
//     * @deprecated
//     */
//    public RequestWikiCat(String wikiTitle, int limite) {
//        super.wikiTitle = wikiTitle;
//        this.limite = limite;
//        this.doInit();
//    }// fine del metodo costruttore completo
//
//    /**
//     * Costruttore for testing purpose only
//     *
//     * @param wikiTitle titolo della pagina wiki su cui operare
//     * @param wikiLogin del collegamento
//     *
//     * @deprecated
//     */
//    public RequestWikiCat(String wikiTitle, WikiLogin wikiLogin) {
//        this(wikiTitle, LIMITE, wikiLogin);
//    }// fine del metodo costruttore completo
//
//    /**
//     * Costruttore completo for testing purpose only
//     *
//     * @param wikiTitle titolo della pagina wiki su cui operare
//     * @param limite    di pagine da caricare
//     * @param wikiLogin del collegamento
//     *
//     * @deprecated
//     */
//    public RequestWikiCat(String wikiTitle, int limite, WikiLogin wikiLogin) {
//        super.wikiTitle = wikiTitle;
//        this.limite = limite;
//        super.wikiLogin = wikiLogin;
//        this.debug = false;
//        this.doInit();
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
        super.needToken = false;
        super.needContinua = true;
        super.needBot = true;
    }// fine del metodo


    /**
     * Effettua la request per la categoria specificata
     */
    public void esegue(String wikiTitle) {
        super.wikiTitle = wikiTitle;
        super.doRequest();
    } // fine del metodo


    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda della sottoclasse)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain = super.getDomain();

        try { // prova ad eseguire il codice
            domain += API_QUERY + CAT + TYPE_VOCI + TITLE + URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        if (limite > 0) {
            domain += TAG_LIMIT + limite;
        }// end of if cycle

        if (!tokenContinua.equals("")) {
            domain += CONTINUE + tokenContinua;
        }// fine del blocco if

        return domain;
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
        HashMap<String, ArrayList> mappa;
        super.elaboraRisposta(rispostaRequest);
        String warningMessage = "";

        listaWrapCat = array.somma(listaWrapCat, LibWiki.getListaWrapCat(rispostaRequest));
//        mappa = LibWiki.getMappaWrap(rispostaRequest);
//
//        if (mappa == null) {
//            if (Api.esiste("Category:" + wikiTitle)) {
//                risultato = TipoRisultato.letta;
//            } else {
//                risultato = TipoRisultato.nonTrovata;
//            }// end of if/else cycle
//            valida = false;
//            return;
//        }// fine del blocco if
//
//        if (mappa.get(LibWiki.KEY_VOCE_PAGEID) != null && mappa.get(LibWiki.KEY_VOCE_PAGEID).size() > 0) {
//            this.listaVociPageids = array.somma(this.listaVociPageids, mappa.get(LibWiki.KEY_VOCE_PAGEID));
//        }// end of if cycle
//
//        if (mappa.get(LibWiki.KEY_VOCE_TITLE) != null && mappa.get(LibWiki.KEY_VOCE_TITLE).size() > 0) {
//            this.listaVociTitles = array.somma(this.listaVociTitles, mappa.get(LibWiki.KEY_VOCE_TITLE));
//        }// end of if cycle

        risultato = TipoRisultato.letta;
        valida = true;
        tokenContinua = LibWiki.creaCatContinue(rispostaRequest);

        warningMessage = LibWiki.getWarning(rispostaRequest);
        if (warningMessage != null && warningMessage.startsWith(START_LIMIT_ERROR)) {
            risultato = TipoRisultato.limitOver;
        }// end of if cycle

    } // fine del metodo


    public ArrayList<Long> getListaVociPageids() {
        return listaVociPageids;
    }// end of getter method


    public ArrayList<String> getListaVociTitles() {
        return listaVociTitles;
    }// end of getter method


    public ArrayList<WrapCat> getListaWrapCat() {
        return listaWrapCat;
    }// end of getter method

} // fine della classe
