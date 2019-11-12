package it.algos.vaadwiki.modules.professione;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.annotation.AIView;
import it.algos.vaadflow.modules.role.EARoleType;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.MainLayout;
import it.algos.vaadflow.ui.MainLayout14;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatList;
import it.algos.vaadwiki.schedule.TaskProfessione;
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
@Route(value = TAG_PRO, layout = MainLayout14.class)
@Qualifier(TAG_PRO)
@Slf4j
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, menuName = "professione", menuIcon = VaadinIcon.BOAT, searchProperty = "singolare", roleTypeVisibility = EARoleType.developer)
public class ProfessioneList extends AttNazProfCatList {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskProfessione taskProfessione;



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
    public ProfessioneList(@Qualifier(TAG_PRO) IAService service) {
        super(service, Professione.class);
    }// end of Vaadin/@Route constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse
     * Può essere sovrascritto, per aggiungere informazioni
     * Invocare PRIMA il metodo della superclasse
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.titoloModulo = serviceWiki.titoloModuloProfessione;
        super.usaBottoneUpload = false;
        super.usaBottoneStatistiche = false;
        super.usaPagination = true;
        super.task = taskProfessione;
        super.codeFlagDownload = USA_DAEMON_PROFESSIONE;
        super.codeLastDownload = LAST_DOWNLOAD_PROFESSIONE;
        super.durataLastDownload = DURATA_DOWNLOAD_PROFESSIONE;
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
        alertPlacehorder.add(new Label("Modulo:Bio/Link attività. Modulo Lua di supporto a Modulo:Bio"));
        alertPlacehorder.add(new Label("Contiene la tabella di conversione delle attività passate via parametri Attività/Attività2/Attività3, dal nome dell'attività a quello della voce corrispondente, per creare dei piped wikilink."));
        alertPlacehorder.add(new Label("Quando l'attività corrisponde già al titolo della voce di destinazione NON serve inserirla qui."));
        alertPlacehorder.add(new Label("Le attività sono elencate all'interno del modulo con la seguente sintassi:"));
        alertPlacehorder.add(new Label("[\"attivitaforma1\"] = \"voce di riferimento\""));
        alertPlacehorder.add(new Label("[\"attivitaforma2\"] = \"voce di riferimento\""));
        alertPlacehorder.add(new Label("Viene utilizzata principalmente per convertire le attività da femminile (che può essere usato nell'incipit) a maschile (usato nel wikilink) e per orfanizzare i redirect"));
        alertPlacehorder.add(new Label("All'interno della tabella le attività sono in ordine alfabetico."));
        alertPlacehorder.add(new Label("Nella collezione locale mongoDB vengono aggiunte ANCHE le voci delle attività (maschili) che corrispondono alla pagina (non presenti nel Modulo su Wiki)."));
        alertPlacehorder.add(new Label("Indipendentemente da come sono scritte nel modulo wiki, tutte le attività e le pagine sono convertite in minuscolo."));
    }// end of method

    /**
     * Crea effettivamente il Component Grid <br>
     * <p>
     * Può essere Grid oppure PaginatedGrid <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
     * Oppure queste possono essere fatte nella sottoclasse, se non sono standard <br>
     */
    @Override
    protected Grid creaGridComponent() {
        return new PaginatedGrid<Professione>();
    }// end of method


}// end of class