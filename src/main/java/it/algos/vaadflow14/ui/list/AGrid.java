package it.algos.vaadflow14.ui.list;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: ven, 01-mag-2020
 * Time: 17:39
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AGrid {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AColumnService columnService;

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
    public AReflectionService reflection;

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
    public ATextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AMongoService mongo;

    protected AILogic entityLogic;

    protected List<String> gridPropertyNamesList;

    protected Class<? extends AEntity> entityClazz;

    protected Span headerLabelPlaceHolder;

    protected Map<String, Grid.Column<AEntity>> columnsMap;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected AArrayService array;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected AAnnotationService annotation;

    @Autowired
    private ADataProviderService dataProviderService;

    private Grid grid;


    public AGrid() {
    }


    public AGrid(Class<? extends AEntity> entityClazz) {
        super();
        this.grid = new Grid(entityClazz);
    }


    public AGrid(Class<? extends AEntity> entityClazz, AILogic entityLogic) {
        super();
        this.grid = new Grid(entityClazz, false);
        this.entityLogic = entityLogic;
        this.entityClazz = entityClazz;
    }


    /**
     * Questa classe viene costruita partendo da @Route e non da SprinBoot <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Prima viene chiamato il costruttore <br>
     * Prima viene chiamato init(); <br>
     * Viene chiamato @PostConstruct (con qualsiasi firma) <br>
     * Dopo viene chiamato setParameter(); <br>
     * Dopo viene chiamato beforeEnter(); <br>
     * <p>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     * Creazione e posizionamento dei componenti UI <br>
     * Possono essere sovrascritti nelle sottoclassi <br>
     */
    @PostConstruct
    protected void postConstruct() {
        grid.setHeightByRows(true);
        this.grid.setDataProvider(dataProviderService.creaDataProvider(entityClazz, null));
        grid.setHeight("100%");

        if (AEPreferenza.usaDebug.is()) {
            grid.getElement().getStyle().set("background-color", AEColor.blue.getEsadecimale());
        }

        //--Costruisce una lista di nomi delle properties della Grid
        gridPropertyNamesList = entityLogic != null ? entityLogic.getGridColumns() : null;

        if (gridPropertyNamesList == null || gridPropertyNamesList.size() == 0) {
            logger.error("Mancano le colonne della Grid", this.getClass(), "postConstruct");
            return;
        }

        //--Colonne normali indicate in @AIList(fields =... , aggiunte in automatico
        columnsMap = new HashMap<>();
        this.addColumnsGrid();
        this.creaGridHeader();
    }

    //    /**
    //     * Costruisce una lista di nomi delle properties <br>
    //     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
    //     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
    //     * 3) Sovrascrive il metodo getGridPropertyNamesList() nella sottoclasse specifica di xxxService <br>
    //     * Un eventuale modifica dell'ordine di presentazione delle colonne viene regolata nel metodo sovrascritto <br>
    //     */
    //    protected List<String> getGridPropertyNamesList() {
    //        List<String> gridPropertyNamesList = service != null ? service.getGridPropertyNamesList() : null;
    //        return gridPropertyNamesList;
    //    }


    /**
     * Aggiunge in automatico le colonne previste in gridPropertyNamesList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>@todo non è proprio cosi
     */
    protected void addColumnsGrid() {
        Grid.Column<AEntity> colonna = null;
        String indexWidth = VUOTA;

        //--se usa la numerazione automatica, questa occupa la prima colonna
        if (annotation.usaRowIndex(entityClazz)) {
//            indexWidth = annotation.getIndexWith(entityClazz);
            indexWidth = getWidth();
            grid.addColumn(item -> VUOTA).setKey(FIELD_INDEX).setHeader("#").setWidth(indexWidth).setFlexGrow(0);
        }

        //--se esiste la colonna 'ordine', la posiziono prima di un eventuale colonna col bottone 'edit'
        //--ed elimino la property dalla lista gridPropertyNamesList
        if (true) {
            if (reflection.isEsiste(entityClazz, FIELD_ORDINE)) {
                if (gridPropertyNamesList.contains(FIELD_ORDINE)) {
                    colonna = columnService.add(grid, entityClazz, FIELD_ORDINE);
                    if (colonna != null) {
                        columnsMap.put(FIELD_ORDINE, colonna);
                    }
                    gridPropertyNamesList.remove(FIELD_ORDINE);
                }
            }
        }

        //--Eventuale inserimento (se previsto nelle preferenze) del bottone Edit come seconda colonna (dopo ordinamento)
        //--Apre il dialog di detail
        //@todo PROVVISORIO
        //        if (((AILogic) entityLogic).usaBottoneEdit) {
        //            this.addDetailDialog();
        //        }

        //--costruisce in automatico tutte le colonne dalla lista gridPropertyNamesList
        if (gridPropertyNamesList != null) {
            for (String propertyName : gridPropertyNamesList) {
                colonna = columnService.add(grid, entityClazz, propertyName);
                if (colonna != null) {
                    columnsMap.put(propertyName, colonna);
                }
            }

            //            for (Object colonna : this.getColumns()) {
            //                ((Column) colonna).setAutoWidth(true);
            //            }// end of for cycle
            //            this.recalculateColumnWidths();

        }
    }

    /**
     * Larghezza della colonna di numerazione automatica in funzione della dimensione della collezione <br>
     * Larghezza aggiustata al massimo valore numerico <br>
     */
    protected String getWidth() {
        String indexWidth = VUOTA;
        int dim1 = 100;
        int dim2 = 1000;
        String tag1 = "2.5" + TAG_EM;
        String tag2 = "3.5" + TAG_EM;
        String tag3 = "4.5" + TAG_EM;

        int dim = mongo.count(entityClazz);

        if (dim < dim1) {
            indexWidth = tag1;
        }
        else {
            if (dim < dim2) {
                indexWidth = tag2;
            }
            else {
                indexWidth = tag3;
            }
        }

        return indexWidth;
    }

    /**
     * Apre il dialog di detail <br>
     * Eventuale inserimento (se previsto nelle preferenze) del bottone Edit come prima o ultima colonna <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    protected void addDetailDialog() {
        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
        if (true) {//@todo Funzionalità ancora da implementare
            ComponentRenderer renderer = new ComponentRenderer<>(this::createEditButton);
            Grid.Column colonna = grid.addColumn(renderer);

            colonna.setWidth("2.5em");
            colonna.setFlexGrow(0);
        }
        else {
            grid.setSelectionMode(Grid.SelectionMode.NONE);
            //            grid.addItemDoubleClickListener(event -> apreDialogo((ItemDoubleClickEvent) event));
        }
    }


    protected Button createEditButton(AEntity entityBean) {
        Button buttonEdit = new Button();
        String iconaTxt = "edit";

        buttonEdit.setIcon(new Icon("lumo", iconaTxt));
        buttonEdit.addClassName("review__edit");
        buttonEdit.getElement().setAttribute("theme", "tertiary");
        buttonEdit.setHeight("1em");
        //        buttonEdit.addClickListener(event -> entityLogic.performAction(AEAction.doubleClick, entityBean));@//@todo PROVVISORIO

        return buttonEdit;
    }


    public void setItems(Collection items) {

        //        grid.deselectAll();
        //        grid.setItems(items);
        grid.setHeight("100%");

        fixGridHeader(items);
    }


    /**
     * Aggiunta di tutti i listener <br>
     * Chiamato da AEntityService <br>
     * Aggiunge il listener alla riga, specificando l'azione di ritorno associata <br>
     *
     * @param entityLogic a cui rinviare l'evento/azione da eseguire
     */
    public void setAllListener(AILogic entityLogic) {
        this.entityLogic = entityLogic;

        if (annotation.usaRowIndex(entityClazz)) {
            grid.addAttachListener(event -> {
                grid.getColumnByKey(FIELD_INDEX).getElement().executeJs("this.renderer = function(root, column, rowData) {root.textContent = rowData.index + 1}");
            });
        }

        grid.addItemDoubleClickListener(event -> performAction((ItemClickEvent) event, AEAction.doubleClick));
    }


    //@todo Funzionalità eventualmente ancora da implementare
    public void detail(ItemDoubleClickEvent click) {
        String keyID = VUOTA;
        keyID = ((AEntity) click.getItem()).id;
        //        openDialogRoute(keyID);
    }


    /**
     * Esegue l'azione del bottone. <br>
     * <p>
     * Passa a AEntityService.performAction(azione) <br>
     *
     * @param azione da eseguire
     */
    public void performAction(ItemClickEvent event, AEAction azione) {
        AEntity entityBean = (AEntity) event.getItem();

        if (entityLogic != null) {
            entityLogic.performAction(azione, entityBean);
        }
    }


    /**
     * PlaceHolder per un eventuale header text <br>
     * Il PlaceHolder (una label) esiste SEMPRE. Il contenuto viene modificato da setItems() <br>
     */
    protected void creaGridHeader() {
        this.headerLabelPlaceHolder = new Span();

        try {
            HeaderRow topRow = grid.prependHeaderRow();
            Grid.Column[] matrix = array.getColumnArray(grid);

            if (matrix != null && matrix.length > 0) {
                HeaderRow.HeaderCell informationCell = topRow.join(matrix);
                informationCell.setComponent(headerLabelPlaceHolder);
            }
        } catch (Exception unErrore) {
            logger.error(unErrore.toString());
        }
    }


    /**
     * Eventuale header text <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    @Deprecated
    public void fixGridHeader(Collection items) {
        String message = VUOTA;

        if (true) {//@todo Funzionalità ancora da implementare con preferenza locale
            message = annotation.getTitleList(entityClazz).toUpperCase() + SEP;
            if (items != null && items.size() > 0) {
                if (items.size() == 1) {
                    message += "Lista di un solo elemento";
                }
                else {
                    message += "Lista di " + text.format(items.size() * 67) + " elementi";
                }
            }
            else {
                message += "Al momento non ci sono elementi in questa collezione";
            }

            if (headerLabelPlaceHolder != null) {
                headerLabelPlaceHolder.setText(message);
                headerLabelPlaceHolder.getElement().getStyle().set(AETypeColor.verde.getTag(), AETypeColor.verde.get());
                headerLabelPlaceHolder.getElement().getStyle().set(AETypeWeight.bold.getTag(), AETypeWeight.bold.get());
            }
        }
    }

    /**
     * Eventuale header text <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    public void fixGridHeader(int items) {
        String message = VUOTA;

        if (true) {//@todo Funzionalità ancora da implementare con preferenza locale
            message = annotation.getTitleList(entityClazz).toUpperCase() + SEP;
            if (items > 0) {
                if (items == 1) {
                    message += "Lista di un solo elemento";
                }
                else {
                    message += "Lista di " + text.format(items) + " elementi";
                }
            }
            else {
                message += "Al momento non ci sono elementi in questa collezione";
            }

            if (headerLabelPlaceHolder != null) {
                headerLabelPlaceHolder.setText(message);
                headerLabelPlaceHolder.getElement().getStyle().set(AETypeColor.verde.getTag(), AETypeColor.verde.get());
                headerLabelPlaceHolder.getElement().getStyle().set(AETypeWeight.bold.getTag(), AETypeWeight.bold.get());
            }
        }
    }

    //    /**
    //     * Esegue l'azione del bottone. <br>
    //     * <p>
    //     * Passa a AEntityService.performAction(azione) <br>
    //     *
    //     * @param azione da eseguire
    //     */
    //    public void performAction(AEAction azione, AEntity entityBean) {
    //        if (service != null) {
    //            service.performAction(azione, entityBean);
    //        }
    //    }


    public Grid getGrid() {
        return grid;
    }


    public void deselectAll() {
        grid.deselectAll();
    }


    public void refreshAll() {
        grid.getDataProvider().refreshAll();
    }


    public void setItems(List<Mese> items) {
        grid.setItems(items);
    }


}
