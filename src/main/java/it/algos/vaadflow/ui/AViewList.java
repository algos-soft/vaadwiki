package it.algos.vaadflow.ui;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SingleSelectionEvent;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.shared.ui.LoadMode;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAMenu;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.footer.AFooter;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.ASearchDialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadflow.ui.menu.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.USA_MENU;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 05-mag-2018
 * Time: 18:49
 * Classe astratta per visualizzare la Grid <br>
 * <p>
 * <p>
 * La sottoclasse concreta viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le property di questa classe/sottoclasse vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired della sottoclasse concreta <br>
 * 2) usano una loro classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
 * 3) vengono usate in un un metodo @PostConstruct di questa classe/sottoclasse, perché SpringBoot le inietta solo DOPO init() <br>
 * <p>
 * Le sottoclassi concrete NON hanno le annotation @SpringComponent, @SpringView e @Scope
 * NON annotated with @SpringComponent - Sbagliato perché va in conflitto con la @Route
 * NON annotated with @SpringView - Sbagliato perché usa la Route di VaadinFlow
 * NON annotated with @Scope - Usa @UIScope
 * Annotated with @Route (obbligatorio) per la selezione della vista.
 * <p>
 * Graficamente abbiamo:
 * 1) una barra di menu (obbligatorio) di tipo IAMenu
 * 2) un topPlaceholder (eventuale, presente di default) di tipo HorizontalLayout
 * - con o senza campo edit search, regolato da preferenza o da parametro
 * - con o senza bottone New, regolato da preferenza o da parametro
 * - con eventuali bottoni specifici, aggiuntivi o sostitutivi
 * 3) un alertPlaceholder di avviso (eventuale) con label o altro per informazioni; di norma per il developer
 * 4) un headerGridHolder della Grid (obbligatoria) con informazioni sugli elementi della lista
 * 5) una Grid (obbligatoria); alcune regolazioni da preferenza o da parametro (bottone Edit, ad esempio)
 * 6) un bottomLayout della Grid (eventuale) con informazioni sugli elementi della lista; di norma delle somme
 * 7) un bottomLayout (eventuale) con bottoni aggiuntivi
 * 8) un footer (obbligatorio) con informazioni generali
 * <p>
 * Le injections vengono fatta da SpringBoot nel metodo @PostConstruct DOPO init() automatico
 * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
 * <p>
 * Annotation @Route(value = "") per la vista iniziale - Ce ne pouò essere solo una per applicazione
 * ATTENZIONE: se rimangono due (o più) classi con @Route(value = ""), in fase di compilazione appare l'errore:
 * -'org.springframework.context.ApplicationContextException:
 * -Unable to start web server;
 * -nested exception is org.springframework.boot.web.server.WebServerException:
 * -Unable to start embedded Tomcat'
 * <p>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 */
@HtmlImport(value = "styles/algos-styles.html", loadMode = LoadMode.INLINE)
@Slf4j
public abstract class AViewList extends VerticalLayout implements IAView, BeforeEnterObserver, BeforeLeaveObserver {

    protected final static String EDIT_NAME = "Edit";

    protected final static String SHOW_NAME = "Show";

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AAnnotationService annotation = AAnnotationService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AArrayService array = AArrayService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AColumnService column = AColumnService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ADateService date = ADateService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AFieldService field = AFieldService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public AReflectionService reflection = AReflectionService.getInstance();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    @Autowired
    public AMongoService mongo;

    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected LogService logger;

    @Autowired
    protected UtenteService utenteService;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    protected PreferenzaService pref;

    protected TextField searchField;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * L'istanza viene  dichiarata nel costruttore @Autowired della sottoclasse concreta <br>
     */
    protected IAPresenter presenter;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * L'istanza viene  dichiarata nel costruttore @Autowired della sottoclasse concreta <br>
     */
    protected IADialog dialog;

    protected ASearchDialog searchDialog;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * Il service viene recuperato dal presenter, <br>
     * La repository è gestita direttamente dal service <br>
     */
    protected IAService service;

