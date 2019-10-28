package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.List;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 25-set-2019
 * Time: 18:39
 * <p>
 * Classe astratta per visualizzare la Grid <br>
 * La classe viene divisa verticalmente in alcune classi astratte, per 'leggerla' meglio (era troppo grossa) <br>
 * Nell'ordine (dall'alto):
 * - 1 APropertyViewList (che estende la classe Vaadin VerticalLayout) per elencare tutte le property usate <br>
 * - 2 AViewList con la business logic principale <br>
 * - 3 APrefViewList per regolare le preferenze ed i flags <br>
 * - 4 ALayoutViewList per regolare il layout <br>
 * - 5 AGridViewList per gestire la Grid <br>
 * - 6 APaginatedGridViewList (opzionale) per gestire una Grid specializzata (add-on) che usa le Pagine <br>
 * L'utilizzo pratico per il programmatore è come se fosse una classe sola <br>
 * <p>
 */
@Slf4j
public abstract class APaginatedGridViewList extends AGridViewList {

    /**
     * Griglia con paginazione <br>
     * Alcune regolazioni da preferenza o da parametro (bottone Edit, ad esempio) <br>
     */
    protected PaginatedGrid paginatedGrid;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param entityClazz modello-dati specifico di questo modulo
     */
    public APaginatedGridViewList(IAService service, Class<? extends AEntity> entityClazz) {
        super(service, entityClazz);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze specifiche di questa view <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse APrefViewList <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.usaBottoneEdit = false;
    }// end of method


    /**
     * Crea il corpo centrale della view <br>
     * Componente grafico obbligatorio <br>
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     * Costruisce la Grid con le colonne. Gli items vengono caricati in updateItems() <br>
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto) <br>
     */
    protected void creaBody() {
        gridPlaceholder = new VerticalLayout();
        gridPlaceholder.setMargin(false);
        gridPlaceholder.setSpacing(false);
        gridPlaceholder.setPadding(false);

        //--Costruisce una lista di nomi delle properties della Grid
        List<String> gridPropertyNamesList = getGridPropertyNamesList();

        PaginatedGrid local = creaGrid(gridPropertyNamesList);
        Object alfa = local;
        gridPlaceholder.add(local);
        gridPlaceholder.setFlexGrow(0);

        //--Regolazioni di larghezza
        //gridPlaceholder.setWidth(gridWith + "em");
        //gridPlaceholder.setFlexGrow(0);
        //gridPlaceholder.getElement().getStyle().set("background-color", "#ffaabb");//rosa

        //--eventuale barra di bottoni sotto la grid
        creaGridBottomLayout();
    }// end of method


    /**
     * Crea la grid <br>
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     * Costruisce la Grid con le colonne. Gli items vengono caricati in updateItems() <br>
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto) <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto <br>
     */
    protected PaginatedGrid creaGrid(List<String> gridPropertyNamesList) {
        if (entityClazz != null && AEntity.class.isAssignableFrom(entityClazz)) {
            try { // prova ad eseguire il codice
                //--Costruisce la Grid SENZA creare automaticamente le colonne
                //--Si possono così inserire colonne manuali prima e dopo di quelle automatiche
                creaGridPaginata();
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return null;
            }// fine del blocco try-catch
        } else {
            paginatedGrid = new PaginatedGrid();
        }// end of if/else cycle

        //--regola la gridPaginated in fixPreferenze() della sottoclasse

        //--Apre il dialog di detail
        //--Eventuale inserimento (se previsto nelle preferenze) del bottone Edit come prima colonna
        this.addDetailDialog();

        //--Eventuali colonne specifiche aggiunte PRIMA di quelle automatiche
        this.addSpecificColumnsBefore();

        //--Colonne normali indicate in @AIList(fields =... , aggiunte in automatico
        this.addColumnsGrid(gridPropertyNamesList);

        //--Eventuali colonne specifiche aggiunte DOPO quelle automatiche
        this.addSpecificColumnsAfter();

        // Sets the max number of items to be rendered on the grid for each page
        paginatedGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        paginatedGrid.setPageSize(limit);
        paginatedGrid.setHeightByRows(true);
        paginatedGrid.getElement().getStyle().set("background-color", "#aabbcc");

        fixGridHeader();

        paginatedGrid.addSelectionListener(new SelectionListener<Grid<AEntity>, AEntity>() {

            @Override
            public void selectionChange(SelectionEvent<Grid<AEntity>, AEntity> selectionEvent) {
                boolean enabled = selectionEvent != null && selectionEvent.getAllSelectedItems().size() > 0;
                sincroBottoniMenu(enabled);
            }// end of inner method
        });//end of lambda expressions and anonymous inner class

        return paginatedGrid;
    }// end of method


