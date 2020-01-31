package it.algos.vaadwiki.modules.nome;

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
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadflow.wrapper.AFiltro;
import it.algos.vaadwiki.modules.wiki.WikiList;
import it.algos.vaadwiki.statistiche.StatisticheNomiA;
import it.algos.vaadwiki.statistiche.StatisticheNomiB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;
import static it.algos.vaadwiki.modules.nome.NomeService.TITOLO_TEMPLATE_INCIPIT_NOMI;

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
@Route(value = TAG_NOM, layout = MainLayout14.class)
@Qualifier(TAG_NOM)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "nome", menuIcon = VaadinIcon.BOAT, searchProperty = "nome", roleTypeVisibility = EARoleType.developer)
public class NomeList extends WikiList {


    @Autowired
    public MongoOperations mongoOp;

    @Autowired
    @Qualifier(TASK_NOM)
    protected ATask taskNomi;


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
    public NomeList(@Qualifier(TAG_NOM) IAService service) {
        super(service, Nome.class);
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
        return new PaginatedGrid<Nome>();
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

        super.titoloPaginaStatistiche = ((NomeService) service).TITOLO_PAGINA_WIKI;
        super.titoloPaginaStatistiche2 = ((NomeService) service).TITOLO_PAGINA_WIKI_2;
        this.sogliaWiki = pref.getInt(SOGLIA_NOMI_PAGINA_WIKI, 50);
        super.usaPagination = true;
        super.usaPopupFiltro = true;
        super.task = taskNomi;
        super.flagDaemon = USA_DAEMON_NOMI;

        this.lastDownload = VUOTA;
        this.durataLastDownload = VUOTA;
        this.eaTempoTypeDownload = EATempo.nessuno;
        super.lastElaborazione = LAST_ELABORA_NOME;
        super.durataLastElaborazione = DURATA_ELABORA_NOMI;
        super.eaTempoTypeElaborazione = EATempo.minuti;
        super.lastUpload = LAST_UPLOAD_NOMI;
        super.durataLastUpload = DURATA_UPLOAD_NOMI;
        super.eaTempoTypeUpload = EATempo.minuti;
        super.lastUploadStatistiche = LAST_UPLOAD_STATISTICHE_NOMI;
        super.durataLastUploadStatistiche = DURATA_UPLOAD_STATISTICHE_NOMI;
        super.eaTempoTypeStatistiche = EATempo.secondi;
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

        alertPlacehorder.add(getLabelBlue("Progetto:Antroponimi/Nomi."));
        alertPlacehorder.add(getLabelBlue("Progetto:Antroponimi/Liste nomi."));
        alertPlacehorder.add(getLabelBlue(TITOLO_TEMPLATE_INCIPIT_NOMI));
        alertPlacehorder.add(new Label("Sono elencati i nomi usati nelle biografie"));
        alertPlacehorder.add(new Label("La lista accetta SOLO nomi singoli"));
        alertPlacehorder.add(new Label("La lista prevede nomi utilizzati da almeno 'sogliaNomiMongo' biografie"));
        alertPlacehorder.add(new Label("Upload crea una pagina su wiki per ogni nome che supera 'sogliaNomiWiki' biografie"));
        alertPlacehorder.add(getLabelRed("Quando si elabora la lista dei 'Nomi', i nomi doppi vengono scaricati da wikipedia ed aggiunti a questa lista"));
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

        Button testButton = new Button("Test", new Icon(VaadinIcon.SEARCH));
        testButton.addClassName("view-toolbar__button");
        testButton.addClickListener(e -> test());
        topPlaceholder.add(testButton);

        sincroBottoniMenu(false);
    }// end of method


