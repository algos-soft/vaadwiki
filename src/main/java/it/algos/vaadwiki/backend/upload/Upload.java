package it.algos.vaadwiki.backend.upload;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.login.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.query.*;
import static it.algos.vaadwiki.wiki.query.QueryWrite.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import javax.annotation.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-gen-2022
 * Time: 21:25
 * <p>
 * Classe specializzata per caricare (upload) le liste biografiche sul server wiki. <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Necessita del login come bot <br>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 * <p>
 * La (List<Bio>) listaBio, la (List<String>) listaDidascalie, la (Map<String, List>) mappa e (String) testoConParagrafi
 * vengono tutte regolate alla creazione dell'istanza in @PostConstruct e sono disponibili da subito <br>
 * Si usa SOLO la chiamata appContext.getBean(UploadXxx.class, yyy) per caricare l'istanza ListaXxx.class <br>
 * L'effettivo upload su wiki avviene SOLO con uploadPagina() o uploadPaginaTest() <br>
 */
public abstract class Upload {

    public final static String TAG_INDICE = "__FORCETOC__";

    public final static String TAG_NO_INDICE = "__NOTOC__";

    protected final static String TAG_NON_SCRIVERE = "<!-- NON MODIFICATE DIRETTAMENTE QUESTA PAGINA - GRAZIE -->";

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DateService date;

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BotLogin botLogin;

    public int taglioSottoPagina;

    //    protected boolean usaWikiPaginaTest = true;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected PreferenzaService pref;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected WikiUtility wikiUtility;

    //    protected AEntity entityBean;
    protected boolean usaHeadNonScrivere;

    protected boolean usaHeadRitorno; // prima del template di avviso

    protected boolean usaHeadInclude;

    protected boolean usaHeadTemplateAvviso;

    protected boolean usaHeadIncipit; // dopo il template di avviso

    protected boolean usaNote;

    protected boolean usaVociCorrelate;

    protected String tagHeadTemplateAvviso;

    protected String tagHeadTemplateProgetto;

    protected AETypeHeadToc typeHeadToc;

    protected String wikiPaginaTitle = WIKI_TITLE_DEBUG;

    protected String newTextPagina;

    protected String summary;

    protected Lista lista;

    protected int numVoci;

    protected String notaDidascalie;

    protected String notaOrdinamento;

    protected String notaEsaustiva;

    protected String notaAttivita;

    protected String notaNazionalita;

    protected String notaSottoPagina;

    /**
     * Performing the initialization in a constructor is not suggested <br>
     * as the state of the UI is not properly set up when the constructor is invoked. <br>
     * <p>
     * La injection viene fatta da SpringBoot solo alla fine del metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * L'istanza può essere creata con  appContext.getBean(xxxClass.class);  oppure con @Autowired <br>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void inizia() {
        summary = text.setDoppieQuadre("Utente:" + botLogin.getUsername() + "|" + botLogin.getUsername());
        this.fixPreferenze();
        this.regolazioniIniziali();
        this.fixLista();
        this.fixVoci();
        this.fixPagina();

    }

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        // head
        usaHeadNonScrivere = true;
        usaHeadRitorno = true;
        usaHeadInclude = true;
        typeHeadToc = AETypeHeadToc.conIndice;
        usaHeadTemplateAvviso = true;
        tagHeadTemplateAvviso = "ListaBio"; //--Sovrascrivibile da preferenze
        tagHeadTemplateProgetto = "biografie"; //--Sovrascrivibile da preferenze
        usaHeadIncipit = true; //--normalmente true. Sovrascrivibile da preferenze

        // body

        // footer
        usaNote = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        usaVociCorrelate = false; //--normalmente false. Sovrascrivibile nelle sottoclassi

        // note <ref>
        notaDidascalie = "Le didascalie delle voci sono quelle previste nel [[Progetto:Biografie/Didascalie|progetto biografie]]";
        notaOrdinamento = "Le voci, all'interno di ogni paragrafo, sono in ordine alfabetico per " + "cognome" + "; se questo manca si utilizza il " + "titolo" + " della pagina.";
        notaEsaustiva = "La lista non è esaustiva e contiene solo le persone che sono citate nell'enciclopedia e per le quali è stato implementato correttamente il [[template:Bio|template Bio]]";
        notaSottoPagina = "Questa sottoPagina specifica viene creata se il numero di voci biografiche nel paragrafo della pagina principale supera le " + taglioSottoPagina + " unità.";
        notaAttivita = "Le attività sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale attività|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
        notaNazionalita = "Le nazionalità sono quelle [[Discussioni progetto:Biografie/Nazionalità|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale nazionalità|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";


        String didascalia = text.setRef(notaDidascalie);
        String ordinamento = text.setRef(notaOrdinamento);
        String esaustiva = text.setRef(notaEsaustiva);
//        String creazione = text.setRef(notaCreazione);
        String attivita = text.setRef(notaAttivita);
//        String suddivisione = text.setRef(notaSuddivisione);
        String nazionalita = text.setRef(notaNazionalita);
//        String paragrafo = text.setRef(notaParagrafoVuoto);
    }


    /**
     * Regolazioni iniziali per gestire i due costruttori:attività plurali o attività singola <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniIniziali() {
    }

    /**
     * Costruisce l'istanza associata di ListaXxx <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void fixLista() {
    }

    /**
     * Calcola il numero di voci della pagina <br>
     */
    private void fixVoci() {
        numVoci = lista != null ? lista.getNumDidascalie() : 0;
    }

