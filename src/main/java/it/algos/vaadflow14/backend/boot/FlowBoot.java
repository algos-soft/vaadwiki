package it.algos.vaadflow14.backend.boot;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.data.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.packages.crono.secolo.*;
import it.algos.vaadflow14.backend.packages.geografica.continente.*;
import it.algos.vaadflow14.backend.packages.geografica.provincia.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.packages.security.utente.*;
import it.algos.vaadflow14.backend.packages.utility.versione.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.wizard.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.*;
import org.springframework.core.env.*;

import javax.servlet.*;
import java.time.*;
import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: ven, 01-mag-2020
 * Time: 10:33
 * <p>
 * Running logic after the Spring context has been initialized <br>
 * Executed on container startup, before any browse command <br>
 * Any class that use this @EventListener annotation, will be executed
 * before the application is up and its onContextRefreshEvent method will be called
 * The method onApplicationEvent() will be executed nella sottoclasse before
 * the application is up and <br>
 * <p>
 * Questa classe è astratta e NON ricevere un @EventListener, che viene gestito dalla indispensabile sottoclasse concreta <br>
 * La sottoclasse concreta di ogni applicazione usa le API di ServletContextListener e riceve un @EventListener <br>
 * (una volta sola) che rimanda al metodo onContextRefreshEvent() di questa classe astratta <br>
 * <p>
 * Not annotated with @SpringComponent (sbagliato, SpringBoot crea la sottoclasse concreta) <br>
 * Not annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (sbagliato) <br>
 * <p>
 * Deve essere creata una sottoclasse concreta per l' applicazione specifica che: <br>
 * 1) regola alcuni parametri standard del database MongoDB <br>
 * 2) regola le variabili generali dell'applicazione <br>
 * 3) crea i dati di alcune collections sul DB mongo <br>
 * 4) crea le preferenze standard e specifiche dell'applicazione <br>
 * 5) aggiunge le @Route (view) standard e specifiche <br>
 * 6) lancia gli schedulers in background <br>
 * 7) costruisce una versione demo <br>
 * 8) controlla l' esistenza di utenti abilitati all' accesso <br>
 */
public abstract class FlowBoot implements ServletContextListener {


    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    public Environment environment;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    public AMongoService mongo;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    public ALogService logger;

    /**
     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    public FlowData dataInstance;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public PreferenzaService preferenzaService;

    /**
     * Constructor with @Autowired on setter. Usato quando ci sono sottoclassi. <br>
     * Per evitare di avere nel costruttore tutte le property che devono essere iniettate e per poterle aumentare <br>
     * senza dover modificare i costruttori delle sottoclassi, l'iniezione tramite @Autowired <br>
     * viene delegata ad alcuni metodi setter() che vengono qui invocati con valore (ancora) nullo. <br>
     * Al termine del ciclo init() del costruttore il framework SpringBoot/Vaadin, inietterà la relativa istanza <br>
     */
    public FlowBoot() {
        this.setEnvironment(environment);
        this.setMongo(mongo);
        this.setLogger(logger);
        this.setDataInstance(dataInstance);
    }// end of constructor with @Autowired on setter


    /**
     * Primo ingresso nel programma <br>
     * <p>
     * registrare nella xxxApp, il servlet context non appena è disponibile @todo forse
     * <p>
     * 1) regola alcuni parametri standard del database MongoDB <br>
     * 2) regola le variabili generali dell'applicazione <br>
     * 3) crea le preferenze standard e specifiche dell'applicazione <br>
     * 4) crea i dati di alcune collections sul DB mongo <br>
     * 5) aggiunge al menu le @Route (view) standard e specifiche <br>
     * 6) lancia gli schedulers in background <br>
     * 7) costruisce una versione demo <br>
     * 8) controllare l' esistenza di utenti abilitati all' accesso <br>
     * <p>
     * Metodo privato perché non può essere sovrascritto <br>
     */
    @EventListener(ContextRefreshedEvent.class)
    private void onContextRefreshEvent() {
        logger.startupIni();

        this.fixDBMongo();
        this.fixVariabili();
        this.fixPreferenze();
        this.fixData();
        this.fixMenuRoutes();
        this.fixSchedules();
        this.fixDemo();
        this.fixUsers();
        this.fixVersioni();

        logger.startupEnd();
    }


