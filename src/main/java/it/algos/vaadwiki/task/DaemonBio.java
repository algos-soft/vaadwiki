package it.algos.vaadwiki.task;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
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
    private TaskAttività attivita;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskNazionalita nazionalita;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskProfessione professione;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskCategoria categoria;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private TaskCicloBio cicloBio;


    @PostConstruct
    public void startBio() throws IllegalStateException {
        if (!isStarted()) {
            super.start();

            // Schedule task
            // Ogni giorno
            schedule(EASchedule.giornoPrimoMinuto.getTag(), attivita);
            schedule(EASchedule.giornoSecondoMinuto.getTag(), nazionalita);
            schedule(EASchedule.giornoTerzoMinuto.getTag(), professione);
            schedule(EASchedule.giornoQuartoMinuto.getTag(), categoria);
            schedule(EASchedule.giornoDecimoMinuto.getTag(), cicloBio);

//            if (Pref.getBool(CostBio.USA_LOG_DAEMONS, false)) {
//                Log.debug("daemonCicloCrono", "Attivato ciclo daemonCicloCrono; flag in preferenze per confermare esecuzione alle 0:01");
//            }// fine del blocco if
        }// fine del blocco if
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
