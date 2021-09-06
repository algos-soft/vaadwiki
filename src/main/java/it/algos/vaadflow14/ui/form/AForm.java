package it.algos.vaadflow14.ui.form;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.customfield.*;
import com.vaadin.flow.component.formlayout.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.shared.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadflow14.ui.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import javax.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: ven, 22-mag-2020
 * Time: 17:18
 * <p>
 * Scheda di dettaglio <br>
 * Può essere inserita in un dialogo oppure in una view <br>
 * I bottoni sono gestiti dal service e non da questa classe <br>
 * La scheda grafica è composta da due diversi FormLayout sovrapposti in modo da poter avere due diverse suddivisioni
 * di colonne. Tipicamente due nella prima ed una sola nella seconda <br>
 */
public abstract class AForm extends VerticalLayout {

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnotationService annotation;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ReflectionService reflection;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

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
    public ArrayService array;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AFieldService fieldService;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ClassService classService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BeanService beanService;


    @Autowired
    public AIMongoService mongo;

    /**
     * Mappa di tutti i fields del form <br>
     * La chiave è la propertyName del field <br>
     * Serve per recuperarli dal nome per successive elaborazioni <br>
     */
    public HashMap<String, AField> fieldsMap;

    /**
     * La scheda grafica è composta da due diversi FormLayout sovrapposti <br>
     */
    protected FormLayout topLayout;

    /**
     * La scheda grafica è composta da due diversi FormLayout sovrapposti <br>
     * L'uso di questo layout è regolata dal parametro 'usaTopLayout', di default 'true' <br>
     */
    protected boolean usaTopLayout;

    /**
     * La scheda grafica è composta da due diversi FormLayout sovrapposti <br>
     * I FormLayout.ResponsiveStep sono regolati nella property 'stepTopLayout' (integer) <br>
     * Tipicamente il primo FormLayout ha una colonna <br>
     */
    protected int stepTopLayout;

    /**
     * La scheda grafica è composta da due diversi FormLayout sovrapposti <br>
     */
    protected FormLayout bottomLayout;

    /**
     * La scheda grafica è composta da due diversi FormLayout sovrapposti <br>
     * L' uso di questo layout è regolata dal parametro 'usaBottomLayout', di default 'false' <br>
     */
    protected boolean usaBottomLayout;

    /**
     * La scheda grafica è composta da due diversi FormLayout sovrapposti <br>
     * I FormLayout.ResponsiveStep sono regolati nella property 'stepBottomLayout' (integer) <br>
     * Tipicamente il secondo FormLayout ha una sola colonna <br>
     */
    protected int stepBottomLayout;

    /**
     * Preferenza per la larghezza 'minima' del Form. Normalmente "50em". <br>
     */
    protected String minWidthForm;

    /**
     * The Entity Bean  (obbligatorio  per il form)
     */
    protected AEntity entityBean;

    //--collegamento tra i fields e la entityBean
    public Binder binder;

    /**
     * The Entity Class  (obbligatorio per liste e form)
     */
    protected Class<? extends AEntity> entityClazz;

    /**
     * Lista ordinata di tutti i fields del form <br>
     * Serve per presentarli (ordinati) dall' alto in basso nel form <br>
     */
    protected List<AField> fieldsList;

    /**
     * The Entity Service (obbligatorio)
     */
    protected AIService entityService;

    /**
     * The Entity Logic (obbligatorio)
     */
    protected AILogic entityLogic;

    protected WrapForm wrap;

    /**
     * Tipologia di Form in uso <br>
     */
    protected AEOperation operationForm;

    protected boolean usaFieldNote = false;

    protected List<String> fieldsNameList;

    protected AComboField fieldMaster;

    protected AComboField fieldSlave;

    protected Registration master;

    protected Registration slave;

    private LinkedHashMap<String, List> enumMap;

    @Deprecated
    public AForm( WrapForm wrap) {
        System.err.println("È stato chiamato il costruttore AForm con parametri errati. Non può funzionare");
    }

    @Deprecated
    public AForm() {
        System.err.println("È stato chiamato il costruttore AForm senza parametri. Non può funzionare");
    }


