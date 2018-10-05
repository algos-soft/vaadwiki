package it.algos.vaadflow.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.entity.AEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: mar, 21-ago-2018
 * Time: 16:04
 * Diversamente dagli altri service, questo è un SINGLETON <br>
 * Può essere utilizzato PRIMA della chiamata al browser e PRIMA della UI <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AMongoService {

    /**
     * Inietta da Spring
     */
    @Autowired
    public MongoOperations mongo;


    /**
     * Default constructor
     */
    public AMongoService() {
    }// end of constructor


    /**
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
//    public AMongoService(MongoRepository repository, MongoOperations mongo) {
//        this.repository = repository;
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
    public void insert(Object item, String collection) {
        mongo.insert(item, collection);
    }// end of method


    /**
     * Count all
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public int count(Class clazz) {
        return (int) mongo.count(new Query(), clazz);
    }// end of single test

    /**
     * Find all
     *
     * @param clazz della collezione
     *
     * @return lista
     */
    public List findAll(Class clazz) {
        return mongo.findAll(clazz);
    }// end of single test


    /**
     * Inserts multiple documents into a collection.
     *
     * @param lista di elementi da inserire
     * @param clazz della collezione
     *
     * @return lista
     */
    public void insertMany(Collection lista, Class clazz) {
         mongo.insert(lista, clazz);
    }// end of single test

    /**
     * Delete from a collection.
     *
     * @param lista di elementi da inserire
     * @param clazz della collezione
     *
     * @return lista
     */
    public void deleteWhere(Class clazz, String property, String value) {
        Query searchQuery = new Query(Criteria.where(property).is(value));
        mongo.remove(searchQuery, clazz);
    }// end of single test


}// end of class
