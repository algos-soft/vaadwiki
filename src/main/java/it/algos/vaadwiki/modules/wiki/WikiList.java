package it.algos.vaadwiki.modules.wiki;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.list.AGridViewList;
import it.algos.vaadwiki.schedule.TaskStatistiche;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.statistiche.StatisticheService;
import it.algos.vaadwiki.upload.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadflow.service.ADateService.INFERIORE_MINUTO;
import static it.algos.vaadflow.service.ADateService.INFERIORE_SECONDO;
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


//    /**
//     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
//     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
//     */
//    protected UploadService uploadService = UploadService.getInstance();

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected UploadService uploadService;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected StatisticheService statisticheService;

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


    /**
     * Bottone opzionale (un flag controlla se mostrarlo o meno) (primo dopo quelli standard) <br>
     */
    protected Button buttonDownload;

    /**
     * Flag di preferenza per eseguire 'download'. Normalmente false. <br>
     */
    protected boolean usaButtonDownload;

    /**
     * Bottone opzionale (un flag controlla se mostrarlo o meno) (secondo dopo quelli standard) <br>
     */
    protected Button buttonUpdate;

    /**
     * Flag di preferenza per eseguire 'update'. Normalmente false. <br>
     */
    protected boolean usaButtonUpdate;

    /**
     * Bottone opzionale (un flag controlla se mostrarlo o meno) (terzo dopo quelli standard) <br>
     */
    protected Button buttonElabora;

    /**
     * Flag di preferenza per eseguire 'elabora''. Normalmente false. <br>
     */
    protected boolean usaButtonElabora;


    /**
     * Bottone opzionale (un flag controlla se mostrarlo o meno) (quarto dopo quelli standard) <br>
     */
    protected Button buttonUpload;

    /**
     * Flag di preferenza per eseguire 'upload''. Normalmente false. <br>
     */
    protected boolean usaButtonUpload;


    /**
     * Bottone opzionale (un flag controlla se mostrarlo o meno) (quinto dopo quelli standard) <br>
     */
    protected Button buttonModulo;

    /**
     * Flag di preferenza per eseguire 'modulo''. Normalmente false. <br>
     */
    protected boolean usaButtonModulo;


    /**
     * Bottone opzionale (un flag controlla se mostrarlo o meno) (sesto dopo quelli standard) <br>
     */
    protected Button buttonShowStatisticheA;

    /**
     * Flag di preferenza per eseguire 'show statistiche A''. Normalmente false. <br>
     */
    protected boolean usaButtonShowStatisticheA;

    /**
     * Bottone opzionale (un flag controlla se mostrarlo o meno) (settimo dopo quelli standard) <br>
     */
    protected Button buttonShowStatisticheB;

    /**
     * Flag di preferenza per eseguire 'show statistiche B''. Normalmente false. <br>
     */
    protected boolean usaButtonShowStatisticheB;


    /**
     * Bottone opzionale (un flag controlla se mostrarlo o meno) (ottavo dopo quelli standard) <br>
     */
    protected Button buttonUploadStatistiche;

    /**
     * Flag di preferenza per eseguire 'upload statistiche''. Normalmente false. <br>
     */
    protected boolean usaButtonUploadStatistiche;

    protected Button buttonUploadOneNato;

    protected Button buttonUploadOneMorto;


    protected String titoloCategoria;

    protected String titoloModulo;

    protected String titoloPaginaStatistiche;

    protected String titoloPaginaStatistiche2;

    protected String flagDaemon;

    protected String lastDownload;

    protected String durataLastDownload;

    protected String lastElabora;

    protected String durataLastElabora;

    protected String lastUpload;

    protected String durataLastUpload;

    protected String lastUploadStatistica;

    protected String durataLastUploadStatistica;

    protected String infoColor = "green";


    protected WikiService wikiService;

    protected boolean previstoDownload;

    protected boolean previstoElabora;

    protected boolean previstoUpload;

    protected boolean previstoStatistica;

    protected ATask taskDownload;

    protected ATask taskElabora;

    protected ATask taskUpload;

    protected EATempo eaTempoTypeDownload;

    protected EATempo eaTempoTypeElaborazione;

    protected EATempo eaTempoTypeUpload;

    protected EATempo eaTempoTypeStatistiche;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private TaskStatistiche taskStatistica;


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

        //--preferenze e bottoni standard
        super.usaButtonDelete = true;
        super.usaButtonReset = false;
        super.usaButtonNew = false;
        super.usaBottoneEdit = true;
        super.isEntityModificabile = false;
        super.usaPagination = true;

        //--bottoni vaadwiki
        super.usaSecondTopPlaceholder = true;
        this.usaButtonDownload = false;
        this.usaButtonUpdate = false;
        this.usaButtonElabora = false;
        this.usaButtonUpload = false;
        this.usaButtonModulo = false;
        this.usaButtonShowStatisticheA = false;
        this.usaButtonShowStatisticheB = false;
        this.usaButtonUploadStatistiche = false;

        this.lastDownload = VUOTA;
        this.durataLastDownload = VUOTA;
        this.eaTempoTypeDownload = EATempo.nessuno;
        this.lastElabora = VUOTA;
        this.durataLastElabora = VUOTA;
        this.eaTempoTypeElaborazione = EATempo.nessuno;
        this.lastUpload = VUOTA;
        this.durataLastUpload = VUOTA;
        this.eaTempoTypeUpload = EATempo.nessuno;
        this.lastUploadStatistica = VUOTA;
        this.durataLastUploadStatistica = VUOTA;
        this.eaTempoTypeStatistiche = EATempo.nessuno;
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

        creaInfo("download", previstoDownload, taskDownload, flagDaemon, lastDownload, durataLastDownload);
        creaInfo("elabora", previstoElabora, taskElabora, flagDaemon, lastElabora, durataLastElabora);
        creaInfo("upload", previstoUpload, taskUpload, flagDaemon, lastUpload, durataLastUpload);
        creaInfo("statistica", previstoStatistica, taskStatistica, flagDaemon, lastUploadStatistica, durataLastUploadStatistica);
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

        if (usaButtonDownload) {
            buttonDownload = new Button("Download", new Icon(VaadinIcon.DOWNLOAD));
            buttonDownload.getElement().setAttribute("theme", "primary");
            buttonDownload.addClickListener(e -> download(System.currentTimeMillis()));
            topPlaceholder.add(buttonDownload);
        }// end of if cycle

        if (usaButtonUpdate) {
            buttonUpdate = new Button("Update", new Icon(VaadinIcon.LIST));
            buttonUpdate.addClassName("view-toolbar__button");
            buttonUpdate.addClickListener(e -> update(System.currentTimeMillis()));
            topPlaceholder.add(buttonUpdate);
        }// end of if cycle

        if (usaButtonElabora) {
            buttonElabora = new Button("Elabora", new Icon(VaadinIcon.LIST));
            buttonElabora.addClassName("view-toolbar__button");
            buttonElabora.addClickListener(e -> elabora(System.currentTimeMillis()));
            topPlaceholder.add(buttonElabora);
        }// end of if cycle

        if (usaButtonUpload) {
            buttonUpload = new Button("Upload", new Icon(VaadinIcon.UPLOAD));
            buttonUpload.getElement().setAttribute("theme", "error");
            buttonUpload.addClickListener(e -> upload(System.currentTimeMillis()));
            topPlaceholder.add(buttonUpload);
        }// end of if cycle

        if (usaButtonModulo) {
            buttonModulo = new Button("Modulo", new Icon(VaadinIcon.LIST));
            buttonModulo.addClassName("view-toolbar__button");
            buttonModulo.addClickListener(e -> showWikiPagina(titoloModulo));
            secondTopPlaceholder.add(buttonModulo);
        }// end of if cycle

        if (usaButtonShowStatisticheA) {
            buttonShowStatisticheA = new Button("Statistiche", new Icon(VaadinIcon.TABLE));
            buttonShowStatisticheA.addClassName("view-toolbar__button");
            buttonShowStatisticheA.addClickListener(e -> showWikiPagina(titoloPaginaStatistiche));
            secondTopPlaceholder.add(buttonShowStatisticheA);
        }// end of if cycle

        if (usaButtonShowStatisticheB) {
            buttonShowStatisticheB = new Button("Statistiche 2", new Icon(VaadinIcon.TABLE));
            buttonShowStatisticheB.addClassName("view-toolbar__button");
            buttonShowStatisticheB.addClickListener(e -> showWikiPagina(titoloPaginaStatistiche2));
            secondTopPlaceholder.add(buttonShowStatisticheB);
        }// end of if cycle

        if (usaButtonUploadStatistiche) {
            buttonUploadStatistiche = new Button("Upload statistiche", new Icon(VaadinIcon.UPLOAD));
            buttonUploadStatistiche.getElement().setAttribute("theme", "error");
            buttonUploadStatistiche.addClassName("view-toolbar__button");
            buttonUploadStatistiche.addClickListener(e -> uploadStatistiche(System.currentTimeMillis()));
            secondTopPlaceholder.add(buttonUploadStatistiche);
        }// end of if cycle

        sincroBottoniMenu(false);
    }// end of method


    /**
     * Download standard. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void download(long inizio) {
        wikiService.download();
        setLastDownload(inizio);
        updateFiltri();
        updateGrid();
    }// end of method


    /**
     * Update standard. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void update(long inizio) {
    }// end of method


    /**
     * Elabora standard. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void elabora(long inizio) {
        ((NomeCognomeService) wikiService).elabora();
    }// end of method


    /**
     * Upload standard. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void upload(long inizio) {
        setLastUpload(inizio);
        super.updateGrid();
    }// end of method


    /**
     * Upload standard delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    protected void uploadStatistiche(long inizio) {
        super.updateGrid();
    }// end of method


    protected void showWikiPagina(String titoloPagina) {
        String link = "\"" + PATH_WIKI + titoloPagina + "\"";
        UI.getCurrent().getPage().executeJavaScript("window.open(" + link + ");");
    }// end of method


    protected void sincroBottoniMenu(boolean enabled) {
        if (buttonUploadOneNato != null) {
            buttonUploadOneNato.setEnabled(enabled);
        }// end of if cycle
        if (buttonUploadOneMorto != null) {
            buttonUploadOneMorto.setEnabled(enabled);
        }// end of if cycle
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultimo download effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo download effettuato <br>
     */
    protected void setLastDownload(long inizio) {
        pref.saveValue(lastDownload, LocalDateTime.now());
        pref.saveValue(durataLastDownload, eaTempoTypeDownload.get(inizio));
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultimo upload effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo upload effettuato <br>
     */
    protected void setLastUpload(long inizio) {
        pref.saveValue(lastUpload, LocalDateTime.now());
        pref.saveValue(durataLastUpload, eaTempoTypeUpload.get(inizio));
    }// end of method


//    /**
//     * Registra nelle preferenze la data dell'ultimo upload statistiche effettuato <br>
//     * Registra nelle preferenze la durata dell'ultimo upload statistiche effettuato <br>
//     */
//    protected void setLastUploadStatistiche(long inizio) {
//        pref.saveValue(lastUploadStatistiche, LocalDateTime.now());
//        pref.saveValue(durataLastUploadStatistiche, eaTempoTypeStatistiche.get(inizio));
//    }// end of method


    /**
     * Eventuale caption sopra la grid
     */
    protected void creaInfo(String type, boolean operazionePrevista, ATask task, String flagDaemon, String flagLastDownload, String flagDurataDownload) {
        String tag = " Scheduled ";
        boolean scheduledPrevisto = task != null;
        boolean scheduledAttivo = false;
        String message = text.primaMaiuscola(type) + ".";
        LocalDateTime lastDownload = pref.getDateTime(flagLastDownload);

        if (scheduledPrevisto && text.isValid(flagDaemon)) {
            scheduledAttivo = pref.isBool(flagDaemon);
        }// end of if cycle

        if (operazionePrevista) {
            if (scheduledPrevisto) {
                if (scheduledAttivo) {
                    message += tag;
                    message += task.getNota();
                } else {
                    message += tag + "non attivo.";
                    message += " Attivabile " + task.getNota() ;
                }// end of if/else cycle
            } else {
                message += tag + "non previsto.";
            }// end of if/else cycle

            if (date.isValid(lastDownload)) {
                message += " Ultimo " + type + (scheduledAttivo ? "" : " manuale") + " il " + date.getTime(lastDownload);
                message += getDurata(eaTempoTypeDownload, flagDurataDownload);
            } else {
                message += " Non ancora effettuato.";
            }// end of if/else cycle
        } else {
            message += " Non previsto.";
        }// end of if/else cycle

        alertPlacehorder.add(getLabelUser(message));
    }// end of method


    /**
     * Durata suddivisa in secondi/minuti/ore
     */
    protected String getDurata(EATempo eaTempoType, String flagDurata) {
        String message = VUOTA;
        int durata = 0;

        if (eaTempoType == EATempo.nessuno || text.isEmpty(flagDurata)) {
            return VUOTA;
        }// end of if cycle

        durata = pref.getInt(flagDurata);
        message = ", in ";

        switch (eaTempoType) {
            case nessuno:
            case millisecondi:
                message += date.toText(durata);
                break;
            case secondi:
                message += durata > 1 ? date.toTextSecondi(durata) : INFERIORE_SECONDO;
                break;
            case minuti:
                message += durata > 1 ? date.toTextMinuti(durata) : INFERIORE_MINUTO;
                break;
            case ore:
                message += date.toText(durata);
                break;
            case giorni:
                message += date.toText(durata);
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        return message;
    }// end of method

}// end of class
