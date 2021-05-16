package it.algos.vaadflow14.backend.annotation;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;

import java.lang.annotation.*;


/**
 * /**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: lun, 27-apr-2020
 * Time: 14:55
 * <p>
 * Annotation Algos per le Entity Class <br>
 * Controlla le property ed il comportamento del Form. <br>
 * <p>
 * Regola:
 * fields()="all fields" -> (Mandatory) List of visible fields on Form
 * operationForm()=AEOperation.edit -> (Optional) Tipologia di operazioni ammesse sul Form
 * widthID()=16 -> (Optional) The width of the ID field
 * usaSpostamentoTraSchede()=false -> (Optional) Flag per l'utilizzo delle frecce di spostamento tra scheda precedente e successiva
 * <p>
 * Standard:
 * AIForm(fields = "code,descrizione", usaSpostamentoTraSchede = true)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//--Class, interface (including annotation type), or enum declaration
public @interface AIForm {


    /**
     * (Mandatory) List of visible fields on Form
     * Presentati in successione e separati da virgola
     * Vengono poi convertiti in una List
     * Defaults to all.
     *
     * @return the string commas separate
     */
    String fields() default VUOTA;

    /**
     * (Optional) tipologia di operazioni ammesse sul Form
     * Di default AEOperation.edit
     *
     * @return the status
     */
    AEOperation operationForm() default AEOperation.edit;

    /**
     * (Optional) The width of the ID field.
     * Expressed in int, to be converted in String ending with "em"
     * Defaults to "16em".
     *
     * @return the int
     */
    int widthIDEM() default 16;

    /**
     * (Optional) Flag per l'utilizzo delle frecce di spostamento tra scheda precedente e successiva
     * Di default false
     *
     * @return the status
     */
    boolean usaSpostamentoTraSchede() default false;


}
