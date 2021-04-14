package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 28-mag-2018
 * Time: 08:37
 * Simple layer around Checkbox <br>
 * Banale, ma serve per avere tutti i fields omogenei <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ACheckBox extends AField<Boolean> {

    private Checkbox innerField;


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(ACheckBox.class, caption) <br>
     *
     * @param caption label visibile del field
     */
    public ACheckBox(String caption) {
        this(VUOTA, caption);
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(ACheckBox.class, fieldKey, caption) <br>
     *
     * @param fieldKey nome interno del field
     * @param caption  label visibile del field
     */
    public ACheckBox(String fieldKey, String caption) {
        super.fieldKey = fieldKey;
        super.caption = caption;
        innerField = new Checkbox(caption);
        add(innerField);
    } // end of SpringBoot constructor


    @Override
    public Checkbox getBinder() {
        return innerField;
    }

}
