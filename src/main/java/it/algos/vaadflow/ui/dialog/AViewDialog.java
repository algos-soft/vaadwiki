package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.shared.Registration;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.IAView;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.AIntegerField;
import it.algos.vaadflow.ui.fields.ATextArea;
import it.algos.vaadflow.ui.fields.ATextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 05-mag-2018
 * Time: 19:10
 */
@Slf4j
public abstract class AViewDialog<T extends Serializable> extends Dialog implements IADialog {


    protected final Button saveButton = new Button(REGISTRA);
    protected final Button cancelButton = new Button(ANNULLA);
    protected final Button deleteButton = new Button(DELETE);
    protected final FormLayout formLayout = new FormLayout();
    protected final HorizontalLayout buttonBar = new HorizontalLayout(saveButton, cancelButton, deleteButton);
    private final H2 titleField = new H2();
    private final String confirmText = "Conferma";
    private final ConfirmationDialog<T> confirmationDialog = new ConfirmationDialog<>();
    public Consumer<T> itemAnnulla;
    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public AAnnotationService annotation;
    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public ADateService date;
    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AReflectionService reflection;
    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected PreferenzaService pref;
    /**
     * Service iniettato da Spring (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
//    @Autowired
//    public CompanyService companyService;
    protected IAService service;
    protected IAPresenter presenter;
    //--collegamento tra i fields e la entityBean
    protected Binder<T> binder;
    protected Class binderClass;
    protected LinkedHashMap<String, AbstractField> fieldMap;
    protected AFieldService fieldService;
    protected T currentItem;
    protected Operation operation;
    protected BiConsumer<T, Operation> itemSaver;
    protected AComboBox companyField;
    private Consumer<T> itemDeleter;
    private String itemType;
    private Registration registrationForSave;

    /**
     * Constructs a new instance.
     *
     * @param presenter per gestire la business logic del package
     */
    public AViewDialog(IAPresenter presenter) {
        this(presenter, null, null);
    }// end of constructor

    /**
     * Constructs a new instance.
     *
     * @param presenter   per gestire la business logic del package
     * @param itemSaver   funzione associata al bottone 'registra'
     * @param itemDeleter funzione associata al bottone 'annulla'
     */
    @Deprecated
    public AViewDialog(IAPresenter presenter, BiConsumer<T, Operation> itemSaver, Consumer<T> itemDeleter) {
        this(presenter, itemSaver, itemDeleter, null, false);
    }// end of constructor


