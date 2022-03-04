package it.algos.vaadwiki.backend.upload;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import static it.algos.vaadwiki.backend.application.WikiCost.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.login.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import javax.annotation.*;
import java.util.*;

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

    public final static String WIKI_TITLE_DEBUG = "Utente:Biobot/";

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
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public HtmlService html;

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
    public ALogService logger;

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
    protected UploadService uploadService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected WikiUtility wikiUtility;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ArrayService arrayService;


    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * La mappa viene creata nel @PostConstruct dell'istanza <br>
     */
    protected Map<String, List<String>> mappaUno;

    protected Map<String, Map<String, List<String>>> mappaDue;

    protected Map<String, LinkedHashMap<String, LinkedHashMap<String, List<String>>>> mappaTre;

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

    protected String wikiPaginaTitle;

    protected String wikiSottoTitolo;

    protected String newTextPagina;

    protected String summary;

    protected int numVoci;

    protected String notaDidascalie;

    protected String notaOrdinamento;

    protected String notaEsaustiva;

    protected String notaAttivita;

    protected String notaLinkAttivita;

    protected String notaAttivitaMultiple;

    protected String notaNazionalita;

    protected String notaSottoPagina;

    protected String notaCreazioneSottoPagina;

    protected String notaSuddivisione;

    protected String notaVuotoAttivita;

    protected String notaVuotoNazionalita;

    protected String notaTemplate;

    protected String tagCategoria;

    protected Attivita attivita;

    protected AETypePagina typePagina;

    protected List<WrapUploadSottoPagina> wrapSottopagine;

    protected Lista lista;

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
    public void inizia() {
        summary = text.setDoppieQuadre("Utente:" + botLogin.getUsername() + "|" + botLogin.getUsername());
        this.regolazioniIniziali();
        this.fixPreferenze();
        this.fixMappa();
        this.fixPagina();
    }


    /**
     * Registra la pagina che è già stata elaborata in @PostConstruct.inizia() <br>
     */
    public AIResult upload() {
        AIResult result = null;
        Attivita attivita;
        String subTitle;
        String subParagrafo;
        Map<String, List<String>> mappaUno;

        //--pagina principale
        if (text.isValid(newTextPagina)) {
            result = appContext.getBean(QueryWrite.class).urlRequest(wikiPaginaTitle, newTextPagina, summary);
        }

        //--sottopagine
        if (wrapSottopagine != null && wrapSottopagine.size() > 0) {
            for (WrapUploadSottoPagina wrap : wrapSottopagine) {
                attivita = wrap.getAttivita();
                subTitle = wrap.getWikiPaginaTitle();
                subParagrafo = wrap.getWikiSottoTitolo();
                subTitle += SLASH + subParagrafo;
                mappaUno = wrap.getMappaUno();
                appContext.getBean(UploadAttivita.class, attivita, AETypePagina.sottoPagina, subTitle, subParagrafo, mappaUno).upload();
            }
        }

        return result;
    }

    /**
     * Regolazioni iniziali per gestire i due costruttori:attività plurali o attività singola <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniIniziali() {
    }

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        taglioSottoPagina = 50; //@todo preferenze
        String unitaBio = wikiUtility.bold(taglioSottoPagina + " unità");

        // head
        usaHeadNonScrivere = true;
        usaHeadRitorno = switch (typePagina) {
            case paginaPrincipale -> false;
            case sottoPagina -> true;
            default -> false;
        };
        usaHeadInclude = true;
        typeHeadToc = AETypeHeadToc.conIndice;
        usaHeadTemplateAvviso = true;
        tagHeadTemplateAvviso = "ListaBio"; //--Sovrascrivibile da preferenze
        tagHeadTemplateProgetto = "biografie"; //--Sovrascrivibile da preferenze
        usaHeadIncipit = true; //--normalmente true. Sovrascrivibile da preferenze

        // body

        // footer
        usaNote = true; //--normalmente true. Sovrascrivibile nelle sottoclassi
        usaVociCorrelate = true; //--normalmente false. Sovrascrivibile nelle sottoclassi

        // note <ref>
        String progetto = wikiUtility.bold("[[Progetto:Biografie/Didascalie|progetto biografie]]");
        String cognome = wikiUtility.bold("cognome");
        String titolo = wikiUtility.bold("titolo");
        String previsteAtt = wikiUtility.bold("[[Discussioni progetto:Biografie/Attività|convenzionalmente previste]]");
        String elencoAtt = wikiUtility.bold("[[Modulo:Bio/Plurale attività|inserite nell'elenco]]");
        String previsteNaz = wikiUtility.bold("[[Discussioni progetto:Biografie/Nazionalità|convenzionalmente previste]]");
        String elencoNaz = wikiUtility.bold("[[Modulo:Bio/Plurale nazionalità|inserite nell'elenco]]");
        String non = wikiUtility.bold("non");
        String nazionalita = wikiUtility.bold("''nazionalità''");
        String bot = html.verde("'''bot'''");
        String altre = wikiUtility.bold(ALTRE);

        notaTemplate = wikiUtility.bold("[[template:Bio|template Bio]]");
        notaDidascalie = String.format("Le didascalie delle voci sono quelle previste nel %s", progetto);
        notaOrdinamento = String.format("Le voci, all'interno di ogni paragrafo, sono in ordine alfabetico per %s; se questo manca si utilizza il %s della pagina.", cognome, titolo);
        notaEsaustiva = String.format("La lista non è esaustiva e contiene solo le persone che sono citate nell'enciclopedia e per le quali è stato implementato correttamente il %s", notaTemplate);
        notaSottoPagina = String.format("Questa sottopagina specifica viene creata se il numero di voci biografiche nel paragrafo della pagina principale supera le %s.", unitaBio);
        //        notaAttivita = "Le attività sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale attività|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
        notaAttivita = String.format("Le attività sono quelle %s dalla comunità ed %s utilizzato dal %s", previsteAtt, elencoAtt, notaTemplate);
        notaNazionalita = String.format("Le nazionalità sono quelle %s dalla comunità ed %s utilizzato dal %s", previsteNaz, elencoNaz, notaTemplate);
        notaVuotoAttivita = String.format("Nel paragrafo %s (eventuale) vengono raggruppate quelle voci biografiche che %s usano il parametro %s oppure che usano una nazionalità di difficile elaborazione da parte del %s", altre, non, nazionalita, bot);
    }


    /**
     * Costruisce l'istanza associata di ListaXxx <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void fixMappa() {
        this.mappaUno = this.mappaUno != null ? mappaUno : new LinkedHashMap<>();
        this.mappaDue = this.mappaDue != null ? mappaDue : new LinkedHashMap<>();
        this.mappaTre = this.mappaTre != null ? mappaTre : new LinkedHashMap<>();
        this.wrapSottopagine = new ArrayList<>();
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
        String testoLista;
        testoLista = this.getTestoConParagrafi();

        if (true) { //todo necessita preferenza
            testoLista = fixSottoPagine(testoLista);
        }

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
     * Estrae dalla mappa un testo completo <br>
     * Comprende titoli dei paragrafi e la lista delle didascalie <br>
     * Può essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    public String getTestoConParagrafi() {
        StringBuilder buffer = new StringBuilder();
        List<String> didascalie;
        int numSottoPagina = 50;//@todo preferenza
        WrapUploadSottoPagina wrap;
        Map<String, List<String>> subMappa;

        try {
            if (mappaUno != null && mappaUno.size() > 0) {
                for (String key : mappaUno.keySet()) {
                    didascalie = mappaUno.get(key);

                    if (didascalie != null && didascalie.size() > numSottoPagina) {
                        buffer.append(this.getTitoloParagrafo(key, didascalie.size()));//@todo creare preferenza per il numero
                        buffer.append(String.format("%s%s%s%s%s", VEDI, wikiPaginaTitle, SLASH, key, DOPPIE_GRAFFE_END));
                        buffer.append(A_CAPO);
                        buffer.append(A_CAPO);

                        //--@todo in futuro prevedere anche il livello 3 - cioe anche la sottopagina può avere a sua volta un'altra sottopagina
                        if (wrapSottopagine != null) {
                            subMappa = lista.getMappaDue().get(key);
                            wrap = new WrapUploadSottoPagina(attivita, wikiPaginaTitle, key, subMappa);
                            wrapSottopagine.add(wrap);
                        }
                    }
                    else {
                        buffer.append(this.getTitoloParagrafo(key, didascalie.size()));//@todo creare preferenza per il numero

                        for (String stringa : didascalie) {
                            buffer.append(ASTERISCO);
                            buffer.append(SPAZIO);
                            buffer.append(stringa);
                            buffer.append(A_CAPO);
                        }
                        buffer.append(A_CAPO);
                    }
                }
            }
        } catch (Exception unErrore) {
            logger.warn(unErrore, this.getClass(), "nomeDelMetodo");
        }

        return buffer.toString().trim();
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
            testo += usaVociCorrelate();
        }

        testo += text.setDoppieGraffe(String.format("%s%s", PORTALE, "biografie"));
        testo += A_CAPO;
        testo += tagCategoria;
        //        cat = nascosta ? LibWiki.setNowiki(cat) : cat;
        //        testo += cat;

        return testo;
    }


    /**
     * Inserisce nel testo i rinvii alle sottoPagine <br>
     * Elabora gli upload delle sottoPagine <br>
     * <p>
     * Sovrascritto <br>
     */
    protected String fixSottoPagine(final String testoIn) {
        return testoIn;
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
        String paginaMadre;

        if (usaHeadRitorno) {
            paginaMadre = wikiPaginaTitle.substring(0, wikiPaginaTitle.lastIndexOf(SLASH));
            if (text.isValid(paginaMadre)) {
                testo += "Torna a|" + paginaMadre;
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
        String personeTxt = text.format(getNumVoci());

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
     * Paragrafo delle voci correlate (eventuale)
     */
    protected String usaVociCorrelate() {
        StringBuilder testo = new StringBuilder(VUOTA);
        String titolo = "Voci correlate";
        List<String> lista = listaVociCorrelate();

        if (lista != null && lista.size() > 0) {
            testo.append(wikiUtility.setParagrafo(titolo));
            testo.append(A_CAPO);
            for (String riga : lista) {
                testo.append(ASTERISCO);
                testo.append(text.setDoppieQuadre(riga));
                testo.append(A_CAPO);
            }
            testo.append(A_CAPO);
        }

        return testo.toString();
    }


    /**
     * Lista delle voci correlate (eventuale) <br>
     * DEVE essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected List<String> listaVociCorrelate() {
        return null;
    }

    public String getTitoloParagrafo(final String keyParagrafo, int numero) {
        String keyMaiuscola = text.primaMaiuscola(keyParagrafo);
        String tag = "Progetto:Biografie/Nazionalità/";
        String titolo;

        titolo = switch (typePagina) {
            case paginaPrincipale -> {
                tag += keyMaiuscola;
                tag += PIPE;
                tag += keyMaiuscola;
                tag = text.setDoppieQuadre(tag);
                yield keyMaiuscola.equals(ALTRE) ? ALTRE : tag;
            }
            case sottoPagina -> keyMaiuscola;
            default -> VUOTA;
        };

        //        tag += keyMaiuscola;
        //        tag += PIPE;
        //        tag += keyMaiuscola;
        //        tag = text.setDoppieQuadre(tag);
        //
        //        titolo = keyMaiuscola.equals(ALTRE) ? ALTRE : tag;
        return wikiUtility.setParagrafo(titolo, numero);
    }

    /**
     * Elabora la pagina <br>
     * Registra la pagina <br>
     */
    @Deprecated
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
        return switch (typePagina) {
            case paginaPrincipale -> numVoci;
            case sottoPagina -> arrayService.dimMappa(mappaUno);
            default -> 0;
        };
    }

    public Map<String, List<String>> getMappaUno() {
        return mappaUno;
    }

    public Map<String, Map<String, List<String>>> getMappaDue() {
        return mappaDue;
    }

    public Map<String, LinkedHashMap<String, LinkedHashMap<String, List<String>>>> getMappaTre() {
        return mappaTre;
    }

    public String getTestoPagina() {
        return newTextPagina != null ? newTextPagina : VUOTA;
    }

    public String getWikiPaginaTitle() {
        return wikiPaginaTitle;
    }

    public void setWikiPaginaTitle(String wikiPaginaTitle) {
        this.wikiPaginaTitle = wikiPaginaTitle;
    }

    public enum AETypeHeadToc {
        nessuno, conIndice, senzaIndice
    }


}
