package it.algos.vaadflow.ui.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadflow.enumeration.EAMenu;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.menu.*;
import lombok.extern.slf4j.Slf4j;

import static it.algos.vaadflow.application.FlowCost.USA_MENU;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: Mon, 20-May-2019
 * Time: 07:48
 * <p>
 * Sottoclasse di servizio per regolare il layout di AViewList in una classe 'dedicata' <br>
 * Alleggerisce la 'lettura' della classe principale <br>
 * Le property sono regolarmente disponibili in AViewList ed in tutte le sue sottoclassi <br>
 * Regola menu, topLayout e alertLayout <br>
 * Qui vengono regolati i layout 'standard'. <br>
 * Nelle sottoclassi concrete i layout possono essere modificati. <br>
 */
@Slf4j
public abstract class ALayoutViewList extends APrefViewList {

    /**
     * Costruttore <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    public ALayoutViewList(IAPresenter presenter, IADialog dialog) {
        super(presenter, dialog);
    }// end of Spring constructor


    /**
     * Costruisce la barra di menu e l'aggiunge alla UI <br>
     * Lo standard è 'Flowingcode'
     * Può essere sovrascritto
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaMenuLayout() {
        IAMenu menu;
        EAMenu typeMenu = EAMenu.getMenu(pref.getStr(USA_MENU));

        if (typeMenu != null) {
            switch (typeMenu) {
                case buttons:
                    menu = StaticContextAccessor.getBean(AButtonMenu.class);
                    this.add(menu.getComp());
                    break;
                case popup:
                    menu = StaticContextAccessor.getBean(APopupMenu.class);
                    this.add(menu.getComp());
                    break;
                case flowing:
                    menu = StaticContextAccessor.getBean(AFlowingcodeAppLayoutMenu.class);
                    this.add(menu.getComp());
                    break;
                case vaadin:
                    menu = StaticContextAccessor.getBean(AAppLayoutMenu.class);
                    this.add(new Label("."));
                    this.add(((AFlowingcodeAppLayoutMenu) menu).getAppLayoutFlowing());
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

    }// end of method


    /**
     * Placeholder (eventuale, presente di default) SOPRA la Grid
     * - con o senza campo edit search, regolato da preferenza o da parametro
     * - con o senza bottone New, regolato da preferenza o da parametro
     * - con eventuali altri bottoni specifici
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaTopLayout() {
        topPlaceholder.removeAll();
        topPlaceholder.addClassName("view-toolbar");
        String buttonTitle;
        Button deleteAllButton;
        Button resetButton;
        Button clearFilterTextBtn;
        Button searchButton;
        Button allButton;
        boolean isDeveloper = login.isDeveloper();
        boolean isAdmin = login.isAdmin();

        if (usaBottoneDeleteAll && isDeveloper) {
            deleteAllButton = new Button("Delete", new Icon(VaadinIcon.CLOSE_CIRCLE));
            deleteAllButton.getElement().setAttribute("theme", "error");
            deleteAllButton.addClassName("view-toolbar__button");
            deleteAllButton.addClickListener(e -> openConfirmDialogDelete());
            topPlaceholder.add(deleteAllButton);
        }// end of if cycle

        if (usaBottoneReset && isDeveloper) {
            resetButton = new Button("Reset", new Icon(VaadinIcon.CLOSE_CIRCLE));
            resetButton.getElement().setAttribute("theme", "error");
            resetButton.addClassName("view-toolbar__button");
            resetButton.addClickListener(e -> {
                service.reset();
                updateView();
            });
            topPlaceholder.add(resetButton);
        }// end of if cycle

        if (usaSearchTextField) {
            searchField = new TextField("", "Search");
            searchField.setPrefixComponent(new Icon("lumo", "search"));
            searchField.addClassName("view-toolbar__search-field");
            searchField.setValueChangeMode(ValueChangeMode.EAGER);
            searchField.addValueChangeListener(e -> updateView());

            clearFilterTextBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
            clearFilterTextBtn.addClickListener(e -> searchField.clear());

            topPlaceholder.add(searchField, clearFilterTextBtn);
        }// end of if cycle


        if (usaSearchTextDialog) {
            buttonTitle = text.primaMaiuscola(pref.getStr(FlowCost.FLAG_TEXT_SEARCH));
            searchButton = new Button(buttonTitle, new Icon("lumo", "search"));
            searchButton.getElement().setAttribute("theme", "secondary");
            searchButton.addClassName("view-toolbar__button");
            searchButton.addClickListener(e -> openSearch());
            topPlaceholder.add(searchButton);
        }// end of if cycle

        //--bottone piccolo per eliminare i filtri di una ricerca e restituire tutte le entities
        if (usaAllButton) {
            allButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
            allButton.addClickListener(e -> {
                updateItems();
                updateView();
            });
            topPlaceholder.add(allButton);
        }// end of if cycle


        if (usaSearchBottoneNew) {
            buttonTitle = text.primaMaiuscola(pref.getStr(FlowCost.FLAG_TEXT_NEW));
            newButton = new Button(buttonTitle, new Icon("lumo", "plus"));
            newButton.getElement().setAttribute("theme", "primary");
            newButton.addClassName("view-toolbar__button");
            newButton.addClickListener(e -> openNew());
            topPlaceholder.add(newButton);
        }// end of if cycle

    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    protected void creaAlertLayout() {
        alertPlacehorder.removeAll();
//        alertPlacehorder.addClassName("view-toolbar");
        alertPlacehorder.setMargin(false);
        alertPlacehorder.setSpacing(false);
        alertPlacehorder.setPadding(false);

        if (isEntityDeveloper || isEntityAdmin || isEntityEmbedded || isEntityUsaDatiDemo) {
            usaTopAlert = true;
        }// end of if cycle

        if (usaTopAlert) {
            if (isEntityDeveloper) {
                alertPlacehorder.add(new Label("Lista visibile solo perché sei collegato come developer. Gli admin e gli utenti normali non la vedono."));
            }// end of if cycle

            if (isEntityAdmin) {
                alertPlacehorder.add(new Label("Lista visibile solo perché sei collegato come admin. Gli utenti normali non la vedono."));
            }// end of if cycle

            if (isEntityEmbedded) {
                alertPlacehorder.add(new Label("Questa lista non dovrebbe mai essere usata direttamente (serve come test o per le sottoclassi specifiche)"));
                alertPlacehorder.add(new Label("L'entity è 'embedded' nelle collezioni che la usano (no @Annotation property DbRef)"));
            }// end of if cycle

            if (isEntityEmbedded || isEntityUsaDatiDemo) {
                alertPlacehorder.add(new Label("Allo startup del programma, sono stati creati alcuni elementi di prova"));
            }// end of if cycle
        }// end of if cycle

    }// end of method

}// end of class
