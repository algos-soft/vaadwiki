package it.algos.vaadwiki.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadwiki.didascalia.Didascalia;
import it.algos.vaadwiki.didascalia.DidascaliaService;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioDialog;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.upload.Upload;
import it.algos.vaadwiki.upload.UploadService;
import it.algos.wiki.Api;
import it.algos.wiki.Page;
import it.algos.wiki.WikiLoginOld;
import it.algos.wiki.web.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.application.WikiCost.PATH_WIKI;
import static it.algos.vaadwiki.application.WikiCost.TAG_UTI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 21-gen-2019
 * Time: 17:03
 */
@UIScope
@Route(value = TAG_UTI, layout = MainLayout.class)
@Qualifier(TAG_UTI)
@AIView(roleTypeVisibility = EARoleType.developer)
@Slf4j
public class UtilityView extends VerticalLayout {

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.MAGIC;

    public static final String IRON_ICON = "build";

    public static final String WIKI_TITLE_DEBUG = "Utente:Biobot/2";

    public static final String wikiPagineDidascalie = "Progetto:Biografie/Didascalie";

    private static List<String> elencoNomiFemminili = new ArrayList<>(Arrays.asList(
            "Paola",
            "Risa",
            "Alicia",
            "Cornelia",
            "Egidia"));

    private static List<String> elencoNomiMaschili = new ArrayList<>(Arrays.asList(
            "João",
            "Francesco",
            "Alric",
            "Ming",
            "Joachim",
            "Max",
            "Ivanko",
            "Nazareno",
            "Diego",
            "Anton",
            "Stephen",
            "Tim",
            "Cornelius",
            "Ottavio",
            "Marcos",
            "Alfredo",
            "Dante",
            "Antonio",
            "Antonino",
            "Marcello",
            "Giulio",
            "Paul",
            "Louis",
            "Angelo",
            "Nino",
            "Iacopo",
            "Daniel",
            "Davide",
            "August",
            "Michele",
            "William",
            "Alessandro",
            "Teodoro",
            "Gianni",
            "Enrico",
            "Manuel",
            "Cesare",
            "Alberto",
            "Orlando",
            "Dario",
            "Franco",
            "Guido",
            "Salvatore",
            "Luigi",
            "Pierluigi",
            "Lorenzo",
            "Albert",
            "Franco",
            "Marco",
            "Renato",
            "Otto",
            "Pietro",
            "Carlo",
            "Gregorio",
            "Amadeo",
            "Anselmo",
            "Sam",
            "Domenico",
            "Giacomo",
            "Claudio"));

    @Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    public UploadService uploadService;

    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    protected BioService bioService;

    @Autowired
    protected BioDialog dialog;

    //    @Autowired
    private WikiLoginOld wikiLoginOld;

    @Autowired
    private WLogin wLogin;

    @Autowired
    private Api api;

    //    @Autowired
    private Didascalia didascalia;

    @Autowired
    private DidascaliaService didascaliaService;

    @Autowired
    private ADateService date;

    @Autowired
    private ATextService text;

    @Autowired
    private LibBio libBio;

    private String wikiTitle = "Ron Clarke";

    private Div pageDidascalie = new Div();

    private Div pageLogin = new Div();

    private Div pageQuery = new Div();

    private Div pageSesso = new Div();

    private Div pageLocalita = new Div();

    private VerticalLayout pageSessoResult = new VerticalLayout();

    private VerticalLayout pageLocalitaResult = new VerticalLayout();


    @Autowired
    public UtilityView() {
        checkIniziale();
    }// end of Spring constructor


    public void checkIniziale() {
        this.setMargin(true);
        this.setSpacing(true);

        this.add(creaTitolo());
        this.creaTab();
        this.creaDidascalie();
        this.creaTestLogin();
        this.creaTestQuery();
        this.creaCheckSesso();
        this.creaCheckLocalita();
    }// end of method


    public Component creaTitolo() {
        Label label = new Label("Pagina di servizio per alcune utility");
        return label;
    }// end of method


    public void creaTab() {
        Tab tabDidascalie = new Tab("Didascalie");
        Tab tabLogin = new Tab("Login");
        Tab tabQuery = new Tab("Query");
        Tab tabSesso = new Tab("Sesso");
        Tab tabLocalita = new Tab("Località");

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tabDidascalie, pageDidascalie);
        tabsToPages.put(tabLogin, pageLogin);
        tabsToPages.put(tabQuery, pageQuery);
        tabsToPages.put(tabSesso, pageSesso);
        tabsToPages.put(tabLocalita, pageLocalita);

