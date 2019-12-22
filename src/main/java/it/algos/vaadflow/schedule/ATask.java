package it.algos.vaadflow.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AMailService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadwiki.statistiche.StatisticheService;
import it.algos.vaadwiki.upload.UploadService;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 15-lug-2018
 * Time: 16:38
 */
@SpringComponent
@Slf4j
public class ATask extends Task {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ADateService date;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mailService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected UploadService uploadService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected StatisticheService statisticheService;

    /**
     * Property usata da Daemonxxx <br>
     * Property usata dalle xxxViewList <br>
     */
    protected EASchedule eaSchedule;

    protected boolean usaDaemon;

    /**
     * Costruttore <br>
     */
    public ATask() {
    }// end of Spring constructor


    /**
     * Costruttore @Autowired (nella sottoclasse concreta) <br>
     * La sottoclasse usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * La sottoclasse usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param service iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public ATask(IAService service) {
    }// end of Spring constructor


    public boolean usaDaemon() {
        return usaDaemon;
    }// end of method


    public String getSchedule() {
        return eaSchedule.getPattern();
    }// end of method


    public String getNota() {
        return eaSchedule.getNota();
    }// end of method


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
    }// end of method

}// end of class
