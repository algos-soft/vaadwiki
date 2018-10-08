package it.algos.vaadflow.modules.mese;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow.application.FlowCost.TAG_MES;


/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 7-ott-2018 21.00.40 <br>
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
@Qualifier(TAG_MES)
@Slf4j
@AIScript(sovrascrivibile = false)
public class MeseService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public MeseRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public MeseService(@Qualifier(TAG_MES) MongoRepository repository) {
        super(repository);
        super.entityClass = Mese.class;
        this.repository = (MeseRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity e la registra <br>
     *
     * @param titoloLungo nome completo (obbligatorio, unico)
     * @param titoloBreve nome abbreviato di tre cifre (obbligatorio, unico)
     * @param giorni      numero di giorni presenti (obbligatorio)
     *
     * @return la entity appena creata
     */
    public Mese crea(String titoloLungo, String titoloBreve, int giorni) {
        return (Mese) save(newEntity(titoloLungo, titoloBreve, giorni));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Mese newEntity() {
        return newEntity("", "", 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param titoloLungo nome completo (obbligatorio, unico)
     * @param titoloBreve nome abbreviato di tre cifre (obbligatorio, unico)
     * @param giorni      numero di giorni presenti (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Mese newEntity(String titoloLungo, String titoloBreve, int giorni) {
        Mese entity = null;

        entity = findByKeyUnica(titoloLungo);
        if (entity != null) {
            return findByKeyUnica(titoloLungo);
        }// end of if cycle

        entity = Mese.builderMese()
                .titoloLungo(titoloLungo)
                .titoloBreve(titoloBreve)
                .giorni(giorni)
                .build();

        return (Mese) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Property unica (se esiste).
     */
    public String getPropertyUnica(AEntity entityBean) {
        return text.isValid(((Mese) entityBean).getTitoloLungo()) ? ((Mese) entityBean).getTitoloLungo() : "";
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param titoloLungo nome completo (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Mese findByKeyUnica(String titoloLungo) {
        return repository.findByTitoloLungo(titoloLungo);
    }// end of method


}// end of class