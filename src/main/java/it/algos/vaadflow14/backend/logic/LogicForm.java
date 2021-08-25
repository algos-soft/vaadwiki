package it.algos.vaadflow14.backend.logic;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.icon.*;
import de.codecamp.vaadin.components.messagedialog.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.form.*;
import it.algos.vaadflow14.ui.header.*;
import it.algos.vaadflow14.ui.interfaces.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 01-mar-2021
 * Time: 18:42
 */
public abstract class LogicForm extends Logic {

    /**
     * The Form Class  (obbligatoria per costruire la currentForm)
     */
    protected Class<? extends AForm> formClazz;


    protected AForm currentForm;

    protected int backSteps = -1;

    protected String sortProperty;

    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro del costruttore usato <br>
     */
    public LogicForm() {
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
    public LogicForm(final AIService entityService, final Class<? extends AEntity> entityClazz) {
        super.entityService = entityService;
        super.entityClazz = entityClazz;
    }// end of Vaadin/@Route constructor

    /**
     * Property per il tipo di view (List o Form) <br>
     * Property per il tipo di operazione (solo Form) <br>
     * Può essere sovrascritto, SENZA invocare prima il metodo della superclasse <br>
     */
    @Override
    protected void fixTypeView() {
        operationForm = routeParameter != null ? routeParameter.getOperationForm() : AEOperation.addNew;
    }

    /**
     *
     */
    @Override
    protected void fixEntityBean() {
        super.fixEntityBean();

        if (routeParameter.get(KEY_BEAN_PREV_ID) != null) {
            entityBeanPrevID = routeParameter.get(KEY_BEAN_PREV_ID);
        }

        if (routeParameter.get(KEY_BEAN_NEXT_ID) != null) {
            entityBeanNextID = routeParameter.get(KEY_BEAN_NEXT_ID);
        }
    }


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        boolean usaNew = annotation.usaNew(entityClazz);
        boolean isResetMethod = false;

        try {
            isResetMethod = entityService.getClass().getDeclaredMethod(TAG_METHOD_RESET) != null;
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "nomeDelMetodo");
        }

        //        super.usaBottoneCancella = AEPreferenza.usaMenuReset.is() && annotation.usaDelete(entityClazz);
        //        super.usaBottoneRegistra = AEPreferenza.usaMenuReset.is() && annotation.usaModifica(entityClazz);

        super.usaBottoneResetForm = false;
        super.usaBottoneBack = true;
        super.usaBottoneAnnulla = false;
        super.usaBottoneCancella = usaNew && operationForm != AEOperation.addNew;
        super.usaBottoneConferma = false;
        super.usaBottoneRegistra = usaNew || annotation.usaReset(entityClazz) && isResetMethod;

