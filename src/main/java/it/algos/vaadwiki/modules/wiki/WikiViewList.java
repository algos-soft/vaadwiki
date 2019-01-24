package it.algos.vaadwiki.modules.wiki;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.ACronoViewList;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 11:00
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class WikiViewList extends ACronoViewList {

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    protected Button uploadAllButton;

    protected Button uploadOneNatoButton;

    protected Button uploadOneMortoButton;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * La sottoclasse usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * La sottoclasse usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     */
    public WikiViewList(IAPresenter presenter, IADialog dialog) {
        super(presenter, dialog);
    }// end of Spring constructor


    /**
     * Le preferenze sovrascritte nella sottoclasse
     */
    @Override
    protected void fixPreferenzeSpecifiche() {
        super.fixPreferenzeSpecifiche();
        super.testoBottoneEdit = SHOW_NAME;
    }// end of method


    /**
     * Placeholder (eventuale, presente di default) SOPRA la Grid
     * - con o senza campo edit search, regolato da preferenza o da parametro
     * - con o senza bottone New, regolato da preferenza o da parametro
     * - con eventuali altri bottoni specifici
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected boolean creaTopLayout() {
        super.creaTopLayout();

        //--upload della lista completa di 365 + 365 giorni
        uploadAllButton = new Button("Upload all", new Icon(VaadinIcon.UPLOAD));
        uploadAllButton.getElement().setAttribute("theme", "error");
//        uploadAllButton.addClickListener(e -> openUploadDialog());
        topPlaceholder.add(uploadAllButton);

        //--upload singola lista di un giorno per i nati
        uploadOneNatoButton = new Button("Upload nati", new Icon(VaadinIcon.UPLOAD));
        uploadOneNatoButton.getElement().setAttribute("theme", "error");
//        uploadOneNatoButton.addClickListener(selectionEvent -> {
//            if (grid.getSelectedItems().size() == 1) {
//                giornoCorrente = (Giorno) grid.getSelectedItems().toArray()[0];
//                uploadGiornoNato.esegue(giornoCorrente);
//            }// end of if cycle
//        });//end of lambda expressions and anonymous inner class
        topPlaceholder.add(uploadOneNatoButton);

        //--upload singola lista di un giorno per i nati
        uploadOneMortoButton = new Button("Upload morti", new Icon(VaadinIcon.UPLOAD));
        uploadOneMortoButton.getElement().setAttribute("theme", "error");
//        uploadOneMortoButton.addClickListener(selectionEvent -> {
//            if (grid.getSelectedItems().size() == 1) {
//                giornoCorrente = (Giorno) grid.getSelectedItems().toArray()[0];
//                uploadGiornoMorto.esegue(giornoCorrente);
//            }// end of if cycle
//        });//end of lambda expressions and anonymous inner class
        topPlaceholder.add(uploadOneMortoButton);

        sincroBottoniMenu(false);
        return topPlaceholder.getComponentCount() > 0;
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void openUploadDialog(String tag) {
        String message = "Sei sicuro di voler aggiornare su wikipedia tutte le pagine " + tag + " ?";
        ADeleteDialog dialog = appContext.getBean(ADeleteDialog.class);
        dialog.open(message, this::uploadEffettivo);
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void uploadEffettivo() {
    }// end of method


    protected void sincroBottoniMenu(boolean enabled) {
        if (uploadOneNatoButton != null) {
            uploadOneNatoButton.setEnabled(enabled);
        }// end of if cycle
        if (uploadOneMortoButton != null) {
            uploadOneMortoButton.setEnabled(enabled);
        }// end of if cycle
    }// end of method


}// end of class
