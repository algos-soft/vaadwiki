package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.ui.list.ALayoutViewList;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.DidascaliaService;
import org.springframework.beans.factory.annotation.Autowired;

import static it.algos.vaadwiki.application.WikiCost.PATH_WIKI;
import static it.algos.vaadwiki.application.WikiCost.ROUTE_DIDASCALIE;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sat, 08-Jun-2019
 * Time: 21:14
 * <p>
 * Esempi di didascalie
 * Estende la classe astratta ALayoutViewList (non usa la Grid) <br>
 * <p>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le istanze @Autowired usate da questa classe vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired di questa classe, oppure <br>
 * 2) la property è di una classe che implementa il pattern SINGLETON e viene invocata come getInstance(), oppure <br>
 * 3) vengono usate in un un metodo @PostConstruct di questa classe, perché SpringBoot le inietta DOPO init() <br>
 * 4) vengono usate in un un metodo successivo a beforeEnter(), perché SpringBoot le inietta automaticamente <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 */
@Route(value = ROUTE_DIDASCALIE)
public class ViewDidascalie extends ALayoutViewList {

    //--titolo della pagina di servizio sul server wiki
    protected String titoloPaginaWiki = "Progetto:Biografie/Didascalie";


    private Bio bioDidascalia;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo un eventuale metodo @PostConstruct <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine di init() <br>
     */
    @Autowired
    private BioService bioService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo un eventuale metodo @PostConstruct <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine di init() <br>
     */
    @Autowired
    private DidascaliaService didascaliaService;


    /**
     * Costruttore senza parametri <br>
     */
    public ViewDidascalie() {
        super(null, null);
    }// end of constructor


    public void beforeEnter(BeforeEnterEvent event) {
        this.setMargin(true);
        this.setSpacing(true);
        updateView();
    }// end of method


    /**
     * Le preferenze standard <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
    }// end of method


    /**
     * Placeholder (eventuale, presente di default) SOPRA la Grid <br>
     * - con o senza campo edit search, regolato da preferenza o da parametro <br>
     * - con o senza bottone New, regolato da preferenza o da parametro <br>
     * - con eventuali altri bottoni specifici <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaTopLayout() {
        String bioIniziale = "Ron Clarke";

        searchField = new TextField("", "Search");
        searchField.setPrefixComponent(new Icon("lumo", "user"));
        searchField.addClassName("view-toolbar__search-field");
        searchField.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        searchField.setValue(bioIniziale);
        searchField.addValueChangeListener(e -> updateView());

        topPlaceholder.add(searchField);

        Button showWikiButton = new Button("View pagina wiki", new Icon(VaadinIcon.TABLE));
        showWikiButton.addClassName("view-toolbar__button");
        showWikiButton.addClickListener(e -> showPaginaWiki());
        topPlaceholder.add(showWikiButton);

        Button uploadWikiButton = new Button("Upload pagina wiki", new Icon(VaadinIcon.UPLOAD));
        uploadWikiButton.getElement().setAttribute("theme", "error");
        uploadWikiButton.addClassName("view-toolbar__button");
        uploadWikiButton.addClickListener(e -> uploadPaginaWiki());
        topPlaceholder.add(uploadWikiButton);
    }// end of method


    @Override
    public void updateView() {
        String bioSelezionataTxt = "";

        if (searchField != null) {
            bioSelezionataTxt = searchField.getValue();

            if (text.isValid(bioSelezionataTxt)) {
                bioDidascalia = bioService.findByKeyUnica(bioSelezionataTxt);
                if (bioDidascalia != null) {
                    creaDidascalie();
                }// end of if cycle
            }// end of if cycle
        }// end of if cycle
    }// end of method


    private void creaDidascalie() {
        if (bioDidascalia != null) {
            gridHolder.removeAll();
            gridHolder.add(didascaliaGiornoNato());
            gridHolder.add(didascaliaGiornoMorto());
            gridHolder.add(didascaliaAnnoNato());
            gridHolder.add(didascaliaAnnoMorto());
            gridHolder.add(didascaliaListe());
            gridHolder.add(didascaliaBiografie());
        }// end of if cycle
    }// end of method


    private Component didascaliaGiornoNato() {
        Label label = new Label();
        String didascalia = "";
        String giornoNato = "";
        String message = "";

        if (bioDidascalia != null) {
            giornoNato = bioDidascalia.getGiornoNato();
            if (text.isValid(giornoNato)) {
                didascalia = didascaliaService.getGiornoNato(bioDidascalia);
                message = "Nati il " + giornoNato + " -> " + didascalia;
                label.setText(message);
            }// end of if cycle
        }// end of if cycle

        return label;
    }// end of method


    private Component didascaliaGiornoMorto() {
        Label label = new Label();
        String didascalia = "";
        String giornoMorto = "";
        String message = "";

        if (bioDidascalia != null) {
            giornoMorto = bioDidascalia.getGiornoMorto();
            if (text.isValid(giornoMorto)) {
                didascalia = didascaliaService.getGiornoMorto(bioDidascalia);
                message = "Morti il " + giornoMorto + " -> " + didascalia;
                label.setText(message);
            }// end of if cycle
        }// end of if cycle

        return label;
    }// end of method


    private Component didascaliaAnnoNato() {
        Label label = new Label();
        String didascalia = "";
        String annoNato = "";
        String message = "";

        if (bioDidascalia != null) {
            annoNato = bioDidascalia.getAnnoNato();
            if (text.isValid(annoNato)) {
                didascalia = didascaliaService.getAnnoNato(bioDidascalia);
                message = "Nati nel " + annoNato + " -> " + didascalia;
                label.setText(message);
            }// end of if cycle
        }// end of if cycle

        return label;
    }// end of method


    private Component didascaliaAnnoMorto() {
        Label label = new Label();
        String didascalia = "";
        String annoMorto = "";
        String message = "";

        if (bioDidascalia != null) {
            annoMorto = bioDidascalia.getAnnoMorto();
            if (text.isValid(annoMorto)) {
                didascalia = didascaliaService.getAnnoMorto(bioDidascalia);
                message = "Morti nel " + annoMorto + " -> " + didascalia;
                label.setText(message);
            }// end of if cycle
        }// end of if cycle

        return label;
    }// end of method


    private Component didascaliaListe() {
        Label label = new Label();
        String didascalia = "";
        String message = "";

        if (bioDidascalia != null) {
            didascalia = didascaliaService.getListe(bioDidascalia);
            message = "Liste di nomi, cognomi " + " -> " + didascalia;
            label.setText(message);
        }// end of if cycle

        return label;
    }// end of method


    private Component didascaliaBiografie() {
        Label label = new Label();
        String didascalia = "";
        String message = "";

        if (bioDidascalia != null) {
            didascalia = didascaliaService.getBiografie(bioDidascalia);
            message = "Incipit della biografia " + " -> " + didascalia;
            label.setText(message);
        }// end of if cycle

        return label;
    }// end of method


    /**
     * Apre una scheda del browser sulla pagina di wikipedia <br>
     */
    private void showPaginaWiki() {
        String link = "\"" + PATH_WIKI + titoloPaginaWiki + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    /**
     * Registra una nuova versione della pagina sul server di wikipedia <br>
     */
    private void uploadPaginaWiki() {
    }// end of method

}// end of class
