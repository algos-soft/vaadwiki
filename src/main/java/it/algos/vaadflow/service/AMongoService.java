package it.algos.vaadflow.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import it.algos.vaadflow.backend.entity.AEntity;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.service.AService.FIELD_NAME_CODE;
import static it.algos.vaadflow.service.AService.FIELD_NAME_ORDINE;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mar, 21-ago-2018
 * Time: 16:04
 * <p>
 * Gestione degli accessi al database MongoDB <br>
 * Classe di libreria; NON deve essere astratta, altrimenti Spring non la costruisce <br>
 * Implementa il 'pattern' SINGLETON; l'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AMongoService.class); <br>
 * 2) AMongoService.getInstance(); <br>
 * 3) @Autowired private AMongoService mongoService; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, basta il 'pattern') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 */
@Service
@Slf4j
public class AMongoService extends AbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Private final property
     */
    private static final AMongoService INSTANCE = new AMongoService();

    /**
     * Inietta da Spring
     */
    @Autowired
    public MongoOperations mongoOp;
//    @Autowired
//    public MongoTemplate template;


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private AMongoService() {
    }// end of constructor

    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AMongoService getInstance() {
        return INSTANCE;
    }// end of static method


//    /**
//     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
//     * Si usa un @Qualifier(), per avere la sottoclasse specifica
//     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
//     */
//    public AMongoService(MongoTemplate template, MongoOperations mongo) {
//        this.template = template;
//        this.mongo = mongo;
//    }// end of Spring constructor


//    /**
//     * Returns the number of entities available.
//     *
//     * @return the number of entities
//     */
//    public int count() {
//        return (int) repository.count();
//    }// end of method

