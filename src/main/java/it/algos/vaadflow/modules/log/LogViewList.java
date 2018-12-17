package it.algos.vaadflow.modules.log;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.AViewList;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.fields.AComboBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static it.algos.vaadflow.application.FlowCost.TAG_LOG;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 26-ott-2018 9.59.58 <br>
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
@Route(value = TAG_LOG, layout = MainLayout.class)
@Qualifier(TAG_LOG)
@AIView(roleTypeVisibility = EARoleType.admin)
@Slf4j
@AIScript(sovrascrivibile = false)
public class LogViewList extends AViewList {

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    public static final String IRON_ICON = "history";

    private AComboBox<Livello> comboLivello;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public LogViewList(@Qualifier(TAG_LOG) IAPresenter presenter, @Qualifier(TAG_LOG) IADialog dialog) {
        super(presenter, dialog);
        ((LogViewDialog) dialog).fixFunzioni(this::save, this::delete);
    }// end of Spring constructor


    /**
     * Le preferenze sovrascritte nella sottoclasse
     */
    @Override
    protected void fixPreferenzeSpecifiche() {
        super.usaSearchTextField = false;
        super.usaSearchBottoneNew = false;
        super.isEntityAdmin = true;
        if (login.isDeveloper()) {
            super.usaBottoneDeleteAll = true;
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
    @Override
    protected void fixTopLayout() {
        super.fixTopLayout();
        topPlaceholder.add(creaPopup());
    }// end of method


    protected Component creaPopup() {
        comboLivello = new AComboBox();
        comboLivello.setWidth("8em");
        comboLivello.setItems(Livello.values());
        comboLivello.addValueChangeListener(e -> super.updateView());

        return comboLivello;
    }// end of method


    public void updateItems() {
        Livello livello = (Livello) comboLivello.getValue();
        items = ((LogService) service).findAllByLivello(livello);
    }// end of method

}// end of class