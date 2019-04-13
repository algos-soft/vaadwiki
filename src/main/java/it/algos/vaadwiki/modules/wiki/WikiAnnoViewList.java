package it.algos.vaadwiki.modules.wiki;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoViewDialog;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoViewDialog;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwiki.upload.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;
import static it.algos.vaadflow.application.FlowCost.TAG_GIO;
import static it.algos.vaadwiki.application.WikiCost.TAG_WANN;
import static it.algos.vaadwiki.application.WikiCost.TAG_WGIO;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 17:17
 */
@UIScope
@Route(value = TAG_WANN, layout = MainLayout.class)
@Qualifier(TAG_WANN)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WikiAnnoViewList extends WikiViewList {

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;


    @Autowired
    private UploadAnni uploadAnni;

    @Autowired
    private UploadAnnoNato uploadAnnoNato;

    @Autowired
    private UploadAnnoMorto uploadAnnoMorto;

    private Anno annoCorrente;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public WikiAnnoViewList(@Qualifier(TAG_ANN) IAPresenter presenter, @Qualifier(TAG_ANN) IADialog dialog) {
        super(presenter, dialog);
        ((AnnoViewDialog) dialog).fixFunzioni(this::save, this::delete);
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

        uploadAllButton.addClickListener(e -> openUploadDialog("degli anni"));

        uploadOneNatoButton.addClickListener(selectionEvent -> {
            if (grid.getSelectedItems().size() == 1) {
                annoCorrente = (Anno) grid.getSelectedItems().toArray()[0];
                uploadAnnoNato.esegue(annoCorrente);
            }// end of if cycle
        });//end of lambda expressions and anonymous inner class

        uploadOneMortoButton.addClickListener(selectionEvent -> {

            Object pippo=grid.getSelectedItems();
            int alfa=grid.getSelectedItems().size();
            if (grid.getSelectedItems().size() == 1) {
                annoCorrente = (Anno) grid.getSelectedItems().toArray()[0];
                uploadAnnoMorto.esegue(annoCorrente);
            }// end of if cycle
        });//end of lambda expressions and anonymous inner class

        sincroBottoniMenu(false);
        return topPlaceholder.getComponentCount() > 0;
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void uploadEffettivo() {
        uploadAnni.esegueAll();
    }// end of method


}// end of class