package it.algos.vaadwiki.modules.bio;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
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
        super.fixPreferenzeSpecifiche();
        super.usaSearchTextField = false;
        super.usaSearchBottoneNew = true;
        super.usaBottoneStatistiche = false;
        super.usaBottoneModulo = false;
        super.usaBottoneEdit = true;
        super.task = taskBio;
        super.codeFlagDownload = USA_DAEMON_BIO;
        super.codeLastDownload = LAST_DOWNLOAD_BIO;
        super.durataLastDownload = DURATA_DOWNLOAD_BIO;
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
    protected boolean creaTopLayout() {
        super.creaTopLayout();

        //--ciclo
        ciclodButton = new Button("Ciclo", new Icon(VaadinIcon.REFRESH));
        ciclodButton.addClickListener(e -> {
            cicloService.esegue();
            updateView();
        });//end of lambda expressions and anonymous inner class
        topPlaceholder.add(ciclodButton);

        //--delete singola biografia
        deleteButton = new Button("Delete", new Icon(VaadinIcon.CLOSE_CIRCLE));
        deleteButton.getElement().setAttribute("theme", "error");
        deleteButton.addClickListener(e -> service.delete(null));
        topPlaceholder.add(deleteButton);

        //--aggiorna singola biografia
        updateButton = new Button("Update", new Icon(VaadinIcon.DOWNLOAD));
        updateButton.getElement().setAttribute("theme", "primary");
        topPlaceholder.add(updateButton);

        //--elabora singola biografia
        elaboraButton = new Button("Elabora", new Icon(VaadinIcon.ARROW_RIGHT));
        elaboraButton.addClickListener(e -> elaboraService.esegue());
        topPlaceholder.add(elaboraButton);

        //--upload singola biografia
        uploadButton = new Button("Upload", new Icon(VaadinIcon.UPLOAD));
        uploadButton.getElement().setAttribute("theme", "error");
        topPlaceholder.add(uploadButton);

        sincroBottoniMenu(null);
        return topPlaceholder.getComponentCount() > 0;
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void openConfirmDialog() {
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
    protected void openSecondConfirmDialog() {
        String message = "SEI ASSOLUTAMENTE SICURO ?";
        ADeleteDialog dialog = appContext.getBean(ADeleteDialog.class);
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

        if (deleteButton != null) {
            deleteButton.setEnabled(enabled);
        }// end of if cycle

        if (deleteAllButton != null) {
            deleteAllButton.setEnabled(!enabled);
        }// end of if cycle

        if (updateButton != null) {
            updateButton.setEnabled(enabled);
        }// end of if cycle

        if (elaboraButton != null) {
            elaboraButton.setEnabled(enabled);
        }// end of if cycle

        if (uploadButton != null) {
            uploadButton.setEnabled(enabled);
        }// end of if cycle

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