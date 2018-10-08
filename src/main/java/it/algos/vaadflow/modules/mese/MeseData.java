package it.algos.vaadflow.modules.mese;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.enumeration.EAMese;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.TAG_MES;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 08-ott-2018
 * Time: 07:24
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class MeseData extends AData {


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param service di collegamento per la Repository
     */
    @Autowired
    public MeseData(@Qualifier(TAG_MES) IAService service) {
        super(Mese.class, service);
    }// end of Spring constructor


    /**
     * Creazione della collezione
     * Crea i dodici mesi
     */
    protected int creaAll() {
        int num = 0;

        for (EAMese eaMese : EAMese.values()) {
            ((MeseService)service).crea(eaMese.getLungo(), eaMese.getBreve(), eaMese.getGiorni());
            num++;
        }// end of for cycle

        return num;
    }// end of method

}// end of class
