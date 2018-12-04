package it.algos.vaadwiki.task;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadwiki.modules.attnazprofcat.AttNazProfCatService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-dic-2018
 * Time: 21:22
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public   class BioTask extends ATask {


    protected AttNazProfCatService service;

    protected boolean usaDaemon;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * La sottoclasse usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * La sottoclasse usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param service iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public BioTask(IAService service) {
        super();
        this.service = (AttNazProfCatService)service;
    }// end of Spring constructor


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        service.download();

        //@TODO Prevedere un flag di preferenze per mostrare o meno la nota
        //@TODO Prevedere un flag di preferenze per usare il log interno
        if (true) {
            System.out.println("Task di download nazionalità: " + date.getTime(LocalDateTime.now()));
        }// end of if cycle
    }// end of method

}// end of class
