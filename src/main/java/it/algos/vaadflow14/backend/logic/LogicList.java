package it.algos.vaadflow14.backend.logic;

import com.mongodb.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.data.provider.*;
import de.codecamp.vaadin.components.messagedialog.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
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
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.operationForm = annotation.getOperation(entityClazz);
        super.usaBottoneDeleteAll = AEPreferenza.usaMenuReset.is() && annotation.usaReset(entityClazz);
        super.usaBottoneResetList = AEPreferenza.usaMenuReset.is() && annotation.usaReset(entityClazz);
        super.usaBottoneNew = AEPreferenza.usaMenuReset.is() && annotation.usaCreazione(entityClazz);

        this.fixOperationForm();
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

    //    /**
    //     * Controlla che esista il riferimento alla entityClazz <br>
    //     * Se non esiste nella List, è un errore <br>
    //     * Se non esiste nel Form, lo crea dall'url del browser <br>
    //     * Deve essere sovrascritto, senza invocare il metodo della superclasse <br>
    //     */
    //    @Override
    //    protected void fixEntityClazz() {
    //        if (entityClazz == null) {
    //            logger.error("Non esiste la entityClazz", LogicList.class, "fixEntityClazz");
    //        }
    //    }


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
        headerSpan = appContext.getBean(AHeaderSpanList.class, this.getSpanList());
        super.fixAlertLayout();
    }

    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * DEVE essere sovrascritto, senza invocare il metodo della superclasse <br>
     *
     * @return una lista di elementi html di tipo 'span'
     */
    protected List<Span> getSpanList() {
        return null;
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

        if (usaBottoneDeleteAll) {
            listaBottoni.add(AEButton.deleteAll);
        }
        if (usaBottoneResetList && entityService != null) {
            //--se manca la classe specifica il metodo è vuoto e il bottone non potrebbe funzionare
            if (entityService.resetEmptyOnly().isErrato()) {
                listaBottoni.add(AEButton.resetList);
            }
        }
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
        //        if (usaBottoneResetForm) {
        //            listaBottoni.add(AEButton.resetForm);
        //        }
        //        if (usaBottoneBack) {
        //            listaBottoni.add(AEButton.back);
        //        }
        //        if (usaBottoneAnnulla) {
        //            listaBottoni.add(AEButton.annulla);
        //        }
        //        if (usaBottoneConferma) {
        //            listaBottoni.add(AEButton.conferma);
        //        }
        //        if (usaBottoneRegistra) {
        //            listaBottoni.add(AEButton.registra);
        //        }
        //        if (usaBottoneCancella) {
        //            listaBottoni.add(AEButton.delete);
        //        }
        //        if (usaBottonePrima) {
        //            listaBottoni.add(AEButton.prima);
        //        }
        //        if (usaBottoneDopo) {
        //            listaBottoni.add(AEButton.dopo);
        //        }

        return listaBottoni;
    }


    /**
     * Costruisce il corpo principale (obbligatorio) della Grid <br>
     */
    @Override
    protected void fixBodyLayout() {
        DataProvider dataProvider;
        String sortProperty = annotation.getSortProperty(entityClazz);
        BasicDBObject sort = null;
        grid = appContext.getBean(AGrid.class, entityClazz, this);
        sort = new BasicDBObject(sortProperty, 1);

        dataProvider = dataService.creaDataProvider(entityClazz, sort);
        grid.getGrid().setDataProvider(dataProvider);
        grid.getGrid().setHeight("100%");
        grid.fixGridHeader(dataProvider.size(null));
        this.addGridListeners();

        if (bodyPlaceHolder != null && grid != null) {
            bodyPlaceHolder.add(grid.getGrid());
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
                this.operationForm = AEOperation.addNew;
                this.executeRoute();
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
    public boolean performAction(AIAction iAzione, AEntity entityBean) {
        boolean status = true;
        AEAction azione = iAzione instanceof AEAction ? (AEAction) iAzione : null;

        if (azione == null) {
            return false;
        }

        switch (azione) {
            case doubleClick:
                this.executeRoute(entityBean);
                break;
            default:
                status = false;
                break;
        }

        return status;
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
