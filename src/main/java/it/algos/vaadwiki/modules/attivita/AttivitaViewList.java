package it.algos.vaadwiki.modules.attivita;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SingleSelectionEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwiki.WikiLayout;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatViewList;
import it.algos.vaadwiki.schedule.TaskAttivita;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import javax.annotation.PostConstruct;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 5-ott-2018 12.04.32 <br>
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
@Route(value = TAG_ATT, layout = MainLayout.class)
@Qualifier(TAG_ATT)
@Slf4j
@AIScript(sovrascrivibile = false)
public class AttivitaViewList extends AttNazProfCatViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.BOAT;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskAttivita taskAttivita;

    private PaginatedGrid<Attivita> gridPaginated;

    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public AttivitaViewList(@Qualifier(TAG_ATT) IAPresenter presenter, @Qualifier(TAG_ATT) IADialog dialog) {
        super(presenter, dialog);
        ((AttivitaViewDialog) dialog).fixFunzioni(this::save, this::delete);
    }// end of Spring constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenzeSpecifiche() {
        super.fixPreferenzeSpecifiche();
        super.titoloModulo = service.titoloModuloAttivita;
        super.titoloPaginaStatistiche = service.titoloPaginaStatisticheAttivita;
        super.task = taskAttivita;
        super.usaPagination = true;
        super.codeFlagDownload = USA_DAEMON_ATTIVITA;
        super.codeLastDownload = LAST_DOWNLOAD_ATTIVITA;
        super.durataLastDownload = DURATA_DOWNLOAD_ATTIVITA;
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

        gridPaginated.addColumn(Attivita::getSingolare).setHeader("Singolare").setFlexGrow(0).setWidth("25em");
        gridPaginated.addColumn(Attivita::getPlurale).setHeader("Plurale");

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

}// end of class