package it.algos.vaadflow.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
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
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.footer.AFooter;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 05-mag-2018
 * Time: 18:49
 * Classe astratta per visualizzare la Grid e il Form/Dialog <br>
 * <p>
 * <p>
 * La sottoclasse concreta viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le property di questa classe/sottoclasse vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired della sottoclasse concreta <br>
 * 2) usano una loro classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
 * 3) vengono usate in un un metodo @PostConstruct di questa classe/sottoclasse,
 * perché SpringBoot le inietta solo DOPO init() <br>
 * <p>
 * Le sottoclassi concrete NON hanno le annotation @SpringComponent, @SpringView e @Scope
 * NON annotated with @SpringComponent - Sbagliato perché va in conflitto con la @Route
 * NON annotated with @SpringView - Sbagliato perché usa la Route di VaadinFlow
 * NON annotated with @Scope - Usa @UIScope
 * Annotated with @Route (obbligatorio) per la selezione della vista.
 * <p>
 * Graficamente abbiamo:
 * una SearchBar (eventuale, presente di default); con o senza bottone New, regolato da preferenza o da parametro
 * un avviso (eventuale) per il developer
 * un layout top (eventuale) con bottoni aggiuntivi
 * una Grid (obbligatoria); alcune regolazioni da preferenza o da parametro (bottone Edit, ad esempio)
 * un layout bottom (eventuale) con bottoni aggiuntivi
 * un footer (obbligatorio) con informazioni generali
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
@Slf4j
public abstract class AViewList extends VerticalLayout implements IAView, BeforeEnterObserver {

    protected final static String EDIT_NAME = "Edit";
    protected final static String SHOW_NAME = "Show";
    protected final TextField searchField = new TextField("", "Search");

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    public ALogin login;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    public AAnnotationService annotation;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    public AReflectionService reflection;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    public AColumnService column;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    public AArrayService array;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    protected ADateService date;

    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot solo DOPO init() automatico <br>
     * Usare quindi un metodo @PostConstruct per averla disponibile <br>
     */
    @Autowired
    protected AFooter footer;


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
     * Placeholder per (eventuali) bottoni SOPRA della Grid <br>
     */
    protected HorizontalLayout topLayout = new HorizontalLayout();


    /**
     * Placeholder per (eventuali) bottoni SOTTO la Grid <br>
     */
    protected HorizontalLayout bottomLayout = new HorizontalLayout();


    /**
     * Griglia principale <br>
     */
    protected Grid<AEntity> grid;

    /**
     * Flag di preferenza per usare la searchBar. Normalmente true.
     */
    protected boolean usaSearchBar;


    /**
     * Flag di preferenza per usare il campo-testo di ricerca e selezione. Normalmente true.
     */
    protected boolean usaSearchTextField;


    /**
     * Flag di preferenza per usare il bottone new situato nella searchBar. Normalmente true.
     */
    protected boolean usaSearchBottoneNew;


    /**
     * Flag di preferenza per mostrare una caption sopra la grid. Normalmente true.
     */
    protected boolean usaCaption;


    /**
     * Flag di preferenza per modificare la entity. Normalmente true.
     */
    protected boolean isEntityModificabile;


    /**
     * Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente false.
     */
    protected boolean usaBottoneEdit;


    /**
     * Flag di preferenza per il testo del bottone Edit. Normalmente 'Edit'.
     */
    protected String testoBottoneEdit;


    /**
     * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
     */
    protected boolean isEntityDeveloper;


    /**
     * * Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
     */
    protected boolean isEntityEmbadded;


    /**
     * Flag di preferenza per un refresh dopo aggiunta/modifica/cancellazione di una entity. Normalmente true.
     */
    protected boolean usaRefresh;

    private Label headerHolder;

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
        addClassName("categories-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        this.setMargin(false);
        this.setSpacing(false);
        this.removeAll();

        //--Le preferenze standard
        fixPreferenze();

        //--Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
        fixPreferenzeSpecifiche();

        creaSearchBar();
        creaDeveloperAlert();
        creaTopLayout();
        creaCaption("");
        creaGrid();
        creaBottomLayout();
        creaFooter();
    }// end of method


