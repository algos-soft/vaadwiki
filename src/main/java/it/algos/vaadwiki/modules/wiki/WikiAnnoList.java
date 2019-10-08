package it.algos.vaadwiki.modules.wiki;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoDialog;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadwiki.upload.UploadAnnoMorto;
import it.algos.vaadwiki.upload.UploadAnnoNato;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import java.time.LocalDateTime;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 17:17
 * <p>
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
@Route(value = TAG_WANN, layout = MainLayout14.class)
@Qualifier(TAG_WANN)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WikiAnnoList extends WikiList {

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    public static final String MENU_NAME = "Anno";

    @Autowired
    @Qualifier(TASK_ANN)
    protected ATask task;

    //    @Autowired
    private UploadAnnoNato uploadAnnoNato;

    //    @Autowired
    private UploadAnnoMorto uploadAnnoMorto;


//    /**
//     * Costruttore @Autowired <br>
//     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
//     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
//     *
//     * @param presenter per gestire la business logic del package
//     * @param dialog    per visualizzare i fields
//     */
//    @Autowired
//    public WikiAnnoViewList(@Qualifier(TAG_ANN) IAPresenter presenter, @Qualifier(TAG_ANN) IADialog dialog) {
//        super(presenter, dialog);
//        ((AnnoDialog) dialog).fixFunzioni(this::save, this::delete);
//    }// end of Spring constructor

    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    @Autowired
    public WikiAnnoList(@Qualifier(TAG_ANN) IAService service) {
        super(service, Anno.class);
    }// end of Vaadin/@Route constructor

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.titoloPaginaStatistiche = attNazProfCatService.titoloPaginaStatisticheAnni;
        super.codeLastUpload = LAST_UPLOAD_ANNI;
        super.durataLastUpload = DURATA_UPLOAD_ANNI;
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
    protected void creaTopLayout() {
        super.creaTopLayout();

//        uploadAllButton.addClickListener(e -> openUploadDialog("degli anni")); //@todo versione 14
        sincroBottoniMenu(false);
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();

        alertPlacehorder.add(creaInfoImport(task, USA_DAEMON_ANNI, LAST_UPLOAD_ANNI));
    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected Label creaInfoImport(ATask task, String flagDaemon, String flagLastUpload) {
        Label label = null;
        String testo = "";
        String tag = "Upload automatico: ";
        String nota = task != null ? task.getNota() : "";
        int durata = pref.getInt(DURATA_UPLOAD_ANNI);
        String message = "";
        LocalDateTime lastUpload = pref.getDate(flagLastUpload);
        testo = tag;

        if (pref.isBool(flagDaemon)) {
            testo += nota;
        } else {
            testo += "disattivato.";
        }// end of if/else cycle

        if (lastUpload != null) {
            message += testo + " Ultimo upload il " + date.getTime(lastUpload);
            message += ", in circa " + durata + " minuti";
        } else {
            if (pref.isBool(flagDaemon)) {
                message = tag + nota + " Non ancora effettuato.";
            } else {
                message = testo;
            }// end of if/else cycle
        }// end of if/else cycle
        label = new Label(message);

        return label;
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
        paginatedGrid = new PaginatedGrid<Anno>();
        super.creaGridPaginata();
    }// end of method



//    /**
//     * Aggiunge le colonne alla PaginatedGrid <br>
//     * Sovrascritto (obbligatorio) <br>
//     */
//    protected void addColumnsGridPaginata() {
//        fixColumn(Anno::getOrdine, "ordine");
//        fixColumn(Anno::getSecolo, "secolo");
//        fixColumn(Anno::getTitolo, "titolo");
//    }// end of method


//    /**
//     * Costruisce la colonna in funzione della PaginatedGrid specifica della sottoclasse <br>
//     * DEVE essere sviluppato nella sottoclasse, sostituendo AEntity con la classe effettiva  <br>
//     */
//    protected void fixColumn(ValueProvider<Anno, ?> valueProvider, String propertyName) {
//        Grid.Column singleColumn;
//        singleColumn = ((PaginatedGrid<Anno>) grid).addColumn(valueProvider);
//        columnService.fixColumn(singleColumn, Anno.class, propertyName);
//    }// end of method
//

    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        String lar = "7em";
        ComponentRenderer renderer;
        Grid.Column colonna;

        renderer = new ComponentRenderer<>(this::createViewNatoButton);
        colonna = paginatedGrid.addColumn(renderer);
        colonna.setHeader("Test");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createViewMortoButton);
        colonna = paginatedGrid.addColumn(renderer);
        colonna.setHeader("Test");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createWikiNatoButton);
        colonna = paginatedGrid.addColumn(renderer);
        colonna.setHeader("Wiki");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createWikiMortoButton);
        colonna = paginatedGrid.addColumn(renderer);
        colonna.setHeader("Wiki");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createUploadNatoButton);
        colonna = paginatedGrid.addColumn(renderer);
        colonna.setHeader("Upload");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createUploadMortoButton);
        colonna = paginatedGrid.addColumn(renderer);
        colonna.setHeader("Upload");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);
    }// end of method


    protected Button createViewNatoButton(Anno entityBean) {
        uploadOneNatoButton = new Button("Nati", new Icon(VaadinIcon.LIST));
        uploadOneNatoButton.getElement().setAttribute("theme", "secondary");
        uploadOneNatoButton.addClickListener(e -> viewNato(entityBean));
        return uploadOneNatoButton;
    }// end of method


    protected Button createViewMortoButton(Anno entityBean) {
        uploadOneMortoButton = new Button("Morti", new Icon(VaadinIcon.LIST));
        uploadOneMortoButton.getElement().setAttribute("theme", "secondary");
        uploadOneMortoButton.getElement().setAttribute("color", "green");
        uploadOneMortoButton.addClickListener(e -> viewMorto(entityBean));
        return uploadOneMortoButton;
    }// end of method


    protected Button createWikiNatoButton(Anno entityBean) {
        Element input = ElementFactory.createInput();
        uploadOneNatoButton = new Button("Nati", new Icon(VaadinIcon.SERVER));
        uploadOneNatoButton.getElement().setAttribute("theme", "secondary");
        uploadOneNatoButton.getElement().getStyle().set("background-color", input.getProperty("green"));
        uploadOneNatoButton.addClickListener(e -> wikiPageNato(entityBean));
        return uploadOneNatoButton;
    }// end of method


    protected Button createWikiMortoButton(Anno entityBean) {
        uploadOneMortoButton = new Button("Morti", new Icon(VaadinIcon.SERVER));
        uploadOneMortoButton.getElement().setAttribute("theme", "secondary");
//        uploadOneMortoButton.getElement().getClassList().add("green");
        uploadOneMortoButton.addClickListener(e -> wikiPageMorto(entityBean));
        return uploadOneMortoButton;
    }// end of method


    protected Button createUploadNatoButton(Anno entityBean) {
        uploadOneNatoButton = new Button("Nati", new Icon(VaadinIcon.UPLOAD));
        uploadOneNatoButton.getElement().setAttribute("theme", "error");
        uploadOneNatoButton.addClickListener(e -> uploadService.uploadAnnoNato(entityBean));
        return uploadOneNatoButton;
    }// end of method


    protected Button createUploadMortoButton(Anno entityBean) {
        uploadOneMortoButton = new Button("Morti", new Icon(VaadinIcon.UPLOAD));
        uploadOneMortoButton.getElement().setAttribute("theme", "error");
        uploadOneMortoButton.addClickListener(e -> uploadService.uploadAnnoMorto(entityBean));
        return uploadOneMortoButton;
    }// end of method


    protected void viewNato(Anno anno) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_ANNO_NATI + "/" + anno.id));
    }// end of method


    protected void viewMorto(Anno anno) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_ANNO_MORTI + "/" + anno.id));
    }// end of method


    protected void wikiPageNato(Anno anno) {
        String link = "\"" + PATH_WIKI + uploadService.getTitoloAnnoNato(anno) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void wikiPageMorto(Anno anno) {
        String link = "\"" + PATH_WIKI + uploadService.getTitoloAnnoMorto(anno) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    /**
     * Creazione ed apertura del dialogo per una nuova entity oppure per una esistente <br>
     * Il dialogo è PROTOTYPE e viene creato esclusivamente da appContext.getBean(... <br>
     * Nella creazione vengono regolati il service e la entityClazz di riferimento <br>
     * Contestualmente alla creazione, il dialogo viene aperto con l'item corrente (ricevuto come parametro) <br>
     * Se entityBean è null, nella superclasse AViewDialog viene modificato il flag a EAOperation.addNew <br>
     * Si passano al dialogo anche i metodi locali (di questa classe AViewList) <br>
     * come ritorno dalle azioni save e delete al click dei rispettivi bottoni <br>
     * Il metodo DEVE essere sovrascritto <br>
     *
     * @param entityBean item corrente, null se nuova entity
     */
    @Override
    protected void openDialog(AEntity entityBean) {
        appContext.getBean(AnnoDialog.class, service, entityClazz).open(entityBean, EAOperation.edit, this::save, this::delete);
    }// end of method

    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void uploadEffettivo() {
        uploadService.uploadAllAnni();
    }// end of method


}// end of class