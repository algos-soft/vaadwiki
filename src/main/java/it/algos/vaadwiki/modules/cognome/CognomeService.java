package it.algos.vaadwiki.modules.cognome;

import com.mongodb.client.DistinctIterable;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AService;
import it.algos.vaadwiki.modules.bio.Bio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 14-giu-2019 16.34.34 <br>
 * <br>
 * Business class. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * NOT annotated with @VaadinSessionScope (sbagliato, perché SpringBoot va in loop iniziale) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_COG)
@Slf4j
@AIScript(sovrascrivibile = false)
public class CognomeService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public CognomeRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public CognomeService(@Qualifier(TAG_COG) MongoRepository repository) {
        super(repository);
        super.entityClass = Cognome.class;
        this.repository = (CognomeRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param cognome di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Cognome findOrCrea(String cognome) {
        Cognome entity = findByKeyUnica(cognome);

        if (entity == null) {
            entity = crea(cognome, 0);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param cognome di riferimento (obbligatorio ed unico)
     * @param voci    biografiche con questo cognome <br>
     *
     * @return la entity trovata o appena creata
     */
    public Cognome findOrCrea(String cognome, int voci) {
        Cognome entity = findByKeyUnica(cognome);

        if (entity == null) {
            entity = crea(cognome, voci);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param cognome di riferimento (obbligatorio ed unico)
     *
     * @return la entity appena creata
     */
    public Cognome crea(String cognome) {
        return crea(cognome, 0);
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param cognome di riferimento (obbligatorio ed unico)
     * @param voci    biografiche con questo cognome <br>
     *
     * @return la entity appena creata
     */
    public Cognome crea(String cognome, int voci) {
        return (Cognome) save(newEntity(cognome, voci));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Cognome newEntity() {
        return newEntity("", 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param cognome di riferimento (obbligatorio ed unico)
     * @param voci    biografiche con questo nome <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Cognome newEntity(String cognome, int voci) {
        Cognome entity = null;

        entity = findByKeyUnica(cognome);
        if (entity != null) {
            return findByKeyUnica(cognome);
        }// end of if cycle

        entity = Cognome.builderCognome()
                .cognome(text.isValid(cognome) ? cognome : null)
                .voci(voci != 0 ? voci : this.getNewOrdine())
                .build();

        return (Cognome) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Cognome findById(String id) {
        return (Cognome) super.findById(id);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param cognome di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Cognome findByKeyUnica(String cognome) {
        return repository.findByCognome(cognome);
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Cognome) entityBean).getCognome();
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     * Se esiste la property 'ordine', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'code', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'descrizione', ordinate secondo questa property <br>
     * Altrimenti, ordinate secondo il metodo sovrascritto nella sottoclasse concreta <br>
     * Altrimenti, ordinate in ordine di inserimento nel DB mongo <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<Cognome> findAll() {
        return (List<Cognome>) super.findAll(new Sort(Sort.Direction.DESC, "voci"));
    }// end of method


    /**
     * Cancella i cognomi esistenti <br>
     * Crea tutti i cognomi <br>
     * Controlla che ci siano almeno n voci biografiche per il singolo cognomi <br>
     * Registra la entity <br>
     * Non registra la entity col cognomi mancante <br>
     */
    public void crea() {
        long inizio = System.currentTimeMillis();
        int cont = 0;
        System.out.println("Creazione completa cognomi delle biografie. Circa 1 minuto.");
        deleteAll();

        DistinctIterable<String> listaCognomiDistinti = mongo.mongoOp.getCollection("bio").distinct("cognome", String.class);
        for (String cognome : listaCognomiDistinti) {
            cont++;
            saveNumVoci(cognome);
        }// end of for cycle

        pref.saveValue(LAST_ELABORA_NOME, LocalDateTime.now());
        System.out.println("Creazione completa di " + cont + " cognomi. Tempo impiegato: " + date.deltaText(inizio));
    }// end of method


    /**
     * Controlla che ci siano almeno n voci biografiche per il singolo nome <br>
     */
    public void update() {
        long inizio = System.currentTimeMillis();
        System.out.println("Elaborazione cognomi delle biografie. Meno di 1 minuto.");
        for (Cognome cognome : findAll()) {
            saveNumVoci(cognome);
        }// end of for cycle
        pref.saveValue(LAST_ELABORA_COGNOME, LocalDateTime.now());
        System.out.println("Elaborazione completa dei cognomi. Tempo impiegato: " + date.deltaText(inizio));
    }// end of method


    /**
     * Registra il numero di voci biografiche che hanno il nome indicato <br>
     */
    public void saveNumVoci(String cognome) {
        //--Soglia minima per creare una entity nella collezione Nomi sul mongoDB
        int sogliaMongo = pref.getInt(SOGLIA_COGNOMI_MONGO, 10);
        int numVoci = 0;
        Query query = new Query();
        query.addCriteria(Criteria.where("cognome").is(cognome));
        numVoci = ((List) mongo.mongoOp.find(query, Bio.class)).size();
        if (numVoci >= sogliaMongo && text.isValid(cognome)) {
            this.findOrCrea(cognome, numVoci);
        }// end of if cycle
    }// end of method


    /**
     * Registra il numero di voci biografiche che hanno il nome indicato <br>
     */
    public void saveNumVoci(Cognome cognome) {
        int numVoci = 0;
        Query query = new Query();
        query.addCriteria(Criteria.where("cognome").is(cognome.cognome));
        numVoci = ((List) mongo.mongoOp.find(query, Bio.class)).size();
        cognome.voci = numVoci;
        save(cognome);
    }// end of method

}// end of class