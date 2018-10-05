package it.algos.vaadflow.modules.role;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.TAG_ROL;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 12-nov-2017
 * Time: 14:54
 * <p>
 * Estende la classe astratta AData per la costruzione inziale della Collection <br>
 * <p>
 * I valori iniziali sono presi da una Enumeration codificata e standard <br>
 * Vengono caricati sul DB (mongo) in modo che se ne possano aggiungere altri specifici per l'applicazione<br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio per le injections) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class RoleData extends AData {


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param service di collegamento per la Repository
     */
    @Autowired
    public RoleData(@Qualifier(TAG_ROL) IAService service) {
        super(Role.class, service);
    }// end of Spring constructor


    /**
     * Metodo invocato da ABoot <br>
     * <p>
     * Creazione di una collezione - Solo se non ci sono records
     */
    public void loadData() {
        int numRec = super.count();

        if (numRec == 0) {
            numRec = creaAll();
            log.warn("Algos - Creazione dati iniziali RoleData.loadData(): " + numRec + " schede");
        } else {
            log.info("Algos - Data. La collezione Role è presente: " + numRec + " schede");
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione della collezione
     */
    private int creaAll() {
        int num = 0;

        for (EARole ruolo : EARole.values()) {
            service.findOrCrea(ruolo.toString());
            num++;
        }// end of for cycle

        return num;
    }// end of method


}// end of class
