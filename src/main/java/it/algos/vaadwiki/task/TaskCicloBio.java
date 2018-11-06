package it.algos.vaadwiki.task;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.USA_DAEMON_ATTIVITA;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 12:00
 */
@Slf4j
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
class TaskCicloBio extends ATask {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private AttivitaService attivitaService;



    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {

        if (pref.isBool(USA_DAEMON_ATTIVITA)) {
            attivitaService.download();

            //@TODO Prevedere un flag di preferenze per mostrare o meno la nota
            //@TODO Prevedere un flag di preferenze per usare il log interno
            if (true) {
                System.out.println("Task di download attività: " + date.getTime(LocalDateTime.now()));
            }// end of if cycle
        }// end of if cycle
    }// end of method


    public void executes(TaskExecutionContext context) throws RuntimeException {
                log.debug("daemonCicloCrono", "Attivato ciclo daemonCicloCrono; flag in preferenze per confermare esecuzione alle 0:01");
System.out.println("Un minuto");
//        WikiLogin loginWiki = VaadApp.WIKI_LOGIN;
////            if (loginWiki == null) {
//        loginWiki = new WikiLogin("Gacbot@Gacbot", "tftgv0vhl16c0qnmfdqide3jqdp1i5m7");
//        VaadApp.WIKI_LOGIN = loginWiki;
////            }// end of if cycle
//
//        if (Pref.getBool(CostBio.USA_DAEMONS_DOWNLOAD, true)) {
//            Esegue.cicloDownload();
//        }// fine del blocco if
//
//        if (Bio.count() > NUMERO_VOCI_MINIMO_PER_OPERATIVITA_NORMALE) {
//            Esegue.cicloUpdate();
//            Esegue.cicloElabora();
//
//            if (Pref.getBool(CostBio.USA_DAEMONS_CRONO, true)) {
//                Esegue.cicloUpload();
//            }// end of if cycle
//        }// end of if cycle

    }// end of method

}// end of class
