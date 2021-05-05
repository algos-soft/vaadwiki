package it.algos.vaadflow14.backend.logic;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.*;
import de.codecamp.vaadin.components.messagedialog.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.ui.button.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.form.*;
import it.algos.vaadflow14.ui.header.*;
import it.algos.vaadflow14.ui.list.*;
import it.algos.vaadflow14.ui.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;
import org.vaadin.haijian.*;

import javax.annotation.*;
import java.lang.reflect.Field;
import java.time.*;
import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: gio, 30-apr-2020
 * Time: 07:46
 * Classe astratta di gestione della 'business logic' di una Entity e di un package <br>
 * Collegamento tra il 'backend' e le views <br>
 * Le sottoclassi concrete sono SCOPE_PROTOTYPE e mantengono lo stato del package <br>
 * Viene creata un istanza per ogni view. <br>
 * Quindi la ViewList (con la Grid) e la ViewForm ( con il Form), hanno istanze diverse della sottoclasse xxxLogic <br>
 * <p>
 * Questo 'service' garantisce i metodi di collegamento per accedere al database <br>
 * Implementa le API dell' interfaccia  <br>
 * Classe astratta. L' implementazione concreta standard è GenericLogic <br>
 * Contiene i riferimenti ad altre classi per usarli nelle sottoclassi concrete <br>
 * I riferimenti sono 'public' per poterli usare con TestUnit <br>
 * <p>
 * Le sottoclassi concrete vengono create in AView.fixEntityLogic() <br>
 * col metodo logic = (AILogic) appContext.getBean(Class.forName(canonicalName)); <br>
 * sono pertanto SCOPE_PROTOTYPE e ne viene creata un' istanza diversa per ogni view <br>
 * possono quindi mantenere delle property senza possibilità che si 'mischino' con altri utenti di altri browser <br>
 */
public abstract class ALogicOld implements AILogicOld {

    protected static final String BLOCCATA = "Collezione bloccata. Non si può ne creare, ne modificare, ne cancellare la singola entity.";

    private static final int WIDTH = 10;

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ATextService text;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AArrayService array;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AReflectionService reflection;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AAnnotationService annotation;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ADateService date;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ARouteService route;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AWikiService wiki;


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
    public AMongoService mongo;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ABeanService beanService;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AFileService fileService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AFieldService fieldService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AVaadinService vaadinService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AImageService imageService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ADataProviderService dataService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AHtmlService html;


    /**
     * Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true. <br>
     */
    public boolean usaBottoneEdit;

    /**
     * The Entity Class  (obbligatoria sempre; in ViewForm può essere ricavata dalla entityBean)
     */
    protected Class<? extends AEntity> entityClazz;

    /**
     * The Entity Bean  (obbligatoria per ViewForm)
     */
    protected AEntity entityBean;

    /**
     * The Grid  (obbligatoria per ViewList)
     */
    protected AGrid grid;

    /**
     * The Form Class  (obbligatoria per costruire la currentForm)
     */
    protected Class<? extends AForm> formClazz;

    /**
     * The Form (obbligatoria nel ViewForm)
     */
    protected AForm currentForm;

    /**
     * Tipologia di Form in uso <br>
     */
    protected AEOperation operationForm;

    /**
     * Flag di preferenza per specificare la property della entity da usare come ID <br>
     */
    protected String keyPropertyName;

    /**
     * Flag di preferenza per selezionare la ricerca testuale: <br>
     * 1) nessuna <br>
     * 2) campo editText di selezione per una property specificata in searchProperty <br>
     * 3) bottone che apre un dialogo di selezione <br>
     */
    protected AESearch searchType;

    /**
     * Flag di preferenza per specificare la property della entity su cui effettuare la ricerca <br>
     * Ha senso solo se searchType=EASearch.editField
     */
    protected String searchProperty;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneDelete;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneResetList;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default true. <br>
     */
    protected boolean usaBottoneNew;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneSearch;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneExport;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottonePaginaWiki;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneUpdate;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneUpload;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneDownload;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneElabora;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneCheck;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneModulo;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneTest;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneStatistiche;


    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneResetForm;

    /**
     * Flag di preferenza per l' utilizzo dei bottoni di spostamento. Di default false. <br>
     */
    protected boolean usaBottoniSpostamentoForm;

    /**
     * Flag di preferenza per specificare il titolo della pagina wiki da mostrare in lettura <br>
     */
    protected String wikiPageTitle;

    /**
     * Flag di preferenza per specificare il massimo numero di bottoni della prima riga <br>
     */
    protected int maxNumeroBottoniPrimaRiga;

    /**
     * Flag di preferenza per i messaggi di avviso in alertPlacehorder <br>
     * Si può usare la classe AHeaderWrap con i messaggi suddivisi per ruolo (user, admin, developer) <br>
     * Oppure si può usare la classe AHeaderList con i messaggi in Html (eventualmente colorati) <br>
     * Di default false <br>
     */
    protected boolean usaHeaderWrap;

    /**
     * Flag per usare i log di sistema. Normalmente true <br>
     */
    protected boolean usaDataLogger = true; //@todo Creare una preferenza e sostituirla qui

    protected DataProvider dataProvider;

    protected String searchFieldValue = VUOTA;

    protected List<AFiltro> filtri;

    protected Sort sortView;

    protected List<AEButton> listaBottoni;

    protected LinkedHashMap<String, ComboBox> mappaComboBox;

    /**
     * The entityService obbligatorio, singleton di tipo xxxService <br>
     */
    protected AIService entityService;

    /**
     * Costruttore senza parametri <br>
     */
    public ALogicOld() {
    }


    public ALogicOld(final AEOperation operationForm) {
        this.operationForm = operationForm;
    }

    public ALogicOld(final AEOperation operationForm, final AIService entityService) {
        this.operationForm = operationForm;
        this.entityService = entityService;
    }

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l' istanza SOLO come SCOPE_PROTOTYPE <br>
     * Costruttore usato da AView <br>
     * L' istanza DEVE essere creata con (ALogic) appContext.getBean(Class.forName(canonicalName), entityService, operationForm) <br>
     *
     * @param entityService layer di collegamento tra il 'backend' e mongoDB
     * @param operationForm tipologia di Form in uso
     */
    public ALogicOld(AIService entityService, AEOperation operationForm) {
        this.entityService = entityService;
        this.operationForm = operationForm;
        this.entityClazz = entityService != null ? entityService.getEntityClazz() : null;
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        fixProperties();
        fixPreferenze();
    }


    public void fixEntityBean(AEntity entityBean) {
        this.entityBean = entityBean;
    }


    /**
     * Costruisce le properties di questa istanza <br>
     */
    private void fixProperties() {
        mappaComboBox = new LinkedHashMap<>();
        this.fixMappaComboBox();
    }