    public AForm(AIService entityService, AILogic entityLogic, WrapForm wrap) {
        this.entityService = entityService;
        this.entityLogic = entityLogic;
        this.wrap = wrap;
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le (eventuali) istanze @Autowired <br>
     * Questo metodo viene chiamato subito dopo che il framework ha terminato l' init() implicito <br>
     * del costruttore e PRIMA di qualsiasi altro metodo <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l' ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        this.fixParameters();
        this.fixPreferenze();
        this.fixProperties();
        this.fixView();
    }


    /**
     * Regola i parametri arrivati col wrapper <br>
     */
    private void fixParameters() {
        if (wrap != null) {
            this.entityClazz = wrap.getEntityClazz();
            this.entityBean = wrap.getEntityBean();
            this.fieldsNameList = wrap.getFieldsName();
            this.fieldsMap = wrap.getFieldsMap();
            this.enumMap = wrap.getEnumMap();
            this.operationForm = wrap.getOperationForm();
        }
    }


    /**
     * Preferenze standard <br>
     * Normalmente il primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        if (wrap != null) {
            this.usaTopLayout = wrap.isUsaTopLayout();
            this.stepTopLayout = wrap.getStepTopLayout();
            this.usaBottomLayout = wrap.isUsaBottomLayout();
            this.stepBottomLayout = wrap.getStepBottomLayout();
            this.minWidthForm = wrap.getMinWidthForm();
        }
    }


    /**
     * Regola alcune properties (grafiche e non grafiche) <br>
     * Regola la business logic di questa classe <br>
     */
    protected void fixProperties() {
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);

        if (usaTopLayout) {
            this.topLayout = new FormLayout();
            this.topLayout.addClassName("no-padding");
            this.fixColonne(topLayout, stepTopLayout, minWidthForm);
            this.add(topLayout);
        }

        if (usaBottomLayout) {
            this.bottomLayout = new FormLayout();
            this.bottomLayout.addClassName("no-padding");
            this.fixColonne(bottomLayout, stepBottomLayout, minWidthForm);
            this.add(bottomLayout);
        }

        if (entityClazz == null && entityBean != null) {
            entityClazz = entityBean.getClass();
        }

        if (entityClazz != null) {
            //            entityLogic = entityLogic != null ? entityLogic : classService.getLogicFromEntityClazz(entityClazz); //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
        }

        if (entityBean == null && entityService != null) {
            entityBean = entityService.newEntity();
        }

        if (entityClazz == null) {
            logger.warn("Manca la entityClazz", this.getClass(), "fixProperties");
            return;
        }

        this.usaFieldNote = annotation.usaNote(entityClazz);

