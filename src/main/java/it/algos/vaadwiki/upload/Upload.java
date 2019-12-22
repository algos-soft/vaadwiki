package it.algos.vaadwiki.upload;

import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.mese.MeseService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.service.*;
import it.algos.vaadwiki.download.*;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.liste.Lista;
import it.algos.vaadwiki.liste.ListaService;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.Api;
import it.algos.wiki.LibWiki;
import it.algos.wiki.web.AQueryWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 17-gen-2019
 * Time: 17:45
 * <p>
 * Classe specializzata per caricare (upload) le liste sul server wiki. <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Necessita del login come bot <br>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 * Punto di inzio @PostConstruct inizia() nella superclasse <br>
 */
@Slf4j
public abstract class Upload {

    public final static String PAGINA_PROVA = "Utente:Biobot/2";

    protected final static String TAG_NON_SCRIVERE = "<!-- NON MODIFICATE DIRETTAMENTE QUESTA PAGINA - GRAZIE -->";

    protected final static String TAG_INDICE = "__FORCETOC__";

    protected final static String TAG_NO_INDICE = "__NOTOC__";

    //
//    protected boolean usaOrdineAlfabeticoParagrafi;

    //--property
    public boolean usaRigheRaggruppate;

    public int taglioSottoPagina;

    //--property
    public boolean usaParagrafoSize;

    //--property
    protected boolean usaBodySottopagine;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AttivitaService attivitaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected NazionalitaService nazionalitaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ProfessioneService professioneService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected BioService bioService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected NewService newService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected DeleteService deleteService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected UpdateService updateService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ElaboraService elaboraService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected PageService pageService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AArrayService array;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ADateService date;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected Api api;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AMongoService mongo;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AMailService mail;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected LogService logger;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected MeseService mese;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AnnoService anno;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected SecoloService secolo;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ListaService listaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected UploadService uploadService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected LibBio libBio;

    protected boolean usaHeadTemplateAvviso; // uso del template StatBio

    protected String tagHeadTemplateAvviso; // template 'StatBio'

    protected String tagHeadTemplateProgetto;

    protected boolean usaHeadNonScrivere;

    protected boolean usaHeadToc;

    protected boolean usaHeadTocIndice;

    protected boolean usaHeadRitorno; // prima del template di avviso

    protected boolean usaHeadInclude; // vero per Giorni ed Anni

    protected boolean usaHeadIncipit; // dopo il template di avviso

    protected boolean usaBodyDoppiaColonna;

    protected boolean usaBodyTemplate;

    protected boolean usaSuddivisioneParagrafi;

    protected boolean usaNote;

    protected boolean usaVociCorrelate;

    protected String titoloParagrafoVuoto;

    protected String incipitSottopagina;

    protected List<String> listaCorrelate;

    //--property
    protected Lista lista;

    //--property
    protected String titoloPagina;

    //--property
    protected String tagCategoria;

    //--property
    protected String testoLista;

    //--property
    protected String testoPagina;

    //--property
    protected String soggetto;

    //--property
    protected EADidascalia typeDidascalia;

