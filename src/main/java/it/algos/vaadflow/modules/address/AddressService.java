package it.algos.vaadflow.modules.address;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow.application.FlowCost.TAG_ADD;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.14.56 <br>
 * <br>
 * Estende la classe astratta AService. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Service (ridondante) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_ADD)
@Slf4j
@AIScript(sovrascrivibile = false)
public class AddressService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    public AddressService(@Qualifier(TAG_ADD) MongoRepository repository) {
        super(repository);
        super.entityClass = Address.class;
    }// end of Spring constructor


    /**
     * Crea una entity <br>
     * Se esiste già, la cancella prima di ricrearla <br>
     *
     * @param indirizzo: via, nome e numero (obbligatoria, non unica)
     * @param localita:  località (obbligatoria, non unica)
     * @param cap:       codice di avviamento postale (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Address crea(String indirizzo, String localita, String cap) {
        Address entity;

        entity = newEntity(indirizzo, localita, cap);
        save(entity);

        return entity;
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Address newEntity() {
        return newEntity("", "", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param indirizzo: via, nome e numero (obbligatoria, non unica)
     * @param localita:  località (obbligatoria, non unica)
     * @param cap:       codice di avviamento postale (obbligatoria, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Address newEntity(String indirizzo, String localita, String cap) {
        Address entity = null;

        entity = Address.builderAddress()
                .indirizzo(text.isValid(indirizzo) ? indirizzo : null)
                .localita(text.isValid(localita) ? localita : null)
                .cap(text.isValid(cap) ? cap : null)
                .build();

        return (Address) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param eaAddress: enumeration di dati iniziali di prova
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Address newEntity(EAAddress eaAddress) {
        String indirizzo;
        String localita;
        String cap;

        if (eaAddress != null) {
            indirizzo = eaAddress.getIndirizzo();
            localita = eaAddress.getLocalita();
            cap = eaAddress.getCap();

            return newEntity(indirizzo, localita, cap);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method

}// end of class