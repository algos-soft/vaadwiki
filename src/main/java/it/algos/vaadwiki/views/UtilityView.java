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
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
    private Api api;

    @Autowired
    private Didascalia didascalia;

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
    }// end of method


    public Component creaTitolo() {
        Label label = new Label("Pagina di servizio per alcune utility");
        return label;
    }// end of method


    public Component creaDidascalie() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        Label label = new Label("creaDidascalie");

        Button buttonTest = new Button("Test", new Icon(VaadinIcon.REFRESH));
        buttonTest.getElement().setAttribute("theme", "secondary");
        buttonTest.addClickListener(e -> esegueTest());

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


    /**
     * Test con uscita sul terminale di Idea
     */
    public void esegueTest() {
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
        String testo = VUOTA;

        testo += topLayout();
        testo += bodyLayout(wikiTitle);
        testo += bottomLayout();

        Api.scriveVoce(wikiTitleDebug, testo);
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
