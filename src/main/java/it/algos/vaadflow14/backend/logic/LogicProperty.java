package it.algos.vaadflow14.backend.logic;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.ui.button.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadflow14.ui.header.*;
import it.algos.vaadflow14.ui.interfaces.*;
import it.algos.vaadflow14.ui.service.*;
import it.algos.vaadflow14.ui.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 27-feb-2021
 * Time: 14:37
 * Superclasse astratta di LogicList. <br>
 * Serve per 'dichiarare' i riferimenti ad altre classi ed usarli nelle sottoclassi concrete <br>
 * I riferimenti sono 'public' per poterli usare con TestUnit <br>
 * Serve per 'dichiarare' le property ed usarle nelle sottoclassi concrete <br>
 * Le properties sono 'protected' per poterle usare nelle sottoclassi <br>
 * <p>
 * Alleggerisce la 'lettura' della sottoclasse principale. Non contiene 'business logic' <br>
 * Le property sono regolarmente disponibili in LogicList ed in tutte le sue (probabili) sottoclassi <br>
 */
public abstract class LogicProperty extends VerticalLayout {

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
    public AHtmlService html;

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
    public AAnnotationService annotation;

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
    public ARouteService route;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AClassService classService;

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
    public ADateService date;

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
    public AUtilityService utility;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ADataProviderService dataService;

    /**
     * PlaceHolder iniziale per avvisi sopra la Grid (o Form) <br><br>
     * Label o altro per informazioni specifiche; di norma per il developer <br>
     * Contenuto facoltativo, assente di default <br>
     */
    protected VerticalLayout alertPlaceHolder;

    /**
     * Avvisi SOPRA la Grid (o Form) <br>
     */
    protected AHeaderSpan headerSpan;

    /**
     * PlaceHolder per bottoni di comando SOPRA la Grid (o Form) <br>
     * Contenuto semi-obbligatorio <br>
     */
    protected VerticalLayout topPlaceHolder;

    /**
     * Bottoni di comando SOPRA la Grid (o Form) <br>
     * Riferimento al contenitore dei bottoni per eventuali regolazioni <br>
     */
    protected ATopLayout topLayout;

    /**
     * PlaceHolder principale per la Grid (o Form) <br>
     * Alcune regolazioni da preferenza o da parametro (bottone Edit, ad esempio) <br>
     * Contenuto obbligatorio <br>
     */
    protected VerticalLayout bodyPlaceHolder;

    /**
     * PlaceHolder per bottoni di comando SOTTO la Grid (o Form) <br>
     * Contenuto facoltativo, assente di default <br>
     */
    protected VerticalLayout bottomPlaceHolder;


    /**
     * Bottoni di comando SOTTO la Grid (o Form) <br>
     * Riferimento al contenitore dei bottoni per eventuali regolazioni <br>
     */
    protected ABottomLayout bottomLayout;


    /**
     * PlaceHolder finale per messaggi sotto la Grid (o Form) <br>
     * Barra inferiore di messaggi per l'utilizzatore <br>
     * Contenuto facoltativo, assente di default <br>
     */
    protected VerticalLayout footerPlaceHolder;


    /**
     * The entityClazz obbligatorio di tipo AEntity, per liste e form <br>
     */
    protected Class<? extends AEntity> entityClazz;

    /**
     * The entityService obbligatorio, singleton di tipo xxxService che implementa l'interfaccia AIService <br>
     * È il riferimento al service specifico correlato a questa istanza (prototype) di LogicList/FormList <br>
     * Viene regolato nel costruttore della sottoclasse concreta xxxService <br>
     * Tramite un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     */
    protected AIService entityService;

    /**
     * The entityBean, istanza di entityClazz obbligatorio solo per il form <br>
     */
    protected AEntity entityBean;

    /**
     * The entityBeanPrevID, keyID della entity precedente solo per il form (facoltativa) <br>
     */
    protected String entityBeanPrevID;

    /**
     * The entityBeanPrevID, keyID della entity successiva solo per il form (facoltativa) <br>
     */
    protected String entityBeanNextID;

    /**
     * The @route() of xxxLogicForm facoltativo  <br>
     */
    protected String routeNameForm;

    /**
     * Wrapper di dati recuperati dall'url del browser, obbligatorio per il form <br>
     */
    protected Parametro routeParameter;