    /**
     * Elaborazione principale della pagina
     * <p>
     * Costruisce head <br>
     * Costruisce body <br>
     * Costruisce footer <br>
     * Ogni blocco esce trimmato (inizio e fine) <br>
     * Gli spazi (righe) di separazione vanno aggiunti qui <br>
     */
    protected void fixPagina() {
        //header
        newTextPagina = this.elaboraHead();

        //body
        newTextPagina += this.elaboraBody();

        //footer
        //di fila nella stessa riga, senza ritorno a capo (se inizia con <include>)
        newTextPagina += A_CAPO;
        newTextPagina += this.elaboraFooter();
    }

    /**
     * Costruisce il testo iniziale della pagina (header)
     * <p>
     * Non sovrascrivibile <br>
     * Posiziona il TOC <br>
     * Posiziona il ritorno (eventuale) <br>
     * Posizione il template di avviso <br>
     * Posiziona l'incipit della pagina (eventuale) <br>
     * Ritorno e avviso vanno (eventualmente) protetti con 'include' <br>
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

        // Scrittura, toc, ritorno, ed avviso vanno (eventualmente) protetti con 'include'
        testo += elaboraInclude(testoIncluso);

        // Posiziona l'incipit della pagina
        testo += A_CAPO;
        testo += elaboraIncipit();
        testo += A_CAPO;

        // valore di ritorno
        return testo;
    }

    /**
     * Corpo della pagina
     * Decide se c'è la doppia colonna
     * Controlla eventuali template di rinvio
     * Sovrascritto
     */
    protected String elaboraBody() {
        String testoLista = VUOTA;
        testoLista = lista.getTestoConParagrafi();

        //aggiunge i tag per l'incolonnamento automatico del testo (proprietà mediawiki)
        //        if (usaBodyDoppiaColonna && (numVoci > pref.getInt(MAX_RIGHE_COLONNE))) {
        //            testoLista = LibWiki.setColonne(testoLista);
        //        }

        //        if (usaBodyTemplate) {
        //            if (!pref.isBool(USA_DEBUG)) {
        //                testoLista = elaboraTemplate(testoLista);
        //            }
        //        }

        return testoLista;
    }


    /**
     * Piede della pagina
     * Sovrascritto
     */
    protected String elaboraFooter() {
        String testo = A_CAPO;
        //        boolean nascosta = pref.isBool(FlowCost.USA_DEBUG);
        String cat;

        if (usaNote) {
            testo += usaNote();
        }

        if (usaVociCorrelate) {
            //                    testo += usaVociCorrelate();
        }

        //        testo += LibWiki.setPortale(tagHeadTemplateProgetto);
        //        cat = tagCategoria;
        //        cat = nascosta ? LibWiki.setNowiki(cat) : cat;
        //        testo += cat;

        return testo;
    }


