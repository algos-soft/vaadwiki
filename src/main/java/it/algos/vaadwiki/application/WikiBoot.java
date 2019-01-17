package it.algos.vaadwiki.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.boot.ABoot;
import it.algos.vaadflow.modules.role.EARole;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadwiki.modules.attivita.AttivitaViewList;
import it.algos.vaadwiki.modules.bio.BioViewList;
import it.algos.vaadwiki.modules.categoria.CategoriaViewList;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaViewList;
import it.algos.vaadwiki.modules.professione.ProfessioneViewList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.servlet.ServletContextEvent;
import java.time.LocalDate;
import java.util.ArrayList;

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
        PROJECT_VERSION = "1.5";
        PROJECT_DATE = LocalDate.of(2019, 1, 17);
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
        FlowCost.MENU_CLAZZ_LIST.add(AttivitaViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(NazionalitaViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(ProfessioneViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(CategoriaViewList.class);
        FlowCost.MENU_CLAZZ_LIST.add(BioViewList.class);
    }// end of method


}// end of boot class