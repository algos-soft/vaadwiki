package it.algos.vaadflow14.ui.dialog;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dialog.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.util.function.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 12-ott-2018
 * Time: 20:30
 * <p>
 * Dialogo costruito 'al volo' <br>
 * Costruttori ridotti al minimo; con e senza titolo (non obbligatorio) <br>
 * I parametri (variabili) vengono passati nel metodo open() <br>
 * Se si costruisce l'istanza con StaticContextAccessor.getBean(ADialog.class), non si possono passare i Consumer <br>
 * diventa quindi indispensabile usare un metodo successivo 'open()' per questi parametri <br>
 * <p>
 * A confirmation dialog is a simple dialog that asks the user to confirm an action.
 * They are commonly used in situations where triggering an action prematurely would lead to diminished user experience or execute an irreversible application functionality.
 * Typical examples are actions that result in additional work for the end user when triggered by accident like logging out of the application or storing wrong/incomplete user data.
 * To avoid annoying the user, confirmation dialogs should not be overused.
 * Actions that are easily reversible should therefore not require confirmation dialogs.
 * An excellent example of this would be actions that trigger navigation events.
 * <p>
 * Confirmation dialogs usually consist of the same three parts as most other dialogs.
 * A header section, a content, and a footer section.
 * The header contains the title of the dialog and most often a button to close the dialog.
 * The content section typically contains the confirmation question.
 * This can be a simple "Are you sure?" or provide more specifics about the action in question.
 * This section sometimes also contains a summary of the data related to the action.
 * The footer part comprises two buttons, one to confirm and one to cancel the operation.
 * This is an example of what a confirmation dialog might look like:
 * <p>
 * Due to the simplicity of confirmation dialogs, they are highly reusable and should be implemented as such.
 * Only three things change for different confirmation dialogs:
 * 1- The title in the header section
 * 2- The confirmation question in the content section
 * 3- The action that gets triggered once the confirm button in the footer section is pressed.
 * <p>
 * The default constructor shown here creates the three different sections of the dialog and introduces some basic styling for the dialog itself.
 * In this case, this is just setting the width and the height of the dialog.
 * The title, confirmation question and confirm button are class variables which values/actions can be set either by using a second constructor or by setting them directly using public setter methods.
 * <p>
 * In this example, the header is a horizontal layout which contains the title label as well as a close button.
 * To close the dialog simply calls the close() method.
 * This helper method also introduces some styling to the section like setting the background color and then adds the container component to the dialog.
 * The content and footer sections are created the same way.
 * <p>
 * The confirm button also closes the dialog since confirming and action also resolves the confirmation process and the dialog is no longer needed.
 * <p>
 * To attach the dialog to the UI and show it to the user simply calls the open() method of the dialog.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ADialog extends Dialog {

    protected final H2 titleField = new H2();

    protected final Div messageLabel = new Div();

    protected final Div extraMessageLabel = new Div();

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public TextService text;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public PreferenzaService pref;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AIMongoService mongo;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ALogService logger;

    public Runnable cancelHandler;

    public Runnable confirmHandler;

    /**
     * Flag di preferenza per usare il bottone Cancel. Normalmente true.
     */
    public boolean usaCancelButton;

    /**
     * Flag di preferenza per usare il bottone Confirm. Normalmente true.
     */
    public boolean usaConfirmButton;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnotationService annotation;

    //    /**
    //     * Flag di preferenza per il testo del bottone Confirm. Normalmente 'Conferma'.
    //     */
    //    protected String textConfirmlButton = "Conferma";

    /**
     * Flag di preferenza per il testo del bottone Cancel. Normalmente 'Annulla'.
     */
    protected String textCancelButton = "Annulla";

    /**
     * Corpo centrale del Dialog <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected VerticalLayout bodyPlaceHolder = new VerticalLayout();

    //    protected String message;
    //
    //    protected String additionalMessage;

    /**
     * Barra dei bottoni di comando <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected HorizontalLayout bottomLayout = new HorizontalLayout();

    /**
     * Titolo del dialogo <br>
     */
    protected String title;

    protected String message;

    protected String additionalMessage;

    protected Button cancelButton = new Button(textCancelButton);

    protected Button confirmButton = new Button(VUOTA);

    protected AETypeTheme confirmTheme;

    protected VaadinIcon confirmIcon;

    protected String confirmText;

    /**
     * Service (@Scope = 'singleton') iniettato dal costruttore @Autowired di Spring <br>
     * Unico per tutta l'applicazione. Usato come libreria. <br>
     */
    @Autowired
    protected AVaadinService vaadinService;

    /**
     * Costruttore <br>
     */
    public ADialog() {
        this("");
    }// end of constructor


    /**
     * Costruttore <br>
     */
    public ADialog(String title) {
        this.title = title;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizia() {
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(true);
        this.getElement().getClassList().add("confirm-dialog");

        //--preferenze standard. Possono essere modificate anche selezionando la firma di open(...)
        fixPreferenze();

        //--Titolo placeholder del dialogo
        this.add(titleField);

        //--spazio per distanziare i bottoni sottostanti
        this.add(new H2());

        //--Corpo centrale del Dialog
        this.add(bodyPlaceHolder);

        //--spazio per distanziare i bottoni sottostanti
        this.add(new H2());

        //--Barra placeholder dei bottoni, creati e regolati
        this.add(bottomLayout);

        this.creaTitleLayout();

        //--Body placeholder
        this.fixBodyLayout();

        //--Barra placeholder dei bottoni, creati e regolati
        this.fixBottomLayout();

        super.open();
    }// end of method


    /**
     * Preferenze standard. <br>
     * Possono essere modificate anche selezionando la firma di open(...)
     * Le preferenze vengono eventualmente sovrascritte nella sottoclasse
     * Invocare PRIMA il metodo della superclasse
     */
    protected void fixPreferenze() {

        //        this.usaCancelButton = pref.isBool(USA_BACK_BUTTON);
        this.usaCancelButton = true;
        this.usaConfirmButton = true;

        this.confirmTheme = AETypeTheme.primary;
        this.confirmIcon = VaadinIcon.CHECK;
        this.confirmText = KEY_BUTTON_CONFERMA;
    }// end of method


    /**
     * Titolo del dialogo <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected void creaTitleLayout() {
        if (text.isValid(title)) {
            titleField.setText(title);
        }// end of if cycle
    }// end of method

    //    /**
    //     * Apre un dialogo di 'avviso' <br>
    //     * Il title è già stato regolato dal costruttore <br>
    //     */
    //    public void open() {
    ////        this.usaCancelButton = false;
    //        this.open("", "", (Runnable) null, (Runnable) null);
    //    }// end of method


    /**
     * Rimanda alla superclasse <br>
     */
    public void open() {
        super.open();
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param message Detail message
     */
    public void open(String message) {
        this.usaCancelButton = false;
        this.open(message, "", (Runnable) null, (Runnable) null);
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param message           Detail message
     * @param additionalMessage Additional message (optional, may be empty)
     */
    public void open(String message, String additionalMessage) {
        this.usaCancelButton = false;
        this.open(message, additionalMessage, (Runnable) null, (Runnable) null);
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param message        Detail message
     * @param confirmHandler The confirmation handler function
     */
    public void open(String message, Runnable confirmHandler) {
        this.usaCancelButton = false;
        this.open(message, "", confirmHandler, (Runnable) null);
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param message           Detail message
     * @param additionalMessage Additional message (optional, may be empty)
     * @param confirmHandler    The confirmation handler function
     */
    public void open(String message, String additionalMessage, Runnable confirmHandler) {
        this.usaCancelButton = false;
        this.open(message, additionalMessage, confirmHandler, (Runnable) null);
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param message        Detail message
     * @param confirmHandler The confirmation handler function
     * @param cancelHandler  The cancellation handler function
     */
    public void open(String message, Runnable confirmHandler, Runnable cancelHandler) {
        this.open(message, "", confirmHandler, cancelHandler);
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param message           Detail message
     * @param additionalMessage Additional message (optional, may be empty)
     * @param confirmHandler    The confirmation handler function
     * @param cancelHandler     The cancellation handler function
     */
    public void open(String message, String additionalMessage, Runnable confirmHandler, Runnable cancelHandler) {
        this.confirmHandler = confirmHandler;
        this.cancelHandler = cancelHandler;

        //--Body placeholder
        this.fixBodyLayout(message, additionalMessage);

        //--Barra placeholder dei bottoni, creati e regolati
        this.fixBottomLayout();

        super.open();
    }// end of method


    /**
     * Apre un dialogo di 'avviso' <br>
     * Il title è già stato regolato dal costruttore <br>
     *
     * @param bodyLayout     contenuto del dialogo
     * @param confirmHandler The confirmation handler function
     * @param cancelHandler  The cancellation handler function
     */
    public void open(VerticalLayout bodyLayout, Runnable confirmHandler, Runnable cancelHandler) {
        this.confirmHandler = confirmHandler;
        this.cancelHandler = cancelHandler;

        //--Body placeholder
        this.fixBodyLayout(bodyLayout);

        //--Barra placeholder dei bottoni, creati e regolati
        this.fixBottomLayout();

        super.open();
    }// end of method

    //    /**
    //     * Opens the given item for editing in the dialog.
    //     * Crea i fields e visualizza il dialogo <br>
    //     *
    //     * @param entityBean  The item to edit; it may be an existing or a newly created instance
    //     * @param operation   The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
    //     * @param itemSaver   funzione associata al bottone 'accetta' ('registra', 'conferma')
    //     * @param itemDeleter funzione associata al bottone 'delete'
    //     */
    //    public void open(AEntity entityBean, EAOperation operation, BiConsumer itemSaver, Consumer itemDeleter) {
    //
    //    }


    public void open(String message, Consumer itemSaver) {
    }


    /**
     * Corpo centrale del Dialog, alternativo al Form <br>
     */
    protected void fixBodyLayout() {
        bodyPlaceHolder.setPadding(false);
        bodyPlaceHolder.setSpacing(true);
        bodyPlaceHolder.setMargin(false);
        VerticalLayout bodyLayout = new VerticalLayout();
        bodyLayout.setPadding(false);
        bodyLayout.setSpacing(true);
        bodyLayout.setMargin(false);
        bodyPlaceHolder.removeAll();

        if (text.isValid(message)) {
            messageLabel.setText(message);
            bodyLayout.add(messageLabel);
        }
        if (text.isValid(additionalMessage)) {
            extraMessageLabel.setText(additionalMessage);
            bodyLayout.add(extraMessageLabel);
        }

        bodyPlaceHolder.add(bodyLayout);
    }


    /**
     * Corpo centrale del Dialog, alternativo al Form <br>
     *
     * @param message           Detail message
     * @param additionalMessage Additional message (optional, may be empty)
     */
    protected void fixBodyLayout(String message, String additionalMessage) {
        bodyPlaceHolder.setPadding(false);
        bodyPlaceHolder.setSpacing(true);
        bodyPlaceHolder.setMargin(false);
        VerticalLayout bodyLayout = new VerticalLayout();
        bodyLayout.setPadding(false);
        bodyLayout.setSpacing(true);
        bodyLayout.setMargin(false);
        bodyPlaceHolder.removeAll();

        if (text.isValid(message)) {
            messageLabel.setText(message);
            bodyLayout.add(messageLabel);
        }// end of if cycle
        if (text.isValid(additionalMessage)) {
            extraMessageLabel.setText(additionalMessage);
            bodyLayout.add(extraMessageLabel);
        }// end of if cycle

        bodyPlaceHolder.add(bodyLayout);
    }// end of method


    /**
     * Corpo centrale del Dialog, alternativo al Form <br>
     *
     * @param bodyLayout contenuto del dialogo
     */
    protected void fixBodyLayout(VerticalLayout bodyLayout) {
        bodyPlaceHolder.removeAll();
        bodyPlaceHolder.add(bodyLayout);
    }// end of method


    /**
     * Barra dei bottoni
     */
    protected void fixBottomLayout() {
        bottomLayout.setClassName("buttons");
        bottomLayout.setPadding(false);
        bottomLayout.setSpacing(true);
        bottomLayout.setMargin(false);
        bottomLayout.setClassName("confirm-dialog-buttons");
        bottomLayout.getStyle().set("background-color", AEColor.grigio1.getEsadecimale());
        Label spazioVuotoEspandibile = new Label("");

        if (usaCancelButton) {
            cancelButton.setText(textCancelButton);
            cancelButton.getElement().setAttribute("theme", "primary");
            cancelButton.addClickListener(e -> cancellaHandler());
            cancelButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
            bottomLayout.add(cancelButton);
        }// end of if cycle

        bottomLayout.add(spazioVuotoEspandibile);

        if (usaConfirmButton) {
            confirmButton.setText(confirmText);
            if (usaCancelButton) {
                confirmButton.getElement().setAttribute("theme", confirmTheme.toString());
            }
            else {
                confirmButton.getElement().setAttribute("theme", confirmTheme.toString());
            }// end of if/else cycle
            confirmButton.addClickListener(e -> confermaHandler());
            confirmButton.setIcon(new Icon(confirmIcon));
            confirmButton.getElement().getStyle().set("margin-right", "auto");
            bottomLayout.add(confirmButton);
        }// end of if cycle

        bottomLayout.setFlexGrow(1, spazioVuotoEspandibile);
    }// end of method


    public void cancellaHandler() {
        if (cancelHandler != null) {
            cancelHandler.run();
        }// end of if cycle
        close();
    }// end of method


    /**
     * Esegue l'azione principale confermata <br>
     * L'azione viene individuata nella sottoclasse specifica <br>
     * DEVE essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     * Lancia, in un thread separato, il metodo run() ricevuto come parametro della classe <br>
     * Chiude il dialogo <br>
     */
    public void confermaHandler() {
        if (confirmHandler != null) {
            confirmHandler.run();
        }

        close();
    }

    //    /**
    //     * Opens the given item for editing in the dialog.
    //     *
    //     * @param item      The item to edit; it may be an existing or a newly created instance
    //     * @param operation The operation being performed on the item
    //     * @param context   legato alla sessione
    //     */
    //    @Override
    //    public void open(AEntity item, EAOperation operation, AContext context) {
    //    }
    //

    //    /**
    //     * Opens the given item for editing in the dialog.
    //     *
    //     * @param item      The item to edit; it may be an existing or a newly created instance
    //     * @param operation The operation being performed on the item
    //     * @param context   legato alla sessione
    //     * @param title     of the window dialog
    //     */
    //    @Override
    //    public void open(AEntity item, EAOperation operation, AContext context, String title) {
    //    }

    //    /**
    //     * Azione proveniente dal click sul bottone Confirm (delete)
    //     */
    //    private void confirmClicked() {
    //        close();
    //    }// end of method

    //    public void parte(String title, String message, String additionalMessage, Runnable pippo) {
    //        this.pippo = pippo;
    //        Button edit = new Button("Prova", event -> pippo.run());
    //        this.add(edit);
    //
    //        super.open();
    //    }// end of method


}// end of class
