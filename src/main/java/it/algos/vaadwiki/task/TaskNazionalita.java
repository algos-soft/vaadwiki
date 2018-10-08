package it.algos.vaadwiki.task;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.VaadwikiCost.USA_DAEMON_NAZIONALITA;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 12:19
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class TaskNazionalita extends ATask {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private NazionalitaService nazionalitaService;


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {

        if (pref.isBool(USA_DAEMON_NAZIONALITA)) {
            nazionalitaService.download();

            //@TODO Prevedere un flag di preferenze per mostrare o meno la nota
            //@TODO Prevedere un flag di preferenze per usare il log interno
            if (true) {
                System.out.println("Task di download nazionalità: " + date.getTime(LocalDateTime.now()));
            }// end of if cycle
        }// end of if cycle
    }// end of method

}// end of class
