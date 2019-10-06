package it.algos.vaadwiki.modules.professione;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatList;
import it.algos.vaadwiki.schedule.TaskProfessione;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 6-ott-2018 7.29.00 <br>
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
@Route(value = TAG_PRO, layout = MainLayout.class)
@Qualifier(TAG_PRO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class ProfessioneList extends AttNazProfCatList {


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskProfessione taskProfessione;



//    /**
//     * Costruttore @Autowired <br>
//     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
//     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
//     *
//     * @param presenter per gestire la business logic del package
//     * @param dialog    per visualizzare i fields
//     */
//    @Autowired
//    public ProfessioneViewList(@Qualifier(TAG_PRO) IAPresenter presenter, @Qualifier(TAG_PRO) IADialog dialog) {
//        super(presenter, dialog);
//        ((ProfessioneViewDialog) dialog).fixFunzioni(this::save, this::delete);
//    }// end of Spring constructor

    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa alla superclasse il service iniettato qui da Vaadin/@Route <br>
     * Passa alla superclasse anche la entityClazz che viene definita qui (specifica di questo modulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    @Autowired
    public ProfessioneList(@Qualifier(TAG_PRO) IAService service) {
        super(service, Professione.class);
    }// end of Vaadin/@Route constructor

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.titoloModulo = serviceWiki.titoloModuloProfessione;
        super.usaBottoneUpload = false;
        super.usaBottoneStatistiche = false;
        super.usaPagination = true;
        super.task = taskProfessione;
        super.codeFlagDownload = USA_DAEMON_PROFESSIONE;
        super.codeLastDownload = LAST_DOWNLOAD_PROFESSIONE;
        super.durataLastDownload = DURATA_DOWNLOAD_PROFESSIONE;
    }// end of method


    /**
     * Crea la GridPaginata <br>
     * Per usare una GridPaginata occorre:
     * 1) la view xxxList deve estendere APaginatedGridViewList anziche AGridViewList <br>
     * 2) deve essere sovrascritto questo metodo nella classe xxxList <br>
     * 3) nel metodo sovrascritto va creata la PaginatedGrid 'tipizzata' con la entityClazz (Collection) specifica <br>
     * 4) il metodo sovrascritto deve invocare DOPO questo stesso superMetodo in APaginatedGridViewList <br>
     */
    @Override
    protected void creaGridPaginata() {
        paginatedGrid = new PaginatedGrid<Professione>();
        super.creaGridPaginata();
    }// end of method

//    /**
//     * Crea la GridPaginata <br>
//     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
//     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
//     * Oppure queste possono essere fatte nella sottoclasse , se non sono standard <br>
//     */
//    protected void creaGridPaginata() {
//        PaginatedGrid<Professione> gridPaginated = new PaginatedGrid<Professione>();
//        super.grid = gridPaginated;
//        super.creaGridPaginata();
//    }// end of method


//    /**
//     * Aggiunge le colonne alla PaginatedGrid <br>
//     * Sovrascritto (obbligatorio) <br>
//     */
//    protected void addColumnsGridPaginata() {
//        fixColumn(Professione::getSingolare, "singolare");
//        fixColumn(Professione::getPagina, "pagina");
//    }// end of method
//
//
//    /**
//     * Costruisce la colonna in funzione della PaginatedGrid specifica della sottoclasse <br>
//     * DEVE essere sviluppato nella sottoclasse, sostituendo AEntity con la classe effettiva  <br>
//     */
//    protected void fixColumn(ValueProvider<Professione, ?> valueProvider, String propertyName) {
//        Grid.Column singleColumn;
//        singleColumn = ((PaginatedGrid<Professione>) grid).addColumn(valueProvider);
//        columnService.fixColumn(singleColumn, Professione.class, propertyName);
//    }// end of method


//    /**
//     * Apre il dialog di detail
//     */
//    protected void addDetailDialog() {
//        //--Flag di preferenza per aprire il dialog di detail con un bottone Edit. Normalmente true.
//        if (usaBottoneEdit) {
//            ComponentRenderer renderer = new ComponentRenderer<>(this::createEditButton);
//            Grid.Column colonna = gridPaginated.addColumn(renderer);
//            colonna.setWidth("6em");
//            colonna.setFlexGrow(0);
//        } else {
//            EAOperation operation = isEntityModificabile ? EAOperation.edit : EAOperation.showOnly;
//            grid.addSelectionListener(evento -> apreDialogo((SingleSelectionEvent) evento, operation));
//        }// end of if/else cycle
//    }// end of method
//
//
//    /**
//     * Eventuale header text
//     */
//    protected void fixGridHeader(String messaggio) {
//        try { // prova ad eseguire il codice
//            HeaderRow topRow = gridPaginated.prependHeaderRow();
//            Grid.Column[] matrix = array.getColumnArray(gridPaginated);
//            HeaderRow.HeaderCell informationCell = topRow.join(matrix);
//            Label testo = new Label(messaggio);
//            informationCell.setComponent(testo);
//        } catch (Exception unErrore) { // intercetta l'errore
//            log.error(unErrore.toString());
//        }// fine del blocco try-catch
//    }// end of method

}// end of class