        //--Crea un nuovo binder (vuoto) per questo Form e questa entityBean
        binder = new Binder(entityClazz);
    }


    /**
     * Costruisce graficamente la scheda <br>
     * <p>
     * Crea i fields e li posiziona in una mappa <br>
     * Associa i valori di entityBean al binder. Dal DB alla UI <br>
     * Aggiunge ogni singolo field della fieldMap al layout <br>
     */
    protected void fixView() {
        //--Crea in automatico i fields normali associati al binder
        //--Li aggiunge al binder
        //--Li aggiunge alla fieldsList
        this.creaFieldsBinder();

        //--Sincronizza il binder all' apertura della scheda
        //--Trasferisce (binder read) i valori dal DB alla UI
        binder.readBean((AEntity) entityBean);

        //--Eventuali fields aggiunti extra binder
        //--Li aggiunge alla fieldsList
        this.creaFieldsExtra();

        //--Riordina (eventualmente) la lista fieldsList,
        //--I fieldsExtra vengono necessariamente inseriti DOPO i fields normali mentre, magari, devono apparire prima
        this.reorderFieldList();

        //--Aggiunge ogni singolo field della lista fieldsList al layout grafico
        this.addFieldsToLayout();

        //--Crea una mappa fieldMap, per recuperare i fields dal nome
        this.creaMappaFields();

        //--Regola in lettura eventuali fields extra non associati al binder. Dal DB alla UI
        this.readFieldsExtra();

        //--Eventuali aggiustamenti finali al layout
        //--Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
        //--Regola eventuali valori delle property in apertura della scheda
        this.fixLayoutFinal();
    }


    /**
     * Crea in automatico i fields normali associati al binder <br>
     * Aggiunge i fields normali al binder <br>
     * Trasferisce (binder read) i valori dal DB alla UI <br>
     * Li aggiunge alla fieldsList <br>
     * Lista ordinata di tutti i fields normali del form <br>
     * Serve per presentarli (ordinati) dall' alto in basso nel form <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void creaFieldsBinder() {
        Field reflectionJavaField;
        AField field = null;
        fieldsNameList = fieldsNameList != null ? fieldsNameList : getPropertyNamesList();

        if (array.isAllValid(fieldsNameList)) {
            fieldsList = new ArrayList<>();
            for (String fieldKey : fieldsNameList) {
                field = fieldService.crea(entityBean, binder, operationForm, fieldKey);
                if (field != null) {
                    fieldsList.add(field);
                }
                else {
                    AETypeField type = annotation.getFormType(reflection.getField(entityBean.getClass(), fieldKey));
                    logger.warn("Non sono riuscito a creare il field " + fieldKey + " di type " + type, this.getClass(), "creaFieldsBinder");
                }

                //                reflectionJavaField = reflection.getField(entityBean.getClass(), fieldKey);
                //                field = fieldService.creaOnly(reflectionJavaField);
                //                if (field != null) {
                //                    fieldService.addToBinder(entityBean, binder, operationForm, reflectionJavaField, field);
                //                    fieldsList.add(field);
                //                    //                    binder.forField(field).bind(fieldKey);
                //                } else {
                //                    AETypeField type = annotation.getFormType(reflection.getField(entityBean.getClass(), fieldKey));
                //                    logger.warn("Non sono riuscito a creare il field " + fieldKey + " di type " + type, this.getClass(), "creaFieldsBinder");
                //                }
            }
        }
    }


    /**
     * Costruisce una lista ordinata di nomi delle properties del Form. <br>
     * La lista viene usata per la costruzione automatica dei campi e l' inserimento nel binder <br>
     * Nell' ordine: <br>
     * 1) Cerca nell' annotation @AIForm della Entity e usa quella lista (con o senza ID) <br>
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse) <br>
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxLogic <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Se serve, modifica l' ordine della lista oppure esclude una property che non deve andare nel binder <br>
     *
     * @return lista di nomi di properties
     */
    protected List<String> getPropertyNamesList() {
        return entityLogic.getFormPropertyNamesList();
    }


    /**
     * Crea gli eventuali fields extra NON associati al binder, oltre a quelli normali <br>
     * Li aggiunge alla fieldsList <br>
     * Lista ordinata di tutti i fields normali del form <br>
     * Serve per presentarli (ordinati) dall' alto in basso nel form <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void creaFieldsExtra() {
        AField field = null;

        if (usaFieldNote) {
            field = fieldService.crea(entityBean, binder, operationForm, FlowCost.FIELD_NOTE);
            if (field != null) {
                fieldsList.add(field);
            }
        }
    }


    /**
     * Riordina (eventualmente) la lista fieldsList <br>
     * I fieldsExtra vengono necessariamente inseriti DOPO i fields normali mentre potrebbero dover apparire prima <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void reorderFieldList() {
    }


    /**
     * Aggiunge ogni singolo field della lista fieldsList al layout <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void addFieldsToLayout() {
        topLayout.removeAll();
        Component comp;

        if (array.isAllValid(fieldsList)) {
            for (CustomField field : fieldsList) {
                topLayout.add(field);

                //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE la posizione del campo Note
                //                    if (text.isValid(field.getKey()) && field.getKey().equals(FlowCost.FIELD_NOTE)) {
                //                        if (usaBottomLayout) {
                //                            bottomLayout.add(comp);
                //                        } else {
                //                            topLayout.add(comp);
                //                        }
                //                    } else {
                //                        topLayout.add(comp);
                //                    }
            }
        }
        else {
            logger.warn("La fieldsList è vuota", this.getClass(), "addFieldsToLayout");
        }
    }


    /**
     * Crea una mappa fieldMap, per recuperare i fields dal nome <br>
     */
    protected void creaMappaFields() {
        if (array.isAllValid(fieldsList)) {
            fieldsMap = new HashMap<String, AField>();

            for (AField field : fieldsList) {
                fieldsMap.put(field.getKey(), field);
            }
        }
    }


    /**
     * Regola in lettura eventuali valori NON associati al binder. <br>
     * Dal DB alla UI <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void readFieldsExtra() {
        CustomField field = null;

        if (usaFieldNote) {
            if (fieldsMap != null) {
                field = fieldsMap.get(FlowCost.FIELD_NOTE);
                if (field != null) {
                    if (text.isValid(entityBean.note)) {
                        field.setValue(entityBean.note);
                    }
                }
            }
        }
    }


    /**
     * Eventuali aggiustamenti finali al layout <br>
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap) <br>
     * Regola eventuali valori delle property in apertura della scheda <br>
     * Può essere sovrascritto <br>
     */
    protected void fixLayoutFinal() {
    }


    /**
     * Sincronizza il funzionamento di due AComboField <br>
     * Il primo è 'master', il secondo 'slave' <br>
     * Quando si modifica il valore del master, lo slave viene regolato di conseguenza <br>
     * Quando si modifica il valore dello slave, il master riceve gli items relativi <br>
     */
    protected void fixDueCombo(String masterName, String slaveName) {
        fieldMaster = (AComboField) fieldsMap.get(masterName);
        master = fieldMaster.addValueChangeListener(event -> sincroMaster(event));

        fieldSlave = (AComboField) fieldsMap.get(slaveName);
        slave = fieldSlave.addValueChangeListener(event -> sincroSlave(event));
    }


    /**
     * Evento generato dal AComboField 'master' <br>
     * DEVE essere sovrascritto <br>
     */
    protected void sincroMaster(HasValue.ValueChangeEvent event) {
    }


    /**
     *
     */
    protected void sincroDueCombo(AEntity masterValue, AEntity slaveValue) {
        if (!masterValue.id.equals(slaveValue.id)) {
            slave.remove();
            fieldSlave.setValue(masterValue);
            slave = fieldSlave.addValueChangeListener(eventSlave -> sincroSlave(eventSlave));
        }
    }


    /**
     * Evento generato dal AComboField 'slave' <br>
     * DEVE essere sovrascritto <br>
     */
    protected void sincroSlave(HasValue.ValueChangeEvent event) {
    }


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void writeFieldsExtra() {
    }


    /**
     * Get the current valid entity <br>
     * Use the returned instance for further operations <br>
     *
     * @return the checked entity
     */
    public AEntity getValidBean() {
        writeFieldsExtra();

        try {
            //--associa i fields del binder alla entityBean. Dalla UI alla business logic
            if (binder.writeBeanIfValid( entityBean)) {
                return entityBean;
            }
            else {
                BinderValidationStatus<AEntity> status = binder.validate();
                Notification.show(status.getValidationErrors().stream()
                        .map(ValidationResult::getErrorMessage)
                        .collect(Collectors.joining("; ")), 3000, Notification.Position.BOTTOM_START);
                return null;
            }
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "getValidBean");
            return null;
        }
    }


    /**
     * Regola i ResponsiveStep (colonne) del FormLayout indicato <br>
     *
     * @param formLayout   componente grafico Form
     * @param step         numero di 'colonne'
     * @param minWidthForm larghezza minima
     */
    public void fixColonne(FormLayout formLayout, int step, String minWidthForm) {
        if (step == 1) {
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep(minWidthForm, 1));
        }
        if (step == 2) {
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep(minWidthForm, 2));
        }
    }


    public boolean isModificato() {
        try {
            binder.writeBean(entityBean);
        } catch (Exception unErrore) {
            int a = 87;
        }
        return beanService.isModificata(entityBean);
    }

    public HashMap<String, AField> getFieldsMap() {
        return fieldsMap;
    }

    public AField getField(String name) {
        return fieldsMap != null ? fieldsMap.get(name) : null;
    }

    public Binder getBinder() {
        return binder;
    }

    public FormLayout getTopLayout() {
        return topLayout;
    }

}
