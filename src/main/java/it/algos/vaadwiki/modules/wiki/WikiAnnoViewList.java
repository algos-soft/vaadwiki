package it.algos.vaadwiki.modules.wiki;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SingleSelectionEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoViewDialog;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoViewDialog;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwiki.upload.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;
import static it.algos.vaadflow.application.FlowCost.TAG_GIO;
import static it.algos.vaadwiki.application.WikiCost.TAG_WANN;
import static it.algos.vaadwiki.application.WikiCost.TAG_WGIO;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 17:17
 */
@UIScope
@Route(value = TAG_WANN, layout = MainLayout.class)
@Qualifier(TAG_WANN)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WikiAnnoViewList extends WikiViewList {

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;


    @Autowired
    private UploadAnni uploadAnni;

    @Autowired
    private UploadAnnoNato uploadAnnoNato;

    @Autowired
    private UploadAnnoMorto uploadAnnoMorto;

    private Anno annoCorrente;

    private PaginatedGrid<Anno> gridPaginated;

    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public WikiAnnoViewList(@Qualifier(TAG_ANN) IAPresenter presenter, @Qualifier(TAG_ANN) IADialog dialog) {
        super(presenter, dialog);
        ((AnnoViewDialog) dialog).fixFunzioni(this::save, this::delete);
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
    protected boolean creaTopLayout() {
        super.creaTopLayout();

        uploadAllButton.addClickListener(e -> openUploadDialog("degli anni"));

        sincroBottoniMenu(false);
        return topPlaceholder.getComponentCount() > 0;
    }// end of method


    /**
     * Prova a creare la grid paginata (secondo il flag)
     * Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse
     * Nella sottoclasse specifica vanno aggiunte le colonne che non si riesce ad aggiungere in automatico
     * Componente grafico obbligatorio
     * Costruisce la Grid con le colonne. Gli items vengono caricati in updateView()
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto)
     */
    protected void updateGridPaginata() {
        FlexLayout layout = new FlexLayout();
        gridPaginated = new PaginatedGrid<>();
        super.gridPaginataBefore();

        gridPaginated.addColumn(Anno::getOrdine).setHeader("#").setFlexGrow(0).setWidth("5em");
        gridPaginated.addColumn(Anno::getSecolo).setHeader("Secolo").setFlexGrow(0).setWidth("10em");
        gridPaginated.addColumn(Anno::getTitolo).setHeader("Titolo").setFlexGrow(0).setWidth("10em");

        addSpecificColumnsAfter();

        super.gridPaginataAfter();

        gridPaginated.setItems(items);

        // Sets the max number of items to be rendered on the grid for each page
        gridPaginated.setPageSize(15);

        // Sets how many pages should be visible on the pagination before and/or after the current selected page
        gridPaginated.setPaginatorSize(1);

        gridHolder.add(gridPaginated);
        gridHolder.setFlexGrow(1, gridPaginated);
    }// end of method


    /**
     * Apre il dialog di detail
     */
    protected void addDetailDialog() {
        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        if (usaBottoneEdit) {
            ComponentRenderer renderer = new ComponentRenderer<>(this::createEditButton);
            Grid.Column colonna = gridPaginated.addColumn(renderer);
            colonna.setWidth("6em");
            colonna.setFlexGrow(0);
        } else {
            EAOperation operation = isEntityModificabile ? EAOperation.edit : EAOperation.showOnly;
            grid.addSelectionListener(evento -> apreDialogo((SingleSelectionEvent) evento, operation));
        }// end of if/else cycle
    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        ComponentRenderer renderer;
        Grid.Column colonna;

        renderer = new ComponentRenderer<>(this::createUploadNatoButton);
        colonna = gridPaginated.addColumn(renderer);
        colonna.setWidth("10em");
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createUploadMortoButton);
        colonna = gridPaginated.addColumn(renderer);
        colonna.setWidth("10em");
        colonna.setFlexGrow(0);
    }// end of method

    protected Button createUploadNatoButton(Anno entityBean) {
        uploadOneNatoButton = new Button("Upload nati", new Icon(VaadinIcon.UPLOAD));
        uploadOneNatoButton.getElement().setAttribute("theme", "error");
        uploadOneNatoButton.addClickListener(e ->  uploadAnnoNato.esegue(entityBean));
        return uploadOneNatoButton;
    }// end of method


    protected Button createUploadMortoButton(Anno entityBean) {
        uploadOneNatoButton = new Button("Upload morti", new Icon(VaadinIcon.UPLOAD));
        uploadOneNatoButton.getElement().setAttribute("theme", "error");
        uploadOneNatoButton.addClickListener(e ->  uploadAnnoMorto.esegue(entityBean));
        return uploadOneNatoButton;
    }// end of method

    /**
     * Eventuale header text
     */
    protected void fixGridHeader(String messaggio) {
        try { // prova ad eseguire il codice
            HeaderRow topRow = gridPaginated.prependHeaderRow();
            Grid.Column[] matrix = array.getColumnArray(gridPaginated);
            HeaderRow.HeaderCell informationCell = topRow.join(matrix);
            Label testo = new Label(messaggio);
            informationCell.setComponent(testo);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch
    }// end of method

    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void uploadEffettivo() {
        uploadAnni.esegueAll();
    }// end of method


}// end of class