    /**
     * Regola alcuni parametri standard del database MongoDB <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixDBMongo() {
        mongo.getMaxBlockingSortBytes();
        mongo.fixMaxBytes();
    }


    /**
     * Regola le variabili generali dell' applicazione con il loro valore iniziale di default <br>
     * Le variabili (static) sono uniche per tutta l' applicazione <br>
     * Il loro valore può essere modificato SOLO in questa classe o in una sua sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixVariabili() {

        /**
         * Controlla se l' applicazione gira in 'debug mode' oppure no <br>
         * Di default (per sicurezza) uguale a true <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.usaDebug = true;

        /**
         * Controlla se l' applicazione è multi-company oppure no <br>
         * Di default (per sicurezza) uguale a true <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         * Se usaCompany=true anche usaSecurity deve essere true <br>
         */
        FlowVar.usaCompany = true;

        /**
         * Controlla se l' applicazione usa il login oppure no <br>
         * Se si usa il login, occorre la classe SecurityConfiguration <br>
         * Se non si usa il login, occorre disabilitare l'Annotation @EnableWebSecurity di SecurityConfiguration <br>
         * Di default (per sicurezza) uguale a true <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         * Se usaCompany=true anche usaSecurity deve essere true <br>
         * Può essere true anche se usaCompany=false <br>
         */
        FlowVar.usaSecurity = true;

        /**
         * Nome identificativo dell' applicazione <br>
         * Usato (eventualmente) nella barra di menu in testa pagina <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.projectName = VUOTA;

        /**
         * Descrizione completa dell' applicazione <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.projectDescrizione = VUOTA;

        /**
         * Versione dell' applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
         */
        FlowVar.projectVersion = Double.parseDouble(Objects.requireNonNull(environment.getProperty("algos.vaadflow.version")));

        /**
         * Data della versione dell' applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
         */
        int anno = Integer.parseInt(environment.getProperty("algos.vaadflow.version.date.anno"));
        int mese = Integer.parseInt(environment.getProperty("algos.vaadflow.version.date.mese"));
        int giorno = Integer.parseInt(environment.getProperty("algos.vaadflow.version.date.giorno"));
        FlowVar.versionDate = LocalDate.of(anno, mese, giorno);

        /**
         * Eventuali informazioni aggiuntive da utilizzare nelle informazioni <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.projectNote = VUOTA;

        /**
         * Flag per usare le icone VaadinIcon <br>
         * In alternativa usa le icone 'lumo' <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.usaVaadinIcon = true;

        /**
         * Classe da usare per lo startup del programma <br>
         * Di default FlowData oppure possibile sottoclasse del progetto <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.dataClazz = FlowData.class;

        /**
         * Classe da usare per le Company (o sottoclassi) <br>
         * Di default 'company' oppure eventuale sottoclasse specializzata per Company particolari <br>
         * Eventuale casting a carico del chiamante <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.companyClazz = Company.class;

        /**
         * Path per recuperare dalle risorse un' immagine da inserire nella barra di menu di MainLayout. <br>
         * Ogni applicazione può modificarla <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.pathLogo = "img/medal.ico";

        /**
         * Lista dei moduli di menu da inserire nel Drawer del MainLayout per le gestione delle @Routes. <br>
         * Regolata dall'applicazione durante l'esecuzione del 'container startup' (non-UI logic) <br>
         * Usata da ALayoutService per conto di MainLayout allo start della UI-logic <br>
         */
        FlowVar.menuRouteList = new ArrayList<>();

