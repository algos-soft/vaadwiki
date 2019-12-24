package it.algos.vaadwiki.schedule;

import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import javax.annotation.PostConstruct;

import static it.algos.vaadwiki.application.WikiCost.*;
import static it.algos.vaadwiki.application.WikiCost.USA_DAEMON_ATTIVITA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 23-gen-2019
 * Time: 07:11
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_NAZ)
@Slf4j
public class TaskNazionalita extends ATask {


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
        super.eaSchedule = EASchedule.oreOttoVenerdi;
        super.usaDaemon = pref.isBool(USA_DAEMON_NAZIONALITA);
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        if (pref.isBool(USA_DAEMON_NAZIONALITA)) {
            uploadService.uploadAllNazionalita();
        }// end of if cycle
    }// end of method

}// end of class