    /**
     * Preferenze usate da questa istanza e dalle Views collegate <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        //        this.keyPropertyName = annotation.getKeyPropertyName(entityClazz);
        this.searchType = AESearch.nonUsata;
        this.searchProperty = annotation.getSearchPropertyName(entityClazz);

        this.usaBottoneDelete = false;
        this.usaBottoneResetList = false;
        this.usaBottoneNew = true;
        this.usaBottoneSearch = false;
        this.usaBottoneExport = false;
        this.usaBottonePaginaWiki = false;
        this.usaBottoneUpdate = false;
        this.usaBottoneUpload = false;
        this.usaBottoneDownload = false;
        this.usaBottoneElabora = false;
        this.usaBottoneCheck = false;
        this.usaBottoneModulo = false;
        this.usaBottoneTest = false;
        this.usaBottoneStatistiche = false;
        this.maxNumeroBottoniPrimaRiga = AEPreferenza.numeroBottoni.getInt();

        this.wikiPageTitle = VUOTA;
        this.usaHeaderWrap = true;
        this.usaBottoneEdit = true;
        this.usaBottoneResetForm = false;
        this.usaBottoniSpostamentoForm = false;

        this.formClazz = AGenericForm.class;
    }

    /**
     * Costruisce un (eventuale) layout per avvisi aggiuntivi in alertPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Nell'implementazione standard di default NON presenta nessun avviso <br>
     * Recupera dal service specifico gli (eventuali) avvisi <br>
     * Costruisce un'istanza dedicata con le liste di avvisi <br>
     * Gli avvisi sono realizzati con tag html 'span' differenziati per colore anche in base all'utente collegato <br>
     * Se l'applicazione non usa security, il colore è deciso dal service specifico <br<
     * Se esiste, inserisce l'istanza (grafica) in alertPlacehorder della view <br>
     * alertPlacehorder viene sempre aggiunto, per poter (eventualmente) essere utilizzato dalle sottoclassi <br>
     *
     * @param typeVista in cui inserire gli avvisi
     *
     * @return componente grafico per il placeholder
     */
    @Override
    public AIHeader getAlertLayout(AEVista typeVista) {
        AIHeader headerSpan = null;
        List<Span> spanHtmlList = getSpanList();

        if (array.isAllValid(spanHtmlList)) {
            headerSpan = appContext.getBean(AHeaderSpanList.class, spanHtmlList);
        }

        return headerSpan;
    }

    /**
     * Costruisce un (eventuale) layout per avvisi aggiuntivi in alertPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Normalmente ad uso esclusivo del developer <br>
     * Nell' implementazione standard di default NON presenta nessun avviso <br>
     * Recupera dal service specifico gli (eventuali) avvisi <br>
     * Costruisce un' istanza dedicata (secondo il flag usaHeaderWrap) con le liste di avvisi <br>
     * <p>
     * AHeaderWrap:
     * Gli avvisi sono realizzati con label differenziate per colore in base all' utente collegato <br>
     * Se l' applicazione non usa security, il colore è unico (blue) <br>
     * Se AHeaderWrap esiste, inserisce l' istanza (grafica) in alertPlacehorder della view <br>
     * alertPlacehorder viene sempre aggiunto, per poter (eventualmente) essere utilizzato dalle sottoclassi <br>
     * <p>
     * AHeaderList:
     * Gli avvisi sono realizzati con elementi html con possibilità di color e bold <br>
     *
     * @param typeVista in cui inserire gli avvisi
     *
     * @return componente grafico per il placeHolder
     */
    @Override
    @Deprecated
    public AHeader getAlertHeaderLayout(final AEVista typeVista) {
        AHeader header = null;
        AlertWrap wrap = null;
        //        List<String> alertHtmlList = getAlertList(typeVista);

        switch (typeVista) {
            case list:
                wrap = getAlertWrapList();
                break;
            case form:
                wrap = getAlertWrapForm();
                break;
            default:
                logger.warn("Switch - caso non definito", this.getClass(), "getAlertHeaderLayout");
                break;
        }

        if (usaHeaderWrap) {
            if (wrap != null) {
                header = appContext.getBean(AHeaderWrap.class, wrap);
            }
        }
        else {
            //            if (alertHtmlList != null) {
            //                header = appContext.getBean(AHeaderList.class, alertHtmlList);
            //            }
        }

        return header;
    }


    /**
     * Informazioni (eventuali) specifiche di ogni modulo, mostrate nella List <br>
     * Costruisce una liste di 'span' per costruire l' istanza di AHeaderSpan <br>
     * DEVE essere sovrascritto <br>
     *
     * @return una liste di 'span'
     */
    protected List<Span> getSpanList() {
        return null;
    }

    /**
     * Informazioni (eventuali) specifiche di ogni modulo, mostrate nella List <br>
     * Costruisce un wrapper di liste di informazioni per costruire l' istanza di AHeaderWrap <br>
     * DEVE essere sovrascritto <br>
     *
     * @return wrapper per passaggio dati
     */
    @Deprecated
    protected AlertWrap getAlertWrapList() {
        //        return new AlertWrap(new ArrayList(Arrays.asList("uno", "due", "tre")));
        return null;
    }

    /**
     * Informazioni (eventuali) specifiche di ogni modulo, mostrate nel Form <br>
     * Costruisce un wrapper di liste di informazioni per costruire l' istanza di AHeaderWrap <br>
     * Deve essere sovrascritto <br>
     * Esempio:     return new AlertWrap(new ArrayList(Arrays.asList("uno", "due", "tre")));
     *
     * @return wrapper per passaggio dati
     */
    @Deprecated
    protected AlertWrap getAlertWrapForm() {
        return null;
    }

    //    /**
    //     * Costruisce una lista di informazioni per costruire l' istanza di AHeaderList <br>
    //     * Informazioni (eventuali) specifiche di ogni modulo <br>
    //     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
    //     * Esempio:     return new ArrayList(Arrays.asList("uno", "due", "tre"));
    //     *
    //     * @param typeVista in cui inserire gli avvisi
    //     *
    //     * @return wrapper per passaggio dati
    //     */
    //    @Deprecated
    //    protected List<String> getAlertList(AEVista typeVista) {
    //        String headerAlert = annotation.getHeaderAlert(entityClazz);
    //        return text.isValid(headerAlert) ? new ArrayList(Arrays.asList(headerAlert)) : new ArrayList<String>();
    //    }


    /**
     * Costruisce una mappa di ComboBox di selezione e filtro <br>
     * DEVE essere sovrascritto nella sottoclasse <br>
     */
    protected void fixMappaComboBox() {
    }


