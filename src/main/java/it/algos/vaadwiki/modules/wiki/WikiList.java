package it.algos.vaadwiki.modules.wiki;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.selection.SingleSelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.upload.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.Set;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 11:00
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class WikiList extends AGridViewList {


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected UploadService uploadService;

    @Autowired
    @Qualifier(TAG_BIO)
    protected AttNazProfCatService attNazProfCatService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected LibBio libBio;



    //--Soglia minima per creare una entity nella collezione Nomi sul mongoDB
    //--Soglia minima per creare una pagina di un nome sul server wiki
    protected int sogliaWiki;


    protected Button uploadOneNatoButton;

    protected Button uploadOneMortoButton;

    protected Button creaButton;

    protected Button updateButton;

    protected Button uploadAllButton;

    protected Button statisticheButton;

    protected Button statistiche2Button;

    protected Button uploadStatisticheButton;

    protected String titoloPaginaStatistiche;

    protected String titoloPaginaStatistiche2;

    protected String codeLastUpload;

    protected String durataLastUpload;

    protected String codeLastUploadStatistiche;

    protected String durataLastUploadStatistiche;

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

    protected boolean usaBottoneUpload;


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param entityClazz modello-dati specifico di questo modulo
     */
    public WikiList(IAService service, Class<? extends AEntity> entityClazz) { //@todo versione 14
        super(service, entityClazz);
    }// end of Vaadin/@Route constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBottoneNew = false;

        super.isEntityModificabile = false;
        super.usaBottoneEdit = true;
        super.usaPagination = true;

        this.usaCreaButton = false;
        this.usaUpdateButton = false;
        this.usaUploadAllButton = true;
        this.usaStatisticheButton = true;
        this.usaBottoneUpload = false;
    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected Label creaInfoImport(ATask task, String flagDaemon, String flagLastUpload) {
        Label label = null;
        String testo = "";
        String tag = "Upload automatico: ";
        String nota = task != null ? task.getNota() : "";
        int durata = pref.getInt(DURATA_UPLOAD_ANNI);
        String message = "";
        LocalDateTime lastUpload = pref.getDate(flagLastUpload);
        testo = tag;

        if (pref.isBool(flagDaemon)) {
            testo += nota;
        } else {
            testo += "disattivato.";
        }// end of if/else cycle

        if (lastUpload != null) {
            message += testo + " Ultimo upload il " + date.getTime(lastUpload);
            message += ", in circa " + durata + " minuti";
        } else {
            if (pref.isBool(flagDaemon)) {
                message = tag + nota + " Non ancora effettuato.";
            } else {
                message = testo;
            }// end of if/else cycle
        }// end of if/else cycle
        label = new Label(message);

        return label;
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

//        SingleSelect<Grid<AEntity>, AEntity> entitySelected = null;
//        boolean selezioneSingola = grid.getSelectionModel() == (GridSelectionModel<AEntity>) Grid.SelectionMode.SINGLE;
//        if (selezioneSingola) {
//            entitySelected = grid.asSingleSelect();
//        }// end of if cycle
//        int alfa = grid.getSelectedItems().size();

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


//    /**
//     * Opens the confirmation dialog before deleting the current item.
//     * <p>
//     * The dialog will display the given title and message(s), then call
//     * <p>
//     */
//    protected void openUploadDialog(String tag) {
//        String message = "Sei sicuro di voler aggiornare su wikipedia tutte le pagine " + tag + " ?";
//        AConfirmDialog dialog = appContext.getBean(AConfirmDialog.class);
//        dialog.open(message, this::uploadEffettivo);
//    }// end of method


    /**
     * Opens the confirmation dialog before deleting the current item.
     * <p>
     * The dialog will display the given title and message(s), then call
     * <p>
     */
    protected void uploadEffettivo() {
    }// end of method


    protected void sincroBottoniMenu(boolean enabled) {
        if (uploadOneNatoButton != null) {
            uploadOneNatoButton.setEnabled(enabled);
        }// end of if cycle
        if (uploadOneMortoButton != null) {
            uploadOneMortoButton.setEnabled(enabled);
        }// end of if cycle
    }// end of method


    protected void showWikiStatistiche() {
        String link = "\"" + PATH_WIKI + titoloPaginaStatistiche + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void showWikiStatistiche2() {
        String link = "\"" + PATH_WIKI + titoloPaginaStatistiche2 + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void uploadStatistiche() {
    }// end of method

    /**
     * Eventuale caption sopra la grid
     */
    protected Label creaInfoUpload(String flagLastUploadStatistiche, String flagDurataLastUploadStatistiche) {
        Label label = null;
        LocalDateTime lastDownload = pref.getDate(flagLastUploadStatistiche);
        int durata = pref.getInt(flagDurataLastUploadStatistiche);

        if (lastDownload != null) {
            label = new Label("Ultimo upload delle statistiche effettuato il " + date.getTime(lastDownload) + " in " + date.toTextMinuti(durata));
        } else {
            label = new Label("Upload delle statistiche non ancora effettuato");
        }// end of if/else cycle

        return label;
    }// end of method

}// end of class
