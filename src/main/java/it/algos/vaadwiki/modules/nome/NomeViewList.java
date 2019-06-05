package it.algos.vaadwiki.modules.nome;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.AViewList;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static it.algos.vaadwiki.application.WikiCost.TAG_NOM;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 29-mag-2019 19.55.00 <br>
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
@Route(value = TAG_NOM, layout = MainLayout.class)
@Qualifier(TAG_NOM)
@Slf4j
@AIScript(sovrascrivibile = true)
public class NomeViewList extends AViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public NomeViewList(@Qualifier(TAG_NOM) IAPresenter presenter, @Qualifier(TAG_NOM) IADialog dialog) {
        super(presenter, dialog);
        ((NomeViewDialog) dialog).fixFunzioni(this::save, this::delete);
    }// end of Spring constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenzeSpecifiche() {
        super.fixPreferenzeSpecifiche();
        super.usaSearchBottoneNew = false;
        super.usaBottoneDeleteAll = true;
    }// end of if cycle


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

        Button creaButton = new Button("Crea", new Icon(VaadinIcon.LIST));
        creaButton.addClassName("view-toolbar__button");
        creaButton.addClickListener(e -> ((NomeService) service).crea());
        topPlaceholder.add(creaButton);

        return true;
    }// end of if cycle


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        String lar = "7em";
        ComponentRenderer renderer;
        Grid.Column colonna;

        renderer = new ComponentRenderer<>(this::createViewButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Test");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);


        renderer = new ComponentRenderer<>(this::createWikiButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Wiki");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);


        renderer = new ComponentRenderer<>(this::createUploaButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Upload");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

    }// end of method


    protected Button createViewButton() {
        Button uploadOneNatoButton = new Button("Test", new Icon(VaadinIcon.LIST));
        uploadOneNatoButton.getElement().setAttribute("theme", "secondary");
//        uploadOneNatoButton.addClickListener(e -> viewNato(entityBean));
        return uploadOneNatoButton;
    }// end of method


    protected Component createWikiButton(Nome entityBean) {

        if (entityBean!=null&&entityBean.voci>=75) {
            Button    uploadOneNatoButton = new Button("Wiki", new Icon(VaadinIcon.SERVER));
            uploadOneNatoButton.getElement().setAttribute("theme", "secondary");
//        uploadOneNatoButton.addClickListener(e -> viewNato(entityBean));
            return uploadOneNatoButton;
        } else {
            return new Label("");
        }// end of if/else cycle



    }// end of method


    protected Component createUploaButton(Nome entityBean) {

        if (entityBean!=null&&entityBean.voci>=75) {
            Button     uploadOneNatoButton = new Button("Upload", new Icon(VaadinIcon.UPLOAD));
            uploadOneNatoButton.getElement().setAttribute("theme", "error");
//        uploadOneNatoButton.addClickListener(e -> viewNato(entityBean));
            return uploadOneNatoButton;
        } else {
            return new Label("Niente");
        }// end of if/else cycle
    }// end of method

}// end of class