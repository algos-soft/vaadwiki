package it.algos.vaadflow14.backend.annotation;


import static it.algos.vaadflow14.backend.application.FlowCost.*;

import java.lang.annotation.*;


/**
 * /**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: lun, 27-apr-2020
 * Time: 14:55
 * <p>
 * Annotation per le Entity Class <br>
 * Lista dei fields automatici nella Grid delle liste <br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //--Class, interface (including annotation type), or enum declaration
public @interface AIList {


    /**
     * (Optional) Alert sopra la lista
     * Di default VUOTA
     *
     * @return the string
     */
    String headerAlert() default VUOTA;
    /**
     * (Optional) Title della lista
     * Di default VUOTA
     *
     * @return the string
     */
    String title() default VUOTA;

    /**
     * (Optional) prima colonna indice a sinistra della grid
     * Di default false
     *
     * @return the status
     */
    boolean usaRowIndex() default false;

    /**
     * (Optional) ri-creazione automatica della lista
     * Di default false
     *
     * @return the status
     */
    boolean usaReset() default false;

    /**
     * (Optional) The width of the index.
     * Expressed in double, to be converted in String ending with "em"
     * Defaults to 2.5
     *
     * @return the int
     */
    double rowIndexWidthEM() default 2.5;


    /**
     * (Optional) List of visible fields on Grid
     * Presentati in successione e separati da virgola
     * Vengono poi convertiti in una List
     * Defaults to all.
     *
     * @return the string commas separate
     */
    String fields() default VUOTA;


    /**
     * (Optional) The width of the ID columnService.
     * Defaults to 290
     *
     * @return the int
     */
    int widthID() default 290;


}