    /**
     * Flag di preferenza per l' utilizzo degli span in rosso nell'header della lista. Di default false. <br>
     */
    protected boolean usaSpanHeaderRossi;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneDeleteAll;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneResetList;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
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
    protected boolean usaBottoneUpload;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneDownload;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneResetForm;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneBack;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneAnnulla;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneConferma;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneRegistra;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneCancella;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottonePrima;

    /**
     * Flag di preferenza per l' utilizzo del bottone. Di default false. <br>
     */
    protected boolean usaBottoneDopo;

    /**
     * Flag di preferenza per specificare il massimo numero di bottoni della prima riga <br>
     */
    protected int maxNumeroBottoniPrimaRiga;

    /**
     * Flag di preferenza per specificare il titolo della pagina wiki da mostrare in lettura <br>
     */
    protected String wikiPageTitle;

    /**
     * Tipologia di Form in uso <br>
     * Si recupera nel metodo AView.setParameter(), chiamato dall'interfaccia HasUrlParameter <br>
     */
    protected AEOperation operationForm = AEOperation.listNoForm;

    /**
     * ComboBox usati in topLayout <br>
     * La mappa si costruisce in regolazioniIniziali() della LogicList <br>
     * I ComboBox vengono aggiunti in fixComboBox() della LogicList <br>
     */
    protected Map<String, ComboBox> mappaComboBox;

    /**
     * Filtri collegati a dataProvider <br>
     * La mappa si costruisce in regolazioniIniziali() della LogicList <br>
     */
    protected Map<String, AFiltro> mappaFiltri;

    /**
     * Lista (eventuale) di 'span' da mostrare come header della view <br>
     * La lista si costruisce in regolazioniIniziali() della LogicList <br>
     */
    protected List<Span> spanHeaderList;


    protected void fixProperty() {
        if (routeParameter == null && annotation.getRouteName(this.getClass()).equals(ROUTE_NAME_GENERIC_VIEW)) {
            logger.error("Qualcosa non quadra", Logic.class, "fixProperty");
        }

        //        this.entityClazz = null;
        this.entityBean = null;
        this.entityBeanPrevID = VUOTA;
        this.entityBeanNextID = VUOTA;
        //        this.entityService = null;

        this.wikiPageTitle = VUOTA;
        //        this.wikiModuloTitle = VUOTA;
        //        this.wikiStatisticheTitle = VUOTA;
        this.topLayout = null;
        this.bottomLayout = null;
    }

    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        this.usaSpanHeaderRossi = AEPreferenza.usaSpanHeaderRossi.is();
        this.usaBottoneDeleteAll = false;
        this.usaBottoneResetList = false;
        this.usaBottoneNew = false;
        this.usaBottoneSearch = false;
        this.usaBottoneExport = false;
        this.usaBottonePaginaWiki = false;
        this.usaBottoneDownload = false;
        this.usaBottoneUpload = false;

        this.usaBottoneResetForm = false;
        this.usaBottoneBack = false;
        this.usaBottoneAnnulla = false;
        this.usaBottoneConferma = false;
        this.usaBottoneRegistra = false;
        this.usaBottoneCancella = false;
        this.usaBottonePrima = false;
        this.usaBottoneDopo = false;

