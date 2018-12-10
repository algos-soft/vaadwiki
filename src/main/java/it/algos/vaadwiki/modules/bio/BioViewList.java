package it.algos.vaadwiki.modules.bio;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.ADialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatViewList;
import it.algos.vaadwiki.modules.categoria.CategoriaService;
import it.algos.vaadwiki.service.*;
import it.algos.vaadwiki.task.TaskBio;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static it.algos.vaadwiki.application.WikiCost.*;


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
public class BioViewList extends AttNazProfCatViewList {

    /**
     * Icona visibile nel menu (facoltativa)
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    protected Button ciclodButton;

    protected Button deleteButton;

    protected Button deleteAllButton;

    protected Button newButton;

    protected Button searchButton;

    protected Button updateButton;

    protected Button elaboraButton;

    protected Button uploadButton;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected CategoriaService categoriaService;


    private ATextField input;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ADateService date;

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
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private PageService pageService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private Api api;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskBio taskBio;


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
    }// end of Spring constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     */
    @Override
    protected void fixPreferenzeSpecifiche() {
        super.usaSearchTextField = true;//@todo Provvisorio. Occore sviluppare un searchDialog
        super.usaSearchBottoneNew = false;
        super.usaBottoneEdit = true;
        super.task = taskBio;
        super.codeFlagDownload = USA_DAEMON_BIO;
        super.codeLastDownload = LAST_DOWNLOAD_BIO;
        super.durataLastDownload = DURATA_DOWNLOAD_BIO;
    }// end of method


    protected void creaBottoni() {
        creaCiclo();
        creaDelete();
        creaDeleteAll();
        creaNew();
        creaSearch();
        creaUpdate();
        creaElabora();
        creaUpload();

        addBottoni();
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
        ciclodButton = new Button("Ciclo", new Icon(VaadinIcon.REFRESH));
        ciclodButton.addClickListener(e -> {
            cicloService.esegue();
            updateView();
        });//end of lambda expressions and anonymous inner class
    }// end of method


    private void creaDelete() {
        deleteButton = new Button("Delete", new Icon(VaadinIcon.CLOSE_CIRCLE));
        deleteButton.getElement().setAttribute("theme", "error");
        deleteButton.addClickListener(e -> service.delete(null));
    }// end of method


    private void creaDeleteAll() {
        deleteAllButton = new Button("Delete All", new Icon(VaadinIcon.CLOSE_CIRCLE));
        deleteAllButton.getElement().setAttribute("theme", "error");
        deleteAllButton.addClickListener(e -> openConfirmDialog());
    }// end of method


    private void creaNew() {
        newButton = new Button("New", new Icon("lumo", "plus"));
//        newButton.addClickListener(e -> newService.esegue(null));
    }// end of method


    private void creaSearch() {
        searchButton = new Button("Cerca", new Icon(VaadinIcon.SEARCH));
//        newButton.addClickListener(e -> newService.esegue(null));
    }// end of method


    private void creaUpdate() {
        updateButton = new Button("Update", new Icon(VaadinIcon.DOWNLOAD));
        updateButton.getElement().setAttribute("theme", "primary");

//        updateButton.addClickListener(e -> updateService.esegue());
    }// end of method


    private void creaElabora() {
        elaboraButton = new Button("Elabora", new Icon(VaadinIcon.ARROW_RIGHT));
        elaboraButton.addClickListener(e -> elaboraService.esegue());
    }// end of method


    private void creaUpload() {
        uploadButton = new Button("Upload", new Icon(VaadinIcon.UPLOAD));
        uploadButton.getElement().setAttribute("theme", "error");
//        uploadButton.addClickListener(event -> upload());
        //        uploadButton.addClickListener(e -> cicloService.esegue());
    }// end of method


//    private void cicloTest() {
//        ArrayList<Long> listaVociCategoriaSuServer;
//        listaVociCategoriaSuServer = categoriaService.findPageids();
////        service.deleteAll();
//        newService.esegue(listaVociCategoriaSuServer);
//        super.updateView();
//    }// end of method


    protected void addBottoni() {
        if (topPlaceholder != null) {
            topPlaceholder.removeAll();
            topPlaceholder.add(ciclodButton);
            topPlaceholder.add(deleteButton);
            topPlaceholder.add(deleteAllButton);
            topPlaceholder.add(newButton);
            topPlaceholder.add(searchButton);
            topPlaceholder.add(updateButton);
            topPlaceholder.add(elaboraButton);
            topPlaceholder.add(uploadButton);

            sincroBottoniMenu(null);
        }// end of if cycle
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected  void openConfirmDialog() {
        String message = "Vuoi veramente cancellare TUTTE le biografie ?";
        String additionalMessage = "L'operazione non è reversibile";
        ADeleteDialog dialog = appContext.getBean(ADeleteDialog.class);
        dialog.open(message, additionalMessage, this::openSecondConfirmDialog);
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected  void openSecondConfirmDialog() {
        String message = "SEI ASSOLUTAMENTE SICURO ?";
        ADeleteDialog dialog =  appContext.getBean(ADeleteDialog.class);
        dialog.open(message, this::deleteMongo);
    }// end of method


    /**
     * Crea il corpo centrale della view
     * Componente grafico obbligatorio
     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto)
     */
    @Override
    protected void creaGrid() {
        super.creaGrid();
        grid.addSelectionListener(new SelectionListener<Grid<AEntity>, AEntity>() {

            @Override
            public void selectionChange(SelectionEvent<Grid<AEntity>, AEntity> selectionEvent) {
                sincroBottoniMenu(selectionEvent);
            }// end of inner method
        });//end of lambda expressions and anonymous inner class
    }// end of method


    protected void sincroBottoniMenu(SelectionEvent<Grid<AEntity>, AEntity> selectionEvent) {
        boolean enabled = selectionEvent != null && selectionEvent.getAllSelectedItems().size() > 0;

        deleteButton.setEnabled(enabled);
        deleteAllButton.setEnabled(!enabled);
        updateButton.setEnabled(enabled);
        elaboraButton.setEnabled(enabled);
        uploadButton.setEnabled(enabled);

    }// end of method


    /**
     * Aggiunge eventuali colonne calcolate
     */
    protected void addSpecificColumnsBefore() {
        String key = "pageid";

        grid.addColumn(new ComponentRenderer<>(entity -> {
            long pageid = ((Bio) entity).getPageid();

            // button for opening the wiki page
            Button link = new Button(pageid + "", event -> {
                String title = ((Bio) entity).getWikiTitle();
                String edit = "https://it.wikipedia.org/w/index.php?title=" + title + "&action=edit&section=0";
                String wikiLink = "\"" + edit + "\"";

                UI.getCurrent().getPage().executeJavaScript("window.open(" + wikiLink + ");");
            });

            return link;
        })).setHeader(key);
        super.addSpecificColumnsBefore();
    }// end of method


}// end of class