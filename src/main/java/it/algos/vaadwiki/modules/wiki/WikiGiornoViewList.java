package it.algos.vaadwiki.modules.wiki;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoViewDialog;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwiki.upload.UploadGiornoMorto;
import it.algos.vaadwiki.upload.UploadGiornoNato;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadflow.application.FlowCost.TAG_GIO;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 19-gen-2019 11.33.37 <br>
 * <p>
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
@Route(value = TAG_WGIO, layout = MainLayout.class)
@Qualifier(TAG_WGIO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WikiGiornoViewList extends WikiViewList {

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;


    @Autowired
    private UploadGiornoNato uploadGiornoNato;

    @Autowired
    private UploadGiornoMorto uploadGiornoMorto;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public WikiGiornoViewList(@Qualifier(TAG_GIO) IAPresenter presenter, @Qualifier(TAG_GIO) IADialog dialog) {
        super(presenter, dialog);
        ((GiornoViewDialog) dialog).fixFunzioni(this::save, this::delete);
    }// end of Spring constructor




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

        uploadAllButton.addClickListener(e -> openUploadDialog("dei giorni"));
        sincroBottoniMenu(false);
    }// end of method


    /**
     * Crea la GridPaginata <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
     * Oppure queste possono essere fatte nella sottoclasse , se non sono standard <br>
     */
    protected void creaGridPaginata() {
        PaginatedGrid<Giorno> gridPaginated = new PaginatedGrid<Giorno>();
        super.grid = gridPaginated;
        super.creaGridPaginata();
    }// end of method


    /**
     * Aggiunge le colonne alla PaginatedGrid <br>
     * Sovrascritto (obbligatorio) <br>
     */
    protected void addColumnsGridPaginata() {
        fixColumn(Giorno::getOrdine, "ordine");
        fixColumn(Giorno::getMese, "mese");
        fixColumn(Giorno::getTitolo, "titolo");
    }// end of method


    /**
     * Costruisce la colonna in funzione della PaginatedGrid specifica della sottoclasse <br>
     * DEVE essere sviluppato nella sottoclasse, sostituendo AEntity con la classe effettiva  <br>
     */
    protected void fixColumn(ValueProvider<Giorno, ?> valueProvider, String propertyName) {
        Grid.Column singleColumn;
        singleColumn = ((PaginatedGrid<Giorno>) grid).addColumn(valueProvider);
        columnService.fixColumn(singleColumn, Giorno.class, propertyName);
    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        String lar = "7em";
        ComponentRenderer renderer;
        Grid.Column colonna;

        renderer = new ComponentRenderer<>(this::createViewNatoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Test");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createViewMortoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Test");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createWikiNatoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Wiki");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createWikiMortoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Wiki");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createUploadNatoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Upload");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createUploadMortoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Upload");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);
    }// end of method


    protected Button createViewNatoButton(Giorno entityBean) {
        uploadOneNatoButton = new Button("Nati", new Icon(VaadinIcon.LIST));
        uploadOneNatoButton.getElement().setAttribute("theme", "secondary");
        uploadOneNatoButton.addClickListener(e -> viewNato(entityBean));
        return uploadOneNatoButton;
    }// end of method


    protected Button createViewMortoButton(Giorno entityBean) {
        uploadOneMortoButton = new Button("Morti", new Icon(VaadinIcon.LIST));
        uploadOneMortoButton.getElement().setAttribute("theme", "secondary");
        uploadOneMortoButton.getElement().setAttribute("color", "green");
        uploadOneMortoButton.addClickListener(e -> viewMorto(entityBean));
        return uploadOneMortoButton;
    }// end of method


    protected Button createWikiNatoButton(Giorno entityBean) {
        Element input = ElementFactory.createInput();
        uploadOneNatoButton = new Button("Nati", new Icon(VaadinIcon.SERVER));
        uploadOneNatoButton.getElement().setAttribute("theme", "secondary");
        uploadOneNatoButton.getElement().getStyle().set("background-color", input.getProperty("green"));
        uploadOneNatoButton.addClickListener(e -> wikiPageNato(entityBean));
        return uploadOneNatoButton;
    }// end of method


    protected Button createWikiMortoButton(Giorno entityBean) {
        uploadOneMortoButton = new Button("Morti", new Icon(VaadinIcon.SERVER));
        uploadOneMortoButton.getElement().setAttribute("theme", "secondary");
//        uploadOneMortoButton.getElement().getClassList().add("green");
        uploadOneMortoButton.addClickListener(e -> wikiPageMorto(entityBean));
        return uploadOneMortoButton;
    }// end of method


    protected Button createUploadNatoButton(Giorno entityBean) {
        uploadOneNatoButton = new Button("Nati", new Icon(VaadinIcon.UPLOAD));
        uploadOneNatoButton.getElement().setAttribute("theme", "error");
        uploadOneNatoButton.addClickListener(e -> uploadGiornoNato.esegue(entityBean));
        return uploadOneNatoButton;
    }// end of method


    protected Button createUploadMortoButton(Giorno entityBean) {
        uploadOneMortoButton = new Button("Morti", new Icon(VaadinIcon.UPLOAD));
        uploadOneMortoButton.getElement().setAttribute("theme", "error");
        uploadOneMortoButton.addClickListener(e -> uploadGiornoMorto.esegue(entityBean));
        return uploadOneMortoButton;
    }// end of method


    protected void viewNato(Giorno giorno) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_GIORNO_NATI + "/" + giorno.id));
    }// end of method


    protected void viewMorto(Giorno giorno) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_GIORNO_MORTI + "/" + giorno.id));
    }// end of method


    protected void wikiPageNato(Giorno giorno) {
        String link = "\"" + PATH_WIKI + uploadGiornoNato.getTitoloPagina(giorno) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void wikiPageMorto(Giorno giorno) {
        String link = "\"" + PATH_WIKI + uploadGiornoMorto.getTitoloPagina(giorno) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void uploadEffettivo() {
        uploadGiornoNato.esegueAll();
    }// end of method


}// end of class