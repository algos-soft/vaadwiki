package it.algos.vaadwiki.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.boot.ABoot;
import it.algos.vaadwiki.modules.genere.GenereViewList;
import it.algos.vaadwiki.modules.cognome.CognomeViewList;
import it.algos.vaadwiki.didascalia.ViewDidascalie;
import it.algos.vaadwiki.modules.nome.NomeViewList;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadwiki.modules.attivita.AttivitaViewList;
import it.algos.vaadwiki.modules.bio.BioViewList;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaViewList;
import it.algos.vaadwiki.modules.professione.ProfessioneViewList;
import it.algos.vaadwiki.modules.wiki.WikiAnnoViewList;
import it.algos.vaadwiki.modules.wiki.WikiGiornoViewList;
import it.algos.vaadwiki.views.UtilityView;
import it.algos.wiki.web.AQueryLogin;
import it.algos.wiki.web.WLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContextEvent;
import java.time.LocalDate;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadwiki
 * Created by Algos flowbase
 * User: gac
 * Date: ven, 8-mag-2018
 * <p>
 * Estende la classe ABoot per le regolazioni iniziali di questa applicazione <br>
 * Running logic after the Spring context has been initialized
 * The method onApplicationEvent() will be executed before the application is up and <br>
 * Aggiunge tutte le @Route (views) standard e specifiche di questa applicazione <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope (obbligatorio) <br>
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script di algos <br>
 * <p>
 * Sovrascrivibile è normalmente 'true' per permettere l'inserimento di
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovrascrivibile = false)
public class WikiBoot extends ABoot {

    public final static String DEMO_COMPANY_CODE = "demo";


    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    protected WLogin wLogin;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private RoleService roleService;

    /**
     * Iniettata dal costruttore <br>
     */
    private WikiVers vaadwikiVers;


    /**
     * Costruttore @Autowired <br>
     *
     * @param wamVersBoot Log delle versioni, modifiche e patch installat
     */
    @Autowired
    public WikiBoot(WikiVers vaadwikiVers) {
        super();
        this.vaadwikiVers = vaadwikiVers;
    }// end of Spring constructor


    /**
     * Executed on container startup <br>
     * Setup non-UI logic here <br>
     * Viene sovrascritto in questa sottoclasse concreta che invoca il metodo super.inizia() <br>
     * Nella superclasse vengono effettuate delle regolazioni standard; <br>
     * questa sottoclasse concreta può singolarmente modificarle <br>
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.inizia();
//        WikiCost.WIKI_LOGIN = new WikiLogin("Gacbot@Gacbot", "tftgv0vhl16c0qnmfdqide3jqdp1i5m7");
    }// end of method


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     * <p>
     * Login inziale del bot
     */
    @PostConstruct
    protected void inizia() {
        appContext.getBean(AQueryLogin.class);
    }// end of method


    /**
     * Inizializzazione dei dati di alcune collections specifiche sul DB Mongo
     */
    protected void iniziaDataProgettoSpecifico() {
//        this.secoloService.loadData();
//        this.meseService.loadData();
//        this.annoService.loadData();
//        this.giornoService.loadData();


        //--patch di accesso
        utenteService.creaIfNotExist(null, "gac", "fulvia", roleService.getRoles(EARole.developer), "gac@algos.it");
        utenteService.creaIfNotExist(null, "biobot", "", roleService.getRoles(EARole.developer), "gac@algos.it");
    }// end of method


    /**
     * Inizializzazione delle versioni del programma specifico
     */
    protected void iniziaVersioni() {
        vaadwikiVers.inizia();
    }// end of method


    /**
     * Regola alcune informazioni dell'applicazione
     */
    protected void regolaInfo() {
        PROJECT_NAME = "vaadwiki";
        PROJECT_VERSION = "3.0";
        PROJECT_DATE = LocalDate.of(2019, 5, 29);

        if (wLogin != null) {
            PROJECT_NOTE = "- loggato come " + wLogin.getLgusername();
        }// end of if cycle
    }// end of method


    /**
     * Regola alcune preferenze iniziali
     * Se non esistono, le crea
     * Se esistono, sostituisce i valori esistenti con quelli indicati qui
     */
    protected void regolaPreferenze() {
//        pref.setBool(FlowCost.USA_COMPANY, true);
    }// end of method


    /**
     * Aggiunge le @Route (view) specifiche di questa applicazione
     * Le @Route vengono aggiunte ad una Lista statica mantenuta in FlowCost
     * Vengono aggiunte dopo quelle standard
     * Verranno lette da MainLayout la prima volta che il browser 'chiama' una view
     */
    protected void addRouteSpecifiche() {
        FlowCost.MENU_CLAZZ_LIST.add(ViewDidascalie.class);
        FlowCost.MENU_CLAZZ_LIST.add(UtilityView.class);
        FlowCost.MENU_CLAZZ_LIST.add(AttivitaViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(NazionalitaViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(ProfessioneViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(GenereViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(BioViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(WikiGiornoViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(WikiAnnoViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(NomeViewList.class);
		FlowCost.MENU_CLAZZ_LIST.add(CognomeViewList.class);
	}// end of method


}// end of boot class