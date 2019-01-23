package it.algos.vaadwiki.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EASchedule;
import it.algos.vaadflow.schedule.ATask;
import it.algos.vaadwiki.download.CicloService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.TASK_CRO;
import static it.algos.vaadwiki.application.WikiCost.USA_DAEMON_BIO;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 23-gen-2019
 * Time: 07:11
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TASK_CRO)
@Slf4j
public class TaskCrono extends ATask {


}// end of class
