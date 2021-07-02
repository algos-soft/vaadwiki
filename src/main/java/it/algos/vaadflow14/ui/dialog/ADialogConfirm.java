package it.algos.vaadflow14.ui.dialog;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.io.*;
import java.util.function.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 29-giu-2021
 * Time: 17:38
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ADialogConfirm extends ADialog {

    //--Titolo standard, eventualmente modificabile nelle sottoclassi
    private static final String TITOLO = "Delete";

    private static final String MESSAGE = "Pippoz ?";
    protected Button deleteButton = new Button(TITOLO);

    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro del costruttore usato <br>
     */
    public ADialogConfirm() {
        super(TITOLO);
    }// end of Vaadin/Spring constructor


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        super.inizia();
    }

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
//        deleteButton.addClickListener(e -> confermaHandler());
        deleteButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        bottomLayout.add(deleteButton);
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
    public void open( Consumer confirmHandler) {
        this.usaCancelButton = false;
        this.usaConfirmButton = true;

        //--Body placeholder
        this.fixBodyLayout(MESSAGE,"forse");

        //--Barra placeholder dei bottoni, creati e regolati
        this.fixBottomLayout();

        deleteButton.addClickListener(e -> confirmHandler.accept(null));

        super.open();
    }

}
