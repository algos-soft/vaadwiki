package it.algos.vaadwiki.modules.genere;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatViewList;
import it.algos.vaadwiki.schedule.TaskGenere;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 15-giu-2019 11.00.02 <br>
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
@Route(value = TAG_GEN, layout = MainLayout.class)
@Qualifier(TAG_GEN)
@Slf4j
@AIScript(sovrascrivibile = true)
public class GenereViewList extends AttNazProfCatViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private TaskGenere taskGenere;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public GenereViewList(@Qualifier(TAG_GEN) IAPresenter presenter, @Qualifier(TAG_GEN) IADialog dialog) {
        super(presenter, dialog);
        ((GenereViewDialog) dialog).fixFunzioni(this::save, this::delete);
    }// end of Spring constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.titoloModulo = service.titoloModuloGenere;
        super.usaBottoneStatistiche = false;
        super.usaBottoneUpload = false;
        super.task = taskGenere;
        super.usaPagination = true;
        super.codeFlagDownload = USA_DAEMON_GENERE;
        super.codeLastDownload = LAST_DOWNLOAD_GENERE;
        super.durataLastDownload = DURATA_DOWNLOAD_GENERE;
    }// end of method


    /**
     * Crea la GridPaginata <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
     * Oppure queste possono essere fatte nella sottoclasse , se non sono standard <br>
     */
    protected void creaGridPaginata() {
        PaginatedGrid<Genere> gridPaginated = new PaginatedGrid<Genere>();
        super.grid = gridPaginated;
        super.creaGridPaginata();
    }// end of method


    /**
     * Aggiunge le colonne alla PaginatedGrid <br>
     * Sovrascritto (obbligatorio) <br>
     */
    protected void addColumnsGridPaginata() {
        fixColumn(Genere::getSingolare, "singolare");
        fixColumn(Genere::getPluraleMaschile, "pluraleMaschile");
        fixColumn(Genere::getPluraleFemminile, "pluraleFemminile");
    }// end of method


    /**
     * Costruisce la colonna in funzione della PaginatedGrid specifica della sottoclasse <br>
     * DEVE essere sviluppato nella sottoclasse, sostituendo AEntity con la classe effettiva  <br>
     */
    protected void fixColumn(ValueProvider<Genere, ?> valueProvider, String propertyName) {
        Grid.Column singleColumn;
        singleColumn = ((PaginatedGrid<Genere>) grid).addColumn(valueProvider);
        columnService.fixColumn(singleColumn, Genere.class, propertyName);
    }// end of method

}// end of class