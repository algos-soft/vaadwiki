package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.ASearchDialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.fields.ATextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

import static it.algos.vaadflow.application.FlowCost.TAG_LOGIN;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Mon, 20-May-2019
 * Time: 07:06
 * <p>
 * Superclasse di servizio per separare le property di AViewList in una classe 'dedicata' <br>
 * Alleggerisce la 'lettura' della sottoclasse principale <br>
 * Le property sono regolarmente disponibili in AViewList ed in tutte le sue sottoclassi <br>
 */
public abstract class APropertyViewList extends VerticalLayout {


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
    public AColumnService columnService = AColumnService.getInstance();

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

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    public AMongoService mongo;

    /**
     * Istanza unica di una classe di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected LogService logger;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected UtenteService utenteService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AVaadinService vaadinService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected PreferenzaService pref;

    //--property
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

    //--property
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
     * Placeholder SOPRA la Grid <br>
     * Contenuto eventuale, presente di default <br>
     * - con o senza campo edit search, regolato da preferenza o da parametro <br>
     * - con o senza bottone New, regolato da preferenza o da parametro <br>
     * - con eventuali altri bottoni specifici <br>
     */
    protected HorizontalLayout topPlaceholder;

    /**
     * Placeholder SOPRA la Grid <br>
     * Contenuto eventuale, non presente di default <br>
     * Label o altro per informazioni specifiche; di norma per il developer <br>
     */
    protected VerticalLayout alertPlacehorder;

    /**
     * Label (obbligatoria)  che appare nell'header della Grid. <br>
     * Informazioni sugli elementi della lista <br>
     */
    protected Label headerGridHolder;

    /**
     * Placeholder per la Grid dichiarata nella superclasse oppure <br>
     * per la griglia con paginazione che deve essere dichiarata nella sottoclasse specifica <br>
     */
    protected VerticalLayout gridPlaceholder;

    /**
     * Griglia principale con o senza senza paginazione <br>
     * Alcune regolazioni da preferenza o da parametro (bottone Edit, ad esempio) <br>
     */
    protected Grid grid;

    /**
     * Placeholder SOTTO la Grid <br>
     * Eventuali bottoni aggiuntivi <br>
     */
    protected HorizontalLayout bottomPlacehorder;


    /**
     * Flag di preferenza per usare il campo-testo di ricerca e selezione nella barra dei menu. <br>
     * Facoltativo ed alternativo a usaSearchTextDialog. Normalmente false. <br>
     */
    protected boolean usaSearchTextField;

    /**
     * Flag di preferenza per aprire un dialogo di ricerca e selezione. <br>
     * Facoltativo ed alternativo a usaSearchTextField. Normalmente true.
     */
    protected boolean usaSearchTextDialog;

    /**
     * Flag di preferenza per usare il bottone all situato nella searchBar. Normalmente true <br>
     */
    protected boolean usaAllButton;

    /**
     * Flag di preferenza per limitare le righe della Grid e mostrarle a gruppi (pagine). Normalmente true. <br>
     */
    protected boolean usaPagination;

    /**
     * Flag di preferenza per la soglia di elementi che fanno scattare la pagination. <br>
     * Specifico di ogni ViewList. Se non specificato è uguale alla preferenza. Default 50 <br>
     */
    protected int sogliaPagination;

    /**
     * Flag per costruire una Grid normale o una PaginatedGrid. Viene regolato da codice. <br>
     */
    protected boolean usaGridPaginata;

    /**
     * Flag di preferenza per usare il bottone new situato nella topLayout. Normalmente true. <br>
     */
    protected boolean usaSearchBottoneNew;

    protected Button newButton;

    /**
     * Flag di preferenza per usare il placeholder di informazioni specifiche sopra la Grid. Normalmente false. <br>
     */
    protected boolean usaTopAlert;

    /**
     * Flag di preferenza per la Label nell'header della Grid grid. Normalmente true. <br>
     */
    protected boolean usaHaederGrid;

    /**
     * Flag di preferenza per mostrare una caption sopra la grid. Normalmente true. <br>
     */
    @Deprecated
    protected boolean usaCaption;

    /**
     * Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true. <br>
     */
    protected boolean usaBottoneEdit;

    /**
     * Flag di preferenza per il testo del bottone Edit. Normalmente 'Edit'. <br>
     */
    protected String testoBottoneEdit;

    /**
     * Flag di preferenza per usare il placeholder di bottoni ggiuntivi sotto la Grid. Normalmente false. <br>
     */
    protected boolean usaBottomLayout;

    /**
     * Flag di preferenza per cancellare tutti gli elementi. Normalmente false. <br>
     */
    protected boolean usaBottoneDeleteAll;

    /**
     * Flag di preferenza per resettare le condizioni standard di partenza. Normalmente false. <br>
     */
    protected boolean usaBottoneReset;

    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false. <br>
     */
    protected boolean isEntityDeveloper;

    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false. <br>
     */
    protected boolean isEntityAdmin;

    /**
     * Flag di preferenza per modificare la entity. Normalmente true. <br>
     */
    protected boolean isEntityModificabile;

    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false. <br>
     */
    protected boolean isEntityEmbedded;

    /**
     * Flag di preferenza se si caricano dati demo alla creazione. Resettabili. Normalmente false. <br>
     */
    protected boolean isEntityUsaDatiDemo;

    /**
     * Flag di preferenza per un refresh dopo aggiunta/modifica/cancellazione di una entity. Normalmente true. <br>
     */
    protected boolean usaRefresh;

    /**
     * Flag di preferenza per selezionare il numero di righe visibili della Grid. <br>
     * Normalmente limit = pref.getInt(FlowCost.MAX_RIGHE_GRID). <br>
     */
    protected int limit;

    /**
     * Flag per la larghezza della Grid. Default a 75. Espressa come numero per comodità; poi viene convertita in "em". <br>
     */
    protected int gridWith;

    /**
     * Istanza (@VaadinSessionScope) inietta da Spring ed unica nella sessione <br>
     */
    @Autowired
    @Qualifier(TAG_LOGIN)
    protected ALogin login;

    /**
     * Recuperato dalla sessione, quando la @route fa partire la UI. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    protected AContext context;

    protected int offset;

    protected boolean isPagination;

    protected Collection items;

    protected ADeleteDialog deleteDialog;

    protected ArrayList<AppLayoutMenuItem> specificMenuItems = new ArrayList<AppLayoutMenuItem>();

    /**
     * Flag di preferenza per usare una route view come detail della singola istanza. Normalmente true. <br>
     * In alternativa si può usare un Dialog.
     */
    protected boolean usaRouteFormView;

    /**
     * Nome della route per la location della pagina di modifica (standard) del Form <br>
     */
    protected String routeNameFormEdit;

}// end of class
