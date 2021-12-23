package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.datetimepicker.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.time.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 29-ago-2020
 * Time: 17:07
 * Simple layer around DateTimePicker <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ADateTimeField extends AField<LocalDateTime> {

    private static final Duration STEP = Duration.ofMinutes(15);

    private final DateTimePicker dateTimePicker;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(ADateTimeField.class) <br>
     */
    public ADateTimeField() {
        dateTimePicker = new DateTimePicker();
        dateTimePicker.setStep(STEP);
        add(dateTimePicker);
    } // end of SpringBoot constructor


    @Override
    protected LocalDateTime generateModelValue() {
        return dateTimePicker.getValue();
    }


    @Override
    protected void setPresentationValue(LocalDateTime newPresentationValue) {
        dateTimePicker.setValue(newPresentationValue);
    }

    @Override
    public void setWidth(String width) {
        dateTimePicker.setWidth(width);
    }


    @Override
    public void setErrorMessage(String errorMessage) {
        dateTimePicker.setErrorMessage(errorMessage);
        ALogService.messageError(errorMessage);//@todo Creare una preferenza e sostituirla qui
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener valueChangeListener) {
        return dateTimePicker.addValueChangeListener(valueChangeListener);
    }

}