    /**
     * Costruisce un layout per i bottoni di comando in topPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * 1) Recupera dal service specifico una List<AEButton> di bottoni previsti <br>
     * Se List<AEButton> è vuota, ATopLayout usa i bottoni di default (solo New) <br>
     * 2) Recupera dal service specifico la condizione e la property previste (searchType,searchProperty) <br>
     * 3) Recupera dal service specifico una List<ComboBox> di popup di selezione e filtro <br>
     * Se List<ComboBox> è vuota, ATopLayout non usa popup <br>
     * Costruisce un' istanza dedicata con i bottoni, il campo textEdit di ricerca (eventuale) ed i comboBox (eventuali) <br>
     * Inserisce l' istanza (grafica) in topPlacehorder della view <br>
     *
     * @return componente grafico per il placeHolder
     */
    @Override
    public AButtonLayout getTopLayout() {
        AButtonLayout topLayout = appContext.getBean(ATopLayout.class, getWrapButtonsTop());
        this.addTopListeners(topLayout);
        return topLayout;
    }


    /**
     * Costruisce un wrapper di dati <br>
     * I dati sono gestiti da questa 'logic' (nella sottoclasse eventualmente) <br>
     * I dati vengono passati alla View che li usa <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     *
     * @return wrapper di dati per la view
     */
    public WrapButtons getWrapButtonsTop() {
        List<AEButton> listaAEBottoni = this.getListaAEBottoni();
        WrapSearch wrapSearch = this.getWrapSearch();
        LinkedHashMap<String, ComboBox> mappaComboBox = this.mappaComboBox;
        List<Button> listaBottoniSpecifici = this.getListaBottoniSpecifici();
        //        AEOperation operationForm = null;

        return appContext.getBean(WrapButtons.class, this, listaAEBottoni, wrapSearch, mappaComboBox, listaBottoniSpecifici, maxNumeroBottoniPrimaRiga);
    }


    /**
     * Costruisce una lista di bottoni (enumeration) <br>
     * Di default costruisce (come da flag) i bottoni 'delete' e 'reset' <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected List<AEButton> getListaAEBottoni() {
        List<AEButton> listaBottoni = new ArrayList<>();

        if (usaBottoneDelete) {
            listaBottoni.add(AEButton.deleteAll);
        }
        if (usaBottoneResetList) {
            listaBottoni.add(AEButton.resetList);
        }
        if (usaBottoneNew) {
            listaBottoni.add(AEButton.nuovo);
        }
        if (usaBottoneSearch) {
            listaBottoni.add(AEButton.searchDialog);
        }
        if (usaBottoneExport) {
            listaBottoni.add(AEButton.export);
        }
        if (usaBottonePaginaWiki) {
            listaBottoni.add(AEButton.wiki);
        }
        if (usaBottoneUpload) {
            listaBottoni.add(AEButton.upload);
        }
        if (usaBottoneDownload) {
            listaBottoni.add(AEButton.download);
        }

        return listaBottoni;
    }


    /**
     * Costruisce un wrap per la ricerca <br>
     * Può essere sovrascritto <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected WrapSearch getWrapSearch() {
        if (searchType == AESearch.editField && text.isEmpty(searchProperty)) {
            logger.error("Tipo di ricerca prevede un campo edit ma manca il nome della property", this.getClass(), "getWrapSearch");
            return null;
        }
        else {
            return new WrapSearch(searchType, searchProperty);
        }
    }


    /**
     * Costruisce una lista di bottoni specifici <br>
     * Di default non costruisce nulla <br>
     * Deve essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected List<Button> getListaBottoniSpecifici() {
        return new ArrayList<>();
    }


    /**
     * Costruisce un layout per la Grid posizionata nel bodyPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Costruisce un' istanza dedicata <br>
     * Crea il DataProvider <br>
     * Aggiunge i listener <br>
     * Restituisce l' istanza (grafica) alla view <br>
     *
     * @return componente grafico per il placeHolder
     */
    @Override
    public AGrid getBodyGridLayout() {
        grid = appContext.getBean(AGrid.class, entityClazz, this);
        //        dataProvider = dataService.creaDataProvider(entityClazz);
        //        grid.getGrid().setDataProvider(dataProvider);
        //        grid.getGrid().setHeight("100%");

        refreshGrid();
        addGridListeners();
        return grid;
    }


    /**
     * Aggiunge tutti i listeners alla Grid di 'bodyPlaceholder' che è stata creata SENZA listeners <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected void addGridListeners() {
        if (grid != null && grid.getGrid() != null) {
//            grid.setAllListener(this);
        }
    }

    //    /**
    //     * Costruisce un layout per il Form in bodyPlacehorder della view <br>
    //     * <p>
    //     * Chiamato da AView.initView() <br>
    //     * Costruisce un' istanza dedicata <br>
    //     * Inserisce l' istanza (grafica) in bodyPlacehorder della view <br>
    //     *
    //     * @param entityClazz the class of type AEntity
    //     *
    //     * @return componente grafico per il placeHolder
    //     */
    //    @Override
    //    public AForm getBodyFormLayout(Class<? extends AEntity> entityClazz) {
    //        form = null;
    //
    //        //--entityClazz dovrebbe SEMPRE esistere, ma meglio controllare
    //        if (entityClazz != null) {
    //            form = appContext.getBean(AGenericForm.class, entityClazz);
    //        }
    //
    //        return form;
    //    }


    /**
     * Costruisce un layout per il Form in bodyPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Costruisce un' istanza dedicata <br>
     * Passa all' istanza un wrapper di dati <br>
     * Inserisce l' istanza (grafica) in bodyPlacehorder della view <br>
     *
     * @param entityBean interessata
     *
     * @return componente grafico per il placeHolder
     */
    @Override
    public AForm getBodyFormLayout(AEntity entityBean) {
        currentForm = null;

        //--entityBean dovrebbe SEMPRE esistere (anche vuoto), ma meglio controllare
        if (entityBean != null) {
            currentForm = appContext.getBean(formClazz, this, getWrapForm(entityBean));
        }

        return currentForm;
    }


    /**
     * Costruisce un wrapper di dati <br>
     * I dati sono gestiti da questa 'logic' (nella sottoclasse eventualmente) <br>
     * I dati vengono passati alla View che li usa <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     *
     * @param entityBean interessata
     *
     * @return wrapper di dati per il Form
     */
    public WrapForm getWrapForm(AEntity entityBean) {
        return new WrapForm(entityBean, operationForm);
    }


    /**
     * Costruisce un layout per i bottoni di comando in bottomPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Recupera dal service specifico una List<AEButton> di bottoni previsti <br>
     * Se List<AEButton> è vuota, ATopLayout usa i bottoni di default (solo New) <br>
     * Costruisce un' istanza dedicata con i bottoni <br>
     * Inserisce l' istanza (grafica) in bottomPlacehorder della view <br>
     *
     * @return componente grafico per il placeHolder
     */
    @Override
    public ABottomLayout getBottomLayout(AEOperation operationForm) {
        ABottomLayout bottomLayout = appContext.getBean(ABottomLayout.class, getWrapButtonsBottom(operationForm));
        this.addBottomListeners(bottomLayout);
        return bottomLayout;
    }