    /**
     * Il modello-dati specifico viene recuperato dal presenter <br>
     */
    protected Class<? extends AEntity> entityClazz;

    /**
     * Placeholder (eventuale, presente di default) SOPRA la Grid
     * - con o senza campo edit search, regolato da preferenza o da parametro
     * - con o senza bottone New, regolato da preferenza o da parametro
     * - con eventuali altri bottoni specifici
     */
    protected HorizontalLayout topPlaceholder = new HorizontalLayout();

    /**
     * Placeholder (eventuale) SOPRA la Grid <br>
     * Label o altro per informazioni specifiche; di norma per il developer
     */
    protected VerticalLayout alertPlacehorder = new VerticalLayout();

    /**
     * Label (obbligatoria)  che appare nell'header della Grid.
     * Informazioni sugli elementi della lista
     */
    protected Label headerGridHolder;

    /**
     * Griglia principale (obbligatoria)
     * Alcune regolazioni da preferenza o da parametro (bottone Edit, ad esempio)
     */
    protected Grid<AEntity> grid;

    /**
     * Placeholder (eventuale) SOTTO la Grid <br>
     * Eventuali bottoni aggiuntivi
     */
    protected HorizontalLayout bottomLayout;

    /**
     * Placeholder (obbligatorio) SOTTO la Grid con informazioni generali <br>
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    protected AFooter footer;

    /**
     * Flag di preferenza per usare il campo-testo di ricerca e selezione nella barra dei menu.
     * Facoltativo ed alternativo a usaSearchTextDialog. Normalmente false.
     */
    protected boolean usaSearchTextField;

    /**
     * Flag di preferenza per usare il campo-testo di ricerca e selezione nella barra dei menu.
     * Facoltativo ed alternativo a usaSearchTextField. Normalmente true.
     */
    protected boolean usaSearchTextDialog;

    /**
     * Flag di preferenza per usare il bottone new situato nella topLayout. Normalmente true.
     */
    protected boolean usaSearchBottoneNew;

    /**
     * Flag di preferenza per usare il placeholder di informazioni specifiche sopra la Grid. Normalmente false.
     */
    protected boolean usaTopAlert;

    /**
     * Flag di preferenza per la Label nell'header della Grid grid. Normalmente true.
     */
    protected boolean usaHaederGrid;

    /**
     * Flag di preferenza per mostrare una caption sopra la grid. Normalmente true.
     */
    @Deprecated
    protected boolean usaCaption;

    /**
     * Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente false.
     */
    protected boolean usaBottoneEdit;

    /**
     * Flag di preferenza posizionare il bottone Edit come prima colonna. Normalmente true.
     */
    protected boolean isBottoneEditBefore;

    /**
     * Flag di preferenza per il testo del bottone Edit. Normalmente 'Edit'.
     */
    protected String testoBottoneEdit;

    /**
     * Flag di preferenza per usare il placeholder di bottoni ggiuntivi sotto la Grid. Normalmente false.
     */
    protected boolean usaBottomLayout;

    /**
     * Flag di preferenza per cancellare tutti gli elementi. Normalmente false.
     */
    protected boolean usaBottoneDeleteAll;

    /**
     * Flag di preferenza per resettare le condizioni standard di partenza. Normalmente false.
     */
    protected boolean usaBottoneReset;

    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
     */
    protected boolean isEntityDeveloper;

    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
     */
    protected boolean isEntityAdmin;

    /**
     * Flag di preferenza per modificare la entity. Normalmente true.
     */
    protected boolean isEntityModificabile;

    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
     */
    protected boolean isEntityEmbadded;

    /**
     * Flag di preferenza se si caricano dati demo alla creazione. Resettabili. Normalmente false.
     */
    protected boolean isEntityUsaDatiDemo;

    /**
     * Flag di preferenza per un refresh dopo aggiunta/modifica/cancellazione di una entity. Normalmente true.
     */
    protected boolean usaRefresh;

    /**
     * Flag di preferenza per limitare le righe della Grid e mostrarle a gruppi (pagine). Normalmente true.
     */
    protected boolean usaPagination;

