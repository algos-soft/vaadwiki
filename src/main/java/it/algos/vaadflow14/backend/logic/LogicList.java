package it.algos.vaadflow14.backend.logic;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.provider.*;
import de.codecamp.vaadin.components.messagedialog.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.header.*;
import it.algos.vaadflow14.ui.interfaces.*;
import it.algos.vaadflow14.ui.list.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 26-feb-2021
 * Time: 17:27
 */
public abstract class LogicList extends Logic {


    /**
     * The Grid  (obbligatoria per ViewList)
     */
    protected AGrid grid;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro del costruttore usato <br>
     */
    public LogicList() {
    }// end of Vaadin/@Route constructor


    /**
     * Costruttore con parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nel costruttore della sottoclasse l'annotation @Autowired potrebbe essere omessa perché c'è un solo costruttore <br>
     * Nel costruttore della sottoclasse usa un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     * Riceve e regola la entityClazz (final) associata a questa logicView <br>
     *
     * @param entityService (obbligatorio) riferimento al service specifico correlato a questa istanza (prototype) di LogicList
     * @param entityClazz   (obbligatorio)  the class of type AEntity
     */
    public LogicList(final AIService entityService, final Class<? extends AEntity> entityClazz) {
        super.entityService = entityService;
        super.entityClazz = entityClazz;
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.operationForm = annotation.getOperation(entityClazz);
        super.usaBottoneDeleteAll = AEPreferenza.usaMenuReset.is() && annotation.usaDeleteMenu(entityClazz);
        super.usaBottoneResetList = AEPreferenza.usaMenuReset.is() && annotation.usaResetMenu(entityClazz);
        super.usaBottoneNew = AEPreferenza.usaMenuReset.is() && annotation.usaCreazione(entityClazz);

        this.fixOperationForm();
    }

    /**
     * Regolazioni iniziali di alcuni oggetti <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void regolazioniIniziali() {
        super.regolazioniIniziali();

        //--costruisce una lista (vuota) di Span per l'header della lista
        super.spanHeaderList = new ArrayList<>();

        //--costruisce una mappa (vuota) di ComboBox per il topLayout
        super.mappaComboBox = new HashMap<>();

        //--costruisce una mappa (vuota) di filtri per la Grid
        super.mappaFiltri = new HashMap<>();
    }

    /**
     * Regola il modo di presentare la scheda (Form) prima di lanciare la @Route. <br>
     * 1) Usa l'annotation @AIForm.operationForm() nella AEntity del package <br>
     * - nel package la classe AEntity esiste sempre <br>
     * - se esiste l'annotation, la usa <br>
     * - valore fisso per tutto il programma <br>
     * - se non esiste l'annotation, viene comunque gestito un valore di default <br>
     * 2) Si può modificare il valore di operationForm in xxxLogicList.fixOperationForm(); <br>
     * - nel package la classe xxxLogicList è facoltativa <br>
     * - se esiste la classe specifica xxxLogicList, può regolare il valore <br>
     * - per differenziarlo ad esempio in base all'utente collegato <br>
     * - se manca la classe specifica xxxLogicList nel package, usa il valore della AEntity <br>
     * 3) Il valore viene usato da executeRoute() di questa xxxLogicList <br>
     * - viene passato alla @Route come parametro KEY_FORM_TYPE <br>
     * - viene estratto da routeParameter in setParameter() della xxxLogicForm <br>
     * - viene recepito in fixTypeView() della xxxLogicForm <br>
     * 4) Potrebbe eventualmente essere modificato anche in xxxLogicForm.fixPreferenze(); <br>
     * - nel package la classe xxxLogicForm è facoltativa <br>
     * - se esiste la classe specifica xxxLogicForm, può regolare il valore <br>
     * - se manca la classe specifica xxxLogicForm nel package, usa il valore della @Route <br>
     * Può essere sovrascritto senza invocare il metodo della superclasse <br>
     */
    protected void fixOperationForm() {
        this.operationForm = annotation.getOperation(entityClazz);
    }


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * <p>
     * Chiamato da Logic.initView() <br>
     * Nell' implementazione standard di default NON presenta nessun avviso <br>
     * Recupera dal service specifico gli (eventuali) avvisi <br>
     * Costruisce un' istanza dedicata con la lista degli avvisi <br>
     * Gli avvisi sono realizzati con tag html 'span' differenziati per colore anche in base all'utente collegato <br>
     * Se l'applicazione non usa security, il colore è deciso dal service specifico <br<
     * DEVE essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertLayout() {
        this.fixSpanList();
        if (spanHeaderList != null && spanHeaderList.size() > 0) {
            headerSpan = appContext.getBean(AHeaderSpanList.class, super.spanHeaderList);
        }

        super.fixAlertLayout();
    }

    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * DEVE essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    protected void fixSpanList() {
    }

