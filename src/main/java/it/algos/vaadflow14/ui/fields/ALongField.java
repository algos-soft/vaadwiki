package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 09-giu-2021
 * Time: 19:17
 * Simple layer around NumberField <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ALongField extends AField<Long> {


    private TextField longField;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(AIntegerField.class) <br>
     */
    public ALongField() {
        longField = new TextField();
        longField.setAutoselect(true);
        longField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        add(longField);
    } // end of SpringBoot constructor


    @Override
    protected Long generateModelValue() {
        return Long.valueOf(longField.getValue());
    }


    @Override
    protected void setPresentationValue(Long value) {
        longField.setValue(value.toString());
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    public int compareTo(Long o) {
        return Long.valueOf(longField.getValue()).compareTo(o);
    }

    @Override
    public void setWidth(String width) {
        longField.setWidth(width);
    }

}
