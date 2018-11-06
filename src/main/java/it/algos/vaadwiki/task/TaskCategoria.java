package it.algos.vaadwiki.task;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.categoria.CategoriaService;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.USA_DAEMON_CATEGORIA;


/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 14:42
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class TaskCategoria extends ATask {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private CategoriaService categoriaService;


    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {

        if (pref.isBool(USA_DAEMON_CATEGORIA)) {
            categoriaService.download();

            //@TODO Prevedere un flag di preferenze per mostrare o meno la nota
            //@TODO Prevedere un flag di preferenze per usare il log interno
            if (true) {
                System.out.println("Task di download categoria: " + date.getTime(LocalDateTime.now()));
            }// end of if cycle
        }// end of if cycle
    }// end of method

}// end of class
