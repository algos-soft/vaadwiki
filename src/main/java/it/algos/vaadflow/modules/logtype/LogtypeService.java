package it.algos.vaadflow.modules.logtype;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow.application.FlowCost.*;

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
@Qualifier(TAG_TYP)
@Slf4j
@AIScript(sovrascrivibile = false)
public class LogtypeService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public LogtypeRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public LogtypeService(@Qualifier(TAG_TYP) MongoRepository repository) {
        super(repository);
        super.entityClass = Logtype.class;
        this.repository = (LogtypeRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param code di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Logtype findOrCrea(String code) {
        Logtype entity = findByKeyUnica(code);

        if (entity == null) {
            entity = crea(code);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param code di riferimento (obbligatorio ed unico)
     *
     * @return la entity appena creata
     */
    public Logtype crea(String code) {
        return (Logtype) save(newEntity(0, code));
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Logtype newEntity() {
        return newEntity(0, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param ordine di presentazione (obbligatorio con inserimento automatico se è zero)
     * @param code   codice di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Logtype newEntity(int ordine, String code) {
        Logtype entity = findByKeyUnica(code);

        if (entity == null) {
            entity = Logtype.builderLogtype()
                    .ordine(ordine != 0 ? ordine : this.getNewOrdine())
                    .code(text.isValid(code) ? code : null)
                    .build();
        }// end of if cycle

        return (Logtype) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Logtype findByKeyUnica(String code) {
        return repository.findByCode(code);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (nuova entity)
     *
     * @return la entity appena trovata
     */
    public Logtype getSetup() {
        return findByKeyUnica(SETUP);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (nuova entity)
     *
     * @return la entity appena trovata
     */
    public Logtype getNew() {
        return findByKeyUnica(NEW);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (entity modificata)
     *
     * @return la entity appena trovata
     */
    public Logtype getEdit() {
        return findByKeyUnica(EDIT);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (entity cancellata)
     *
     * @return la entity appena trovata
     */
    public Logtype getDelete() {
        return findByKeyUnica(DELETE);
    }// end of method

    /**
     * Raggruppamento logico dei log per type di eventi (import di dati)
     *
     * @return la entity appena trovata
     */
    public Logtype getImport() {
        return findByKeyUnica(IMPORT);
    }// end of method

}// end of class