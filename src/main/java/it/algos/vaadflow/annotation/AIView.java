package it.algos.vaadflow.annotation;

import it.algos.vaadflow.modules.role.EARoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 19-dic-2017
 * Time: 11:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class and interface.
public @interface AIView {

    /**
     * (Optional) Visibilità a secondo del ruolo dell'User collegato
     * Defaults to user.
     */
    EARoleType roleTypeVisibility() default EARoleType.user;

}// end of interface annotation
