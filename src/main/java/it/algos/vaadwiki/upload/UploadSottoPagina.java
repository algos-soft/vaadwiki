package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.liste.ListaSottopagina;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.USA_DEBUG;
import static it.algos.vaadwiki.application.WikiCost.MAX_RIGHE_COLONNE;
import static it.algos.wiki.LibWiki.PARAGRAFO;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 29-set-2019
 * Time: 18:35
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UploadSottoPagina extends Upload {

    protected String prefixTitolo;

    //--property
    protected String titoloBrevePaginaPrincipale;

    //--property
    protected String titoloBreveSottoPagina;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadSottoPagina() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadSottoPagina.class, titoloBrevePaginaPrincipale, titoloCompletoSottoPagina, mappaAlfabetica) <br>
     *
     * @param titoloBrevePaginaPrincipale solo il nome/cognome per la costruzione del titolo e per le categorie
     * @param titoloBreveSottoPagina      attività della sottopagina per l'incipit (eventuale) e per le categorie
     * @param mappaAlfabetica             mappa delle didascalie suddivise per iniziale alfabetica del cognome
     */
    public UploadSottoPagina(String titoloBrevePaginaPrincipale, String titoloBreveSottoPagina, LinkedHashMap<String, List<String>> mappaAlfabetica) {
        this.titoloBrevePaginaPrincipale = titoloBrevePaginaPrincipale;
        this.titoloBreveSottoPagina = titoloBreveSottoPagina;
        this.mappaAlfabetica = mappaAlfabetica;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se hanno la stessa firma, chiama prima @PostConstruct della sottoclasse <br>
     * Se hanno firme diverse, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    protected void inizia() {
        super.inizia();
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.isSottoPagina = true;
        super.usaSuddivisioneParagrafi = false;
        super.usaRigheRaggruppate = false;
        super.usaHeadTocIndice = false;
        super.usaHeadIncipit = true;
        super.usaBodyDoppiaColonna = false;
    }// end of method


    /**
     * Corpo della pagina
     * Decide se c'è la doppia colonna
     * Controlla eventuali template di rinvio
     * Sovrascritto
     */
    protected String elaboraBody() {
        ListaSottopagina sottoPagina;
        String testoLista = "";

        testoLista = mettereInService(mappaAlfabetica);
        numVoci = 87;

        //aggiunge i tag per l'incolonnamento automatico del testo (proprietà mediawiki)
        if (usaBodyDoppiaColonna && (numVoci > pref.getInt(MAX_RIGHE_COLONNE))) {
            testoLista = LibWiki.setColonne(testoLista);
        }// fine del blocco if

        if (usaBodyTemplate) {
//            if (Pref.getBool(CostBio.USA_DEBUG, false)) {
//                text = elaboraTemplate("") + text;
//            } else {
//                text = elaboraTemplate(text);
//            }// end of if/else cycle
            if (!pref.isBool(USA_DEBUG)) {
                testoLista = elaboraTemplate(testoLista);
            }// end of if cycle
        }// end of if cycle

        return testoLista;
    }// fine del metodo


    /**
     * Titolo della pagina 'madre'
     * <p>
     * Sovrascritto
     */
    protected String getTitoloPaginaMadre() {
        return prefixTitolo + titoloBrevePaginaPrincipale;//@todo controllare
    }// fine del metodo


    protected String mettereInService(LinkedHashMap<String, List<String>> mappaAlfabetica) {
        StringBuilder testoLista = new StringBuilder();

        if (mappaAlfabetica != null) {
            for (String titoloParagrafo : mappaAlfabetica.keySet()) {
                testoLista.append(PARAGRAFO).append(titoloParagrafo).append(PARAGRAFO).append(A_CAPO);
                for (String didascalia : mappaAlfabetica.get(titoloParagrafo)) {
                    testoLista.append("*").append(didascalia).append("\n");
                }// end of for cycle
            }// end of for cycle
        }// end of if cycle

        return testoLista.toString();
    }// fine del metodo

}// end of class
