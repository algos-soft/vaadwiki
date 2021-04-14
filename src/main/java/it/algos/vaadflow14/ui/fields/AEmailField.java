package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.service.ALogService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 16-ago-2020
 * Time: 17:44
 * Simple layer around EmailField <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AEmailField extends AField<String> {

    private final EmailField emailField;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(ATextField.class) <br>
     */
    public AEmailField() {
        emailField = new EmailField();
        emailField.setAutoselect(true);
        emailField.setClearButtonVisible(true);

        add(emailField);
    } // end of SpringBoot constructor


    @Override
    protected String generateModelValue() {
        return emailField.getValue();
    }


    @Override
    protected void setPresentationValue(String value) {
        emailField.setValue(value);
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
        return emailField.getValue().compareTo(o);
    }

    @Override
    public void setWidth(String width) {
        emailField.setWidth(width);
    }


    @Override
    public void setErrorMessage(String errorMessage) {
        emailField.setErrorMessage(errorMessage);
        ALogService.messageError(errorMessage);//@todo Creare una preferenza e sostituirla qui
    }


}
