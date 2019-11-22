package it.algos.vaadwiki.modules.wiki;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.selection.SingleSelectionEvent;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.upload.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.Set;

import static it.algos.vaadwiki.application.WikiCost.PATH_WIKI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 11:00
 */
@Slf4j
public abstract class WikiList extends AGridViewList {


    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected UploadService uploadService = UploadService.getInstance();


    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected LibBio libBio = LibBio.getInstance();

    /**
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ApplicationContext appContext;


    //--Soglia minima per creare una entity nella collezione Nomi sul mongoDB
    //--Soglia minima per creare una pagina di un nome sul server wiki
    protected int sogliaWiki;


    protected Button donwloadMongoButton;

    protected Button uploadStatisticheButton;

    protected Button showCategoriaButton;

    protected Button showModuloButton;

    protected Button showStatisticheButton;

    protected boolean usaBottoneCategoria;

    protected boolean usaBottoneDeleteMongo;

    protected boolean usaBottoneDownload;

    protected boolean usaBottoneModulo;


    protected Button uploadOneNatoButton;

    protected Button uploadOneMortoButton;

    protected Button creaButton;

    protected Button updateButton;

    protected Button uploadAllButton;

    protected Button statisticheButton;

    protected Button statistiche2Button;

    protected String titoloCategoria;

    protected String titoloModulo;

    protected String titoloPaginaStatistiche;

    protected String titoloPaginaStatistiche2;

    protected String flagDaemon;

    protected String lastDownload;

    protected String durataLastDownload;

    protected String lastUpload;

    protected String durataLastUpload;

    protected String lastUploadStatistiche;

    protected String durataLastUploadStatistiche;

    protected String infoColor = "green";

    /**
     * Flag di preferenza per usare il bottone creaButton situato nella topLayout. Normalmente false. <br>
     */
    protected boolean usaCreaButton;

    /**
     * Flag di preferenza per usare il bottone updateButton situato nella topLayout. Normalmente false. <br>
     */
    protected boolean usaUpdateButton;

    /**
     * Flag di preferenza per usare il bottone uploadAllButton situato nella topLayout. Normalmente true. <br>
     */
    protected boolean usaUploadAllButton;

    /**
     * Flag di preferenza per usare il bottone usaStatisticheButton situato nella topLayout. Normalmente true. <br>
     */
    protected boolean usaStatisticheButton;

    /**
     * Flag di preferenza per usare il bottone usaStatisticheButton situato nella topLayout. Normalmente true. <br>
     */
    protected boolean usaStatistiche2Button;

    protected boolean usaBottoneUploadStatistiche;

    protected boolean usaBottoneUpload;

    protected WikiService wikiService;

    protected ATask task;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param entityClazz modello-dati specifico di questo modulo
     */
    public WikiList(IAService service, Class<? extends AEntity> entityClazz) {
        super(service, entityClazz);
        this.wikiService = (WikiService) service;
    }// end of Vaadin/@Route constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        this.usaBottoneCategoria = false;
        this.usaBottoneDeleteMongo = true;
        this.usaBottoneDownload = true;
        this.usaBottoneModulo = true;
        this.usaBottoneUploadStatistiche = true;

        super.usaBottoneNew = false;
        super.usaBottoneEdit = true;
        super.isEntityModificabile = false;
        super.usaBottoneDeleteAll = true;
        super.usaPagination = true;

        this.usaCreaButton = false;
        this.usaUpdateButton = false;
        this.usaUploadAllButton = false;
        this.usaStatisticheButton = true;
        this.usaBottoneUploadStatistiche = true;
        this.usaBottoneUpload = false;
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();
        creaInfoDownload(task, flagDaemon, lastDownload, durataLastDownload);
        creaInfoUpload(task, flagDaemon, lastUpload, durataLastUpload);
        creaInfoStatistiche(lastUploadStatistiche, durataLastUploadStatistiche);
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

        if (usaBottoneDownload) {
            donwloadMongoButton = new Button("Download", new Icon(VaadinIcon.DOWNLOAD));
            donwloadMongoButton.getElement().setAttribute("theme", "primary");
            donwloadMongoButton.addClickListener(e -> download());
            topPlaceholder.add(donwloadMongoButton);
        }// end of if cycle

        if (usaBottoneCategoria) {
            showCategoriaButton = new Button("Categoria", new Icon(VaadinIcon.LIST));
            showCategoriaButton.addClassName("view-toolbar__button");
            showCategoriaButton.addClickListener(e -> showWikiCategoria());
            topPlaceholder.add(showCategoriaButton);
        }// end of if cycle