        this.maxNumeroBottoniPrimaRiga = AEPreferenza.numeroBottoni.getInt();
        this.routeNameForm = classService.getRouteNameForm(entityClazz);
    }

    /**
     * Regolazioni iniziali di alcuni oggetti <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniIniziali() {
    }

    /**
     * Costruisce i 5 oggetti base (placeholder) di questa view <br>
     * <p>
     * Chiamato da LogicList.initView() <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixLayout() {
        this.removeAll();
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(true);

        //--Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
        this.alertPlaceHolder = new AVerticalLayout();

        //--Costruisce un layout (semi-obbligatorio) per i bottoni di comando della view <br>
        //--Eventualmente i bottoni potrebbero andare su due righe <br>
        this.topPlaceHolder = new AVerticalLayout();

        //--Costruisce il corpo principale (obbligatorio) della Grid <br>
        this.bodyPlaceHolder = new AVerticalLayout();

        //--Costruisce un (eventuale) layout per i bottoni sotto la Grid <br>
        this.bottomPlaceHolder = new AVerticalLayout();

        //--Costruisce un (eventuale) layout per scritte in basso della pagina <br>
        this.footerPlaceHolder = new AVerticalLayout();

        if (AEPreferenza.usaDebug.is()) {
            this.getElement().getStyle().set("background-color", AEColor.yellow.getEsadecimale());
            this.alertPlaceHolder.getElement().getStyle().set("background-color", AEColor.bisque.getEsadecimale());
            this.topPlaceHolder.getElement().getStyle().set("background-color", AEColor.lightgreen.getEsadecimale());
            this.bodyPlaceHolder.getElement().getStyle().set("background-color", AEColor.lightpink.getEsadecimale());
            this.bottomPlaceHolder.getElement().getStyle().set("background-color", AEColor.gainsboro.getEsadecimale());
            this.footerPlaceHolder.getElement().getStyle().set("background-color", AEColor.silver.getEsadecimale());
        }
    }


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Chiamato da Logic.initView() <br>
     * Inserisce l' istanza (grafica) in alertPlaceHolder della view <br>
     * DEVE essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    protected void fixAlertLayout() {
        if (alertPlaceHolder != null && headerSpan != null) {
            alertPlaceHolder.add(headerSpan);
        }
    }


    /**
     * Costruisce un singolo componente 'span' da mostrare nella header della view <br>
     *
     * @param message da visualizzare
     *
     * @return un componente 'span' con property della lista
     */
    protected Span getSpan(final String message) {
        return html.getSpanBlu(message, AETypeSize.small);
    }


    /**
     * Costruisce un singolo componente 'span' da mostrare nella header della view <br>
     * Attivo SOLO in modalità debug <br>
     *
     * @param message da visualizzare
     *
     * @return un componente 'span' con property della condizione debug
     */
    protected Span getSpanDebug(final String message) {
        Span span = new Span();

        if (AEPreferenza.usaDebug.is()) {
            span = html.getSpanRosso(message, AETypeWeight.bold, AETypeSize.small);
        }

        return span;
    }

    /**
     * Costruisce un layout per i bottoni di comando al Top della view <br>
     * Chiamato da Logic.initView() <br>
     * Aggiunge tutti i listeners ai bottoni <br>
     * Obbligatorio per la List, facoltativo per il Form <br>
     * Nella List i bottoni possono andare su due righe <br>
     * Nel Form i bottoni vanno su una sola riga <br>
     * Inserisce l' istanza (grafica) in topPlaceHolder della view <br>
     * Può essere sovrascritto senza invocare il metodo della superclasse <br>
     */
    protected void fixTopLayout() {
        topLayout = appContext.getBean(ATopLayout.class, this.getWrapButtonsTop());
        if (topPlaceHolder != null && topLayout != null) {
            topPlaceHolder.add(topLayout);
        }
    }

    /**
     * Regola una mappa di ComboBox (solo per la List e facoltativi) da usare nel wrapper getWrapButtonsTop() <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixMappaComboBox() {
    }


    /**
     * Costruisce un wrapper (obbligatorio) di dati per i bottoni di comando al Top della view <br>
     * I dati sono gestiti da questa 'logic' <br>
     * I dati vengono passati alla View che li usa <br>
     * Può essere sovrascritto senza invocare il metodo della superclasse <br>
     *
     * @return wrapper di dati per la view
     */
    protected WrapButtons getWrapButtonsTop() {
        List<AIButton> listaAEBottoni = this.getListaAEBottoniTop();
        //        WrapSearch wrapSearch = this.getWrapSearch();
        //                LinkedHashMap<String, ComboBox> mappaComboBox = this.mappaComboBox;
        //        List<Button> listaBottoniSpecifici = this.getListaBottoniSpecifici();
        //        AEOperation operationForm = null;
        //    public WrapButtons(final AILogic entityLogic, final List<AIButton> listaABottoni, final WrapSearch wrapSearch, final LinkedHashMap<String, ComboBox> mappaComboBox, final List<Button> listaBottoniSpecifici, final int maxNumeroBottoniPrimaRiga) {

        return appContext.getBean(WrapButtons.class, this, listaAEBottoni, (WrapSearch) null, mappaComboBox, (List<Button>) null, maxNumeroBottoniPrimaRiga);
    }

    /**
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Costruisce i bottoni come dai flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected List<AIButton> getListaAEBottoniTop() {
        return new ArrayList<>();
    }


    /**
     * Costruisce il corpo principale (obbligatorio) della Grid <br>
     * <p>
     * Chiamato da LogicList.initView() <br>
     * Costruisce un' istanza dedicata con la Grid <br>
     * Inserisce l' istanza (grafica) in bodyPlacehorder della view <br>
     */
    protected void fixBodyLayout() {
    }


    /**
     *
     */
    public List<String> getFormPropertyNamesList() {
        return null;
    }

    /**
     * Costruisce un layout per i bottoni di comando al Bottom della view <br>
     * Chiamato da Logic.initView() <br>
     * Aggiunge tutti i listeners ai bottoni <br>
     * Obbligatorio per il Form, facoltativo per la List <br>
     * Inserisce l' istanza (grafica) in bottomPlaceHolder della view <br>
     * Può essere sovrascritto senza invocare il metodo della superclasse <br>
     */
    protected void fixBottomLayout() {
        bottomLayout = appContext.getBean(ABottomLayout.class, this.getWrapButtonsBottom());
        if (bottomPlaceHolder != null && bottomLayout != null) {
            bottomPlaceHolder.add(bottomLayout);
        }
    }


    /**
     * Costruisce un wrapper (obbligatorio) di dati per i bottoni di comando al Bottom della view <br>
     * I dati sono gestiti da questa 'logic' <br>
     * I dati vengono passati alla View che li usa <br>
     * Può essere sovrascritto senza invocare il metodo della superclasse <br>
     *
     * @return wrapper di dati per la view
     */
    protected WrapButtons getWrapButtonsBottom() {
        List<AIButton> listaAEBottoni = this.getListaAEBottoniBottom();

        return appContext.getBean(WrapButtons.class, this, listaAEBottoni, null, null);
    }


    /**
     * Costruisce una lista di bottoni (enumeration) al Bottom della view <br>
     * Costruisce i bottoni come previsto dal flag operationForm <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected List<AIButton> getListaAEBottoniBottom() {
        return new ArrayList<>();
    }


    /**
     * Costruisce un (eventuale) layout per scritte in basso della pagina <br>
     * <p>
     * Chiamato da LogicList.initView() <br>
     * Normalmente non usato <br>
     * Se esiste, inserisce l' istanza (grafica) in footerPlaceHolder della view <br>
     */
    protected void fixFooterLayout() {
    }


    /**
     * Regolazioni finali di alcuni oggetti <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void regolazioniFinali() {
    }


    /**
     * Aggiunge i 5 oggetti base (placeholder) alla view, se sono utilizzati <br>
     */
    protected void addToLayout() {
        if (alertPlaceHolder != null && alertPlaceHolder.getComponentCount() > 0) {
            this.add(alertPlaceHolder);
        }
        if (topPlaceHolder != null && topPlaceHolder.getComponentCount() > 0) {
            this.add(topPlaceHolder);
        }
        if (bodyPlaceHolder != null && bodyPlaceHolder.getComponentCount() > 0) {
            this.add(bodyPlaceHolder);
        }
        if (bottomPlaceHolder != null && bottomPlaceHolder.getComponentCount() > 0) {
            this.add(bottomPlaceHolder);
        }
        if (footerPlaceHolder != null && footerPlaceHolder.getComponentCount() > 0) {
            this.add(footerPlaceHolder);
        }

        this.addFooterCopyright();
    }

    /**
     * Aggiunge i listener ai vari oggetti <br>
     */
    protected void fixListener() {
        if (topLayout != null) {
            topLayout.setAllListener((AILogic) this);
        }
    }

    /**
     * Aggiunge il footer <br>
     * //@todo Funzionalità ancora da implementare per andare in basso alla finestra
     */
    protected void addFooterCopyright() {
        Span span;
        String message;
        String copy = DEVELOPER_COMPANY;
        String project = FlowVar.projectNameUpper;
        String version = String.valueOf(FlowVar.projectVersion);
        String data = date.get(FlowVar.versionDate);

        message = String.format("%s - %s %s del %s", copy, project, version, data);
        if (AEPreferenza.usaDebug.is() && text.isValid(FlowVar.projectNote)) {
            message += SEP + FlowVar.projectNote;
        }
        span = html.getSpanBlu(message, AETypeWeight.bold, AETypeSize.small);
        this.add(span);
    }

    /**
     * The entityService obbligatorio, singleton di tipo xxxService che implementa l'interfaccia AIService <br>
     * È il riferimento al service specifico correlato a questa istanza (prototype) di LogicList/FormList <br>
     * Viene regolato nel costruttore della sottoclasse concreta xxxService <br>
     * Tramite un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     */
    public AIService getEntityService() {
        return entityService;
    }

}
