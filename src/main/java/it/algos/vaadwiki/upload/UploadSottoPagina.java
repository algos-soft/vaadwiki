package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.LibWiki;
import it.algos.wiki.web.AQueryWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;

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

    //--property
    public boolean usaParagrafoSize;

    protected String prefixTitolo;

    //--property
    protected String titoloBrevePaginaPrincipale;

    //--property
    protected String titoloBreveSottoPagina;

    //--property
    protected LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa;

    private boolean usaNoteSottopagina;

    private boolean usaVociCorrelateSottopagina;


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
     * @param mappa                       mappa delle didascalie suddivise per iniziale alfabetica del cognome
     * @param usaParagrafoSize            nei titoli dei paragrafi
     * @param incipitSottopagina          creato nella classe specifica di Upload
     * @param usaNote                     come nella pagina principale
     * @param usaVociCorrelate            come nella pagina principale
     * @param listaCorrelate              creata nella classe specifica di Upload
     */
    public UploadSottoPagina(String titoloBrevePaginaPrincipale, String titoloBreveSottoPagina, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa, EADidascalia typeDidascalia, int numVoci, boolean usaParagrafoSize, String incipitSottopagina, boolean usaNote, boolean usaVociCorrelate, List<String> listaCorrelate) {
        this.titoloBrevePaginaPrincipale = titoloBrevePaginaPrincipale;
        this.titoloBreveSottoPagina = titoloBreveSottoPagina;
        this.mappa = mappa;
        this.typeDidascalia = typeDidascalia;
        this.numVoci = numVoci;
        this.usaParagrafoSize = usaParagrafoSize;
        this.incipitSottopagina = incipitSottopagina;
        this.usaNoteSottopagina = usaNote;
        this.usaVociCorrelateSottopagina = usaVociCorrelate;
        this.listaCorrelate = listaCorrelate;
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

        super.usaSuddivisioneParagrafi = false;
        super.usaRigheRaggruppate = false;
        super.usaHeadTocIndice = false;
        super.usaHeadIncipit = true;
        super.usaBodyDoppiaColonna = false;
        super.usaNote = usaNoteSottopagina;
        super.usaVociCorrelate = usaVociCorrelateSottopagina;

        String titoloPagina = text.primaMaiuscola(titoloBrevePaginaPrincipale);
        String titoloSotto = text.primaMaiuscola(titoloBreveSottoPagina);
        this.prefixTitolo = typeDidascalia.pagina;
        super.titoloPagina = prefixTitolo + titoloPagina + "/" + titoloSotto;
        String nomeCat = titoloPagina + "/" + titoloSotto;
        super.tagCategoria = LibWiki.setCat(typeDidascalia.categoria, nomeCat);
    }// end of method


    /**
     * Elaborazione principale della pagina
     * <p>
     * Costruisce head <br>
     * Costruisce body <br>
     * Costruisce footer <br>
     * Ogni blocco esce trimmato (inizio e fine) <br>
     * Gli spazi (righe) di separazione vanno aggiunti qui <br>
     * Registra la pagina <br>
     */
    @Override
    protected void elaboraPagina() {
        String summary = LibWiki.getSummary();
        testoPagina = VUOTA;

        //header
        testoPagina += this.elaboraHead();

        //body
        testoPagina += this.elaboraBody();

        //footer
        //di fila nella stessa riga, senza ritorno a capo (se inizia con <include>)
        testoPagina += A_CAPO;
        testoPagina += this.elaboraFooter();

        //--registra la sottopagina
        if (dimensioniValide()) {
            testoPagina = testoPagina.trim();

            if (pref.isBool(FlowCost.USA_DEBUG)) {
                testoPagina = titoloPagina + A_CAPO + testoPagina;
                titoloPagina = PAGINA_PROVA;
            }// end of if cycle

            //--nelle sottopagine non eseguo il controllo e le registro sempre (per adesso)
            appContext.getBean(AQueryWrite.class, titoloPagina, testoPagina);
            logger.info("Registrata la pagina: " + titoloPagina);
        }// fine del blocco if

    }// fine del metodo


    protected boolean dimensioniValide() {
        boolean uploadValido = false;

        switch (typeDidascalia) {
            case giornoNato:
            case giornoMorto:
            case annoNato:
            case annoMorto:
                uploadValido = numVoci > pref.getInt(TAGLIO_SOTTOPAGINA_GIORNI_ANNI);
                break;
            case listaNomi:
            case listaCognomi:
                uploadValido = numVoci > pref.getInt(TAGLIO_SOTTOPAGINA_NOMI_COGNOMI);
                break;
            case listaAttivita:
            case listaNazionalita:
                uploadValido = numVoci > pref.getInt(TAGLIO_SOTTOPAGINA_ATT_NAZ);
                break;
            default:
                logger.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        if (!uploadValido) {
            logger.warn("La sottopagina " + titoloPagina + " non contiene un numero sufficiente di voci biografiche e non è stata creata");
        }// end of if cycle

        return uploadValido;
    }// fine del metodo


    /**
     * Costruisce la frase di incipit iniziale
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecifico() {
        return incipitSottopagina;
    }// fine del metodo


    /**
     * Corpo della pagina
     * Decide se c'è la doppia colonna
     * Controlla eventuali template di rinvio
     * Sovrascritto
     */
    protected String elaboraBody() {
        return getTesto();
    }// fine del metodo


    /**
     * Testo finale della pagina <br>
     * Con o senza suddivisione per paragrafi <br>
     * Senza righe raggruppate <br>
     * Senza wikilink nel titolo dei paragrafi <br>
     * Con o senza dimensioni nel titolo dei paragrafi <br>
     * Se si usano le sottosottopagine riporta solo il link alla sottopagina. La lista delle biografie è in altra mappa <br>
     */
    public String getTesto() {
        StringBuilder testoLista = new StringBuilder();
        List<String> listaRighe = getListaRighe();

        if (listaRighe != null && listaRighe.size() > 0) {
            for (String riga : listaRighe) {
                testoLista.append(riga);
                testoLista.append(A_CAPO);
            }// end of for cycle
        }// end of if cycle

        return testoLista.toString().trim();
    }// fine del metodo


    /**
     * Lista di righe <br>
     */
    private List<String> getListaRighe() {
        List<String> listaRighe = null;
        LinkedHashMap<String, List<String>> mappaSottoPaginaTxt;
        List<String> listaTxt = null;
        String titoloParagrafo = VUOTA;

        if (mappa != null && mappa.size() > 0) {
            listaRighe = new ArrayList<>();
            for (String chiaveSottoPagina : mappa.keySet()) {
                mappaSottoPaginaTxt = mappa.get(chiaveSottoPagina);
                if (mappaSottoPaginaTxt.size() > 0) {
                    titoloParagrafo = addSize(chiaveSottoPagina, mappaSottoPaginaTxt.get(null).size());
                    listaRighe.add(LibBio.setParagrafo(titoloParagrafo));
                    for (String chiaveSottoSottoPagina : mappaSottoPaginaTxt.keySet()) {
                        listaTxt = mappaSottoPaginaTxt.get(chiaveSottoSottoPagina);
                        listaRighe.addAll(listaTxt);
                    }// end of for cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return listaRighe;
    }// fine del metodo


    /**
     * Titolo con dimensioni
     */
    protected String addSize(String chiaveParagrafo, int size) {
        String titoloParagrafo = chiaveParagrafo;

        if (usaParagrafoSize) {
            titoloParagrafo = chiaveParagrafo + " <span style=\"font-size:70%\">(" + size + ")</span>";
        }// end of if cycle

        return titoloParagrafo;
    }// fine del metodo


    /**
     * Titolo della pagina 'madre'
     * <p>
     * Sovrascritto
     */
    protected String getTitoloPaginaMadre() {
        return prefixTitolo + text.primaMaiuscola(titoloBrevePaginaPrincipale);
    }// fine del metodo


//    protected String mettereInService(LinkedHashMap<String, List<String>> mappaAlfabetica) {
//        StringBuilder testoLista = new StringBuilder();
//
//        if (mappaAlfabetica != null) {
//            for (String titoloParagrafo : mappaAlfabetica.keySet()) {
//                testoLista.append(PARAGRAFO).append(titoloParagrafo).append(PARAGRAFO).append(A_CAPO);
//                for (String didascalia : mappaAlfabetica.get(titoloParagrafo)) {
//                    testoLista.append("*").append(didascalia).append("\n");
//                }// end of for cycle
//            }// end of for cycle
//        }// end of if cycle
//
//        return testoLista.toString();
//    }// fine del metodo

}// end of class
