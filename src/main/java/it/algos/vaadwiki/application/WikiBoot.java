package it.algos.vaadwiki.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.boot.ABoot;
import it.algos.vaadwiki.modules.attivita.AttivitaList;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AMailService;
import it.algos.vaadwiki.didascalia.ViewDidascalie;
import it.algos.vaadwiki.modules.bio.BioList;
import it.algos.vaadwiki.modules.cognome.CognomeList;
import it.algos.vaadwiki.modules.genere.GenereList;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaList;
import it.algos.vaadwiki.modules.nome.NomeList;
import it.algos.vaadwiki.modules.doppinomi.DoppinomiList;
import it.algos.vaadwiki.modules.professione.ProfessioneList;
import it.algos.vaadwiki.modules.wiki.WikiAnnoList;
import it.algos.vaadwiki.modules.wiki.WikiGiornoList;
import it.algos.vaadwiki.views.UtilityView;
import it.algos.wiki.web.AQueryLogin;
import it.algos.wiki.web.WLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContextEvent;
import java.net.InetAddress;
import java.time.LocalDate;

import static it.algos.vaadwiki.application.WikiCost.SEND_MAIL_RESTART;

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
@Slf4j
public class WikiBoot extends ABoot {

    public final static String DEMO_COMPANY_CODE = "demo";

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ADateService date;

    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    protected WLogin wLogin;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mailService;

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
     * Regola alcune preferenze iniziali
     * Se non esistono, le crea
     * Se esistono, sostituisce i valori esistenti con quelli indicati qui
     */
    protected void regolaPreferenze() {
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
        /**
         * Controlla se l'applicazione usa il login oppure no <br>
         * Se si usa il login, occorre la classe SecurityConfiguration <br>
         * Se non si usa il login, occorre disabilitare l'Annotation @EnableWebSecurity di SecurityConfiguration <br>
         * Di defaul (per sicurezza) uguale a true <br>
         */
        FlowVar.usaSecurity = false;

        /**
         * Controlla se l'applicazione è multi-company oppure no <br>
         * Di defaul (per sicurezza) uguale a true <br>
         * Deve essere regolato in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.usaCompany = false;


        /**
         * Nome identificativo dell'applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.projectName = "vaadwiki";

        /**
         * Versione dell'applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.projectVersion = 5.7;

        /**
         * Data della versione dell'applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.versionDate = LocalDate.of(2019, 10, 8);

        if (wLogin != null) {
            FlowVar.projectNote = "- loggato come " + wLogin.getLgusername();
        }// end of if cycl

        mailRestart();
    }// end of method


    /**
     * Mail di controllo
     */
    protected void mailRestart() {
        InetAddress inetAddress = null;
        String server = "";
        String tag = "Mini-di-Guido";

        if (pref.isBool(SEND_MAIL_RESTART)) {
            try { // prova ad eseguire il codice
                inetAddress = InetAddress.getLocalHost();
                if (inetAddress != null) {
                    server = inetAddress.getHostName();
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch

            if (text.isValid(server) && server.equals(tag)) {
                String message = date.getDataOraComplete();
                mailService.send("Restart", "Applicazione vaadwiki che gira sul server " + server + " riavviata " + message);
            }// end of if cycle
        }// end of if cycle
    }// end of method




    /**
     * Aggiunge le @Route (view) specifiche di questa applicazione
     * Le @Route vengono aggiunte ad una Lista statica mantenuta in FlowCost
     * Vengono aggiunte dopo quelle standard
     * Verranno lette da MainLayout la prima volta che il browser 'chiama' una view
     */
    protected void addRouteSpecifiche() {
        FlowVar.menuClazzList.add(ViewDidascalie.class);
        FlowVar.menuClazzList.add(UtilityView.class);
        FlowVar.menuClazzList.add(AttivitaList.class);
        FlowVar.menuClazzList.add(NazionalitaList.class);
        FlowVar.menuClazzList.add(ProfessioneList.class);
        FlowVar.menuClazzList.add(GenereList.class);
        FlowVar.menuClazzList.add(BioList.class);
        FlowVar.menuClazzList.add(WikiGiornoList.class);
        FlowVar.menuClazzList.add(WikiAnnoList.class);
        FlowVar.menuClazzList.add(NomeList.class);
        FlowVar.menuClazzList.add(CognomeList.class);
        FlowVar.menuClazzList.add(DoppinomiList.class);
		FlowVar.menuClazzList.add(AttivitaList.class);
	}// end of method


}// end of boot class