    /**
     * Constructs a new instance.
     *
     * @param presenter               per gestire la business logic del package
     * @param itemSaver               funzione associata al bottone 'registra'
     * @param itemDeleter             funzione associata al bottone 'annulla'
     * @param itemAnnulla             funzione associata al bottone 'annulla'
     * @param confermaSenzaRegistrare cambia il testo del bottone 'Registra' in 'Conferma'
     */
    @Deprecated
    public AViewDialog(IAPresenter presenter, BiConsumer<T, Operation> itemSaver, Consumer<T> itemDeleter, Consumer<T> itemAnnulla, boolean confermaSenzaRegistrare) {
        this.presenter = presenter;
        this.service = presenter.getService();
        this.itemSaver = itemSaver;
        this.itemDeleter = itemDeleter;
        this.itemAnnulla = itemAnnulla;
        this.binderClass = presenter.getEntityClazz();
        this.fieldService = presenter.getService().getFieldService();

        if (confermaSenzaRegistrare) {
            this.saveButton.setText(confirmText);
        }// end of if cycle

        initTitle();
        initFormLayout();
        initButtonBar();
//        creaFields();

        //--Eventuali aggiustamenti finali al layout
        fixLayout();

        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);
        addOpenedChangeListener(event -> {
            if (!isOpened()) {
                getElement().removeFromParent();
            }
        });
    }// end of constructor


    public void fixFunzioni(BiConsumer<T, AViewDialog.Operation> itemSaver, Consumer<T> itemDeleter) {
        fixFunzioni(itemSaver, itemDeleter, null);
    }// end of method

    public void fixFunzioni(BiConsumer<T, AViewDialog.Operation> itemSaver, Consumer<T> itemDeleter, Consumer<T> itemAnnulla) {
        this.itemSaver = itemSaver;
        this.itemDeleter = itemDeleter;
        this.itemAnnulla = itemAnnulla;
    }// end of method


    public void fixConfermaAndNotRegistrazione() {
        this.saveButton.setText(confirmText);
    }// end of method


    private void initTitle() {
        add(titleField);
    }

    private void initFormLayout() {
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("50em", 2));
        formLayout.addClassName("no-padding");
        Div div = new Div(formLayout);
        div.addClassName("has-padding");
        add(div);
    }// end of method

    protected void initButtonBar() {
        saveButton.getElement().setAttribute("theme", "primary");
        cancelButton.addClickListener(e -> close());
        deleteButton.addClickListener(e -> deleteClicked());
        deleteButton.getElement().setAttribute("theme", "tertiary danger");
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);
        buttonBar.setMargin(true);
        add(buttonBar);
    }// end of method

    /**
     * Crea i fields (non esiste ancora la entityBean, che arriva nel metodo open())
     * <p>
     * Crea un nuovo binder (vuoto) per questo Dialog e questa Entity
     * Crea una mappa fieldMap (vuota), per recuperare i fields dal nome
     * Costruisce una lista di nomi delle properties. Ordinata. Sovrascrivibile.
     * <p>
     * Costruisce i fields (di tipo AbstractField) della lista, in base ai reflectedFields ricevuti dal service
     * Inizializza le properties grafiche (caption, visible, editable, width, ecc)
     * Aggiunge i fields al binder
     * Aggiunge i fields alla mappa fieldMap
     * <p>
     * Aggiunge eventuali fields specifici (costruiti non come standard type) al binder ed alla fieldMap
     * Aggiunge i fields della fieldMap al layout grafico
     * Aggiunge eventuali fields specifici direttamente al layout grafico (senza binder e senza fieldMap)
     */
    private void creaFields() {
        List<String> formPropertyNamesList;
        AbstractField propertyField = null;

        //--controllo iniziale di sicurezza
        if (service == null) {
            return;
        }// end of if cycle

        //--Crea un nuovo binder (vuoto) per questo Dialog e questa Entity
        binder = new Binder(binderClass);

        //--Crea una mappa fieldMap (vuota), per recuperare i fields dal nome
        fieldMap = new LinkedHashMap<>();

        //--Costruisce una lista di nomi delle properties del Form nell'ordine:
        //--1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
        //--2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
        //--3) Sovrascrive la lista nella sottoclasse specifica di xxxService
        formPropertyNamesList = service != null ? service.getFormPropertyNamesList((AEntity) currentItem) : null;

        //--Costruisce ogni singolo field
        //--Aggiunge il field al binder, nel metodo create() del fieldService
        //--Aggiunge il field ad una fieldMap, per recuperare i fields dal nome
        for (String propertyName : formPropertyNamesList) {
            propertyField = fieldService.create(binder, binderClass, propertyName);
            if (propertyField != null) {
                fieldMap.put(propertyName, propertyField);
            }// end of if cycle
        }// end of for cycle

        //--Costruisce eventuali fields specifici (costruiti non come standard type)
        //--Aggiunge i fields specifici al binder (facoltativo, alcuni fields non funzionano col binder)
        //--Aggiunge i fields specifici alla fieldMap (obbligatorio)
        addSpecificAlgosFields();

        //--Aggiunge ogni singolo field della fieldMap al layout grafico
        addFieldsToLayout();

        //--Eventuali aggiustamenti finali al layout
        //--Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
        fixLayout();

        //--Controlla l'esistenza del field company e ne regola i valori
        fixCompanyField();

        //--Regola il focus iniziale
        fixFocus();
    }// end of method


