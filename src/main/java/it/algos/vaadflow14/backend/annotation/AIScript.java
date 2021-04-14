package it.algos.vaadflow14.backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * /**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: lun, 27-apr-2020
 * Time: 12:12
 * <p>
 * Annotation per controllare il flusso degli scripts di creazione del framework.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE) //--Class, interface (including annotation type), or enum declaration
public @interface AIScript {


    /**
     * (Optional) Status of this file.
     * Defaults to false.
     *
     * @return the boolean
     */
    boolean sovraScrivibile() default false;


}