    /**
     * Avviso visibile solo in modifica <br>
     * <p>
     * Non sovrascrivibile <br>
     */
    private String elaboraAvvisoScrittura() {
        String testo = VUOTA;

        if (usaHeadNonScrivere) {
            testo += TAG_NON_SCRIVERE;
            testo += A_CAPO;
        }

        return testo;
    }

    /**
     * Costruisce il TOC (tavola contenuti) <br>
     * <p>
     * Non sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo di una delle tre possibilità <br>
     */
    private String elaboraTOC() {
        return switch (typeHeadToc) {
            case nessuno:
                yield VUOTA;
            case senzaIndice:
                yield TAG_NO_INDICE;
            case conIndice:
                yield TAG_INDICE;
        };
    }

    /**
     * Costruisce il ritorno alla pagina 'madre' <br>
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
                testo = text.setDoppieGraffe(testo);
            }
        }

        return testo;
    }

    /**
     * Titolo della pagina 'madre' <br>
     * <p>
     * DEVE essere sovrascritto, SENZA invocare prima il metodo della superclasse <br>
     */
    protected String getTitoloPaginaMadre() {
        return VUOTA;
    }


    /**
     * Costruisce il template di avviso <br>
     * <p>
     * Non sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) il nome del template da usare <br>
     */
    protected String elaboraTemplateAvviso() {
        String testo = VUOTA;
        String dataCorrente = date.get();
        String personeTxt = VUOTA;
        personeTxt = text.format(numVoci);

        if (usaHeadTemplateAvviso) {
            testo += tagHeadTemplateAvviso;
            testo += "|bio=";
            testo += personeTxt;
            testo += "|data=";
            testo += dataCorrente.trim();
            testo += "|progetto=";
            testo += tagHeadTemplateProgetto;
            testo = text.setDoppieGraffe(testo);
        }

        return testo;
    }

    /**
     * Incorpora il testo iniziale nel tag 'include'
     * <p>
     * Non sovrascrivibile <br>
     * Tipicamente sempre true. Si attiva solo se c'è del testo (iniziale) da includere
     */
    private String elaboraInclude(final String testoIn) {
        String testoOut = testoIn;
        String tagIni = "<noinclude>";
        String tagEnd = "</noinclude>";

        if (usaHeadInclude) {
            testoOut = tagIni;
            testoOut += testoIn;
            testoOut += tagEnd;
        }

        return testoOut;
    }


    /**
     * Costruisce la frase di incipit iniziale <br>
     * <p>
     * Non sovrascrivibile <br>
     */
    private String elaboraIncipit() {
        String testo = VUOTA;

        if (usaHeadIncipit) {
            testo += elaboraIncipitSpecifico();
            testo += A_CAPO;
        }

        return testo;
    }

    /**
     * Costruisce la frase di incipit iniziale <br>
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecifico() {
        return VUOTA;
    }


    /**
     * Paragrafo delle note (eventuale)
     * Sovrascritto
     */
    protected String usaNote() {
        String testo = VUOTA;
        String tag = "Note";
        String ref = "<references/>";

        testo += wikiUtility.setParagrafo(tag);
        testo += ref;
        testo += A_CAPO;

        return newTextPagina.contains(REF_OPEN) ? testo : VUOTA;
    }

    /**
     * Elaborazione la pagina <br>
     * Registra la pagina <br>
     */
    public AIResult uploadTest() {
        AIResult result = null;

        if (text.isValid(newTextPagina)) {
            result = appContext.getBean(QueryWrite.class).urlRequest(wikiPaginaTitle, newTextPagina, summary);
        }

        return result;
    }

    /**
     * Elaborazione la pagina <br>
     * Registra la pagina <br>
     */
    public AIResult uploadPagina() {
        AIResult result = null;

        if (text.isValid(newTextPagina)) {
            result = appContext.getBean(QueryWrite.class).urlRequest(wikiPaginaTitle, newTextPagina, summary);
        }

        return result;
    }


    protected String getSummary() {
        return summary;
    }


    public int getNumVoci() {
        return numVoci;
    }

    public String getTestoConParagrafi() {
        return lista != null ? lista.getTestoConParagrafi() : VUOTA;
    }

    public String getTestoPagina() {
        return newTextPagina != null ? newTextPagina : VUOTA;
    }

    public enum AETypeHeadToc {
        nessuno, conIndice, senzaIndice
    }


}
