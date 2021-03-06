package it.algos.vaadwiki.modules.attivita;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadwiki.modules.wiki.WikiList;
import it.algos.vaadwiki.schedule.TaskAttivita;
import it.algos.vaadwiki.schedule.TaskStatistiche;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 6-ott-2019 17.51.06 <br>
 * <br>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * <p>
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
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 * Annotated with @AIView (facoltativo Algos) per regolare alcune property associate a questa classe <br>
 * Se serve una Grid paginata estende APaginatedGridViewList altrimenti AGridViewList <br>
 * Se si usa APaginatedGridViewList è obbligatorio creare la PaginatedGrid
 * 'tipizzata' con la entityClazz (Collection) specifica nel metodo creaGridPaginata <br>
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Route(value = TAG_ATT, layout = MainLayout14.class)
@Qualifier(TAG_ATT)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "attivita", menuIcon = VaadinIcon.BOAT, searchProperty = "plurale", roleTypeVisibility = EARoleType.developer)
public class AttivitaList extends WikiList {


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private TaskAttivita taskAttivita;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private TaskStatistiche taskStatistica;


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
    public AttivitaList(@Qualifier(TAG_ATT) IAService service) {
        super(service, Attivita.class);
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
        return new PaginatedGrid<Attivita>();
    }// end of method


    /**
     * Preferenze specifiche di questa view <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse APrefViewList <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
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

        super.titoloModulo = wikiService.titoloModuloAttivita;
        super.titoloPaginaStatistiche = wikiService.titoloPaginaStatisticheAttivita;
        super.usaPagination = true;

        super.flagDaemon = USA_DAEMON_ATTIVITA_UPLOAD;
        super.previstoDownload = true;
        super.taskDownload = null;
        super.lastDownload = LAST_DOWNLOAD_ATTIVITA;
        super.durataLastDownload = DURATA_DOWNLOAD_ATTIVITA;
        super.eaTempoTypeDownload = EATempo.secondi;

        super.previstoElabora = false;

        super.previstoUpload = true;
        super.taskUpload = taskAttivita;
        super.lastUpload = LAST_UPLOAD_ATTIVITA;
        super.durataLastUpload = DURATA_UPLOAD_ATTIVITA;
        super.eaTempoTypeUpload = EATempo.minuti;

        super.previstoStatistica = true;
        super.lastUploadStatistica = LAST_UPLOAD_STATISTICHE_ATTIVITA;
        super.durataLastUploadStatistica = DURATA_UPLOAD_STATISTICHE_ATTIVITA;
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

        alertPlacehorder.add(text.getLabelAdmin("Modulo:Bio/Plurale attività."));
        alertPlacehorder.add(text.getLabelAdmin("Progetto:Biografie/Attività."));
        alertPlacehorder.add(new Label("Contiene la tabella di conversione delle attività passate via parametri Attività/Attività2/Attività3,"));
        alertPlacehorder.add(new Label(" da singolare maschile e femminile (usati nell'incipit) al plurale maschile, per categorizzare la pagina"));
        alertPlacehorder.add(new Label("All'interno della tabella le attività sono in ordine alfabetico al fine di rendere più agevole la manutenzione delle stesse."));
        alertPlacehorder.add(new Label("Le attività sono elencate all'interno del modulo con la seguente sintassi:"));
        alertPlacehorder.add(new Label("[\"attivitaforma1\"] = \"attività al plurale\","));
        alertPlacehorder.add(new Label("[\"attivitaforma2\"] = \"attività al plurale\","));
        alertPlacehorder.add(text.getLabelDev("Nella collezione locale mongoDB vengono aggiunte ANCHE le voci delle EX-attività (non presenti nel Modulo su Wiki) recuperate dal modulo Modulo:Bio/Plurale attività genere."));
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


    protected Button createViewButton(Attivita entityBean) {
        Button viewButton = new Button(entityBean.plurale, new Icon(VaadinIcon.LIST));
        viewButton.getElement().setAttribute("theme", "secondary");
        viewButton.addClickListener(e -> viewAttivita(entityBean));
        return viewButton;
    }// end of method


    protected Component createWikiButton(Attivita entityBean) {
        Button wikiButton = new Button(entityBean.plurale, new Icon(VaadinIcon.SERVER));
        wikiButton.getElement().setAttribute("theme", "secondary");
        wikiButton.addClickListener(e -> wikiPage(entityBean));
        return wikiButton;
    }// end of method


    protected Component createUploaButton(Attivita entityBean) {
        Button uploadButton = new Button(entityBean.plurale, new Icon(VaadinIcon.UPLOAD));
        uploadButton.getElement().setAttribute("theme", "error");
        uploadButton.addClickListener(e -> uploadService.uploadAttivita(entityBean));
        return uploadButton;
    }// end of method


    protected void viewAttivita(Attivita attivita) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_ATTIVITA + "/" + attivita.id));
    }// end of method


    protected void wikiPage(Attivita attivita) {
        String link = "\"" + PATH_WIKI + uploadService.getTitoloAttivita(attivita) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    /**
     * Upload standard. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void upload(long inizio) {
        uploadService.uploadAllAttivita();
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
        appContext.getBean(AttivitaDialog.class, service, entityClazz).open(entityBean, isEntityModificabile ? EAOperation.edit : EAOperation.showOnly, this::save, this::delete);
    }// end of method


    /**
     * Upload standard delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    @Override
    protected void uploadStatistiche(long inizio) {
        statisticheService.updateAttivita();
        super.uploadStatistiche(inizio);
    }// end of method


}// end of class