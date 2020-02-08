package it.algos.vaadwiki.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadwiki.application.WikiCost.TAG_PRO;
import static it.algos.vaadwiki.application.WikiCost.USA_DAEMON_PROFESSIONE_DOWNLOAD;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sat, 30-Mar-2019
 * Time: 17:45
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_PRO)
@Slf4j
public class TaskProfessione extends WikiTask {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ProfessioneService service;


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     * <p>
     * Esiste il flag di preferenze per usare o meno questa Task <br>
     * Se la si usa, controlla il flag generale di debug per 'intensificare' l'import <br>
     */
    @PostConstruct
    protected void inizia() {
        super.eaSchedule = EASchedule.giornoTerzoMinuto;
        super.usaDaemon = pref.isBool(USA_DAEMON_PROFESSIONE_DOWNLOAD);
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        if (pref.isBool(USA_DAEMON_PROFESSIONE_DOWNLOAD)) {
            service.download();
        }// end of if cycle
    }// end of method

}// end of class
