package it.algos.vaadflow14.backend.service;

import com.mongodb.client.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.bson.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;

import java.io.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 02-set-2021
 * Time: 06:49
 */
public interface AIMongoService {

    int STANDARD_MONGO_MAX_BYTES = 33554432;

    int EXPECTED_ALGOS_MAX_BYTES = 50151432;

    /**
     * Nome del database. <br>
     *
     * @return nome del database
     */
    String getDatabaseName();

    /**
     * Database. <br>
     *
     * @return database
     */
    MongoDatabase getDataBase();

    /**
     * Tutte le collezioni esistenti. <br>
     *
     * @return collezioni del database
     */
    List<String> getCollezioni();


    /**
     * Collection del database. <br>
     *
     * @param entityClazz della collezione da controllare
     *
     * @return collection if exist
     */
    MongoCollection<Document> getCollection(final Class<? extends AEntity> entityClazz);


    /**
     * Check the existence of a collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection exist
     */
    boolean isExistsCollection(final Class<? extends AEntity> entityClazz) throws AlgosException;

    /**
     * Check the existence of a collection. <br>
     *
     * @param collectionName corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection exist
     */
    boolean isExistsCollection(final String collectionName) throws AlgosException;

    /**
     * Check the existence (not empty) of a collection. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true if the collection has entities
     */
    boolean isValidCollection(final Class<? extends AEntity> entityClazz) throws AlgosException;

    //    /**
    //     * Check if a collection is empty. <br>
    //     *
    //     * @param entityClazz corrispondente ad una collection sul database mongoDB
    //     *
    //     * @return true if the collection is empty
    //     */
    //    boolean isEmptyCollection(final Class<? extends AEntity> entityClazz) throws AlgosException;


    /**
     * Conteggio di TUTTE le entities di una collection NON filtrate. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return numero totale di entities
     */
    int count(final Class<? extends AEntity> entityClazz) throws AlgosException;


    /**
     * Conteggio di alcune entities selezionate di una collection. <br>
     * La selezione è keyID=keyValue <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyValue    (serializable) per costruire la query
     *
     * @return numero di entities selezionate
     */
    int count(final Class<? extends AEntity> entityClazz, final Serializable keyValue) throws AlgosException;

    /**
     * Conteggio di alcune entities selezionate di una collection. <br>
     * La selezione è propertyName=propertyValue <br>
     * Se la propertyName o la propertyValue non sono valide, restituisce TUTTE le entities della collezione <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  per costruire la query
     * @param propertyValue (serializable) per costruire la query
     *
     * @return numero di entities selezionate
     */
    int count(final Class<? extends AEntity> entityClazz, final String propertyName, final Serializable propertyValue) throws AlgosException;


    /**
     * Conteggio di alcune entities di una collection filtrate con una mappa di filtri. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB. Obbligatoria.
     * @param wrapFiltri  eventuali condizioni di filtro. Se nullo o vuoto recupera tutta la collection.
     *
     * @return numero di entities eventualmente filtrate
     */
    int count(final Class<? extends AEntity> entityClazz, final WrapFiltri wrapFiltri) throws AlgosException;

    int count(final Class<? extends AEntity> entityClazz, final Map<String, AFiltro> mappaFiltri) throws AlgosException;


    /**
     * Conta tutte le entities con la property rest=true. <br>
     * Controlla che esista la collezione <br>
     * Controlla che esista la property 'reset' <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     *
     * @return true se non ci sono properties con reset=true
     */
    int countReset(Class<? extends AEntity> entityClazz) throws AlgosException;

    //    /**
    //     * Controlla che NON ci siano entities con la property rest=true. <br>
    //     * Controlla che esista la collezione <br>
    //     * Controlla che esista la property 'reset' <br>
    //     *
    //     * @param entityClazz corrispondente ad una collection sul database mongoDB
    //     *
    //     * @return true se non ci sono properties con reset=true
    //     */
    //    boolean isResetVuoto(Class<? extends AEntity> entityClazz) throws AlgosException;

    //    /**
    //     * Costruzione della entity partendo dal valore della keyID <br>
    //     *
    //     * @param entityClazz della AEntity
    //     * @param valueID     della entityBean
    //     *
    //     * @return new entity
    //     */
    //    AEntity crea(final Class entityClazz, final String valueID) throws AlgosException;

    /**
     * Crea un set di entities da una collection. Utilizzato (anche) da DataProvider. <br>
     * <p>
     * Se la propertyName non esiste, restituisce il valore zero <br>
     * Se la propertyValue non esiste, restituisce il valore zero <br>
     * Se la propertyValue esiste, restituisce il numero di entities che soddisfano le condizioni (che può anche essere zero) <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  per costruire la query
     * @param propertyValue (serializable) per costruire la query
     *
     * @return numero di entities eventualmente filtrate (se esiste la propertyName)
     */
    List<? extends AEntity> fetch(final Class<? extends AEntity> entityClazz, final String propertyName, final Serializable propertyValue) throws AlgosException;

