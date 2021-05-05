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
 * Annotation per le Entity Class <br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //--Class, interface (including annotation type), or enum declaration
public @interface AIEntity {

    /**
     * (Optional) Label del record
     * Usato nel dialog come Edit...recordName oppure Modifica...recordName
     * Di default usa il 'name' della collection mongoDB @Document
     *
     * @return the string
     */
    String recordName() default VUOTA;

    /**
     * (Optional) key property unica
     * Di default usa la property 'id' della collection mongoDB
     *
     * @return the string
     */
    String keyPropertyName() default VUOTA;

    /**
     * (Optional) il campo della superclasse
     * Di default false
     *
     * @return the status
     */
    boolean usaCompany() default true;

    /**
     * (Optional) il campo della superclasse
     * Di default false
     *
     * @return the status
     */
    boolean usaNote() default false;


    /**
     * (Optional) possibilità di creazione di una nuova entity
     * Di default true
     *
     * @return the status
     */
    boolean usaCreazione() default true;

    /**
     * (Optional) possibilità di modifica di una entity
     * Di default true
     *
     * @return the status
     */
    boolean usaModifica() default true;

    /**
     * (Optional) uso di timestamps per memorizzare date di creazione e di modifica della entity
     * Di default false
     *
     * @return the status
     */
    boolean usaTimeStamp() default false;

    /**
     * (Optional) possibilità di cancellare una entity
     * Di default true
     *
     * @return the status
     */
    boolean usaDelete() default true;

    /**
     * (Optional) possibilità di resettare i data alla partenza
     * Di default false
     *
     * @return the status
     */
    boolean usaResetIniziale() default false;


}