    protected void addSpanBlu(final String message) {
        if (spanHeaderList != null) {
            spanHeaderList.add(html.getSpanBlu(message));
        }
    }

    protected void addSpanVerde(final String message) {
        if (spanHeaderList != null) {
            spanHeaderList.add(html.getSpanVerde(message));
        }
    }

    protected void addSpanRosso(final String message) {
        if (spanHeaderList != null && usaSpanHeaderRossi) {
            spanHeaderList.add(html.getSpanRosso(message));
        }
    }

    /**
     * Regola una mappa di ComboBox (solo per la List e facoltativi) da usare nel wrapper getWrapButtonsTop() <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixMappaComboBox() {
        for (String fieldName : annotation.getGridColumns(entityClazz)) {
            if (annotation.usaComboBoxGrid(entityClazz, fieldName)) {
                this.fixComboBox(fieldName);
            }
        }
    }

    /**
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Costruisce i bottoni come dai flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected List<AIButton> getListaAEBottoniTop() {
        List<AIButton> listaBottoni = super.getListaAEBottoniTop();
        String message = VUOTA;

        if (usaBottoneDeleteAll) {
            listaBottoni.add(AEButton.deleteAll);
        }

        if (usaBottoneResetList && entityService != null) {
            //--se manca il metodo specifico il bottone non potrebbe funzionare
            try {
                if (entityService.getClass().getDeclaredMethod("resetEmptyOnly") != null) {
                    listaBottoni.add(AEButton.resetList);
                }
            } catch (Exception unErrore) {
                message = String.format("Non sono riuscito a controllare se esiste il metodo resetEmptyOnly() nella classe %s", entityService.getClass().getSimpleName());
                logger.log(AETypeLog.checkData, message);
            }
        }

        //        if (methodExists) {
        //            result = entityService.resetEmptyOnly();
        //            logger.log(AETypeLog.checkData, result.getMessage());
        //        }
        //        else {
        //            if (!nameService.equals(TAG_GENERIC_SERVICE)) {
        //                message = String.format("Nel package %s la classe %s non ha il metodo resetEmptyOnly() ", packageName, entityServicePrevista);
        //                logger.log(AETypeLog.checkData, message);
        //            }
        //        }

        if (usaBottoneNew) {
            listaBottoni.add(AEButton.nuovo);
        }
        if (usaBottoneSearch) {
            listaBottoni.add(AEButton.searchDialog);
        }
        if (usaBottoneExport) {
            listaBottoni.add(AEButton.export);
        }
        if (usaBottonePaginaWiki) {
            listaBottoni.add(AEButton.wiki);
        }
        if (usaBottoneDownload) {
            listaBottoni.add(AEButton.download);
        }
        if (usaBottoneUpload) {
            listaBottoni.add(AEButton.upload);
        }

        return listaBottoni;
    }

    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     * Invoca un metodo del service <br>
     * Aggiunge il ComboBox alla mappa <br>
     *
     * @param fieldName (obbligatorio) della property da utilizzare per il ComboBox
     */
    protected ComboBox fixComboBox(final String fieldName) {
        return fixComboBox(fieldName, (DataProvider) null, COMBO_WIDTH, null);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     * Invoca un metodo del service <br>
     * Aggiunge il ComboBox alla mappa <br>
     *
     * @param fieldName    (obbligatorio) della property da utilizzare per il ComboBox
     * @param dataProvider fornitore degli items. Se manca lo costruisce con la collezione completa
     */
    protected ComboBox fixComboBox(final String fieldName, final DataProvider dataProvider) {
        return fixComboBox(fieldName, dataProvider, COMBO_WIDTH, null);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     * Invoca un metodo del service <br>
     * Aggiunge il ComboBox alla mappa <br>
     *
     * @param fieldName (obbligatorio) della property da utilizzare per il ComboBox
     * @param width     larghezza a video del ComboBox. Se manca usa il default FlowCost.COMBO_WIDTH
     */
    protected ComboBox fixComboBox(final String fieldName, final int width) {
        return fixComboBox(fieldName, (DataProvider) null, width, null);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     * Invoca un metodo del service <br>
     * Aggiunge il ComboBox alla mappa <br>
     *
     * @param fieldName    (obbligatorio) della property da utilizzare per il ComboBox
     * @param initialValue eventuale valore iniziale di selezione
     */
    protected ComboBox fixComboBox(final String fieldName, final Object initialValue) {
        return fixComboBox(fieldName, (DataProvider) null, COMBO_WIDTH, initialValue);
    }

    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     * Invoca un metodo del service <br>
     * Aggiunge il ComboBox alla mappa <br>
     *
     * @param fieldName    (obbligatorio) della property da utilizzare per il ComboBox
     * @param dataProvider fornitore degli items. Se manca lo costruisce con la collezione completa
     * @param width        larghezza a video del ComboBox. Se manca usa il default FlowCost.COMBO_WIDTH
     * @param initialValue eventuale valore iniziale di selezione
     */
    protected ComboBox fixComboBox(final String fieldName, final DataProvider dataProvider, final int width, final Object initialValue) {
        ComboBox combo = utility.creaComboBox(entityClazz, fieldName, dataProvider, width, initialValue);

        if (mappaComboBox != null && combo != null) {
            mappaComboBox.put(fieldName, combo);
        }

        return combo;
    }

    /**
     * Costruisce il corpo principale (obbligatorio) della Grid <br>
     */
    @Override
    protected void fixBodyLayout() {
        //--con dataProvider standard - con filtro base (vuoto=tutta la collection) e sort di default della AEntity
        //--può essere ri-filtrato successivamente
        grid = appContext.getBean(AGrid.class, entityClazz, this, mappaFiltri);

        grid.fixGridHeader();
        this.addGridListeners();

        /**
         * Regolazioni INDISPENSABILI per usare DataProvider sui DB voluminosi <br>
         * Deve essere MENO di 100% il VerticalLayout esterno <br
         * Deve essere MENO di 100% il bodyPlaceHolder <br
         * Deve essere ESATTAMENTE il 100% la Grid <br
         */
        if (bodyPlaceHolder != null && grid != null) {
            this.setHeight("95%");
            bodyPlaceHolder.setHeight("95%");
            grid.getGrid().setHeight("100%");
            bodyPlaceHolder.add(grid.getGrid());
        }
    }

    /**
     * Regolazioni finali di alcuni oggetti <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void regolazioniFinali() {
        TextField searchField = this.topLayout.getSearchField();
        String placeHolder = annotation.getSearchPropertyName(entityClazz);

        if (searchField != null) {
            searchField.setPlaceholder(text.primaMaiuscola(placeHolder) + TRE_PUNTI);
        }
    }


    /**
     * Aggiunge tutti i listeners alla Grid di 'bodyPlaceHolder' che è stata creata SENZA listeners <br>
     */
    protected void addGridListeners() {
        if (grid != null && grid.getGrid() != null) {
            grid.setAllListener(this);
        }
    }


    /**
     * Costruisce una lista ordinata di nomi delle properties della Grid. <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIList della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica xxxLogicList <br>
     * Può essere sovrascritto senza invocare il metodo della superclasse <br>
     *
     * @return lista di nomi di properties
     */
    @Override
    public List<String> getGridColumns() {
        return annotation.getGridColumns(entityClazz);
    }


    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     * Interfaccia utilizzata come parametro per poter sovrascrivere il metodo <br>
     * Nella classe base eseguirà un casting a AEAction <br>
     * Nella (eventuale) sottoclasse specifica del progetto eseguirà un casting a AExxxAction <br>
     *
     * @param iAzione interfaccia dell'azione selezionata da eseguire
     *
     * @return false se il parametro non è una enumeration valida o manca lo switch
     */
    @Override
    public boolean performAction(AIAction iAzione) {
        boolean status = true;
        AEAction azione = iAzione instanceof AEAction ? (AEAction) iAzione : null;

        if (azione == null) {
            return false;
        }

        switch (azione) {
            case deleteAll:
                this.openConfirmDeleteAll();
                break;
            case resetList:
                this.openConfirmReset();
                break;
            case nuovo:
                this.newForm();
                break;
            case searchDialog:
                //                Notification.show("Not yet. Coming soon.", 3000, Notification.Position.MIDDLE);
                //                logger.info("Not yet. Coming soon", this.getClass(), "performAction");
                break;
            case searchField:
                //                this.searchFieldValue = searchFieldValue;
                //                refreshGrid();
                break;
            case valueChanged:
                //                refreshGrid();
                break;
            case export:
                //                export();
                break;
            case showWiki:
                openWikiPage(wikiPageTitle);
                break;
            case download:
                download();
                break;
            case upload:
                break;
            //            case edit:
            //            case show:
            //            case editNoDelete:
            //            case doubleClick:
            //                break;
            default:
                status = false;
                break;
        }

        return status;
    }

    /**
     * Esegue l'azione del bottone, textEdit o comboBox. <br>
     * Interfaccia utilizzata come parametro per poter sovrascrivere il metodo <br>
     * Nella classe base eseguirà un casting a AEAction <br>
     * Nella (eventuale) sottoclasse specifica del progetto eseguirà un casting a AExxxAction <br>
     *
     * @param iAzione    interfaccia dell'azione selezionata da eseguire
     * @param entityBean selezionata
     *
     * @return false se il parametro iAzione non è una enumeration valida o manca lo switch
     */
    @Override
    public boolean performAction(final AIAction iAzione, final AEntity entityBean) {
        boolean status = true;
        AEAction azione = iAzione instanceof AEAction ? (AEAction) iAzione : null;

        if (azione == null) {
            return false;
        }

        switch (azione) {
            case doubleClick:
                this.openForm(entityBean);
                break;
            default:
                status = false;
                break;
        }

        return status;
    }

    /**
     * Esegue l'azione del bottone. Azione che necessita di una stringa. <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param iAzione          interfaccia dell'azione selezionata da eseguire
     * @param searchFieldValue valore corrente del campo searchField (solo per List)
     *
     * @return false se il parametro iAzione non è una enumeration valida o manca lo switch
     */
    @Override
    public boolean performAction(final AIAction iAzione, final String searchFieldValue) {
        boolean status = true;
        AEAction azione = iAzione instanceof AEAction ? (AEAction) iAzione : null;

        if (azione == null) {
            return false;
        }

        switch (azione) {
            case searchField:
                this.fixFiltroSearch(searchFieldValue);
                this.grid.getGrid().getDataProvider().refreshAll();
                grid.fixGridHeader();
                break;
            default:
                status = false;
                break;
        }

        return status;
    }

    /**
     * Esegue l'azione del bottone. Azione che necessita di un field e di un valore. <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param iAzione    interfaccia dell'azione selezionata da eseguire
     * @param fieldName  nome del field
     * @param fieldValue valore corrente del field
     *
     * @return false se il parametro iAzione non è una enumeration valida o manca lo switch
     */
    @Override
    public boolean performAction(final AIAction iAzione, final String fieldName, final Object fieldValue) {
        boolean status = true;
        AEAction azione = iAzione instanceof AEAction ? (AEAction) iAzione : null;
        AFiltro filtro = null;

        if (azione == null) {
            return false;
        }

        switch (azione) {
            case valueChanged:
                this.fixFiltroCombo(fieldName, fieldValue);
                this.grid.getGrid().getDataProvider().refreshAll();
                grid.fixGridHeader();
                break;
            default:
                status = false;
                break;
        }

        return status;
    }

    /**
     * Costruisce una nuova @route in modalità new <br>
     * Seleziona (eventualmente) il Form da usare <br>
     * Può essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    protected void newForm() {
        this.operationForm = AEOperation.addNew;
        this.executeRoute();
    }

    /**
     * Costruisce una nuova @route in modalità new <br>
     * Seleziona (eventualmente) il Form da usare <br>
     * Può essere sovrascritto, senza invocare il metodo della superclasse <br>
     *
     * @param entityBean selezionata
     */
    protected void openForm(final AEntity entityBean) {
        this.executeRoute(entityBean);
    }


    /**
     * Costruisce il filtro di un TextField. <br>
     * Recupera il field su cui selezionare il valore <br>
     * Filtro per caratteri iniziali <br>
     *
     * @param searchFieldValue valore corrente del campo searchField (solo per List)
     */
    protected AFiltro fixFiltroSearch(final String searchFieldValue) {
        AFiltro filtro = null;
        String searchFieldName = annotation.getSearchPropertyName(entityClazz);

        if (text.isValid(searchFieldName)) {
            filtro = AFiltro.start(searchFieldName, searchFieldValue);
        }

        if (mappaFiltri != null) {
            mappaFiltri.remove(KEY_MAPPA_SEARCH);
            if (filtro != null) {
                mappaFiltri.put(KEY_MAPPA_SEARCH, filtro);
            }
        }

        return filtro;
    }


    /**
     * Costruisce il filtro di un ComboBox. <br>
     * Recupera il field su cui selezionare il valore <br>
     * Filtro per uguaglianza <br>
     *
     * @param fieldName  nome del field
     * @param fieldValue valore corrente del field
     */
    protected AFiltro fixFiltroCombo(final String fieldName, final Object fieldValue) {
        AFiltro filtro = null;

        if (text.isValid(fieldName) && fieldValue != null) {
            filtro = AFiltro.ugualeObj(fieldName, fieldValue);
        }

        if (mappaFiltri != null) {
            mappaFiltri.remove(fieldName);
            if (filtro != null) {
                mappaFiltri.put(fieldName, filtro);
            }
        }

        return filtro;
    }

    protected void executeRoute() {
        executeRoute(VUOTA, VUOTA, VUOTA);
    }

    /**
     * Lancia una @route con la visualizzazione di una singola scheda. <br>
     * Se il package usaSpostamentoTraSchede=true, costruisce una query
     * con le keyIDs della scheda precedente e di quella successiva
     * (calcolate secondo l'ordinamento previsto) <br>
     */
    protected void executeRoute(final AEntity entityBean) {
        if (entityBean == null) {
            executeRoute(VUOTA, VUOTA, VUOTA);
            return;
        }

        final String sortProperty = annotation.getSortProperty(entityClazz);
        final Object valueProperty = reflection.getPropertyValue(entityBean, sortProperty);
        final String beanPrevID = text.isValid(entityBeanPrevID) ? entityBeanPrevID : mongo.findPreviousID(entityClazz, sortProperty, valueProperty);
        final String beanNextID = text.isValid(entityBeanNextID) ? entityBeanPrevID : mongo.findNextID(entityClazz, sortProperty, valueProperty);

        executeRoute(entityBean.id, beanPrevID, beanNextID);
    }

    /**
     * Opens the confirmation dialog before deleting all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected final void openConfirmDeleteAll() {
        MessageDialog messageDialog;
        String message = "Vuoi veramente cancellare tutto? L' operazione non è reversibile.";
        VaadinIcon icon = VaadinIcon.WARNING;

        if (mongo.isValid(entityClazz)) {
            messageDialog = new MessageDialog().setTitle("Delete").setMessage(message);
            messageDialog.addButton().text("Cancella").icon(icon).error().onClick(e -> clickDeleteAll()).closeOnClick();
            messageDialog.addButtonToLeft().text("Annulla").primary().clickShortcutEscape().clickShortcutEnter().closeOnClick();
            messageDialog.open();
        }
    }

    /**
     * Cancellazione effettiva (dopo dialogo di conferma) di tutte le entities della collezione. <br>
     * Azzera gli items <br>
     * Ridisegna la GUI <br>
     */
    public void clickDeleteAll() {
        entityService.deleteAll();
        logger.deleteAll(entityClazz);
        this.refreshGrid();
        //            this.reloadList();//@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
    }

    /**
     * Opens the confirmation dialog before reset all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected final void openConfirmReset() {
        MessageDialog messageDialog;
        String message = "Vuoi veramente ripristinare i valori originali predeterminati di questa collezione? L' operazione cancellerà tutti i valori successivamente aggiunti o modificati.";
        VaadinIcon icon = VaadinIcon.WARNING;

        if (mongo.isEmpty(entityClazz)) {
            clickReset();
        }
        else {
            messageDialog = new MessageDialog().setTitle("Reset").setMessage(message);
            messageDialog.addButton().text("Continua").icon(icon).error().onClick(e -> clickReset()).closeOnClick();
            messageDialog.addButtonToLeft().text("Annulla").primary().clickShortcutEscape().clickShortcutEnter().closeOnClick();
            messageDialog.open();
        }
    }


    /**
     * Azione proveniente dal click sul bottone Reset <br>
     * Creazione di alcuni dati iniziali <br>
     * Rinfresca la griglia <br>
     */
    public void clickReset() {
        if (resetDeletingAll()) {
            this.refreshGrid();
            //            this.reloadList();
        }
    }

    /**
     * Ricreazione di alcuni dati iniziali standard <br>
     * Invocato dal bottone Reset di alcune liste <br>
     * Cancella la collection (parzialmente, se usaCompany=true) <br>
     * I dati possono essere: <br>
     * 1) recuperati da una Enumeration interna <br>
     * 2) letti da un file CSV esterno <br>
     * 3) letti da Wikipedia <br>
     * 4) creati direttamente <br>
     *
     * @return false se non esiste il metodo sovrascritto o se la collection
     * ....... true se esiste il metodo sovrascritto è la collection viene ri-creata
     */
    private boolean resetDeletingAll() {
        AIResult result;
        entityService.delete();
        result = entityService.resetEmptyOnly();

        logger.log(AETypeLog.reset, result.getMessage());
        return result.isValido();
    }


    /**
     * Aggiorna gli items della Grid, utilizzando (anche) i filtri. <br>
     * Chiamato inizialmente alla creazione della Grid <br>
     * Chiamato per modifiche effettuate ai filtri, popup, newEntity, deleteEntity, ecc... <br>
     */
    public void refreshGrid() {
        List<? extends AEntity> items;

        if (grid != null && grid.getGrid() != null) {
            //            updateFiltri();
            items = mongo.findAll(entityClazz);
            grid.getGrid().deselectAll();
            grid.setItems(items);
            grid.getGrid().getDataProvider().refreshAll();
        }
    }


}
