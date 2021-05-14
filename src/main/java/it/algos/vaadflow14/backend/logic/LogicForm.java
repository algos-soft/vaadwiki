package it.algos.vaadflow14.backend.logic;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.icon.*;
import de.codecamp.vaadin.components.messagedialog.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
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

        //        super.usaBottoneCancella = AEPreferenza.usaMenuReset.is() && annotation.usaDelete(entityClazz);
        //        super.usaBottoneRegistra = AEPreferenza.usaMenuReset.is() && annotation.usaModifica(entityClazz);

        super.usaBottoneResetForm = false;
        super.usaBottoneBack = true;
        super.usaBottoneAnnulla = false;
        super.usaBottoneCancella = operationForm.isDeleteEnabled();
        super.usaBottoneConferma = false;
        super.usaBottoneRegistra = operationForm.isSaveEnabled();

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
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixOperationForm() {
        //--regola l'aspetto dei bottoni spostamento (se esistono)
        if (annotation.usaSpostamentoTraSchede(entityClazz) && operationForm.isPossibileUsoFrecce()) {
            sortProperty = annotation.getSortProperty(entityClazz);
            super.usaBottonePrima = true;
            super.usaBottoneDopo = true;

            //            bottone = bottomLayout.getMappaBottoni().get(AEButton.prima);
            //            if (bottone != null) {
            //                bottone.setEnabled(text.isValid(entityBeanPrevID));
            //            }
            //            bottone = bottomLayout.getMappaBottoni().get(AEButton.dopo);
            //            if (bottone != null) {
            //                bottone.setEnabled(text.isValid(entityBeanNextID));
            //            }
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
     * DEVE essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    @Override
    protected void fixAlertLayout() {
        headerSpan = appContext.getBean(AHeaderSpanForm.class, this.getSpanForm());
        super.fixAlertLayout();
    }


    /**
     * Costruisce una singola 'span' da mostrare come header della view <br>
     * Puo essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     *
     * @return un messaggio di 'span' col nome della collezione e la scheda visualizzata
     */
    protected String getSpanForm() {
        String titolo = "SCHEDA";
        String sep = SEP;

        if (entityBean != null) {
            return String.format("%s %s %s.%s", titolo, sep, entityClazz.getSimpleName(), entityBean.toString());
        }
        else {
            return String.format("%s %s %s", titolo, sep, entityClazz.getSimpleName());
        }
    }

    /**
     * Costruisce una lista di bottoni (enumeration) al Top della view <br>
     * Costruisce i bottoni come dai Flag regolati di default o nella sottoclasse <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected List<AIButton> getListaAEBottoniTop() {
        return new ArrayList<>();
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
     * Costruisce una lista di bottoni (enumeration) al Bottom della view <br>
     * Costruisce i bottoni come previsto dal flag operationForm, regolato in fixPreferenze() <br>
     * Nella sottoclasse possono essere aggiunti i bottoni specifici dell'applicazione <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected List<AIButton> getListaAEBottoniBottom() {
        List<AIButton> listaBottoni = super.getListaAEBottoniTop();

        if (usaBottoneResetForm) {
            listaBottoni.add(AEButton.resetForm);
        }
        if (usaBottoneBack) {
            listaBottoni.add(AEButton.back);
        }
        if (usaBottoneAnnulla) {
            listaBottoni.add(AEButton.annulla);
        }
        if (usaBottoneCancella) {
            listaBottoni.add(AEButton.delete);
        }
        if (usaBottoneConferma) {
            listaBottoni.add(AEButton.conferma);
        }
        if (usaBottoneRegistra) {
            listaBottoni.add(AEButton.registra);
        }
        if (usaBottonePrima) {
            listaBottoni.add(AEButton.prima);
        }
        if (usaBottoneDopo) {
            listaBottoni.add(AEButton.dopo);
        }

        return listaBottoni;
    }

    /**
     * Regolazioni finali di alcuni oggetti <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniFinali() {
        Button bottone;

        //--regola l'aspetto dei bottoni spostamento (se esistono)
        bottone = bottomLayout.getMappaBottoni().get(AEButton.prima);
        if (bottone != null) {
            bottone.setEnabled(text.isValid(entityBeanPrevID));
        }
        bottone = bottomLayout.getMappaBottoni().get(AEButton.dopo);
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
        AEAction azione = (AEAction) iAzione;

        switch (azione) {
            case resetForm:
                //                this.reloadForm(entityBean);
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
                this.deleteForm();
                this.back();
                break;
            case conferma:
            case registra:
                if (saveDaForm()) {
                    this.backToList();
                }
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
                messageDialog.addButtonToLeft().text("Back").icon(iconBack).error().onClick(e -> back()).closeOnClick();
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
        final AEntity newEntityBean = mongo.findById(entityClazz, newEntityBeanID);
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
     * La entityBean viene recuperare dal form <br>
     *
     * @return true se la entity è stata registrata o definitivamente scartata; esce dal dialogo
     * .       false se manca qualche field e la situazione è recuperabile; resta nel dialogo
     */
    public boolean saveDaForm() {
        AEntity entityBean = null;
        if (currentForm != null) {
            entityBean = currentForm.getValidBean();
        }

        return entityBean != null ? save(entityBean) : false;
    }

    /**
     * Saves a given entity.
     * Use the returned instance for further operations as the save operation
     * might have changed the entity instance completely.
     *
     * @return true se la entity è stata registrata o definitivamente scartata; esce dal dialogo
     * .       false se manca qualche field e la situazione è recuperabile; resta nel dialogo
     */
    public boolean save(final AEntity entityToSave) {
        boolean status = false;
        AEntity oldEntityBean;
        //        AEntity entityBean = beforeSave(entityToSave, operationForm);
        AEntity entityBean = entityToSave;

        if (entityBean == null) {
            return status;
        }

        //        if (beanService.isModificata(entityBean)) {
        //        }
        //        else {
        //            return true;
        //        }

        if (text.isEmpty(entityBean.id) && !(operationForm == AEOperation.addNew)) {
            logger.error("operationForm errato in una nuova entity che NON è stata salvata", LogicForm.class, "save");
            return status;
        }

        if (entityBean != null) {
            if (operationForm == AEOperation.addNew && entityBean.id == null) {
                entityBean = entityService.fixKey(entityBean);
            }
            oldEntityBean = mongo.find(entityBean);
            entityBean = mongo.save(entityBean);
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
