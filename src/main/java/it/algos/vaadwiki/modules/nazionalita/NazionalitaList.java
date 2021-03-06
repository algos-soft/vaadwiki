package it.algos.vaadwiki.modules.nazionalita;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attivita.AttivitaDialog;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatList;
import it.algos.vaadwiki.modules.wiki.WikiList;
import it.algos.vaadwiki.schedule.TaskNazionalita;
import it.algos.vaadwiki.statistiche.StatisticheAttivita;
import it.algos.vaadwiki.statistiche.StatisticheNazionalita;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 5-ott-2018 12.02.34 <br>
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
@Route(value = TAG_NAZ, layout = MainLayout14.class)
@Qualifier(TAG_NAZ)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "nazionalita", menuIcon = VaadinIcon.BOAT, searchProperty = "singolare", roleTypeVisibility = EARoleType.developer)
public class NazionalitaList extends WikiList {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskNazionalita taskNazionalita;

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
    public NazionalitaList(@Qualifier(TAG_NAZ) IAService service) {
        super(service, Nazionalita.class);
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
        return new PaginatedGrid<Nazionalita>();
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
        super.usaButtonDownload = true;
        super.usaButtonUpload = true;
        super.usaButtonModulo = true;
        super.usaButtonShowStatisticheA = true;
        super.usaButtonUploadStatistiche = true;

        super.titoloModulo = wikiService.titoloModuloNazionalita;
        super.titoloPaginaStatistiche = wikiService.titoloPaginaStatisticheNazionalita;
        super.usaPagination = true;

        super.flagDaemon = USA_DAEMON_NAZIONALITA_UPLOAD;
        super.previstoDownload = true;
        super.taskDownload = null;
        super.lastDownload = LAST_DOWNLOAD_NAZIONALITA;
        super.durataLastDownload = DURATA_DOWNLOAD_NAZIONALITA;
        super.eaTempoTypeDownload = EATempo.secondi;

        super.previstoElabora = false;

        super.previstoUpload = true;
        super.taskUpload = taskNazionalita;
        super.lastUpload = LAST_UPLOAD_NAZIONALITA;
        super.durataLastUpload = DURATA_UPLOAD_NAZIONALITA;
        super.eaTempoTypeUpload = EATempo.minuti;

        super.previstoStatistica = true;
        super.lastUploadStatistica = LAST_UPLOAD_STATISTICHE_NAZIONALITA;
        super.durataLastUploadStatistica = DURATA_UPLOAD_STATISTICHE_NAZIONALITA;
        super.eaTempoTypeStatistiche = EATempo.minuti;
    }// end of method


    /**
     * Eventuali messaggi di avviso specifici di questa view ed inseriti in 'alertPlacehorder' <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Normalmente ad uso esclusivo del developer (eventualmente dell'admin) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();

        alertPlacehorder.add(text.getLabelAdmin("Modulo:Bio/Plurale nazionalità."));
        alertPlacehorder.add(text.getLabelAdmin("Progetto:Biografie/Nazionalità."));
        alertPlacehorder.add(new Label("Contiene la tabella di conversione delle nazionalità passate via parametri Nazionalità/Cittadinanza/NazionalitàNaturalizzato,"));
        alertPlacehorder.add(new Label(" da singolare maschile e femminile (usati nell'incipit) al plurale maschile, per categorizzare la pagina"));
        alertPlacehorder.add(new Label("All'interno della tabella le nazionalità sono in ordine alfabetico al fine di rendere più agevole la manutenzione delle stesse."));
        alertPlacehorder.add(new Label("Le nazionalità sono elencate all'interno del modulo con la seguente sintassi:"));
        alertPlacehorder.add(new Label("[\"nazionalitaforma1\"] = \"nazionalità al plurale\","));
        alertPlacehorder.add(new Label("[\"nazionalitaforma2\"] = \"nazionalità al plurale\","));
    }// end of method


    /**
     * Eventuali colonne calcolate aggiunte DOPO quelle automatiche
     * Sovrascritto
     */
    protected void addSpecificColumnsAfter() {
        String lar = "12em";
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


    protected Button createViewButton(Nazionalita entityBean) {
        Button viewButton = new Button(entityBean.plurale, new Icon(VaadinIcon.LIST));
        viewButton.getElement().setAttribute("theme", "secondary");
        viewButton.addClickListener(e -> viewNazionalita(entityBean));
        return viewButton;
    }// end of method


    protected Component createWikiButton(Nazionalita entityBean) {
        Button wikiButton = new Button(entityBean.plurale, new Icon(VaadinIcon.SERVER));
        wikiButton.getElement().setAttribute("theme", "secondary");
        wikiButton.addClickListener(e -> wikiPage(entityBean));
        return wikiButton;
    }// end of method


    protected Component createUploaButton(Nazionalita entityBean) {
        Button uploadButton = new Button(entityBean.plurale, new Icon(VaadinIcon.UPLOAD));
        uploadButton.getElement().setAttribute("theme", "error");
        uploadButton.addClickListener(e -> uploadService.uploadNazionalita(entityBean));
        return uploadButton;
    }// end of method


    protected void viewNazionalita(Nazionalita nazionalita) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_NAZIONALITA + "/" + nazionalita.id));
    }// end of method


    protected void wikiPage(Nazionalita nazionalita) {
        String link = "\"" + PATH_WIKI + uploadService.getTitoloNazionalita(nazionalita) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method

    /**
     * Upload standard. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void upload(long inizio) {
        uploadService.uploadAllNazionalita();
        super.upload(inizio);
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
        appContext.getBean(NazionalitaDialog.class, service, entityClazz).open(entityBean, isEntityModificabile ? EAOperation.edit : EAOperation.showOnly, this::save, this::delete);
    }// end of method


    /**
     * Upload standard delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    @Override
    protected void uploadStatistiche(long inizio) {
        statisticheService.updateNazionalita();
        super.uploadStatistiche(inizio);
    }// end of method


}// end of class