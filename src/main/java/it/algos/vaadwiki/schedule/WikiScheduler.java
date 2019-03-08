package it.algos.vaadwiki.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.schedule.ATask;
import it.sauronsoftware.cron4j.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 23-gen-2019
 * Time: 07:06
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class WikiScheduler extends Scheduler {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PreferenzaService pref;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected TaskDownload download;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected TaskUpdate update;


    @PostConstruct
    public void startBio() throws IllegalStateException {
        if (!isStarted()) {
            super.start();

            this.task(download);
            this.task(update);

        }// fine del blocco if
    }// end of method


    public void task(ATask task) throws IllegalStateException {
        if (task.usaDaemon()) {
            schedule(task.getSchedule(), task);
        }// end of if cycle
    }// end of method


    @Override
    public void stop() throws IllegalStateException {
        if (isStarted()) {
            super.stop();
        }// fine del blocco if
    }// end of method

}// end of class