        if (usaCreaButton) {
            creaButton = new Button("Crea all", new Icon(VaadinIcon.LIST));
            creaButton.addClassName("view-toolbar__button");
            creaButton.addClickListener(e -> {
                ((NomeCognomeService) service).crea();
                updateFiltri();
                updateGrid();
            });//end of lambda expressions and anonymous inner class
            topPlaceholder.add(creaButton);
        }// end of if cycle

        if (usaUpdateButton) {
            updateButton = new Button("Elabora all", new Icon(VaadinIcon.LIST));
            updateButton.addClassName("view-toolbar__button");
            updateButton.addClickListener(e -> {
                ((NomeCognomeService) service).update();
                updateFiltri();
                updateGrid();
            });//end of lambda expressions and anonymous inner class
            topPlaceholder.add(updateButton);
        }// end of if cycle

        //--upload della lista completa di 365 + 365 giorni (nel caso di giorni o anni)
        if (usaUploadAllButton) {
            uploadAllButton = new Button("Upload all", new Icon(VaadinIcon.UPLOAD));
            uploadAllButton.getElement().setAttribute("theme", "error");
            uploadAllButton.addClickListener(e -> uploadEffettivo());
            topPlaceholder.add(uploadAllButton);
        }// end of if cycle

        if (usaBottoneModulo) {
            showModuloButton = new Button("Modulo", new Icon(VaadinIcon.LIST));
            showModuloButton.addClassName("view-toolbar__button");
            showModuloButton.addClickListener(e -> showWikiModulo());
            topPlaceholder.add(showModuloButton);
        }// end of if cycle

        if (usaStatisticheButton) {
            statisticheButton = new Button("View statistiche", new Icon(VaadinIcon.TABLE));
            statisticheButton.addClassName("view-toolbar__button");
            statisticheButton.addClickListener(e -> showWikiStatistiche());
            topPlaceholder.add(statisticheButton);
        }// end of if cycle

        if (usaStatistiche2Button) {
            statistiche2Button = new Button("View statistiche 2", new Icon(VaadinIcon.TABLE));
            statistiche2Button.addClassName("view-toolbar__button");
            statistiche2Button.addClickListener(e -> showWikiStatistiche2());
            topPlaceholder.add(statistiche2Button);
        }// end of if cycle


        if (usaBottoneUploadStatistiche) {
            uploadStatisticheButton = new Button("Upload statistiche", new Icon(VaadinIcon.UPLOAD));
            uploadStatisticheButton.getElement().setAttribute("theme", "error");
            uploadStatisticheButton.addClassName("view-toolbar__button");
            uploadStatisticheButton.addClickListener(e -> uploadStatistiche());
            topPlaceholder.add(uploadStatisticheButton);
        }// end of if cycle

        if (usaBottoneUpload) {
            uploadStatisticheButton = new Button("Upload statistiche", new Icon(VaadinIcon.UPLOAD));
            uploadStatisticheButton.getElement().setAttribute("theme", "error");
            uploadStatisticheButton.addClassName("view-toolbar__button");
            uploadStatisticheButton.addClickListener(e -> uploadStatistiche());
            topPlaceholder.add(uploadStatisticheButton);
        }// end of if cycle

