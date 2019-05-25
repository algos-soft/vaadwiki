package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.mese.MeseService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.service.*;
import it.algos.vaadwiki.download.*;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.Api;
import it.algos.wiki.LibWiki;
import it.algos.wiki.web.AQueryWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import java.util.*;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.didascalia.Didascalia.TAG_SEP;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 17-gen-2019
 * Time: 17:45
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier("aaa")
@Slf4j
public abstract class Upload {

    public final static String PAGINA_PROVA = "Utente:Biobot/2";

    protected final static String TAG_NON_SCRIVERE = "<!-- NON MODIFICATE DIRETTAMENTE QUESTA PAGINA - GRAZIE -->";

    protected final static String TAG_INDICE = "__FORCETOC__";

    protected final static String TAG_NO_INDICE = "__NOTOC__";

    @Autowired
    protected ApplicationContext appContext;

//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    protected WikiLoginOld wikiLoginOld;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AttivitaService attivitaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected NazionalitaService nazionalitaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ProfessioneService professioneService;

//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    protected CategoriaService categoriaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected BioService bioService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected NewService newService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected DeleteService deleteService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected UpdateService updateService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ElaboraService elaboraService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PageService pageService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AArrayService array;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ADateService date;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected Api api;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMongoService mongo;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mail;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected LogService logger;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected MeseService mese;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AnnoService anno;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected SecoloService secolo;

    protected boolean usaHeadTemplateAvviso; // uso del template StatBio

    protected String tagHeadTemplateAvviso; // template 'StatBio'

    protected String tagHeadTemplateProgetto;

    protected LinkedHashMap<String, ArrayList<String>> mappaDidascalie;

    protected int numPersone = 0;

    protected String titoloPagina;

    protected boolean usaHeadNonScrivere;

    protected boolean usaHeadToc;

    protected boolean usaHeadTocIndice;

    protected boolean usaHeadRitorno; // prima del template di avviso

    protected boolean usaHeadInclude; // vero per Giorni ed Anni

    protected boolean usaHeadIncipit; // dopo il template di avviso

    protected boolean usaBodyDoppiaColonna;

    protected boolean usaBodyTemplate;

    protected boolean usaSuddivisioneParagrafi;

    protected boolean usaOrdineAlfabeticoParagrafi;

    protected boolean usaBodySottopagine;

    protected boolean usaBodyRigheMultiple;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected UploadService uploadService;

//    protected String titoloPaginaMadre = "";


    /**
     * Costruttore completo
     */
    public Upload() {
    }// end of constructor


    /**
     * Esegue un ciclo di creazione (UPLOAD) delle liste
     */
    public void esegue() {
        elaboraParametri();
        elaboraTitolo();
        creaMappaDidascalie();
        elaboraPagina();
    }// end of method


    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    protected void elaboraParametri() {
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
        usaSuddivisioneParagrafi = false;
        usaOrdineAlfabeticoParagrafi = false;
        usaBodySottopagine = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        usaBodyRigheMultiple = true; //--normalmente false. Sovrascrivibile da preferenze
        usaBodyDoppiaColonna = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        usaBodyTemplate = true; //--normalmente false. Sovrascrivibile nelle sottoclassi
    }// fine del metodo


    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    protected void elaboraTitolo() {
    }// fine del metodo


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Sovrascritto nella sottoclasse concreta <br>
     * DOPO invoca il metodo della superclasse per calcolare la dimensione della mappa <br>
     */
    protected void creaMappaDidascalie() {
        int num = 0;

        if (mappaDidascalie != null) {
            for (Map.Entry<String, ArrayList<String>> entry : mappaDidascalie.entrySet()) {
                num += entry.getValue().size();
            }// end of for cycle
        }// end of if cycle

        numPersone = num;
    }// fine del metodo


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
        String testo = VUOTA;
        String titolo;

        if (numPersone > 0) {
            //header
            testo += this.elaboraHead();

            //body
            //a capo, ma senza senza righe di separazione
            testo += this.elaboraBody();

            //footer
            //di fila nella stessa riga, senza ritorno a capo (se inizia con <include>)
            testo += this.elaboraFooter();
        }// fine del blocco if

