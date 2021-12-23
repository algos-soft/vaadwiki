package it.algos.vaadflow14.ui.validator;

import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.ui.fields.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 10-set-2020
 * Time: 12:11
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ANotNullValidator implements Validator {

    private static final long serialVersionUID = 1L;

    private String message = VUOTA;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public ANotNullValidator() {
    } // end of SpringBoot constructor


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(ANotNullValidator.class, message) <br>
     *
     * @param message da mostrare
     */
    public ANotNullValidator(String message) {
        this.message = message;
    } // end of SpringBoot constructor


    @Override
    public ValidationResult apply(Object obj, ValueContext valueContext) {
        Optional optional=valueContext.getComponent();
        AComboField comboField=(AComboField)optional.get();
        Object value= comboField.comboBox.getValue();
        if (obj == null) {
            if (message.equals("")) {
                return ValidationResult.error("Non può essere nullo");
            } else {
                return ValidationResult.error(message);
            }
        } else {
            return ValidationResult.ok();
        }
    }


    /**
     * Applies this function to the given arguments.
     *
     * @param o  the first function argument
     * @param o2 the second function argument
     *
     * @return the function result
     */
    @Override
    public Object apply(Object o, Object o2) {
        return null;
    }

}