        sincroBottoniMenu(false);
    }// end of method


    protected void apreDialogo(SingleSelectionEvent evento, EAOperation operation) {
        AEntity entitySelected = null;
        Set selezione = grid.getSelectedItems();
        boolean selezioneSingola = (selezione != null && selezione.size() == 1);

        if (selezioneSingola) {
            entitySelected = (AEntity) grid.getSelectedItems().toArray()[0];
        }// end of if cycle

        if (evento != null && evento.getOldValue() != evento.getValue()) {
            if (evento.getValue().getClass().getName().equals(entityClazz.getName())) {
                if (usaRouteFormView && text.isValid(routeNameFormEdit)) {
                    AEntity entity = (AEntity) evento.getValue();
                    routeVerso(routeNameFormEdit, entity);
                } else {
//                    dialog.open((AEntity) evento.getValue(), operation, context); //@todo versione 14
                }// end of if/else cycle
            }// end of if cycle
        }// end of if cycle

        if (selezioneSingola) {
            grid.select(entitySelected);
        }// end of if cycle
    }// end of method


    protected void deleteMongo() {
        this.service.deleteAll();
        updateFiltri();
        updateGrid();
    }// end of method


    protected void findOrCrea(String singolare, String plurale) {
    }// end of method



    protected void sincroBottoniMenu(boolean enabled) {
        if (uploadOneNatoButton != null) {
            uploadOneNatoButton.setEnabled(enabled);
        }// end of if cycle
        if (uploadOneMortoButton != null) {
            uploadOneMortoButton.setEnabled(enabled);
        }// end of if cycle
    }// end of method


    protected void showWikiCategoria() {
        String link = "\"" + PATH_WIKI + titoloCategoria + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void showWikiModulo() {
        String link = "\"" + PATH_WIKI + titoloModulo + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void showWikiStatistiche() {
        String link = "\"" + PATH_WIKI + titoloPaginaStatistiche + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void showWikiStatistiche2() {
        String link = "\"" + PATH_WIKI + titoloPaginaStatistiche2 + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void download() {
        wikiService.download();
        updateFiltri();
        updateGrid();
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void uploadEffettivo() {
    }// end of method


    protected void uploadStatistiche() {
    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected void creaInfoDownload(ATask task, String flagDaemon, String flagLastDownload, String flagDurataDownload) {
        String testo;
        String message = "";
        String tag = "Download automatico";
        LocalDateTime lastDownload = pref.getDate(flagLastDownload);
        int durata;

        if (task == null) {
            testo = tag + " non previsto.";
        } else {
            if (pref.isBool(flagDaemon)) {
                testo = tag + ": " + task.getNota() + ".";
            } else {
                testo = tag + " disattivato.";
            }// end of if/else cycle
        }// end of if/else cycle

        if (lastDownload != null) {
            message = testo + " Ultimo import il " + date.getTime(lastDownload);
        } else {
            if (pref.isBool(flagDaemon)) {
                message = tag + ": " + task.getNota() + "." + " Non ancora effettuato.";
            } else {
                message = testo;
            }// end of if/else cycle
        }// end of if/else cycle

        if (text.isValid(flagDurataDownload)) {
            durata = pref.getInt(flagDurataDownload);
            message += ", in " + date.toTextSecondi(durata);
        }// end of if cycle

        if (text.isValid(message)) {
            alertPlacehorder.add(getLabelGreen(message));
        }// end of if cycle
    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected void creaInfoUpload(ATask task, String flagDaemon, String flagLastUpload, String flagDurataLastUpload) {
        String testo = "";
        String message = "";
        String tag = "Upload automatico: ";
        String nota;
        LocalDateTime lastUpload;
        int durata;
        testo = tag;

        if (text.isEmpty(flagDaemon) || text.isEmpty(flagLastUpload)) {
            message = "Upload non previsto";
        } else {
            nota = task != null ? task.getNota() : "";
            lastUpload = pref.getDate(flagLastUpload);
            if (pref.isBool(flagDaemon)) {
                testo += nota;
            } else {
                testo += "disattivato.";
            }// end of if/else cycle

            if (lastUpload != null) {
                message += testo + " Ultimo upload il " + date.getTime(lastUpload);
            } else {
                if (pref.isBool(flagDaemon)) {
                    message = tag + nota + " Non ancora effettuato.";
                } else {
                    message = testo;
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if/else cycle

        if (text.isValid(flagDurataLastUpload)) {
            durata = pref.getInt(flagDurataLastUpload);
            message += ", in " + date.toTextSecondi(durata);
        }// end of if cycle

        if (text.isValid(message)) {
            alertPlacehorder.add(getLabelGreen(message));
        }// end of if cycle
    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected void creaInfoStatistiche(String flagLastUploadStatistiche, String flagDurataLastUploadStatistiche) {
        String message = "";
        LocalDateTime lastDownload = pref.getDate(flagLastUploadStatistiche);
        int durata = pref.getInt(flagDurataLastUploadStatistiche);

        if (lastDownload != null) {
            message = "Ultimo upload delle statistiche effettuato il " + date.getTime(lastDownload) + " in " + date.toTextSecondi(durata);
        } else {
            if (usaBottoneUploadStatistiche) {
                message = "Upload delle statistiche non ancora effettuato";
            } else {
                message = "Upload delle statistiche non previsto";
            }// end of if/else cycle
        }// end of if/else cycle

        if (text.isValid(message)) {
            alertPlacehorder.add(getLabelGreen(message));
        }// end of if cycle
    }// end of method


    /**
     * Label colorata
     */
    protected Label getLabel(String message, String labelColor) {
        Label label = null;

        if (text.isValid(message)) {
            label = new Label(message);
            label.getElement().getStyle().set("color", labelColor);
        }// end of if cycle

        return label;
    }// end of method


    /**
     * Label colorata
     */
    protected Label getLabelRed(String message) {
        return getLabel(message, "red");
    }// end of method


    /**
     * Label colorata
     */
    protected Label getLabelGreen(String message) {
        return getLabel(message, "green");
    }// end of method


    /**
     * Label colorata
     */
    protected Label getLabelBlue(String message) {
        return getLabel(message, "blue");
    }// end of method

}// end of class
