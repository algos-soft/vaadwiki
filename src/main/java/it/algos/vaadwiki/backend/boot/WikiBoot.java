package it.algos.vaadwiki.backend.boot;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.boot.*;
import static it.algos.vaadwiki.backend.application.WikiCost.*;
import it.algos.vaadwiki.backend.data.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.genere.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.packages.prenome.*;
import it.algos.vaadwiki.backend.packages.professione.*;
import it.algos.vaadwiki.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.time.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 14-apr-2021
 * Time: 8:22
 * <p>
 * Running logic after the Spring context has been initialized <br>
 * Executed on container startup, before any browse command <br>
 * Any class that use this @EventListener annotation, will be executed
 * before the application is up and its onContextRefreshEvent method will be called
 * The method onApplicationEvent() will be executed nella sottoclasse before
 * the application is up and <br>
 * <p>
 * Sottoclasse concreta dell' applicazione specifica che: <br>
 * 1) regola alcuni parametri standard del database MongoDB <br>
 * 2) regola le variabili generali dell'applicazione <br>
 * 3) crea i dati di alcune collections sul DB mongo <br>
 * 4) crea le preferenze standard e specifiche dell'applicazione <br>
 * 5) aggiunge le @Route (view) standard e specifiche <br>
 * 6) lancia gli schedulers in background <br>
 * 7) costruisce una versione demo <br>
 * 8) controlla l' esistenza di utenti abilitati all' accesso <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file da wizard/scripts di algos <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
public class WikiBoot extends FlowBoot {


    /**
     * Regola alcuni parametri standard del database MongoDB <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixDBMongo() {
        super.fixDBMongo();
    }


    /**
     * Regola le variabili generali dell' applicazione con il loro valore iniziale di default <br>
     * Le variabili (static) sono uniche per tutta l' applicazione <br>
     * Il loro valore può essere modificato SOLO in questa classe o in una sua sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixVariabili() {
        super.fixVariabili();

        FlowVar.usaDebug = false;
        FlowVar.usaCompany = false;
        FlowVar.dataClazz = WikiData.class;
        FlowVar.usaSecurity = false;
        FlowVar.projectNameDirectoryIdea = "vaadwiki";
        FlowVar.projectNameModulo = "vaadwiki";
        FlowVar.projectNameUpper = "Wiki";
        FlowVar.projectDescrizione = "Gestione di BioBot";
        FlowVar.projectVersion = environment.getProperty("algos.wiki.version") != null ? Double.parseDouble(environment.getProperty("algos.wiki.version")) : 0.0;
        FlowVar.preferenzeSpecificheList = Arrays.asList(AEWikiPreferenza.values());
        FlowVar.versionDate = LocalDate.of(2021, 4, 14);
        FlowVar.projectNote = "Sviluppo di una applicazione in Vaadin14";
        FlowVar.usaCronoPackages = false;
        FlowVar.usaGeografiaPackages = false;
    }


    /**
     * Primo ingresso nel programma nella classe concreta, tramite il <br>
     * metodo FlowBoot.onContextRefreshEvent() della superclasse astratta <br>
     * Crea i dati di alcune collections sul DB mongo <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * <p>
     * Invoca il metodo fixData() di FlowData oppure della sottoclasse <br>
     *
     * @since java 8
     */
    protected void fixData() {
        if (FlowVar.dataClazz != null && FlowVar.dataClazz.equals(WikiData.class)) {
            dataInstance.resetData();
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
    @Override
    protected void fixMenuRoutes() {
        super.fixMenuRoutes();
        FlowVar.menuRouteList.add(Prenome.class);
        FlowVar.menuRouteList.add(Genere.class);
        FlowVar.menuRouteList.add(Professione.class);
        FlowVar.menuRouteList.add(Attivita.class);
        FlowVar.menuRouteList.add(Nazionalita.class);
        FlowVar.menuRouteList.add(Bio.class);
    }

    /**
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixSchedules() {
    }

    /**
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixDemo() {
    }

    /**
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixUsers() {
        appContext.getBean(QueryLogin.class).urlRequest();
    }

    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_WIKI_DATA)
    public void setDataInstance(WikiData dataInstance) {
        this.dataInstance = dataInstance;
    }

}