        /**
         * Lista delle enum di preferenze specifiche. <br>
         * Quelle generali dell'applicazione sono in AEPreferenza.values() <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.preferenzeSpecificheList = null;

        /**
         * Lista delle enum di bottoni specifici. <br>
         * Quelli generali dell'applicazione sono in AEButton.values() <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.bottoniSpecificiList = null;

        /**
         * Mostra i 2 (incrementabili) packages di admin (preferenza, versione) <br>
         * Anche se non visibili nel menu, sono sempre disponibili col nome della @Route <br>
         * Di default (per sicurezza) uguale a false <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.usaAdminPackages = false;

        /**
         * Mostra i 3 (incrementabili) packages di gestione (address, via, persona) <br>
         * Anche se non visibili nel menu, sono sempre disponibili col nome della @Route <br>
         * Di default (per sicurezza) uguale a false <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.usaGestionePackages = false;

        /**
         * Mostra i 4 (fissi) packages geografici (stato, regione, provincia, comune) <br>
         * Anche se non visibili nel menu, sono sempre disponibili col nome della @Route <br>
         * Di default (per sicurezza) uguale a false <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.usaGeografiaPackages = false;

        /**
         * Mostra i 4 (fissi) packages cronologici (secolo, anno, mese, giorno) <br>
         * Anche se non visibili nel menu, sono sempre disponibili col nome della @Route <br>
         * Di default (per sicurezza) uguale a false <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
         */
        FlowVar.usaCronoPackages = false;
    }




    /**
     * Crea le preferenze standard e specifiche dell'applicazione <br>
     * Se non esistono, le crea <br>
     * Se esistono, NON modifica i valori esistenti <br>
     * Per un reset ai valori di default, c'è il metodo reset() chiamato da preferenzaService <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        AIResult result = preferenzaService.resetEmptyOnly();
        result.setErrorMessage("Le preferenze generali e specifiche esistono già e non c'è bisogno di modificarle");
        logger.log(AETypeLog.preferenze, result);
    }


    /**
     * Primo ingresso nel programma nella classe concreta, tramite il <br>
     * metodo FlowBoot.onContextRefreshEvent() della superclasse astratta <br>
     * Crea i dati di alcune collections sul DB mongo <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     * <p>
     * Invoca il metodo fixData() di FlowData oppure della sottoclasse <br>
     */
    protected void fixData() {
        if (FlowVar.dataClazz != null && FlowVar.dataClazz.equals(FlowData.class)) {
            dataInstance.fixData();
        }
    }


    /**
     * Aggiunge al menu le @Route (view) standard e specifiche <br>
     * <p>
     * Questa classe viene invocata PRIMA della chiamata del browser <br>
     * Se NON usa la security, le @Route vengono create solo qui <br>
     * Se USA la security, le @Route vengono sovrascritte all' apertura del browser nella classe AUserDetailsService <br>
     * <p>
     * Nella sottoclasse concreta che invoca questo metodo, aggiunge le @Route (view) specifiche dell' applicazione <br>
     * Le @Route vengono aggiunte ad una Lista statica mantenuta in FlowVar <br>
     * Verranno lette da MainLayout la prima volta che il browser 'chiama' una view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixMenuRoutes() {

        if (FlowVar.usaCompany) {
            FlowVar.menuRouteList.add(Utente.class);
            FlowVar.menuRouteList.add(Company.class);
        }

        if (FlowVar.usaAdminPackages) {
            FlowVar.menuRouteList.add(Preferenza.class);
            FlowVar.menuRouteList.add(Wizard.class);
            FlowVar.menuRouteList.add(Versione.class);
        }

        if (FlowVar.usaGestionePackages) {
            FlowVar.menuRouteList.add(Via.class);
        }

        if (AEPreferenza.usaMenuGeo.is()) {
            FlowVar.menuRouteList.add(Continente.class);
            FlowVar.menuRouteList.add(Stato.class);
            FlowVar.menuRouteList.add(Regione.class);
            FlowVar.menuRouteList.add(Provincia.class);
        }
        if (AEPreferenza.usaMenuCrono.is()) {
            FlowVar.menuRouteList.add(Secolo.class);
            FlowVar.menuRouteList.add(Anno.class);
            FlowVar.menuRouteList.add(Mese.class);
            FlowVar.menuRouteList.add(Giorno.class);
        }

    }

    /**
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixSchedules() {
    }

    /**
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixDemo() {
    }

    /**
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixUsers() {
    }

    /**
     * Inizializzazione delle versioni standard di vaadinFlow <br>
     * Inizializzazione delle versioni del programma specifico <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixVersioni() {
        String codeVersione = VUOTA;
        String descVersione = VUOTA;
        Versione entityBean;

        //--inizio
        codeVersione = "Setup";
        descVersione = "Creazione ed installazione iniziale dell'applicazione";
        entityBean = (Versione) mongo.findByKey(Versione.class, codeVersione);
        if (entityBean == null) {
            entityBean = new Versione(codeVersione, LocalDate.now(), descVersione);
            entityBean.id = codeVersione;
            mongo.save(entityBean);
        }

    }


    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public void setMongo(final AMongoService mongo) {
        this.mongo = mongo;
    }


    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public void setLogger(final ALogService logger) {
        this.logger = logger;
    }

    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_FLOW_DATA)
    public void setDataInstance(final FlowData dataInstance) {
        this.dataInstance = dataInstance;
    }

}
