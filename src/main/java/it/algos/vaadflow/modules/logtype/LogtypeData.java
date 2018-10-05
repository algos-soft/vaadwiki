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
     * Il service iniettato dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia IAService
     * Spring costruisce al volo, quando serve, una implementazione di IAService (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private LogtypeService service;


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
        this.service = (LogtypeService) service;
    }// end of Spring constructor


    /**
     * Metodo invocato da ABoot <br>
     * <p>
     * Creazione di una collezione - Solo se non ci sono records
     */
    @Override
    public void loadData() {
        int numRec = super.count();

        if (numRec == 0) {
            numRec = creaAll();
            log.warn("Algos - Creazione dati iniziali LogtypeData.inizia(): " + numRec + " schede");
        } else {
            log.info("Algos - Data. La collezione Logtype è presente: " + numRec + " schede");
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione della collezione
     */
    private int creaAll() {
        int num = 0;

        for (EALogType type : EALogType.values()) {
            service.findOrCrea(type.getTag());
            num++;
        }// end of for cycle

        return num;
    }// end of method


}// end of class
