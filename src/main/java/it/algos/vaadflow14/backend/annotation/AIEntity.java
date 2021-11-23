package it.algos.vaadflow14.backend.annotation;


import static it.algos.vaadflow14.backend.application.FlowCost.*;

import java.lang.annotation.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: lun, 27-apr-2020
 * Time: 14:55
 * <p>
 * Annotation Algos per le Entity Class <br>
 * Controlla le property ed il comportamento della List e della Grid. <br>
 * <p>
 * Regola:
 * recordName()=VUOTA -> (Optional) Nome visibile della singola scheda. Se manca usa il nome della collezione.
 * keyPropertyName()=VUOTA -> (Mandatory) Field property unica nella collezione.
 * usaReset()=false -> (Optional) Flag per la creazione automatica della lista
 * usaBoot()=false -> (Optional) Creazione automatica dei dati alla partenza se la collezione è vuota
 * usaNew()=true -> (Optional) Possibilità di creazione di una nuova entity
 * usaCompany()=false -> (Optional) Utilizza il field della superclasse
 * usaNote()=false -> (Optional) Utilizza il field della superclasse
 * usaTimeStamp()=false -> (Optional) Utilizza timestamps per memorizzare date di creazione e di modifica della entity
 * <p>
 * Standard:
 * AIEntity(recordName = "Xxx", keyPropertyName = "code", usaReset = true, usaBoot = true, usaNew = true)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//--Class, interface (including annotation type), or enum declaration
public @interface AIEntity {

    /**
     * (Optional) Nome visibile della singola scheda.
     * Usato nel dialog come Edit...recordName oppure Modifica...recordName
     * Di default usa il 'name' della collection mongoDB @Document
     *
     * @return the string
     */
    String recordName() default VUOTA;


    /**
     * (Mandatory) key property unica
     * Di default usa la property 'id' della collection mongoDB
     *
     * @return the string
     */
    String keyPropertyName() default FIELD_ID;


    /**
     * (Optional) chiave keyId creata tutta minuscola e case-insensitive
     * Di default false
     *
     * @return the status
     */
    boolean usaKeyIdMinuscolaCaseInsensitive() default false;

    /**
     * (Optional) Creazione automatica dei dati alla partenza se collezione vuota
     * Ha senso solo se esiste il metodo xxxService.reset()
     * Di default false
     *
     * @return the status
     */
    boolean usaBoot() default false;


    /**
     * (Optional) Possibilità di creazione di una nuova entity
     * Di default true
     *
     * @return the status
     */
    boolean usaNew() default true;


    /**
     * (Optional) Utilizza il field della superclasse
     * Di default false
     *
     * @return the status
     */
    boolean usaCompany() default false;

    /**
     * (Optional) Utilizza il field della superclasse
     * Di default false
     *
     * @return the status
     */
    boolean usaNote() default false;

    /**
     * (Optional) Utilizza timestamps per memorizzare date di creazione e di modifica della entity
     * Di default false
     *
     * @return the status
     */
    boolean usaTimeStamp() default false;


}