    /**
     * Flag di preferenza per selezionare il numero di righe visibili della Grid. Normalmente limit = pref.getInt(FlowCost.MAX_RIGHE_GRID) .
     */
    protected int limit;

    /**
     * Istanza (@VaadinSessionScope) inietta da Spring ed unica nella sessione <br>
     */
    @Autowired
    protected ALogin login;

    /**
     * Recuperato dalla sessione, quando la @route fa partire la UI. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    protected AContext context;

    protected HorizontalLayout footerLayout;

    protected int offset;

    protected Button minusButton;

    protected Button plusButton;

    protected ATextField paginationField;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Unica per tutta l'applicazione. Usata come libreria. <br>
     */
    @Autowired
    protected AVaadinService vaadinService;


    protected boolean isPagination;

    protected Collection items;

    protected ADeleteDialog deleteDialog;

    protected ArrayList<AppLayoutMenuItem> specificMenuItems = new ArrayList<AppLayoutMenuItem>();


    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * La sottoclasse usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * La sottoclasse usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     */
    public AViewList(IAPresenter presenter, IADialog dialog) {
        this.presenter = presenter;
        this.dialog = dialog;
        if (presenter != null) {
            this.presenter.setView(this);
            this.service = presenter.getService();
            this.entityClazz = presenter.getEntityClazz();
        }// end of if cycle
    }// end of Spring constructor


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    @PostConstruct
    protected void initView() {
//        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        this.setMargin(false);
        this.setSpacing(false);
        this.removeAll();

        //--Login and context della sessione
        context = vaadinService.fixLoginAndContext(login);
        login = context.getLogin();

        //--Le preferenze standard
        fixPreferenze();

        //--Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
        fixPreferenzeSpecifiche();

        creaLayout();
    }// end of method


    /**
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    private void fixPreferenze() {

        /**
         * Flag di preferenza per usare il campo-testo di ricerca e selezione nella barra dei menu.
         * Facoltativo ed alternativo a usaSearchTextDialog. Normalmente false.
         */
        usaSearchTextField = false;

        /**
         * Flag di preferenza per usare il campo-testo di ricerca e selezione nella barra dei menu.
         * Facoltativo ed alternativo a usaSearchTextField. Normalmente true.
         */
        usaSearchTextDialog = true;

        //--Flag di preferenza per usare il bottone new situato nella searchBar. Normalmente true.
        usaSearchBottoneNew = true;

        //--Flag di preferenza per usare il placeholder di informazioni specifiche sopra la Grid. Normalmente false.
        usaTopAlert = false;

        //--Flag di preferenza per la Label nell'header della Grid grid. Normalmente true.
        usaHaederGrid = true;

        //--Flag di preferenza per modificare la entity. Normalmente true.
        isEntityModificabile = true;

        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        usaBottoneEdit = true;

        //--Flag di preferenza posizionare il bottone Edit come prima colonna. Normalmente true
        isBottoneEditBefore = true;

        //--Flag di preferenza per il testo del bottone Edit. Normalmente 'Edit'.
        testoBottoneEdit = EDIT_NAME;

        //--Flag di preferenza per usare il placeholder di botoni ggiuntivi sotto la Grid. Normalmente false.
        usaBottomLayout = false;

        //--Flag di preferenza per cancellare tutti gli elementi. Normalmente false.
        usaBottoneDeleteAll = false;

        //--Flag di preferenza per resettare le condizioni standard di partenza. Normalmente false.
        usaBottoneReset = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityDeveloper = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityAdmin = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityEmbadded = false;

        //--Flag di preferenza se si caricano dati demo alla creazione. Resettabili. Normalmente false.
        isEntityUsaDatiDemo = false;

        //--Flag di preferenza per un refresh dopo aggiunta/modifica/cancellazione di una entity. Normalmente true.
        usaRefresh = true;

        //--Flag di preferenza per limitare le righe della Grid e mostrarle a gruppi (pagine). Normalmente true.
        usaPagination = true;

