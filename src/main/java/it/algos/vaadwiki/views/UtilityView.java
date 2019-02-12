package it.algos.vaadwiki.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadwiki.didascalia.Didascalia;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.DidascaliaService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.Api;
import it.algos.wiki.Page;
import it.algos.wiki.WikiLoginOld;
import it.algos.wiki.web.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

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

    public static final String wikiTitleDebug = "Utente:Biobot/2";

    public static final String wikiPagineDidascalie = "Progetto:Biografie/Didascalie";

    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    private WikiLoginOld wikiLoginOld;

    @Autowired
    private WLogin wLogin;

    @Autowired
    private Api api;

    @Autowired
    private Didascalia didascalia;

    @Autowired
    private DidascaliaService didascaliaService;

    @Autowired
    private ADateService date;

    @Autowired
    private ATextService text;

    private String wikiTitle = "Ron Clarke";


    @Autowired
    public UtilityView() {
        checkIniziale();
    }// end of Spring constructor


    public void checkIniziale() {
        this.setMargin(true);
        this.setSpacing(true);

        this.add(creaTitolo());
        this.add(creaDidascalie());
        this.add(creaTestLogin());
        this.add(creaTestQuery());
    }// end of method


    public Component creaTitolo() {
        Label label = new Label("Pagina di servizio per alcune utility");
        return label;
    }// end of method


    public Component creaDidascalie() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        Label label = new Label("Didascalie");

        Button buttonTest = new Button("Test", new Icon(VaadinIcon.REFRESH));
        buttonTest.getElement().setAttribute("theme", "secondary");
        buttonTest.addClickListener(e -> esegueTestDidascalie());

        Button buttonUploadTest = new Button("WikiTest", new Icon(VaadinIcon.REFRESH));
        buttonUploadTest.getElement().setAttribute("theme", "secondary");
        buttonUploadTest.addClickListener(e -> esegueUploadTest());

        Button buttonUpload = new Button("Upload", new Icon(VaadinIcon.REFRESH));
        buttonUpload.getElement().setAttribute("theme", "error");
        buttonUpload.addClickListener(e -> esegueUpload());

        Button buttonView = new Button("View", new Icon(VaadinIcon.REFRESH));
        buttonView.getElement().setAttribute("theme", "primary");
        buttonView.addClickListener(e -> esegueView());

        layout.add(label, buttonTest, buttonUploadTest, buttonUpload, buttonView);
        return layout;
    }// end of method


    public Component creaTestLogin() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        Label label = new Label("Login");

        Button buttonLogin = new Button("Test", new Icon(VaadinIcon.REFRESH));
        buttonLogin.getElement().setAttribute("theme", "secondary");
        buttonLogin.addClickListener(e -> esegueTestLogin());

        layout.add(label, buttonLogin);
        return layout;
    }// end of method


    public Component creaTestQuery() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(false);
        layout.setSpacing(true);

        Label label = new Label("Query");

        Button buttonTest = new Button("Test", new Icon(VaadinIcon.REFRESH));
        buttonTest.getElement().setAttribute("theme", "secondary");
        buttonTest.addClickListener(e -> esegueTestQuery());

        layout.add(label, buttonTest);
        return layout;
    }// end of method


    /**
     * Test con uscita sul terminale di Idea
     */
    public void esegueTestDidascalie() {
        log.info("");
        log.info("Algos");
        log.info("");
        log.info("Tipi possibili di discalie");
        log.info("Esempio '" + wikiTitle + "'");
        log.info("");
        Bio bio = api.leggeBio(wikiTitle);
        for (EADidascalia dida : EADidascalia.values()) {
            log.info(dida.name() + ": " + didascalia.esegue(bio, dida));
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
//        Api.scriveVoce(wikiTitleDebug, testo);
    }// end of method


    public void esegueUpload() {
        String testo = VUOTA;

        testo += topLayout();
        testo += bodyLayout(wikiTitle);
        testo += bottomLayout();

        Api.scriveVoce(wikiPagineDidascalie, testo);
    }// end of method


    public void esegueView() {
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
        titoliVociCategoria = appContext.getBean(AQueryCat.class).urlRequestTitle(wikiCat);
        log.info("AQueryCat: " + wikiCat + " - Response: " + (titoliVociCategoria != null ? "OK, costruttore senza parametri - " + titoliVociCategoria.size() + " voci" : "No buono"));

//        wikiCat = "BioBot";
//        long inizio = System.currentTimeMillis();
//        titoliVociCategoria = appContext.getBean(AQueryCat.class, wikiCat).urlRequestTitle();
//        log.info("AQueryCat: " + wikiCat + " - Response: " + (titoliVociCategoria != null ? "OK, costruttore con wikiCat - " + titoliVociCategoria.size() + " voci" : "No buono"));
//        log.info("AQueryCat: " + wikiCat + " - Response: ci sono " + titoliVociCategoria.size() + " voci e sono state caricate in " + date.deltaText(inizio));
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

        if (text.isValid(bio.getGiornoNato())) {
            testo += ASTERISCO;
            testo += pagina;
            testo += "[[Nati il " + bio.getGiornoNato() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.giornoNato) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoNato())) {
            testo += ASTERISCO;
            testo += pagina;
            testo += "[[Nati nel " + bio.getAnnoNato() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.annoNato) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getGiornoMorto())) {
            testo += ASTERISCO;
            testo += pagina;
            testo += "[[Morti il " + bio.getGiornoMorto() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.giornoMorto) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorto())) {
            testo += ASTERISCO;
            testo += pagina;
            testo += "[[Morti nel " + bio.getAnnoMorto() + "]]" + " -> " + "'''" + didascalia.esegue(bio, EADidascalia.annoMorto) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorto())) {
            testo += ASTERISCO;
            testo += "Nelle liste di attività, nazionalità, nome e cognomi";
            testo += " -> " + "'''" + didascalia.esegue(bio, EADidascalia.standard) + "'''";
            testo += A_CAPO;
        }// end of if cycle

        if (text.isValid(bio.getAnnoMorto())) {
            testo += ASTERISCO;
            testo += "Completa (nella biografia)";
            testo += " -> " + "'''" + didascalia.esegue(bio, EADidascalia.completa) + "'''";
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

}// end of class
