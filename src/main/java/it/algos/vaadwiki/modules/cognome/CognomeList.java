package it.algos.vaadwiki.modules.cognome;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadwiki.modules.wiki.WikiList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 14-giu-2019 16.34.34 <br>
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
@Route(value = TAG_COG, layout = MainLayout14.class)
@Qualifier(TAG_COG)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "cognome", menuIcon = VaadinIcon.BOAT, searchProperty = "cognome", roleTypeVisibility = EARoleType.developer)
public class CognomeList extends WikiList {


    @Autowired
    public MongoOperations mongoOp;

    @Autowired
    @Qualifier(TASK_COG)
    protected ATask taskCognomi;


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
    public CognomeList(@Qualifier(TAG_COG) IAService service) {
        super(service, Cognome.class);
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
        return new PaginatedGrid<Cognome>();
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        //--bottoni vaadwiki
        super.usaButtonElabora = true;
        super.usaButtonUpload = true;
        super.usaButtonShowStatisticheA = true;
        super.usaButtonShowStatisticheB = true;
        super.usaButtonUploadStatistiche = true;


        super.titoloPaginaStatistiche = ((CognomeService) service).TITOLO_PAGINA_WIKI;
        super.titoloPaginaStatistiche2 = ((CognomeService) service).TITOLO_PAGINA_WIKI_2;
        this.sogliaWiki = pref.getInt(SOGLIA_COGNOMI_PAGINA_WIKI, 50);
        super.usaPagination = true;
        super.usaPopupFiltro = true;
        super.taskUpload = taskCognomi;
        super.flagDaemon = USA_DAEMON_COGNOMI;

        this.lastDownload = VUOTA;
        this.durataLastDownload = VUOTA;
        this.eaTempoTypeDownload = EATempo.nessuno;
        super.lastElabora = LAST_ELABORA_COGNOME;
        super.durataLastElabora = DURATA_ELABORA_COGNOMI;
        super.eaTempoTypeElaborazione = EATempo.minuti;
        super.lastUpload = LAST_UPLOAD_COGNOMI;
        super.durataLastUpload = DURATA_UPLOAD_COGNOMI;
        super.eaTempoTypeUpload = EATempo.minuti;
        super.lastUploadStatistica = LAST_UPLOAD_STATISTICHE_COGNOMI;
        super.durataLastUploadStatistica = DURATA_UPLOAD_STATISTICHE_COGNOMI;
        super.eaTempoTypeStatistiche = EATempo.secondi;
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

        alertPlacehorder.add(getLabelAdmin("Progetto:Antroponimi/Cognomi."));
        alertPlacehorder.add(getLabelAdmin("Progetto:Antroponimi/Liste cognomi."));
        alertPlacehorder.add(new Label("Sono elencati i cognomi usati nelle biografie"));
        alertPlacehorder.add(new Label("La lista prevede cognomi utilizzati da almeno " + pref.getInt(SOGLIA_COGNOMI_MONGO) + " (sogliaCognomiMongo) biografie"));
        alertPlacehorder.add(new Label("Upload crea una pagina su wiki per ogni cognome che supera " + pref.getInt(SOGLIA_COGNOMI_PAGINA_WIKI) + " (sogliaCognomiWiki) biografie"));
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

        sincroBottoniMenu(false);
    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        String lar = "9em";
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


    protected Button createViewButton(Cognome entityBean) {
        Button viewButton = new Button(entityBean.cognome, new Icon(VaadinIcon.LIST));
        viewButton.getElement().setAttribute("theme", "secondary");
        viewButton.addClickListener(e -> viewCognome(entityBean));
        return viewButton;
    }// end of method


    protected Component createWikiButton(Cognome entityBean) {
        if (entityBean != null && entityBean.voci >= sogliaWiki) {
            Button uploadOneNatoButton = new Button(entityBean.cognome, new Icon(VaadinIcon.SERVER));
            uploadOneNatoButton.getElement().setAttribute("theme", "secondary");
            uploadOneNatoButton.addClickListener(e -> wikiPage(entityBean));
            return uploadOneNatoButton;
        } else {
            return new Label("");
        }// end of if/else cycle
    }// end of method


    protected Component createUploaButton(Cognome entityBean) {
        if (entityBean != null && entityBean.voci >= sogliaWiki) {
            Button uploadOneNatoButton = new Button(entityBean.cognome, new Icon(VaadinIcon.UPLOAD));
            uploadOneNatoButton.getElement().setAttribute("theme", "error");
            uploadOneNatoButton.addClickListener(e -> uploadService.uploadCognome(entityBean));
            return uploadOneNatoButton;
        } else {
            return new Label("");
        }// end of if/else cycle
    }// end of method


    protected void viewCognome(Cognome cognome) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_COGNOMI + "/" + cognome.id));
    }// end of method


    protected void wikiPage(Cognome cognome) {
        String link = "\"" + PATH_WIKI + uploadService.getTitoloCognome(cognome) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    /**
     * Upload standard. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void upload(long inizio) {
        uploadService.uploadAllCognomi();
        super.upload(inizio);
    }// end of method


    /**
     * Upload standard delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    @Override
    protected void uploadStatistiche(long inizio) {
        statisticheService.updateCognomi();
        super.uploadStatistiche(inizio);
    }// end of method

}// end of class