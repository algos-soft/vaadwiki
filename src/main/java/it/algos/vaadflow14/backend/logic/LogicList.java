package it.algos.vaadflow14.backend.logic;

import ch.carnet.kasparscherrer.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.server.*;
import de.codecamp.vaadin.components.messagedialog.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.ui.dialog.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.header.*;
import it.algos.vaadflow14.ui.interfaces.*;
import it.algos.vaadflow14.ui.list.*;
import org.vaadin.haijian.*;

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
        boolean isResetMethod = false;

        try {
            isResetMethod = entityService.getClass().getDeclaredMethod(TAG_METHOD_RESET) != null;
        } catch (Exception unErrore) {
        }

        super.usaBottoneDeleteAll = annotation.usaNew(entityClazz);
        super.usaBottoneResetList = annotation.usaReset(entityClazz) && isResetMethod;
        super.usaBottoneNew = annotation.usaNew(entityClazz);
        super.usaBottoneSearch = annotation.usaSearchField(entityClazz);

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
        super.alertList = new ArrayList<>();

        //        //--costruisce una mappa (vuota) di ComboBox per il topLayout
        //        super.mappaComboBox = new HashMap<>();

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
     */
    @Override
    protected void fixAlertLayout() {
        this.fixAlertList();

        String preferenza = html.bold("Preferenza");
        String delete = html.bold("Delete");
        String tutte = html.bold("tutte");
        String entity = entityClazz.getSimpleName();
        String aREntity = html.bold("AREntity");
        String service = entityService.getClass().getSimpleName();
        String collezione = html.bold(annotation.getCollectionName(entityClazz));
        String reset = html.bold("Reset");
        String nuovo = html.bold("New");
        String usaBoot = html.bold("usaBoot");
        String usaNew = html.bold("usaNew");
        String solo = html.bold("solo");
        String resetTrue = html.bold("reset=true");
        String resetFalse = html.bold("reset=false");
        String search = html.bold("SearchField");
        String property = html.bold("searchProperty");
        String and = html.bold("e");
        String non = html.bold("non");
        String propReset = html.bold("reset");
        String methodReset = html.bold("reset()");
        String xxx = html.bold("xxx");

        addSpanRosso(String.format("La visualizzazione di questi avvisi rossi si regola in %s:usaSpanHeaderRossi", preferenza));
        addSpanRosso(String.format("Bottone %s presente se %s->@AIEntity(%s=true)", delete, entity, usaNew));
        addSpanRosso(String.format("Bottone %s presente se %s->@AIEntity(%s=true)", nuovo, entity, usaNew));
        addSpanRosso(String.format("%s presente se  %s->@AIView(%s=%s)", search, entity, property, xxx));
        if (annotation.usaReset(entityClazz)) {
            addSpanRosso(String.format("Bottone %s presente se %s extends %s ed esiste %s.%s", reset, entity, aREntity, service, methodReset));
            addSpanRosso(String.format("Bottone %s agisce %s sulle entities della collezione %s che hanno la property %s", reset, solo, collezione, resetTrue));
            addSpanRosso(String.format("Bottone %s agisce su %s le entities della collezione %s", delete, tutte, collezione));
            addSpanRosso(String.format("La collezione %s viene ricreata (mantenendo le entities che hanno %s) ad ogni avvio del programma se:", collezione, resetFalse));
            addSpanRosso(String.format("- %s estende %s,", entity, aREntity));
            addSpanRosso(String.format("- %s->@AIEntity %s=true,", entity, usaBoot));
            addSpanRosso(String.format("- esiste %s.%s,", service, methodReset));
            addSpanRosso(String.format("- la collezione %s contiene nessuna entity che abbia la property %s ", non, resetTrue));
            addSpanRosso(String.format("Se %s extends %s e %s->@AIEntity(%s=false), %s compare la property %s", entity, aREntity, entity, usaNew, non, propReset));
            addSpanRosso(String.format("Se %s extends %s e %s->@AIEntity(%s=true), compare la property %s uguale a true per le schede create con reset", entity, aREntity, entity, usaNew, propReset));
        }

        if (alertList != null && alertList.size() > 0) {
            headerSpan = appContext.getBean(AHeaderAlertList.class, super.alertList);
        }

        super.fixAlertLayout();
    }

    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixAlertList() {
    }


    protected void addSpanBlu(final String message) {
        if (alertList != null) {
            alertList.add(html.getSpanBlu(message));
        }
    }

    protected void addSpanVerde(final String message) {
        if (alertList != null) {
            alertList.add(html.getSpanVerde(message));
        }
    }

    protected void addSpanRossoFix(final String message) {
        if (alertList != null) {
            alertList.add(html.getSpanRosso(message));
        }
    }

    protected void addSpanRosso(final String message) {
        if (alertList != null && usaSpanHeaderRossi) {
            alertList.add(html.getSpanRosso(message));
        }
    }


    /**
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Bottoni standard AIButton di VaadinFlow14 e della applicazione corrente <br>
     * Costruisce i bottoni come dai flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAEBottoniTop() {
        String message;

        if (usaBottoneDeleteAll) {
            putMappa(AEButton.deleteAll);
        }

        if (usaBottoneResetList && entityService != null) {
            //--se manca il metodo specifico il bottone non potrebbe funzionare
            try {
                if (entityService.getClass().getDeclaredMethod(TAG_METHOD_RESET) != null) {
                    putMappa(AEButton.resetList);
                }
            } catch (Exception unErrore) {
                message = String.format("Non sono riuscito a controllare se esiste il metodo resetEmptyOnly() nella classe %s", entityService.getClass().getSimpleName());
                logger.log(AETypeLog.checkData, message);
            }
        }

        if (usaBottoneNew) {
            putMappa(AEButton.nuovo);
        }
        if (usaBottonePaginaWiki) {
            putMappa(AEButton.wiki);
        }
        if (usaBottoneDownload) {
            putMappa(AEButton.download);
        }
        if (usaBottoneUpload) {
            putMappa(AEButton.upload);
        }
        if (usaBottoneSearch) {
            putMappa(AEButton.searchDialog);
        }
        if (usaBottoneExport) {
            putMappa(AEButton.export);
        }
    }

    /**
     * Costruisce una mappa di componenti di comando/selezione/filtro al Top della view <br>
     * <p>
     * I componenti possono essere (nell'ordine):
     * Bottoni standard AIButton di VaadinFlow14 e della applicazione corrente <br>
     * SearchField per il filtro testuale di ricerca <br>
     * ComboBox di filtro <br>
     * CheckBox di filtro <br>
     * IndeterminateCheckbox di filtro <br>
     * Bottoni specifici non standard <br>
     * <p>
     * Costruisce i bottoni standard come dai flag regolati di default o nella sottoclasse <br>
     * Costruisce il searchField previsto in AEntity->@AIView(searchProperty) <br>
     * Costruisce i comboBox previsti nella AEntity->@AIField(usaComboBox = true) <br>
     * Costruisce i checkBox previsti nella AEntity->@AIField(usaCheckBox = true) <br>
     * Costruisce gli indeterminateCheckbox previsti nella AEntity->@AIField(usaCheckBox3Vie = true) <br>
     * Nella sottoclasse possono essere aggiunti i bottoni, comboBox e checkBox <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaComandiTop() {
        super.creaComandiTop();
        ComboBox combo;
        Checkbox check;
        IndeterminateCheckbox check3Vie;

        for (String fieldName : annotation.getGridColumns(entityClazz)) {

            if (annotation.usaComboBox(entityClazz, fieldName)) {
                combo = this.getComboBox(fieldName);
                mappaComponentiTop.put(fieldName, combo);
            }

            if (annotation.usaCheckBox(entityClazz, fieldName)) {
                check = new Checkbox(text.primaMaiuscola(fieldName));
                mappaComponentiTop.put(fieldName, check);
            }

            if (annotation.usaCheckBox3Vie(entityClazz, fieldName)) {
                check3Vie = new IndeterminateCheckbox(text.primaMaiuscola(fieldName));
                mappaComponentiTop.put(fieldName, check3Vie);
            }
        }

        if (utility.usaReset(entityClazz)) {
            check3Vie = new IndeterminateCheckbox(text.primaMaiuscola(FIELD_NAME_RESET));
            mappaComponentiTop.put(FIELD_NAME_RESET, check3Vie);
        }
    }

    //    /**
    //     * Aggiunge una enumeration alla mappa dei componenti <br>
    //     *
    //     * @param aiButton enumeration da aggiungere alla mappa componenti
    //     */
    //    protected void putMappa(final AIButton aiButton) {
    //        mappaComponentiTop.put(aiButton.getTesto(), aiButton);
    //    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     * Invoca un metodo del service <br>
     * Aggiunge il ComboBox alla mappa <br>
     *
     * @param fieldName (obbligatorio) della property da utilizzare per il ComboBox
     */
    protected ComboBox getComboBox(final String fieldName) {
        return getComboBox(fieldName, (DataProvider) null, 0, null);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     * Invoca un metodo del service <br>
     * Aggiunge il ComboBox alla mappa <br>
     *
     * @param fieldName    (obbligatorio) della property da utilizzare per il ComboBox
     * @param dataProvider fornitore degli items. Se manca lo costruisce con la collezione completa
     */
    protected ComboBox getComboBox(final String fieldName, final DataProvider dataProvider) {
        return getComboBox(fieldName, dataProvider, 0, null);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     * Invoca un metodo del service <br>
     * Aggiunge il ComboBox alla mappa <br>
     *
     * @param fieldName (obbligatorio) della property da utilizzare per il ComboBox
     * @param width     larghezza a video del ComboBox. Se manca usa il default FlowCost.COMBO_WIDTH
     */
    protected ComboBox getComboBox(final String fieldName, final int width) {
        return getComboBox(fieldName, (DataProvider) null, width, null);
    }


    /**
     * Crea un ComboBox e lo aggiunge alla mappa <br>
     * Invoca un metodo del service <br>
     * Aggiunge il ComboBox alla mappa <br>
     *
     * @param fieldName    (obbligatorio) della property da utilizzare per il ComboBox
     * @param initialValue eventuale valore iniziale di selezione
     */
    protected ComboBox getComboBox(final String fieldName, final Object initialValue) {
        return getComboBox(fieldName, (DataProvider) null, 0, initialValue);
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
    protected ComboBox getComboBox(final String fieldName, final DataProvider dataProvider, final int width, final Object initialValue) {
        return utility.creaComboBox(entityClazz, fieldName, dataProvider, width, initialValue);
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
        List<String> lista = annotation.getGridColumns(entityClazz);

        if (utility.usaReset(entityClazz)) {
            lista.add(FIELD_NAME_RESET);
        }

        return lista;
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
                this.openReset();
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
            case check:
                this.fixFiltroCheck(fieldName, fieldValue);
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

    /**
     * Costruisce il filtro di un Checkbox. <br>
     * Recupera il field su cui selezionare il valore <br>
     * Filtro booleano a 3 stati <br>
     *
     * @param fieldName  nome del field
     * @param fieldValue valore corrente del field
     */
    protected AFiltro fixFiltroCheck(final String fieldName, final Object fieldValue) {
        AFiltro filtro = null;

        if (text.isValid(fieldName)) {
            if (fieldValue == null) {
                filtro = null;
            }
            else {
                if ((boolean) fieldValue) {
                    filtro = AFiltro.vero(fieldName);
                }
                else {
                    filtro = AFiltro.falso(fieldName);
                }
            }
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
        final String beanPrevID = text.isValid(entityBeanPrevID) ? entityBeanPrevID : ((MongoService) mongo).findPreviousID(entityClazz, sortProperty, valueProperty);//@todo da controllare
        final String beanNextID = text.isValid(entityBeanNextID) ? entityBeanPrevID : ((MongoService) mongo).findNextID(entityClazz, sortProperty, valueProperty);//@todo da controllare

        executeRoute(entityBean.id, beanPrevID, beanNextID);
    }

    /**
     * Opens the confirmation dialog before deleting all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected void openConfirmDeleteAll() {
        MessageDialog messageDialog;
        String message = "Vuoi veramente cancellare tutto? L' operazione non è reversibile.";
        VaadinIcon icon = VaadinIcon.WARNING;

        if (((MongoService) mongo).isValidCollection(entityClazz)) {//@todo da controllare
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
        String message;
        String collectionName;

        entityService.deleteAll();

        collectionName = annotation.getCollectionName(entityClazz);
        message = "La collezione " + collectionName + " è stata interamente cancellata";
        logger.log(AETypeLog.deleteAll, message);
        this.refreshGrid();
    }

    /**
     * Opens the confirmation dialog before reset all items. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected final void openReset() {
        appContext.getBean(AResetDialog.class).open(this::clickReset);

        MessageDialog messageDialog;
        //        String message = "Vuoi veramente ripristinare i valori originali predeterminati di questa collezione? L' operazione cancellerà tutti i valori originali. Eventuali valori inseriti manualmente NON vengono cancellati/modificati";
        //        VaadinIcon icon = VaadinIcon.WARNING;
        //
        //        if (mongo.isEmpty(entityClazz)) {
        //            clickReset();
        //        }
        //        else {
        //            messageDialog = new MessageDialog().setTitle("Reset").setMessage(message);
        //            messageDialog.addButton().text("Continua").icon(icon).error().onClick(e -> clickReset()).closeOnClick();
        //            messageDialog.addButtonToLeft().text("Annulla").primary().clickShortcutEscape().clickShortcutEnter().closeOnClick();
        //            messageDialog.open();
        //        }
    }



    /**
     * Azione proveniente dal click sul bottone Reset <br>
     * Creazione di alcuni dati iniziali <br>
     * Rinfresca la griglia <br>
     */
    public void clickReset() {
        String collection = annotation.getCollectionName(entityClazz);
        String message;
        int numRec;
        String type;

        AIResult result = entityService.reset();
        if (result.isValido()) {
            numRec = result.getValue();
            type = result.getMessage();
            message = String.format("Nella collezione %s sono stati re-inseriti %d elementi %s", collection, numRec, type);
            logger.log(AETypeLog.reset, message);
            this.refreshGrid();
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
    @Deprecated
    private boolean resetDeletingAll() {
        AIResult result = null;
        entityService.delete();
        //        result = entityService.resetEmptyOnly();

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
            items = ((MongoService) mongo).findAll(entityClazz);//@todo da controllare
            grid.getGrid().deselectAll();
            grid.setItems(items);
            grid.getGrid().getDataProvider().refreshAll();
        }
    }

    protected void addLink(final String http, final String title) {
        Span span = html.getSpanBlu(title, AETypeWeight.bold);
        if (alertList != null) {
            alertList.add(new Anchor(http, span));
        }
    }

    protected void export() {
        Grid grid = new Grid(entityClazz, false);
        grid.setColumns("nome");
        grid.setItems(((MongoService) mongo).findAll(entityClazz));//@todo da controllare

        String message = "Export";
        InputStreamFactory factory = Exporter.exportAsExcel(grid);
        StreamResource streamRes = new StreamResource(message + ".xls", factory);

        Anchor anchorEsporta = new Anchor(streamRes, "Download");
        anchorEsporta.getElement().setAttribute("style", "color: red");
        anchorEsporta.getElement().setAttribute("Export", true);
        Button button = new Button(new Icon(VaadinIcon.DOWNLOAD_ALT));
        button.getElement().setAttribute("style", "color: red");
        anchorEsporta.add(button);
        //        exportPlaceholder.removeAll();
        //        exportPlaceholder.add(anchorEsporta);

    }

}
