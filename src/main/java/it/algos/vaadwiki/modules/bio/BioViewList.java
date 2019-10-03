package it.algos.vaadwiki.modules.bio;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AMailService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.fields.ATextField;
import it.algos.vaadwiki.download.*;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatViewList;
import it.algos.vaadwiki.schedule.TaskUpdate;
import it.algos.vaadwiki.upload.Upload;
import it.algos.wiki.Api;
import it.algos.wiki.DownloadResult;
import it.algos.wiki.web.AQueryBot;
import it.algos.wiki.web.WLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

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


    protected Button deleteButton;

    protected Button downloadButton;

    protected Button cicloButton;

    protected Button elaboraButton;

    protected Button updateButton;

    protected Button uploadButton;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mailService;

//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    protected CategoriaService categoriaService;


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
    private CicloDownload cicloDownload;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private CicloUpdate cicloUpdate;

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
    private TaskUpdate taskUpdate;

    @Autowired
    private WLogin wLogin;

//    @Autowired
//    private Upload upload;
//
//    @Autowired
//    private UploadGiorni giorni;
//
//    @Autowired
//    private UploadAnni anni;

    private AComboBox<Upload> comboUpload;

    private PaginatedGrid<Bio> gridPaginated;


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
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaSearchTextField = false;
        super.usaSearchBottoneNew = true;
        super.usaBottoneDeleteMongo = true;
        super.usaBottoneDownload = true;
        super.usaBottoneUpload = false;
        super.usaBottoneStatistiche = false;
        super.usaBottoneModulo = false;
        super.usaBottoneEdit = true;
        super.task = taskUpdate;
        super.usaPagination = false;
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
    protected void creaTopLayout() {
//        //--cancella il database
//        deleteButton = new Button("Delete", new Icon(VaadinIcon.CLOSE_CIRCLE));
//        deleteButton.getElement().setAttribute("theme", "error");
//        deleteButton.addClickListener(e -> openConfirmDeleteDialog());
//        topPlaceholder.add(deleteButton);
//
        //-- bottoni 'Ricerca' e 'New'
        super.creaTopLayout();
        newButton.getElement().setAttribute("theme", "secondary");

//        //--download iniziale
//        downloadButton = new Button("Reset", new Icon(VaadinIcon.DOWNLOAD));
//        downloadButton.getElement().setAttribute("theme", "error");
//        downloadButton.addClickListener(e -> openConfirmResetDialog());
//        topPlaceholder.add(downloadButton);

//        //--ciclo upodate corrente
//        updateButton = new Button("Update", new Icon(VaadinIcon.DOWNLOAD));
//        updateButton.getElement().setAttribute("theme", "primary");
//        updateButton.addClickListener(e -> download());
//        topPlaceholder.add(updateButton);

        //--ri-elabora tutte le biografie
        elaboraButton = new Button("Elabora", new Icon(VaadinIcon.ARROW_RIGHT));
        elaboraButton.addClickListener(e -> elaboraService.esegue());
        topPlaceholder.add(elaboraButton);

//        //--upload le pagine cronologiche (giorni ed anni)
//        uploadButton = new Button("Upload", new Icon(VaadinIcon.UPLOAD));
//        uploadButton.getElement().setAttribute("theme", "error");
//        topPlaceholder.add(uploadButton);
//
//        cicloButton = new Button("Ciclo", new Icon(VaadinIcon.UPLOAD));
//        cicloButton.getElement().setAttribute("theme", "error");
////        cicloButton.addClickListener(e -> service.delete(null));
//        topPlaceholder.add(cicloButton);

//        //--aggiorna singola biografia
//        updateButton = new Button("Update", new Icon(VaadinIcon.DOWNLOAD));
//        updateButton.getElement().setAttribute("theme", "primary");
//        topPlaceholder.add(updateButton);
//
//        //--elabora singola biografia
//        elaboraButton = new Button("Elabora", new Icon(VaadinIcon.ARROW_RIGHT));
//        elaboraButton.addClickListener(e -> elaboraService.esegue());
//        topPlaceholder.add(elaboraButton);

//        topPlaceholder.add(creaPopup());

//        sincroBottoniMenu(false);
    }// end of method


    /**
     * Eventuale header text
     */
    protected void fixGridHeader() {
        int numRec = service.count();
        String message = "Ci sono " + text.format(numRec) + " biografie che non posso mostrare perché è troppo lento";

        try { // prova ad eseguire il codice
            HeaderRow topRow = grid.prependHeaderRow();
            Grid.Column[] matrix = array.getColumnArray(grid);
            HeaderRow.HeaderCell informationCell = topRow.join(matrix);
            headerGridHolder = new Label(message);
            informationCell.setComponent(headerGridHolder);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch
    }// end of method


//    protected Component creaPopup() {
//        ArrayList<Upload> items = new ArrayList<>();
////        items.add(null);
//        items.add(giorni);
//        items.add(anni);
//        comboUpload = new AComboBox();
//        comboUpload.setWidth("8em");
//        comboUpload.setItems(items);
////        comboUpload.setValue(null);
//        comboUpload.addValueChangeListener(event -> openUploadDialog(event));
//
//        return comboUpload;
//    }// end of method


//    /**
//     * Opens the confirmation dialog before uploadin.
//     * <p>
//     * The dialog will display the given title and message(s), then call
//     */
//    protected void openUploadDialog(HasValue.ValueChangeEvent event) {
//        Upload value = (Upload) event.getValue();
//
//        if (value!=null) {
//            String message = "Vuoi eseguire un UPLOAD di tutte le biografie divise per " + value + " ?";
//            String additionalMessage = "L'operazione non si può interrompere";
//            AConfirmDialog dialog = appContext.getBean(AConfirmDialog.class);
//            dialog.open(message, additionalMessage, new RunnableUploadDialogo(value), null);
//        }// end of if cycle
//
//    }// end of method


    protected void download() {
        DownloadResult result;
        int numRec = service.count();
        String message;

        if (appContext.getBean(AQueryBot.class).isBot()) {
            if (numRec == 0) {
                result = cicloDownload.esegue();
            } else {
                result = cicloUpdate.esegue();
            }// end of if/else cycle
        } else {
            message = "Non riesco a leggere le pagine dal server. Forse non sono loggato come bot";
            log.warn(message);
            logger.warning("Download - " + message);
        }// end of if/else cycle


        updateView();
    }// end of method


    /**
     * Opens the confirmation dialog before deleting all items.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void openConfirmDeleteDialog() {
        String message = "Vuoi veramente cancellare TUTTE le biografie ?";
        String additionalMessage = "L'operazione non è reversibile";
        ADeleteDialog dialog = appContext.getBean(ADeleteDialog.class);
        dialog.open(message, additionalMessage, this::openSecondConfirmDeleteDialog);
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void openSecondConfirmDeleteDialog() {
        String message = "SEI ASSOLUTAMENTE SICURO ?";
        ADeleteDialog dialog = appContext.getBean(ADeleteDialog.class);
        dialog.open(message, this::deleteMongo);
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void openConfirmResetDialog() {
        String message = "Questa operazione di download cancella TUTTE le biografie";
        String additionalMessage = "L'operazione non è reversibile";
        ADeleteDialog dialog = appContext.getBean(ADeleteDialog.class);
        dialog.open(message, this::esegueDownload);
    }// end of method


    protected void esegueDownload() {
        DownloadResult result;
        deleteMongo();
        result = cicloDownload.esegue();
        updateView();
    }// end of method


//    /**
//     * Crea il corpo centrale della view
//     * Componente grafico obbligatorio
//     * Alcune regolazioni vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
//     * Facoltativo (presente di default) il bottone Edit (flag da mongo eventualmente sovrascritto)
//     */
//    @Override
//    protected void creaGrid() {
//        super.creaGrid();
//        grid.addSelectionListener(new SelectionListener<Grid<AEntity>, AEntity>() {
//
//            @Override
//            public void selectionChange(SelectionEvent<Grid<AEntity>, AEntity> selectionEvent) {
//                sincroBottoniMenu(selectionEvent);
//            }// end of inner method
//        });//end of lambda expressions and anonymous inner class
//    }// end of method


//    protected void sincroBottoniMenu(boolean enabled) {
//        if (deleteButton != null) {
//            deleteButton.setEnabled(enabled);
//        }// end of if cycle
//
//        if (deleteAllButton != null) {
//            deleteAllButton.setEnabled(!enabled);
//        }// end of if cycle
//    }// end of method


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


//    private class RunnableUploadDialogo implements Runnable {
//
//        private Upload upload;
//
//
//        public RunnableUploadDialogo(Upload upload) {
//            this.upload = upload;
//        }// end of constructor
//
//
//        /**
//         * When an object implementing interface <code>Runnable</code> is used
//         * to create a thread, starting the thread causes the object's
//         * <code>run</code> method to be called in that separately executing
//         * thread.
//         * <p>
//         * The general contract of the method <code>run</code> is that it may
//         * take any action whatsoever.
//         *
//         * @see Thread#run()
//         */
//        @Override
//        public void run() {
//            esegueUploadDialogo(upload);
//        }// end of method
//
//    }// end of inner class


//    private void sendMail(DownloadResult result) {
//        long inizio = result.getInizio();
//        Date startDate = new Date(inizio);
//        LocalDateTime start = date.dateToLocalDateTime(startDate);
//        LocalDateTime end;
//        String testo = "";
//        testo += A_CAPO;
//        testo += "Carica le nuove pagine biografiche e aggiorna tutte quelle esistenti";
//        testo += A_CAPO;
//        testo += A_CAPO;
//        testo += "Ciclo del " + date.get();
//        testo += A_CAPO;
//        testo += "Iniziato alle " + date.getOrario(start);
//        testo += A_CAPO;
//
//        if (pref.isBool(SEND_MAIL_CICLO)) {
//            end = LocalDateTime.now();
//            testo += "Terminato alle " + date.getOrario(end);
//            testo += A_CAPO;
//            testo += "Durata totale: " + date.deltaText(inizio);
//            testo += A_CAPO;
//            testo += "Nel db ci sono " + text.format(service.count()) + " pagine biografiche";
//            testo += A_CAPO;
//            testo += "Sono state aggiornate " + text.format(result.getNumVociRegistrate()) + " pagine";
//            mailService.send("Ciclo update", testo);
//        }// end of if cycle
//    }// end of method


}// end of class