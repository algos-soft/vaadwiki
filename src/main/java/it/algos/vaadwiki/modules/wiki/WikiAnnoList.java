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
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoDialog;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadwiki.statistiche.StatisticheAnni;
import it.algos.vaadwiki.upload.UploadAnnoMorto;
import it.algos.vaadwiki.upload.UploadAnnoNato;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

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
@AIView(vaadflow = false, menuName = "anno", menuIcon = VaadinIcon.BOAT, searchProperty = "titolo", roleTypeVisibility = EARoleType.developer)
public class WikiAnnoList extends WikiList {


    @Autowired
    @Qualifier(TASK_ANN)
    protected ATask taskAnni;

    //    @Autowired
    private UploadAnnoNato uploadAnnoNato;

    //    @Autowired
    private UploadAnnoMorto uploadAnnoMorto;


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
    public WikiAnnoList(@Qualifier(TAG_WANN) IAService service) {
        super(service, Anno.class);
    }// end of Vaadin/@Route constructor


    /**
     * Crea effettivamente il Component Grid <br>
     * <p>
     * Può essere Grid oppure PaginatedGrid <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
     * Oppure queste possono essere fatte nella sottoclasse, se non sono standard <br>
     */
    @Override
    protected Grid creaGridComponent() {
        return new PaginatedGrid<Anno>();
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        //--preferenze e bottoni standard
        super.usaButtonDelete = false;

        //--bottoni vaadwiki
        super.usaButtonUpload = true;
        super.usaButtonShowStatisticheA = true;
        super.usaButtonUploadStatistiche = true;

        super.titoloPaginaStatistiche = wikiService.titoloPaginaStatisticheAnni;
        super.task = taskAnni;
        super.flagDaemon = USA_DAEMON_ANNI;

        super.lastUpload = LAST_UPLOAD_ANNI;
        super.durataLastUpload = DURATA_UPLOAD_ANNI;
        super.eaTempoTypeUpload = EATempo.minuti;
        super.lastUploadStatistiche = LAST_UPLOAD_STATISTICHE_ANNI;
        super.durataLastUploadStatistiche = DURATA_UPLOAD_STATISTICHE_ANNI;
        super.eaTempoTypeStatistiche = EATempo.minuti;
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

        alertPlacehorder.add(getLabelBlue(" Progetto:Biografie/Anni"));
        alertPlacehorder.add(getLabelBlue("Sovrascrive il modulo 'Anno' di vaadflow. Contiene i 1.000 anni A.C. ed i 2020 (estensibile) D.C.. Non modificabili."));
        alertPlacehorder.add(new Label("Upload crea le pagina wiki (2) 'Nati nel...' e 'Morti nel...' per ogni anno."));
        alertPlacehorder.add(new Label("Le statistiche contengono il numero delle biografie dei nati/morti per ogni anno."));
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
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        String lar = "7em";
        ComponentRenderer renderer;
        Grid.Column colonna;

        renderer = new ComponentRenderer<>(this::createViewNatoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Test");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createViewMortoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Test");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createWikiNatoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Wiki");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createWikiMortoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Wiki");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createUploadNatoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Upload");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);

        renderer = new ComponentRenderer<>(this::createUploadMortoButton);
        colonna = grid.addColumn(renderer);
        colonna.setHeader("Upload");
        colonna.setWidth(lar);
        colonna.setFlexGrow(0);
    }// end of method


    protected Button createViewNatoButton(Anno entityBean) {
        buttonUploadOneNato = new Button("Nati", new Icon(VaadinIcon.LIST));
        buttonUploadOneNato.getElement().setAttribute("theme", "secondary");
        buttonUploadOneNato.addClickListener(e -> viewNato(entityBean));
        return buttonUploadOneNato;
    }// end of method


    protected Button createViewMortoButton(Anno entityBean) {
        buttonUploadOneMorto = new Button("Morti", new Icon(VaadinIcon.LIST));
        buttonUploadOneMorto.getElement().setAttribute("theme", "secondary");
        buttonUploadOneMorto.getElement().setAttribute("color", "green");
        buttonUploadOneMorto.addClickListener(e -> viewMorto(entityBean));
        return buttonUploadOneMorto;
    }// end of method


    protected Button createWikiNatoButton(Anno entityBean) {
        Element input = ElementFactory.createInput();
        buttonUploadOneNato = new Button("Nati", new Icon(VaadinIcon.SERVER));
        buttonUploadOneNato.getElement().setAttribute("theme", "secondary");
        buttonUploadOneNato.getElement().getStyle().set("background-color", input.getProperty("green"));
        buttonUploadOneNato.addClickListener(e -> wikiPageNato(entityBean));
        return buttonUploadOneNato;
    }// end of method


    protected Button createWikiMortoButton(Anno entityBean) {
        buttonUploadOneMorto = new Button("Morti", new Icon(VaadinIcon.SERVER));
        buttonUploadOneMorto.getElement().setAttribute("theme", "secondary");
//        uploadOneMortoButton.getElement().getClassList().add("green");
        buttonUploadOneMorto.addClickListener(e -> wikiPageMorto(entityBean));
        return buttonUploadOneMorto;
    }// end of method


    protected Button createUploadNatoButton(Anno entityBean) {
        buttonUploadOneNato = new Button("Nati", new Icon(VaadinIcon.UPLOAD));
        buttonUploadOneNato.getElement().setAttribute("theme", "error");
        buttonUploadOneNato.addClickListener(e -> uploadService.uploadAnnoNato(entityBean));
        return buttonUploadOneNato;
    }// end of method


    protected Button createUploadMortoButton(Anno entityBean) {
        buttonUploadOneMorto = new Button("Morti", new Icon(VaadinIcon.UPLOAD));
        buttonUploadOneMorto.getElement().setAttribute("theme", "error");
        buttonUploadOneMorto.addClickListener(e -> uploadService.uploadAnnoMorto(entityBean));
        return buttonUploadOneMorto;
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
     * Upload standard. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void upload(long inizio) {
        uploadService.uploadAllAnni();
        super.upload(inizio);
    }// end of method


    /**
     * Upload standard delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    @Override
    protected void uploadStatistiche(long inizio) {
        statisticheService.updateAnni();
        super.uploadStatistiche(inizio);
    }// end of method

}// end of class