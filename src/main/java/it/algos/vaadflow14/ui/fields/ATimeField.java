package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.timepicker.*;
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
 * Time: 17:08
 * Simple layer around TimePicker <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ATimeField extends AField<LocalTime> {

    private final Duration STEP = Duration.ofMinutes(15);

    private TimePicker timePicker;


    /**
     * Costruttore senza parametri <br>
     * L' istanza viene costruita con appContext.getBean(ATimeField.class) <br>
     *
     */
    public ATimeField() {
        try {
            timePicker = new TimePicker();
            timePicker.setStep(STEP);
            add(timePicker);
        } catch (Exception unErrore) {
        }
    } // end of SpringBoot constructor


    @Override
    protected LocalTime generateModelValue() {
        return timePicker.getValue();
    }


    @Override
    protected void setPresentationValue(LocalTime newPresentationValue) {
        timePicker.setValue(newPresentationValue);
    }

    @Override
    public void setWidth(String width) {
        timePicker.setWidth(width);
    }


    @Override
    public void setErrorMessage(String errorMessage) {
        timePicker.setErrorMessage(errorMessage);
        ALogService.messageError(errorMessage);//@todo Creare una preferenza e sostituirla qui
    }

}
