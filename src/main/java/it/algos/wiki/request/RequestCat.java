package it.algos.wiki.request;


import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.service.AArrayService;
import it.algos.wiki.Api;
import it.algos.wiki.LibWiki;
import it.algos.wiki.TipoRisultato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gac on 01 feb 2016.
 * .
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RequestCat extends ARequest {

    /**
     * Il limite massimo ''for users'' è di 500
     * Il limite massimo ''for bots or sysops'' è di 5.000
     * Il limite 'max' equivale a 5.000
     */
    protected static final int LIMITE = 5000;

    protected static final String START_LIMIT_ERROR = "cmlimit may not be over";

    //--stringa per la lista di categoria
    protected static String CAT = "&list=categorymembers";

    //--stringa per selezionare il namespace (0=principale - 14=sottocategorie) ed il tipo di categoria (page, subcat, file)
    protected static String TYPE_VOCI = "&cmnamespace=0&cmtype=page";
    protected static String TYPE_ALL = "&cmnamespace=0|14&cmtype=page|subcat";

    //--stringa per selezionare il numero di valori in risposta
    protected static String TAG_LIMIT = "&cmlimit=";

    //--stringa per indicare il titolo della pagina
    protected static String TAG_TITOLO_CAT = "&cmtitle=Category:";

    //--stringa per il successivo inizio della lista
    private static String CONTINUE = "&cmcontinue=";

    protected int limite;

    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    public AArrayService array = StaticContextAccessor.getBean(AArrayService.class);

//    /**
//     * Costruttore completo
//     *
//     * @param wikiTitle titolo della pagina wiki su cui operare
//     */
//    public RequestCat(String wikiTitle) {
//        super(wikiTitle);
//    }// fine del metodo costruttore completo




    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    @Override
    protected void elaboraParametri() {
        super.elaboraParametri();
        needContinua = true;
        needPost = false;
        needLogin = true;
        needToken = false;
        needBot = true;
        limite = 5000;

//        needBot = false;
//        needLogin = false;

    }// fine del metodo


    /**
     * Stringa del browser per la request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected String elaboraDomain() {
        String domainTmp = API_BASE + API_ACTION + API_QUERY + CAT + TYPE_ALL;

        if (wikiTitle != null && !wikiTitle.equals("")) {
            domainTmp += TAG_TITOLO_CAT + titleEncoded();
        }// end of if/else cycle

        if (needBot) {
            domainTmp += API_ASSERT;
        }// end of if cycle

        if (limite > 0) {
            domainTmp += TAG_LIMIT + limite;
        }// end of if cycle

        if (!tokenContinua.equals("")) {
            domainTmp += CONTINUE + tokenContinua;
        }// fine del blocco if

        domain = domainTmp;
        return domainTmp;
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
        mappa = LibWiki.getMappaWrap(rispostaRequest);

        if (mappa == null) {
            if (Api.esiste("Category:" + titleEncoded())) {
                risultato = TipoRisultato.letta;
            } else {
                risultato = TipoRisultato.nonTrovata;
            }// end of if/else cycle
            valida = false;
            return;
        }// fine del blocco if

        if (array != null && mappa.get(LibWiki.KEY_VOCE_PAGEID) != null && mappa.get(LibWiki.KEY_VOCE_PAGEID).size() > 0) {
            this.listaVociPageids = array.sommaDisordinata(this.listaVociPageids, mappa.get(LibWiki.KEY_VOCE_PAGEID));
        }// end of if cycle

        if (array != null && mappa.get(LibWiki.KEY_VOCE_TITLE) != null && mappa.get(LibWiki.KEY_VOCE_TITLE).size() > 0) {
            this.listaVociTitles = array.sommaDisordinata(this.listaVociTitles, mappa.get(LibWiki.KEY_VOCE_TITLE));
        }// end of if cycle

        if (array != null && mappa.get(LibWiki.KEY_CAT_PAGEID) != null && mappa.get(LibWiki.KEY_CAT_PAGEID).size() > 0) {
            this.listaCatPageids = array.sommaDisordinata(this.listaCatPageids, mappa.get(LibWiki.KEY_CAT_PAGEID));
        }// end of if cycle

        if (array != null && mappa.get(LibWiki.KEY_CAT_TITLE) != null && mappa.get(LibWiki.KEY_CAT_TITLE).size() > 0) {
            this.listaCatTitles = array.sommaDisordinata(this.listaCatTitles, mappa.get(LibWiki.KEY_CAT_TITLE));
        }// end of if cycle

        risultato = TipoRisultato.letta;
        valida = true;
        tokenContinua = LibWiki.creaCatContinue(rispostaRequest);

        warningMessage = LibWiki.getWarning(rispostaRequest);
        if (warningMessage != null && warningMessage.startsWith(START_LIMIT_ERROR)) {
            risultato = TipoRisultato.limitOver;
        }// end of if cycle

    } // fine del metodo


} // fine della classe
