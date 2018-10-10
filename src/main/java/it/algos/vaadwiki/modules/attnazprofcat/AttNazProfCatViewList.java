package it.algos.vaadwiki.modules.attnazprofcat;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.AViewList;
import it.algos.vaadflow.ui.dialog.IADialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.VaadwikiCost.PATH_WIKI;

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


    protected Button deleteMongoButton;
    protected Button donwloadMongoButton;
    protected Button uploadStatisticheButton;
    protected Button showModuloButton;
    protected Button showStatisticheButton;

    protected String titoloModulo;
    protected String titoloPaginaStatistiche;
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
    private void creaBottoni() {
        creaDeleteMongo();
        creaDownload();
        creaUpload();
        creaShowModulo();
        creaShowStatistiche();
        addBottoni();
    }// end of method


    private void creaDeleteMongo() {
        deleteMongoButton = new Button("Delete");
        deleteMongoButton.addClickListener(e -> deleteMongo());
    }// end of method


    private void creaDownload() {
        donwloadMongoButton = new Button("Download");
        donwloadMongoButton.addClickListener(e -> service.download());
    }// end of method


    private void creaUpload() {
        uploadStatisticheButton = new Button("Upload");
        uploadStatisticheButton.addClickListener(e -> uploadStatistiche());
    }// end of method


    private void creaShowModulo() {
        showModuloButton = new Button("Modulo");
        showModuloButton.addClickListener(e -> showWikiModulo());
    }// end of method

    private void creaShowStatistiche() {
        showStatisticheButton = new Button("Statistiche");
        showStatisticheButton.addClickListener(e -> showWikiStatistiche());
    }// end of method


    private void deleteMongo() {
        this.service.deleteAll();
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
        if (topLayout != null && deleteMongoButton != null) {
            topLayout.removeAll();
            topLayout.add(deleteMongoButton);
            topLayout.add(donwloadMongoButton);
            if (usaBottoneUpload) {
                topLayout.add(uploadStatisticheButton);
            }// end of if cycle
            if (usaBottoneModulo) {
                topLayout.add(showModuloButton);
            }// end of if cycle
            if (usaBottoneStatistiche) {
                topLayout.add(showStatisticheButton);
            }// end of if cycle
        }// end of if cycle
    }// end of method

    /**
     * Le preferenze sovrascritte nella sottoclasse
     */
    protected void fixPreferenzeSpecifiche() {
//        super.usaBottoneNew = false; @todo rimettere
        super.isEntityModificabile = false;
    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected VerticalLayout creaCaption() {
        VerticalLayout layout = null;
//        layout = super.creaCaption(); @todo rimettere
        LocalDateTime lastDownload = pref.getDate(codeLastDownload);
        String message = "";
        String time = "";
        int durata = 0;
        String durataTxt = "";

        layout.add(new Label("Aggiornamento automatico: " + EASchedule.giorno.getNota()));
        if (lastDownload != null) {
//            message = pref.getDesc(codeLastDownload) + ": "; @todo rimettere
            time = date.getTime(lastDownload);
//            durata = pref.getInt(durataLastDownload); @todo rimettere
            durataTxt = " (" + durata + " sec)";
            layout.add(new Label(message + time + durataTxt));
        }// end of if cycle

        return null;
    }// end of method

}// end of class
