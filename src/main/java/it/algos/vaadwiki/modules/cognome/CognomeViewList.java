package it.algos.vaadwiki.modules.cognome;

import com.vaadin.flow.component.Component;
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
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.upload.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 14-giu-2019 16.34.34 <br>
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
@Route(value = TAG_COG, layout = MainLayout.class)
@Qualifier(TAG_COG)
@Slf4j
@AIScript(sovrascrivibile = true)
public class CognomeViewList extends AGridViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;


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

    //--Soglia minima per creare una entity nella collezione Nomi sul mongoDB
    //--Soglia minima per creare una pagina di un nome sul server wiki
    private int sogliaWiki;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public CognomeViewList(@Qualifier(TAG_COG) IAPresenter presenter, @Qualifier(TAG_COG) IADialog dialog) {
        super(presenter, dialog);
        ((CognomeViewDialog) dialog).fixFunzioni(this::save, this::delete);
    }// end of Spring constructor

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaSearchBottoneNew = false;
        super.usaBottoneDeleteAll = true;
        this.sogliaWiki = pref.getInt(SOGLIA_COGNOMI_PAGINA_WIKI, 50);
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

        Button creaButton = new Button("Crea all", new Icon(VaadinIcon.LIST));
        creaButton.addClassName("view-toolbar__button");
        creaButton.addClickListener(e -> {
            ((CognomeService) service).crea();
            updateItems();
            updateView();
        });//end of lambda expressions and anonymous inner class
        topPlaceholder.add(creaButton);

        Button updateButton = new Button("Elabora", new Icon(VaadinIcon.LIST));
        updateButton.addClassName("view-toolbar__button");
        updateButton.addClickListener(e -> {
            ((CognomeService) service).update();
            updateItems();
            updateView();
        });//end of lambda expressions and anonymous inner class
        topPlaceholder.add(updateButton);
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
        LocalDateTime lastDownload = pref.getDate(LAST_ELABORA_COGNOME);
        if (lastDownload != null) {
            label = new Label("Ultimo aggiornamento dei cognomi il " + date.getTime(lastDownload));
        } else {
            label = new Label("I cognomi non sono aggiornati ");
        }// end of if/else cycle

        alertPlacehorder.add(label);
    }// end of method


    /**
     * Crea la GridPaginata <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
     * Oppure queste possono essere fatte nella sottoclasse , se non sono standard <br>
     */
    protected void creaGridPaginata() {
        PaginatedGrid<Nome> gridPaginated = new PaginatedGrid<Nome>();
        super.grid = gridPaginated;
        super.creaGridPaginata();
    }// end of method


    /**
     * Aggiunge le colonne alla PaginatedGrid <br>
     * Sovrascritto (obbligatorio) <br>
     */
    protected void addColumnsGridPaginata() {
        fixColumn(Cognome::getCognome, "cognome");
        fixColumn(Cognome::getVoci, "voci");
    }// end of method


    /**
     * Costruisce la colonna in funzione della PaginatedGrid specifica della sottoclasse <br>
     * DEVE essere sviluppato nella sottoclasse, sostituendo AEntity con la classe effettiva  <br>
     */
    protected void fixColumn(ValueProvider<Cognome, ?> valueProvider, String propertyName) {
        Grid.Column singleColumn;
        singleColumn = ((PaginatedGrid<Cognome>) grid).addColumn(valueProvider);
        columnService.fixColumn(singleColumn, Cognome.class, propertyName);
    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        String lar = "9em";
        ComponentRenderer renderer;
        Grid.Column colonna;

        renderer = new ComponentRenderer<>(this::createViewButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Test");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);


        renderer = new ComponentRenderer<>(this::createWikiButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Wiki");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);


        renderer = new ComponentRenderer<>(this::createUploaButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Upload");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);
    }// end of method

    protected Button createViewButton(Cognome entityBean) {
        Button viewButton = new Button(entityBean.cognome, new Icon(VaadinIcon.LIST));
        viewButton.getElement().setAttribute("theme", "secondary");
        viewButton.addClickListener(e -> viewCognome(entityBean));
        return viewButton;
    }// end of method


    protected Component createWikiButton(Cognome entityBean) {
        if (entityBean != null && entityBean.voci >= sogliaWiki) {
            Button uploadOneNatoButton = new Button(entityBean.cognome, new Icon(VaadinIcon.SERVER));
            uploadOneNatoButton.getElement().setAttribute("theme", "secondary");
            uploadOneNatoButton.addClickListener(e -> wikiPage(entityBean));
            return uploadOneNatoButton;
        } else {
            return new Label("");
        }// end of if/else cycle
    }// end of method


    protected Component createUploaButton(Cognome entityBean) {
        if (entityBean != null && entityBean.voci >= sogliaWiki) {
            Button uploadOneNatoButton = new Button(entityBean.cognome, new Icon(VaadinIcon.UPLOAD));
            uploadOneNatoButton.getElement().setAttribute("theme", "error");
            uploadOneNatoButton.addClickListener(e -> uploadService.uploadCognome(entityBean));
            return uploadOneNatoButton;
        } else {
            return new Label("");
        }// end of if/else cycle
    }// end of method

    protected void viewCognome(Cognome cognome) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_COGNOMI + "/" + cognome.id));
    }// end of method


    protected void wikiPage(Cognome cognome) {
        String link = "\"" + PATH_WIKI + uploadService.getTitoloCognome(cognome) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method

}// end of class