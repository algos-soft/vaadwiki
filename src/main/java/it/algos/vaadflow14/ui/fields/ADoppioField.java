package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 10-feb-2022
 * Time: 17:14
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ADoppioField extends AField<Double> {


    private TextField doppioField;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(AIntegerField.class) <br>
     */
    public ADoppioField() {
        doppioField = new TextField();
        doppioField.setAutoselect(true);
        doppioField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        add(doppioField);
    } // end of SpringBoot constructor


    @Override
    protected Double generateModelValue() {
        return Double.valueOf(doppioField.getValue());
    }


    @Override
    protected void setPresentationValue(Double value) {
        doppioField.setValue(value.toString());
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
    public int compareTo(Double o) {
        return Double.valueOf(doppioField.getValue()).compareTo(o);
    }

    @Override
    public void setWidth(String width) {
        doppioField.setWidth(width);
    }

}
