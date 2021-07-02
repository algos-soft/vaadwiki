package it.algos.vaadflow14.ui.dialog;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.io.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: sab, 28-set-2019
 * Time: 21:47
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AResetDialog<T extends Serializable> extends ADialog {

    //--Titolo standard, eventualmente modificabile nelle sottoclassi
    private static String TITOLO = "Reset";

    protected Button deleteButton = new Button(TITOLO);

//    private static  String message = "Vuoi veramente ripristinare i valori originali predeterminati di questa collezione ?";
    private static  String additionalMessage = "L'operazione cancellerà tutti i valori successivamente aggiunti o modificati";

    private static  String   message = "Vuoi veramente ripristinare i valori originali predeterminati di questa collezione? L' operazione cancellerà tutti i valori originali. Eventuali valori inseriti manualmente NON vengono cancellati/modificati";
    /**
     * Costruttore <br>
     */
    public AResetDialog() {
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
        if (pref.isBool(PREF_USA_BUTTON_SHORTCUT)) {
            cancelButton.addClickShortcut(Key.ARROW_LEFT);
        }// end of if cycle
        bottomLayout.add(cancelButton);

        deleteButton.getElement().setAttribute("theme", "error");
        deleteButton.addClickListener(e -> confermaHandler());
        deleteButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        bottomLayout.add(deleteButton);
    }// end of method

    /**
     * Apre il dialogo <br>
     *
     * @param resetHandler The confirmation handler function for resetting entities
     */
    public void open(Runnable resetHandler) {
        this.usaCancelButton = true;
        this.confirmHandler = resetHandler;

        //--Body placeholder
        this.fixBodyLayout(message, additionalMessage);

        //--Barra placeholder dei bottoni, creati e regolati
        this.fixBottomLayout();

        super.open();
    }// end of method


}// end of class