//    /**
//     * Costruisce una lista di nomi delle properties nell'ordine:
//     * 1) Cerca nell'annotation @AIForm della Entity
//     * 2) Utilizza tutte le properties della Entity (e delle sue superclassi)
//     * 3) Sovrascrive la lista nel metodo getSpecificFormPropertiesName() della sottoclasse specifica
//     */
//    private List<String> getFormPropertiesNameList() {
//        List<String> properties = null;
//
//        if (service != null) {
//            properties = service.getFormPropertiesName();
//        }// end of if cycle
//
//        return getSpecificFormPropertiesName(properties);
//    }// end of method


    /**
     * Costruisce una lista di nomi delle properties nella sottoclasse specifica <br>
     * Se serve, modifica l'ordine della lista <br>
     * Sovrasritto nella sottoclasse <br>
     */
    protected List<String> getSpecificFormPropertiesName(List<String> properties) {
        return properties;
    }// end of method


    /**
     * Costruisce eventuali fields specifici (costruiti non come standard type)
     * Aggiunge i fields specifici al binder
     * Aggiunge i fields specifici alla fieldMap
     * Sovrascritto nella sottoclasse
     */
    protected void addSpecificAlgosFields() {
    }// end of method


    /**
     * Aggiunge ogni singolo field della fieldMap al layout grafico
     */
    protected void addFieldsToLayout() {
        getFormLayout().removeAll();
        for (String name : fieldMap.keySet()) {
            getFormLayout().add(fieldMap.get(name));
        }// end of for cycle
    }// end of method

    /**
     * Eventuali aggiustamenti finali al layout
     * Aggiunge eventuali altri componenti direttamente al layout grafico (senza binder e senza fieldMap)
     * Sovrascritto nella sottoclasse
     */
    protected void fixLayout() {
    }// end of method


    /**
     * Controlla l'esistenza del field company e ne regola i valori
     * Il field company esiste solo se si verificano contemporaneamente i seguenti:
     * 1) l'applicazione usa multiCompany
     * 2) la entity usa company
     * 3) siamo collegati (login) come developer
     */
    protected void fixCompanyField() {
//        companyField = (AComboBox) getField(AService.FIELD_NAME_COMPANY);
//        if (companyField != null) {
//            List items = companyService.findAll();
//            companyField.setItems(items);
//            companyField.setEnabled(false);
//        }// end of if cycle
    }// end of method

    /**
     * Regola il focus iniziale
     * Spazzola tuti i fields e legge le @annotation per vedere se c'è un field col focus
     * Se manca, prende il primo field
     */
    private void fixFocus() {
        boolean hasFocus = false;
        AbstractField field;
        AbstractField firstField = null;

        for (String fieldName : fieldMap.keySet()) {
            if (firstField == null) {
                firstField = fieldMap.get(fieldName);
            }// end of if cycle

            hasFocus = annotation.isFocus(binderClass, fieldName);
            if (hasFocus) {
                field = fieldMap.get(fieldName);
                if (field instanceof ATextField) {
                    ((ATextField) field).focus();
                }// end of if cycle
                if (field instanceof AIntegerField) {
                    ((AIntegerField) field).focus();
                }// end of if cycle
                break;
            }// end of if cycle
        }// end of for cycle

        if (!hasFocus && firstField != null) {
            if (firstField instanceof ATextField) {
                ((ATextField) firstField).focus();
            }// end of if cycle
            if (firstField instanceof AIntegerField) {
                ((AIntegerField) firstField).focus();
            }// end of if cycle
        }// end of if cycle

    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created
     *                  instance
     * @param operation The operation being performed on the item
     */
    @Override
    public void open(AEntity item, AViewDialog.Operation operation) {
        open(item, operation, "");
    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     * Legge la entityBean, ed inserisce i valori nel binder
     * Legge la entityBean ed inserisce nella UI i valori di eventuali fields NON associati al binder
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     */
    @Override
    public void open(AEntity item, AViewDialog.Operation operation, String title) {
        if (((AService) service).mancaCompanyNecessaria()) {
            Notification.show("Non è stata selezionata nessuna company in AViewDialog.open()", 3000, Notification.Position.BOTTOM_START);
            return;
        }// end of if cycle
        if (item == null) {
            Notification.show("Qualcosa non ha funzionato in AViewDialog.open()", 3000, Notification.Position.BOTTOM_START);
            return;
        }// end of if cycle

        this.currentItem = (T) item;
        this.operation = operation;
        Object view = presenter.getView();
        if (view != null) {
            this.itemType = presenter.getView().getName();
        }// end of if cycle
        title = title.equals("") ? itemType : title;
        titleField.setText(operation.getNameInTitle() + " " + title);

        if (registrationForSave != null) {
            registrationForSave.remove();
        }
        registrationForSave = saveButton.addClickListener(e -> saveClicked(operation));


        creaFields();
        binder.readBean(currentItem);
        readSpecificFields();
        readCompanyField();

        deleteButton.setEnabled(operation.isDeleteEnabled());

        if (this.operation == Operation.SHOW) {
            saveButton.setVisible(false);
            deleteButton.setVisible(false);
        }// end of if cycle

        open();
    }// end of method


    /**
     * Regola in lettura eventuali valori NON associati al binder
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readSpecificFields() {
//        AbstractField field;
//        Object genericValue;
//
//        for (Map.Entry<String, AbstractField> entry : fieldMap.entrySet()) {
//            field = entry.getValue();
//            genericValue = field.getValue();
//            AEntity alfa=(AEntity) currentItem;
//            if (field instanceof ADatePicker) {
//                genericValue= ((Versione)currentItem).timestamp;
////                genericValue = field.getValue();
//                if (genericValue!=null) {
//                    genericValue = date.localDateTimeToLocalDate((LocalDateTime) genericValue);
//                    field.setValue(genericValue);
//                }// end of if cycle
//            }// end of if cycle
//
//        }// end of for cycle
//
    }// end of method


    /**
     * Regola in lettura l'eeventuale field company (un combo)
     * Dal DB alla UI
     * Sovrascritto
     */
    protected void readCompanyField() {
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder
     * Dalla  UI al DB
     * Sovrascritto
     */
    protected void writeSpecificFields() {
    }// end of method


    protected void focusOnPost(String currentFieldName) {
        List<String> keys = new ArrayList<>(fieldMap.keySet());
        String nameFocus = "";
        String nameTmp;
        int pos = 0;

        for (int k = 0; k < keys.size(); k++) {
            nameTmp = keys.get(k);
            if (nameTmp.equals(currentFieldName)) {
                pos = keys.indexOf(nameTmp);
                pos++;
                pos = pos < keys.size() ? pos : 0;
                nameFocus = keys.get(pos);
            }// end of if cycle
        }// end of for cycle

        AbstractField field = getField(nameFocus);
        if (field instanceof ATextField) {
            ((ATextField) getField(nameFocus)).focus();
        }// end of if cycle
        if (field instanceof ATextArea) {
            ((ATextArea) getField(nameFocus)).focus();
        }// end of if cycle
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Registra
     * Inizio delle operazioni di registrazione
     */
    protected void saveClicked(AViewDialog.Operation operation) {
        boolean isValid = false;
        if (currentItem != null) {
            isValid = binder.writeBeanIfValid(currentItem);
        }// end of if cycle

        if (isValid) {
            writeSpecificFields();
            itemSaver.accept(currentItem, operation);
            close();
        } else {
            BinderValidationStatus<T> status = binder.validate();
            Notification.show(status.getValidationErrors().stream()
                    .map(ValidationResult::getErrorMessage)
                    .collect(Collectors.joining("; ")), 3000, Notification.Position.BOTTOM_START);
        }
    }// end of method


    /**
     * Azione proveniente dal click sul bottone Cancella (delete)
     */
    private void deleteClicked() {
        if (confirmationDialog.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(confirmationDialog));
        }
        confirmDelete();
    }// end of method

    /**
     * Azione proveniente dal click sul bottone Annulla
     */
    public void close() {
        super.close();
        IAView vista = presenter.getView();
        if (vista != null) {
            vista.updateView();
        }// end of if cycle
    }// end of method

//    /**
//     * Opens the confirmation dialog before deleting the current item.
//     * <p>
//     * The dialog will display the given title and message(s), then call
//     * {@link #deleteConfirmed(Serializable)} if the Delete button is clicked.
//     *
//     * @param title             The title text
//     * @param message           Detail message (optional, may be empty)
//     * @param additionalMessage Additional message (optional, may be empty)
//     */
//    protected final void openConfirmationDialog(String title, String message, String additionalMessage) {
//        close();
//        confirmationDialog.open(title, message, additionalMessage, "Elimina",
//                true, getCurrentItem(), this::deleteConfirmed,
//                this::open);
//    }

    protected void confirmDelete() {
        ConfirmDialog dialog = new ConfirmDialog(
                "Elimina",
                "Vuoi veramente cancellare " + getCurrentItem().toString() + "? \nL'operazione non è reversibile",
                "Elimina",
                new ComponentEventListener<ConfirmDialog.ConfirmEvent>() {
                    @Override
                    public void onComponentEvent(ConfirmDialog.ConfirmEvent confirmEvent) {
                        deleteConfirmed(getCurrentItem());
                    }
                },
                "Annulla",
                new ComponentEventListener<ConfirmDialog.CancelEvent>() {
                    @Override
                    public void onComponentEvent(ConfirmDialog.CancelEvent cancelEvent) {
                        open();
                    }
                });
        dialog.open();
    }// end of method


    private void deleteConfirmed(T item) {
        itemDeleter.accept(item);
        close();
    }// end of method


    /**
     * Gets the form layout, where additional components can be added for
     * displaying or editing the item's properties.
     *
     * @return the form layout
     */
    protected final FormLayout getFormLayout() {
        return formLayout;
    }

    /**
     * Gets the binder.
     *
     * @return the binder
     */
    protected final Binder<T> getBinder() {
        return binder;
    }

    /**
     * Gets the item currently being edited.
     *
     * @return the item currently being edited
     */
    protected final T getCurrentItem() {
        return currentItem;
    }

    /**
     * Recupera il field dal nome
     */
    protected AbstractField getField(String publicFieldName) {

        if (fieldMap != null) {
            return fieldMap.get(publicFieldName);
        } else {
            return null;
        }// end of if/else cycle

    }// end of method

    public IAPresenter getPresenter() {
        return presenter;
    }// end of method

    public void setPresenter(IAPresenter presenter) {
        this.presenter = presenter;
    }// end of method

    /**
     * The operations supported by this dialog.
     * Delete is enabled when editing an already existing item.
     */
    public enum Operation {
        ADD("Add New", "add", false),
        EDIT("Edit", "edit", true),
        SHOW("Mostra", "mostra", false);

        private final String nameInTitle;
        private final String nameInText;
        private final boolean deleteEnabled;

        Operation(String nameInTitle, String nameInText, boolean deleteEnabled) {
            this.nameInTitle = nameInTitle;
            this.nameInText = nameInText;
            this.deleteEnabled = deleteEnabled;
        }

        public String getNameInTitle() {
            return nameInTitle;
        }// end of method

        public String getNameInText() {
            return nameInText;
        }// end of method

        public boolean isDeleteEnabled() {
            return deleteEnabled;
        }// end of method
    }// end of enum

}// end of class
