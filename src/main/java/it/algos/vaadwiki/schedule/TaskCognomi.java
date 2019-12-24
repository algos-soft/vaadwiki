package it.algos.vaadwiki.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadflow.schedule.ATask;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadwiki.application.WikiCost.TASK_COG;
import static it.algos.vaadwiki.application.WikiCost.USA_DAEMON_COGNOMI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 23-gen-2019
 * Time: 07:11
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_COG)
@Slf4j
public class TaskCognomi extends ATask {


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
    @PostConstruct
    protected void inizia() {
        super.eaSchedule = EASchedule.oreOttoMercoledi;
        super.usaDaemon = pref.isBool(USA_DAEMON_COGNOMI);
    }// end of method


    /**
     * Ricontrollando la preferenza ad ogni esecuzione della 'task', si può attivare/disattivare senza riavviare il programma <br>
     */
    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        if (pref.isBool(USA_DAEMON_COGNOMI)) {
            uploadService.uploadAllCognomi();
        }// end of if cycle
    }// end of method

}// end of class