        Tabs tabs = new Tabs(tabDidascalie, tabLogin, tabQuery, tabSesso, tabLocalita);
        Div pages = new Div(pageDidascalie, pageLogin, pageQuery, pageSesso, pageLocalita);
        Set<Component> pagesShown = Stream.of(pageDidascalie).collect(Collectors.toSet());

        tabs.addSelectedChangeListener(event -> {
            pagesShown.forEach(page -> page.setVisible(false));
            pagesShown.clear();
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            pagesShown.add(selectedPage);
        });

        this.add(tabs);
        this.add(pages);
    }// end of method


    public void creaDidascalie() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        Button buttonTest = new Button("Test console", new Icon(VaadinIcon.LIST));
        buttonTest.getElement().setAttribute("theme", "secondary");
        buttonTest.addClickListener(e -> esegueTestDidascalieConsole());

        Button buttonUploadTest = new Button("Test debug", new Icon(VaadinIcon.MODAL));
        buttonUploadTest.getElement().setAttribute("theme", "secondary");
        buttonUploadTest.addClickListener(e -> esegueUploadTest());

        Button buttonView = new Button("Wiki view", new Icon(VaadinIcon.SERVER));
        buttonView.getElement().setAttribute("theme", "primary");
        buttonView.addClickListener(e -> mostraPaginaWiki());

        Button buttonUpload = new Button("Upload", new Icon(VaadinIcon.UPLOAD));
        buttonUpload.getElement().setAttribute("theme", "error");
        buttonUpload.addClickListener(e -> esegueUpload());


        layout.add(buttonTest, buttonUploadTest, buttonView, buttonUpload);
        pageDidascalie.add(layout);
        pageDidascalie.setVisible(false);
    }// end of method


    public void creaTestLogin() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        Button buttonLogin = new Button("Test", new Icon(VaadinIcon.REFRESH));
        buttonLogin.getElement().setAttribute("theme", "secondary");
        buttonLogin.addClickListener(e -> esegueTestLogin());

        layout.add(buttonLogin);
        pageLogin.add(layout);
        pageLogin.setVisible(false);
    }// end of method


    public void creaTestQuery() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        Button buttonTest = new Button("Test", new Icon(VaadinIcon.REFRESH));
        buttonTest.getElement().setAttribute("theme", "secondary");
        buttonTest.addClickListener(e -> esegueTestQuery());

        layout.add(buttonTest);
        pageQuery.add(layout);
        pageQuery.setVisible(false);
    }// end of method


    public void creaCheckSesso() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        Button buttonCount = new Button("Count", new Icon(VaadinIcon.QUESTION));
        buttonCount.getElement().setAttribute("theme", "secondary");
        buttonCount.addClickListener(e -> esegueTestCountSex());

        Button buttonSizeF = new Button("Forse", new Icon(VaadinIcon.FEMALE));
        buttonSizeF.getElement().setAttribute("theme", "secondary");
        buttonSizeF.addClickListener(e -> esegueCountF());

        Button buttonSizeM = new Button("Forse", new Icon(VaadinIcon.MALE));
        buttonSizeM.getElement().setAttribute("theme", "secondary");
        buttonSizeM.addClickListener(e -> esegueCountM());

        Button buttonListF = new Button("List", new Icon(VaadinIcon.FEMALE));
        buttonListF.getElement().setAttribute("theme", "secondary");
        buttonListF.addClickListener(e -> esegueListF());

        Button buttonListM = new Button("List", new Icon(VaadinIcon.MALE));
        buttonListM.getElement().setAttribute("theme", "secondary");
        buttonListM.addClickListener(e -> esegueListM());

        layout.add(buttonCount, buttonSizeF, buttonSizeM, buttonListF, buttonListM);
        pageSesso.add(layout);
        pageSesso.add(pageSessoResult);
        pageSesso.setVisible(false);
    }// end of method


    public void creaCheckLocalita() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        Button buttonCount = new Button("Count", new Icon(VaadinIcon.QUESTION));
        buttonCount.getElement().setAttribute("theme", "secondary");
        buttonCount.addClickListener(e -> esegueTestCountLocalita());

        Button buttonList = new Button("List", new Icon(VaadinIcon.FEMALE));
        buttonList.getElement().setAttribute("theme", "secondary");
        buttonList.addClickListener(e -> esegueListLuogoNato());

        layout.add(buttonCount, buttonList);
        pageLocalita.add(layout);
        pageLocalita.add(pageLocalitaResult);
        pageLocalita.setVisible(false);
    }// end of method


    /**
     * Test con uscita sul terminale di Idea
     */
    public void esegueTestDidascalieConsole() {
        String ottenuto = "";
        log.info("");
        log.info("Algos");
        log.info("");
        log.info("Tipi possibili di discalie");
        log.info("Esempio '" + wikiTitle + "'");
        log.info("");
        Bio bio = api.leggeBio(wikiTitle);

        for (EADidascalia type : EADidascalia.values()) {
            ottenuto = didascaliaService.getBaseCon(bio, type);
            if (text.isValid(ottenuto)) {
                log.info(type.name() + ": " + ottenuto);
            }// end of if cycle
        }// end of for cycle

        log.info("");
    }// end of method


    /**
     * Test con uscita sulla pagina wiki di Utente:Gacbot
     */
    public void esegueUploadTest() {
        didascaliaService.esegueTest();
//        String testo = VUOTA;
//
//        testo += topLayout();
//        testo += bodyLayout(wikiTitle);
//        testo += bottomLayout();
//
//        Api.scriveVoce(WIKI_TITLE_DEBUG, testo);
    }// end of method


    public void esegueUpload() {
        String testo = VUOTA;

        testo += topLayout();
        testo += bodyLayout(wikiTitle);
        testo += bottomLayout();

        Api.scriveVoce(wikiPagineDidascalie, testo);
    }// end of method


    public void mostraPaginaWiki() {
        String link = "\"" + PATH_WIKI + wikiPagineDidascalie + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    /**
     * Test con uscita sul terminale di Idea
     */
    public void esegueTestLogin() {
        boolean isBot = false;
        isBot = appContext.getBean(AQueryBot.class).isBot();

        if (wLogin != null) {
            appContext.getBean(AQueryLogin.class);
        }// end of if cycle
    }// end of method


    /**
     * Test con uscita sul terminale di Idea
     */
    public void esegueTestQuery() {
        String urlDomain = "";
        String wikiTitle = "";
        String wikiCat = "";
        String urlResponse = "";
        AQueryPage queryPage;
        Page page;
        ArrayList<String> titoliVociCategoria;


        log.info("");
        log.info("Algos");
        log.info("Integration test per alcune query");
        log.info("");

        urlResponse = appContext.getBean(AQueryHTTP.class).urlRequest(urlDomain);
        log.info("AQueryHTTP: " + urlDomain + " - Response: " + (text.isEmpty(urlResponse) ? "OK, response nulla" : "Qualcosa non ha funzionato"));

        urlDomain = "quattroprovince.it";
        urlResponse = appContext.getBean(AQueryHTTP.class).urlRequest(urlDomain);
        log.info("AQueryHTTP: " + urlDomain + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore senza parametri" : "No buono"));

        urlDomain = "quattroprovince.it";
        urlResponse = appContext.getBean(AQueryHTTP.class, urlDomain).urlResponse();
        log.info("AQueryHTTP: " + urlDomain + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore con urlDomain" : "No buono"));

        urlDomain = "eff.org/https-everywhere";
        urlResponse = appContext.getBean(AQueryHTTPS.class).urlRequest(urlDomain);
        log.info("AQueryHTTPS: " + urlDomain + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore senza parametri" : "No buono"));

        urlDomain = "eff.org/https-everywhere";
        urlResponse = appContext.getBean(AQueryHTTPS.class, urlDomain).urlResponse();
        log.info("AQueryHTTPS: " + urlDomain + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore con urlDomain" : "No buono"));

        wikiTitle = "Sarmato";
        urlResponse = appContext.getBean(AQueryRaw.class).urlRequest(wikiTitle);
        log.info("AQueryRaw: " + wikiTitle + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore senza parametri - " + urlResponse.substring(0, 30) : "No buono"));

        wikiTitle = "Sarmato";
        urlResponse = appContext.getBean(AQueryRaw.class, wikiTitle).urlResponse();
        log.info("AQueryRaw: " + wikiTitle + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore con wikiTitle - " + urlResponse.substring(0, 30) : "No buono"));

        wikiTitle = "Neal Ascherson";
        urlResponse = appContext.getBean(AQueryRaw.class).urlRequest(wikiTitle);
        log.info("AQueryRaw: " + wikiTitle + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore senza parametri - " + urlResponse.substring(0, 30) : "No buono"));

        wikiTitle = "Neal Ascherson";
        urlResponse = appContext.getBean(AQueryRaw.class, wikiTitle).urlResponse();
        log.info("AQueryRaw: " + wikiTitle + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore con wikiTitle - " + urlResponse.substring(0, 30) : "No buono"));

        wikiTitle = "Neal Ascherson";
        queryPage = (AQueryPage) appContext.getBean("AQueryPage");
        if (queryPage != null) {
            page = queryPage.pageResponse(wikiTitle);
            if (page != null && page.isValida() && text.isValid(page.getText())) {
                log.info("AQueryPage: " + wikiTitle + " - Response: " + "OK, costruttore senza parametri - Costruita l'istanza AQueryPage con la Page");
            } else {
                log.info("AQueryPage: " + wikiTitle + " - Response: " + "No buono, non ha costruito la property Page nella entity AQueryPage");
            }// end of if/else cycle
        } else {
            log.info("AQueryPage: " + wikiTitle + " - Response: " + "No buono, non ha costruito la entity AQueryPage");
        }// end of if/else cycle

        wikiTitle = "Neal Ascherson";
        queryPage = (AQueryPage) appContext.getBean("AQueryPage", wikiTitle);
        if (queryPage != null) {
            page = queryPage.pageResponse();
            if (page != null && page.isValida() && text.isValid(page.getText())) {
                log.info("AQueryPage: " + wikiTitle + " - Response: " + "OK, costruttore con wikiTitle - Contenuto: " + page.getText().substring(0, 30));
            } else {
                log.info("AQueryPage: " + wikiTitle + " - Response: " + "No buono, non ha costruito la property Page nella entity AQueryPage");
            }// end of if/else cycle
        } else {
            log.info("AQueryPage: " + wikiTitle + " - Response: " + "No buono, non ha costruito la entity AQueryPage");
        }// end of if/else cycle

        wikiTitle = "Neal Ascherson";
        page = ((AQueryPage) appContext.getBean("AQueryPage", wikiTitle)).pageResponse();
        if (page != null && page.isValida() && text.isValid(page.getText())) {
            log.info("AQueryPage: " + wikiTitle + " - Response: " + "OK, costruttore con wikiTitle - Contenuto: " + page.getText().substring(0, 30));
        } else {
            log.info("AQueryPage: " + wikiTitle + " - Response: " + "No buono, non ha costruito la property Page nella entity AQueryPage");
        }// end of if/else cycle

        wikiTitle = "Riley Cooper";
        urlResponse = ((AQueryVoce) appContext.getBean("AQueryVoce")).urlRequest(wikiTitle);
        log.info("AQueryVoce: " + wikiTitle + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore senza parametri - " + urlResponse.substring(0, 30) : "No buono"));

        wikiTitle = "Riley Cooper";
        urlResponse = ((AQueryVoce) appContext.getBean("AQueryVoce", wikiTitle)).urlRequest();
        log.info("AQueryVoce: " + wikiTitle + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore con wikiTitle - " + urlResponse.substring(0, 30) : "No buono"));

        wikiTitle = "Riley Cooper";
        urlResponse = ((AQueryBio) appContext.getBean("AQueryBio")).urlRequest(wikiTitle);
        log.info("AQueryBio: " + wikiTitle + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore senza parametri - " + urlResponse.substring(0, 30) : "No buono"));

        wikiTitle = "Riley Cooper";
        urlResponse = ((AQueryBio) appContext.getBean("AQueryBio", wikiTitle)).urlRequest();
        log.info("AQueryBio: " + wikiTitle + " - Response: " + (text.isValid(urlResponse) ? "OK, costruttore con wikiTitle - " + urlResponse.substring(0, 30) : "No buono"));

        wikiCat = "Nati nel 1225";
        titoliVociCategoria = appContext.getBean(AQueryCatPagine.class, wikiCat).listaTitle;
        log.info("AQueryCat: " + wikiCat + " - Response: " + (titoliVociCategoria != null ? "OK, costruttore senza parametri - " + titoliVociCategoria.size() + " pagine" : "No buono"));

        wikiTitle = Upload.PAGINA_PROVA;
        appContext.getBean(AQueryWrite.class, wikiTitle, "Quarta prova di scrittura del bot: " + "Biobot").urlRequest();

//        wikiCat = "BioBot";
//        long inizio = System.currentTimeMillis();
//        titoliVociCategoria = appContext.getBean(AQueryCat.class, wikiCat).urlRequestTitle();
//        log.info("AQueryCat: " + wikiCat + " - Response: " + (titoliVociCategoria != null ? "OK, costruttore con wikiCat - " + titoliVociCategoria.size() + " pagine" : "No buono"));
//        log.info("AQueryCat: " + wikiCat + " - Response: ci sono " + titoliVociCategoria.size() + " pagine e sono state caricate in " + date.deltaText(inizio));
////        for (String titolo : titoliVociCategoria) {
////            log.info(titolo);
////        }// end of for cycle
//        log.info("");
//        log.info("Prime 200");
//        for (int k = 0; k < 200; k++) {
//            log.info((k + 1) + " - " + titoliVociCategoria.get(k));
//        }// end of for cycle
//        log.info("Pagina prima del passaggio dei 5.000");
//        for (int k = 4799; k < 5000; k++) {
//            log.info((k + 1) + " - " + titoliVociCategoria.get(k));
//        }// end of for cycle
//        log.info("Pagina dopo il passaggio dei 5.000");
//        for (int k = 5000; k < 5200; k++) {
//            log.info((k + 1) + " - " + titoliVociCategoria.get(k));
//        }// end of for cycle

    }// end of method


    public String topLayout() {
        String testo = VUOTA;
        String oggi = date.get();

        testo += "<noinclude>__NOTOC__{{StatBio|data=" + oggi + "}}</noinclude>";
        testo += A_CAPO;

        return testo;
    }// end of method


    public String bodyLayout(String wikiTitle) {
        String testo = VUOTA;
        Bio bio = api.leggeBio(wikiTitle);
        String paragrafo = "Didascalie";
        String pagina = "Nella pagina con la lista dei ";

        testo += LibBio.setParagrafo(paragrafo);

        testo += "Pagina di servizio per il '''controllo''' delle didascalie utilizzate nelle pagine delle liste di giorni ed anni. Le didascalie sono di diversi tipi:";
        testo += A_CAPO;

        if (text.isValid(bio.getGiornoNascita())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Nati il " + bio.getGiornoNato() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.giornoNato) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoNascita())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Nati nel " + bio.getAnnoNato() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.annoNato) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getGiornoMorte())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Morti il " + bio.getGiornoMorto() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.giornoMorto) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorte())) {
            testo += ASTERISCO;
            testo += pagina;
//            testo += "[[Morti nel " + bio.getAnnoMorto() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.annoMorto) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorte())) {
            testo += ASTERISCO;
            testo += "Nelle liste di attività, nazionalità, nome e cognomi";
//            testo += " -> " + "'''" + didascalia.esegue(bio, EADidascalia.standard) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorte())) {
            testo += ASTERISCO;
            testo += "Completa (nella biografia)";
//            testo += " -> " + "'''" + didascalia.esegue(bio, EADidascalia.completa) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        return testo;
    }// end of method


    public String bottomLayout() {
        String testo = VUOTA;
        String catText;

        testo += A_CAPO;
        testo += "{{BioCorrelate}}";
        testo += A_CAPO;
        testo += A_CAPO;

        catText = "[[Categoria:Progetto Biografie|Didascalie]]";
        catText = LibBio.setNoIncludeMultiRiga(catText);
        testo += catText;

        return testo;
    }// end of method


    /**
     * Voci biografiche senza valore al parametro 'sesso' (che deve essere 'M' o 'F')
     */
    public List<Bio> bioSenzaSesso() {
        List<Bio> lista;
        Query query = new Query(Criteria.where("sesso").exists(false));
        lista = mongoTemplate.find(query, Bio.class, "bio");

        return lista;
    }// end of method


    /**
     * Voci biografiche che contengono una virgola nel parametro luogoNato
     */
    public long bioConLuogoNato() {
        long voci = 0;
        Query query = new Query(Criteria.where("luogoNato").regex(".*,.*"));
        voci = mongoTemplate.count(query, Bio.class);

        return voci;
    }// end of method


    /**
     * Voci biografiche che contengono una virgola nel parametro luogoNatoLink
     */
    public long bioConLuogoNatoLink() {
        long voci = 0;
        Query query = new Query(Criteria.where("luogoNatoLink").regex(".*,.*"));
        voci = mongoTemplate.count(query, Bio.class);

        return voci;
    }// end of method


    /**
     * Voci biografiche che contengono una virgola nel parametro luogoMorto
     */
    public long bioConLuogoMorto() {
        long voci = 0;
        Query query = new Query(Criteria.where("luogoMorto").regex(".*,.*"));
        voci = mongoTemplate.count(query, Bio.class);

        return voci;
    }// end of method


    /**
     * Voci biografiche che contengono una virgola nel parametro luogoMortoLink
     */
    public long bioConLuogoMortoLink() {
        long voci = 0;
        Query query = new Query(Criteria.where("luogoMortoLink").regex(".*,.*"));
        voci = mongoTemplate.count(query, Bio.class);

        return voci;
    }// end of method


    /**
     * Voci biografiche che contengono una virgola nel parametro luogoNato
     */
    public List<Bio> bioLuogoNato() {
        List<Bio> lista;
        Query query = new Query(Criteria.where("luogoNato").regex(".*,.*"));
        lista = mongoTemplate.find(query, Bio.class);

        return lista;
    }// end of method


    /**
     * Numero di voci biografiche senza valore al parametro 'sesso' (che deve essere 'M' o 'F')
     */
    public void esegueTestCountSex() {
        List<Bio> lista = bioSenzaSesso();
        pageSesso.add(new Label(" Ci sono " + lista.size() + " voci biografiche SENZA indicazione (obbligatoria) del genere"));
    }// end of method


    /**
     * Voci biografiche che sono contenute nella lista di controllo
     */
    public List<Bio> bioProbabilmente(List<String> listaControllo) {
        List<Bio> listaFemmine = new ArrayList<>();
        String nome;
        List<Bio> lista = bioSenzaSesso();

        for (Bio bio : lista) {
            nome = bio.getNome();
            if (listaControllo.contains(nome)) {
                listaFemmine.add(bio);
            }// end of if cycle
        }// end of for cycle

        return listaFemmine;
    }// end of method


    /**
     * Numero di voci biografiche che sono PROBABILMENTE di genere femminile
     */
    public void esegueCountF() {
        List<Bio> listaFemmine = bioProbabilmente(elencoNomiFemminili);
        pageSessoResult.add(new Label("Ci sono " + listaFemmine.size() + " voci biografiche che sono PROBABILMENTE di genere femminile"));
    }// end of method


    /**
     * Numero di voci biografiche che sono PROBABILMENTE di genere maschile
     */
    public void esegueCountM() {
        List<Bio> listaMaschi = bioProbabilmente(elencoNomiMaschili);
        pageSessoResult.add(new Label("Ci sono " + listaMaschi.size() + " voci biografiche che sono PROBABILMENTE di genere maschile"));
    }// end of method


    /**
     * Lista di voci biografiche che sono PROBABILMENTE di genere femminile
     */
    public void esegueListF() {
        List<Bio> listaFemmine = bioProbabilmente(elencoNomiFemminili);
        pageSessoResult.add(new Label("Elenco delle " + listaFemmine.size() + " voci biografiche che sono PROBABILMENTE di genere femminile"));
        allRighe(listaFemmine, "F");
    }// end of method


    /**
     * Lista di voci biografiche che sono PROBABILMENTE di genere femminile
     */
    public void esegueListM() {
        List<Bio> listaMaschi = bioProbabilmente(elencoNomiMaschili);
        pageSessoResult.add(new Label("Elenco delle " + listaMaschi.size() + " voci biografiche che sono PROBABILMENTE di genere maschile"));
        allRighe(listaMaschi, "M");
    }// end of method


    public void allRighe(List<Bio> listaGenere, String genere) {
        for (Bio bio : listaGenere) {
            riga(bio, genere);
        }// end of for cycle
    }// end of method


    public void riga(Bio bio, String genere) {
        final String nome = bio.getNome();
        final String wikiTitle = bio.getWikiTitle();

        Button buttonMongo = new Button("Mongo", new Icon(VaadinIcon.DATABASE));
        buttonMongo.getElement().setAttribute("theme", "secondary");
        buttonMongo.addClickListener(e -> apreDialogo(wikiTitle));

        Button buttonWikiShow = new Button("Wiki view", new Icon(VaadinIcon.SEARCH));
        buttonWikiShow.getElement().setAttribute("theme", "secondary");
        buttonWikiShow.addClickListener(e -> libBio.showWikiPage(wikiTitle));

        Button buttonWikiEdit = new Button("Wiki edit", new Icon(VaadinIcon.EDIT));
        buttonWikiEdit.getElement().setAttribute("theme", "secondary");
        buttonWikiEdit.addClickListener(e -> libBio.editWikiPage(wikiTitle));

        Button buttonTest = new Button("Test", new Icon(VaadinIcon.MAGIC));
        buttonTest.getElement().setAttribute("theme", "secondary");
        buttonTest.addClickListener(e -> esegueTestSesso(wikiTitle, genere));

        Button buttonUpload = new Button("Upload", new Icon(VaadinIcon.ARROW_UP));
        buttonUpload.getElement().setAttribute("theme", "error");
        buttonUpload.addClickListener(e -> esegueFixSesso(wikiTitle, genere));

        pageSessoResult.add(new HorizontalLayout(new Label(wikiTitle), buttonMongo, buttonWikiShow, buttonWikiEdit, buttonTest, buttonUpload));
    }// end of method


    public void apreDialogo(String wikiTitle) {
        Bio bio = bioService.findByKeyUnica(wikiTitle);
        appContext.getBean(BioDialog.class, bioService, Bio.class).open(bio, EAOperation.edit, this::save, null, null);
    }// end of method


    protected void save(AEntity entityBean, EAOperation operation) {
        bioService.save(entityBean, operation);
    }// end of method


    public void esegueTestSesso(String wikiTitle, String genere) {
        Bio bio = bioService.findByKeyUnica(wikiTitle);
        bio.setSesso(genere);
        uploadService.uploadBioDebug(bio);
    }// end of method


    public void esegueFixSesso(String wikiTitle, String genere) {
        Bio bio = bioService.findByKeyUnica(wikiTitle);
        bioService.save(bio);
        bio.setSesso(genere);
        uploadService.uploadBio(wikiTitle);
    }// end of method


    /**
     * Numero di voci biografiche senza valore al parametro 'sesso' (che deve essere 'M' o 'F')
     */
    public void esegueTestCountLocalita() {
        pageLocalitaResult.removeAll();
        pageLocalitaResult.add(new Label(" Ci sono " + bioConLuogoNato() + " voci biografiche con una virgola nel campo 'luogoNato'"));
        pageLocalitaResult.add(new Label(" Ci sono " + bioConLuogoNatoLink() + " voci biografiche con una virgola nel campo 'luogoNatoLink'"));
        pageLocalitaResult.add(new Label(" Ci sono " + bioConLuogoMorto() + " voci biografiche con una virgola nel campo 'luogoMorto'"));
        pageLocalitaResult.add(new Label(" Ci sono " + bioConLuogoMortoLink() + " voci biografiche con una virgola nel campo 'luogoMortoLink'"));
    }// end of method


    /**
     * Lista di voci biografiche che hanno una virgola nel parametro luogoNato
     */
    public void esegueListLuogoNato() {
        pageLocalitaResult.removeAll();
        List<Bio> listaLuogoNato = bioLuogoNato();
        pageLocalitaResult.add(new Label("Elenco delle " + listaLuogoNato.size() + " voci biografiche con una virgola nel campo 'luogoNato'"));

        for (Bio bio : listaLuogoNato) {
            riga(bio);
        }// end of for cycle
    }// end of method


    public void riga(Bio bio) {
        final String nome = bio.getNome();
        final String wikiTitle = bio.getWikiTitle();

        Button buttonMongo = new Button("Mongo", new Icon(VaadinIcon.DATABASE));
        buttonMongo.getElement().setAttribute("theme", "secondary");
        buttonMongo.addClickListener(e -> apreDialogo(wikiTitle));

        pageLocalitaResult.add(new HorizontalLayout(new Label(bio.wikiTitle), buttonMongo));
    }// end of method

}// end of class
