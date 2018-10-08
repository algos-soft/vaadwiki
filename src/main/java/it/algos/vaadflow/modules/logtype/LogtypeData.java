package it.algos.vaadflow.modules.logtype;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.enumeration.EALogType;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.TAG_TYP;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 06-gen-2018
 * Time: 15:29
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class LogtypeData extends AData {


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     *
     * @param service iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public LogtypeData(@Qualifier(TAG_TYP) IAService service) {
        super(Logtype.class,service);
    }// end of Spring constructor


    /**
     * Creazione della collezione
     */
    protected int creaAll() {
        int num = 0;

        for (EALogType type : EALogType.values()) {
            ((LogtypeService)service).findOrCrea(type.getTag());
            num++;
        }// end of for cycle

        return num;
    }// end of method


}// end of class
