package it.algos.vaadwiki.modules.nome;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.wiki.WikiList;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.statistiche.StatisticheNomiA;
import it.algos.vaadwiki.statistiche.StatisticheNomiB;
import it.algos.vaadwiki.upload.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 29-mag-2019 19.55.00 <br>
 * <br>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * <p>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le istanze @Autowired usate da questa classe vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired di questa classe, oppure <br>
 * 2) la property è di una classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON), oppure <br>
 * 3) vengono usate in un un metodo @PostConstruct di questa classe, perché SpringBoot le inietta DOPO init() <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @UIScope (obbligatorio) <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@UIScope
@Route(value = TAG_NOM, layout = MainLayout.class)
@Qualifier(TAG_NOM)
@Slf4j
@AIScript(sovrascrivibile = true)
public class NomeList extends WikiList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    private final static String DIM = "Dimensioni";

    private final static String ALF = "Alfabetico";

    private final static String DOP = "Nomi doppi";

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected LibBio libBio;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected UploadService uploadService;

    private AComboBox<String> comboFiltro; //@todo da modificare dopo l'upgrade del programma con vaadflow

    //--Soglia minima per creare una entity nella collezione Nomi sul mongoDB
    //--Soglia minima per creare una pagina di un nome sul server wiki
    private int sogliaWiki;


