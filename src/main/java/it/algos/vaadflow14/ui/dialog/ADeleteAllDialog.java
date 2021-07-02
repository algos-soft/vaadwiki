package it.algos.vaadflow14.ui.dialog;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.entity.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.io.*;
import java.util.function.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 29-set-2019
 * Time: 07:49
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ADeleteAllDialog<T extends Serializable> extends ADialog {

    //--Titolo standard, eventualmente modificabile nelle sottoclassi
    private static String TITOLO = "Delete";

    private static String message = "Vuoi veramente cancellare TUTTE le entities di questa collezione ?";

    private static String additionalMessage = "L'operazione non è reversibile";

    protected Button deleteButton = new Button(TITOLO);

    /**
     * Costruttore <br>
     */
    public ADeleteAllDialog() {
        super(TITOLO);
    }// end of constructor

    /**
     * Metodo invocato subito DOPO il costruttore.
     * DEVE essere inserito nella sottoclasse e invocare (eventualmente) un metodo della superclasse.
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizializzazione() {
        super.inizia();
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

        Label spazioVuotoEspandibile = new Label("");
        bottomLayout.add(spazioVuotoEspandibile);
        bottomLayout.setFlexGrow(1, spazioVuotoEspandibile);

        cancelButton.getElement().setAttribute("theme", "primary");
        cancelButton.addClickListener(e -> cancellaHandler());
        cancelButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
        bottomLayout.add(cancelButton);

        deleteButton.getElement().setAttribute("theme", "error");
        deleteButton.addClickListener(e -> confermaHandler());
        deleteButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        bottomLayout.add(deleteButton);
    }// end of method

    /**
     * Apre il dialogo <br>
     *
     * @param deleteHandler The confirmation handler function for deleting entities
     */
    public void open(Runnable deleteHandler) {
        this.usaCancelButton = true;
        this.confirmHandler = deleteHandler;

        //--Body placeholder
        this.fixBodyLayout(message, additionalMessage);

        //--Barra placeholder dei bottoni, creati e regolati
        this.fixBottomLayout();

        super.open();
    }// end of method

    /**
     * Opens the given item for editing in the dialog.
     * Crea i fields e visualizza il dialogo <br>
     *
     * @param entityBean  The item to edit; it may be an existing or a newly created instance
     * @param operation   The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param itemSaver   funzione associata al bottone 'accetta' ('registra', 'conferma')
     * @param itemDeleter funzione associata al bottone 'delete'
     */
    public void open(AEntity entityBean, Consumer itemSaver) {
        this.usaCancelButton = false;
        this.usaConfirmButton = true;

        //--Body placeholder
        this.fixBodyLayout(message, additionalMessage);

        //--Barra placeholder dei bottoni, creati e regolati
        this.fixBottomLayout();

        super.open();

    }

    /**
     * Opens the given item for editing in the dialog.
     * Crea i fields e visualizza il dialogo <br>
     *
     * @param entityBean  The item to edit; it may be an existing or a newly created instance
     * @param operation   The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param itemSaver   funzione associata al bottone 'accetta' ('registra', 'conferma')
     * @param itemDeleter funzione associata al bottone 'delete'
     */
    public void open(AEntity entityBean, BiConsumer itemSaver, Consumer itemDeleter) {

    }

}// end of class