        this.fixOperationForm();
    }

    /**
     * Regolazioni iniziali di alcuni oggetti <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void regolazioniIniziali() {
        super.regolazioniIniziali();

        //--costruisce una lista (vuota) di Span per l'header della scheda
        super.spanHeaderForm = new ArrayList<>();

        //--costruisce una lista (vuota) di Component per i comandi sopra la lista
        super.mappaComponentiBottom = new LinkedHashMap<>();
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
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixOperationForm() {
        //--regola l'aspetto dei bottoni spostamento (se esistono)
        if (annotation.usaSpostamentoTraSchede(entityClazz) && operationForm.isPossibileUsoFrecce()) {
            sortProperty = annotation.getSortProperty(entityClazz);
            super.usaBottonePrima = true;
            super.usaBottoneDopo = true;

            //                        bottone = bottomLayout.getMappaBottoni().get(AEButton.prima);
            //                        if (bottone != null) {
            //                            bottone.setEnabled(text.isValid(entityBeanPrevID));
            //                        }
            //                        bottone = bottomLayout.getMappaBottoni().get(AEButton.dopo);
            //                        if (bottone != null) {
            //                            bottone.setEnabled(text.isValid(entityBeanNextID));
            //                        }
        }
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * <p>
     * Chiamato da Logic.initView() <br>
     * Nell' implementazione standard di default presenta sempre avviso col nome della collezione e la scheda visualizzata <br>
     * Recupera dal service specifico un (eventuale) avviso diverso <br>
     * Costruisce un' istanza dedicata con un solo avviso <br>
     * L'avviso è realizzato con tag html 'span' di colore fisso (verde) <br>
     */
    @Override
    protected void fixAlertLayout() {
        String back = html.bold("Back");
        String delete = html.bold("Delete");
        String save = html.bold("Save");
        String entity = entityClazz.getSimpleName();
        String usaNew = html.bold("usaNew");
        String prima = html.bold("<");
        String dopo = html.bold(">");
        String frecce = html.bold("usaSpostamentoTraSchede");
        String aREntity = html.bold("AREntity");
        String service = entityService.getClass().getSimpleName();
        String methodReset = html.bold("reset()");
        String edit = html.bold("edit");

        if (entityBean != null) {
            addSpanVerde(String.format("%s %s %s%s%s", TAG_SCHEDA, SEP, entityClazz.getSimpleName(), DUE_PUNTI, entityBean.toString()));
        }
        else {
            addSpanVerde(String.format("%s %s %s", TAG_SCHEDA, SEP, entityClazz.getSimpleName()));
        }

        this.fixAlertForm();

        String preferenza = html.bold("Preferenza");
        addSpanRosso(String.format("La visualizzazione di questi avvisi rossi si regola in %s:usaSpanHeaderRossi", preferenza));
        addSpanRosso(String.format("Bottone %s sempre presente per tornare alla lista", back));
        if (annotation.usaNew(entityClazz)) {
            addSpanRosso(String.format("Bottone %s presente se %s->@AIEntity(%s=true) e AEOperation=%s", delete, entity, usaNew, edit));
            addSpanRosso(String.format("Bottone %s presente se %s->@AIEntity(%s=true)", save, entity, usaNew));
        }
        if (annotation.usaReset(entityClazz)) {
            addSpanRosso(String.format("Bottone %s presente se %s extends %s ed esiste %s.%s", save, entity, aREntity, service, methodReset));
        }
        addSpanRosso(String.format("Frecce %s e %s presenti se %s->@AIEntity(%s=true)", prima, dopo, entity, frecce));

        if (spanHeaderForm != null && spanHeaderForm.size() > 0) {
            headerSpan = appContext.getBean(AHeaderAlertForm.class, super.spanHeaderForm);
        }

        super.fixAlertLayout();
    }

    /**
     * Costruisce una lista (eventuale) di 'span' da mostrare come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixAlertForm() {
    }

    protected void addSpanBlu(final String message) {
        if (spanHeaderForm != null) {
            spanHeaderForm.add(html.getSpanBlu(message));
        }
    }

    protected void addSpanVerde(final String message) {
        if (spanHeaderForm != null) {
            spanHeaderForm.add(html.getSpanVerde(message));
        }
    }

    protected void addSpanRosso(final String message) {
        if (spanHeaderForm != null && usaSpanHeaderRossi) {
            spanHeaderForm.add(html.getSpanRosso(message));
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
        super.creaAEBottoniTop();

        if (usaBottoneResetForm) {
            putMappa(AEButton.resetForm);
        }
        if (usaBottoneExport) {
            putMappa(AEButton.export);
        }
        if (usaBottoneDownload) {
            putMappa(AEButton.download);
        }
        if (usaBottoneUpload) {
            putMappa(AEButton.upload);
        }
        if (usaBottonePaginaWiki) {
            putMappa(AEButton.wiki);
        }
    }

    /**
     * Costruisce il corpo principale (obbligatorio) della Grid <br>
     */
    @Override
    protected void fixBodyLayout() {
        if (entityBean != null) {
            currentForm = appContext.getBean(AGenericForm.class, entityService, this, getWrapForm(entityBean));
        }
        else {
            logger.warn("Manca entityBean", this.getClass(), "fixBody");
            //            form = entityLogic.getBodyFormLayout(entityLogic.newEntity()); //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
        }

        if (bodyPlaceHolder != null && currentForm != null) {
            bodyPlaceHolder.add(currentForm);
        }
    }

    /**
     * Costruisce un wrapper di dati <br>
     * I dati sono gestiti da questa 'logic' (nella sottoclasse eventualmente) <br>
     * I dati vengono passati alla View che li usa <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     *
     * @param entityBean interessata
     *
     * @return wrapper di dati per il Form
     */
    public WrapForm getWrapForm(AEntity entityBean) {
        return new WrapForm(entityBean, operationForm);
    }

    /**
     * Costruisce una lista ordinata di nomi delle properties del Form. <br>
     * La lista viene usata per la costruzione automatica dei campi e l' inserimento nel binder <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIForm della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, senza invocare il metodo della superclasse <br>
     * Se serve, modifica l' ordine della lista oppure esclude una property che non deve andare nel binder <br>
     * todo ancora da sviluppare
     *
     * @return lista di nomi di properties
     */
    public List<String> getFormPropertyNamesList() {
        List<String> fieldsNameList = annotation.getListaPropertiesForm(entityClazz);

        if (array.isEmpty(fieldsNameList)) {
            reflection.getFieldsName(entityBean.getClass());
        }

        if (FlowVar.usaCompany && annotation.usaCompany(entityBean.getClass())) {
            fieldsNameList.add(0, FIELD_COMPANY);
        }

        return fieldsNameList;
    }

    /**
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Bottoni standard AIButton di VaadinFlow14 e della applicazione corrente <br>
     * Costruisce i bottoni come dai flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAEBottoniBottom() {
        if (usaBottoneBack) {
            mappaComponentiBottom.put(AEButton.back.testo, AEButton.back);
        }
        if (usaBottoneAnnulla) {
            mappaComponentiBottom.put(AEButton.annulla.testo, AEButton.annulla);
        }
        if (usaBottoneCancella) {
            mappaComponentiBottom.put(AEButton.delete.testo, AEButton.delete);
        }
        if (usaBottoneConferma) {
            mappaComponentiBottom.put(AEButton.conferma.testo, AEButton.conferma);
        }
        if (usaBottoneRegistra) {
            mappaComponentiBottom.put(AEButton.registra.testo, AEButton.registra);
        }
        if (usaBottonePrima) {
            mappaComponentiBottom.put(AEButton.prima.testo, AEButton.prima);
        }
        if (usaBottoneDopo) {
            mappaComponentiBottom.put(AEButton.dopo.testo, AEButton.dopo);
        }
    }

    /**
     * Regolazioni finali di alcuni oggetti <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniFinali() {
        Button bottone;

        //--regola l'aspetto dei bottoni spostamento (se esistono)
        bottone = bottomLayout.getBottone((AEButton.prima.testo));
        if (bottone != null) {
            bottone.setEnabled(text.isValid(entityBeanPrevID));
        }
        bottone = bottomLayout.getBottone((AEButton.dopo.testo));
        if (bottone != null) {
            bottone.setEnabled(text.isValid(entityBeanNextID));
        }
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
            case resetForm:
                entityService.resetForm(entityBean);
                executeRoute(entityBean.id);
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
            case back:
            case annulla:
                this.openConfirmExitForm(entityBean);
                break;
            case delete:
                if (deleteForm()) {
                    this.backToList();
                }
                break;
            case conferma:
            case registra:
                this.saveClicked();
                //                if (saveClicked()) {
                //                    this.backToList();
                //                }
                break;
            case prima:
                executeRoute(entityBeanPrevID);
                break;
            case dopo:
                executeRoute(entityBeanNextID);
                break;
            default:
                status = false;
                break;
        }

        return status;
    }

    public boolean deleteForm() {
        boolean status = false;
        AEntity entityBean = (currentForm != null) ? currentForm.getValidBean() : null;

        if (mongo.delete(entityBean)) {
            status = true;
            logger.delete(entityBean);
        }

        return status;
    }

    /**
     * Opens the confirmation dialog before exiting form. <br>
     * <p>
     * The dialog will display the given title and message(s), then call <br>
     * Può essere sovrascritto dalla classe specifica se servono avvisi diversi <br>
     */
    protected final void openConfirmExitForm(AEntity entityBean) {
        MessageDialog messageDialog;
        String message = "La entity è stata modificata. Sei sicuro di voler perdere le modifiche? L' operazione non è reversibile.";
        VaadinIcon iconBack = VaadinIcon.ARROW_LEFT;

        if (operationForm == AEOperation.addNew) {
            backToList();
            return;
        }

        if (currentForm.isModificato()) {
            if (mongo.isValid(entityClazz)) {
                messageDialog = new MessageDialog().setTitle("Ritorno alla lista").setMessage(message);
                messageDialog.addButton().text("Rimani").primary().clickShortcutEscape().clickShortcutEnter().closeOnClick();
                messageDialog.addButtonToLeft().text("Back").icon(iconBack).error().onClick(e -> backToList()).closeOnClick();
                messageDialog.open();
            }
        }
        else {
            backToList();
        }
    }

    /**
     * Azione proveniente dal click sul bottone Annulla
     */
    protected void backToList() {
        UI.getCurrent().getPage().getHistory().go(backSteps);
    }

    /**
     * Azione proveniente dal click sul bottone Annulla
     */
    protected void back() {
        super.reload();
    }

    /**
     * Lancia una @route con la visualizzazione di una singola scheda. <br>
     * Se il package usaSpostamentoTraSchede=true, costruisce una query
     * con le keyIDs della scheda precedente e di quella successiva
     * (calcolate secondo l'ordinamento previsto) <br>
     */
    protected void executeRoute(final String newEntityBeanID) {
        if (entityBean == null) {
            executeRoute(VUOTA, VUOTA, VUOTA);
            return;
        }
        backSteps += -1;
        final AEntity newEntityBean = mongo.findByIdOld(entityClazz, newEntityBeanID);
        final Object valueProperty = reflection.getPropertyValue(newEntityBean, sortProperty);
        final String beanPrevID = mongo.findPreviousID(entityClazz, sortProperty, valueProperty);
        final String beanNextID = mongo.findNextID(entityClazz, sortProperty, valueProperty);

        executeRoute(newEntityBeanID, beanPrevID, beanNextID);
    }

    protected boolean isNotPrimo() {
        return mongo.findPrevious(entityClazz, entityBean.id) != null;
    }

    protected boolean isNotUltimo() {
        return mongo.findNext(entityClazz, entityBean.id) != null;
    }

    /**
     * Save proveniente da un click sul bottone 'registra' del Form. <br>
     * Inizio delle operazioni di registrazione
     *
     * @return true se la entity è stata registrata o definitivamente scartata; esce dalla view
     * .       false se manca qualche field e la situazione è recuperabile; resta nella view
     */
    public void saveClicked() {
        //--passa al metodo del currentForm
        //--associa i fields del binder alla entityBean. Dalla UI alla business logic
        //--restituisce una entityBean solo se è valida, altrimenti null
        entityBean = currentForm != null ? currentForm.getValidBean() : null;

        if (entityBean != null) {
            //--regola in scrittura eventuali fields della UI NON associati al binder <br>
            writeSpecificFields();

            //--passa al service per la registrazione della entityBean
            try {
                entityService.save(entityBean,operationForm);
            } catch (AMongoException unErrore) {
            }

            //--chiude questa view e torna a LogicList tramite @Route
            this.backToList();
        }
    }

    /**
     * Regola in scrittura eventuali valori NON associati al binder <br>
     * Dalla  UI al DB <br>
     * Deve essere sovrascritto <br>
     */
    protected void writeSpecificFields() {
    }

    /**
     * Saves a given entity.
     * Use the returned instance for further operations as the save operation
     * might have changed the entity instance completely.
     *
     * @return true se la entity è stata registrata o definitivamente scartata; esce dalla view
     * .       false se manca qualche field e la situazione è recuperabile; resta nella view
     */
    public boolean save(final AEntity entityToSave) {
        boolean status = false;
        AEntity oldEntityBean;
        //        AEntity entityBean = beforeSave(entityToSave, operationForm);
        AEntity entityBean = entityToSave;

        if (entityBean == null) {
            return status;
        }

        if (text.isEmpty(entityBean.id) && !(operationForm == AEOperation.addNew)) {
            logger.error("operationForm errato in una nuova entity che NON è stata salvata", LogicForm.class, "save");
            return status;
        }

        if (entityBean != null) {
            if (operationForm == AEOperation.addNew && entityBean.id == null) {
                entityBean = entityService.fixKey(entityBean);
            }
            oldEntityBean = mongo.find(entityBean);
            try {
                mongo.save(entityBean);
            } catch (AMongoException unErrore) {
            }
            status = entityBean != null;
            if (status) {
                if (operationForm == AEOperation.addNew) {
                    //                    ALogService.messageSuccess(entityBean.toString() + " è stato creato"); //@todo Creare una preferenza e sostituirla qui
                    logger.nuovo(entityBean);
                }
                else {
                    //                    ALogService.messageSuccess(entityBean.toString() + " è stato modificato"); //@todo Creare una preferenza e sostituirla qui
                    logger.modifica(entityBean, oldEntityBean);
                }
            }
        }
        else {
            logger.error("Object to save must not be null", this.getClass(), "save");
        }

        if (entityBean == null) {
            if (operationForm != null) {
                switch (operationForm) {
                    case addNew:
                        logger.warn("Non sono riuscito a creare la entity ", this.getClass(), "save");
                        break;
                    case edit:
                        logger.warn("Non sono riuscito a modificare la entity ", this.getClass(), "save");
                        break;
                    default:
                        logger.warn("Switch - caso non definito", this.getClass(), "save");
                        break;
                }
            }
            else {
                logger.warn("Non sono riuscito a creare la entity ", this.getClass(), "save");
            }
        }

        return status;
    }

}
