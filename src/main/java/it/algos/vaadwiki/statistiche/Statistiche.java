package it.algos.vaadwiki.statistiche;

import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.nome.NomeService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.upload.UploadService;
import it.algos.wiki.LibWiki;
import it.algos.wiki.web.AQueryWrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.didascalia.DidascaliaService.TITOLO_PAGINA_WIKI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sun, 09-Jun-2019
 * Time: 18:29
 * <p>
 * Classe specializzata per costruire statistiche e pagine di servizio del Progetto Bio. <br>
 * Didascalie <br>
 * <p>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 */
public abstract class Statistiche {

    protected final static String INIZIO_RIGA = "\n|-\n|";

    protected final static String SEP = "||";

    protected static String TAG_HEAD_TEMPLATE_AVVISO = "StatBio";

    protected static String PAGINA_PROVA = "Utente:Biobot/2";

    protected String titoloPagina;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct <br>
     */
    @Autowired
    protected ApplicationContext appContext;

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
    protected ATextService text;

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
    protected LibBio libBio;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected GiornoService giornoService;

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
    protected NomeService nomeService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected BioService bioService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AArrayService array;

    @Autowired
    protected AMongoService mongo;

    //--property
    protected String testoPagina;

    protected String templateCorrelate;

    /**
     * Costruisce la pagina <br>
     * Registra la pagina sul server wiki <br>
     */
    protected void inizia() {
        creaLista();
        elaboraPagina();
        registraPagina();
    }// end of method

    /**
     * Costruisce la pagina <br>
     * Registra la pagina sul server wiki <br>
     */
    protected void creaLista() {
    }// end of method


    /**
     * Elaborazione principale della pagina <br>
     * <p>
     * Costruisce head <br>
     * Costruisce body <br>
     * Costruisce footer <br>
     * Ogni blocco esce trimmato (inizio e fine) <br>
     * Gli spazi (righe) di separazione vanno aggiunti qui <br>
     * Registra la pagina <br>
     */
    protected void elaboraPagina() {
        testoPagina = VUOTA;

        //--preferenze
        fixPreferenze();

        //--header
        this.elaboraHead();

        //--body
        //--a capo, ma senza senza righe di separazione
        testoPagina += A_CAPO;
        this.elaboraBody();

        //--footer
        //--di fila nella stessa riga, senza ritorno a capo (se inizia con <include>)
        testoPagina += A_CAPO;
        this.elaboraFooter();
    }// fine del metodo


    /**
     * Preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        this.templateCorrelate = "BioCorrelate";
    }// fine del metodo


    /**
     * Costruisce il testo iniziale della pagina (header)
     * <p>
     * Non sovrascrivibile <br>
     * Ogni blocco esce trimmato (per l'inizio) e con un solo ritorno a capo per fine riga. <br>
     * Eventuali spazi gestiti da chi usa il metodo <br>
     */
    private void elaboraHead() {
        String testo = VUOTA;

        //--Posizione il template di avviso
        testo = elaboraTemplateAvviso();

        //--Ritorno ed avviso vanno (eventualmente) protetti con 'include'
        testo = elaboraInclude(testo);

        testoPagina += testo.trim();
    }// fine del metodo


    /**
     * Costruisce il template di avviso
     */
    private String elaboraTemplateAvviso() {
        String testo = VUOTA;
        String dataCorrente = date.get();

        testo += TAG_HEAD_TEMPLATE_AVVISO;
        testo += "|data=";
        testo += dataCorrente.trim();
        testo = LibWiki.setGraffe(testo);

        return testo;
    }// fine del metodo


    /**
     * Incorpora il testo iniziale nel tag 'include'
     */
    private String elaboraInclude(String testoIn) {
        String testoOut = testoIn;
        testoOut = LibBio.setNoIncludeRiga(testoIn);
        return testoOut;
    }// fine del metodo


    /**
     * Corpo della pagina <br>
     */
    protected void elaboraBody() {
    }// fine del metodo


    /**
     * Costruisce il testo finale della pagina
     */
    protected void elaboraFooter() {
        String testo = VUOTA;

        testo += LibWiki.setGraffe(templateCorrelate);
        testo += A_CAPO;
        testo += LibBio.setNoIncludeMultiRiga("[[Categoria:Progetto Biografie|{{PAGENAME}}]]");

        testoPagina += testo.trim();
    }// fine del metodo


    /**
     * Registra la pagina sul server wiki <br>
     */
    protected void registraPagina() {
        String titolo;

        if (text.isValid(testoPagina)) {
            testoPagina = testoPagina.trim();

            if (pref.isBool(USA_DEBUG)) {
                titolo = PAGINA_PROVA;
                testoPagina = titoloPagina + A_CAPO + testoPagina;
            } else {
                titolo = titoloPagina;
            }// fine del blocco if-else

            appContext.getBean(AQueryWrite.class, titolo, testoPagina);
        }// fine del blocco if
    }// end of method

}// end of class
