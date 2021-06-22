package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.ui.wrapper.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 14-giu-2021
 * Time: 13:44
 * Simple layer for a series of text fields <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AMappaField extends AField<Map<String, String>> {

    private VerticalLayout layout;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(AIntegerField.class) <br>
     */
    public AMappaField() {
        layout = new AVerticalLayout();
        add(layout);
    } // end of SpringBoot constructor


    @Override
    protected Map<String, String> generateModelValue() {
        //        return Long.valueOf(longField.getValue());
        return null;
    }


    @Override
    protected void setPresentationValue(Map<String, String> value) {
        TextField field;
        layout.removeAll();

        if (value != null) {
            for (String key : value.keySet()) {
                field = new TextField(key, value.get(key));
                layout.add(field);
            }

        }
    }

    //    /**
    //     * Compares this object with the specified object for order.  Returns a
    //     * negative integer, zero, or a positive integer as this object is less
    //     * than, equal to, or greater than the specified object.
    //     *
    //     * @param o the object to be compared.
    //     *
    //     * @return a negative integer, zero, or a positive integer as this object
    //     * is less than, equal to, or greater than the specified object.
    //     *
    //     * @throws NullPointerException if the specified object is null
    //     * @throws ClassCastException   if the specified object's type prevents it
    //     *                              from being compared to this object.
    //     */
    //    public int compareTo(Long o) {
    //        return Long.valueOf(longField.getValue()).compareTo(o);
    //    }

    //    @Override
    //    public void setWidth(String width) {
    //        longField.setWidth(width);
    //    }


}
