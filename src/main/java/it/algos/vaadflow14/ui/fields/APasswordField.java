package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.service.ALogService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 20-ago-2020
 * Time: 08:23
 * Simple layer around PasswordField <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class APasswordField extends AField<String> {

    private final PasswordField passwordField;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(APasswordField.class) <br>
     */
    public APasswordField() {
        passwordField = new PasswordField();
        add(passwordField);
    } // end of SpringBoot constructor


    @Override
    protected String generateModelValue() {
        return passwordField.getValue();
    }


    @Override
    protected void setPresentationValue(String value) {
        passwordField.setValue(value);
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
    public int compareTo(String o) {
        return passwordField.getValue().compareTo(o);
    }

    @Override
    public void setWidth(String width) {
        passwordField.setWidth(width);
    }


    @Override
    public void setErrorMessage(String errorMessage) {
        passwordField.setErrorMessage(errorMessage);
        ALogService.messageError(errorMessage);//@todo Creare una preferenza e sostituirla qui
    }

}