    List<AEntity> fetch(Class<? extends AEntity> entityClazz, Map<String, AFiltro> mappaFiltri, Sort sortSpring, int offset, int limit) throws AlgosException;

    /**
     * Cerca un Document da una collection con una determinata chiave. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyId       chiave identificativa
     *
     * @return the founded document
     */
    Document findDocById(final Class<? extends AEntity> entityClazz, final Serializable keyId) throws AlgosException;


    /**
     * Cerca un Document da una collection con una determinata chiave. <br>
     *
     * @param collectionName The name of the collection or view
     * @param keyId          chiave identificativa
     *
     * @return the founded document
     */
    Document findDocById(final String collectionName, final Serializable keyId) throws AlgosException;


    /**
     * Cerca un Document da una collection con una determinata chiave. <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  per costruire la condition
     * @param propertyValue (serializable) per costruire la condition
     *
     * @return the founded document
     */
    Document findDocByProperty(final Class<? extends AEntity> entityClazz, final String propertyName, final Serializable propertyValue) throws AlgosException;

    /**
     * Crea una singola entity da un document. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param doc         recuperato da mongoDB
     *
     * @return the entity
     */
    AEntity creaByDoc(final Class<? extends AEntity> entityClazz, final Document doc) throws AlgosException;


    /**
     * Check the existence of a single entity. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB
     * @param keyId       chiave identificativa
     *
     * @return true if exist
     */
    boolean isEsiste(final Class<? extends AEntity> entityClazz, final Serializable keyId) throws AlgosException;

    /**
     * Find single entity. <br>
     * Cerca sul database (mongo) la versione registrata di una entity in memoria <br>
     *
     * @param entityBeanToBeFound on mongoDb
     *
     * @return the founded entity
     */
    AEntity find(final AEntity entityBeanToBeFound) throws AlgosException;


    /**
     * Cerca una singola entity di una collection con una determinata chiave. <br>
     *
     * @param entityClazz      corrispondente ad una collection sul database mongoDB
     * @param keyPropertyValue must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws AMongoException if {@keyPropertyValue id} is {@literal null}
     */
    AEntity find(final Class<? extends AEntity> entityClazz, final Serializable keyPropertyValue) throws AlgosException;


    /**
     * Retrieves an entity by a keyProperty.
     * Cerca una singola entity di una collection con una query. <br>
     * Restituisce un valore valido SOLO se ne esiste una sola <br>
     *
     * @param entityClazz   corrispondente ad una collection sul database mongoDB
     * @param propertyName  per costruire la query
     * @param propertyValue must not be {@literal null}
     *
     * @return the founded entity unique or {@literal null} if none found
     *
     * @see(https://docs.mongodb.com/realm/mongodb/actions/collection.findOne//)
     */
    AEntity find(final Class<? extends AEntity> entityClazz, final String propertyName, final Serializable propertyValue) throws AlgosException;


    /**
     * Crea un set di entities da una collection. Utilizzato (anche) da DataProvider. <br>
     * Rimanda al metodo base 'fetch' (unico usato) <br>
     *
     * @param entityClazz corrispondente a una collection sul database mongoDB. Obbligatoria.
     *
     * @return lista di entityBeans
     */
    List<? extends AEntity> fetch(Class<? extends AEntity> entityClazz) throws AlgosException;

    /**
     * Crea un set di entities da una collection. Utilizzato (anche) da DataProvider. <br>
     * Rimanda al metodo base 'fetch' (unico usato) <br>
     *
     * @param entityClazz corrispondente a una collection sul database mongoDB. Obbligatoria.
     * @param wrapFiltri  insieme di filtri.
     *
     * @return lista di entityBeans
     */
    List<? extends AEntity> fetch(Class<? extends AEntity> entityClazz, final WrapFiltri wrapFiltri) throws AlgosException;

    /**
     * Crea un set di entities da una collection. Utilizzato (anche) da DataProvider. <br>
     * Metodo base 'fetch' (unico usato) <br>
     * <p>
     * For multiple criteria on the same field, uses a “comma” to combine them. <br>
     *
     * @param entityClazz corrispondente ad una collection sul database mongoDB. Obbligatoria.
     * @param wrapFiltri  insieme di filtri ed ordinamento.
     * @param offset      eventuale da cui iniziare. Se zero inizia dal primo bean.
     * @param limit       numero di entityBeans da restituire. Se nullo restituisce tutti quelli esistenti (filtrati).
     *
     * @return lista di entityBeans
     */
    List<AEntity> fetch(final Class<? extends AEntity> entityClazz, final WrapFiltri wrapFiltri, final int offset, final int limit) throws AlgosException;

    /**
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @param entityClazz  corrispondente ad una collection sul dat
     *                     abase mongoDB
     * @param propertyName dell'ordinamento
     */
    int getNewOrder(Class<? extends AEntity> entityClazz, String propertyName) throws AMongoException;

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
    AEntity save(AEntity entityBean) throws AlgosException;


    /**
     * Delete a single entity. <br>
     *
     * @return true se la entity esisteva ed è stata cancellata
     */
    boolean delete(Class<? extends AEntity> entityClazz, Query query);

}// end of interface