    /**
     * Costruisce un wrapper di dati <br>
     * I dati sono gestiti da questa 'logic' (nella sottoclasse eventualmente) <br>
     * I dati vengono passati alla View che li usa <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     *
     * @return wrapper di dati per la view
     */
    public WrapButtonsOld getWrapButtonsBottom(AEOperation operationForm) {
        List<AEButton> listaInizialiBottom = this.getListaInizialiBottom();
        List<Button> listaSpecificiBottom = this.getListaSpecificiBottom();
        List<AEButton> listaFinaliBottom = this.getListaFinaliBottom();
        return new WrapButtonsOld(listaInizialiBottom, listaSpecificiBottom, listaFinaliBottom, operationForm);
    }


    /**
     * Costruisce una lista di bottoni (enumeration) per l'istanza di ABottomLayout <br>
     * Di default non costruisce nulla <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     * Di default costruisce i bottoni 'Back, Save e Delete' che la sottoclasse può modificare <br>
     * Se 'listaBottoni' rimane vuota, il layout usa i bottoni di default previsti per il tipo di operationForm <br>
     */
    protected List<AEButton> getListaInizialiBottom() {
        return null;
    }


    /**
     * Costruisce una lista di bottoni (enumeration) per l'istanza di ABottomLayout <br>
     * Di default non costruisce nulla <br>
     * Costruisce bottoni specifici che non appartengono alla enumeration AEButton <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected List<Button> getListaSpecificiBottom() {
        return null;
    }


    /**
     * Costruisce una lista di bottoni (enumeration) per l'istanza di ABottomLayout <br>
     * Di default non costruisce nulla <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected List<AEButton> getListaFinaliBottom() {
        List<AEButton> listaBottoni = new ArrayList<>();

        if (usaBottoniSpostamentoForm) {
            listaBottoni.add(AEButton.prima);
            listaBottoni.add(AEButton.dopo);
        }

        if (usaBottoneResetForm) {
            listaBottoni.add(AEButton.resetForm);
        }

        return listaBottoni;
    }

    /**
     * Aggiunge tutti i listeners ai bottoni di 'topPlaceholder' che sono stati creati SENZA listeners <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected void addTopListeners(AButtonLayout topLayout) {
        if (topLayout != null) {
//            topLayout.setAllListener(this);@//@todo PROVVISORIO
        }
    }


    /**
     * Aggiunge tutti i listeners ai bottoni di 'bottomPlaceholder' che sono stati creati SENZA listeners <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    protected void addBottomListeners(ABottomLayout bottomLayout) {
        if (bottomLayout != null) {
//            bottomLayout.setAllListener(this, entityBean);@//@todo PROVVISORIO
        }
    }


    /**
     * Costruisce una lista di nomi delle properties del Form nell'ordine:
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     *
     * @return lista di nomi delle properties da usare nel form
     */
    @Override
    //@todo Funzionalità ancora da implementare
    public List<String> getListaPropertiesForm() {
        List<String> lista = null;
        lista = annotation.getListaPropertiesForm(entityClazz);

        //        if (lista.contains(FIELD_NAME_COMPANY) && !context.getLogin().isDeveloper()) {
        //            lista.remove(FIELD_NAME_COMPANY);
        //        }// end of if cycle

        return lista;
    }

    //    /**
    //     * Costruisce una lista di nomi delle properties del Form, specializzata per una specifica operazione <br>
    //     * Di default utilizza la lista generale di getListaPropertiesForm() <br>
    //     * Sovrascritto nella sottoclasse concreta <br>
    //     *
    //     * @return lista di nomi delle properties da usare nel form
    //     */
    //    @Override
    //    public List<String> getListaPropertiesFormNew() {
    //        return getListaPropertiesForm();
    //    }


    /**
     * Costruisce una lista di nomi delle properties del Form, specializzata per una specifica operazione <br>
     * Di default utilizza la lista generale di getListaPropertiesForm() <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @return lista di nomi delle properties da usare nel form
     */
    @Override
    public List<String> getListaPropertiesFormEdit() {
        return getListaPropertiesForm();
    }

    //    /**
    //     * Costruisce una lista di nomi delle properties del Form, specializzata per una specifica operazione <br>
    //     * Di default utilizza la lista generale di getListaPropertiesForm() <br>
    //     * Sovrascritto nella sottoclasse concreta <br>
    //     *
    //     * @return lista di nomi delle properties da usare nel form
    //     */
    //    @Override
    //    public List<String> getListaPropertiesFormDelete() {
    //        return getListaPropertiesForm();
    //    }


    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     *
     * @param azione selezionata da eseguire
     */
    public void performAction(AEAction azione) {
        this.performAction(azione, VUOTA, (AEntity) null);
    }


    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     * Invocata solo dalla Grid <br>
     *
     * @param azione           selezionata da eseguire
     * @param searchFieldValue valore corrente del campo editText (solo per List)
     */
    public void performAction(AEAction azione, String searchFieldValue) {
        this.performAction(azione, searchFieldValue, (AEntity) null);
    }


    /**
     * Esegue l' azione del bottone, textEdit o comboBox. <br>
     * Invocata solo dal Form <br>
     *
     * @param azione     selezionata da eseguire
     * @param entityBean selezionata (solo per Form)
     */
    @Override
    public void performAction(AEAction azione, AEntity entityBean) {
        this.performAction(azione, VUOTA, entityBean);
    }


    private void performAction(AEAction azione, String searchFieldValue, AEntity entityBean) {
        switch (azione) {
            case deleteAll:
                this.openConfirmDeleteAll();
                break;
            case resetList:
                this.openConfirmReset();
                break;
            case resetForm:
                //                this.resetForm(entityBean);
                this.reloadForm(entityBean);
                break;
            case doubleClick:
                this.operationForm = AEOperation.edit;
                this.executeRoute(entityBean.id);
                break;
            case nuovo:
                this.operationForm = AEOperation.addNew;
                this.executeRoute();
                break;
            case edit:
                break;
            case show:
                break;
            case editNoDelete:
                break;
            case back:
            case annulla:
                this.openConfirmExitForm(entityBean);
                break;
            case conferma:
            case registra:
                if (saveDaForm()) {
                    this.back();
                }
                break;
            case delete:
                this.deleteForm();
                this.back();
                break;
            case prima:
                this.prima(entityBean);
                break;
            case dopo:
                this.dopo(entityBean);
                break;
            case searchField:
                this.searchFieldValue = searchFieldValue;
                refreshGrid();
                break;
            case searchDialog:
                Notification.show("Not yet. Coming soon.", 3000, Notification.Position.MIDDLE);
                logger.info("Not yet. Coming soon", this.getClass(), "performAction");
                break;
            case valueChanged:
                refreshGrid();
                break;
            case export:
                export();
                break;
            case showWiki:
                openWikiPage();
                break;
            default:
                logger.warn("Switch - caso non definito", ALogicOld.class, "performAction(azione, keyID)");
                break;
        }
    }

