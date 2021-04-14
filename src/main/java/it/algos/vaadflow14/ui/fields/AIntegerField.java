package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mer, 10-giu-2020
 * Time: 18:36
 * Simple layer around IntegerField <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AIntegerField extends AField<Integer> {

    private  IntegerField integerField;



    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(AIntegerField.class) <br>
     *
     */
    public AIntegerField() {
        integerField = new IntegerField();
        integerField.setAutoselect(true);
        integerField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        add(integerField);
    } // end of SpringBoot constructor



    @Override
    protected Integer generateModelValue() {
        return integerField.getValue();
    }


    @Override
    protected void setPresentationValue(Integer value) {
        integerField.setValue(value);
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
    public int compareTo(Integer o) {
        return integerField.getValue().compareTo(o);
    }

    @Override
    public void setWidth(String width) {
        integerField.setWidth(width);
    }

}
