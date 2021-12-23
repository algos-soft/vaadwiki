package it.algos.vaadflow14.backend.annotation;

import com.vaadin.flow.component.icon.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;

import java.lang.annotation.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: lun, 27-apr-2020
 * Time: 14:55
 * <p>
 * Annotation per i fields (property) delle Entity Class <br>
 * <p>
 * Annotation to add some property for a single columnService of the Grid <br>
 * Alcune property sono in comune con AIField <br>
 * Se nell'annotation AIColumn manca una property , <br>
 * si prende il valore della corrispondente property di AIField <br>
 * (se esiste, altrimenti il valore di default) <br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //--Field declaration (includes enum constants)
public @interface AIColumn {


    /**
     * (Required) The type of the field.
     * Se manca (valore di default), prende quello indicato in AIField
     * Se manca anche in AIField, prende il valore di default di AIField
     *
     * @return the field type
     */
    AETypeField type() default AETypeField.ugualeAlForm;


    /**
     * (Optional) The name of the column header.
     * Defaults to the property or field name.
     *
     * @return the string
     */
    String header() default VUOTA;


    /**
     * (Optional) The width of the column.
     * Expressed in double, to be converted in String ending with "em"
     * Defaults to 0.
     *
     * @return the int
     */
    double widthEM() default 0;


    /**
     * (Optional) color of the component
     * Defaults to blue.
     *
     * @return the string
     */
    String color() default "blue";


    /**
     * (Optional) column that expand the maximum
     * Defaults to false.
     *
     * @return the boolean
     */
    boolean flexGrow() default false;

    /**
     * (Optional) column sortable
     * Defaults to false.
     *
     * @return the boolean
     */
    boolean sortable() default false;

    /**
     * (Optional) header icon
     * Defaults to false.
     *
     * @return the vaadin icon
     */
    VaadinIcon headerIcon() default VaadinIcon.YOUTUBE;

    /**
     * (Optional) header icon size
     * Defaults to 20.
     *
     * @return the int
     */
    int headerIconSizePX() default 20;

    /**
     * (Optional) header icon color
     * Defaults to blue.
     *
     * @return the string
     */
    String headerIconColor() default "blue";


    /**
     * (Optional) method name for reflection
     * Defaults to empty.
     *
     * @return the string
     */
    String methodName() default VUOTA;

    /**
     * (Optional) The type of the boolean type range.
     * Defaults to the checkBox type.
     *
     * @return the field type
     */
    AETypeBoolCol typeBool() default AETypeBoolCol.checkIcon;

    /**
     * (Optional) The two strings for boolean type.
     * Defaults to vuota.
     *
     * @return the strings
     */
    String boolEnum() default VUOTA;

    /**
     * The type of the data type
     * Defaults to standard type.
     *
     * @return the strings
     */
    AETypeData typeData() default AETypeData.standard;

}