    /**
     * Azione proveniente dal click sul bottone Prima <br>
     * Recupera la lista FILTRATA e ORDINATA delle properties, ricevuta dalla Grid <br>@todo da realizzare
     * Si sposta alla precedente <br>
     * Carica il form relativo <br>
     */
    protected void prima(AEntity currentEntityBean) {
        AEntity previousEntityBean = mongo.findPrevious(entityClazz, currentEntityBean.id);
        executeRoute(previousEntityBean.id);
    }


    /**
     * Azione proveniente dal click sul bottone Dopo <br>
     * Recupera la lista FILTRATA e ORDINATA delle properties, ricevuta dalla Grid <br>@todo da realizzare
     * Si sposta alla successiva <br>
     * Carica il form relativo <br>
     */
    protected void dopo(AEntity currentEntityBean) {
        AEntity nextEntityBean;

        nextEntityBean = mongo.findNext(entityClazz, currentEntityBean.id);
        executeRoute(nextEntityBean.id);
    }


    /**
     * Azione proveniente dal click sul bottone Annulla
     */
    protected void back() {
        UI.getCurrent().getPage().getHistory().back();
    }


    /**
     * Refresh della lista
     */
    public void reloadList() {
        UI.getCurrent().getPage().reload();
    }

    /**
     * Refresh del form
     */
    public void reloadForm(AEntity entityBean) {
        UI.getCurrent().getPage().reload();
    }


    protected final void openDetail() {
        //        //@todo Funzionalità ancora da implementare
        //        if (false) {
        //            openDialogForm();
        //        } else {
        //            openDialogRoute();
        //        }
    }


    //@todo Funzionalità ancora da implementare
    protected final void openDialogForm() {
    }


    protected final void executeRoute() {
        final QueryParameters query = route.getQueryForm(entityClazz, operationForm,entityBean.id);
        UI.getCurrent().navigate(ROUTE_NAME_GENERIC_FORM, query);
    }


    protected final void executeRoute(final String entityBeanID) {
        final QueryParameters query = route.getQueryForm(entityClazz, operationForm,entityBeanID);
        UI.getCurrent().navigate(ROUTE_NAME_GENERIC_FORM, query);
    }


    /**
     * Opens the confirmation dialog before deleting all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected final void openConfirmDeleteAll() {
        MessageDialog messageDialog;
        String message = "Vuoi veramente cancellare tutto? L' operazione non è reversibile.";
        VaadinIcon icon = VaadinIcon.WARNING;

        if (mongo.isValid(entityClazz)) {
            messageDialog = new MessageDialog().setTitle("Delete").setMessage(message);
            messageDialog.addButton().text("Cancella").icon(icon).error().onClick(e -> clickDeleteAll()).closeOnClick();
            messageDialog.addButtonToLeft().text("Annulla").primary().clickShortcutEscape().clickShortcutEnter().closeOnClick();
            messageDialog.open();
        }
    }


    /**
     * Opens the confirmation dialog before reset all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected final void openConfirmReset() {
        MessageDialog messageDialog;
        String message = "Vuoi veramente ripristinare i valori originali predeterminati di questa collezione? L' operazione cancellerà tutti i valori successivamente aggiunti o modificati.";
        VaadinIcon icon = VaadinIcon.WARNING;

        if (mongo.isEmpty(entityClazz)) {
            clickReset();
        }
        else {
            messageDialog = new MessageDialog().setTitle("Reset").setMessage(message);
            messageDialog.addButton().text("Continua").icon(icon).error().onClick(e -> clickReset()).closeOnClick();
            messageDialog.addButtonToLeft().text("Annulla").primary().clickShortcutEscape().clickShortcutEnter().closeOnClick();
            messageDialog.open();
        }
    }


    /**
     * Opens the confirmation dialog before exiting form. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected final void openConfirmExitForm(AEntity entityBean) {
        MessageDialog messageDialog;
        String message = "La entity è stata modificata. Sei sicuro di voler perdere le modifiche? L' operazione non è reversibile.";
        VaadinIcon iconBack = VaadinIcon.ARROW_LEFT;

        if (operationForm == AEOperation.addNew) {
            back();
            return;
        }

        if (currentForm.isModificato()) {
            if (mongo.isValid(entityClazz)) {
                messageDialog = new MessageDialog().setTitle("Ritorno alla lista").setMessage(message);
                messageDialog.addButton().text("Rimani").primary().clickShortcutEscape().clickShortcutEnter().closeOnClick();
                messageDialog.addButtonToLeft().text("Back").icon(iconBack).error().onClick(e -> back()).closeOnClick();
                messageDialog.open();
            }
        }
        else {
            back();
        }
    }


    /**
     * Apre una pagina di wikipedia. <br>
     */
    protected final void openWikiPage() {
        String link = "\"" + PATH_WIKI + wikiPageTitle + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }


    /**
     * Costruisce una lista ordinata di nomi delle properties della Grid. <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * todo ancora da sviluppare
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getGridPropertyNamesList() {
        return annotation.getGridColumns(entityClazz);
    }


    /**
     * Costruisce una lista ordinata di nomi delle properties del Form. <br>
     * La lista viene usata per la costruzione automatica dei campi e l' inserimento nel binder <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIForm della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Se serve, modifica l' ordine della lista oppure esclude una property che non deve andare nel binder <br>
     * todo ancora da sviluppare
     *
     * @return lista di nomi di properties
     */
    public List<String> getFormPropertyNamesList() {
        List<String> fieldsNameList = annotation.getListaPropertiesForm(entityClazz);

        if (array.isEmpty(fieldsNameList)) {
            reflection.getFieldsName(entityBean.getClass());
        }

        if (FlowVar.usaCompany && annotation.usaCompany(entityBean.getClass())) {
            fieldsNameList.add(0, FIELD_COMPANY);
        }

        return fieldsNameList;
    }


    /**
     * Aggiorna gli items della Grid, utilizzando (anche) i filtri. <br>
     * Chiamato inizialmente alla creazione della Grid <br>
     * Chiamato per modifiche effettuate ai filtri, popup, newEntity, deleteEntity, ecc... <br>
     */
    public void refreshGrid() {
        List<? extends AEntity> items;

        if (grid != null && grid.getGrid() != null) {
            updateFiltri();
            items = mongo.findAll(entityClazz, filtri, sortView);
            //            grid.deselectAll();
            //            grid.refreshAll();
            grid.setItems(items);
        }
    }


    /**
     * Recupera ed aggiorna i filtri. <br>
     */
    public void updateFiltri() {
        AFiltro filtro = null;
        filtri = new ArrayList<>();

        //--filtro base della entity con ordinamento
        this.creaFiltroBaseEntity();

        //--filtro (eventuale) per la company in uso
        this.creaFiltroCompany();

        //--filtro (eventuale) del campo search
        this.creaFiltroSearch();

        //--filtri aggiuntivi (eventuali) dei comboBox
        this.creaFiltriComboBox();
    }