    /**
     * Crea un (eventuale) Popup di selezione, filtro e ordinamento <br>
     * DEVE essere sovrascritto, per regolare il contenuto (items) <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaPopupFiltro() {
        super.creaPopupFiltro();

        filtroComboBox.setPlaceholder("Selezione ...");
        filtroComboBox.setItems(EASelezioneNomi.values());
        filtroComboBox.setValue(EASelezioneNomi.Dimensioni);
    }// end of method


    /**
     * Aggiorna la lista dei filtri della Grid. Modificati per: popup, newEntity, deleteEntity, ecc... <br>
     * Normalmente tutti i filtri  vanno qui <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse AGridViewList <br>
     * Alla prima visualizzazione della view usa SOLO creaFiltri() e non questo metodo <br>
     * Può essere sovrascritto, per costruire i filtri specifici dei combobox, popup, ecc. <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void updateFiltri() {
        super.updateFiltri();
        EASelezioneNomi selezione = (EASelezioneNomi) filtroComboBox.getValue();
        Sort sort = null;

        if (selezione != null) {
            switch (selezione) {
                case Dimensioni:
                    sort = new Sort(Sort.Direction.DESC, "voci");
                    filtri.add(new AFiltro(sort));

//                items = ((NomeService) service).findAllDimensioni();
//                    filtri.add(Criteria.where("nome").exists(true));
                    break;
                case Alfabetico:
                    sort = new Sort(Sort.Direction.ASC, "nome");
                    filtri.add(new AFiltro(sort));

//                items = ((NomeService) service).findAllAlfabetico();
//                    filtri.add(Criteria.where("nome").exists(false));
                    break;
                case NomiDoppi:
                    filtri.add(new AFiltro(Criteria.where("doppio").is(true)));
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

    }// end of method


    /**
     * Aggiorna gli items della Grid, utilizzando i filtri. <br>
     * Chiamato per modifiche effettuate ai filtri, popup, newEntity, deleteEntity, ecc... <br>
     * <p>
     * Sviluppato nella sottoclasse AGridViewList, oppure APaginatedGridViewList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     */
    public void updateGrid2() {
        super.updateGrid();
        if (array.isValid(filtri)) {
            items = findAllByProperty2(entityClazz, filtri);
        }// end of if cycle

        if (items != null) {
            try { // prova ad eseguire il codice
                grid.deselectAll();
                grid.setItems(items);
                headerGridHolder.setText(getGridHeaderText());
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        creaAlertLayout();
    }// end of method


    /**
     * Returns only the property of the type.
     *
     * @param clazz       della collezione
     * @param listaFiltri per le selezioni di filtro
     *
     * @return entity
     */
    public List<AEntity> findAllByProperty2(Class<? extends AEntity> clazz, List<AFiltro> listaFiltri) {
        List<AEntity> lista;
        Query query = new Query();
        CriteriaDefinition criteria;
        Sort sort;
//        EASelezioneNomi selezione = (EASelezioneNomi) filtroComboBox.getValue();

        if (listaFiltri != null && listaFiltri.size() > 0) {
            for (AFiltro filtro : listaFiltri) {
                criteria = filtro.getCriteria();
                if (filtro.getSort() != null) {
                    sort = filtro.getSort();
                } else {
                    sort = new Sort(Sort.Direction.ASC, criteria.getKey());
                }// end of if/else cycle
                query.addCriteria(criteria);
                query.with(sort);
            }// end of for cycle
        }// end of if cycle

//        if (selezione != null) {
//            switch (selezione) {
//                case Dimensioni:
//                    query.with(new Sort(Sort.Direction.DESC, "voci"));
//                    break;
//                case Alfabetico:
//                    query.with(new Sort(Sort.Direction.ASC, "nome"));
//                    break;
//                case NomiDoppi:
//                    query.with(new Sort(Sort.Direction.ASC, "nome"));
//                    break;
//                default:
//                    log.warn("Switch - caso non definito");
//                    break;
//            } // end of switch statement
//        }// end of if cycle

        lista = (List<AEntity>) mongoOp.find(query, clazz);

        return lista;
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


    protected Button createViewButton(Nome entityBean) {
        Button viewButton = new Button(entityBean.nome, new Icon(VaadinIcon.LIST));

        if (entityBean.nome.contains(" ") && entityBean.doppio == false) {
            viewButton.getElement().setAttribute("theme", "error");
        } else {
            viewButton.getElement().setAttribute("theme", "secondary");
        }// end of if/else cycle

        viewButton.addClickListener(e -> viewNome(entityBean));
        return viewButton;
    }// end of method


    protected Component createWikiButton(Nome entityBean) {
        if (entityBean != null && entityBean.voci >= sogliaWiki) {
            Button wikiButton = new Button(entityBean.nome, new Icon(VaadinIcon.SERVER));
            if (entityBean.nome.contains(" ") && entityBean.doppio == false) {
                wikiButton.getElement().setAttribute("theme", "error");
            } else {
                wikiButton.getElement().setAttribute("theme", "secondary");
            }// end of if/else cycle
            wikiButton.addClickListener(e -> wikiPage(entityBean));
            return wikiButton;
        } else {
            return new Label("");
        }// end of if/else cycle
    }// end of method


    protected Component createUploaButton(Nome entityBean) {
        if (entityBean != null && entityBean.voci >= sogliaWiki && entityBean.valido) {
            Button uploadOneNatoButton = new Button(entityBean.nome, new Icon(VaadinIcon.UPLOAD));
            uploadOneNatoButton.getElement().setAttribute("theme", "error");
            uploadOneNatoButton.addClickListener(e -> uploadService.uploadNome(entityBean));
            return uploadOneNatoButton;
        } else {
            return new Label("");
        }// end of if/else cycle
    }// end of method


    protected void viewNome(Nome nome) {
        getUI().ifPresent(ui -> ui.navigate(ROUTE_VIEW_NOMI + "/" + nome.id));
    }// end of method


    protected void wikiPage(Nome nome) {
        String link = "\"" + PATH_WIKI + uploadService.getTitoloNome(nome) + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    /**
     * Upload standard. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void upload(long inizio) {
        uploadService.uploadAllNomi();
        super.upload(inizio);
    }// end of method


    /**
     * Upload standard delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    @Override
    protected void uploadStatistiche(long inizio) {
        statisticheService.updateNomi();
        super.uploadStatistiche(inizio);
    }// end of method

    /**
     * Stampa nella console la lista di tutti i nomi, nell'ordine selezionato <br>
     */
    public void test() {
        EASelezioneNomi selezione = (EASelezioneNomi) filtroComboBox.getValue();

        if (selezione != null) {
            switch (selezione) {
                case Dimensioni:
                    for (Nome nome : (List<Nome>) items) {
                        System.out.println(nome.voci + " - " + nome.nome);
                    }// end of for cycle
                    break;
                case Alfabetico:
                    items = ((NomeService) service).findAllAlfabetico();
                    break;
                case NomiDoppi:
                    for (Nome nome : (List<Nome>) items) {
                        System.out.println(nome.nome + " - " + nome.voci);
                    }// end of for cycle
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

    }// end of method

}// end of class