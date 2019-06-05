package it.algos.vaadwiki.modules.nome;

import com.mongodb.client.DistinctIterable;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.LibBio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.TAG_NOM;

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
public class NomeService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Service (@Scope = 'singleton') iniettato da StaticContextAccessor e usato come libreria <br>
     * Unico per tutta l'applicazione. Usato come libreria.
     */
    public LibBio libBio = LibBio.getInstance();

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
     * Elabora tutti i nomi <br>
     * Controlla che ci siano almeno n voci biografiche per il singolo nome <br>
     * Registra la entity <br>
     */
    public void crea() {
        String message = "";
        long inizio = System.currentTimeMillis();
        List<String> nomi;
        int cont = 0;
        int numVoci = 0;
        int numEntities;
        numEntities = this.count();
        System.out.println("Creazione completa nomi delle biografie. Circa due minuti.");
        Sort sort = new Sort(Sort.Direction.DESC, "nome");

        DistinctIterable<String> listaNomiDistinti = mongo.mongoOp.getCollection("bio").distinct("nome", String.class);
        for (String nome : listaNomiDistinti) {
            cont++;

            org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();

            query.addCriteria(Criteria.where("nome").is(nome));
            query.with(sort);
            numVoci = ((List) mongo.mongoOp.find(query, Bio.class)).size();

            if (numVoci >= 50) {
                this.findOrCrea(nome, numVoci);
            }// end of if cycle

            if (cont > 3700) {
                break;
            }// end of if cycle

        }// end of for cycle

        System.out.println("Creazione completa nomi delle biografie. Tempo impiegato: " + date.deltaText(inizio));
    }// end of method

}// end of class