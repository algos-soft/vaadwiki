package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.backend.login.ALogin;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.AVaadinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

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
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ADialog extends Dialog implements IADialog {

    protected final H2 titleField = new H2();

    protected final Div messageLabel = new Div();

    protected final Div extraMessageLabel = new Div();

    /**
     * Service (pattern SINGLETON) recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

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
     * Corpo centrale del Dialog <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected VerticalLayout bodyPlaceHolder = new VerticalLayout();

    /**
     * Barra dei bottoni di comando <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected HorizontalLayout bottomLayout = new HorizontalLayout();

//    protected String message;
//
//    protected String additionalMessage;

    /**
     * Titolo del dialogo <br>
     */
    protected String title;

    protected Button cancelButton = new Button("Annulla");

    protected Button confirmButton = new Button("Conferma");

    /**
     * Service (@Scope = 'singleton') iniettato dal costruttore @Autowired di Spring <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    @Autowired
    protected AVaadinService vaadinService;

    protected ALogin login;

    /**
     * Recuperato dalla sessione, quando la Spring costruisce un'istanza di questa classe. <br>
     * Viene regolato nel service specifico (AVaadinService) <br>
     */
    protected AContext context;

//    protected Runnable pippo;


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
        this.setCloseOnEsc(false);
        this.setCloseOnOutsideClick(false);
        this.getElement().getClassList().add("confirm-dialog");

        //--Login and context della sessione
        context = vaadinService.fixLoginAndContext();
        login = context.getLogin();

        //--preferenze standard. Possono essere modificate anche selezionando la firma di open(...)
        this.usaCancelButton = true;
        this.usaConfirmButton = true;

        //--Titolo placeholder del dialogo
        this.add(titleField);

        //--Corpo centrale del Dialog
        this.add(bodyPlaceHolder);

        //--spazio per distanziare i bottoni sottostanti
        this.add(new H3());

        //--Barra placeholder dei bottoni, creati e regolati
        this.add(bottomLayout);

        this.creaTitleLayout();
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

        if (usaCancelButton) {
            cancelButton.getElement().setAttribute("theme", "primary");
            cancelButton.addClickListener(e -> cancellaHandler());
            cancelButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
            bottomLayout.add(cancelButton);
        }// end of if cycle

        if (usaConfirmButton) {
            if (usaCancelButton) {
                confirmButton.getElement().setAttribute("theme", "secondary");
            } else {
                confirmButton.getElement().setAttribute("theme", "primary");
            }// end of if/else cycle
            confirmButton.addClickListener(e -> confermaHandler());
            confirmButton.setIcon(new Icon(VaadinIcon.CHECK));
            bottomLayout.add(confirmButton);
        }// end of if cycle
        bottomLayout.setAlignItems(FlexComponent.Alignment.END);
    }// end of method


    public void cancellaHandler() {
        if (cancelHandler != null) {
            cancelHandler.run();
        }// end of if cycle
        close();
    }// end of method


    public void confermaHandler() {
        if (confirmHandler != null) {
            confirmHandler.run();
        }// end of if cycle
        close();
    }// end of method


    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     */
    @Override
    public void open(AEntity item, EAOperation operation, AContext context) {
    }


    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     * @param title     of the window dialog
     */
    @Override
    public void open(AEntity item, EAOperation operation, AContext context, String title) {
    }


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
