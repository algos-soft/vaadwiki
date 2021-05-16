package it.algos.vaadflow14.ui.list;

import com.mongodb.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
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

    /**
     * Filtri per dataProvider <br>
     */
    protected Map<String, AFiltro> mappaFiltri;

    private AILogic entityLogic;

    private List<String> gridPropertyNamesList;

    private Class<? extends AEntity> entityClazz;

    private Span headerLabelPlaceHolder;

    private Map<String, Grid.Column<AEntity>> columnsMap;

    private DataProvider dataProvider;

    @Autowired
    private ADataProviderService dataProviderService;

    private Grid grid;

    @Deprecated
    public AGrid() {
    }

    @Deprecated
    public AGrid(Class<? extends AEntity> entityClazz) {
        super();
        this.grid = new Grid(entityClazz);
    }


    /**
     * Costruttore con parametri <br>
     * Questa istanza viene costruita partendo da LogicList nel metodo fixBodyLayout() <br>
     * con -> grid = appContext.getBean(AGrid.class, entityClazz, this, filtri); <br>
     * <p>
     * La Grid viene costruita e regolata in postConstruct() <br>
     * I filtri e la relativa dataProvider possono essere modificati in maniera dinamica <br>
     *
     * @param entityClazz (obbligatorio)  the class of type AEntity
     * @param entityLogic (obbligatorio) riferimento alla istanza (prototype) di LogicList che crea questa Grid
     * @param mappaFiltri (obbligatorio)  per selezione e ordinamento
     */
    public AGrid(final Class<? extends AEntity> entityClazz, final AILogic entityLogic, Map<String, AFiltro> mappaFiltri) {
        super();
        this.entityClazz = entityClazz;
        this.entityLogic = entityLogic;
        this.mappaFiltri = mappaFiltri;
    }


    /**
     * Questa istanza viene costruita partendo da LogicList <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Prima viene chiamato il costruttore <br>
     * Prima viene chiamato init(); <br>
     * Viene chiamato @PostConstruct (con qualsiasi firma) <br>
     */
    @PostConstruct
    protected void postConstruct() {
        grid = new Grid(entityClazz, false);
        grid.setHeightByRows(true);

        String sortProperty = annotation.getSortProperty(entityClazz);
        BasicDBObject sort = new BasicDBObject(sortProperty, 1);
        dataProvider = dataProviderService.creaDataProvider(entityClazz, mappaFiltri);
        grid.setDataProvider(dataProvider);

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


    /**
     * Aggiunge in automatico le colonne previste in gridPropertyNamesList <br>
     */
    protected void addColumnsGrid() {
        Grid.Column<AEntity> colonna = null;
        String indexWidth = VUOTA;
        boolean esisteColonnaOrdine = reflection.isEsiste(entityClazz, FIELD_ORDINE);

        //--se usa la numerazione automatica, questa occupa la prima colonna
        //--la numerazione automatica, anche se erroneamente prevista nella @AIList della AEntity, non viene attivata se esiste già la colonna numerica 'ordine'
        if (annotation.usaRowIndex(entityClazz) && !esisteColonnaOrdine) {
            indexWidth = getWidth();
            grid.addColumn(item -> VUOTA).setKey(FIELD_INDEX).setHeader("#").setWidth(indexWidth).setFlexGrow(0);
        }

        //--se esiste la colonna 'ordine', la posiziono prima di un eventuale colonna col bottone 'edit'
        //--ed elimino la property dalla lista gridPropertyNamesList
        if (esisteColonnaOrdine) {
            if (gridPropertyNamesList.contains(FIELD_ORDINE)) {
                colonna = columnService.add(grid, entityClazz, FIELD_ORDINE);
                if (colonna != null) {
                    columnsMap.put(FIELD_ORDINE, colonna);
                }
                gridPropertyNamesList.remove(FIELD_ORDINE);
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

    @Deprecated
    public void setItems(Collection items) {

        //        grid.deselectAll();
        //        grid.setItems(items);
        grid.setHeight("100%");

        fixGridHeader();
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

        if (annotation.usaRowIndex(entityClazz) && !reflection.isEsiste(entityClazz, FIELD_ORDINE)) {
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
     * DataProvider è già filtrato (a monte) e vuole una query nulla <br>
     */
    public void fixGridHeader() {
        int totRec = mongo.count(entityClazz);
        int itemsFiltrati = grid.getDataProvider().size(null);
        String message = VUOTA;

        if (true) {//@todo Funzionalità ancora da implementare con preferenza locale
            message = annotation.getTitleList(entityClazz).toUpperCase() + SEP;
            if (itemsFiltrati > 0) {
                if (itemsFiltrati == 1) {
                    message += "Lista di un solo elemento";
                }
                else {
                    if (itemsFiltrati == totRec) {
                        message += String.format("Lista di %s elementi", text.format(itemsFiltrati));
                    }
                    else {
                        message += String.format("Lista filtrata di %s elementi su %s totali", text.format(itemsFiltrati), text.format(totRec));
                    }
                }
            }
            else {
                if (totRec > 0) {
                    message += String.format("Lista filtrata senza elementi su %s totali", text.format(totRec));
                }
                else {
                    message += "Lista senza elementi";
                }
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

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider dataProvider) {
        grid.setDataProvider(dataProvider);
    }

    public void setItems(List<Mese> items) {
        grid.setItems(items);
    }


}