    protected int numVoci = 0;


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
    protected void inizia() {
        this.fixPreferenze();
        this.elaboraPagina();
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        // head
        usaHeadNonScrivere = true; //pref.isBool(CostBio.USA_HEAD_NON_SCRIVERE, true);
        usaHeadInclude = true; //--tipicamente sempre true. Si attiva solo se c'è del testo (iniziale) da includere
        usaHeadToc = true; //--tipicamente sempre true.
        usaHeadTocIndice = true; //--normalmente true. Sovrascrivibile da preferenze
        usaHeadRitorno = true; //--normalmente true. Sovrascrivibile da preferenze
        usaHeadTemplateAvviso = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        tagHeadTemplateAvviso = "ListaBio"; //--Sovrascrivibile da preferenze
        tagHeadTemplateProgetto = "biografie"; //--Sovrascrivibile da preferenze
        usaHeadIncipit = false; //--normalmente false. Sovrascrivibile da preferenze

        // body
        usaBodySottopagine = false; //--normalmente false. Sovrascrivibile nelle sottoclassi
        usaRigheRaggruppate = true; //--normalmente true. Sovrascrivibile da preferenze
        usaBodyDoppiaColonna = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        usaBodyTemplate = true; //--normalmente true. Sovrascrivibile nelle sottoclassi

        // footer
        usaNote = false; //--normalmente false. Sovrascrivibile nelle sottoclassi
        usaVociCorrelate = false; //--normalmente false. Sovrascrivibile nelle sottoclassi
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
    protected void elaboraPagina() {
        String summary = LibWiki.getSummary();
        testoPagina = VUOTA;

        numVoci = lista.size;
        titoloParagrafoVuoto = lista.titoloParagrafoVuoto;
        taglioSottoPagina = lista.taglioSottoPagina;
        usaParagrafoSize = lista.usaParagrafoSize;

        //header
        testoPagina += this.elaboraHead();

        //body
        testoPagina += this.elaboraBody();

        //footer
        //di fila nella stessa riga, senza ritorno a capo (se inizia con <include>)
        testoPagina += A_CAPO;
        testoPagina += this.elaboraFooter();

        //--registra la pagina principale
        if (dimensioniValide()) {
            testoPagina = testoPagina.trim();

            if (pref.isBool(FlowCost.USA_DEBUG)) {
                testoPagina = titoloPagina + A_CAPO + testoPagina;
                titoloPagina = PAGINA_PROVA;
            }// end of if cycle

            //--nelle sottopagine non eseguo il controllo e le registro sempre (per adesso)
            if (checkPossoRegistrare(titoloPagina, testoPagina) || pref.isBool(FlowCost.USA_DEBUG)) {
                appContext.getBean(AQueryWrite.class, titoloPagina, testoPagina);
                log.info("Registrata la pagina: " + titoloPagina);
            } else {
                log.info("Non modificata la pagina: " + titoloPagina);
            }// end of if/else cycle

            //--registra eventuali sottopagine
            if (usaBodySottopagine) {
                uploadSottoPagine();
            }// end of if cycle
        }// end of if cycle

    }// fine del metodo


    protected boolean dimensioniValide() {
        boolean uploadValido = false;

        switch (typeDidascalia) {
            case giornoNato:
            case giornoMorto:
            case annoNato:
            case annoMorto:
                uploadValido = numVoci > 0;
                break;
            case listaNomi:
                uploadValido = numVoci > pref.getInt(SOGLIA_NOMI_PAGINA_WIKI);
                break;
            case listaCognomi:
                uploadValido = numVoci > pref.getInt(SOGLIA_COGNOMI_PAGINA_WIKI);
                break;
            case listaAttivita:
            case listaNazionalita:
                uploadValido = numVoci > pref.getInt(SOGLIA_ATT_NAZ_PAGINA_WIKI);
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        if (!uploadValido) {
            log.info("La pagina " + titoloPagina + " non contiene un numero sufficiente di voci biografiche e non è stata creata");
        }// end of if cycle

        return uploadValido;
    }// fine del metodo


    /**
     * Costruisce il testo iniziale della pagina (header)
     * <p>
     * Non sovrascrivibile <br>
     * Posiziona il TOC <br>
     * Posiziona il ritorno (eventuale) <br>
     * Posizione il template di avviso <br>
     * Posiziona l'incipit della pagina (eventuale) <br>
     * Ritorno ed avviso vanno (eventualmente) protetti con 'include' <br>
     * Ogni blocco esce trimmato (per l'inizio) e con un solo ritorno a capo per fine riga. <br>
     * Eventuali spazi gestiti da chi usa il metodo <br>
     */
    protected String elaboraHead() {
        String testo = VUOTA;
        String testoIncluso = VUOTA;

        // Avviso visibile solo in modifica
        testo += elaboraAvvisoScrittura();

        // Posiziona il TOC
        testoIncluso += elaboraTOC();

        // Posiziona il ritorno
        testoIncluso += elaboraRitorno();

        // Posizione il template di avviso
        testoIncluso += elaboraTemplateAvviso();

        // Ritorno ed avviso vanno (eventualmente) protetti con 'include'
        testo += elaboraInclude(testoIncluso);

        // Posiziona l'incipit della pagina
        testo += A_CAPO;
        testo += elaboraIncipit();

        // valore di ritorno
        return testo;
    }// fine del metodo


    /**
     * Avviso visibile solo in modifica
     * <p>
     * Non sovrascrivibile <br>
     */
    private String elaboraAvvisoScrittura() {
        String testo = VUOTA;

        if (usaHeadNonScrivere) {
            testo += TAG_NON_SCRIVERE;
            testo += A_CAPO;
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Costruisce il TOC (tavola contenuti)
     * <p>
     * Non sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo di una delle due possibilità <br>
     */
    private String elaboraTOC() {
        String testo = VUOTA;

        if (usaHeadToc) {
            if (usaHeadTocIndice) {
                testo += TAG_INDICE;
            } else {
                testo += TAG_NO_INDICE;
            }// testo del blocco if-else
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Costruisce il ritorno alla pagina 'madre'
     * <p>
     * Non sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    private String elaboraRitorno() {
        String testo = VUOTA;
        String titoloPaginaMadre = getTitoloPaginaMadre();

        if (usaHeadRitorno) {
            if (text.isValid(titoloPaginaMadre)) {
                testo += "Torna a|" + titoloPaginaMadre;
                testo = LibWiki.setGraffe(testo);
            }// fine del blocco if
        }// fine del blocco if

        return testo;
    }// fine del metodo


    /**
     * Titolo della pagina 'madre'
     * <p>
     * Sovrascritto
     */
    protected String getTitoloPaginaMadre() {
        return VUOTA;
    }// fine del metodo


    /**
     * Costruisce il template di avviso
     * <p>
     * Non sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) il nome del template da usare <br>
     */
    protected String elaboraTemplateAvviso() {
        String testo = VUOTA;
        String dataCorrente = date.get();
        String personeTxt = "";
        personeTxt = text.format(numVoci);

        if (usaHeadTemplateAvviso) {
            testo += tagHeadTemplateAvviso;
            testo += "|bio=";
            testo += personeTxt;
            testo += "|data=";
            testo += dataCorrente.trim();
            testo += "|progetto=";
            testo += tagHeadTemplateProgetto;
            testo = LibWiki.setGraffe(testo);
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Incorpora il testo iniziale nel tag 'include'
     * <p>
     * Non sovrascrivibile <br>
     * Tipicamente sempre true. Si attiva solo se c'è del testo (iniziale) da includere
     */
    private String elaboraInclude(String testoIn) {
        String testoOut = testoIn;

        if (usaHeadInclude) {
            testoOut = LibBio.setNoIncludeRiga(testoIn);
        }// fine del blocco if

        return testoOut;
    }// fine del metodo


    /**
     * Costruisce la frase di incipit iniziale
     * <p>
     * Non sovrascrivibile <br>
     */
    private String elaboraIncipit() {
        String testo = VUOTA;

        if (usaHeadIncipit) {
            testo += elaboraIncipitSpecifico();
            testo += A_CAPO;
        }// fine del blocco if

        return testo;
    }// fine del metodo


    /**
     * Costruisce la frase di incipit iniziale
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecifico() {
        return VUOTA;
    }// fine del metodo


    /**
     * Costruisce la frase di incipit iniziale per la sottopagina
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecificoSottopagina(String soggettoSottopagina) {
        return VUOTA;
    }// fine del metodo


    /**
     * Corpo della pagina
     * Decide se c'è la doppia colonna
     * Controlla eventuali template di rinvio
     * Sovrascritto
     */
    protected String elaboraBody() {
        String testoLista = "";
        testoLista = lista.getTesto();

        //aggiunge i tag per l'incolonnamento automatico del testo (proprietà mediawiki)
        if (usaBodyDoppiaColonna && (numVoci > pref.getInt(MAX_RIGHE_COLONNE))) {
            testoLista = LibWiki.setColonne(testoLista);
        }// fine del blocco if

        if (usaBodyTemplate) {
            if (!pref.isBool(USA_DEBUG)) {
                testoLista = elaboraTemplate(testoLista);
            }// end of if cycle
        }// end of if cycle

        return testoLista;
    }// fine del metodo


    /**
     * Esegue l'upload delle sottopagine <br>
     */
    protected void uploadSottoPagine() {
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa;
        int numVoci = 0;

        for (String key : lista.getSottoPagine().keySet()) {
            mappa = lista.getSottoPagine().get(key);
            numVoci = lista.getMappaLista().getDimParagrafo(key);
            incipitSottopagina = elaboraIncipitSpecificoSottopagina(key);
            listaCorrelate = listaVociCorrelate();
            appContext.getBean(UploadSottoPagina.class, soggetto, key, mappa, typeDidascalia, numVoci, usaParagrafoSize, incipitSottopagina, usaNote, usaVociCorrelate, listaCorrelate);
        }// end of for cycle
    }// end of method


    /**
     * Incapsula il testo come parametro di un (eventuale) template
     * Se non viene incapsulato, restituisce il testo in ingresso
     * Sovrascritto
     */
    protected String elaboraTemplate(String testoIn) {
        return testoIn;
    }// fine del metodo


    /**
     * Piede della pagina
     * Sovrascritto
     */
    protected String elaboraFooter() {
        String testo = A_CAPO;
        boolean nascosta = pref.isBool(FlowCost.USA_DEBUG);
        String cat;

        if (usaNote) {
            testo += usaNote();
        }// end of if cycle

        if (usaVociCorrelate) {
            testo += usaVociCorrelate();
        }// end of if cycle

        testo += LibWiki.setPortale(tagHeadTemplateProgetto);
        cat = tagCategoria;
        cat = nascosta ? LibWiki.setNowiki(cat) : cat;
        testo += cat;

        return testo;
    }// fine del metodo


    /**
     * Paragrafo delle note (eventuale)
     * Sovrascritto
     */
    protected String usaNote() {
        String testo = VUOTA;
        String tag = "Note";
        String ref = "<references/>";

        testo += LibWiki.setParagrafo(tag);
        testo += A_CAPO;
        testo += ref;
        testo += A_CAPO;
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    /**
     * Paragrafo delle voci correlate (eventuale)
     */
    protected String usaVociCorrelate() {
        StringBuilder testo = new StringBuilder(VUOTA);
        String titolo = "Voci correlate";
        List<String> lista = listaCorrelate == null ? listaVociCorrelate() : listaCorrelate;
        String tag = "*";

        if (array.isValid(lista)) {
            testo.append(LibWiki.setParagrafo(titolo));
            testo.append(A_CAPO);
            for (String riga : lista) {
                testo.append(tag);
                testo.append(LibWiki.setQuadre(riga));
                testo.append(A_CAPO);
            }// end of for cycle
            testo.append(A_CAPO);
        }// end of if cycle

        return testo.toString();
    }// fine del metodo


    /**
     * Lista delle voci correlate (eventuale)
     * Sovrascritto
     */
    protected List<String> listaVociCorrelate() {
        return null;
    }// fine del metodo


    /**
     * Controlla che la modifica sia sostanziale
     * <p>
     * Sovrascritto
     */
    protected boolean checkPossoRegistrare(String titolo, String testo) {
        return uploadService.checkModificaSostanziale(titolo, testo, tagHeadTemplateAvviso, "}}");
    }// fine del metodo


    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Upload";
    }// end of method


    public String getTesto() {
        return testoPagina;
    }// end of method

}// end of class
