package it.algos.vaadwiki.modules.doppinomi;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.presenter.IAPresenter;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.dialog.IADialog;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatViewList;
import it.algos.wiki.web.AQueryVoce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static it.algos.vaadwiki.application.WikiCost.LAST_DOWNLOAD_DOPPI_NOMI;
import static it.algos.vaadwiki.application.WikiCost.TAG_DOP;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 6-ott-2018 7.29.00 <br>
 * <br>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * <p>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le istanze @Autowired usate da questa classe vengono iniettate automaticamente da SpringBoot se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired di questa classe, oppure <br>
 * 2) la property è di una classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON), oppure <br>
 * 3) vengono usate in un un metodo @PostConstruct di questa classe, perché SpringBoot le inietta DOPO init() <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @UIScope (obbligatorio) <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@UIScope
@Route(value = TAG_DOP, layout = MainLayout.class)
@Qualifier(TAG_DOP)
@Slf4j
@AIScript(sovrascrivibile = false)
public class DoppinomiViewList extends AttNazProfCatViewList {

    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final VaadinIcon VIEW_ICON = VaadinIcon.ASTERISK;

    private static String PAGINA_WIKI = "Utente:Biobot/NomiDoppi";


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param presenter per gestire la business logic del package
     * @param dialog    per visualizzare i fields
     */
    @Autowired
    public DoppinomiViewList(@Qualifier(TAG_DOP) IAPresenter presenter, @Qualifier(TAG_DOP) IADialog dialog) {
        super(presenter, dialog);
        ((DoppinomiViewDialog) dialog).fixFunzioni(this::save, this::delete);
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
        super.usaSearchTextDialog = false;
        super.usaAllButton = false;
        super.usaSearchBottoneNew = false;
        super.isEntityModificabile = false;

        super.usaBottoneUpload = false;
        super.usaBottoneCategoria = false;
        super.usaBottoneDeleteMongo = false;
        super.usaBottoneStatistiche = false;

        super.titoloModulo = service.titoloModuloDoppiNomi;
        super.codeLastDownload = LAST_DOWNLOAD_DOPPI_NOMI;
        super.usaPagination = false;
    }// end of method


//    /**
//     * Placeholder (eventuale, presente di default) SOPRA la Grid
//     * - con o senza campo edit search, regolato da preferenza o da parametro
//     * - con o senza bottone New, regolato da preferenza o da parametro
//     * - con eventuali altri bottoni specifici
//     * Può essere sovrascritto, per aggiungere informazioni
//     * Invocare PRIMA il metodo della superclasse
//     */
//    @Override
//    protected void creaTopLayout() {
//        super.creaTopLayout();
//
//        Button showModuloButton = new Button("Download wiki", new Icon(VaadinIcon.DOWNLOAD));
//        showModuloButton.addClassName("view-toolbar__button");
//        showModuloButton.addClickListener(e -> download());
//        topPlaceholder.add(showModuloButton);
//
//        Button uploadStatisticheButton = new Button("Upload wiki", new Icon(VaadinIcon.UPLOAD));
//        uploadStatisticheButton.addClassName("view-toolbar__button");
//        uploadStatisticheButton.addClickListener(e -> upload());
//        topPlaceholder.add(uploadStatisticheButton);
//    }// end of method


    /**
     * Legge server wiki una lista di valori da inserire nel mongoDB (cancellando i precedenti) <br>
     */
    protected void downloadOld() {
        String[] righe;
        String tag = "\\*";
        String testo = ((AQueryVoce) appContext.getBean("AQueryVoce", PAGINA_WIKI)).urlRequest();

        righe = testo.split(tag);
        if (righe != null && righe.length > 1) {
            service.deleteAll();
            for (int k = 1; k < righe.length; k++) {
                ((DoppinomiService) service).findOrCrea(righe[k].trim());
                ;
            }// end of for cycle
        }// end of if cycle

    }// end of method


//    /**
//     * Scrive sul server wiki una lista dei valori del mongoDB <br>
//     */
//    protected void upload() {
//        String testo = "";
//        List<String> lista = ((DoppinomiService) service).findAllCode();
//
//        testo += "Pagina di servizio con la lista dei '''nomi doppi''' da escludere nella creazione delle pagine '''Persone di nome...'''";
//        testo += A_CAPO;
//        testo += A_CAPO;
//
//        for (String stringa : lista) {
//            testo += "*";
//            testo += stringa;
//            testo += A_CAPO;
//        }// end of for cycle
//        testo = text.levaCoda(testo, A_CAPO);
//
//        appContext.getBean(AQueryWrite.class, PAGINA_WIKI, testo);
//    }// end of method

}// end of class