package it.algos.vaadwiki.upload;

import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.mese.MeseService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.service.*;
import it.algos.vaadwiki.download.*;
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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static it.algos.vaadflow.application.FlowCost.*;

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

    protected LinkedHashMap<String, ArrayList<String>> mappaDidascalie;

    protected int numVoci = 0;


    protected boolean usaHeadNonScrivere;

    protected boolean usaHeadToc;

    protected boolean usaHeadTocIndice;

    protected boolean usaHeadRitorno; // prima del template di avviso

    protected boolean usaHeadInclude; // vero per Giorni ed Anni

    protected boolean usaHeadIncipit; // dopo il template di avviso

    protected boolean usaBodyDoppiaColonna;

    protected boolean usaBodyTemplate;

    protected boolean usaSuddivisioneParagrafi;

    //
//    protected boolean usaOrdineAlfabeticoParagrafi;
//
//    protected boolean usaBodySottopagine;
//
    protected boolean usaBodyRigheMultiple;

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
        usaHeadRitorno = true; //--normalmente false. Sovrascrivibile da preferenze
        usaHeadTemplateAvviso = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        tagHeadTemplateAvviso = "ListaBio"; //--Sovrascrivibile da preferenze
        tagHeadTemplateProgetto = "biografie"; //--Sovrascrivibile da preferenze
        usaHeadIncipit = false; //--normalmente false. Sovrascrivibile da preferenze

        // body
//        usaSuddivisioneParagrafi = false;
//        usaOrdineAlfabeticoParagrafi = false;
//        usaBodySottopagine = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        usaBodyRigheMultiple = true; //--normalmente true. Sovrascrivibile da preferenze
        usaBodyDoppiaColonna = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        usaBodyTemplate = true; //--normalmente false. Sovrascrivibile nelle sottoclassi
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
    private void elaboraPagina() {
        String summary = LibWiki.getSummary();
        testoPagina = VUOTA;

        if (lista == null || lista.size == 0) {
            log.info("La pagina: " + titoloPagina + " non aveva nessuna biografia");
            return;
        }// end of if cycle

        //header
        testoPagina += this.elaboraHead();

        //body
        //a capo, ma senza senza righe di separazione
        testoPagina += this.elaboraBody();

        //footer
        //di fila nella stessa riga, senza ritorno a capo (se inizia con <include>)
        testoPagina += this.elaboraFooter();
//        }// fine del blocco if

        //registra la pagina
        if (text.isValid(testoPagina)) {
            testoPagina = testoPagina.trim();

            if (pref.isBool(FlowCost.USA_DEBUG)) {
                testoPagina = titoloPagina + A_CAPO + testoPagina;
                titoloPagina = PAGINA_PROVA;
                appContext.getBean(AQueryWrite.class, titoloPagina, testoPagina);
            } else {
                if (checkPossoRegistrare(titoloPagina, testoPagina)) {
                    appContext.getBean(AQueryWrite.class, titoloPagina, testoPagina);
                    log.info("Registrata la pagina: " + titoloPagina);
                } else {
                    log.info("Non modificata la pagina: " + titoloPagina);
                }// end of if/else cycle
            }// end of if/else cycle
        }// fine del blocco if

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
    private String elaboraHead() {
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
            if (!titoloPaginaMadre.equals(VUOTA)) {
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
    private String elaboraTemplateAvviso() {
        String testo = VUOTA;
        String dataCorrente = date.get();
        String personeTxt = text.format(lista.size);

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
     * Corpo della pagina
     * Decide se c'è la doppia colonna
     * Controlla eventuali template di rinvio
     * Sovrascritto
     */
    protected String elaboraBody() {
        testoLista = lista.testo;
        numVoci = lista.size;
        int maxRigheColonne = 10;//@todo mettere la preferenza

        //aggiunge i tag per l'incolonnamento automatico del testo (proprietà mediawiki)
        if (usaBodyDoppiaColonna && (numVoci > maxRigheColonne)) {
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
        String testo = VUOTA;
        boolean nascosta = pref.isBool(FlowCost.USA_DEBUG);
        String cat;

        testo += LibWiki.setPortale(tagHeadTemplateProgetto);
        cat = tagCategoria;
        cat = nascosta ? LibWiki.setNowiki(cat) : cat;
        testo += cat;

        return testo;
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