    /**
     * Filtro base della entity. <br>
     * Comprensivo di ordinamento di default <br>
     */
    public AFiltro creaFiltroBaseEntity() {
        AFiltro filtro = null;
        Sort sort = annotation.getSortSpring(entityClazz);

        if (sort != null) {
            filtro = new AFiltro(sort);
        }

        if (filtro != null) {
            filtri.add(filtro);
        }

        return filtro;
    }


    /**
     * Filtro (eventuale) per la company in uso. <br>
     * Solo se FlowVar.usaCompany=true <br>
     * Solo se la entity è sottoclasse di ACEntity <br>
     * Come developer, vedo comunque tutto <br>
     * Come admin e user vedo SOLO le entities che hanno la croce selezionata <br>
     */
    public AFiltro creaFiltroCompany() {
        AFiltro filtro = null;
        Company company = vaadinService.getCompany();
        boolean needCompany = annotation.usaCompany(entityClazz);

        if (FlowVar.usaCompany) {
            if (company != null) {
                if (!vaadinService.isDeveloper() && needCompany) {
                    filtri.add(new AFiltro(Criteria.where(FlowVar.companyClazz.getSimpleName().toLowerCase()).is(company)));
                }
            }
            else {
                logger.error("Non è selezionata nessuna company", this.getClass(), "creaFiltroCompany");
            }
        }

        if (filtro != null) {
            filtri.add(filtro);
        }

        return filtro;
    }


    /**
     * Filtro (eventuale) del searchField. <br>
     */
    public void creaFiltroSearch() {
        AFiltro filtro = null;
        CriteriaDefinition criteria = null;
        Sort sort = null;

        if (text.isValid(searchFieldValue)) {
            sort = Sort.by(Sort.Direction.ASC, searchProperty);
            //            if (pref.isBool(USA_SEARCH_CASE_SENSITIVE)) { //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
            if (false) {
                filtro = new AFiltro(Criteria.where(searchProperty).regex("^" + searchFieldValue), sort);
            }
            else {
                if (text.isValid(searchFieldValue)) {
                    filtro = new AFiltro(Criteria.where(searchProperty).regex("^" + searchFieldValue, "i"), sort);
                }
            }
        }
        if (filtro != null) {
            filtri.add(filtro);
        }
    }


    /**
     * Filtri (eventuali) dei comboBox. <br>
     */
    public void creaFiltriComboBox() {
        AFiltro filtro;
        ComboBox combo;
        String comboName;
        String message = VUOTA;
        Object value;
        CriteriaDefinition criteria = null;

        if (array.isAllValid(mappaComboBox)) {
            for (Map.Entry<String, ComboBox> entry : mappaComboBox.entrySet()) {
                filtro = null;
                combo = entry.getValue();
                comboName = entry.getKey();
                value = combo.getValue();
                if (value != null) {
                    filtro = new AFiltro(Criteria.where(entry.getKey()).is(value));

                    message = text.primaMaiuscola(comboName);
                    message += FORWARD;
                    message += value.toString();
                    logger.info(message, this.getClass(), "creaFiltriComboBox");
                }
                if (filtro != null) {
                    filtri.add(filtro);
                }
            }
        }
    }


    /**
     * Cerca tutte le entities di questa collection. <br>
     *
     * @return lista di entityBeans
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.find/#db.collection.find/)
     */
    public List<AEntity> findAll() {
        List<AEntity> items = new ArrayList<>();
        List<AFiltro> filtri = new ArrayList<>();

        //--filtro base della entity con ordinamento
        filtri.add(creaFiltroBaseEntity());

        //--filtro (eventuale) per la company in uso
        filtri.add(creaFiltroCompany());

        return mongo.findAll(entityClazz, filtri, sortView);
    }

