package it.algos.vaadwiki.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadwiki.download.CicloUpdate;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.wiki.DownloadResult;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 23-gen-2019
 * Time: 15:25
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_DOW)
@Slf4j
public class TaskUpdate extends WikiTask {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected CicloUpdate cicloUpdate;


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     * <p>
     * Esiste il flag 'usaDaemonCroci' per usare o meno questa Task <br>
     * Se la si usa, controlla il flag generale di debug per 'intensificare' l'import <br>
     */
//    @PostConstruct
    protected void inizia() {
        super.eaSchedule = EASchedule.giornoDecimoMinuto;
        super.usaDaemon = pref.isBool(USA_DAEMON_BIO);
    }// end of method


    /**
     * Ricontrollando la preferenza ad ogni esecuzione della 'task', si può attivare/disattivare senza riavviare il programma <br>
     */
    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        if (pref.isBool(USA_DAEMON_BIO)) {
            cicloUpdate.esegue();
            statisticheService.updateBiografie();
        }// end of if cycle
    }// end of method

}// end of class
