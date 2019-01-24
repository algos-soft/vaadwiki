package it.algos.vaadwiki.modules.wikigiorno;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoViewDialog;
import it.algos.vaadflow.modules.giorno.GiornoViewList;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwiki.upload.UploadGiorni;
import it.algos.vaadwiki.upload.UploadGiornoMorto;
import it.algos.vaadwiki.upload.UploadGiornoNato;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static it.algos.vaadflow.application.FlowCost.TAG_GIO;
import static it.algos.vaadwiki.application.WikiCost.TAG_WGIO;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 19-gen-2019 11.33.37 <br>
 * <br>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * <p>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le istanze @Autowired usate da questa classe vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired di questa classe, oppure <br>
 * 2) la property è di una classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON), oppure <br>
 * 3) vengono usate in un un metodo @PostConstruct di questa classe, perché SpringBoot le inietta DOPO init() <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @UIScope (obbligatorio) <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@UIScope
@Route(value = TAG_WGIO, layout = MainLayout.class)
@Qualifier(TAG_WGIO)
@Slf4j
@AIScript(sovrascrivibile = true)
public class WikiGiornoViewList extends GiornoViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    protected Button uploadAllButton;

    protected Button uploadOneNatoButton;

    protected Button uploadOneMortoButton;

    @Autowired
    private UploadGiorni uploadGiorni;

    @Autowired
    private UploadGiornoNato uploadGiornoNato;

    @Autowired
    private UploadGiornoMorto uploadGiornoMorto;

    private Giorno giornoCorrente;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public WikiGiornoViewList(@Qualifier(TAG_GIO) IAPresenter presenter, @Qualifier(TAG_GIO) IADialog dialog) {
        super(presenter, dialog);
        ((GiornoViewDialog) dialog).fixFunzioni(this::save, this::delete);
    }// end of Spring constructor


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
        uploadAllButton.addClickListener(e -> openUploadDialog());
        topPlaceholder.add(uploadAllButton);

        //--upload singola lista di un giorno per i nati
        uploadOneNatoButton = new Button("Upload nati", new Icon(VaadinIcon.UPLOAD));
        uploadOneNatoButton.getElement().setAttribute("theme", "error");
        uploadOneNatoButton.addClickListener(selectionEvent -> {
            if (grid.getSelectedItems().size() == 1) {
                giornoCorrente = (Giorno) grid.getSelectedItems().toArray()[0];
                uploadGiornoNato.esegue(giornoCorrente);
            }// end of if cycle
        });//end of lambda expressions and anonymous inner class
        topPlaceholder.add(uploadOneNatoButton);

        //--upload singola lista di un giorno per i nati
        uploadOneMortoButton = new Button("Upload morti", new Icon(VaadinIcon.UPLOAD));
        uploadOneMortoButton.getElement().setAttribute("theme", "error");
        uploadOneMortoButton.addClickListener(selectionEvent -> {
            if (grid.getSelectedItems().size() == 1) {
                giornoCorrente = (Giorno) grid.getSelectedItems().toArray()[0];
                uploadGiornoMorto.esegue(giornoCorrente);
            }// end of if cycle
        });//end of lambda expressions and anonymous inner class
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
    protected void openUploadDialog() {
        String message = "Sei sicuro di voler aggiornare su wikipedia tutte le pagine dei Giorni ?";
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
        uploadGiorni.esegueAll();
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