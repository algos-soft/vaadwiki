package it.algos.vaadwiki.modules.bio;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.AViewList;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadwiki.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.VaadwikiCost.LAST_DOWNLOAD_BIO;
import static it.algos.vaadwiki.application.VaadwikiCost.TAG_BIO;


/**
 * Project vaadbio2 <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Date: 11-ago-2018 17.19.29 <br>
 * <br>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * The only thing that is new here for Spring is the possibility to use dependency injection in the components annotated with @Route. <br>
 * Such a component is instantiated by Spring and becomes a Spring initialized bean. <br>
 * In particular it means that you may autowire other Spring managed beans.
 * Annotated with @Scope (obbligatorio = 'singleton') <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Route(value = TAG_BIO, layout = MainLayout.class)
@Qualifier(TAG_BIO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class BioViewList extends AViewList {


    /**
     * Icona visibile nel menu (facoltativa)
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;
    protected Button ciclodButton;
    protected Button deleteButton;
    protected Button newButton;
    protected Button updateButton;
    protected Button elaboraButton;
    protected Button uploadButton;

    private ATextField input;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private CicloService cicloService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private DeleteService deleteService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private NewService newService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private UpdateService updateService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ElaboraService elaboraService;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private BioService service;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public BioViewList(@Qualifier(TAG_BIO) IAPresenter presenter, @Qualifier(TAG_BIO) IADialog dialog) {
        super(presenter, dialog);
        ((BioViewDialog) dialog).fixFunzioni(this::save, this::delete);
//        this.service = (BioService) presenter.getService();
    }// end of Spring constructor

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     */
    @Override
    protected void fixPreferenzeSpecifiche() {
        super.usaSearchBottoneNew = false;
    }// end of method

    @PostConstruct
    private void creaBottoni() {
        creaCiclo();
        creaDelete();
        creaNew();
        creaUpdate();
        creaElabora();
        creaUpload();

        addBottoni();
        test();
    }// end of method

    private void test() {


        Dialog dialog = new Dialog();
        Div content = new Div();
        content.addClassName("my-style");

        content.setText("This component is styled using global styles");
        dialog.add(content);

// @formatter:off
        String styles = ".my-style { "
                + "  color: red;"
                + " }";
// @formatter:on

        /*
         * The code below register the style file dynamically. Normally you
         * use @StyleSheet annotation for the component class. This way is
         * chosen just to show the style file source code.
         */
        StreamRegistration resource = UI.getCurrent().getSession()
                .getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () -> {
                    byte[] bytes = styles.getBytes(StandardCharsets.UTF_8);
                    return new ByteArrayInputStream(bytes);
                }));
        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());

        dialog.setWidth("400px");
        dialog.setHeight("150px");

        newButton.addClickListener(event -> {
            dialog.open();
            input.getElement().callFunction("focus");
        });
        input = new ATextField();

        dialog.add(input);
        NativeButton confirmButton = new NativeButton("Confirm", event -> {
            service.downloadBio(input.getValue());
            dialog.close();
        });
        NativeButton cancelButton = new NativeButton("Cancel", event -> {
            dialog.close();
        });
        dialog.add(cancelButton);
        dialog.add(confirmButton);
    }// end of method

    private void creaCiclo() {
        ciclodButton = new Button("Ciclo");
        ciclodButton.addClickListener(e -> cicloService.esegue());
    }// end of method

    private void creaDelete() {
        deleteButton = new Button("Delete");
//        deleteButton.addClickListener(e -> deleteService.esegue(null));
    }// end of method

    private void creaNew() {
        newButton = new Button("New");
//        newButton.addClickListener(e -> newService.esegue(null));
    }// end of method

    private void creaUpdate() {
        updateButton = new Button("Update");
//        updateButton.addClickListener(e -> updateService.esegue());
    }// end of method

    private void creaElabora() {
        elaboraButton = new Button("Elabora");
        elaboraButton.addClickListener(e -> elaboraService.esegue());
    }// end of method

    private void creaUpload() {
        uploadButton = new Button("Upload");
//        uploadButton.addClickListener(e -> cicloService.esegue());
    }// end of method



    protected void addBottoni() {
        if (topLayout != null) {
            topLayout.removeAll();
            topLayout.add(ciclodButton);
            topLayout.add(deleteButton);
            topLayout.add(newButton);
            topLayout.add(updateButton);
            topLayout.add(elaboraButton);
            topLayout.add(uploadButton);
        }// end of if cycle
    }// end of method


    /**
     * Aggiunge eventuali colonne calcolate
     */
    protected void addSpecificColumnsBefore() {
        String key = "pageid";

        grid.addColumn(new ComponentRenderer<>(entity -> {
            long pageid = ((Bio) entity).getPageId();

            // button for opening the wiki page
            Button link = new Button(pageid + "", event -> {
                String title = ((Bio) entity).getWikiTitle();
                String edit = "https://it.wikipedia.org/w/index.php?title=" + title + "&action=edit&section=0";
                String wikiLink = "\"" + edit + "\"";

                UI.getCurrent().getPage().executeJavaScript("window.open(" + wikiLink + ");");
            });

            return link;
        })).setHeader(key);

    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected VerticalLayout creaCaption() {
        VerticalLayout layout = super.creaCaption("");
        LocalDateTime lastDownload = pref.getDate(LAST_DOWNLOAD_BIO);
        String message = "";
        String time = "";
        int durata;
        String durataTxt = "";

        layout.add(new Label("Aggiornamento automatico: " + EASchedule.giorno.getNota()));
        if (lastDownload != null) {
//            message = pref.getDesc(LAST_DOWNLOAD_BIO) + ": ";@todo rimettere
            time = date.getTime(lastDownload);
//            durata = pref.getInt(durataLastDownload);
//            durataTxt = " (" + durata + " sec)";
            layout.add(new Label(message + time));
        }// end of if cycle

        return null;
    }// end of method

}// end of class