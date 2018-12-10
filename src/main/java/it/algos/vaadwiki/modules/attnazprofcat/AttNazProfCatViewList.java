package it.algos.vaadwiki.modules.attnazprofcat;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.ui.AViewList;
import it.algos.vaadflow.ui.dialog.ADeleteDialog;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwiki.task.TaskWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.PATH_WIKI;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: mar, 10-lug-2018
 * Time: 07:04
 */
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AttNazProfCatViewList extends AViewList {


    @Autowired
    protected ApplicationContext appContext;

    protected Button deleteMongoButton;

    protected Button donwloadMongoButton;

    protected Button uploadStatisticheButton;

    protected Button showModuloButton;

    protected Button showStatisticheButton;

    protected String titoloModulo;

    protected String titoloPaginaStatistiche;

    protected String codeFlagDownload;

    protected String codeLastDownload;

    protected String durataLastDownload;

    protected boolean usaBottoneUpload = true;

    protected boolean usaBottoneModulo = true;

    protected boolean usaBottoneStatistiche = true;

    /**
     * Il service (singleton) viene recuperato dal presenter <br>
     * Qui si una effettua un casting per usare i metodi specifici <br>
     */
    protected AttNazProfCatService service;

    protected TaskWiki task;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public AttNazProfCatViewList(IAPresenter presenter, IADialog dialog) {
        super(presenter, dialog);
        this.service = (AttNazProfCatService) presenter.getService();
    }// end of Spring constructor


    @PostConstruct
    private void inizia() {
        creaBottoni();
    }// end of method


    protected void creaBottoni() {
        creaDeleteMongo();
        creaDownload();
        creaUpload();
        creaShowModulo();
        creaShowStatistiche();
        addBottoni();
    }// end of method


    private void creaDeleteMongo() {
        deleteMongoButton = new Button("Delete All", new Icon(VaadinIcon.CLOSE_CIRCLE));
        deleteMongoButton.getElement().setAttribute("theme", "error");
        deleteMongoButton.addClickListener(e -> openConfirmDialog());
    }// end of method


    private void creaDownload() {
        donwloadMongoButton = new Button("Download", new Icon(VaadinIcon.DOWNLOAD));
        donwloadMongoButton.getElement().setAttribute("theme", "primary");
        donwloadMongoButton.addClickListener(e -> {
            service.download();
            updateView();
        });//end of lambda expressions and anonymous inner class
    }// end of method


    private void creaUpload() {
        uploadStatisticheButton = new Button("Upload", new Icon(VaadinIcon.UPLOAD));
        uploadStatisticheButton.addClickListener(e -> uploadStatistiche());
    }// end of method


    private void creaShowModulo() {
        showModuloButton = new Button("Modulo", new Icon(VaadinIcon.LIST));
        showModuloButton.addClickListener(e -> showWikiModulo());
    }// end of method


    private void creaShowStatistiche() {
        showStatisticheButton = new Button("Statistiche", new Icon(VaadinIcon.TABLE));
        showStatisticheButton.addClickListener(e -> showWikiStatistiche());
    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected  void openConfirmDialog() {
        String message = "Vuoi veramente cancellare TUTTE le entities ?";
        String additionalMessage = "L'operazione non è reversibile";
        ADeleteDialog dialog = appContext.getBean(ADeleteDialog.class);
        dialog.open(message, additionalMessage, this::deleteMongo);
    }// end of method


    protected void deleteMongo() {
        this.service.deleteAll();
        pref.saveValue(codeLastDownload, null);
        updateView();
    }// end of method


    protected void findOrCrea(String singolare, String plurale) {
    }// end of method


    protected void uploadStatistiche() {
    }// end of method


    protected void showWikiModulo() {
        String link = "\"" + PATH_WIKI + titoloModulo + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void showWikiStatistiche() {
        String link = "\"" + PATH_WIKI + titoloPaginaStatistiche + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void addBottoni() {
        if (topPlaceholder != null && deleteMongoButton != null) {
            topPlaceholder.removeAll();
            topPlaceholder.add(deleteMongoButton);
            topPlaceholder.add(donwloadMongoButton);
            if (usaBottoneUpload) {
                topPlaceholder.add(uploadStatisticheButton);
            }// end of if cycle
            if (usaBottoneModulo) {
                topPlaceholder.add(showModuloButton);
            }// end of if cycle
            if (usaBottoneStatistiche) {
                topPlaceholder.add(showStatisticheButton);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Le preferenze sovrascritte nella sottoclasse
     */
    protected void fixPreferenzeSpecifiche() {
//        super.usaBottoneNew = false; @todo rimettere
        super.usaSearchBottoneNew = false;
        super.isEntityModificabile = false;
    }// end of method


    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive alla grid ed alla lista di elementi
     * Normalmente ad uso esclusivo del developer
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixAlertLayout() {
        super.fixAlertLayout();

        alertPlacehorder.add(creaInfoImport(task, codeFlagDownload, codeLastDownload));
    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected Label creaInfoImport(ATask task, String flagDaemon, String flagLastDownload) {
        Label label = null;
        String testo = "";
        String tag = "Aggiornamento automatico: ";
        String nota = task != null ? task.getNota() : "";

        LocalDateTime lastDownload = pref.getDate(flagLastDownload);
        testo = tag;

        if (pref.isBool(flagDaemon)) {
            testo += nota;
        } else {
            testo += "disattivato.";
        }// end of if/else cycle

        if (lastDownload != null) {
            label = new Label(testo + " Ultimo import il " + date.getTime(lastDownload));
        } else {
            if (pref.isBool(flagDaemon)) {
                label = new Label(tag + nota + " Non ancora effettuato.");
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
