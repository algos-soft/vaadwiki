package it.algos.vaadwiki.modules.nome;

import com.mongodb.client.DistinctIterable;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.wiki.NomeCognomeService;
import it.algos.vaadwiki.service.LibBio;
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
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 29-mag-2019 19.55.00 <br>
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
@Qualifier(TAG_NOM)
@Slf4j
@AIScript(sovrascrivibile = false)
public class NomeService extends NomeCognomeService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public NomeRepository repository;

    @Autowired
    private AMongoService mongo;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public NomeService(@Qualifier(TAG_NOM) MongoRepository repository) {
        super(repository);
        super.entityClass = Nome.class;
        this.repository = (NomeRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param nome di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Nome findOrCrea(String nome) {
        return findOrCrea(nome, 0);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param nome di riferimento (obbligatorio ed unico)
     * @param voci biografiche con questo nome <br>
     *
     * @return la entity trovata o appena creata
     */
    public Nome findOrCrea(String nome, int voci) {
        Nome entity = findByKeyUnica(nome);

        if (entity == null) {
            entity = crea(nome, voci);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param nome di riferimento (obbligatorio ed unico)
     *
     * @return la entity appena creata
     */
    public Nome crea(String nome) {
        return crea(nome, 0);
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param nome di riferimento (obbligatorio ed unico)
     * @param voci biografiche con questo nome <br>
     *
     * @return la entity appena creata
     */
    public Nome crea(String nome, int voci) {
        return (Nome) save(newEntity(nome, voci));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Nome newEntity() {
        return newEntity("", 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param nome di riferimento (obbligatorio ed unico)
     * @param voci biografiche con questo nome <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Nome newEntity(String nome, int voci) {
        Nome entity = null;

        entity = findByKeyUnica(nome);
        if (entity != null) {
            return findByKeyUnica(nome);
        }// end of if cycle

        entity = Nome.builderNome()
                .nome(text.isValid(nome) ? nome : null)
                .voci(voci != 0 ? voci : this.getNewOrdine())
                .build();

        return (Nome) creaIdKeySpecifica(entity);
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
    public Nome findById(String id) {
        return (Nome) super.findById(id);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param nome di riferimento (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Nome findByKeyUnica(String nome) {
        return repository.findByNome(nome);
    }// end of method


    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Nome) entityBean).getNome();
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
    public List<Nome> findAll() {
        return (List<Nome>) super.findAll(new Sort(Sort.Direction.DESC, "voci"));
    }// end of method


    /**
     * Cancella i nomi esistenti <br>
     * Crea tutti i nomi <br>
     * Controlla che ci siano almeno n voci biografiche per il singolo nome <br>
     * Registra la entity <br>
     * Non registra la entity col nome mancante <br>
     */
    public void crea() {
        long inizio = System.currentTimeMillis();
        int cont = 0;
        log.info("Creazione completa nomi delle biografie. Circa 10 minuti.");
        deleteAll();

        //@Field("nome")
        DistinctIterable<String> listaNomiDistinti = mongo.mongoOp.getCollection("bio").distinct("nome", String.class);
        for (String nome : listaNomiDistinti) {
            cont++;
            saveNumVoci(nome);
        }// end of for cycle

        pref.saveValue(LAST_ELABORA_NOME, LocalDateTime.now());
        log.info("Creazione completa di " + cont + " nomi. Tempo impiegato: " + date.deltaText(inizio));
    }// end of method


    /**
     * Controlla che ci siano almeno n voci biografiche per il singolo nome <br>
     * Controlla che nome sia valido <br>
     */
    public void update() {
        long inizio = System.currentTimeMillis();
        log.info("Elaborazione nomi delle biografie. Circa 5 minuti.");

        for (Nome nome : findAll()) {
            saveNumVoci(nome);
        }// end of for cycle

        pref.saveValue(LAST_ELABORA_NOME, LocalDateTime.now());
        log.info("Elaborazione completa dei nomi. Tempo impiegato: " + date.deltaText(inizio));
    }// end of method


    /**
     * Registra il numero di voci biografiche che hanno il nome indicato <br>
     */
    public void saveNumVoci(String nome) {
        //--Soglia minima per creare una entity nella collezione Nomi sul mongoDB
        int sogliaMongo = pref.getInt(SOGLIA_NOMI_MONGO, 10);
        int numVoci = 0;
        Query query = new Query();
        query.addCriteria(Criteria.where("nome").is(nome));
        numVoci = ((List) mongo.mongoOp.find(query, Bio.class)).size();
        if (numVoci >= sogliaMongo && text.isValid(nome)) {
            this.findOrCrea(nome, numVoci);
        }// end of if cycle
    }// end of method


    /**
     * Registra il numero di voci biografiche che hanno il nome indicato <br>
     */
    public void saveNumVoci(Nome nome) {
        int numVoci = 0;
        Query query = new Query();
        query.addCriteria(Criteria.where("nome").is(nome.nome));
        numVoci = ((List) mongo.mongoOp.find(query, Bio.class)).size();
        nome.voci = numVoci;
        save(nome);
    }// end of method


}// end of class