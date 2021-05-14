package it.algos.vaadflow14.backend.service;

import com.google.gson.*;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.result.*;
import com.vaadin.flow.data.provider.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.bson.*;
import org.bson.conversions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mar, 05-mag-2020
 * Time: 17:36
 * <p>
 * Classe di servizio per l'accesso al database <br>
 * Prioritario l'utilizzo di MongoOperations, inserito automaticamente da SpringBoot <br>
 * Per query più specifiche si può usare MongoClient <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired private AMongoService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AMongoService<capture> extends AAbstractService {

    public final static int STANDARD_MONGO_MAX_BYTES = 33554432;

    public final static int EXPECTED_ALGOS_MAX_BYTES = 50151432;

    /**
     * Versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Inietta da Spring
     */
    public MongoOperations mongoOp;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AGSonService agSonService;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    private MongoClient mongoClient;

    private MongoDatabase database;

    /**
     * Costruttore @Autowired. <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore <br>
     * Se ci sono DUE o più costruttori, va in errore <br>
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri <br>
     */
    public AMongoService(MongoOperations mongoOp) {
        this.mongoOp = mongoOp;
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se viene implementata una sottoclasse, passa di qui per ogni sottoclasse oltre che per questa istanza <br>
     * Se esistono delle sottoclassi, passa di qui per ognuna di esse (oltre a questa classe madre) <br>
     */
    @PostConstruct
    protected void postConstruct() {
        fixProperties();
    }


    /**
     * Creazione iniziale di eventuali properties indispensabili per l'istanza <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Metodo private che NON può essere sovrascritto <br>
     */
    private void fixProperties() {
        //        mongoClient = new MongoClient("localhost");

        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + databaseName);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(mongoClientSettings);

        if (text.isValid(databaseName)) {
            database = mongoClient.getDatabase(databaseName);
        }
    }


    /**
     * Collection del database. <br>
     *
     * @param entityClazz della collezione da controllare
     *
     * @return collection if exist
     */
    private MongoCollection<Document> getCollection(Class<? extends AEntity> entityClazz) {
        return getCollection(annotation.getCollectionName(entityClazz));
    }


    /**
     * Collection del database. <br>
     *
     * @param collectionName The name of the collection or view
     *
     * @return collection if exist
     */
    private MongoCollection<Document> getCollection(String collectionName) {
        if (text.isValid(collectionName)) {
            return database != null ? database.getCollection(collectionName) : null;
        }
        else {
            return null;
        }
    }


    /**
     * Check the existence of a collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection has entities
     */
    public boolean isValid(String collectionName) {
        return count(collectionName) > 0;
    }

    /**
     * Check the existence of a collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection has entities
     */
    public boolean isValid(Class<? extends AEntity> entityClazz) {
        return count(entityClazz) > 0;
    }

    /**
     * Check the existence of a collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection exist
     */
    public boolean isExists(Class<? extends AEntity> entityClazz) {
        return isExists(entityClazz.getClass().getSimpleName());
    }

    /**
     * Check the existence of a collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection exist
     */
    public boolean isNotExists(Class<? extends AEntity> entityClazz) {
        return !isExists(entityClazz);
    }

    /**
     * Check the existence of a collection. <br>
     *
     * @param collectionName corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection exist
     */
    public boolean isExists(String collectionName) {
        return mongoOp.collectionExists(collectionName);
    }

    /**
     * Check the existence of a collection. <br>
     *
     * @param collectionName corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection exist
     */
    public boolean isNotExists(String collectionName) {
        return !isExists(collectionName);
    }


    /**
     * Check if a collection is empty. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection is empty
     */
    public boolean isEmpty(Class<? extends AEntity> entityClazz) {
        return count(entityClazz) < 1;
    }


    /**
     * Check the existence of some entities. <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  per costruire la query
     * @param propertyValue (serializable) per costruire la query
     *
     * @return true if the collection has entities requested
     */
    public boolean esistono(Class<? extends AEntity> entityClazz, String propertyName, Serializable propertyValue) {
        return count(entityClazz, propertyName, propertyValue) > 0;
    }


    /**
     * Conteggio di tutte le entities di una collection NON filtrate. <br>
     *
     * @param collectionName The name of the collection or view to count
     *
     * @return numero totale di entities
     */
    public int count(String collectionName) {
        return count(collectionName, (Query) null);
    }


    /**
     * Conteggio di tutte le entities di una collection NON filtrate. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return numero totale di entities
     */
    public int count(Class<? extends AEntity> entityClazz) {
        return count(entityClazz, (Query) null);
    }


    /**
     * Conteggio di alcune entities selezionate di una collection. <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  per costruire la query
     * @param propertyValue (serializable) per costruire la query
     *
     * @return numero di entities selezionate
     */
    public int count(Class<? extends AEntity> entityClazz, String propertyName, Serializable propertyValue) {
        if (entityClazz == null) {
            return 0;
        }
        if (text.isEmpty(propertyName) || propertyValue == null) {
            return count(entityClazz);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(propertyName).is(propertyValue));
        return findAll(entityClazz, query).size();
    }


    /**
     * Conteggio di tutte le entities di una collection filtrate con una query. <br>
     * Se la query è nulla o vuota, restituisce il totale dell'intera collection <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param query       Optional. A query that selects which documents to count in the collection or view.
     *
     * @return numero di entities eventualmente filtrate
     */
    public int count(Class<? extends AEntity> entityClazz, Query query) {
        return count(annotation.getCollectionName(entityClazz), query);
    }


    /**
     * Conteggio di tutte le entities di una collection filtrate con una query. <br>
     * Se la query è nulla o vuota, restituisce il totale dell'intera collection <br>
     * <p>
     * Counts the number of documents in a collection or a view. <br>
     * Returns a document that contains this count and as well as the command status. <br>
     * Avoid using the count and its wrapper methods without a query predicate since without the query predicate, <br>
     * these operations return results based on the collection’s metadata, which may result in an approximate count. <br>
     *
     * @param collectionName The name of the collection or view to count
     * @param query          Optional. A query that selects which documents to count in the collection or view.
     *
     * @return numero di entities eventualmente filtrate
     *
     * @see(https://docs.mongodb.com/manual/reference/command/count/)
     */
    public int count(String collectionName, Query query) {
        if (text.isEmpty(collectionName)) {
            return 0;
        }

        if (query == null) {
            query = new Query();
        }

        return (int) mongoOp.count(query, collectionName);
    }

    /**
     * Conteggio di tutte le entities di una collection filtrate con una mappa di filtri. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB. Obbligatoria.
     * @param filtro      eventuali condizioni di filtro. Se nullo o vuoto recupera tutta la collection.
     *
     * @return numero di entities eventualmente filtrate
     */
    public int count(final Class<? extends AEntity> entityClazz, final AFiltro filtro) {
        Map<String, AFiltro> mappaFiltri = filtro != null ? Collections.singletonMap(filtro.getCriteria().getKey(), filtro) : null;
        return count(entityClazz, mappaFiltri);
    }

    /**
     * Conteggio di tutte le entities di una collection filtrate con una mappa di filtri. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB. Obbligatoria.
     * @param mappaFiltri eventuali condizioni di filtro. Se nullo o vuoto recupera tutta la collection.
     *
     * @return numero di entities eventualmente filtrate
     */
    public int count(final Class<? extends AEntity> entityClazz, final Map<String, AFiltro> mappaFiltri) {
        Map<String, AFiltro> filter = mappaFiltri;
        Query query = new Query();
        Criteria criteriaFiltro;
        Criteria criteriaQuery = null;

        //@todo purtroppo per adesso funziona SOLO per 1 filtro
        //@todo non riesco a clonare AFiltro o Criteria

        if (array.isAllValid(filter)) {
            for (AFiltro filtro : filter.values()) {
                criteriaFiltro = filtro.getCriteria();

                if (criteriaQuery == null) {
                    criteriaQuery = criteriaFiltro;
                }
                else {
                    //--For multiple criteria on the same field, uses a “comma” to combine them.
                    criteriaQuery.andOperator(criteriaFiltro);
                }
            }
            query.addCriteria(criteriaQuery);
        }

        return (int) mongoOp.count(query, annotation.getCollectionName(entityClazz));
    }

    /**
     * Cerca tutte le entities di una collection ordinate. <br>
     * Gli ordinamenti dei vari filtri vengono concatenati nell'ordine di costruzione <br>
     *
     * @param entityClazz    corrispondente ad una collection sul database mongoDB
     * @param sortPrevalente (facoltativa) indipendentemente dai filtri
     *
     * @return entity
     */
    @Deprecated
    public List<AEntity> findAll(Class<? extends
            AEntity> entityClazz, Sort sortPrevalente) {
        return findAll(entityClazz, (List<AFiltro>) null, sortPrevalente);
    }

    /**
     * Cerca tutte le entities di una collection filtrate. <br>
     * Gli ordinamenti dei vari filtri vengono concatenati nell'ordine di costruzione <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param listaFiltri per costruire la query
     *
     * @return entity
     */
    @Deprecated
    public List<AEntity> findAll(Class<? extends
            AEntity> entityClazz, List<AFiltro> listaFiltri) {
        return findAll(entityClazz, listaFiltri, (Sort) null);
    }

    /**
     * Cerca tutte le entities di una collection filtrate e ordinate. <br>
     * Gli ordinamenti dei vari filtri vengono concatenati nell' ordine di costruzione <br>
     * Se esiste sortPrevalente, sostituisce i sort dei vari filtri <br>
     *
     * @param entityClazz    corrispondente ad una collection sul database mongoDB
     * @param listaFiltri    per costruire la query
     * @param sortPrevalente (facoltativa) indipendentemente dai filtri
     *
     * @return entity
     */
    @Deprecated
    public List<AEntity> findAll(Class<? extends
            AEntity> entityClazz, List<AFiltro> listaFiltri, Sort sortPrevalente) {
        Query query = new Query();
        CriteriaDefinition criteria;
        Sort sort;
        if (entityClazz == null) {
            return null;
        }

        if (listaFiltri != null) {
            if (listaFiltri.size() == 1) {
                criteria = listaFiltri.get(0).getCriteria();
                if (criteria != null) {
                    query.addCriteria(criteria);
                }
                sort = listaFiltri.get(0).getSort();
                if (sort != null) {
                    query.with(sort);
                }
                else {
                    if (sortPrevalente != null) {
                        query.with(sortPrevalente);
                    }
                }
            }
            else {
                for (AFiltro filtro : listaFiltri) {
                    criteria = filtro.getCriteria();
                    if (criteria != null) {
                        query.addCriteria(criteria);
                    }
                    sort = filtro.getSort();
                    if (sort != null) {
                        query.with(sort);
                    }
                }
                if (!query.isSorted() && sortPrevalente != null) {
                    query.with(sortPrevalente);
                }
            }
        }
        else {
            if (sortPrevalente != null) {
                query.with(sortPrevalente);
            }
        }

        return findAll(entityClazz, query);
    }

    /**
     * Cerca tutte le entities di una collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return lista di entityBeans
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.find/#db.collection.find/)
     */
    @Deprecated
    public List<AEntity> find(Class<? extends AEntity> entityClazz) {
        return findAll(entityClazz);
    }

    /**
     * Cerca tutte le entities di una collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return lista di entityBeans
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.find/#db.collection.find/)
     */
    @Deprecated
    public List<AEntity> findAll(Class<? extends AEntity> entityClazz) {
        return findAll(entityClazz, (Query) null, VUOTA);
    }

    /**
     * Cerca tutte le entities di una collection filtrate con una query. <br>
     * Selects documents in a collection or view and returns a list of the selected documents. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param query       Optional. Specifies selection filter using query operators.
     *                    To return all documents in a collection, omit this parameter or pass an empty document ({})
     *
     * @return lista di entityBeans
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.find/#db.collection.find/)
     */
    @Deprecated
    public List<AEntity> findAll(Class<? extends AEntity> entityClazz, Query query) {
        return findAll(entityClazz, query, VUOTA);
    }

    /**
     * Cerca tutte le entities di una collection filtrate con una property. <br>
     * Selects documents in a collection or view and returns a list of the selected documents. <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  per costruire la query
     * @param propertyValue (serializable) per costruire la query
     *
     * @return lista di entityBeans
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.find/#db.collection.find/)
     */
    public List<AEntity> findAll(Class<? extends AEntity> entityClazz, String propertyName, Serializable propertyValue) {
        if (entityClazz == null) {
            return null;
        }
        if (text.isEmpty(propertyName) || propertyValue == null) {
            return findAll(entityClazz);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(propertyName).is(propertyValue));

        return findAll(entityClazz, query);
    }

    /**
     * Cerca tutte le entities di una collection filtrate con una query. <br>
     * <p>
     * Selects documents in a collection or view and returns a list of the selected documents. <br>
     * The projection parameter determines which fields are returned in the matching documents. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param query       Optional. Specifies selection filter using query operators.
     *                    To return all documents in a collection, omit this parameter or pass an empty document ({})
     * @param projection  Optional. Specifies the fields to return in the documents that match the query filter.
     *                    To return all fields in the matching documents, omit this parameter.
     *
     * @return lista di entityBeans
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.find/#db.collection.find/)
     */
    @Deprecated
    public List<AEntity> findAll(Class<? extends AEntity> entityClazz, Query query, String projection) {
        if (entityClazz == null) {
            return null;
        }

        if (query == null) {
            return (List<AEntity>) mongoOp.findAll(entityClazz);
        }
        else {
            if (text.isEmpty(projection)) {
                return (List<AEntity>) mongoOp.find(query, entityClazz);
            }
            else {
                return (List<AEntity>) mongoOp.find(query, entityClazz, projection);
            }
        }
    }

    /**
     * Crea un set di entities da una collection. Utilizzato (anche) da DataProvider. <br>
     * Rimanda al metodo base 'fetch' (unico usato) <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB. Obbligatoria.
     *
     * @return lista di entityBeans
     */
    public List<AEntity> fetch(Class<? extends AEntity> entityClazz) {
        return fetch(entityClazz, (AFiltro) null);
    }

    /**
     * Crea un set di entities da una collection. Utilizzato (anche) da DataProvider. <br>
     * Rimanda al metodo base 'fetch' (unico usato) <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB. Obbligatoria.
     * @param filtro      eventuali condizioni di filtro. Se nullo o vuoto recupera tutta la collection.
     *
     * @return lista di entityBeans
     */
    public List<AEntity> fetch(Class<? extends AEntity> entityClazz, AFiltro filtro) {
        Map<String, AFiltro> mappaFiltri = filtro != null ? Collections.singletonMap(filtro.getCriteria().getKey(), filtro) : null;
        return fetch(entityClazz, mappaFiltri);
    }

    /**
     * Crea un set di entities da una collection. Utilizzato (anche) da DataProvider. <br>
     * Rimanda al metodo base 'fetch' (unico usato) <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB. Obbligatoria.
     * @param mappaFiltri eventuali condizioni di filtro. Se nullo o vuoto recupera tutta la collection.
     *
     * @return lista di entityBeans
     */
    public List<AEntity> fetch(Class<? extends AEntity> entityClazz, Map<String, AFiltro> mappaFiltri) {
        return fetch(entityClazz, mappaFiltri, (Sort) null);
    }

    /**
     * Crea un set di entities da una collection. Utilizzato (anche) da DataProvider. <br>
     * Rimanda al metodo base 'fetch' (unico usato) <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB. Obbligatoria.
     * @param mappaFiltri eventuali condizioni di filtro. Se nullo o vuoto recupera tutta la collection.
     * @param sort        eventuali condizioni di ordinamento. Se nullo, cerca quello base della AEntity.
     *
     * @return lista di entityBeans
     */
    public List<AEntity> fetch(Class<? extends AEntity> entityClazz, Map<String, AFiltro> mappaFiltri, Sort sort) {
        return fetch(entityClazz, mappaFiltri, sort, 0, 0);
    }

    /**
     * Crea un set di entities da una collection. Utilizzato SOLO da DataProvider. <br>
     * DataProvider usa QuerySortOrder (Vaadin) mentre invece la Query di MongoDB usa Sort (springframework) <br>
     * Qui effettuo la conversione
     *
     * @param entityClazz    corrispondente ad una collection sul database mongoDB. Obbligatoria.
     * @param mappaFiltri    eventuali condizioni di filtro. Se nullo o vuoto recupera tutta la collection.
     * @param sortVaadinList eventuali condizioni di ordinamento. Se nullo, cerca quello base della AEntity.
     * @param offset         eventuale da cui iniziare. Se zero inizia dal primo bean.
     * @param limit          numero di entityBeans da restituire. Se nullo restituisce tutti quelli esistenti (filtrati).
     *
     * @return lista di entityBeans
     */
    public List<AEntity> fetch(Class<? extends AEntity> entityClazz, Map<String, AFiltro> mappaFiltri, List<QuerySortOrder> sortVaadinList, int offset, int limit) {
        return fetch(entityClazz, mappaFiltri, utility.sortVaadinToSpring(sortVaadinList), offset, limit);
    }

    /**
     * Crea un set di entities da una collection. Utilizzato (anche) da DataProvider. <br>
     * Metodo base 'fetch' (unico usato) <br>
     * <p>
     * For multiple criteria on the same field, uses a “comma” to combine them. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB. Obbligatoria.
     * @param mappaFiltri eventuali condizioni di filtro. Se nullo o vuoto recupera tutta la collection.
     * @param sortSpring        eventuali condizioni di ordinamento. Se nullo, cerca quello base della AEntity.
     * @param offset      eventuale da cui iniziare. Se zero inizia dal primo bean.
     * @param limit       numero di entityBeans da restituire. Se nullo restituisce tutti quelli esistenti (filtrati).
     *
     * @return lista di entityBeans
     *
     * @see(https://mkyong.com/java/due-to-limitations-of-the-basicdbobject-you-cant-add-a-second-and/)
     */
    public List<AEntity> fetch(Class<? extends AEntity> entityClazz, Map<String, AFiltro> mappaFiltri, Sort sortSpring, int offset, int limit) {
        Query query = new Query();
        Criteria criteriaFiltro;
        Criteria criteriaQuery = null;

        if (array.isAllValid(mappaFiltri)) {
            for (AFiltro filtro : mappaFiltri.values()) {
                criteriaFiltro = filtro.getCriteria();

//                if (criteriaQuery == null) {
//                    criteriaQuery = criteriaFiltro;
//                }
//                else {
//                    //--For multiple criteria on the same field, uses a “comma” to combine them.
//                    if (criteriaFiltro.getKey().equals(criteriaQuery.getKey())) {
//                        criteriaQuery.andOperator(criteriaFiltro);//@todo Funzionalità ancora da implementare
//                    }
//                    else {
//                        criteriaQuery.andOperator(criteriaFiltro);//@todo Funzionalità ancora da implementare
//                    }
//                }
                query.addCriteria(filtro.getCriteria());
            }
        }

        if (sortSpring != null) {
            query.with(sortSpring);
        }
        if (offset > 0) {
            query.skip(offset);
        }
        if (limit > 0) {
            query.limit(limit);
        }

        return (List<AEntity>) mongoOp.find(query, entityClazz);
    }

    /**
     * Crea un set di entities da una collection. <br>
     * Utilizzato da DataProvider <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param offset      da cui iniziare
     * @param limit       numero di entityBeans da restituire
     *
     * @return lista di entityBeans
     */
    @Deprecated
    public List<AEntity> findSet(Class<? extends AEntity> entityClazz,
                                 int offset, int limit) {
        return findSet(entityClazz, offset, limit, new BasicDBObject());
    }

    /**
     * Crea un set di entities da una collection. <br>
     * Utilizzato da DataProvider <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param offset      da cui iniziare
     * @param limit       numero di entityBeans da restituire
     *
     * @return lista di entityBeans
     */
    @Deprecated
    public List<AEntity> findSet(Class<? extends AEntity> entityClazz,
                                 int offset, int limit, BasicDBObject sort) {
        return findSet(entityClazz, offset, limit, new BasicDBObject(), sort);
    }

    /**
     * Crea un set di entities da una collection. <br>
     * Utilizzato da DataProvider <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param offset      da cui iniziare
     * @param limit       numero di entityBeans da restituire
     *
     * @return lista di entityBeans
     */
    @Deprecated
    public List<AEntity> findSet(Class<? extends AEntity> entityClazz,
                                 int offset, int limit, BasicDBObject query, BasicDBObject sort) {
        List<AEntity> items = null;
        Gson gSon = new Gson();
        String jsonString;
        String mongoClazzName;
        MongoCollection<Document> collection;
        AEntity entityBean = null;
        List<Field> listaRef;
        boolean esisteTagValue;
        String tag = "\"value\":{\"";
        String tag2;
        String tagEnd = "},";
        int ini = 0;
        int end = 0;
        List<Document> documents;
        query = query != null ? query : new BasicDBObject();

        mongoClazzName = annotation.getCollectionName(entityClazz);
        collection = mongoOp.getCollection(mongoClazzName);
        documents = collection.find(query).sort(sort).skip(offset).limit(limit).into(new ArrayList());

        if (documents.size() > 0) {
            items = new ArrayList<>();
            for (Document doc : documents) {
                esisteTagValue = false;
                tag2 = VUOTA;
                jsonString = gSon.toJson(doc);
                jsonString = jsonString.replaceAll("_id", "id");
                try {
                    entityBean = gSon.fromJson(jsonString, entityClazz);
                } catch (JsonSyntaxException unErrore) {
                    esisteTagValue = jsonString.contains(tag);
                    if (esisteTagValue) {
                        ini = jsonString.indexOf(tag);
                        end = jsonString.indexOf(tagEnd, ini) + tagEnd.length();
                        tag2 = jsonString.substring(ini, end);
                        jsonString = jsonString.replace(tag2, VUOTA);
                        try {
                            entityBean = gSon.fromJson(jsonString, entityClazz);
                        } catch (Exception unErrore2) {
                            logger.error(unErrore, this.getClass(), "findSet");
                        }
                    }
                    else {
                        entityBean = agSonService.crea(doc, entityClazz);

                        //                        if (jsonString.contains("AM")||jsonString.contains("PM")) {
                        //                            logger.error("Non legge la data", this.getClass(), "findSet");
                        //                        }
                        //                        else {
                        //                            logger.error(unErrore, this.getClass(), "findSet");
                        //                        }
                    }
                }

                listaRef = annotation.getDBRefFields(entityClazz);
                if (listaRef != null && listaRef.size() > 0) {
                    entityBean = fixDbRef(doc, gSon, entityBean, listaRef);
                }
                if (esisteTagValue && entityBean.getClass().getSimpleName().equals(Preferenza.class.getSimpleName())) {
                    entityBean = fixPrefValue(doc, gSon, entityBean, tag2);
                    if (((Preferenza) entityBean).value == null) {
                        logger.warn("Valore nullo della preferenza " + ((Preferenza) entityBean).code, this.getClass(), "findSet");
                    }
                }

                if (entityBean != null) {
                    items.add(entityBean);
                }
            }
        }

        return items;
    }

    /**
     * Crea un set di entities da una collection. <br>
     * Utilizzato da DataProvider <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param offset      da cui iniziare
     * @param limit       numero di entityBeans da restituire
     *
     * @return lista di entityBeans
     */
    @Deprecated
    public List<AEntity> findSet2(Class<? extends AEntity> entityClazz,
                                  int offset, int limit, BasicDBObject query, BasicDBObject sort) {
        List<AEntity> items = null;
        Gson gSon = new Gson();
        String jsonString;
        String mongoClazzName;
        MongoCollection<Document> collection;
        AEntity entityBean = null;
        List<Field> listaRef;
        boolean esisteTagValue;
        String tag = "\"value\":{\"";
        String tag2;
        String tagEnd = "},";
        int ini = 0;
        int end = 0;
        List<Document> documents;
        query = query != null ? query : new BasicDBObject();

        mongoClazzName = annotation.getCollectionName(entityClazz);
        collection = mongoOp.getCollection(mongoClazzName);

        documents = collection.find(query).sort(sort).skip(offset).limit(limit).into(new ArrayList());

        if (documents.size() > 0) {
            items = new ArrayList<>();
            for (Document doc : documents) {
                esisteTagValue = false;
                tag2 = VUOTA;
                jsonString = gSon.toJson(doc);
                jsonString = jsonString.replaceAll("_id", "id");
                try {
                    entityBean = gSon.fromJson(jsonString, entityClazz);
                } catch (JsonSyntaxException unErrore) {
                    esisteTagValue = jsonString.contains(tag);
                    if (esisteTagValue) {
                        ini = jsonString.indexOf(tag);
                        end = jsonString.indexOf(tagEnd, ini) + tagEnd.length();
                        tag2 = jsonString.substring(ini, end);
                        jsonString = jsonString.replace(tag2, VUOTA);
                        try {
                            entityBean = gSon.fromJson(jsonString, entityClazz);
                        } catch (Exception unErrore2) {
                            logger.error(unErrore, this.getClass(), "findSet");
                        }
                    }
                    else {
                        entityBean = agSonService.crea(doc, entityClazz);

                        //                        if (jsonString.contains("AM")||jsonString.contains("PM")) {
                        //                            logger.error("Non legge la data", this.getClass(), "findSet");
                        //                        }
                        //                        else {
                        //                            logger.error(unErrore, this.getClass(), "findSet");
                        //                        }
                    }
                }

                listaRef = annotation.getDBRefFields(entityClazz);
                if (listaRef != null && listaRef.size() > 0) {
                    entityBean = fixDbRef(doc, gSon, entityBean, listaRef);
                }
                if (esisteTagValue && entityBean.getClass().getSimpleName().equals(Preferenza.class.getSimpleName())) {
                    entityBean = fixPrefValue(doc, gSon, entityBean, tag2);
                    if (((Preferenza) entityBean).value == null) {
                        logger.warn("Valore nullo della preferenza " + ((Preferenza) entityBean).code, this.getClass(), "findSet");
                    }
                }

                if (entityBean != null) {
                    items.add(entityBean);
                }
            }
        }

        return items;
    }

    /**
     * Aggiunge il valore del campo 'value' di una preferenza. <br>
     *
     * @return entityBean regolato
     */
    private AEntity fixPrefValue(Document doc, Gson gSon, AEntity
            entityBean, String value) {
        int number;
        char c;
        byte[] bytes;
        String riga = text.estraeGraffaSingola(value);
        String valore = riga.substring(riga.lastIndexOf(DUE_PUNTI) + DUE_PUNTI.length());
        valore = valore.trim();
        valore = text.levaCoda(valore, "]");
        valore = text.levaTesta(valore, "[");
        String[] car = valore.split(VIRGOLA);
        String stringa = VUOTA;
        for (String i : car) {
            number = Integer.parseInt(i);
            c = (char) number;
            stringa += Character.toString((char) c);
        }
        bytes = stringa.getBytes();
        ((Preferenza) entityBean).value = bytes;

        return entityBean;
    }

    /**
     * Aggiunge i valori corretti di eventuali campi dbRef. <br>
     *
     * @return entityBean regolato
     */
    private AEntity fixDbRef(Document doc, Gson gSon, AEntity
            entityBean, List<Field> listaRef) {
        JsonElement element = gSon.toJsonTree(doc);
        JsonObject objDoc = element.getAsJsonObject();
        String key = VUOTA;
        JsonElement elementRef = null;
        JsonObject objRef = null;
        JsonElement objID = null;
        String valueID = VUOTA;
        Class clazzRef = null;
        AEntity entityRef = null;

        for (Field field : listaRef) {
            clazzRef = field.getType();
            key = field.getName();
            elementRef = objDoc.get(key);
            if (elementRef == null) {
                break;
            }
            objRef = elementRef.getAsJsonObject();
            objID = objRef.get("id");
            valueID = objID.getAsString();
            entityRef = findById(clazzRef, valueID);
            try {
                field.set(entityBean, entityRef);
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "nomeDelMetodo");
            }
        }

        return entityBean;
    }

    /**
     * Costruzione della entity partendo dal valore della keyID <br>
     *
     * @param entityClazz della AEntity
     * @param valueID     della entityBean
     *
     * @return new entity
     */
    public AEntity crea(final Class entityClazz, final String valueID) {
        return agSonService.crea(entityClazz, valueID);
    }

    /**
     * Cerca una singola entity di una collection con una determinata chiave. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyId       chiave identificativa
     *
     * @return the founded entity
     */
    public AEntity find(Class<? extends AEntity> entityClazz, String keyId) {
        return findById(entityClazz, keyId);
    }

    /**
     * Cerca una singola entity di una collection con una determinata chiave. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyId       chiave identificativa
     *
     * @return the founded entity
     */
    public AEntity findById(Class<? extends AEntity> entityClazz, String keyId) {
        AEntity entity = null;
        if (entityClazz == null) {
            return null;
        }

        if (text.isValid(keyId)) {
            entity = mongoOp.findById(keyId, entityClazz);
        }

        return entity;
    }

    /**
     * Retrieves an entity by its keyProperty.
     *
     * @param entityClazz      corrispondente ad una collection sul database mongoDB
     * @param keyPropertyValue must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public AEntity findByKey(Class<? extends AEntity> entityClazz, String keyPropertyValue) {
        String keyPropertyName = annotation.getKeyPropertyName(entityClazz);
        if (text.isValid(keyPropertyName)) {
            return findOneUnique(entityClazz, keyPropertyName, keyPropertyValue);
        }
        else {
            return findById(entityClazz, keyPropertyValue);
        }
    }

    /**
     * Cerca la entity successiva in una collection con un dato ordine. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyIdValue  chiave ID identificativa
     *
     * @return the founded entity
     */
    public AEntity findNext(final Class<? extends AEntity> entityClazz,
                            final String keyIdValue) {
        return findNext(entityClazz, FIELD_ID, keyIdValue);
    }

    /**
     * Recupera l'ID della entity successiva in una collection con un dato ordine. <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param sortProperty  campo chiave di ordinamento
     * @param valueProperty del campo chiave da cui partire
     *
     * @return the founded entity
     */
    public String findNextID(final Class<? extends AEntity> entityClazz,
                             final String sortProperty, final Object valueProperty) {
        AEntity nextEntity = findBase(entityClazz, sortProperty, valueProperty, Sort.Direction.ASC);
        return nextEntity != null ? nextEntity.id : VUOTA;
    }

    /**
     * Cerca la entity successiva in una collection con un dato ordine. <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param sortProperty  campo chiave di ordinamento
     * @param valueProperty del campo chiave da cui partire
     *
     * @return the founded entity
     */
    public AEntity findNext(final Class<? extends AEntity> entityClazz,
                            final String sortProperty, final Object valueProperty) {
        return findBase(entityClazz, sortProperty, valueProperty, Sort.Direction.ASC);
    }

    /**
     * Cerca la entity precedente in una collection con un dato ordine. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyIdValue  chiave ID identificativa
     *
     * @return the founded entity
     */
    public AEntity findPrevious(
            final Class<? extends AEntity> entityClazz, final String keyIdValue) {
        return findPrevious(entityClazz, FIELD_ID, keyIdValue);
    }

    /**
     * Recupera l'ID della entity precedente in una collection con un dato ordine. <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param sortProperty  campo chiave di ordinamento
     * @param valueProperty del campo chiave da cui partire
     *
     * @return the founded entity
     */
    public String findPreviousID(
            final Class<? extends AEntity> entityClazz, final String sortProperty,
            final Object valueProperty) {
        AEntity previousEntity = findBase(entityClazz, sortProperty, valueProperty, Sort.Direction.DESC);
        return previousEntity != null ? previousEntity.id : VUOTA;
    }

    /**
     * Cerca la entity precedente in una collection con un dato ordine. <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param sortProperty  campo chiave di ordinamento
     * @param valueProperty del campo chiave da cui partire
     *
     * @return the founded entity
     */
    public AEntity findPrevious(
            final Class<? extends AEntity> entityClazz, final String sortProperty,
            final Object valueProperty) {
        return findBase(entityClazz, sortProperty, valueProperty, Sort.Direction.DESC);
    }

    /**
     * Cerca una singola entity di una collection con una determinata chiave. <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param sortProperty  campo chiave di ordinamento
     * @param valueProperty del campo chiave da cui partire
     *
     * @return the founded entity
     */
    private AEntity findBase(final Class<? extends AEntity> entityClazz,
                             final String sortProperty, final Object valueProperty,
                             final Sort.Direction sortDir) {
        AEntity nextEntity = null;
        List<AEntity> listaOrdinata = null;
        Query query = new Query();

        if (valueProperty == null) {
            return null;
        }
        if (entityClazz == null || text.isEmpty(sortProperty)) {
            return null;
        }

        switch (sortDir) {
            case ASC:
                query.addCriteria(Criteria.where(sortProperty).gt(valueProperty));
                break;
            case DESC:
                query.addCriteria(Criteria.where(sortProperty).lt(valueProperty));
                break;
            default:
                logger.warn("Switch - caso non definito", this.getClass(), "findBase");
                break;
        }
        query.with(Sort.by(sortDir, sortProperty));
        query.limit(1);
        listaOrdinata = (List<AEntity>) mongoOp.find(query, entityClazz);

        if (array.isAllValid(listaOrdinata) && listaOrdinata.size() == 1) {
            nextEntity = listaOrdinata.get(0);
        }

        return nextEntity;
    }

    /**
     * Cerca una singola entity di una collection con un determinato valore di una property. <br>
     * Costruisce una query semplice, di uguaglianza del valore per la property indicata <br>
     * Per altre query, costruirle direttamente <br>
     * <p>
     * Return a single document from a collection or view. <br>
     * If multiple documents satisfy the query, this method returns the first document <br>
     * according to the query’s sort order or natural order <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  per costruire la query
     * @param propertyValue (serializable) per costruire la query
     *
     * @return the founded entity
     *
     * @see(https://docs.mongodb.com/realm/mongodb/actions/collection.findOne//)
     */
    public AEntity findOneFirst(Class<? extends
            AEntity> entityClazz, String propertyName, Serializable propertyValue) {
        if (entityClazz == null) {
            return null;
        }
        if (text.isEmpty(propertyName) || propertyValue == null) {
            return null;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(propertyName).is(propertyValue));
        return findOneFirst(entityClazz, query);
    }

    /**
     * Return the first document in the collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return the founded entity
     *
     * @see(https://docs.mongodb.com/realm/mongodb/actions/collection.findOne//)
     */
    public AEntity findOneFirst(Class<? extends AEntity> entityClazz) {
        return findOneFirst(entityClazz, new Query());
    }

    /**
     * Cerca una singola entity di una collection con una query. <br>
     * Return a single document from a collection or view. <br>
     * If multiple documents satisfy the query, this method returns the first document <br>
     * according to the query’s sort order or natural order <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param query       Optional. A standard MongoDB query document that specifies which documents to find.
     *                    Specify an empty query filter ({}) or omit this parameter
     *                    to return the first document in the collection.
     *
     * @return the founded entity
     *
     * @see(https://docs.mongodb.com/realm/mongodb/actions/collection.findOne//)
     */
    public AEntity findOneFirst(Class<? extends
            AEntity> entityClazz, Query query) {
        if (entityClazz == null || query == null) {
            return null;
        }

        return mongoOp.findOne(query, entityClazz);
    }

    /**
     * Cerca una singola entity di una collection con una query. <br>
     * Restituisce un valore valido SOLO se ne esiste una sola <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  per costruire la query
     * @param propertyValue (serializable) per costruire la query
     *
     * @return the founded entity unique
     *
     * @see(https://docs.mongodb.com/realm/mongodb/actions/collection.findOne//)
     */
    public AEntity findOneUnique(Class<? extends
            AEntity> entityClazz, String propertyName, Serializable propertyValue) {
        if (entityClazz == null) {
            return null;
        }
        if (text.isEmpty(propertyName) || propertyValue == null) {
            return null;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(propertyName).is(propertyValue));
        return findOneUnique(entityClazz, query);
    }

    /**
     * Cerca una singola entity di una collection con una query. <br>
     * Restituisce un valore valido SOLO se ne esiste una sola <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param query       A standard MongoDB query document that specifies which documents to find.
     *
     * @return the founded entity unique
     *
     * @see(https://docs.mongodb.com/realm/mongodb/actions/collection.findOne//)
     */
    public AEntity findOneUnique(Class<? extends
            AEntity> entityClazz, Query query) {
        if (entityClazz == null || query == null) {
            return null;
        }

        if (mongoOp.count(query, entityClazz) != 1) {
            return null;
        }

        return mongoOp.findOne(query, entityClazz);
    }

    /**
     * Cerca una singola entity di una collection con una query. <br>
     * Restituisce un valore valido SOLO se ne esiste una sola <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param query       A standard MongoDB query document that specifies which documents to find.
     *
     * @return the founded entity unique
     *
     * @see(https://docs.mongodb.com/realm/mongodb/actions/collection.findOne//)
     */
    public AEntity findByUniqueKey(Class<? extends
            AEntity> entityClazz, Query query) {
        if (entityClazz == null || query == null) {
            return null;
        }

        if (mongoOp.count(query, entityClazz) != 1) {
            return null;
        }

        return mongoOp.findOne(query, entityClazz);
    }

    /**
     * Find lista (interna). <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  da controllare
     * @param propertyValue da controllare
     * @param sort          per individuare il primo
     *
     * @return the founded entity
     */
    private List<AEntity> findLista(Class<? extends
            AEntity> entityClazz, String propertyName, Serializable
                                            propertyValue, Sort sort) {
        List<AEntity> lista = null;
        Query query = new Query();
        query.addCriteria(Criteria.where(propertyName).is(propertyValue));

        if (sort != null) {
            query.with(sort);
        }

        return findAll(entityClazz, query);
    }

    /**
     * Find single entity. <br>
     * Cerca sul database (mongo) la versione registrata di una entity in memoria <br>
     *
     * @param entityBean to be found
     *
     * @return the founded entity
     */
    public AEntity find(AEntity entityBean) {
        return entityBean != null ? findById(entityBean.getClass(), entityBean.getId()) : null;
    }

    /**
     * Check the existence of a single entity. <br>
     * Cerca sul database (mongo) la versione registrata di una entity in memoria <br>
     *
     * @param entityBean to be found
     *
     * @return true if exist
     */
    public boolean isEsiste(AEntity entityBean) {
        return find(entityBean) != null;
    }

    /**
     * Check the existence of a single entity. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyId       chiave identificativa
     *
     * @return true if exist
     */
    public boolean isEsiste(Class<? extends AEntity> entityClazz, String
            keyId) {
        return findById(entityClazz, keyId) != null;
    }

    /**
     * Check the existence of a single entity. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyId       chiave identificativa
     *
     * @return true if exist
     */
    public boolean isNotEsiste(Class<? extends
            AEntity> entityClazz, String keyId) {
        return !isEsiste(entityClazz, keyId);
    }

    /**
     * Registra una nuova entity. <br>
     * Controlla che sia rispettata l'unicità del campo keyID e di tutte le property definite come uniche <br>
     * <p>
     * Inserts a document or documents into a collection. <br>
     * If the document does not specify an _id field, then MongoDB will add the _id field <br>
     * and assign a unique ObjectId for the document before inserting <br>
     * If the document contains an _id field, the _id value must be unique within the collection <br>
     * to avoid duplicate key error. <br>
     * <p>
     * Per registrare la entity, occorre che oltre al field _id
     * sia rispettata l'unicità degli altri (eventuali) fields unici <br>
     *
     * @param newEntityBean da inserire nella collezione
     *
     * @return la entity appena registrata
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.insert/)
     */
    public AEntity insert(AEntity newEntityBean) {
        AEntity entityBean = null;

        try {
            entityBean = mongoOp.insert(newEntityBean);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "insert");
        }

        return entityBean;
    }

    /**
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @param entityClazz  corrispondente ad una collection sul database mongoDB
     * @param propertyName dell'ordinamento
     */
    public int getNewOrder(Class<? extends AEntity> entityClazz, String propertyName) {
        int ordine = 0;
        AEntity entityBean;
        Object value;
        Sort sort = Sort.by(Sort.Direction.DESC, propertyName);
        Field field = reflection.getField(entityClazz, propertyName);
        Query query = new Query().with(sort).limit(1);

        if (isEmpty(entityClazz)) {
            return 1;
        }

        try {
            entityBean = mongoOp.find(query, entityClazz).get(0);
            value = field.get(entityBean);
            ordine = (Integer) value;
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "getNewOrder");
        }

        return ordine + 1;
    }

    /**
     * Saves a given entity. <br>
     * Use the returned instance for further operations <br>
     * as the save operation might have changed the entity instance completely <br>
     * If the document does not contain an _id field, then the save() method calls the insert() method.
     * During the operation, the mongo shell will create an ObjectId and assign it to the _id field.
     * If the document contains an _id field, then the save() method is equivalent to an update
     * with the upsert option set to true and the query predicate on the _id field.
     *
     * @param entityBean to be saved
     *
     * @return the saved entity
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.save/)
     */
    public AEntity save(AEntity entityBean) {
        try {
            return mongoOp.save(entityBean);
        } catch (Exception unErrore) {
            logger.warn(unErrore, this.getClass(), "save");
            return null;
        }
    }

    /**
     * Delete a single entity. <br>
     *
     * @param entityBean da cancellare
     *
     * @return true se la entity esisteva ed è stata cancellata
     */
    public boolean delete(Class<? extends AEntity> entityClazz, Query
            query) {
        boolean status = false;
        DeleteResult result = null;

        result = mongoOp.remove(query, entityClazz);
        if (result != null) {
            status = result.getDeletedCount() == 1;
        }

        return status;
    }

    /**
     * Delete a single entity. <br>
     *
     * @param entityBean da cancellare
     *
     * @return true se la entity esisteva ed è stata cancellata
     */
    public boolean delete(AEntity entityBean) {
        boolean status = false;
        DeleteResult result = null;

        if (entityBean != null && entityBean.id != null) {
            try {
                result = mongoOp.remove(entityBean);
                if (result != null) {
                    status = result.getDeletedCount() == 1;
                }
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "delete");
            }
        }
        else {
            logger.warn("Tentativo di cancellare una entity nulla o con id nullo", this.getClass(), "delete");
        }

        return status;
    }

    /**
     * Delete a list of entities.
     *
     * @param listaEntities di elementi da cancellare
     * @param clazz         della collezione
     *
     * @return lista
     */
    public DeleteResult delete(List<? extends
            AEntity> listaEntities, Class<? extends AEntity> clazz) {
        ArrayList<String> listaId = new ArrayList<String>();

        for (AEntity entity : listaEntities) {
            if (entity != null) {
                listaId.add(entity.id);
            }
            else {
                logger.error("Algos - Manca una entity in AMongoService.delete()");
            }
        }

        return deleteBulk(listaId, clazz);
    }

    /**
     * Delete a list of entities.
     *
     * @param listaId di keyID da cancellare
     * @param clazz   della collezione
     *
     * @return lista
     */
    public DeleteResult deleteBulk(List<String> listaId, Class<? extends
            AEntity> clazz) {
        Bson condition = new Document("$in", listaId);
        Bson filter = new Document("_id", condition);
        return getCollection(clazz).deleteMany(filter);
    }

    /**
     * Delete a collection.
     *
     * @param collectionName della collezione
     */
    public boolean drop(String collectionName) {
        this.mongoOp.dropCollection(collectionName);
        return mongo.count(collectionName) == 0;
    }

    /**
     * Delete a collection.
     *
     * @param entityClazz della collezione
     *
     * @return true se la collection è stata cancellata
     */
    public boolean drop(Class<? extends AEntity> entityClazz) {
        return drop(annotation.getCollectionName(entityClazz));
    }

    /**
     * Restituisce un generico database
     */
    public MongoDatabase getDB(String databaseName) {
        //        MongoClient mongoClient = new MongoClient("localhost", 27017);

        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + databaseName);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(mongoClientSettings);

        return mongoClient.getDatabase(databaseName);
    }

    /**
     * Restituisce il database 'admin' di servizio, sempre presente in MongoDB
     */
    public MongoDatabase getDBAdmin() {
        return getDB("admin");
    }

    /**
     * Recupera il valore di un parametro
     *
     * @param parameterName
     *
     * @return value of parameter
     */
    public Object getParameter(String parameterName) {
        Map<String, Object> mappa;
        MongoDatabase dbAdmin = getDBAdmin();
        Document param;

        mappa = new LinkedHashMap<>();
        mappa.put("getParameter", 1);
        mappa.put(parameterName, 1);

        param = dbAdmin.runCommand(new Document(mappa));

        return param.get(parameterName);
    }

    /**
     * Regola il valore di un parametro
     *
     * @param parameterName
     * @param valueToSet    valore da regolare
     *
     * @return true se il valore è stato acquisito dal database
     */
    public boolean setParameter(String parameterName, Object valueToSet) {
        Map<String, Object> mappa;
        MongoDatabase dbAdmin = getDBAdmin();
        Document param;

        mappa = new LinkedHashMap<>();
        mappa.put("setParameter", 1);
        mappa.put(parameterName, valueToSet);

        param = dbAdmin.runCommand(new Document(mappa));
        return (double) param.get("ok") == 1;
    }

    /**
     * Regola il valore del parametro internalQueryExecMaxBlockingSortBytes
     *
     * @param maxBytes da regolare
     *
     * @return true se il valore è stato acquisito dal database
     */
    public boolean setMaxBlockingSortBytes(int maxBytes) {
        return setParameter("internalQueryExecMaxBlockingSortBytes", maxBytes);
    }

    /**
     * Recupera il valore del parametro internalQueryExecMaxBlockingSortBytes
     *
     * @return value of parameter
     */
    public int getMaxBlockingSortBytes() {
        int numBytes;
        String value = "";

        numBytes = (int) getParameter("internalQueryExecMaxBlockingSortBytes");
        value = text.format(numBytes);

        if (numBytes == AMongoService.STANDARD_MONGO_MAX_BYTES) {
            logger.warn("Algos - mongoDB. La variabile internalQueryExecMaxBlockingSortBytes è regolata col valore standard iniziale settato da mongoDB: " + value);
        }
        else {
            if (numBytes == AMongoService.EXPECTED_ALGOS_MAX_BYTES) {
                logger.info("Algos - mongoDB. La variabile internalQueryExecMaxBlockingSortBytes è regolata col valore richiesto da Algos: " + value);
            }
            else {
                logger.warn("Algos - mongoDB. La variabile internalQueryExecMaxBlockingSortBytes è regolata a cazzo: " + value);
            }
        }

        return numBytes;
    }

    /**
     * Regola il valore iniziale del parametro internalQueryExecMaxBlockingSortBytes
     */
    public void fixMaxBytes() {
        this.setMaxBlockingSortBytes(EXPECTED_ALGOS_MAX_BYTES);
    }

}