    //    /**
    //     * Crea e registra una entity solo se non esisteva <br>
    //     * Deve esistere la keyPropertyName della collezione, in modo da poter creare una nuova entity <br>
    //     * solo col valore di un parametro da usare anche come keyID <br>
    //     * Controlla che non esista già una entity con lo stesso keyID <br>
    //     * Deve esistere il metodo newEntity(keyPropertyValue) con un solo parametro <br>
    //     *
    //     * @param keyPropertyValue obbligatorio
    //     *
    //     * @return la nuova entity appena creata e salvata
    //     */
    //    @Override
    //    public Object creaIfNotExist(String keyPropertyValue) {
    //        return null;
    //    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public AEntity newEntity() {
        return entityService.newEntity();
    }


    /**
     * Ordine di presentazione (facoltativo) <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     */
    @Override
    public int getNewOrdine() {
        return entityService.getNewOrdine();
    }


    /**
     * Retrieves an entity by its id.
     *
     * @param keyID must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public AEntity findById(String keyID) {
        return entityService.findById(keyID);
    }


    /**
     * Retrieves an entity by its keyProperty.
     *
     * @param keyPropertyValue must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public AEntity findByKey(String keyPropertyValue) {
        return entityService.findByKey(keyPropertyValue);
    }


    /**
     * Check the existence of a single entity. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyId       chiave identificativa
     *
     * @return true if exist
     */
    public boolean isEsiste(Class<? extends AEntity> entityClazz, String keyId) {
        return mongo.isEsiste(entityClazz, keyId);
    }

    //    /**
    //     * Registra una entity solo se non esisteva <br>
    //     *
    //     * @param entityBean nuova entity appena creata (non salvata e senza keyID)
    //     *
    //     * @return true se la entity è stata registrata
    //     */
    //    public boolean saveIfNotExist(AEntity entityBean) {
    //        return mongo.saveIfNotKey(entityBean, keyPropertyName) != null;
    //    }

    //    /**
    //     * Inserisce una entity solo se non esisteva <br>
    //     *
    //     * @param newEntityBean to be inserted
    //     *
    //     * @return true se la entity è stata inserita
    //     */
    //    public boolean insertIfNotExist(AEntity newEntityBean) {
    //        if (mongo.isEsiste(entityClazz, newEntityBean.id)) {
    //            return false;
    //        } else {
    //            try {
    //                return insert(newEntityBean) != null;
    //            } catch (Exception unErrore) {
    //                logger.error(unErrore, this.getClass(), "nomeDelMetodo");
    //                return false;
    //            }
    //
    //        }
    //    }


    /**
     * Inserts a document into a collection. <br>
     *
     * @param newEntityBean to be inserted
     *
     * @return the saved entity
     */
    public AEntity insert(AEntity newEntityBean) {
        AEntity entityBean = null;
        entityBean = fixKey(newEntityBean);
        entityBean = beforeSave(entityBean, AEOperation.addNew);

        if (entityBean == null) {
            Notification.show("La entity non è stata registrata", 3000, Notification.Position.MIDDLE);
            return newEntityBean;
        }

        entityBean = mongo.insert(entityBean);

        if (entityBean != null) {
            logger.nuovo(entityBean);
        }
        else {
            Notification.show("La entity non è stata registrata", 3000, Notification.Position.MIDDLE);
        }

        return entityBean;
    }


    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    public int count() {
        return mongo.count(entityClazz);
    }


    /**
     * Regola la chiave se esiste il campo keyPropertyName. <br>
     * Se la company è nulla, la recupera dal login <br>
     * Se la company è ancora nulla, la entity viene creata comunque
     * ma verrà controllata ancora nel metodo beforeSave() <br>
     *
     * @param newEntityBean to be checked
     *                      //     * @param company       to be checked
     *
     * @return the checked entity
     */
    public AEntity fixKey(AEntity newEntityBean) {
        String keyPropertyName;
        String keyPropertyValue;
        Company company;

        if (text.isEmpty(newEntityBean.id)) {
            keyPropertyName = annotation.getKeyPropertyName(newEntityBean.getClass());
            if (text.isValid(keyPropertyName)) {
                keyPropertyValue = reflection.getPropertyValueStr(newEntityBean, keyPropertyName);
                if (text.isValid(keyPropertyValue)) {
                    keyPropertyValue = text.levaSpazi(keyPropertyValue);
                    newEntityBean.id = keyPropertyValue.toLowerCase();
                }
            }
        }

        if (newEntityBean instanceof ACEntity) {
            company = vaadinService.getCompany();
            ((ACEntity) newEntityBean).company = company;
        }

        return newEntityBean;
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     * Controlla che la entity sia valida e superi i validators associati <br>
     *
     * @param newEntityBean da registrare
     *
     * @return la nuova entity appena creata e salvata
     */
    public AEntity checkAndSave(AEntity newEntityBean) {
        boolean valido = false;
        String message = VUOTA;
        Binder binder = null;

        //--controlla che la newEntityBean non esista già
        if (isEsiste(entityClazz, newEntityBean.id)) {
            return null;
        }

        valido = true;
        //        binder = new Binder(newEntityBean.getClass());
        //        beanService.creaFields(newEntityBean, AEOperation.addNew, binder);
        //        //--Sincronizza il binder all' apertura della scheda
        //        //--Trasferisce (binder read) i valori dal DB alla UI
        //        binder.readBean(newEntityBean);
        //        valido = binder.isValid();

        if (valido) {
            newEntityBean = beforeSave(newEntityBean, AEOperation.addNew);
            valido = mongo.insert(newEntityBean) != null;
        }
        else {
            message = "Duplicate key error ";
            message += beanService.getModifiche(newEntityBean);
            logger.warn(message, this.getClass(), "crea");
        }

        return newEntityBean;
    }


    /**
     * Save proveniente da un click sul bottone 'registra' del Form. <br>
     * La entityBean viene recuperare dal form <br>
     *
     * @return true se la entity è stata registrata o definitivamente scartata; esce dal dialogo
     * .       false se manca qualche field e la situazione è recuperabile; resta nel dialogo
     */
    public boolean saveDaForm() {
        AEntity entityBean = null;
        if (currentForm != null) {
            entityBean = currentForm.getValidBean();
        }

        return entityBean != null ? save(entityBean) : false;
    }


    /**
     * Save proveniente da un click sul bottone 'registra' del Form. <br>
     * La entityBean che arriva NON è necessariamente sincronizzata <br>
     * Meglio recuperare dal form la versione più affidabile <br>
     *
     * @return the saved entity
     */
    public boolean saveDaForm2(AEntity entityBean) {
        if (currentForm != null) {
            entityBean = currentForm.getValidBean();
        }

        if (entityBean == null) {
            Notification.show("Alcuni campi non sono adeguati", 3000, Notification.Position.MIDDLE);
            return false;
        }

        if (operationForm != null) {
            switch (operationForm) {
                case addNew:
                    return insert(entityBean) != null;
                case edit:
                case editNoDelete:
                case editProfile:
                case editDaLink:
                    return save(entityBean);
                case showOnly:
                    break;
                default:
                    logger.warn("Switch - caso non definito", this.getClass(), "saveDaForm");
                    break;
            }
        }
        else {
            return false;
        }
        return false;
    }


    /**
     * Operazioni eseguite PRIMA di save o di insert <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     * Controllo per la presenza della company se FlowVar.usaCompany=true <br>
     * Controlla se la entity registra le date di creazione e modifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    public AEntity beforeSave(AEntity entityBean, AEOperation operation) {
        Company company;

        entityBean = fixKey(entityBean);

        if (FlowVar.usaCompany && entityBean instanceof ACEntity) {
            company = ((ACEntity) entityBean).company;
            company = company != null ? company : vaadinService.getCompany();
            if (company == null) {
                return null;
            }
            else {
                ((ACEntity) entityBean).company = company;
            }
        }

        if (annotation.usaModifica(entityClazz)) {
            if (operation == AEOperation.addNew) {
                entityBean.creazione = LocalDateTime.now();
            }
            if (operation != AEOperation.showOnly) {
                if (beanService.isModificata(entityBean)) {
                    entityBean.modifica = LocalDateTime.now();
                }
            }
        }

        return entityBean;
    }


    /**
     * Saves a given entity.
     * Use the returned instance for further operations as the save operation
     * might have changed the entity instance completely.
     *
     * @return true se la entity è stata registrata o definitivamente scartata; esce dal dialogo
     * .       false se manca qualche field e la situazione è recuperabile; resta nel dialogo
     */
    public boolean save(AEntity entityToSave) {
        boolean status = false;
        AEntity oldEntityBean;
        AEntity entityBean = beforeSave(entityToSave, operationForm);

        if (entityBean == null) {
            return status;
        }

        if (beanService.isModificata(entityBean)) {
        }
        else {
            return true;
        }

        if (text.isEmpty(entityBean.id) && !(operationForm == AEOperation.addNew)) {
            logger.error("operationForm errato in una nuova entity che NON è stata salvata", ALogicOld.class, "save");
            return status;
        }

        if (entityBean != null) {
            if (operationForm == AEOperation.addNew && entityBean.id == null) {
                entityBean = fixKey(entityBean);
            }
            oldEntityBean = mongo.find(entityBean);
            entityBean = mongo.save(entityBean);
            status = entityBean != null;
            if (status) {
                if (operationForm == AEOperation.addNew) {
                    ALogService.messageSuccess(entityBean.toString() + " è stato creato"); //@todo Creare una preferenza e sostituirla qui
                    logger.nuovo(entityBean);
                }
                else {
                    ALogService.messageSuccess(entityBean.toString() + " è stato modificato"); //@todo Creare una preferenza e sostituirla qui
                    logger.modifica(entityBean, oldEntityBean);
                }
            }
        }
        else {
            logger.error("Object to save must not be null", this.getClass(), "save");
        }

        if (entityBean == null) {
            if (operationForm != null) {
                switch (operationForm) {
                    case addNew:
                        logger.warn("Non sono riuscito a creare la entity ", this.getClass(), "save");
                        break;
                    case edit:
                        logger.warn("Non sono riuscito a modificare la entity ", this.getClass(), "save");
                        break;
                    default:
                        logger.warn("Switch - caso non definito", this.getClass(), "save");
                        break;
                }
            }
            else {
                logger.warn("Non sono riuscito a creare la entity ", this.getClass(), "save");
            }
        }

        return status;
    }


    public boolean deleteForm() {
        boolean status = false;
        AEntity entityBean = (currentForm != null) ? currentForm.getValidBean() : null;

        if (mongo.delete(entityBean)) {
            status = true;
            logger.delete(entityBean);
        }

        return status;
    }


    /**
     * Cancellazione effettiva (dopo dialogo di conferma) di tutte le entities della collezione. <br>
     * Azzera gli items <br>
     * Ridisegna la GUI <br>
     */
    public void clickDeleteAll() {
        if (entityService.deleteAll()) {
            logger.deleteAll(entityClazz);
            this.refreshGrid();
            this.reloadList();
        }
    }


    /**
     * Azione proveniente dal click sul bottone Reset <br>
     * Creazione di alcuni dati iniziali <br>
     * Rinfresca la griglia <br>
     */
    public void clickReset() {
        if (resetDeletingAll()) {
            this.refreshGrid();
            this.reloadList();
        }
    }


    /**
     * Ricreazione di alcuni dati iniziali standard <br>
     * Invocato dal bottone Reset di alcune liste <br>
     * Cancella la collection (parzialmente, se usaCompany=true) <br>
     * I dati possono essere: <br>
     * 1) recuperati da una Enumeration interna <br>
     * 2) letti da un file CSV esterno <br>
     * 3) letti da Wikipedia <br>
     * 4) creati direttamente <br>
     *
     * @return false se non esiste il metodo sovrascritto o se la collection
     * ....... true se esiste il metodo sovrascritto è la collection viene ri-creata
     */
    private boolean resetDeletingAll() {
        AIResult result;
        entityService.delete();
        result = entityService.resetEmptyOnly();

        logger.log(AETypeLog.reset, result.getMessage());
        return result.isValido();
    }

    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     */
    @Deprecated
    protected void creaComboBox(Class entityClazz) {
        creaComboBox(entityClazz, WIDTH, true, false);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     */
    @Deprecated
    protected void creaComboBox(Class entityClazz, int width) {
        creaComboBox(entityClazz, width, true, false);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     */
    @Deprecated
    protected void creaComboBox(Class entityClazz, boolean clearButtonVisible, boolean required) {
        creaComboBox(entityClazz, WIDTH, clearButtonVisible, required);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     */
    @Deprecated
    protected void creaComboBox(Class entityClazz, int width, boolean clearButtonVisible, boolean required) {
        String tag = TRE_PUNTI;
        String widthEM = width > 0 ? width + TAG_EM : VUOTA;

        ComboBox combo = new ComboBox();
        combo.setWidth(widthEM);
        combo.setPreventInvalidInput(true);
        combo.setAllowCustomValue(false);
        combo.setPlaceholder(annotation.getRecordName(entityClazz) + tag);
        combo.setClearButtonVisible(clearButtonVisible);
        combo.setRequired(required);

        combo.setItems(mongo.find(entityClazz));
        mappaComboBox.put(annotation.getCollectionName(entityClazz), combo);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     */
    protected ComboBox creaComboBox(String propertyName) {
        return creaComboBox(propertyName, (List) null, WIDTH, null);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     */
    protected ComboBox creaComboBox(String propertyName, List items) {
        return creaComboBox(propertyName, items, WIDTH, null);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     */
    protected ComboBox creaComboBox(String propertyName, int width) {
        return creaComboBox(propertyName, (List) null, width, null);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     */
    protected ComboBox creaComboBox(String propertyName, Object initialValue) {
        return creaComboBox(propertyName, (List) null, WIDTH, initialValue);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     */
    protected ComboBox creaComboBox(String propertyName, List items, int width, Object initialValue) {
        ComboBox combo = null;
        Field reflectionJavaField = null;
        String tag = TRE_PUNTI;
        Class comboEnumClazz = null;
        String widthEM = width > 0 ? width + TAG_EM : VUOTA;
        Sort sort;

        reflectionJavaField = reflection.getField(entityClazz, propertyName);
        AETypeField type = annotation.getColumnType(reflectionJavaField);

        if (type != AETypeField.combo && type != AETypeField.enumeration) {
            return null;
        }

        if (type == AETypeField.combo) {
            comboEnumClazz = annotation.getComboClass(reflectionJavaField);
            sort = annotation.getSortSpring(comboEnumClazz);
            items = items != null ? items : comboEnumClazz != null ? mongo.findAll(comboEnumClazz, sort) : null;
            //            items = mongo.find(comboEnumClazz);
        }
        if (type == AETypeField.enumeration) {
            comboEnumClazz = annotation.getEnumClass(reflectionJavaField);
            items = items = items != null ? items : fieldService.getEnumerationItems(reflectionJavaField);
        }
        combo = new ComboBox();
        combo.setWidth(widthEM);
        combo.setPreventInvalidInput(true);
        combo.setAllowCustomValue(false);
        combo.setPlaceholder(text.primaMaiuscola(propertyName) + tag);
        combo.setClearButtonVisible(true);
        combo.setRequired(false);

        combo.setItems(items);
        if (initialValue != null) {
            combo.setValue(initialValue);
        }

        mappaComboBox.put(propertyName, combo);
        return combo;
    }


    /**
     * Aggiunge un ComboBox alla mappa <br>
     */
    protected void addComboBox(ComboBox combo, String fieldName, List items, String width, String placeHolder, boolean clearButtonVisible, boolean required) {
        combo.setWidth(width);
        combo.setPreventInvalidInput(true);
        combo.setAllowCustomValue(false);
        combo.setPlaceholder(placeHolder);
        combo.setClearButtonVisible(clearButtonVisible);
        combo.setRequired(required);

        combo.setItems(items);
        mappaComboBox.put(fieldName, combo);
    }


    protected void export() {
        Grid grid = new Grid(entityClazz, false);
        grid.setColumns("nome");
        grid.setItems(mongo.findAll(entityClazz));

        String message = "Export";
        InputStreamFactory factory = Exporter.exportAsExcel(grid);
        StreamResource streamRes = new StreamResource(message + ".xls", factory);

        Anchor anchorEsporta = new Anchor(streamRes, "Download");
        anchorEsporta.getElement().setAttribute("style", "color: red");
        anchorEsporta.getElement().setAttribute("Export", true);
        Button button = new Button(new Icon(VaadinIcon.DOWNLOAD_ALT));
        button.getElement().setAttribute("style", "color: red");
        anchorEsporta.add(button);
        //        exportPlaceholder.removeAll();
        //        exportPlaceholder.add(anchorEsporta);

    }

}
