package it.algos.vaadwiki.modules.attnazprofcat;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadflow.ui.list.APaginatedGridViewList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.PATH_WIKI;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: mar, 10-lug-2018
 * Time: 07:04
 */
@Slf4j
public abstract class AttNazProfCatList extends APaginatedGridViewList {


    @Autowired
    protected ApplicationContext appContext;

    protected Button deleteMongoButton;

    protected Button donwloadMongoButton;

    protected Button uploadStatisticheButton;

    protected Button showCategoriaButton;

    protected Button showModuloButton;

    protected Button showStatisticheButton;

    protected String titoloCategoria;

    protected String titoloModulo;

    protected String titoloPaginaStatistiche;

    protected String codeFlagDownload;

    protected String codeLastDownload;

    protected String durataLastDownload;

    protected boolean usaBottoneUpload;

    protected boolean usaBottoneCategoria;

    protected boolean usaBottoneDeleteMongo;

    protected boolean usaBottoneDownload;

    protected boolean usaBottoneModulo;

    protected boolean usaBottoneStatistiche;

    //    @Autowired
//    @Qualifier(TAG_ATT)
    protected AttNazProfCatService serviceWiki;

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
    public AttNazProfCatList(IAService service, Class<? extends AEntity> entityClazz) {
        super(service, entityClazz);
        serviceWiki = (AttNazProfCatService) service;
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze specifiche di questa view <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse APrefViewList <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        this.usaBottoneUpload = true;
        this.usaBottoneCategoria = false;
        this.usaBottoneDeleteMongo = true;
        this.usaBottoneDownload = true;
        this.usaBottoneModulo = true;
        this.usaBottoneStatistiche = true;

        super.usaBottoneNew = false;
        super.usaBottoneEdit = true;
        super.isEntityModificabile = false;
        super.usaBottoneDeleteAll = true;
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

        if (usaBottoneModulo) {
            showModuloButton = new Button("Modulo", new Icon(VaadinIcon.LIST));
            showModuloButton.addClassName("view-toolbar__button");
            showModuloButton.addClickListener(e -> showWikiModulo());
            topPlaceholder.add(showModuloButton);
        }// end of if cycle

        if (usaBottoneStatistiche) {
            showStatisticheButton = new Button("View statistiche", new Icon(VaadinIcon.TABLE));
            showStatisticheButton.addClassName("view-toolbar__button");
            showStatisticheButton.addClickListener(e -> showWikiStatistiche());
            topPlaceholder.add(showStatisticheButton);
        }// end of if cycle

        if (usaBottoneUpload) {
            uploadStatisticheButton = new Button("Upload statistiche", new Icon(VaadinIcon.UPLOAD));
            uploadStatisticheButton.getElement().setAttribute("theme", "error");
            uploadStatisticheButton.addClassName("view-toolbar__button");
            uploadStatisticheButton.addClickListener(e -> uploadStatistiche());
            topPlaceholder.add(uploadStatisticheButton);
        }// end of if cycle
    }// end of method


    /**
     * Opens the confirmation dialog before deleting all items.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void openConfirmDialog() {
        String message = "Vuoi veramente cancellare TUTTE le entities ?";
        String additionalMessage = "L'operazione non è reversibile";
        ADeleteDialog dialog = appContext.getBean(ADeleteDialog.class);
        dialog.open(message, additionalMessage, this::deleteMongo);
    }// end of method


    protected void deleteMongo() {
        this.service.deleteAll();
        updateFiltri();
        updateGrid();
    }// end of method


    protected void findOrCrea(String singolare, String plurale) {
    }// end of method


    protected void download() {
        serviceWiki.download();
        updateFiltri();
        updateGrid();
    }// end of method


    protected void uploadStatistiche() {
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


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();
        alertPlacehorder.add(creaInfoImport(task, codeFlagDownload, codeLastDownload));
    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected Label creaInfoImport(ATask task, String flagDaemon, String flagLastDownload) {
        Label label = null;
        String testo;
        String tag = "Download automatico";
        LocalDateTime lastDownload = pref.getDate(flagLastDownload);

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
            label = new Label(testo + " Ultimo import il " + date.getTime(lastDownload));
        } else {
            if (pref.isBool(flagDaemon)) {
                label = new Label(tag + ": " + task.getNota() + "." + " Non ancora effettuato.");
            } else {
                label = new Label(testo);
            }// end of if/else cycle
        }// end of if/else cycle

        return label;
    }// end of method


//    @Override
//    public Collection updateItems() {
//        Collection items = null;
//        String filtro = searchField.getValue();
//        items = service.findFilter(filtro);
//
//        return items;
//    }// end of method

}// end of class
