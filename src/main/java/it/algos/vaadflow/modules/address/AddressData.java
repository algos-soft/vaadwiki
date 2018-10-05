package it.algos.vaadflow.modules.address;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.TAG_ADD;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 02-set-2018
 * Time: 18:47
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AddressData extends AData {

    /**
     * Il service viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private AddressService service;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param service di collegamento per la Repository
     */
    @Autowired
    public AddressData(@Qualifier(TAG_ADD) IAService service) {
        super(Address.class, service);
        this.service = (AddressService) service;
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
            log.warn("Algos - Creazione dati iniziali AddressData.loadData(): " + numRec + " schede");
        } else {
            log.info("Algos - Data. La collezione Address è presente: " + numRec + " schede");
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione della collezione
     */
    private int creaAll() {
        int num = 0;
        String indirizzo;
        String localita;
        String cap;

        for (EAAddress address : EAAddress.values()) {
            indirizzo = address.getIndirizzo();
            localita = address.getLocalita();
            cap = address.getCap();

            service.crea(indirizzo, localita, cap);
            num++;
        }// end of for cycle

        return num;
    }// end of method

}// end of class