        //registra la pagina
        if (!testo.equals(VUOTA)) {
            testo = testo.trim();

            if (pref.isBool(FlowCost.USA_DEBUG)) {
                titoloPagina = PAGINA_PROVA;
            }// fine del blocco if

            if (pref.isBool(FlowCost.USA_DEBUG) || checkPossoRegistrare(titoloPagina, testo)) {
                appContext.getBean(AQueryWrite.class, titoloPagina, testo);
                log.info(titoloPagina);
            }// end of if cycle
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
        String personeTxt = text.format(numPersone);

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
        String testo = VUOTA;
        int maxRigheColonne = 10;//@todo mettere la preferenza

        if (mappaDidascalie != null && mappaDidascalie.size() > 0) {
            if (usaSuddivisioneParagrafi) {
                testo = righeParagrafo();
            } else {
                if (usaBodyRigheMultiple) {
                    testo = righeRaggruppate();
                } else {
                    testo = righeSemplici();
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if cycle

        //aggiunge i tag per l'incolonnamento automatico del testo (proprietà mediawiki)
        if (usaBodyDoppiaColonna && (numPersone > maxRigheColonne)) {
            testo = LibWiki.setColonne(testo);
        }// fine del blocco if

        if (usaBodyTemplate) {
//            if (Pref.getBool(CostBio.USA_DEBUG, false)) {
//                text = elaboraTemplate("") + text;
//            } else {
//                text = elaboraTemplate(text);
//            }// end of if/else cycle
            if (!pref.isBool(USA_DEBUG)) {
                testo = elaboraTemplate(testo);
            }// end of if cycle
        }// end of if cycle

        return testo;
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
     * Costruisce il paragrafo
     * Sovrascrivibile
     */
    protected String righeParagrafo() {
        String testo = VUOTA;
        int numVociParagrafo;
        HashMap<String, Object> mappa;
        String titoloParagrafo;
        String titoloSottopagina;
        String paginaLinkata;
        String titoloVisibile;
        List<Bio> lista;

//        for (Map.Entry<String, HashMap> mappaTmp : mappaBio.entrySet()) {
//            testo += CostBio.A_CAPO;
//
//            mappa = mappaTmp.getValue();
//
//            if (usaOrdineAlfabeticoParagrafi) {
//                titoloParagrafo = (String) mappa.get(KEY_MAP_PARAGRAFO_TITOLO);
//            } else {
//                titoloParagrafo = (String) mappa.get(KEY_MAP_PARAGRAFO_LINK);
//            }// end of if/else cycle
//
//            titoloVisibile = (String) mappa.get(KEY_MAP_PARAGRAFO_TITOLO);
//            lista = (List<Bio>) mappa.get(KEY_MAP_LISTA);
//            numVociParagrafo = lista.size();
//
////            titoloParagrafo = costruisceTitolo(paginaLinkata, titoloVisibile);
//            if (Pref.getBool(CostBio.USA_NUMERI_PARAGRAFO, false)) {
//                testo += LibWiki.setParagrafo(titoloParagrafo, numVociParagrafo);
//            } else {
//                testo += LibWiki.setParagrafo(titoloParagrafo);
//            }// end of if/else cycle
//
//            testo += CostBio.A_CAPO;
//
//            if (usaSottopagine && numVociParagrafo > maxVociParagrafo) {
//                titoloSottopagina = titoloPagina + "/" + titoloVisibile;
//                testo += "{{Vedi anche|" + titoloSottopagina + "}}";
//                creaSottopagina(mappa);
//            } else {
//                for (Bio bio : lista) {
//                    testo += CostBio.ASTERISCO;
//                    testo += bio.getDidascaliaListe();
//                    testo += CostBio.A_CAPO;
//                }// end of for cycle
//            }// end of if/else cycle
//
//        }// end of for cycle

        return testo;
    }// fine del metodo


    /**
     * Raggruppa le biografie
     */
    protected String righeRaggruppate() {
        String testo = VUOTA;
        ArrayList<String> listaDidascalie;

        if (mappaDidascalie != null) {
            for (String key : mappaDidascalie.keySet()) {
                listaDidascalie = mappaDidascalie.get(key);

                if (listaDidascalie.size() == 1) {
                    testo += ASTERISCO + (text.isValid(key) ? key + TAG_SEP : SPAZIO) + listaDidascalie.get(0) + A_CAPO;
                } else {
                    if (text.isValid(key)) {
                        testo += ASTERISCO + key + A_CAPO;
                        for (String didascalia : listaDidascalie) {
                            testo += ASTERISCO + ASTERISCO + didascalia + A_CAPO;
                        }// end of if/else cycle
                    } else {
                        for (String didascalia : listaDidascalie) {
                            testo += ASTERISCO + didascalia + A_CAPO;
                        }// end of if/else cycle
                    }// end of if/else cycle
                }// end of for cycle
            }// end of for cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Nessun raggruppamento
     */
    protected String righeSemplici() {
        String testo = VUOTA;
        ArrayList<String> listaDidascalie;

        if (mappaDidascalie != null) {
            for (String key : mappaDidascalie.keySet()) {
                listaDidascalie = mappaDidascalie.get(key);

                if (text.isValid(key)) {
                    for (String didascalia : listaDidascalie) {
                        testo += ASTERISCO + key + TAG_SEP + didascalia + A_CAPO;
                    }// end of for cycle
                } else {
                    for (String didascalia : listaDidascalie) {
                        testo += ASTERISCO + didascalia + A_CAPO;
                    }// end of for cycle
                }// end of if/else cycle

            }// end of for cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Piede della pagina
     * Sovrascritto
     */
    protected String elaboraFooter() {
        return VUOTA;
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

}// end of class
