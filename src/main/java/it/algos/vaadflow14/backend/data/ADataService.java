package it.algos.vaadflow14.backend.data;

import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 25-ott-2018
 * Time: 20:18
 */
@Service
@Slf4j
public abstract class ADataService {

    //--il modello-dati specifico viene regolato dalla sottoclasse nel costruttore
    public Class<? extends AEntity> entityClass;


    /**
     * Nome della collezione su mongoDB <br>
     * Viene regolato dalla sottoclasse nel costruttore <br>
     */
    protected String collectionName;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AnnotationService annotation;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ArrayService array;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected TextService text;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected AIMongoService mongo;

    /**
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    protected ADataService() {
    }// end of Spring constructor


    @PostConstruct
    protected void postConstruct() {
        this.collectionName = annotation.getCollectionName(entityClass);
    }// end of constructor


    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    public int count() {
        return ((MongoService) mongo).count(entityClass);//@todo da controllare
    }// end of method


    /**
     * Metodo invocato da ABoot (o da una sua sottoclasse) <br>
     * <p>
     * Creazione di una collezione - Solo se non ci sono records
     */
    public void loadData() {
        int numRec = this.count();

        if (numRec == 0) {
            numRec = creaAll();
            log.warn("Algos - Data. La collezione " + collectionName + " è stata creata: " + numRec + " schede");
        }
        else {
            log.info("Algos - Data. La collezione " + collectionName + " è già presente: " + numRec + " schede");
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione della collezione
     */
    protected int creaAll() {
        return 0;
    }// end of method

}// end of class
