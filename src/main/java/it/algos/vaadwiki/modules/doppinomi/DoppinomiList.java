package it.algos.vaadwiki.modules.doppinomi;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatList;
import it.algos.vaadwiki.modules.wiki.WikiList;
import it.algos.wiki.web.AQueryVoce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.klaudeta.PaginatedGrid;

import static it.algos.vaadwiki.application.WikiCost.*;

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
@Route(value = TAG_DOP, layout = MainLayout14.class)
@Qualifier(TAG_DOP)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "nomi doppi", menuIcon = VaadinIcon.BOAT, searchProperty = "code", roleTypeVisibility = EARoleType.developer)
public class DoppinomiList extends WikiList {


    private static String PAGINA_WIKI = "Utente:Biobot/NomiDoppi";


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa alla superclasse il service iniettato qui da Vaadin/@Route <br>
     * Passa alla superclasse anche la entityClazz che viene definita qui (specifica di questo modulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    @Autowired
    public DoppinomiList(@Qualifier(TAG_DOP) IAService service) {
        super(service, Doppinomi.class);
    }// end of Vaadin/@Route constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        //--bottoni vaadwiki
        super.usaButtonDownload = true;
        super.usaButtonModulo = true;

        super.titoloModulo = wikiService.titoloModuloDoppiNomi;
        super.isEntityModificabile = false;
        super.usaPagination = false;

        super.lastDownload = LAST_DOWNLOAD_DOPPI_NOMI;
        super.durataLastDownload = DURATA_DOWNLOAD_DOPPI_NOMI;
        super.eaTempoTypeDownload = EATempo.secondi;
    }// end of method

    /**
     * Eventuali messaggi di avviso specifici di questa view ed inseriti in 'alertPlacehorder' <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Normalmente ad uso esclusivo del developer (eventualmente dell'admin) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaAlertLayout() {
        super.creaAlertLayout();

        alertPlacehorder.add(text.getLabelAdmin("Progetto:Antroponimi/Nomi doppi."));
        alertPlacehorder.add(new Label("Sono elencati i nomi doppi (ad esempio 'Maria Teresa'), per i quali il BioBot deve fare una lista di biografati una volta superate le 50 biografie."));
        alertPlacehorder.add(new Label("Si veda anche la [[Categoria:Prenomi composti]]"));
        alertPlacehorder.add(text.getLabelDev("La lista di 'Nomi' prevede SOLO nomi singoli a cui vengono aggiunti questi 'nomi doppi' accettabili."));
        alertPlacehorder.add(text.getLabelDev("Quando si crea la lista di 'Nomi', i nomi doppi vengono scaricati ed aggiunti alla lista dei 'Nomi'"));
    }// end of method

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


}// end of class