//    /**
//     * Costruttore @Autowired <br>
//     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
//     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
//     *
//     * @param presenter per gestire la business logic del package
//     * @param dialog    per visualizzare i fields
//     */
//    @Autowired
//    public NomeViewList(@Qualifier(TAG_NOM) IAPresenter presenter, @Qualifier(TAG_NOM) IADialog dialog) {
//        super(presenter, dialog);
//        ((NomeViewDialog) dialog).fixFunzioni(this::save, this::delete);
//    }// end of Spring constructor

    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa alla superclasse il service iniettato qui da Vaadin/@Route <br>
     * Passa alla superclasse anche la entityClazz che viene definita qui (specifica di questo modulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    @Autowired
    public NomeList(@Qualifier(TAG_NOM) IAService service) {
        super(service, Nome.class);
    }// end of Vaadin/@Route constructor

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

//        super.usaSearchBottoneNew = false; //@todo versione 14
        super.usaBottoneDeleteAll = true;
        this.sogliaWiki = pref.getInt(SOGLIA_NOMI_PAGINA_WIKI, 50);

        this.usaCreaButton = true;
        this.usaUpdateButton = false;
        this.usaStatistiche2Button = true;
        super.titoloPaginaStatistiche = ((NomeService) service).TITOLO_PAGINA_WIKI;
        super.titoloPaginaStatistiche2 = ((NomeService) service).TITOLO_PAGINA_WIKI_2;
        super.usaBottoneUpload = true;

    }// end of method


    /**
     * Placeholder (eventuale, presente di default) SOPRA la Grid
     * - con o senza campo edit search, regolato da preferenza o da parametro
     * - con o senza bottone New, regolato da preferenza o da parametro
     * - con eventuali altri bottoni specifici
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void creaTopLayout() {
        super.creaTopLayout();

        topPlaceholder.add(creaPopup());

        Button testButton = new Button("Test", new Icon(VaadinIcon.SEARCH));
        testButton.addClassName("view-toolbar__button");
        testButton.addClickListener(e -> test());
        topPlaceholder.add(testButton);

//        uploadAllButton.addClickListener(e -> openUploadDialog("dei nomi")); //@todo versione 14
        sincroBottoniMenu(false);
    }// end of method


    private Component creaPopup() {
        ArrayList<String> items = new ArrayList<>();
        items.add(DIM);
        items.add(ALF);
        items.add(DOP);
        comboFiltro = new AComboBox();
        comboFiltro.setWidth("10em");
        comboFiltro.setItems(items);
        comboFiltro.setValue(DIM);
        comboFiltro.addValueChangeListener(event -> modificaFiltro(event));

        return comboFiltro;
    }// end of method


    private void modificaFiltro(HasValue.ValueChangeEvent event) {
        String value = (String) event.getValue();

        switch (value) {
            case DIM:
                items = ((NomeService) service).findAllDimensioni();
                break;
            case ALF:
                items = ((NomeService) service).findAllAlfabetico();
                break;
            case DOP:
                items = ((NomeService) service).findAllNomiDoppi();
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement
        updateView();
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();

        Label label = null;
        LocalDateTime lastDownload = pref.getDate(LAST_ELABORA_NOME);
        if (lastDownload != null) {
            label = new Label("Ultimo aggiornamento dei nomi il " + date.getTime(lastDownload));
        } else {
            label = new Label("I nomi non sono aggiornati ");
        }// end of if/else cycle

        alertPlacehorder.add(label);
    }// end of method


//    /**
//     * Crea la GridPaginata <br>
//     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
//     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
//     * Oppure queste possono essere fatte nella sottoclasse , se non sono standard <br>
//     */
//    protected void creaGridPaginata() {
//        PaginatedGrid<Nome> gridPaginated = new PaginatedGrid<Nome>();
//        super.grid = gridPaginated;
//        super.creaGridPaginata();
//    }// end of method
//
//
//    /**
//     * Aggiunge le colonne alla PaginatedGrid <br>
//     * Sovrascritto (obbligatorio) <br>
//     */
//    protected void addColumnsGridPaginata() {
//        fixColumn(Nome::getNome, "nome");
//        fixColumn(Nome::getVoci, "voci");
//        fixColumn(Nome::isValido, "valido");
//        fixColumn(Nome::isDoppio, "doppio");
//    }// end of method
//
//
//    /**
//     * Costruisce la colonna in funzione della PaginatedGrid specifica della sottoclasse <br>
//     * DEVE essere sviluppato nella sottoclasse, sostituendo AEntity con la classe effettiva  <br>
//     */
//    protected void fixColumn(ValueProvider<Nome, ?> valueProvider, String propertyName) {
//        Grid.Column singleColumn;
//        singleColumn = ((PaginatedGrid<Nome>) grid).addColumn(valueProvider);
//        columnService.fixColumn(singleColumn, Nome.class, propertyName);
//    }// end of method


    /**
     * Crea la GridPaginata <br>
     * Per usare una GridPaginata occorre:
     * 1) la view xxxList deve estendere APaginatedGridViewList anziche AGridViewList <br>
     * 2) deve essere sovrascritto questo metodo nella classe xxxList <br>
     * 3) nel metodo sovrascritto va creata la PaginatedGrid 'tipizzata' con la entityClazz (Collection) specifica <br>
     * 4) il metodo sovrascritto deve invocare DOPO questo stesso superMetodo in APaginatedGridViewList <br>
     */
    @Override
    protected void creaGridPaginata() {
        paginatedGrid = new PaginatedGrid<Nome>();
        super.creaGridPaginata();
    }// end of method

    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        String lar = "12em";
        ComponentRenderer renderer;
        Grid.Column colonna;

        renderer = new ComponentRenderer<>(this::createViewButton);
        colonna = paginatedGrid.addColumn(renderer);
        colonna.setHeader("Test");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);


        renderer = new ComponentRenderer<>(this::createWikiButton);
        colonna = paginatedGrid.addColumn(renderer);
        colonna.setHeader("Wiki");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);


        renderer = new ComponentRenderer<>(this::createUploaButton);
        colonna = paginatedGrid.addColumn(renderer);
        colonna.setHeader("Upload");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

    }// end of method


    protected Button createViewButton(Nome entityBean) {
        Button viewButton = new Button(entityBean.nome, new Icon(VaadinIcon.LIST));

        if (entityBean.nome.contains(" ") && entityBean.doppio == false) {
            viewButton.getElement().setAttribute("theme", "error");
        } else {
            viewButton.getElement().setAttribute("theme", "secondary");
        }// end of if/else cycle

        viewButton.addClickListener(e -> viewNome(entityBean));
        return viewButton;
    }// end of method


    protected Component createWikiButton(Nome entityBean) {
        if (entityBean != null && entityBean.voci >= sogliaWiki) {
            Button wikiButton = new Button(entityBean.nome, new Icon(VaadinIcon.SERVER));
            if (entityBean.nome.contains(" ") && entityBean.doppio == false) {
                wikiButton.getElement().setAttribute("theme", "error");
            } else {
                wikiButton.getElement().setAttribute("theme", "secondary");
            }// end of if/else cycle
            wikiButton.addClickListener(e -> wikiPage(entityBean));
            return wikiButton;
        } else {
            return new Label("");
        }// end of if/else cycle
    }// end of method


    protected Component createUploaButton(Nome entityBean) {
        if (entityBean != null && entityBean.voci >= sogliaWiki && entityBean.valido) {
            Button uploadOneNatoButton = new Button(entityBean.nome, new Icon(VaadinIcon.UPLOAD));
            uploadOneNatoButton.getElement().setAttribute("theme", "error");
            uploadOneNatoButton.addClickListener(e -> uploadService.uploadNome(entityBean));
            return uploadOneNatoButton;
        } else {
            return new Label("");
        }// end of if/else cycle
    }// end of method


    protected void viewNome(Nome nome) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_NOMI + "/" + nome.id));
    }// end of method


    protected void wikiPage(Nome nome) {
        String link = "\"" + PATH_WIKI + uploadService.getTitoloNome(nome) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void uploadEffettivo() {
        uploadService.uploadAllNomi();
    }// end of method


    protected void uploadStatistiche() {
        appContext.getBean(StatisticheNomiA.class);
        appContext.getBean(StatisticheNomiB.class);
    }// end of method


    /**
     * Stampa nella console la lista di tutti i nomi, nell'ordine selezionato <br>
     */
    public void test() {
        String ordine = comboFiltro.getValore();

        if (items != null) {
            switch (ordine) {
                case DIM:
                    for (Nome nome : (List<Nome>) items) {
                        System.out.println(nome.voci + " - " + nome.nome);
                    }// end of for cycle
                    break;
                case ALF:
                case DOP:
                    for (Nome nome : (List<Nome>) items) {
                        System.out.println(nome.nome + " - " + nome.voci);
                    }// end of for cycle
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle
    }// end of method

}// end of class