    /**
     * Crea la GridPaginata <br>
     * Per usare una GridPaginata occorre:
     * 1) la view xxxList deve estendere APaginatedGridViewList anziche AGridViewList <br>
     * 2) deve essere sovrascritto questo metodo nella classe xxxList <br>
     * 3) nel metodo sovrascritto va creata la PaginatedGrid 'tipizzata' con la entityClazz (Collection) specifica <br>
     * 4) il metodo sovrascritto DOPO deve invocare questo stesso superMetodo in APaginatedGridViewList <br>
     */
    protected void creaGridPaginata() {
        if (paginatedGrid != null) {
            // Sets how many pages should be visible on the pagination before and/or after the current selected page
            paginatedGrid.setPaginatorSize(1);
        }// end of if cycle
    }// end of method


    /**
     * Apre il dialog di detail <br>
     * Eventuale inserimento (se previsto nelle preferenze) del bottone Edit come prima colonna <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    @Override
    protected void addDetailDialog() {
        if (!usaPagination) {
            super.addDetailDialog();
            return;
        }// end of if cycle

        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        if (usaBottoneEdit) {
            ComponentRenderer renderer = new ComponentRenderer<>(this::createEditButton);
            Grid.Column colonna = paginatedGrid.addColumn(renderer);

            if (pref.isBool(FlowCost.USA_TEXT_EDIT_BUTTON)) {
                int lar = pref.getStr(FlowCost.FLAG_TEXT_EDIT).length();
                lar += 1;
                colonna.setWidth(lar + "em");
            } else {
                colonna.setWidth("3em");
            }// end of if/else cycle
            colonna.setFlexGrow(0);
        } else {
            paginatedGrid.setSelectionMode(Grid.SelectionMode.NONE);
            paginatedGrid.addItemDoubleClickListener(event -> apreDialogo((ItemDoubleClickEvent) event));
        }// end of if/else cycle
    }// end of method


    /**
     * Aggiunge in automatico le colonne previste in gridPropertyNamesList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    @Override
    protected void addColumnsGrid(List<String> gridPropertyNamesList) {
        if (!usaPagination) {
            super.addColumnsGrid(gridPropertyNamesList);
            return;
        }// end of if cycle

        if (paginatedGrid != null) {
            if (gridPropertyNamesList != null) {
                for (String propertyName : gridPropertyNamesList) {
                    columnService.create(paginatedGrid, entityClazz, propertyName, searchProperty);
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Eventuale header text <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    @Override
    protected void fixGridHeader() {
        if (!usaPagination) {
            super.fixGridHeader();
            return;
        }// end of if cycle

        try { // prova ad eseguire il codice
            HeaderRow topRow = paginatedGrid.prependHeaderRow();
            Grid.Column[] matrix = array.getColumnArray(paginatedGrid);
            HeaderRow.HeaderCell informationCell = topRow.join(matrix);
            headerGridHolder = new Label("x");
            informationCell.setComponent(headerGridHolder);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch
    }// end of method


    /**
     * Aggiorna gli items della Grid, utilizzando i filtri. <br>
     * Chiamato per modifiche effettuate ai filtri, popup, newEntity, deleteEntity, ecc... <br>
     * <p>
     * Sviluppato nella sottoclasse AGridViewList, oppure APaginatedGridViewList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    @Override
    public void updateGrid() {
        if (!usaPagination) {
            super.updateGrid();
            return;
        }// end of if cycle

        if (array.isValid(filtri)) {
            items = mongo.findAllByProperty(entityClazz, filtri);
        } else {
            items = service != null ? service.findAll() : null;
        }// end of if/else cycle

        if (items != null) {
            try { // prova ad eseguire il codice
                paginatedGrid.deselectAll();
                paginatedGrid.setItems(items);
                headerGridHolder.setText(getGridHeaderText());
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        creaAlertLayout();
    }// end of method

}// end of class
