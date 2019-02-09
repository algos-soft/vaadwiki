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
public class TaskUpdate extends ATask {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected CicloUpdate cicloUpdate;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected BioService bio;


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
        super.eaSchedule = EASchedule.oreQuattro;
        super.usaDaemon = pref.isBool(USA_DAEMON_BIO);
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        long inizio = System.currentTimeMillis();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end;
        DownloadResult result;
        String testo = "";
        testo += EASchedule.oreQuattro.getNota();
        testo += A_CAPO;
        testo += A_CAPO;
        testo += "Carica le nuove voci biografiche e aggiorna tutte quelle esistenti";
        testo += A_CAPO;
        testo += "Ciclo del " + date.get();
        testo += A_CAPO;
        testo += "Iniziato alle " + date.getOrario(start);
        testo += A_CAPO;

        if (pref.isBool(USA_DAEMON_BIO)) {
            System.out.println("Inizio task di download: " + date.getTime(LocalDateTime.now()));

            result = cicloUpdate.esegue();

            if (pref.isBool(SEND_MAIL_CICLO)) {
                end = LocalDateTime.now();
                testo += "Terminato alle " + date.getOrario(end);
                testo += A_CAPO;
                testo += "Durata totale: " + date.deltaText(inizio);
                testo += A_CAPO;
                testo += "Nel db ci sono " + text.format(bio.count()) + " voci biografiche";
                testo += A_CAPO;
                testo +=  "Sono state aggiornate " + text.format(result.getNumVociRegistrate()) + " voci" ;
                mailService.send("Ciclo update", testo);
            }// end of if cycle
            System.out.println("Fine task di download: " + date.getTime(LocalDateTime.now()));
        }// end of if cycle
    }// end of method

}// end of class