        //--Flag di preferenza per selezionare il numero di righe visibili della Grid. Normalmente limit = pref.getInt(FlowCost.MAX_RIGHE_GRID) .
        limit = pref.getInt(FlowCost.MAX_RIGHE_GRID);
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void fixPreferenzeSpecifiche() {
    }// end of method


    /**
     * Creazione e posizionamento dei componenti UI <br>
     * Può essere sovrascritto <br>
     */
    protected void creaLayout() {
        creaMenuLayout();

        if (creaTopLayout()) {
            this.add(topPlaceholder);
        }// end of if cycle

        if (creaAlertLayout()) {
            this.add(alertPlacehorder);
        }// end of if cycle

        creaGrid();
        creaGridBottomLayout();
        creaPaginationLayout();
        creaFooterLayout();
    }// end of method


    /**
     * Costruisce la barra di menu <br>
     */
    protected boolean creaMenuLayout() {
        IAMenu menu;
        EAMenu typeMenu = EAMenu.getMenu(pref.getStr(USA_MENU));

        if (typeMenu != null) {
            switch (typeMenu) {
                case buttons:
                    menu = StaticContextAccessor.getBean(AButtonMenu.class);
                    this.add(menu.getComp());
                    break;
                case popup:
                    menu = StaticContextAccessor.getBean(APopupMenu.class);
                    this.add(menu.getComp());
                    break;
                case flowing:
                    menu = StaticContextAccessor.getBean(AFlowingcodeAppLayoutMenu.class);
                    this.add(menu.getComp());
                    break;
                case vaadin:
                    menu = StaticContextAccessor.getBean(AAppLayoutMenu.class);
                    this.add(new Label("."));
                    this.add(menu.getAppLayout());
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        } else {
            return false;
        }// end of if/else cycle

        return true;
    }// end of method


    /**
     * Placeholder (eventuale, presente di default) SOPRA la Grid
     * - con o senza campo edit search, regolato da preferenza o da parametro
     * - con o senza bottone New, regolato da preferenza o da parametro
     * - con eventuali altri bottoni specifici
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected boolean creaTopLayout() {
        topPlaceholder.removeAll();
        topPlaceholder.addClassName("view-toolbar");
        String buttonTitle;
        Button deleteAllButton;
        Button resetButton;
        Button clearFilterTextBtn;
        Button searchButton;
        Button newButton;
        boolean isDeveloper = login.isDeveloper();
        boolean isAdmin = login.isAdmin();

        if (usaBottoneDeleteAll && isDeveloper) {
            deleteAllButton = new Button("Delete", new Icon(VaadinIcon.CLOSE_CIRCLE));
            deleteAllButton.getElement().setAttribute("theme", "error");
            deleteAllButton.addClassName("view-toolbar__button");
            deleteAllButton.addClickListener(e -> openConfirmDialogDelete());
            topPlaceholder.add(deleteAllButton);
        }// end of if cycle

        if (usaBottoneReset && isDeveloper) {
            resetButton = new Button("Reset", new Icon(VaadinIcon.CLOSE_CIRCLE));
            resetButton.getElement().setAttribute("theme", "error");
            resetButton.addClassName("view-toolbar__button");
            resetButton.addClickListener(e -> {
                service.reset();
                updateView();
            });
            topPlaceholder.add(resetButton);
        }// end of if cycle

        if (usaSearchTextField) {
            searchField = new TextField("", "Search");
            searchField.setPrefixComponent(new Icon("lumo", "search"));
            searchField.addClassName("view-toolbar__search-field");
            searchField.setValueChangeMode(ValueChangeMode.EAGER);
            searchField.addValueChangeListener(e -> updateView());

            clearFilterTextBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
            clearFilterTextBtn.addClickListener(e -> searchField.clear());

            topPlaceholder.add(searchField, clearFilterTextBtn);
        }// end of if cycle

        if (usaSearchTextDialog) {
            buttonTitle = text.primaMaiuscola(pref.getStr(FlowCost.FLAG_TEXT_SEARCH));
            searchButton = new Button(buttonTitle, new Icon("lumo", "search"));
            searchButton.getElement().setAttribute("theme", "secondary");
            searchButton.addClassName("view-toolbar__button");
            searchButton.addClickListener(e -> openSearch());
            topPlaceholder.add(searchButton);
        }// end of if cycle


        if (usaSearchBottoneNew) {
            buttonTitle = text.primaMaiuscola(pref.getStr(FlowCost.FLAG_TEXT_NEW));
            newButton = new Button(buttonTitle, new Icon("lumo", "plus"));
            newButton.getElement().setAttribute("theme", "primary");
            newButton.addClassName("view-toolbar__button");
            newButton.addClickListener(e -> openNew());
            topPlaceholder.add(newButton);
        }// end of if cycle

        return topPlaceholder.getComponentCount() > 0;
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected boolean creaAlertLayout() {
        alertPlacehorder.removeAll();
//        alertPlacehorder.addClassName("view-toolbar");
        alertPlacehorder.setMargin(false);
        alertPlacehorder.setSpacing(false);
        alertPlacehorder.setPadding(false);

        if (isEntityDeveloper || isEntityAdmin || isEntityEmbadded || isEntityUsaDatiDemo) {
            usaTopAlert = true;
        }// end of if cycle

        if (usaTopAlert) {
            if (isEntityDeveloper) {
                alertPlacehorder.add(new Label("Lista visibile solo perché sei collegato come developer. Gli admin e gli utenti normali non la vedono."));
            }// end of if cycle

            if (isEntityAdmin) {
                alertPlacehorder.add(new Label("Lista visibile solo perché sei collegato come admin. Gli utenti normali non la vedono."));
            }// end of if cycle

            if (isEntityEmbadded) {
                alertPlacehorder.add(new Label("Questa lista non dovrebbe mai essere usata direttamente (serve come test o per le sottoclassi specifiche)"));
                alertPlacehorder.add(new Label("L'entity è 'embedded' nelle collezioni che la usano (no @Annotation property DbRef)"));
            }// end of if cycle

            if (isEntityEmbadded || isEntityUsaDatiDemo) {
                alertPlacehorder.add(new Label("Allo startup del programma, sono stati creati alcuni elementi di prova"));
            }// end of if cycle
        }// end of if cycle

        return alertPlacehorder.getComponentCount() > 0;
    }// end of method


    /**
     * Crea il corpo centrale della view
     * Componente grafico obbligatorio
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     * Costruisce la Grid con le colonne. Gli items vengono caricati in updateView()
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto)
     */
    protected void creaGrid() {
        FlexLayout layout = new FlexLayout();
//        layout.setHeight("30em");

        //--Costruisce una lista di nomi delle properties della Grid nell'ordine:
        //--1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
        //--2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
        //--3) Sovrascrive la lista nella sottoclasse specifica di xxxService
        List<String> gridPropertyNamesList = service != null ? service.getGridPropertyNamesList(context) : null;
        if (entityClazz != null && AEntity.class.isAssignableFrom(entityClazz)) {
            try { // prova ad eseguire il codice
                //--Costruisce la Grid SENZA creare automaticamente le colonne
                //--Si possono così inserire colonne manuali prima e dopo di quelle automatiche
                grid = new Grid(entityClazz);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return;
            }// fine del blocco try-catch
        } else {
            grid = new Grid();
        }// end of if/else cycle

        //--@todo solo per la versione 10.0.5
        //--@todo dalla versione 12.0.0, si può levare ed aggiungere 'false' come secondo parametro a new Grid(...,false)
        for (Grid.Column column : grid.getColumns()) {
            grid.removeColumn(column);
        }// end of for cycle

        //--Apre il dialog di detail
        if (isBottoneEditBefore) {
            this.addDetailDialog();
        }// end of if cycle

        //--Eventuali colonne calcolate aggiunte PRIMA di quelle automatiche
        this.addSpecificColumnsBefore();

        //--Eventuale modifica dell'ordine di presentazione delle colonne automatiche
        gridPropertyNamesList = this.reorderingColumns(gridPropertyNamesList);

        //--Colonne normali aggiunte in automatico
        if (gridPropertyNamesList != null) {
            for (String propertyName : gridPropertyNamesList) {
                column.create(grid, entityClazz, propertyName);
            }// end of for cycle
        }// end of if cycle

        //--Eventuali colonne calcolate aggiunte DOPO quelle automatiche
        this.addSpecificColumnsAfter();

        //--Apre il dialog di detail
        if (!isBottoneEditBefore) {
            this.addDetailDialog();
        }// end of if cycle

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setWidth("50em");
        grid.setHeightByRows(true);
        grid.addClassName("pippoz");
        grid.getElement().setAttribute("theme", "row-dividers");
        layout.add(grid);
        this.add(layout);
        layout.setFlexGrow(1, grid);
        this.setFlexGrow(1, layout);

        fixGridHeader();
    }// end of method


    /**
     * Eventuale header text
     */
    protected void fixGridHeader() {
        HeaderRow topRow = grid.prependHeaderRow();
        Grid.Column[] matrix = array.getColumnArray(grid);
        HeaderRow.HeaderCell informationCell = topRow.join(matrix);
        headerGridHolder = new Label("x");
        informationCell.setComponent(headerGridHolder);
    }// end of method


    /**
     * Header text
     */
    protected String getGridHeaderText() {
        int numRecCollezione = service != null ? service.count() : 0;
        String filtro = text.format(items.size());
        String totale = text.format(numRecCollezione);
        String testo = entityClazz != null ? entityClazz.getSimpleName() + " - " : "";

        switch (numRecCollezione) {
            case 0:
                testo += "Al momento non ci sono elementi in questa collezione";
                break;
            case 1:
                testo += "Collezione con un solo elemento";
                break;
            default:
                if (isPagination) {
                    testo += "Collezione di " + limit + " elementi su " + totale + " totali. ";
                } else {
                    testo += "Collezione di " + totale + " elementi";
                }// end of if/else cycle
                break;
        } // end of switch statement

        return testo;
    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte PRIMA di quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsBefore() {
    }// end of method


    /**
     * Eventuale modifica dell'ordine di presentazione delle colonne
     * Sovrascritto
     */
    protected List<String> reorderingColumns(List<String> gridPropertyNamesList) {
        return gridPropertyNamesList;
    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
    }// end of method


    /**
     * Apre il dialog di detail
     */
    protected void addDetailDialog() {
        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        if (usaBottoneEdit) {
            ComponentRenderer renderer = new ComponentRenderer<>(this::createEditButton);
            Grid.Column colonna = grid.addColumn(renderer);
            colonna.setWidth("5em");
            colonna.setFlexGrow(0);
        } else {
            EAOperation operation = isEntityModificabile ? EAOperation.edit : EAOperation.showOnly;
            grid.addSelectionListener(evento -> apreDialogo((SingleSelectionEvent) evento, operation));
        }// end of if/else cycle
    }// end of method


    protected Button createEditButton(AEntity entityBean) {
        Button edit = new Button(testoBottoneEdit, event -> dialog.open(entityBean, EAOperation.edit, context));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        return edit;
    }// end of method


    protected void apreDialogo(SingleSelectionEvent evento, EAOperation operation) {
        if (evento != null && evento.getOldValue() != evento.getValue()) {
            if (evento.getValue().getClass().getName().equals(entityClazz.getName())) {
                dialog.open((AEntity) evento.getValue(), operation, context);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Costruisce un (eventuale) layout con bottoni aggiuntivi
     * Facoltativo (assente di default)
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaGridBottomLayout() {
        bottomLayout = new HorizontalLayout();
        bottomLayout.addClassName("view-toolbar");

        if (usaBottomLayout) {
            this.add(bottomLayout);
        }// end of if cycle
    }// end of method


    protected void creaFooterLayout() {
        if (footer != null) {
            this.add(footer.setAppMessage("", context));
        }// end of if cycle
    }// end of method


    /**
     * Controlla la 'dimensione' della collezione <br>
     * Se è inferiore alla 'soglia', non fa nulla <br>
     * Se è superiore, costruisce un layout con freccia indietro, numero pagina, freccia avanti <br>
     */
    protected void creaPaginationLayout() {
        if (!usaPagination) {
            return;
        }// end of if cycle


        int numRecCollezione = service.count();
        final String mess = "Gli elementi vengono mostrati divisi in pagine da " + limit + " elementi ciascuna. Con i bottoni (-) e (+) ci si muove avanti ed indietro, una pagina alla volta. Oppure si inserisce il numero della pagina desiderata.";

        if (numRecCollezione < limit) {
            isPagination = false;
            return;
        } else {
            isPagination = true;
        }// end of if/else cycle
        offset = 0;

        Button titleButton = new Button("Pagination");
        titleButton.addClickListener(e -> Notification.show(mess, 6000, Notification.Position.BOTTOM_START));

        minusButton = new Button("", new Icon("lumo", "minus"));
        minusButton.addClickListener(e -> diminuiscePagination());
        minusButton.setEnabled(false);

        plusButton = new Button("", new Icon("lumo", "plus"));
        plusButton.addClickListener(e -> aumentaPagination());

        paginationField = new ATextField("");
        paginationField.addValueChangeListener(e -> modificaPagination(e));
        paginationField.setValue("1");
        paginationField.setWidth("4em");

        footerLayout = new HorizontalLayout();
        footerLayout.add(titleButton);
        footerLayout.add(minusButton);
        footerLayout.add(paginationField);
        footerLayout.add(plusButton);
        this.add(footerLayout);
    }// end of method


    public void diminuiscePagination() {
        if (offset > 0) {
            offset--;
            paginationField.setValue("" + (offset + 1));
            updateView();
        }// end of if cycle

//        sincroPagination();
    }// end of method


    public void modificaPagination(AbstractField.ComponentValueChangeEvent event) {
        String value = (String) event.getValue();
        int numPage = Integer.decode(value);
        int maxPage = service.count() / limit + 1;

        if (numPage > 0 && numPage <= maxPage) {
            offset = numPage - 1;
            updateView();
        } else {
            if (numPage < 1) {
                offset = 0;
                paginationField.setValue("1");
                Notification.show("La numerazione delle pagine inizia da 1", 3000, Notification.Position.BOTTOM_START);
            }// end of if cycle
            if (numPage > maxPage) {
                offset = 0;
                paginationField.setValue(maxPage + "");
                Notification.show("La pagina più alta è " + maxPage, 3000, Notification.Position.BOTTOM_START);
            }// end of if cycle
        }// end of if/else cycle

        sincroPagination();
        updateView();
    }// end of method


    public void aumentaPagination() {
        if (offset < service.count()) {
            offset++;
            paginationField.setValue("" + (offset + 1));
        }// end of if cycle
    }// end of method


    public void sincroPagination() {
        if (offset == 0) {
            minusButton.setEnabled(false);
        } else {
            minusButton.setEnabled(true);
        }// end of if/else cycle

        if ((offset + 1) * limit > service.count()) {
            plusButton.setEnabled(false);
        } else {
            plusButton.setEnabled(true);
        }// end of if/else cycle
    }// end of method


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.addSpecificRoutes();
        this.updateView();
    }// end of method


    /**
     * Aggiunge al menu eventuali @routes specifiche
     * Solo sovrascritto
     */
    protected void addSpecificRoutes() {
    }// end of method


    /**
     * Aggiunge al menu la @route
     */
    protected void addRoute(Class<? extends AViewList> viewClazz) {
        MainLayout mainLayout = context.getMainLayout();
        if (specificMenuItems != null && specificMenuItems.size() > 0) {
            specificMenuItems.add(mainLayout.addMenu(viewClazz));
        }// end of if cycle
    }// end of method


    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        AppLayoutMenu appMenu = context.getAppMenu();

        if (dialog != null) {
            dialog.close();
        }// end of if cycle
        if (deleteDialog != null) {
            deleteDialog.close();
        }// end of if cycle
        if (specificMenuItems != null && specificMenuItems.size() > 0) {
            for (AppLayoutMenuItem menuItem : specificMenuItems) {
                appMenu.removeMenuItem(menuItem);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    public void updateView() {
        updateItems();

        if (items != null) {
            try { // prova ad eseguire il codice
                grid.deselectAll();
                grid.setItems(items);
                headerGridHolder.setText(getGridHeaderText());
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        creaAlertLayout();
    }// end of method


    public void updateItems() {
        if (isPagination) {
            items = service != null ? service.findAll(offset, limit) : null;
        } else {
            items = service != null ? service.findAll() : null;
        }// end of if/else cycle
    }// end of method


    protected void openSearch() {
        searchDialog = appContext.getBean(ASearchDialog.class, service);
        searchDialog.open("", "", this::updateViewDopoSearch, null);
    }// end of method


    protected void openNew() {
        dialog.open(service.newEntity(), EAOperation.addNew, context);
    }// end of method


    public void updateViewDopoSearch() {
        LinkedHashMap<String, AbstractField> fieldMap = searchDialog.fieldMap;
        List<AEntity> lista;
        ATextField field;
        String fieldValue;
        ArrayList<CriteriaDefinition> listaCriteriaDefinition = new ArrayList();

        for (String fieldName : searchDialog.fieldMap.keySet()) {
            field = (ATextField) searchDialog.fieldMap.get(fieldName);
            fieldValue = field.getValue();
            if (text.isValid(fieldValue)) {
                listaCriteriaDefinition.add(Criteria.where(fieldName).is(fieldValue));
            }// end of if cycle
        }// end of for cycle

        lista = mongo.findAllByProperty(entityClazz, listaCriteriaDefinition.stream().toArray(CriteriaDefinition[]::new));

        if (array.isValid(lista)) {
            try { // prova ad eseguire il codice
                grid.deselectAll();
                grid.setItems(lista);
                headerGridHolder.setText(getGridHeaderText());
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        } else {
            this.updateView();
        }// end of if/else cycle

        creaAlertLayout();
    }// end of method


    /**
     * Opens the confirmation dialog before deleting all items.
     * <p>
     * The dialog will display the given title and message(s), then call
     */
    protected final void openConfirmDialogDelete() {
        String message = "Vuoi veramente cancellare TUTTE le entities di questa collezione ?";
        String additionalMessage = "L'operazione non è reversibile";
        deleteDialog = appContext.getBean(ADeleteDialog.class);
        deleteDialog.open(message, additionalMessage, this::deleteCollection, null);
    }// end of method


    protected void deleteCollection() {
        service.deleteAll();
        updateView();
    }// end of method


    /**
     * Primo ingresso dopo il click sul bottone <br>
     */
    protected void save(AEntity entityBean, EAOperation operation) {
        entityBean = service.beforeSave(entityBean, operation);
        switch (operation) {
            case addNew:
                if (service.isEsisteEntityKeyUnica(entityBean)) {
                    Notification.show(entityBean + " non è stata registrata, perché esisteva già con lo stesso code ", 3000, Notification.Position.BOTTOM_START);
                } else {
                    service.save(entityBean);
                    updateView();
                    Notification.show(entityBean + " successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
                }// end of if/else cycle
                break;
            case edit:
            case editDaLink:
                service.save(entityBean);
                updateView();
                Notification.show(entityBean + " successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

//        if (usaRefresh) {
//            updateView();
//        }// end of if cycle

    }// end of method


    protected void delete(AEntity entityBean) {
        service.delete(entityBean);
        Notification.show(entityBean + " successfully deleted.", 3000, Notification.Position.BOTTOM_START);

        if (usaRefresh) {
            updateView();
        }// end of if cycle
    }// end of method


    @Override
    public String getMenuName() {
        return annotation.getMenuName(this.getClass());
    }// end of method

}// end of class