    /**
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     */
    private void fixPreferenze() {
        //--Flag di preferenza per usare la searchBar. Normalmente true.
        usaSearchBar = true;

        //--Flag di preferenza per usare il campo-testo di ricerca e selezione. Normalmente true.
        usaSearchTextField = true;

        //--Flag di preferenza per usare il bottone new situato nella searchBar. Normalmente true.
        usaSearchBottoneNew = true;

        //--Flag di preferenza per mostrare una caption sopra la grid. Normalmente false.
        usaCaption = false;

        //--Flag di preferenza per modificare la entity. Normalmente true.
        isEntityModificabile = true;

        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        usaBottoneEdit = true;

        //--Flag di preferenza per il testo del bottone Edit. Normalmente 'Edit'.
        testoBottoneEdit = EDIT_NAME;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityDeveloper = false;

        //--Flag di preferenza per aggiungere una caption di info sopra la grid. Normalmente false.
        isEntityEmbadded = false;

        //--Flag di preferenza per un refresh dopo aggiunta/modifica/cancellazione di una entity. Normalmente true.
        usaRefresh = true;

    }// end of method

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     */
    protected void fixPreferenzeSpecifiche() {
    }// end of method


    /**
     * Costruisce la searchBar
     * CSS specifico
     * Sempre presente il campo edit di ricerca/selezione
     * Sempre presente il bottone di reset del valore del campo di ricerca/selezione
     * Facoltativo (presente di default) il bottone New (flag da mongo eventualmente sovrascritto)
     */
    protected void creaSearchBar() {
        Div viewToolbar = new Div();
        Button newButton = null;
        viewToolbar.addClassName("view-toolbar");

        if (!usaSearchBar) {
            return;
        }// end of if cycle

        if (usaSearchTextField) {
            searchField.setPrefixComponent(new Icon("lumo", "search"));
            searchField.addClassName("view-toolbar__search-field");
            searchField.setValueChangeMode(ValueChangeMode.EAGER);
            searchField.addValueChangeListener(e -> updateView());

            Button clearFilterTextBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
            clearFilterTextBtn.addClickListener(e -> searchField.clear());

            viewToolbar.add(searchField, clearFilterTextBtn);
        }// end of if cycle

        if (usaSearchBottoneNew) {
            newButton = new Button("New entity", new Icon("lumo", "plus"));
            newButton.getElement().setAttribute("theme", "primary");
            newButton.addClassName("view-toolbar__button");
            newButton.addClickListener(e -> dialog.open(service.newEntity(), AViewDialog.Operation.ADD));
            viewToolbar.add(newButton);
        }// end of if cycle

        add(viewToolbar);
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni ad uso esclusivo del developer
     */
    protected void creaDeveloperAlert() {
    }// end of method


    /**
     * Costruisce un (eventuale) layout con bottoni aggiuntivi
     * Facoltativo (assente di default); eventualmente sovrascritto
     */
    protected void creaTopLayout() {
        topLayout = new HorizontalLayout();
        this.add(topLayout);
    }// end of method


    /**
     * Eventuale caption sopra la grid
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected VerticalLayout creaCaption(String testo) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setMargin(false);
        layout.setSpacing(false);
        testo += getHeaderText(0);

        layout.add(new Label(testo));
        this.addCaption(layout);

        if (usaCaption) {
            this.add(layout);
        }// end of if cycle

        return layout;
    }// end of method


    /**
     * Crea il corpo centrale della view
     * Componente grafico obbligatorio
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto)
     */
    protected void creaGrid() {
        FlexLayout layout = new FlexLayout();
//        layout.setHeight("30em");

        //--Costruisce una lista di nomi delle properties della Grid nell'ordine:
        //--1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
        //--2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
        //--3) Sovrascrive la lista nella sottoclasse specifica di xxxService
        List<String> gridPropertyNamesList = service != null ? service.getGridPropertyNamesList() : null;

        if (entityClazz != null && AEntity.class.isAssignableFrom(entityClazz)) {
            try { // prova ad eseguire il codice
                grid = new Grid(entityClazz);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return;
            }// fine del blocco try-catch
        } else {
            grid = new Grid();
        }// end of if/else cycle

        for (Grid.Column column : grid.getColumns()) {
            grid.removeColumn(column);
        }// end of for cycle

        if (gridPropertyNamesList != null) {
            for (String propertyName : gridPropertyNamesList) {
                column.create(grid, entityClazz, propertyName);
            }// end of for cycle
        }// end of if cycle

        //--Aggiunge eventuali colonne calcolate
        addSpecificColumns();

        //--Apre il dialog di detail
        this.addDetailDialog();

        grid.setWidth("50em");
        grid.setHeightByRows(true);
        grid.addClassName("pippoz");
        grid.getElement().setAttribute("theme", "row-dividers");
        layout.add(grid);
        this.add(layout);
        layout.setFlexGrow(1, grid);
        this.setFlexGrow(1, layout);

        fixHeader();
    }// end of method

    /**
     * Eventuale header text
     */
    protected void fixHeader() {
        HeaderRow topRow = grid.prependHeaderRow();
        Grid.Column[] matrix = array.getColumnArray(grid);
        HeaderRow.HeaderCell informationCell = topRow.join(matrix);
        headerHolder = new Label("x");
        informationCell.setComponent(headerHolder);
    }// end of method


    /**
     * Eventuale header text
     */
    protected String getHeaderText() {
        return getHeaderText(service != null ? service.count() : 0);
    }// end of method


    /**
     * Eventuale aggiunta alla caption sopra la grid
     */
    protected String getHeaderText(int count) {
        String testo = entityClazz != null ? entityClazz.getSimpleName() + " - " : "";

        switch (count) {
            case 0:
                testo += "Al momento non ci sono elementi in questa collezione";
                break;
            case 1:
                testo += "Collezione con un solo elemento";
                break;
            default:
                testo += "Collezione di " + text.format(count) + " elementi";
                break;
        } // end of switch statement

        return testo;
    }// end of method

    /**
     * Eventuale aggiunta alla caption sopra la grid
     */
    protected VerticalLayout addCaption(VerticalLayout layout) {

        if (isEntityDeveloper) {
            layout.add(new Label("Lista visibile solo al developer"));
        }// end of if cycle

        if (isEntityEmbadded) {
            layout.add(new Label("Questa lista non dovrebbe mai essere usata (serve come test o per le sottoclassi specifiche)"));
            layout.add(new Label("L'entity è 'embedded' nelle collezioni che la usano (no @Annotation property DbRef)"));
            layout.add(new Label("Allo startup del programma, sono stati creati alcuni elementi di prova"));
        }// end of if cycle

        return layout;
    }// end of method


    /**
     * Aggiunge eventuali colonne calcolate
     */
    protected void addSpecificColumns() {
    }// end of method

    /**
     * Apre il dialog di detail
     */
    private void addDetailDialog() {
        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        if (usaBottoneEdit) {
            ComponentRenderer renderer = new ComponentRenderer<>(this::createEditButton);
            grid.addColumn(renderer);
            this.setFlexGrow(0);
        } else {
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            AViewDialog.Operation operation = isEntityModificabile ? AViewDialog.Operation.EDIT : AViewDialog.Operation.SHOW;
            grid.addSelectionListener(evento -> apreDialogo((SingleSelectionEvent) evento, operation));
        }// end of if/else cycle
    }// end of method


    protected Button createEditButton(AEntity entityBean) {
        Button edit = new Button(testoBottoneEdit, event -> dialog.open(entityBean, AViewDialog.Operation.EDIT));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        return edit;
    }// end of method

    private void apreDialogo(SingleSelectionEvent evento, AViewDialog.Operation operation) {
        if (evento != null && evento.getOldValue() != evento.getValue()) {
            if (evento.getValue().getClass().getName().equals(entityClazz.getName())) {
                dialog.open((AEntity) evento.getValue(), operation);
            }// end of if cycle
        }// end of if cycle
    }// end of method

    /**
     * Costruisce un (eventuale) layout con bottoni aggiuntivi
     * Facoltativo (assente di default); eventualmente sovrascritto
     */
    protected void creaBottomLayout() {
        bottomLayout = new HorizontalLayout();
        this.add(bottomLayout);
    }// end of method


    protected void creaFooter() {
        if (footer != null) {
            this.add(footer.setAppMessage(""));
        }// end of if cycle
    }// end of method


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.updateView();
    }// end of method


    protected void save(AEntity entityBean, AViewDialog.Operation operation) {
        entityBean = service.beforeSave(entityBean, operation);
        switch (operation) {
            case ADD:
                if (service.isEsisteEntityKeyUnica(entityBean)) {
                    Notification.show(entityBean + " non è stata registrata, perché esisteva già con lo stesso code ", 3000, Notification.Position.BOTTOM_START);
                } else {
                    service.save(entityBean);
                    updateView();
                    Notification.show(entityBean + " successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
                }// end of if/else cycle
                break;
            case EDIT:
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


    public void updateView() {
        Collection items = service != null ? service.findAll() : null;


        if (items != null) {
            try { // prova ad eseguire il codice
                grid.deselectAll();
                grid.setItems(items);
                headerHolder.setText(getHeaderText());
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

    }// end of method


    @Override
    public String getName() {
//        return annotation.getViewName(this.getClass());
        return "";
    }// end of method

}// end of class
