package it.algos.vaadwiki.task;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.sauronsoftware.cron4j.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project vaadbase
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 11:55
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class DaemonBio extends Scheduler {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskAttivita taskAttivita;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskNazionalita taskNazionalita;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskProfessione taslProfessione;



    @PostConstruct
    public void startBio() throws IllegalStateException {
        if (!isStarted()) {
            super.start();

            // Schedule task
            // Ogni giorno
//            this.task(update);

//            this.task(taskAttivita);
//            this.task(taskNazionalita);
//            this.task(taslProfessione);

//            schedule(EASchedule.giornoDecimoMinuto.getTag(), cicloBio);

        }// fine del blocco if
    }// end of method


    public void task(TaskWiki task) throws IllegalStateException {
        if (task.usaDaemon()) {
            schedule(task.getSchedule(), task);
        }// end of if cycle
    }// end of method


    @Override
    public void stop() throws IllegalStateException {
        if (isStarted()) {
            super.stop();

            // save daemons status flag into servlet context
//            ServletContext svc = ABootStrap.getServletContext();
//            svc.setAttribute(DAEMON_NAME, false);

//            if (Pref.getBool(CostBio.USA_LOG_DAEMONS, false)) {
//                Log.debug("daemonCicloCrono", "Spento");
//            }// fine del blocco if

        }// fine del blocco if
    }// end of method

}// end of class