//    //save user object into "user" collection / table
//    //class name will be used as collection name
//	mongoOperation.save(user);
//
//    //save user object into "tableA" collection
//	mongoOperation.save(user,"tableA");
//
//    //insert user object into "user" collection
//    //class name will be used as collection name
//	mongoOperation.insert(user);
//
//    //insert user object into "tableA" collection
//	mongoOperation.insert(user, "tableA");
//
//    //insert a list of user objects
//	mongoOperation.insert(listofUser);

    /**
     * Count all
     *
     * @param clazz della collezione
     *
     * @return num recs
     */
    public int count(Class<? extends AEntity> clazz) {
        return (int) mongoOp.count(new Query(), clazz);
    }// end of method


    /**
     * Find single entity
     *
     * @param clazz      della collezione
     * @param entityBean da cercare
     *
     * @return entity
     */
    public AEntity findByEntity(Class<? extends AEntity> clazz, AEntity entityBean) {
        return findById(clazz, entityBean.getId());
    }// end of method


    /**
     * Find single entity
     *
     * @param clazz della collezione
     * @param keyId chiave id
     *
     * @return entity
     */
    public AEntity findById(Class<? extends AEntity> clazz, String keyId) {
        AEntity entity = null;
        Object obj = null;

        if (text.isValid(keyId)) {
            obj = mongoOp.findById(keyId, clazz);
        }// end of if cycle

        if (obj != null && obj instanceof AEntity) {
            entity = (AEntity) obj;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Find single entity
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     *
     * @return entity
     */
    public AEntity findByProperty(Class<? extends AEntity> clazz, String property, Object value) {
        AEntity entity = null;
        Object lista;

        if (reflection.isNotEsiste(clazz, property)) {
            log.error("Algos - Manca la property " + property + " nella classe " + clazz.getSimpleName());
            return null;
        }// end of if cycle

        Query searchQuery = new Query(Criteria.where(property).is(value));
        lista = mongoOp.find(searchQuery, clazz);

        if (lista != null && ((List) lista).size() == 1) {
            entity = (AEntity) ((List) lista).get(0);
        }// end of if cycle

        return entity;
    }// end of method

    /**
     * Controlla l'esistenza della singola entity
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     *
     * @return true se esiste già
     */
    public boolean isEsiste(Class<? extends AEntity> clazz, String property, Object value) {
        return findByProperty(clazz, property, value) != null;
    }// end of method

    /**
     * Controlla l'esistenza della singola entity
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     *
     * @return true se manca
     */
    public boolean isManca(Class<? extends AEntity> clazz, String property, Object value) {
        return findByProperty(clazz, property, value) == null;
    }// end of method

    /**
     * Find all
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public List findAll(Class<? extends AEntity> clazz) {
        return mongoOp.findAll(clazz);
    }// end of method


    /**
     * Insert single document into a collection.
     *
     * @param item  da inserire
     * @param clazz della collezione
     */
    public boolean insert(AEntity item, Class<? extends AEntity> clazz) {
        boolean status = false;
        String collectionName = annotation.getCollectionName(clazz);

        if (findByEntity(clazz, item) == null) {
            try { // prova ad eseguire il codice
                mongoOp.insert(item, collectionName);
                status = true;
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Insert single document into a collection.
     *
     * @param item           da inserire
     * @param collectionName della collezione
     */
    @Deprecated
    public boolean insert(AEntity item, String collectionName) {
        boolean status = false;

        try { // prova ad eseguire il codice
            mongoOp.insert(item, collectionName);
            status = true;
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return status;
    }// end of method


    /**
     * Inserts multiple documents into a collection.
     *
     * @param lista di elementi da inserire
     * @param clazz della collezione
     */
    public void insert(List<? extends AEntity> lista, Class<? extends AEntity> clazz) {
        mongoOp.insert(lista, clazz);
    }// end of method


    /**
     * Update single document into a collection.
     *
     * @param entityBean da modificare
     * @param clazz      della collezione
     */
    public boolean update(AEntity entityBean, Class<? extends AEntity> clazz) {
        boolean status = false;
        AEntity entity = null;
        entity = findByEntity(clazz, entityBean);

        if (entity != null) {
            mongoOp.remove(entity);
        }// end of if cycle

        return insert(entityBean, clazz);
    }// end of method


    /**
     * Update multiple documents of a collection.
     * Spazzola la lista e cancella/insert ogni singola entity
     *
     * @param listaEntities di elementi da modificare
     * @param clazz         della collezione
     */
    public boolean updateBulk(List<? extends AEntity> listaEntities, Class<? extends AEntity> clazz) {
        boolean status = false;
        DeleteResult result;

        try { // prova ad eseguire il codice
            result = delete(listaEntities, clazz);
            insert(listaEntities, clazz);
            status = true;
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return status;
    }// end of method


    /**
     * Delete a single entity.
     *
     * @param entityBean da cancellare
     *
     * @return lista
     */
    public DeleteResult delete(AEntity entityBean) {
        return mongoOp.remove(entityBean);
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaEntities di elementi da cancellare
     * @param clazz         della collezione
     *
     * @return lista
     */
    public DeleteResult delete(List<? extends AEntity> listaEntities, Class<? extends AEntity> clazz) {
        List<String> listaId = new ArrayList<String>();

        for (AEntity entity : listaEntities) {
            if (entity != null) {
                listaId.add(entity.id);
            } else {
                log.error("Algos - Manca una entity in AMongoService.delete()");
            }// end of if/else cycle
        }// end of for cycle

        return deleteBulk(listaId, clazz);
    }// end of method


    /**
     * Delete a list of entities.
     *
     * @param listaId di keyID da cancellare
     * @param clazz   della collezione
     *
     * @return lista
     */
    public DeleteResult deleteBulk(List<String> listaId, Class<? extends AEntity> clazz) {
        Bson condition = new Document("$in", listaId);
        Bson filter = new Document("_id", condition);
        return getCollection(clazz).deleteMany(filter);
    }// end of method


    /**
     * Delete a collection.
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public void drop(Class<? extends AEntity> clazz) {
        this.drop(getCollectionName(clazz));
    }// end of method


    /**
     * Delete a collection.
     *
     * @param collectionName della collezione
     *
     * @return lista
     */
    public void drop(String collectionName) {
        this.mongoOp.dropCollection(collectionName);
    }// end of method


    /**
     * Delete from a collection.
     *
     * @param clazz    della collezione
     * @param property da controllare
     * @param value    da considerare
     */
    public DeleteResult deleteByProperty(Class<? extends AEntity> clazz, String property, String value) {
        Query searchQuery = new Query(Criteria.where(property).is(value));
        return this.mongoOp.remove(searchQuery, clazz);
    }// end of method


    /**
     * Nome (minuscolo) della collezione.
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public String getCollectionName(Class<? extends AEntity> clazz) {
        String collectionName = "";

        if (clazz != null) {
            collectionName = annotation.getCollectionName(clazz);
        }// end of if cycle

        return collectionName;
    }// end of method


    /**
     * Collezione
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public MongoCollection<Document> getCollection(Class<? extends AEntity> clazz) {
        return mongoOp.getCollection(getCollectionName(clazz));
    }// end of method


    /**
     * Collezione
     *
     * @param collectionName della collezione
     *
     * @return lista
     */
    public MongoCollection<Document> getCollection(String collectionName) {
        return mongoOp.getCollection(collectionName);
    }// end of method


    /**
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @param clazz della collezione
     */
    public int getNewOrdine(Class<? extends AEntity> clazz) {
        return getNewOrdine(clazz, FIELD_NAME_ORDINE);
    }// end of method


    /**
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @param clazz        della collezione
     * @param propertyName dell'ordinamento
     */
    public int getNewOrdine(Class<? extends AEntity> clazz, String propertyName) {
        int ordine = 0;
        AEntity entityBean = null;
        Sort sort = new Sort(Sort.Direction.DESC, propertyName);
        List lista;
        Field field;
        Object value;

        if (!reflection.isEsiste(clazz, propertyName)) {
            return 0;
        }// end of if/else cycle

        Query query = new Query().with(sort).limit(1);
        lista = mongoOp.find(query, clazz);

        if (array.isValid(lista) && lista.size() == 1) {
            entityBean = (AEntity) lista.get(0);
        }// end of if cycle

        if (entityBean != null) {
            field = reflection.getField(clazz, propertyName);
            try { // prova ad eseguire il codice
                value = field.get(entityBean);
                if (value instanceof Integer) {
                    ordine = (Integer) value;
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return ordine + 1;
    }// end of method

}// end of class
