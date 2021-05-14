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
 * Annotation Algos per le Entity Class <br>
 * <p>
 * Regola:
 * title()=VUOTA -> Titolo della lista se diverso da @AIEntity.recordName
 * usaDeleteMenu()=false -> Flag per la cancellazione completa della lista dal bottone del menu
 * usaResetMenu()=false -> Flag per la creazione automatica della lista dal bottone del menu
 * spanHeader()=VUOTA -> Singolo span di alert sopra la lista
 * usaRowIndex()=false -> Mostra la prima colonna indice a sinistra della grid
 * rowIndexWidthEM()=2.5 -> The width of the index
 * fields()="all fields" -> List of visible fields on Grid
 * widthID()=250 -> The width of the ID columnService
 * <p>
 * Standard:
 * AIList(fields = "code,descrizione", usaRowIndex = true)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//--Class, interface (including annotation type), or enum declaration
public @interface AIList {


    /**
     * (Optional) Titolo della lista se diverso da @AIEntity.recordName
     * Di default VUOTA
     *
     * @return the string
     */
    String title() default VUOTA;


    /**
     * (Optional) Flag per la cancellazione completa della lista dal bottone del menu
     * Di default false
     *
     * @return the status
     */
    boolean usaDeleteMenu() default false;


    /**
     * (Optional) Flag per la creazione automatica della lista dal bottone del menu
     * Di default false
     *
     * @return the status
     */
    boolean usaResetMenu() default false;


    /**
     * (Optional) Singolo span di alert sopra la lista
     * Di default VUOTA
     *
     * @return the string
     */
    String spanHeader() default VUOTA;


    /**
     * (Optional) Mostra la prima colonna indice a sinistra della grid
     * Di default false
     *
     * @return the status
     */
    boolean usaRowIndex() default false;


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
