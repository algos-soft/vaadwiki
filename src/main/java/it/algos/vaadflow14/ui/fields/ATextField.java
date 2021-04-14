package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.HasValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.service.ALogService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mer, 10-giu-2020
 * Time: 17:30
 * Simple layer around TextField <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ATextField extends AField<String>  {


    public final TextField textField;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(ATextField.class) <br>
     */
    public ATextField() {
        this(VUOTA);
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(ATextField.class, caption) <br>
     */
    public ATextField(String caption) {
        textField = new TextField(caption);
        textField.setAutoselect(true);
        add(textField);
    } // end of SpringBoot constructor


    @Override
    protected String generateModelValue() {
        return textField.getValue();
    }


    @Override
    protected void setPresentationValue(String value) {
        textField.setValue(value);
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
        return textField.getValue().compareTo(o);
    }


    @Override
    public void setWidth(String width) {
        textField.setWidth(width);
    }


    @Override
    public void setErrorMessage(String errorMessage) {
        textField.setErrorMessage(errorMessage);
        ALogService.messageError(errorMessage);//@todo Creare una preferenza e sostituirla qui
    }



}
